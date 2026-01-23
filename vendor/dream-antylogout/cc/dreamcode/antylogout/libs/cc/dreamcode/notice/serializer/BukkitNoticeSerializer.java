package cc.dreamcode.antylogout.libs.cc.dreamcode.notice.serializer;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.DeserializationData;
import java.util.Locale;
import cc.dreamcode.antylogout.libs.cc.dreamcode.notice.NoticeType;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class BukkitNoticeSerializer implements ObjectSerializer<BukkitNotice>
{
    @Override
    public boolean supports(@NonNull final Class<? super BukkitNotice> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return BukkitNotice.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final BukkitNotice object, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        data.add("type", object.getNoticeType(), NoticeType.class);
        data.add("text", object.getNoticeText(), String.class);
        if (!object.getLocale().equals((Object)Locale.forLanguageTag("pl"))) {
            data.add("locale", object.getLocale().toLanguageTag());
        }
        if (object.getTitleFadeIn() != 10) {
            data.add("title-fade-in", object.getTitleFadeIn());
        }
        if (object.getTitleStay() != 20) {
            data.add("title-stay", object.getTitleStay());
        }
        if (object.getTitleFadeOut() != 10) {
            data.add("title-fade-out", object.getTitleFadeOut());
        }
    }
    
    @Override
    public BukkitNotice deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        final BukkitNotice bukkitNotice = new BukkitNotice(data.get("type", NoticeType.class), new String[] { data.get("text", String.class) });
        if (data.containsKey("locale")) {
            bukkitNotice.setLocale(Locale.forLanguageTag((String)data.get("locale", String.class)));
        }
        if (data.containsKey("title-fade-in")) {
            bukkitNotice.setTitleFadeIn(data.get("title-fade-in", Integer.class));
        }
        if (data.containsKey("title-stay")) {
            bukkitNotice.setTitleStay(data.get("title-stay", Integer.class));
        }
        if (data.containsKey("title-fade-out")) {
            bukkitNotice.setTitleFadeOut(data.get("title-fade-out", Integer.class));
        }
        return bukkitNotice;
    }
}
