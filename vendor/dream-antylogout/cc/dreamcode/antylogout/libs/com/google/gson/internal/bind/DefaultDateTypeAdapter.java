package cc.dreamcode.antylogout.libs.com.google.gson.internal.bind;

import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapterFactory;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonSyntaxException;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParsePosition;
import java.text.ParseException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonToken;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonWriter;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.PreJava9DateFormatProvider;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.JavaVersion;
import java.text.SimpleDateFormat;
import java.util.Locale;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.$Gson$Preconditions;
import java.util.ArrayList;
import java.text.DateFormat;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapter;
import java.util.Date;

public final class DefaultDateTypeAdapter<T extends Date> extends TypeAdapter<T>
{
    private static final String SIMPLE_NAME = "DefaultDateTypeAdapter";
    private final DateType<T> dateType;
    private final List<DateFormat> dateFormats;
    
    private DefaultDateTypeAdapter(final DateType<T> dateType, final String datePattern) {
        this.dateFormats = (List<DateFormat>)new ArrayList();
        this.dateType = $Gson$Preconditions.checkNotNull(dateType);
        this.dateFormats.add((Object)new SimpleDateFormat(datePattern, Locale.US));
        if (!Locale.getDefault().equals((Object)Locale.US)) {
            this.dateFormats.add((Object)new SimpleDateFormat(datePattern));
        }
    }
    
    private DefaultDateTypeAdapter(final DateType<T> dateType, final int style) {
        this.dateFormats = (List<DateFormat>)new ArrayList();
        this.dateType = $Gson$Preconditions.checkNotNull(dateType);
        this.dateFormats.add((Object)DateFormat.getDateInstance(style, Locale.US));
        if (!Locale.getDefault().equals((Object)Locale.US)) {
            this.dateFormats.add((Object)DateFormat.getDateInstance(style));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add((Object)PreJava9DateFormatProvider.getUSDateFormat(style));
        }
    }
    
    private DefaultDateTypeAdapter(final DateType<T> dateType, final int dateStyle, final int timeStyle) {
        this.dateFormats = (List<DateFormat>)new ArrayList();
        this.dateType = $Gson$Preconditions.checkNotNull(dateType);
        this.dateFormats.add((Object)DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US));
        if (!Locale.getDefault().equals((Object)Locale.US)) {
            this.dateFormats.add((Object)DateFormat.getDateTimeInstance(dateStyle, timeStyle));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add((Object)PreJava9DateFormatProvider.getUSDateTimeFormat(dateStyle, timeStyle));
        }
    }
    
    @Override
    public void write(final JsonWriter out, final Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        final DateFormat dateFormat = (DateFormat)this.dateFormats.get(0);
        final String dateFormatAsString;
        synchronized (this.dateFormats) {
            dateFormatAsString = dateFormat.format(value);
        }
        out.value(dateFormatAsString);
    }
    
    @Override
    public T read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        final Date date = this.deserializeToDate(in);
        return this.dateType.deserialize(date);
    }
    
    private Date deserializeToDate(final JsonReader in) throws IOException {
        final String s = in.nextString();
        synchronized (this.dateFormats) {
            for (final DateFormat dateFormat : this.dateFormats) {
                try {
                    return dateFormat.parse(s);
                }
                catch (final ParseException ex) {
                    continue;
                }
                break;
            }
        }
        try {
            return ISO8601Utils.parse(s, new ParsePosition(0));
        }
        catch (final ParseException e) {
            throw new JsonSyntaxException("Failed parsing '" + s + "' as Date; at path " + in.getPreviousPath(), (Throwable)e);
        }
    }
    
    @Override
    public String toString() {
        final DateFormat defaultFormat = (DateFormat)this.dateFormats.get(0);
        if (defaultFormat instanceof SimpleDateFormat) {
            return "DefaultDateTypeAdapter(" + ((SimpleDateFormat)defaultFormat).toPattern() + ')';
        }
        return "DefaultDateTypeAdapter(" + defaultFormat.getClass().getSimpleName() + ')';
    }
    
    public abstract static class DateType<T extends Date>
    {
        public static final DateType<Date> DATE;
        private final Class<T> dateClass;
        
        protected DateType(final Class<T> dateClass) {
            this.dateClass = dateClass;
        }
        
        protected abstract T deserialize(final Date p0);
        
        private final TypeAdapterFactory createFactory(final DefaultDateTypeAdapter<T> adapter) {
            return TypeAdapters.newFactory(this.dateClass, adapter);
        }
        
        public final TypeAdapterFactory createAdapterFactory(final String datePattern) {
            return this.createFactory(new DefaultDateTypeAdapter<T>(this, datePattern, null));
        }
        
        public final TypeAdapterFactory createAdapterFactory(final int style) {
            return this.createFactory(new DefaultDateTypeAdapter<T>(this, style, null));
        }
        
        public final TypeAdapterFactory createAdapterFactory(final int dateStyle, final int timeStyle) {
            return this.createFactory(new DefaultDateTypeAdapter<T>(this, dateStyle, timeStyle, null));
        }
        
        public final TypeAdapterFactory createDefaultsAdapterFactory() {
            return this.createFactory(new DefaultDateTypeAdapter<T>(this, 2, 2, null));
        }
        
        static {
            DATE = new DateType<Date>() {
                @Override
                protected Date deserialize(final Date date) {
                    return date;
                }
            };
        }
    }
}
