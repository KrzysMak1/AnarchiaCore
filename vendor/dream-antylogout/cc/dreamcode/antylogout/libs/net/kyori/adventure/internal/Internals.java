package cc.dreamcode.antylogout.libs.net.kyori.adventure.internal;

import cc.dreamcode.antylogout.libs.net.kyori.examination.Examiner;
import cc.dreamcode.antylogout.libs.net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.net.kyori.examination.Examinable;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class Internals
{
    private Internals() {
    }
    
    @NotNull
    public static String toString(@NotNull final Examinable examinable) {
        return examinable.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
}
