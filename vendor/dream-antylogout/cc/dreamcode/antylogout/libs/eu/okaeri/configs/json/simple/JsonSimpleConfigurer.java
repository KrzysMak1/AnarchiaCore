package cc.dreamcode.antylogout.libs.eu.okaeri.configs.json.simple;

import cc.dreamcode.antylogout.libs.org.json.simple.JSONObject;
import java.io.OutputStream;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.postprocessor.ConfigPostprocessor;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.ConfigDeclaration;
import java.io.InputStream;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.FieldDeclaration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.exception.OkaeriException;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesContext;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import java.util.LinkedHashMap;
import cc.dreamcode.antylogout.libs.org.json.simple.parser.JSONParser;
import java.util.Map;
import cc.dreamcode.antylogout.libs.org.json.simple.parser.ContainerFactory;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.configurer.Configurer;

public class JsonSimpleConfigurer extends Configurer
{
    private static final ContainerFactory CONTAINER_FACTORY;
    private Map<String, Object> map;
    private JSONParser parser;
    
    public JsonSimpleConfigurer() {
        this.parser = new JSONParser();
        this.map = (Map<String, Object>)new LinkedHashMap();
    }
    
    public JsonSimpleConfigurer(@NonNull final JSONParser parser) {
        this(parser, (Map<String, Object>)new LinkedHashMap());
        if (parser == null) {
            throw new NullPointerException("parser is marked non-null but is null");
        }
    }
    
    public JsonSimpleConfigurer(@NonNull final JSONParser parser, @NonNull final Map<String, Object> map) {
        if (parser == null) {
            throw new NullPointerException("parser is marked non-null but is null");
        }
        if (map == null) {
            throw new NullPointerException("map is marked non-null but is null");
        }
        this.parser = parser;
        this.map = map;
    }
    
    @Override
    public List<String> getExtensions() {
        return (List<String>)Collections.singletonList((Object)"json");
    }
    
    @Override
    public Object simplify(final Object value, final GenericsDeclaration genericType, @NonNull final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        if (value == null) {
            return null;
        }
        final GenericsDeclaration genericsDeclaration = GenericsDeclaration.of(value);
        if (genericsDeclaration.getType() == Character.TYPE || genericsDeclaration.getType() == Character.class) {
            return super.simplify(value, genericType, serdesContext, false);
        }
        return super.simplify(value, genericType, serdesContext, conservative);
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
        this.map = (Map<String, Object>)this.parser.parse(data, JsonSimpleConfigurer.CONTAINER_FACTORY);
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
        final JSONObject object = new JSONObject(this.map);
        ConfigPostprocessor.of(object.toJSONString()).write(outputStream);
    }
    
    static {
        CONTAINER_FACTORY = new ContainerFactory() {
            @Override
            public Map createObjectContainer() {
                return (Map)new LinkedHashMap();
            }
            
            @Override
            public List creatArrayContainer() {
                return (List)new ArrayList();
            }
        };
    }
}
