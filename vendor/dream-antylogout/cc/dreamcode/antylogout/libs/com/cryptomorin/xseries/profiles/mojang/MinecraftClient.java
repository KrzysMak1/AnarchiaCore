package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang;

import java.io.InputStream;
import com.google.common.io.CharStreams;
import com.google.common.base.Charsets;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.Streams;
import java.io.Reader;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.net.SocketException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.MojangAPIException;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.exceptions.MojangAPIRetryException;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.ProfileLogger;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.util.function.BiFunction;
import java.net.HttpURLConnection;
import org.bukkit.Bukkit;
import java.time.Duration;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.ProfilesCore;
import java.net.URISyntaxException;
import org.jetbrains.annotations.Nullable;
import java.net.URI;
import cc.dreamcode.antylogout.libs.com.google.gson.Gson;
import java.net.Proxy;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class MinecraftClient
{
    private static final AtomicInteger SESSION_ID;
    private static final Proxy PROXY;
    private static final Gson GSON;
    private static final RateLimiter TOTAL_REQUESTS;
    private static final String USER_AGENT;
    private final String method;
    private final URI baseURL;
    private final RateLimiter rateLimiter;
    
    public Session session(@Nullable final ProfileRequestConfiguration config) {
        final Session session = new Session();
        if (config != null) {
            config.configure(session);
        }
        return session;
    }
    
    public MinecraftClient(final String method, final String baseURL, final RateLimiter rateLimiter) {
        this.method = method;
        try {
            this.baseURL = new URI(baseURL);
        }
        catch (final URISyntaxException e) {
            throw new IllegalArgumentException("Invalid Minecraft API URL: " + baseURL, (Throwable)e);
        }
        this.rateLimiter = rateLimiter;
    }
    
    private static String totalReq() {
        return " (total: " + MinecraftClient.TOTAL_REQUESTS.getEffectiveRequestsCount() + ')';
    }
    
    static {
        SESSION_ID = new AtomicInteger();
        PROXY = ((ProfilesCore.PROXY == null) ? Proxy.NO_PROXY : ProfilesCore.PROXY);
        GSON = new Gson();
        TOTAL_REQUESTS = new RateLimiter(Integer.MAX_VALUE, Duration.ofMinutes(10L));
        USER_AGENT = "XSeries/13.3.2 (" + System.getProperty("os.name") + "; " + System.getProperty("os.version") + "; " + System.getProperty("java.vendor") + "; " + System.getProperty("java.version") + ") " + Bukkit.getName() + '/' + Bukkit.getBukkitVersion() + ' ' + Bukkit.getVersion();
    }
    
    public final class Session
    {
        private final int sessionId;
        private Duration connectTimeout;
        private Duration readTimeout;
        private Duration retryDelay;
        private int retries;
        private boolean waitInQueue;
        private Object body;
        private String append;
        private HttpURLConnection connection;
        private BiFunction<Session, Throwable, Boolean> errorHandler;
        
        public Session() {
            this.sessionId = MinecraftClient.SESSION_ID.getAndIncrement();
            this.connectTimeout = Duration.ofSeconds(10L);
            this.readTimeout = Duration.ofSeconds(10L);
            this.retryDelay = Duration.ofSeconds(5L);
            this.waitInQueue = true;
        }
        
        private void debug(final String message, final Object... vars) {
            final Object[] variables = XReflection.concatenate(new Object[] { this.sessionId, this.append }, vars);
            ProfileLogger.debug("[MinecraftClient-{}][{}] " + message, variables);
        }
        
        public Session exceptionally(final BiFunction<Session, Throwable, Boolean> errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }
        
        public Session waitInQueue(final boolean wait) {
            this.waitInQueue = wait;
            return this;
        }
        
        public Session retry(final int retries, final Duration delay) {
            this.retries = retries;
            this.retryDelay = delay;
            return this;
        }
        
        public Session body(final Object body) {
            this.validateMethod("POST");
            this.body = Objects.requireNonNull(body);
            return this;
        }
        
        public Session append(@NotNull final String append) {
            this.append = (String)Objects.requireNonNull((Object)append);
            return this;
        }
        
        public Session timeout(final Duration connectTimeout, final Duration readTimeout) {
            this.connectTimeout = connectTimeout;
            this.readTimeout = readTimeout;
            return this;
        }
        
        private void validateMethod(final String method) {
            if (!MinecraftClient.this.method.equals((Object)method)) {
                throw new UnsupportedOperationException("Cannot " + method + " with a client using method " + (Object)MinecraftClient.this);
            }
        }
        
        @Nullable
        public JsonElement request() throws IOException, MojangAPIException {
            try {
                final JsonElement response = this.request0();
                this.debug("Received response: {}", response);
                return (response == null) ? null : (response.isJsonNull() ? null : response);
            }
            catch (final Exception ex) {
                if (this.retries > 0) {
                    --this.retries;
                    if (ex instanceof MojangAPIRetryException) {
                        if (((MojangAPIRetryException)ex).getReason() == MojangAPIRetryException.Reason.RATELIMITED) {
                            return this.request();
                        }
                    }
                    try {
                        Thread.sleep(this.retryDelay.toMillis());
                    }
                    catch (final InterruptedException e) {
                        throw new IllegalStateException("Mojang API retry thread was interrupted unexpectedly", (Throwable)e);
                    }
                    return this.request();
                }
                if (this.errorHandler == null) {
                    throw ex;
                }
                final Boolean shouldRetry = (Boolean)this.errorHandler.apply((Object)this, (Object)ex);
                if (shouldRetry == null || shouldRetry) {
                    return this.request();
                }
                throw ex;
            }
        }
        
        @Nullable
        private JsonElement request0() throws IOException, MojangAPIException {
            if (this.waitInQueue) {
                MinecraftClient.this.rateLimiter.acquireOrWait();
            }
            else if (!MinecraftClient.this.rateLimiter.acquire()) {
                throw new MojangAPIRetryException(MojangAPIRetryException.Reason.RATELIMITED, "Rate limit has been hit! " + (Object)MinecraftClient.this.rateLimiter + totalReq());
            }
            (this.connection = (HttpURLConnection)((this.append == null) ? MinecraftClient.this.baseURL : MinecraftClient.this.baseURL.resolve(this.append)).toURL().openConnection(MinecraftClient.PROXY)).setRequestMethod(MinecraftClient.this.method);
            this.connection.setConnectTimeout((int)this.connectTimeout.toMillis());
            this.connection.setReadTimeout((int)this.readTimeout.toMillis());
            this.connection.setDoInput(true);
            this.connection.setUseCaches(false);
            this.connection.setAllowUserInteraction(false);
            this.connection.setRequestProperty("User-Agent", MinecraftClient.USER_AGENT);
            if (this.body != null) {
                this.connection.setDoOutput(true);
                final String stringBody = MinecraftClient.GSON.toJson(this.body);
                this.debug("Writing body {} to {}", stringBody, this.connection.getURL());
                final byte[] bodyBytes = stringBody.getBytes(StandardCharsets.UTF_8);
                this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                this.connection.setRequestProperty("Content-Length", String.valueOf(bodyBytes.length));
                try (final OutputStream outputStream = this.connection.getOutputStream()) {
                    outputStream.write(bodyBytes);
                }
            }
            else {
                this.connection.setDoOutput(false);
            }
            return this.request00();
        }
        
        @Nullable
        private JsonElement request00() {
            this.debug("Sending request to {}", this.connection.getURL());
            try {
                return this.connectionStreamToJson(false);
            }
            catch (final Throwable ex) {
                MojangAPIException exception = null;
                try {
                    switch (this.connection.getResponseCode()) {
                        case 404: {
                            return null;
                        }
                        case 429: {
                            final String rateLimitBefore = MinecraftClient.this.rateLimiter.toString();
                            MinecraftClient.this.rateLimiter.instantRateLimit();
                            throw new MojangAPIRetryException(MojangAPIRetryException.Reason.RATELIMITED, "Rate limit has been hit (server confirmed): " + rateLimitBefore + " -> " + rateLimitBefore + totalReq());
                        }
                        default: {
                            if (ex instanceof SocketException && ex.getMessage().toLowerCase(Locale.ENGLISH).contains((CharSequence)"connection reset")) {
                                throw new MojangAPIRetryException(MojangAPIRetryException.Reason.CONNECTION_RESET, "Connection was closed", ex);
                            }
                            final JsonElement errorJson = this.connectionStreamToJson(true);
                            exception = new MojangAPIException((errorJson == null) ? "[NO ERROR RESPONSE]" : errorJson.toString(), ex);
                            break;
                        }
                    }
                }
                catch (final MojangAPIRetryException rethrowEx) {
                    throw rethrowEx;
                }
                catch (final Throwable errorEx) {
                    exception = new MojangAPIException("Failed to read both normal response and error response from '" + (Object)this.connection.getURL() + '\'');
                    exception.addSuppressed(ex);
                    exception.addSuppressed(errorEx);
                }
                throw exception;
            }
        }
        
        private JsonElement connectionStreamToJson(final boolean error) throws IOException, IllegalStateException {
            try (final InputStream inputStream = error ? this.connection.getErrorStream() : this.connection.getInputStream()) {
                if (error && inputStream == null) {
                    final JsonElement jsonElement = null;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    return jsonElement;
                }
                try (final JsonReader reader = new JsonReader((Reader)new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    Exception ex = null;
                    JsonElement json;
                    try {
                        json = Streams.parse(reader);
                    }
                    catch (final Exception e) {
                        ex = e;
                        json = null;
                    }
                    if (json == null) {
                        final String rawResponse = CharStreams.toString((Readable)new InputStreamReader(error ? this.connection.getErrorStream() : this.connection.getInputStream(), Charsets.UTF_8));
                        throw new IllegalStateException((error ? "error response" : "normal response") + " is not a JSON object '" + this.connection.getResponseCode() + " - " + this.connection.getResponseMessage() + "': " + rawResponse, (Throwable)ex);
                    }
                    return json;
                }
            }
        }
    }
}
