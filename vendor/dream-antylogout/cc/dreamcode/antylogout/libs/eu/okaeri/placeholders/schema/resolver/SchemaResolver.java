package cc.dreamcode.antylogout.libs.eu.okaeri.placeholders.schema.resolver;

import cc.dreamcode.antylogout.libs.eu.okaeri.placeholders.message.part.MessageField;

public interface SchemaResolver
{
    boolean supports(final Class<?> type);
    
    String resolve(final Object object, final MessageField field);
    
    @Deprecated
    String resolve(final Object object);
}
