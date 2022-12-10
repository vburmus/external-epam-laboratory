import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private int size;

    /**
     * @param size - size of current cache;
     *             0.75f - standard load factor for linkedHashMap;(to increase capacity for example)
     *             the ordering mode - true for access-order, false for insertion-order
     *             Can be used for:
     *             - saving intermediate results
     *             - saving temporary files
     *             - saving last sessions
     *             - saving las transactions
     */
    public LRUCache(int size) {
        super(size, 0.75f, true);
        this.size = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > size;
    }

}
