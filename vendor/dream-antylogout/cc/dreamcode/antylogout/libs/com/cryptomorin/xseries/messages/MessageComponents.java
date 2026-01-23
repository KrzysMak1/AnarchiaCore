package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.messages;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftPackage;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.ApiStatus;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import java.lang.invoke.MethodHandle;

public final class MessageComponents
{
    private static final MethodHandle CraftChatMessage_fromJson;
    
    @ApiStatus.Experimental
    public static Object bungeeToVanilla(final BaseComponent component) throws Throwable {
        final String json = ComponentSerializer.toString(component);
        return MessageComponents.CraftChatMessage_fromJson.invoke(json);
    }
    
    public static BaseComponent fromLegacy(final String message) {
        return (BaseComponent)new TextComponent(TextComponent.fromLegacyText(message));
    }
    
    static {
        final MinecraftClassHandle IChatBaseComponentClass = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent");
        MethodHandle fromJson;
        try {
            fromJson = XReflection.ofMinecraft().inPackage(MinecraftPackage.CB, "util").named("CraftChatMessage").method("public static IChatBaseComponent fromJSON(String jsonMessage)").returns(IChatBaseComponentClass).reflect();
        }
        catch (final Throwable ex) {
            fromJson = null;
        }
        CraftChatMessage_fromJson = fromJson;
    }
    
    public static final class MessageTextString implements MessageText
    {
        private final String string;
        
        public MessageTextString(final String string) {
            this.string = string;
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + '(' + this.string + ')';
        }
        
        @Override
        public String asString() {
            return this.string;
        }
        
        @Override
        public BaseComponent asComponent() {
            return MessageComponents.fromLegacy(this.string);
        }
    }
    
    public static final class MessageTextComponent implements MessageText
    {
        private final BaseComponent component;
        
        public MessageTextComponent(final BaseComponent component) {
            this.component = component;
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + '(' + (Object)this.component + ')';
        }
        
        @Override
        public String asString() {
            return this.component.toLegacyText();
        }
        
        @Override
        public BaseComponent asComponent() {
            return this.component;
        }
    }
    
    public interface MessageText
    {
        String asString();
        
        BaseComponent asComponent();
    }
}
