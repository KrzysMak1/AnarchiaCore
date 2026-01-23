package cc.dreamcode.antylogout.libs.cc.dreamcode.notice.bukkit;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.messages.Titles;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.messages.ActionBar;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.StringColorUtil;
import java.util.Arrays;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.util.Collection;
import java.util.Map;
import org.bukkit.command.CommandSender;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.notice.NoticeType;
import cc.dreamcode.antylogout.libs.cc.dreamcode.notice.Notice;

public class BukkitNotice extends Notice<BukkitNotice> implements BukkitSender
{
    public BukkitNotice(@NonNull final NoticeType noticeType, @NonNull final String... noticeText) {
        super(noticeType, noticeText);
        if (noticeType == null) {
            throw new NullPointerException("noticeType is marked non-null but is null");
        }
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
    }
    
    public static BukkitNotice of(@NonNull final NoticeType noticeType, @NonNull final String... noticeText) {
        if (noticeType == null) {
            throw new NullPointerException("noticeType is marked non-null but is null");
        }
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(noticeType, noticeText);
    }
    
    public static BukkitNotice chat(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.CHAT, noticeText);
    }
    
    public static BukkitNotice actionBar(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.ACTION_BAR, noticeText);
    }
    
    public static BukkitNotice title(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.TITLE, noticeText);
    }
    
    public static BukkitNotice subtitle(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.SUBTITLE, noticeText);
    }
    
    public static BukkitNotice titleSubtitle(@NonNull final String... noticeText) {
        if (noticeText == null) {
            throw new NullPointerException("noticeText is marked non-null but is null");
        }
        return new BukkitNotice(NoticeType.TITLE_SUBTITLE, noticeText);
    }
    
    @Override
    public void send(@NonNull final CommandSender target) {
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        this.sendFormatted(target);
        this.clearRender();
    }
    
    @Override
    public void send(@NonNull final CommandSender target, @NonNull final Map<String, Object> mapReplacer) {
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        this.sendFormatted(target);
        this.clearRender();
    }
    
    @Override
    public void send(@NonNull final CommandSender target, @NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders) {
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        this.sendFormatted(target, colorizePlaceholders);
        this.clearRender();
    }
    
    @Override
    public void send(@NonNull final Collection<CommandSender> targets) {
        if (targets == null) {
            throw new NullPointerException("targets is marked non-null but is null");
        }
        targets.forEach(this::sendFormatted);
        this.clearRender();
    }
    
    @Override
    public void send(@NonNull final Collection<CommandSender> targets, @NonNull final Map<String, Object> mapReplacer) {
        if (targets == null) {
            throw new NullPointerException("targets is marked non-null but is null");
        }
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        targets.forEach(this::sendFormatted);
        this.clearRender();
    }
    
    @Override
    public void send(@NonNull final Collection<CommandSender> targets, @NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders) {
        if (targets == null) {
            throw new NullPointerException("targets is marked non-null but is null");
        }
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        targets.forEach(target -> this.sendFormatted(target, colorizePlaceholders));
        this.clearRender();
    }
    
    @Override
    public void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::sendFormatted);
        this.clearRender();
    }
    
    @Override
    public void sendAll(@NonNull final Map<String, Object> mapReplacer) {
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(this::sendFormatted);
        this.clearRender();
    }
    
    @Override
    public void sendAll(@NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders) {
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(target -> this.sendFormatted((CommandSender)target, colorizePlaceholders));
        this.clearRender();
    }
    
    @Override
    public void sendBroadcast() {
        Bukkit.getOnlinePlayers().forEach(this::sendFormatted);
        this.sendFormatted((CommandSender)Bukkit.getConsoleSender());
        this.clearRender();
    }
    
    @Override
    public void sendBroadcast(@NonNull final Map<String, Object> mapReplacer) {
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(this::sendFormatted);
        this.sendFormatted((CommandSender)Bukkit.getConsoleSender());
        this.clearRender();
    }
    
    @Override
    public void sendBroadcast(@NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders) {
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(target -> this.sendFormatted((CommandSender)target, colorizePlaceholders));
        this.sendFormatted((CommandSender)Bukkit.getConsoleSender(), colorizePlaceholders);
        this.clearRender();
    }
    
    @Override
    public void sendPermitted(@NonNull final String permission) {
        if (permission == null) {
            throw new NullPointerException("permission is marked non-null but is null");
        }
        Bukkit.getOnlinePlayers().stream().filter(target -> target.hasPermission(permission)).forEach(this::sendFormatted);
        this.clearRender();
    }
    
    @Override
    public void sendPermitted(@NonNull final String permission, @NonNull final Map<String, Object> mapReplacer) {
        if (permission == null) {
            throw new NullPointerException("permission is marked non-null but is null");
        }
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().stream().filter(target -> target.hasPermission(permission)).forEach(this::sendFormatted);
        this.clearRender();
    }
    
    @Override
    public void sendPermitted(@NonNull final String permission, @NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders) {
        if (permission == null) {
            throw new NullPointerException("permission is marked non-null but is null");
        }
        if (mapReplacer == null) {
            throw new NullPointerException("mapReplacer is marked non-null but is null");
        }
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().stream().filter(target -> target.hasPermission(permission)).forEach(target -> this.sendFormatted((CommandSender)target, colorizePlaceholders));
        this.clearRender();
    }
    
    private void sendFormatted(@NonNull final CommandSender target) {
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        this.sendFormatted(target, true);
    }
    
    private void sendFormatted(@NonNull final CommandSender target, final boolean colorizePlaceholders) {
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        if (!(target instanceof Player)) {
            Arrays.stream((Object[])this.getNoticeText().split(Notice.lineSeparator())).forEach(text -> target.sendMessage(this.placeholdersExists() ? StringColorUtil.fixColor(text, this.getPlaceholders(), colorizePlaceholders) : StringColorUtil.fixColor(text)));
            return;
        }
        final Player player = (Player)target;
        final NoticeType noticeType = this.getNoticeType();
        switch (noticeType) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                Arrays.stream((Object[])this.getNoticeText().split(Notice.lineSeparator())).forEach(text -> target.sendMessage(this.placeholdersExists() ? StringColorUtil.fixColor(text, this.getPlaceholders(), colorizePlaceholders) : StringColorUtil.fixColor(text)));
                break;
            }
            case ACTION_BAR: {
                final String text = this.getNoticeText().replace((CharSequence)Notice.lineSeparator(), (CharSequence)"");
                ActionBar.sendActionBar(player, this.placeholdersExists() ? StringColorUtil.fixColor(text, this.getPlaceholders(), colorizePlaceholders) : StringColorUtil.fixColor(text));
                break;
            }
            case TITLE: {
                final String text = this.getNoticeText().replace((CharSequence)Notice.lineSeparator(), (CharSequence)"");
                Titles.sendTitle(player, this.getTitleFadeIn(), this.getTitleStay(), this.getTitleFadeOut(), this.placeholdersExists() ? StringColorUtil.fixColor(text, this.getPlaceholders(), colorizePlaceholders) : StringColorUtil.fixColor(text), "");
                break;
            }
            case SUBTITLE: {
                final String text = this.getNoticeText().replace((CharSequence)Notice.lineSeparator(), (CharSequence)"");
                Titles.sendTitle(player, this.getTitleFadeIn(), this.getTitleStay(), this.getTitleFadeOut(), "", this.placeholdersExists() ? StringColorUtil.fixColor(text, this.getPlaceholders(), colorizePlaceholders) : StringColorUtil.fixColor(text));
                break;
            }
            case TITLE_SUBTITLE: {
                final String[] split = this.getNoticeText().split(Notice.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + Notice.lineSeparator() + ") to separate two messages.");
                }
                String title;
                String subTitle;
                if (this.placeholdersExists()) {
                    title = StringColorUtil.fixColor(split[0], this.getPlaceholders(), colorizePlaceholders);
                    subTitle = StringColorUtil.fixColor(split[1], this.getPlaceholders(), colorizePlaceholders);
                }
                else {
                    title = StringColorUtil.fixColor(split[0]);
                    subTitle = StringColorUtil.fixColor(split[1]);
                }
                Titles.sendTitle(player, this.getTitleFadeIn(), this.getTitleStay(), this.getTitleFadeOut(), title, subTitle);
                break;
            }
            default: {
                this.clearRender();
                throw new RuntimeException("Cannot resolve notice-type. (" + (Object)this.getNoticeType() + ")");
            }
        }
    }
}
