package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.annotations.XInfo;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.annotations.XMerge;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.annotations.XChange;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class XModuleMetadata
{
    private final boolean wasRemoved;
    private final XChange[] changes;
    private final XMerge[] merges;
    private final XInfo info;
    
    public XModuleMetadata(final boolean wasRemoved, final XChange[] changes, final XMerge[] merges, final XInfo info) {
        this.wasRemoved = wasRemoved;
        this.changes = changes;
        this.merges = merges;
        this.info = info;
    }
    
    public boolean wasRemoved() {
        return this.wasRemoved;
    }
    
    public XChange[] getChanges() {
        return this.changes;
    }
    
    public XMerge[] getMerges() {
        return this.merges;
    }
    
    public XInfo getInfo() {
        return this.info;
    }
}
