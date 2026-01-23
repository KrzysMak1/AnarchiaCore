package cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence;

import lombok.Generated;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comments;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.CustomKey;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comment;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;

public class StorageConfig extends OkaeriConfig
{
    @Comment({ "W jakiej formie maja byc zapisywane dane o graczu?", "Dostepne zapisy: (FLAT, MYSQL, MONGO, H2)" })
    @CustomKey("storage-type")
    public StorageType storageType;
    @Comment({ "Jaki prefix ustawic dla danych?", "Nie uzupelniaj prefixu dla zapisu typu FLAT." })
    @CustomKey("prefix")
    public String prefix;
    @Comments({ @Comment({ "FLAT   : not applicable" }), @Comment({ "MONGO  : mongodb://{host}:{port}/{database}" }), @Comment({ "MYSQL  : jdbc:mysql://{host}:{port}/{database}?user={username}&password={password}" }), @Comment({ "H2     : jdbc:h2:file:./plugins/{data_folder}/storage;mode=mysql" }), @Comment({ "uri" }) })
    public String uri;
    
    public StorageConfig(@NonNull final String prefix) {
        this.storageType = StorageType.FLAT;
        this.prefix = "dreamtemplate";
        this.uri = "";
        if (prefix == null) {
            throw new NullPointerException("prefix is marked non-null but is null");
        }
        this.prefix = prefix;
    }
    
    @Generated
    public StorageConfig() {
        this.storageType = StorageType.FLAT;
        this.prefix = "dreamtemplate";
        this.uri = "";
    }
    
    @Generated
    public StorageConfig(final StorageType storageType, final String prefix, final String uri) {
        this.storageType = StorageType.FLAT;
        this.prefix = "dreamtemplate";
        this.uri = "";
        this.storageType = storageType;
        this.prefix = prefix;
        this.uri = uri;
    }
}
