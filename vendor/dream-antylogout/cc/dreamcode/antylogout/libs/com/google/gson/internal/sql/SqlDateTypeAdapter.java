package cc.dreamcode.antylogout.libs.com.google.gson.internal.sql;

import cc.dreamcode.antylogout.libs.com.google.gson.reflect.TypeToken;
import cc.dreamcode.antylogout.libs.com.google.gson.Gson;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParseException;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonSyntaxException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonToken;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapterFactory;
import java.sql.Date;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapter;

final class SqlDateTypeAdapter extends TypeAdapter<Date>
{
    static final TypeAdapterFactory FACTORY;
    private final DateFormat format;
    
    private SqlDateTypeAdapter() {
        this.format = (DateFormat)new SimpleDateFormat("MMM d, yyyy");
    }
    
    @Override
    public Date read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        final String s = in.nextString();
        try {
            final java.util.Date utilDate;
            synchronized (this) {
                utilDate = this.format.parse(s);
            }
            return new Date(utilDate.getTime());
        }
        catch (final ParseException e) {
            throw new JsonSyntaxException("Failed parsing '" + s + "' as SQL Date; at path " + in.getPreviousPath(), (Throwable)e);
        }
    }
    
    @Override
    public void write(final JsonWriter out, final Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        final String dateString;
        synchronized (this) {
            dateString = this.format.format((java.util.Date)value);
        }
        out.value(dateString);
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                return (TypeAdapter<T>)((typeToken.getRawType() == Date.class) ? new SqlDateTypeAdapter(null) : null);
            }
        };
    }
}
