package cc.dreamcode.antylogout.libs.eu.okaeri.placeholders.message.part;

import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import java.util.Locale;

public class MessageField implements MessageElement, MessageFieldAccessor
{
    private static final MessageFieldTokenizer TOKENIZER;
    private final Locale locale;
    private final String name;
    private final String source;
    @Nullable
    private final MessageField sub;
    @Nullable
    private String defaultValue;
    @Nullable
    private String metadataRaw;
    @Nullable
    private String raw;
    private FieldParams params;
    private String lastSubPath;
    private MessageField lastSub;
    private String[] metadataOptions;
    
    @Deprecated
    public static MessageField unknown() {
        return of("unknown");
    }
    
    public static MessageField of(@NonNull final String source) {
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        return of(Locale.ENGLISH, source);
    }
    
    public static MessageField of(@NonNull final Locale locale, @NonNull final String source) {
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        final List<FieldParams> parts = MessageField.TOKENIZER.tokenize(source);
        MessageField field = null;
        for (int i = parts.size() - 1; i >= 0; --i) {
            final FieldParams pathElement = (FieldParams)parts.get(i);
            field = new MessageField(locale, pathElement.getField(), source, field);
            field.setParams(pathElement);
        }
        if (field != null) {
            final MessageField lastSub = field.getLastSub();
            final String lastSubPath = field.getLastSubPath();
            field.getParams();
        }
        return field;
    }
    
    private static String lastSubPath(@NonNull final MessageField field) {
        if (field == null) {
            throw new NullPointerException("field is marked non-null but is null");
        }
        MessageField last = field;
        final StringBuilder out = new StringBuilder(field.getName());
        while (last.getSub() != null) {
            last = last.getSub();
            out.append(".").append(last.getName());
        }
        return out.toString();
    }
    
    public void setDefaultValue(@Nullable final String defaultValue) {
        this.defaultValue = defaultValue;
        MessageField sub;
        for (MessageField field = this; field.getSub() != null; field = sub) {
            sub = field.getSub();
            sub.setDefaultValue(defaultValue);
        }
    }
    
    public boolean hasSub() {
        return this.sub != null;
    }
    
    @Nullable
    public MessageField getLastSub() {
        if (this.sub == null) {
            return null;
        }
        if (this.lastSub == null) {
            MessageField last;
            for (last = this.sub; last.getSub() != null; last = last.getSub()) {}
            this.lastSub = last;
        }
        return this.lastSub;
    }
    
    public String getLastSubPath() {
        if (this.lastSubPath == null) {
            this.lastSubPath = lastSubPath(this);
        }
        return this.lastSubPath;
    }
    
    public void setMetadataRaw(@Nullable final String metadataRaw) {
        this.metadataRaw = metadataRaw;
        MessageField sub;
        for (MessageField field = this; field.getSub() != null; field = sub) {
            sub = field.getSub();
            sub.setMetadataRaw(metadataRaw);
            sub.updateMetadataOptionsCache();
        }
        this.updateMetadataOptionsCache();
    }
    
    public void updateMetadataOptionsCache() {
        if (this.metadataRaw == null) {
            return;
        }
        this.metadataOptions = (String[])MessageField.TOKENIZER.tokenizeArgs(this.metadataRaw).toArray((Object[])new String[0]);
    }
    
    @Override
    public Locale locale() {
        return this.getLocale();
    }
    
    @Override
    public FieldParams params() {
        return this.getParams();
    }
    
    @Override
    public MessageField unsafe() {
        return this;
    }
    
