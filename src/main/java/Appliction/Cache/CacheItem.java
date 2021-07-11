package Appliction.Cache;

public class CacheItem <K,V>{
    K key;
    V value;
    CacheItem<K, V> previous;
    CacheItem<K, V> next;

    public CacheItem(CacheItem<K, V> previous, CacheItem<K, V> next, K key, V value) {
        this.previous = previous;
        this.next = next;
        this.key = key;
        this.value = value;
    }
}
