package cc.dreamcode.antylogout.libs.com.google.gson.internal.bind;

import cc.dreamcode.antylogout.libs.com.google.gson.reflect.TypeToken;
import cc.dreamcode.antylogout.libs.com.google.gson.Gson;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonWriter;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonSyntaxException;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParsePosition;
import java.text.ParseException;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonToken;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.PreJava9DateFormatProvider;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.JavaVersion;
import java.util.Locale;
import java.util.ArrayList;
import java.text.DateFormat;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapterFactory;
import java.util.Date;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapter;

public final class DateTypeAdapter extends TypeAdapter<Date>
{
    public static final TypeAdapterFactory FACTORY;
    private final List<DateFormat> dateFormats;
    
    public DateTypeAdapter() {
        (this.dateFormats = (List<DateFormat>)new ArrayList()).add((Object)DateFormat.getDateTimeInstance(2, 2, Locale.US));
        if (!Locale.getDefault().equals((Object)Locale.US)) {
            this.dateFormats.add((Object)DateFormat.getDateTimeInstance(2, 2));
        }
        if (JavaVersion.isJava9OrLater()) {
            this.dateFormats.add((Object)PreJava9DateFormatProvider.getUSDateTimeFormat(2, 2));
        }
    }
    
    @Override
    public Date read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return this.deserializeToDate(in);
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
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                return (TypeAdapter<T>)((typeToken.getRawType() == Date.class) ? new DateTypeAdapter() : null);
            }
        };
    }
}
