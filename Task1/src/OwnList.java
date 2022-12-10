//1. Посмотреть, как реализована защита от искажения возвращаемых данных,
// если во время итерирования содержимое контейнера меняется извне.
//1.1. Придумать и реализовать итератор, который будет устойчив к
// изменению данных извне. (схема copy-on-write).
// Реализовать наследование от класса List, переопределить методы
// add, addAll, contains, containsAll, retainAll, remove, removeAll, get,
// clear, isEmpty,size, get, toArray, iterato
//1 пункт: это написать свою реализацию потоко-безопасного списка
// при его итерации(в джаве есть такая раелазицая, можете подсмотреть, но не копипастить)

import java.util.*;

/*
 * Task 1
 * Write own thread-safety list */
public class OwnList<E> extends AbstractList<E> {
    private transient volatile Object[] mainArray;

    public OwnList() {
        setArray(new Object[0]);
    }

    final Object[] getArray() {
        return mainArray;
    }

    final void setArray(Object[] a) {
        mainArray = a;
    }

    @Override
    public int size() {
        Object[] array = getArray();
        return array.length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        Object[] array = getArray();
        if (indexOf(o, array) == -1)
            return false;
        return true;

    }

    static final class Iterator<E> implements ListIterator<E> {
        private int pointer;
        private final Object[] array;

        Iterator(int pointer, Object[] array) {
            this.pointer = pointer;
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return pointer < array.length;
        }

        @Override
        public E next() {
            if (hasNext())
                return (E) array[pointer++];
            throw new NoSuchElementException();
        }

        @Override
        public boolean hasPrevious() {
            return pointer > 0;
        }

        @Override
        public E previous() {
            if (hasPrevious())
                return (E) array[pointer--];
            throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return pointer;
        }

        @Override
        public int previousIndex() {
            return pointer--;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();

        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<E> iterator() {
        Object[] array = getArray();
        return new Iterator<E>(0, array);
    }

    @Override
    public Object[] toArray() {
        Object[] array = getArray();
        return array.clone();
    }

    @Override
    public E remove(int index) {

        Object[] deletingArray = getArray();
        E oldValue = (E) deletingArray[index];
        int numMoved = deletingArray.length - index - 1;
        Object[] newArray;
        if (numMoved == 0)
            newArray = Arrays.copyOf(deletingArray, deletingArray.length - 1);
        else {
            newArray = new Object[deletingArray.length - 1];
            System.arraycopy(deletingArray, 0, newArray, 0, index);
            System.arraycopy(deletingArray, index + 1, newArray, index,
                    numMoved);
        }
        setArray(newArray);
        return oldValue;

    }

    @Override
    public boolean remove(Object o) {
        Object[] deletingArray = getArray();
        int index = indexOf(o, deletingArray);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        Object[] addingArray = getArray();
        addingArray = Arrays.copyOf(addingArray, addingArray.length + 1);
        addingArray[addingArray.length - 1] = e;
        setArray(addingArray);
        return true;
    }

    @Override
    public void add(int index, E element) {
        Object[] array = getArray();
        int len = array.length;
        if (index > len || index < 0)
            throw new IndexOutOfBoundsException();
        Object[] newArray;
        int itemsToMove = len - index;
        if (itemsToMove == 0)
            newArray = Arrays.copyOf(array, len + 1);
        else {
            newArray = new Object[len + 1];
            System.arraycopy(array, 0, newArray, 0, index);
            System.arraycopy(array, index, newArray, index + 1,
                    itemsToMove);
        }
        newArray[index] = element;
        setArray(newArray);
    }

    private static int indexOf(Object o, Object[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(o) || array[i] == null)
                return i;
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Object[] containsArray = getArray();
        for (Object o : c) {
            if (indexOf(o, containsArray) == -1)
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] array = getArray();
        return addAll(array.length, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (!c.isEmpty()) {
            Object[] array = getArray();
            Object[] ca = c.toArray();
            int len = array.length;
            if (index > len || index < 0)
                throw new IndexOutOfBoundsException();
            int itemsToMove = len - index;
            Object[] newArray;
            if (itemsToMove == 0) {
                newArray = Arrays.copyOf(array, len + ca.length);
            } else {
                newArray = new Object[len + ca.length];
                System.arraycopy(array, 0, newArray, 0, index);
                System.arraycopy(array, index, newArray, index + ca.length, itemsToMove);
            }
            System.arraycopy(ca, 0, newArray, index, ca.length);
            setArray(newArray);
            return true;
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (!c.isEmpty()) {
            boolean res = false;
            for (Object o : c.toArray()) {
                if (remove(o)) {
                    res = true;
                }
                ;
            }
            return res;
        } else {
            throw new NullPointerException();
        }

    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (!c.isEmpty()) {
            Object[] array = getArray();
            boolean res = false;
            for (Object o : array) {
                if (c.contains(o))
                    if (remove(o))
                        res = true;
            }
            return res;
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public E get(int index) {
        Object[] array = getArray();
        return (E) array[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object o : getArray()) {
            sb.append(o + " ");
        }
        sb.append("]");
        return sb.toString();
    }
}
