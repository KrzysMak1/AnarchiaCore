package cc.dreamcode.antylogout.libs.eu.okaeri.configs.json.gson;

import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.postprocessor.ConfigPostprocessor;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.ConfigDeclaration;
import java.io.InputStream;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesContext;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.FieldDeclaration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import java.util.LinkedHashMap;
import cc.dreamcode.antylogout.libs.com.google.gson.GsonBuilder;
import cc.dreamcode.antylogout.libs.com.google.gson.Gson;
import java.util.Map;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.configurer.Configurer;

public class JsonGsonConfigurer extends Configurer
{
    private Map<String, Object> map;
    private Gson gson;
    
    public JsonGsonConfigurer() {
        this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        this.map = (Map<String, Object>)new LinkedHashMap();
    }
    
    public JsonGsonConfigurer(@NonNull final Gson gson) {
        this(gson, (Map<String, Object>)new LinkedHashMap());
        if (gson == null) {
            throw new NullPointerException("gson is marked non-null but is null");
        }
    }
    
    public JsonGsonConfigurer(@NonNull final Gson gson, @NonNull final Map<String, Object> map) {
        if (gson == null) {
            throw new NullPointerException("gson is marked non-null but is null");
        }
        if (map == null) {
            throw new NullPointerException("map is marked non-null but is null");
        }
        this.gson = gson;
        this.map = map;
    }
    
    @Override
    public List<String> getExtensions() {
        return (List<String>)Collections.singletonList((Object)"json");
    }
    
    @Override
    public void setValue(@NonNull final String key, final Object value, final GenericsDeclaration type, final FieldDeclaration field) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        final Object simplified = this.simplify(value, type, SerdesContext.of(this, field), true);
        this.map.put((Object)key, simplified);
    }
    
    @Override
    public void setValueUnsafe(@NonNull final String key, final Object value) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        this.map.put((Object)key, value);
    }
    
    @Override
    public Object getValue(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.map.get((Object)key);
    }
    
    @Override
    public Object remove(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.map.remove((Object)key);
    }
    
    @Override
    public boolean keyExists(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.map.containsKey((Object)key);
    }
    
    @Override
    public List<String> getAllKeys() {
        return (List<String>)Collections.unmodifiableList((List)new ArrayList((Collection)this.map.keySet()));
    }
    
    @Override
    public void load(@NonNull final InputStream inputStream, @NonNull final ConfigDeclaration declaration) throws Exception {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is marked non-null but is null");
        }
        if (declaration == null) {
            throw new NullPointerException("declaration is marked non-null but is null");
        }
        final String data = ConfigPostprocessor.of(inputStream).getContext();
        this.map = (Map<String, Object>)this.gson.fromJson(data, Map.class);
        if (this.map != null) {
            return;
        }
        this.map = (Map<String, Object>)new LinkedHashMap();
    }
    
    @Override
    public void write(@NonNull final OutputStream outputStream, @NonNull final ConfigDeclaration declaration) throws Exception {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is marked non-null but is null");
        }
        if (declaration == null) {
            throw new NullPointerException("declaration is marked non-null but is null");
        }
        final OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        this.gson.toJson(this.map, (Appendable)writer);
        writer.flush();
    }
}
