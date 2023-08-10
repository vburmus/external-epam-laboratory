import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModifiedList<E> extends AbstractList<E> {
    private List<E> unmodifiableList;
    private List<E> modifiableList;
    private int unmodSize;
    private int size;

    public ModifiedList(int unmodSize) {
        this.unmodifiableList = new ArrayList<E>();
        this.modifiableList = new ArrayList<E>();
        this.unmodSize = unmodSize;
        size = unmodSize;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return unmodifiableList.isEmpty() && modifiableList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return unmodifiableList.contains(o) || modifiableList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return concat().iterator();
    }

    @Override
    public Object[] toArray() {
        return concat().toArray();
    }

    public List<E> concat() {
        return Stream.concat(unmodifiableList.stream(), modifiableList.stream()).collect(Collectors.toList());
    }

    @Override
    public boolean add(E e) {
        if (unmodSize > unmodifiableList.size()) {
            return unmodifiableList.add(e);
        } else {
            size++;
            return modifiableList.add(e);
        }

    }

    @Override
    public boolean remove(Object o) {
        if (modifiableList.contains(o)) {
            size--;
        }
        return modifiableList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return concat().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (Object o : c) {
            add((E) o);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return modifiableList.retainAll(c);
    }


    @Override
    public E get(int index) {
        if (index >= unmodifiableList.size()) return modifiableList.get(index - unmodifiableList.size());
        else return unmodifiableList.get(index);
    }

}
