package cc.dreamcode.antylogout.libs.com.google.gson.internal;

import java.io.Writer;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonWriter;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonParseException;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonIOException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonSyntaxException;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonNull;
import cc.dreamcode.antylogout.libs.com.google.gson.internal.bind.TypeAdapters;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonElement;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;

public final class Streams
{
    private Streams() {
        throw new UnsupportedOperationException();
    }
    
    public static JsonElement parse(final JsonReader reader) throws JsonParseException {
        boolean isEmpty = true;
        try {
            reader.peek();
            isEmpty = false;
            return TypeAdapters.JSON_ELEMENT.read(reader);
        }
        catch (final EOFException e) {
            if (isEmpty) {
                return JsonNull.INSTANCE;
            }
            throw new JsonSyntaxException((Throwable)e);
        }
        catch (final MalformedJsonException e2) {
            throw new JsonSyntaxException((Throwable)e2);
        }
        catch (final IOException e3) {
            throw new JsonIOException((Throwable)e3);
        }
        catch (final NumberFormatException e4) {
            throw new JsonSyntaxException((Throwable)e4);
        }
    }
    
    public static void write(final JsonElement element, final JsonWriter writer) throws IOException {
        TypeAdapters.JSON_ELEMENT.write(writer, element);
    }
    
    public static Writer writerForAppendable(final Appendable appendable) {
        return (appendable instanceof Writer) ? appendable : new AppendableWriter(appendable);
    }
    
    private static final class AppendableWriter extends Writer
    {
        private final Appendable appendable;
        private final CurrentWrite currentWrite;
        
        AppendableWriter(final Appendable appendable) {
            this.currentWrite = new CurrentWrite();
            this.appendable = appendable;
        }
        
        public void write(final char[] chars, final int offset, final int length) throws IOException {
            this.currentWrite.chars = chars;
            this.appendable.append((CharSequence)this.currentWrite, offset, offset + length);
        }
        
        public void write(final int i) throws IOException {
            this.appendable.append((char)i);
        }
        
        public void flush() {
        }
        
        public void close() {
        }
        
        static class CurrentWrite implements CharSequence
        {
            char[] chars;
            
            public int length() {
                return this.chars.length;
            }
            
            public char charAt(final int i) {
                return this.chars[i];
            }
            
            public CharSequence subSequence(final int start, final int end) {
                return (CharSequence)new String(this.chars, start, end - start);
            }
        }
    }
}
