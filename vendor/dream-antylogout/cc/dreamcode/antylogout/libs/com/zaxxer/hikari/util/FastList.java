package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util;

import java.util.Comparator;
import java.util.function.UnaryOperator;
import java.util.function.Predicate;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.ListIterator;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.lang.reflect.Array;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.List;

public final class FastList<T> implements List<T>, RandomAccess, Serializable
{
    private static final long serialVersionUID = -4598088075242913858L;
    private final Class<?> clazz;
    private T[] elementData;
    private int size;
    
    public FastList(final Class<?> clazz) {
        this.elementData = (T[])Array.newInstance((Class)clazz, 32);
        this.clazz = clazz;
    }
    
    public FastList(final Class<?> clazz, final int capacity) {
        this.elementData = (T[])Array.newInstance((Class)clazz, capacity);
        this.clazz = clazz;
    }
    
    public boolean add(final T element) {
        if (this.size < this.elementData.length) {
            this.elementData[this.size++] = element;
        }
        else {
            final int oldCapacity = this.elementData.length;
            final int newCapacity = oldCapacity << 1;
            final T[] newElementData = (T[])Array.newInstance((Class)this.clazz, newCapacity);
            System.arraycopy((Object)this.elementData, 0, (Object)newElementData, 0, oldCapacity);
            newElementData[this.size++] = element;
            this.elementData = newElementData;
        }
        return true;
    }
    
    public T get(final int index) {
        return this.elementData[index];
    }
    
    public T removeLast() {
        final T[] elementData = this.elementData;
        final int size = this.size - 1;
        this.size = size;
        final T element = elementData[size];
        this.elementData[this.size] = null;
        return element;
    }
    
    public boolean remove(final Object element) {
        for (int index = this.size - 1; index >= 0; --index) {
            if (element == this.elementData[index]) {
                final int numMoved = this.size - index - 1;
                if (numMoved > 0) {
                    System.arraycopy((Object)this.elementData, index + 1, (Object)this.elementData, index, numMoved);
                }
                this.elementData[--this.size] = null;
                return true;
            }
        }
        return false;
    }
    
    public void clear() {
        for (int i = 0; i < this.size; ++i) {
            this.elementData[i] = null;
        }
        this.size = 0;
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public T set(final int index, final T element) {
        final T old = this.elementData[index];
        this.elementData[index] = element;
        return old;
    }
    
    public T remove(final int index) {
        if (this.size == 0) {
            return null;
        }
        final T old = this.elementData[index];
        final int numMoved = this.size - index - 1;
        if (numMoved > 0) {
            System.arraycopy((Object)this.elementData, index + 1, (Object)this.elementData, index, numMoved);
        }
        this.elementData[--this.size] = null;
        return old;
    }
    
    public boolean contains(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    public Iterator<T> iterator() {
        return (Iterator<T>)new Iterator<T>() {
            private int index;
            
            public boolean hasNext() {
                return this.index < FastList.this.size;
            }
            
            public T next() {
                if (this.index < FastList.this.size) {
                    return FastList.this.elementData[this.index++];
                }
                throw new NoSuchElementException("No more elements in FastList");
            }
        };
    }
    
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
    
    public <E> E[] toArray(final E[] a) {
        throw new UnsupportedOperationException();
    }
    
    public boolean containsAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(final Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(final int index, final Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }
    
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    public void add(final int index, final T element) {
        throw new UnsupportedOperationException();
    }
    
    public int indexOf(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    public int lastIndexOf(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }
    
    public ListIterator<T> listIterator(final int index) {
        throw new UnsupportedOperationException();
    }
    
    public List<T> subList(final int fromIndex, final int toIndex) {
        throw new UnsupportedOperationException();
    }
    
    public Object clone() {
        throw new UnsupportedOperationException();
    }
    
    public void forEach(final Consumer<? super T> action) {
        throw new UnsupportedOperationException();
    }
    
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }
    
    public boolean removeIf(final Predicate<? super T> filter) {
        throw new UnsupportedOperationException();
    }
    
    public void replaceAll(final UnaryOperator<T> operator) {
        throw new UnsupportedOperationException();
    }
    
    public void sort(final Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }
}
