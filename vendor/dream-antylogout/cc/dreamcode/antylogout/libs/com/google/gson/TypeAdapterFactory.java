package cc.dreamcode.antylogout.libs.com.google.gson;

import cc.dreamcode.antylogout.libs.com.google.gson.reflect.TypeToken;

public interface TypeAdapterFactory
{
     <T> TypeAdapter<T> create(final Gson p0, final TypeToken<T> p1);
}