    public Locale getLocale() {
        return this.locale;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSource() {
        return this.source;
    }
    
    @Nullable
    public MessageField getSub() {
        return this.sub;
    }
    
    @Nullable
    public String getDefaultValue() {
        return this.defaultValue;
    }
    
    @Nullable
    public String getMetadataRaw() {
        return this.metadataRaw;
    }
    
    @Nullable
    public String getRaw() {
        return this.raw;
    }
    
    public String[] getMetadataOptions() {
        return this.metadataOptions;
    }
    
    public void setRaw(@Nullable final String raw) {
        this.raw = raw;
    }
    
    public void setParams(final FieldParams params) {
        this.params = params;
    }
    
    public void setLastSubPath(final String lastSubPath) {
        this.lastSubPath = lastSubPath;
    }
    
    public void setLastSub(final MessageField lastSub) {
        this.lastSub = lastSub;
    }
    
    public void setMetadataOptions(final String[] metadataOptions) {
        this.metadataOptions = metadataOptions;
    }
    
    @Override
    public String toString() {
        return "MessageField(locale=" + (Object)this.getLocale() + ", name=" + this.getName() + ", source=" + this.getSource() + ", sub=" + (Object)this.getSub() + ", defaultValue=" + this.getDefaultValue() + ", metadataRaw=" + this.getMetadataRaw() + ", raw=" + this.getRaw() + ", params=" + (Object)this.getParams() + ", lastSubPath=" + this.getLastSubPath() + ", lastSub=" + (Object)this.getLastSub() + ", metadataOptions=" + Arrays.deepToString((Object[])this.getMetadataOptions()) + ")";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MessageField)) {
            return false;
        }
        final MessageField other = (MessageField)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        Label_0065: {
            if (this$locale == null) {
                if (other$locale == null) {
                    break Label_0065;
                }
            }
            else if (this$locale.equals(other$locale)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0102: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0102;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$source = this.getSource();
        final Object other$source = other.getSource();
        Label_0139: {
            if (this$source == null) {
                if (other$source == null) {
                    break Label_0139;
                }
            }
            else if (this$source.equals(other$source)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$sub = this.getSub();
        final Object other$sub = other.getSub();
        Label_0176: {
            if (this$sub == null) {
                if (other$sub == null) {
                    break Label_0176;
                }
            }
            else if (this$sub.equals(other$sub)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$defaultValue = this.getDefaultValue();
        final Object other$defaultValue = other.getDefaultValue();
        Label_0213: {
            if (this$defaultValue == null) {
                if (other$defaultValue == null) {
                    break Label_0213;
                }
            }
            else if (this$defaultValue.equals(other$defaultValue)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$metadataRaw = this.getMetadataRaw();
        final Object other$metadataRaw = other.getMetadataRaw();
        Label_0250: {
            if (this$metadataRaw == null) {
                if (other$metadataRaw == null) {
                    break Label_0250;
                }
            }
            else if (this$metadataRaw.equals(other$metadataRaw)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$params = this.getParams();
        final Object other$params = other.getParams();
        Label_0287: {
            if (this$params == null) {
                if (other$params == null) {
                    break Label_0287;
                }
            }
            else if (this$params.equals(other$params)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$lastSubPath = this.getLastSubPath();
        final Object other$lastSubPath = other.getLastSubPath();
        Label_0324: {
            if (this$lastSubPath == null) {
                if (other$lastSubPath == null) {
                    break Label_0324;
                }
            }
            else if (this$lastSubPath.equals(other$lastSubPath)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$lastSub = this.getLastSub();
        final Object other$lastSub = other.getLastSub();
        if (this$lastSub == null) {
            if (other$lastSub == null) {
                return Arrays.deepEquals((Object[])this.getMetadataOptions(), (Object[])other.getMetadataOptions());
            }
        }
        else if (this$lastSub.equals(other$lastSub)) {
            return Arrays.deepEquals((Object[])this.getMetadataOptions(), (Object[])other.getMetadataOptions());
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MessageField;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $locale = this.getLocale();
        result = result * 59 + (($locale == null) ? 43 : $locale.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $source = this.getSource();
        result = result * 59 + (($source == null) ? 43 : $source.hashCode());
        final Object $sub = this.getSub();
        result = result * 59 + (($sub == null) ? 43 : $sub.hashCode());
        final Object $defaultValue = this.getDefaultValue();
        result = result * 59 + (($defaultValue == null) ? 43 : $defaultValue.hashCode());
        final Object $metadataRaw = this.getMetadataRaw();
        result = result * 59 + (($metadataRaw == null) ? 43 : $metadataRaw.hashCode());
        final Object $params = this.getParams();
        result = result * 59 + (($params == null) ? 43 : $params.hashCode());
        final Object $lastSubPath = this.getLastSubPath();
        result = result * 59 + (($lastSubPath == null) ? 43 : $lastSubPath.hashCode());
        final Object $lastSub = this.getLastSub();
        result = result * 59 + (($lastSub == null) ? 43 : $lastSub.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getMetadataOptions());
        return result;
    }
    
    private MessageField(final Locale locale, final String name, final String source, @Nullable final MessageField sub) {
        this.locale = locale;
        this.name = name;
        this.source = source;
        this.sub = sub;
    }
    
    public FieldParams getParams() {
        return this.params;
    }
    
    static {
        TOKENIZER = new MessageFieldTokenizer();
    }
}
