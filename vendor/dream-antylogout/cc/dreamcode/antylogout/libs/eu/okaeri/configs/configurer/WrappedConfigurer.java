package cc.dreamcode.antylogout.libs.eu.okaeri.configs.configurer;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.ConfigDeclaration;
import java.util.Map;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.exception.OkaeriException;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesContext;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.FieldDeclaration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.List;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import lombok.Generated;

public class WrappedConfigurer extends Configurer
{
    private final Configurer wrapped;
    
    @Generated
    public WrappedConfigurer(final Configurer wrapped) {
        this.wrapped = wrapped;
    }
    
    @Generated
    public Configurer getWrapped() {
        return this.wrapped;
    }
    
    @Generated
    @Override
    public void register(final OkaeriSerdesPack pack) {
        this.getWrapped().register(pack);
    }
    
    @Generated
    @Override
    public List<String> getExtensions() {
        return this.getWrapped().getExtensions();
    }
    
    @Generated
    @Override
    public void setValue(final String key, final Object value, final GenericsDeclaration genericType, final FieldDeclaration field) {
        this.getWrapped().setValue(key, value, genericType, field);
    }
    
    @Generated
    @Override
    public void setValueUnsafe(final String key, final Object value) {
        this.getWrapped().setValueUnsafe(key, value);
    }
    
    @Generated
    @Override
    public Object getValue(final String key) {
        return this.getWrapped().getValue(key);
    }
    
    @Generated
    @Override
    public Object getValueUnsafe(final String key) {
        return this.getWrapped().getValueUnsafe(key);
    }
    
    @Generated
    @Override
    public Object remove(final String key) {
        return this.getWrapped().remove(key);
    }
    
    @Generated
    @Override
    public boolean isToStringObject(final Object object, final GenericsDeclaration genericType, final SerdesContext serdesContext) {
        return this.getWrapped().isToStringObject(object, genericType, serdesContext);
    }
    
    @Deprecated
    @Generated
    @Override
    public boolean isToStringObject(final Object object, final GenericsDeclaration genericType) {
        return this.getWrapped().isToStringObject(object, genericType);
    }
    
    @Generated
    @Override
    public Object simplifyCollection(final Collection<?> value, final GenericsDeclaration genericType, final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        return this.getWrapped().simplifyCollection(value, genericType, serdesContext, conservative);
    }
    
    @Generated
    @Override
    public Object simplifyMap(final Map<Object, Object> value, final GenericsDeclaration genericType, final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        return this.getWrapped().simplifyMap(value, genericType, serdesContext, conservative);
    }
    
    @Generated
    @Override
    public Object simplify(final Object value, final GenericsDeclaration genericType, final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        return this.getWrapped().simplify(value, genericType, serdesContext, conservative);
    }
    
    @Generated
    @Override
    public <T> T getValue(final String key, final Class<T> clazz, final GenericsDeclaration genericType, final SerdesContext serdesContext) {
        return this.getWrapped().getValue(key, clazz, genericType, serdesContext);
    }
    
    @Generated
    @Override
    public <T> T resolveType(final Object object, final GenericsDeclaration genericSource, final Class<T> targetClazz, final GenericsDeclaration genericTarget, final SerdesContext serdesContext) throws OkaeriException {
        return this.getWrapped().resolveType(object, genericSource, targetClazz, genericTarget, serdesContext);
    }
    
    @Generated
    @Override
    public Class<?> resolveTargetBaseType(final SerdesContext serdesContext, final GenericsDeclaration target, final GenericsDeclaration source) {
        return this.getWrapped().resolveTargetBaseType(serdesContext, target, source);
    }
    
    @Generated
    @Override
    public Object createInstance(final Class<?> clazz) throws OkaeriException {
        return this.getWrapped().createInstance(clazz);
    }
    
    @Generated
    @Override
    public boolean keyExists(final String key) {
        return this.getWrapped().keyExists(key);
    }
    
    @Generated
    @Override
    public boolean isValid(final FieldDeclaration declaration, final Object value) {
        return this.getWrapped().isValid(declaration, value);
    }
    
    @Generated
    @Override
    public List<String> getAllKeys() {
        return this.getWrapped().getAllKeys();
    }
    
    @Generated
    @Override
    public Set<String> sort(final ConfigDeclaration declaration) {
        return this.getWrapped().sort(declaration);
    }
    
    @Generated
    @Override
    public void write(final OutputStream outputStream, final ConfigDeclaration declaration) throws Exception {
        this.getWrapped().write(outputStream, declaration);
    }
    
    @Generated
    @Override
    public void load(final InputStream inputStream, final ConfigDeclaration declaration) throws Exception {
        this.getWrapped().load(inputStream, declaration);
    }
    
    @Generated
    @Override
    public OkaeriConfig getParent() {
        return this.getWrapped().getParent();
    }
    
    @Generated
    @Override
    public void setParent(final OkaeriConfig parent) {
        this.getWrapped().setParent(parent);
    }
    
    @Generated
    @Override
    public void setRegistry(final SerdesRegistry registry) {
        this.getWrapped().setRegistry(registry);
    }
    
    @Generated
    @Override
    public SerdesRegistry getRegistry() {
        return this.getWrapped().getRegistry();
    }
}
