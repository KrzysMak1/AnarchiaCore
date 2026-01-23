package cc.dreamcode.antylogout.libs.com.google.gson;

import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;

public interface ToNumberStrategy
{
    Number readNumber(final JsonReader p0) throws IOException;
}
