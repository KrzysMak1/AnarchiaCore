package cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.index;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.Document;

public class InMemoryIndex extends Document
{
    private ConcurrentHashMap<String, String> keyToValue;
    private ConcurrentHashMap<String, Set<String>> valueToKeys;
    
    public InMemoryIndex() {
        this.keyToValue = (ConcurrentHashMap<String, String>)new ConcurrentHashMap();
        this.valueToKeys = (ConcurrentHashMap<String, Set<String>>)new ConcurrentHashMap();
    }
    
    public ConcurrentHashMap<String, String> getKeyToValue() {
        return this.keyToValue;
    }
    
    public ConcurrentHashMap<String, Set<String>> getValueToKeys() {
        return this.valueToKeys;
    }
}
