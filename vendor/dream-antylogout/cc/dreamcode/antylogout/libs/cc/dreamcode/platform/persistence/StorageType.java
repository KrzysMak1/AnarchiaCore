package cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence;

import lombok.Generated;

public enum StorageType
{
    FLAT("FLAT"), 
    MYSQL("MySQL"), 
    H2("H2"), 
    MONGO("MongoDB");
    
    private final String name;
    
    @Generated
    public String getName() {
        return this.name;
    }
    
    @Generated
    private StorageType(final String name) {
        this.name = name;
    }
}
