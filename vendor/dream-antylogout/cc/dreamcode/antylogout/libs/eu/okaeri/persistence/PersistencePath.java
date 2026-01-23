package cc.dreamcode.antylogout.libs.eu.okaeri.persistence;

import java.util.stream.Collectors;
import java.util.Locale;
import java.util.List;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;
import lombok.NonNull;
import java.io.File;

public class PersistencePath
{
    public static final String SEPARATOR = ":";
    private String value;
    
    public static PersistencePath of(@NonNull final File file) {
        if (file == null) {
            throw new NullPointerException("file is marked non-null but is null");
        }
        return new PersistencePath(file.getPath().replace((CharSequence)File.separator, (CharSequence)":"));
    }
    
    public static PersistencePath of(@NonNull final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        return new PersistencePath(String.valueOf((Object)uuid));
    }
    
    public static PersistencePath of(@NonNull final String path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return new PersistencePath(path);
    }
    
    public static PersistencePath parse(@NonNull final String source, @NonNull final String separator) {
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        if (separator == null) {
            throw new NullPointerException("separator is marked non-null but is null");
        }
        return new PersistencePath(source.replace((CharSequence)separator, (CharSequence)":"));
    }
    
    public PersistencePath sub(@NonNull final UUID sub) {
        if (sub == null) {
            throw new NullPointerException("sub is marked non-null but is null");
        }
        return this.sub(String.valueOf((Object)sub));
    }
    
    public PersistencePath sub(@NonNull final PersistencePath sub) {
        if (sub == null) {
            throw new NullPointerException("sub is marked non-null but is null");
        }
        if (this.value.isEmpty()) {
            return of(sub.getValue());
        }
        return this.sub(sub.getValue());
    }
    
    public PersistencePath sub(@NonNull final String sub) {
        if (sub == null) {
            throw new NullPointerException("sub is marked non-null but is null");
        }
        final boolean startsWithSeparator = sub.startsWith(":");
        if (this.value.isEmpty()) {
            return of(startsWithSeparator ? sub.substring(1) : sub);
        }
        final String separator = startsWithSeparator ? "" : ":";
        return this.append(separator + sub);
    }
    
    public PersistencePath append(@NonNull final String element) {
        if (element == null) {
            throw new NullPointerException("element is marked non-null but is null");
        }
        return of(this.value + element);
    }
    
    public PersistencePath removeStart(@NonNull final String part) {
        if (part == null) {
            throw new NullPointerException("part is marked non-null but is null");
        }
        return this.value.startsWith(part) ? of(this.value.substring(part.length())) : this;
    }
    
    public PersistencePath removeStart(@NonNull final PersistencePath path) {
        if (path == null) {
            throw new NullPointerException("path is marked non-null but is null");
        }
        return this.removeStart(path.getValue());
    }
    
    public PersistencePath group() {
        final String[] parts = this.value.split(":");
        return of(String.join((CharSequence)":", (CharSequence[])Arrays.copyOfRange((Object[])parts, 0, parts.length - 1)));
    }
    
    public File toFile() {
        return new File(this.toSafeFilePath());
    }
    
    public Path toPath() {
        return this.toFile().toPath();
    }
    
    public String toSqlIdentifier() {
        final String identifier = this.value.replace((CharSequence)":", (CharSequence)"_");
        if (!identifier.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException("identifier '" + identifier + "' cannot be used as sql identifier");
        }
        return identifier;
    }
    
    public String toMariaDbJsonPath() {
        final String identifier = "$." + this.value.replace((CharSequence)":", (CharSequence)".");
        if (identifier.contains((CharSequence)"'") || identifier.contains((CharSequence)"/") || identifier.contains((CharSequence)"#") || identifier.contains((CharSequence)"--")) {
            throw new IllegalArgumentException("identifier '" + identifier + "' cannot be used as sql json path");
        }
        return identifier;
    }
    
    public String toPostgresJsonPath() {
        return this.toPostgresJsonPath(false);
    }
    
    public String toPostgresJsonPath(final boolean stringify) {
        final StringBuilder identifier = new StringBuilder();
        final String[] parts = this.value.split(":");
        for (int i = 0; i < parts.length; ++i) {
            if (parts[i].contains((CharSequence)"'")) {
                throw new IllegalArgumentException("identifier " + (Object)identifier + " cannot be used as sql json path");
            }
            if (i > 0) {
                if (i == parts.length - 1 && stringify) {
                    identifier.append("->>");
                }
                else {
                    identifier.append("->");
                }
                identifier.append("'");
            }
            identifier.append(parts[i]);
            if (i > 0) {
                identifier.append("'");
            }
        }
        if (identifier.toString().contains((CharSequence)"/") || identifier.toString().contains((CharSequence)"#") || identifier.toString().contains((CharSequence)"--")) {
            throw new IllegalArgumentException("identifier " + (Object)identifier + " cannot be used as sql json path");
        }
        return identifier.toString();
    }
    
    public String toMongoPath() {
        return this.value.replace((CharSequence)":", (CharSequence)".");
    }
    
    public List<String> toParts() {
        return (List<String>)Arrays.asList((Object[])this.value.split(":"));
    }
    
    public String toSafeFilePath() {
        if (this.value.length() >= 3 && "::".equals((Object)this.value.substring(1, 3)) && System.getProperty("os.name").toLowerCase(Locale.ROOT).contains((CharSequence)"windows")) {
            final String suffix = this.toSafeFilePath(this.value.substring(3).split(":"));
            return this.value.charAt(0) + ":" + File.separator + suffix;
        }
        return this.toSafeFilePath(this.value.split(":"));
    }
    
    private String toSafeFilePath(final String[] parts) {
        return (String)Arrays.stream((Object[])parts).map(part -> part.replace((CharSequence)"^\\.+", (CharSequence)"").replaceAll("[\\\\/:*?\"<>|]", "")).collect(Collectors.joining((CharSequence)File.separator));
    }
    
    public String toSafeFileName() {
        return this.toSafeFilePath().replace((CharSequence)File.separator, (CharSequence)"_");
    }
    
    public UUID toUUID() {
        return UUID.fromString(this.value);
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return "PersistencePath(value=" + this.getValue() + ")";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PersistencePath)) {
            return false;
        }
        final PersistencePath other = (PersistencePath)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PersistencePath;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    protected PersistencePath() {
    }
    
    protected PersistencePath(final String value) {
        this.value = value;
    }
}
