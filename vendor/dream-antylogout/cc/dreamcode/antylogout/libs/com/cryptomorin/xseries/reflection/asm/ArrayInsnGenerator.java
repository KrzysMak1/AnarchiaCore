package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.asm;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

final class ArrayInsnGenerator
{
    private final GeneratorAdapter mv;
    private final int length;
    private int index;
    private final int storeInsn;
    
    public ArrayInsnGenerator(final GeneratorAdapter mv, final Class<?> type, final int length) {
        this.index = 0;
        if (type.getComponentType() != null) {
            throw new IllegalArgumentException("The raw array element type must be given, not the array type itself: " + (Object)type);
        }
        this.mv = mv;
        this.length = length;
        final Type asmType = Type.getType((Class)type);
        this.storeInsn = ((type == Object.class) ? -1 : asmType.getOpcode(79));
        mv.push(length);
        mv.newArray(asmType);
    }
    
    private boolean isDynamicStoreInsn() {
        return this.storeInsn == -1;
    }
    
    public void add(final Runnable instruction) {
        if (this.isDynamicStoreInsn()) {
            throw new IllegalStateException("Must provide the type of stored object since this is a dynamic type array");
        }
        this.add(instruction, this.storeInsn);
    }
    
    public void add(final Type elementType, final Runnable instruction) {
        this.add(instruction, elementType.getOpcode(79));
    }
    
    private void add(final Runnable instruction, final int storeInsn) {
        if (this.index >= this.length) {
            throw new IllegalStateException("Array is already full, at index " + this.index);
        }
        this.mv.visitInsn(89);
        this.mv.push(this.index++);
        instruction.run();
        this.mv.visitInsn(storeInsn);
    }
}
