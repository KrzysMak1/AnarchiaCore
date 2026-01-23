package cc.dreamcode.antylogout.libs.eu.okaeri.configs.configurer;

import java.io.OutputStream;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.ConfigDeclaration;
import java.io.InputStream;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.FieldDeclaration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import lombok.NonNull;
import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryConfigurer extends Configurer
{
    private Map<String, Object> map;
    
    public InMemoryConfigurer() {
        this.map = (Map<String, Object>)new LinkedHashMap();
    }
    
    @Override
    public void setValue(@NonNull final String key, final Object value, final GenericsDeclaration type, final FieldDeclaration field) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        this.map.put((Object)key, value);
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
    }
    
    @Override
    public void write(@NonNull final OutputStream outputStream, @NonNull final ConfigDeclaration declaration) throws Exception {
        if (outputStream == null) {
            throw new NullPointerException("outputStream is marked non-null but is null");
        }
        if (declaration == null) {
            throw new NullPointerException("declaration is marked non-null but is null");
        }
    }
}
