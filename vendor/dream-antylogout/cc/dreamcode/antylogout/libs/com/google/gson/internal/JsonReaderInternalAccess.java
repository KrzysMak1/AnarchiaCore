package cc.dreamcode.antylogout.libs.com.google.gson.internal;

import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;

public abstract class JsonReaderInternalAccess
{
    public static JsonReaderInternalAccess INSTANCE;
    
    public abstract void promoteNameToValue(final JsonReader p0) throws IOException;
}
