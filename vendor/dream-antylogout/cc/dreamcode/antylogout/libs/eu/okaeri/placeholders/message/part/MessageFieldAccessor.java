package cc.dreamcode.antylogout.libs.eu.okaeri.placeholders.message.part;

import java.util.Locale;

public interface MessageFieldAccessor
{
    Locale locale();
    
    FieldParams params();
    
    MessageField unsafe();
}
