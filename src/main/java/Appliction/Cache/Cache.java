package Appliction.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache <K,V>{
    private CacheItem<K, V> leastRecentlyUsed;
    private CacheItem<K, V> mostRecentlyUsed;
    private Map<K, CacheItem<K, V>> container;
    private int capacity;
    private int currentSize;

    public Cache(int capacity) {
        this.capacity = capacity;
        this.currentSize = 0;
        leastRecentlyUsed = new CacheItem<K, V>(null, null, null, null);
        mostRecentlyUsed= leastRecentlyUsed;
        container = new ConcurrentHashMap<K, CacheItem<K, V>>();
    }


    public V get(K key) {

        CacheItem<K, V> tempNode = container.get(key);
        if (tempNode == null) {
            return null;
        }

        else if (tempNode.key == mostRecentlyUsed.key) {
            return mostRecentlyUsed.value;
        }
        CacheItem<K, V> nextNode = tempNode.next;
        CacheItem<K, V> prevNode = tempNode.previous;


        if (tempNode.key == leastRecentlyUsed.key) {
            nextNode.previous = null;
            leastRecentlyUsed = nextNode;
        }

        else {
            prevNode.next = nextNode;
            nextNode.previous = prevNode;
        }


        tempNode.previous = mostRecentlyUsed;
        mostRecentlyUsed.next = tempNode;
        mostRecentlyUsed = tempNode;
        mostRecentlyUsed.next = null;

        return tempNode.value;

    }
    public void put(K key, V value) {
        if (container.containsKey(key)) {
            return;
        }

        CacheItem<K, V> myNode = new CacheItem<>(mostRecentlyUsed, null, key, value);
        mostRecentlyUsed.next = myNode;
        container.put(key, myNode);
        mostRecentlyUsed = myNode;


        if (currentSize == capacity) {
            container.remove(leastRecentlyUsed.key);
            leastRecentlyUsed = leastRecentlyUsed.next;
            leastRecentlyUsed.previous = null;
        }


        else if (currentSize < capacity) {
            if (currentSize == 0) {
                leastRecentlyUsed = myNode;
            }
            currentSize++;
        }
    }


    public void clear(){
        container.clear();
    }

}

