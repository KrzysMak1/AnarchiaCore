package cc.dreamcode.antylogout.libs.cc.dreamcode.notice;

import lombok.Generated;
import lombok.NonNull;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class Notice<R extends Notice<?>>
{
    private final NoticeType noticeType;
    private final String noticeText;
    private Locale locale;
    private int titleFadeIn;
    private int titleStay;
    private int titleFadeOut;
    private final Map<String, Object> placeholders;
    
    public Notice(final NoticeType noticeType, final String... noticeText) {
        this.locale = Locale.forLanguageTag("pl");
        this.titleFadeIn = 10;
        this.titleStay = 20;
        this.titleFadeOut = 10;
        this.placeholders = (Map<String, Object>)new HashMap();
        this.noticeType = noticeType;
        if (noticeText.length == 1) {
            this.noticeText = noticeText[0];
            return;
        }
        this.noticeText = StringUtil.join(noticeText, lineSeparator());
    }
    
    public boolean placeholdersExists() {
        return !this.placeholders.isEmpty();
    }
    
    public Map<String, Object> getPlaceholders() {
        return (Map<String, Object>)Collections.unmodifiableMap((Map)this.placeholders);
    }
    
    public R setLocale(final Locale locale) {
        this.locale = locale;
        return (R)this;
    }
    
    public R with(@NonNull final String from, @NonNull final Object to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        this.placeholders.put((Object)from, to);
        return (R)this;
    }
    
    public R with(@NonNull final Map<String, Object> replaceMap) {
        if (replaceMap == null) {
            throw new NullPointerException("replaceMap is marked non-null but is null");
        }
        this.placeholders.putAll((Map)replaceMap);
        return (R)this;
    }
    
    public R clearRender() {
        this.placeholders.clear();
        return (R)this;
    }
    
    public R setTitleFadeIn(final int titleFadeIn) {
        this.titleFadeIn = titleFadeIn;
        return (R)this;
    }
    
    public R setTitleStay(final int titleStay) {
        this.titleStay = titleStay;
        return (R)this;
    }
    
    public R setTitleFadeOut(final int titleFadeOut) {
        this.titleFadeOut = titleFadeOut;
        return (R)this;
    }
    
    public static String lineSeparator() {
        return "%NEWLINE%";
    }
    
    @Generated
    public NoticeType getNoticeType() {
        return this.noticeType;
    }
    
    @Generated
    public String getNoticeText() {
        return this.noticeText;
    }
    
    @Generated
    public Locale getLocale() {
        return this.locale;
    }
    
    @Generated
    public int getTitleFadeIn() {
        return this.titleFadeIn;
    }
    
    @Generated
    public int getTitleStay() {
        return this.titleStay;
    }
    
    @Generated
    public int getTitleFadeOut() {
        return this.titleFadeOut;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Notice)) {
            return false;
        }
        final Notice<?> other = (Notice<?>)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTitleFadeIn() != other.getTitleFadeIn()) {
            return false;
        }
        if (this.getTitleStay() != other.getTitleStay()) {
            return false;
        }
        if (this.getTitleFadeOut() != other.getTitleFadeOut()) {
            return false;
        }
        final Object this$noticeType = this.getNoticeType();
        final Object other$noticeType = other.getNoticeType();
        Label_0104: {
            if (this$noticeType == null) {
                if (other$noticeType == null) {
                    break Label_0104;
                }
            }
            else if (this$noticeType.equals(other$noticeType)) {
                break Label_0104;
            }
            return false;
        }
        final Object this$noticeText = this.getNoticeText();
        final Object other$noticeText = other.getNoticeText();
        Label_0141: {
            if (this$noticeText == null) {
                if (other$noticeText == null) {
                    break Label_0141;
                }
            }
            else if (this$noticeText.equals(other$noticeText)) {
                break Label_0141;
            }
            return false;
        }
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        Label_0178: {
            if (this$locale == null) {
                if (other$locale == null) {
                    break Label_0178;
                }
            }
            else if (this$locale.equals(other$locale)) {
                break Label_0178;
            }
            return false;
        }
        final Object this$placeholders = this.getPlaceholders();
        final Object other$placeholders = other.getPlaceholders();
        if (this$placeholders == null) {
            if (other$placeholders == null) {
                return true;
            }
        }
        else if (this$placeholders.equals(other$placeholders)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof Notice;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTitleFadeIn();
        result = result * 59 + this.getTitleStay();
        result = result * 59 + this.getTitleFadeOut();
        final Object $noticeType = this.getNoticeType();
        result = result * 59 + (($noticeType == null) ? 43 : $noticeType.hashCode());
        final Object $noticeText = this.getNoticeText();
        result = result * 59 + (($noticeText == null) ? 43 : $noticeText.hashCode());
        final Object $locale = this.getLocale();
        result = result * 59 + (($locale == null) ? 43 : $locale.hashCode());
        final Object $placeholders = this.getPlaceholders();
        result = result * 59 + (($placeholders == null) ? 43 : $placeholders.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "Notice(noticeType=" + (Object)this.getNoticeType() + ", noticeText=" + this.getNoticeText() + ", locale=" + (Object)this.getLocale() + ", titleFadeIn=" + this.getTitleFadeIn() + ", titleStay=" + this.getTitleStay() + ", titleFadeOut=" + this.getTitleFadeOut() + ", placeholders=" + (Object)this.getPlaceholders() + ")";
    }
}
