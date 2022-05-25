package hashmap;

import javax.swing.*;
import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private double loadFactor;
    private int size;
    private Set<K> keySet;
    private int load;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = new Collection[16];
        loadFactor = 0.75;
        size = 0;
        load = 16;
        keySet = new HashSet<>();
    }

    public MyHashMap(int initialSize) {
        buckets = new Collection[initialSize];
        loadFactor = 0.75;
        size = 0;
        load = initialSize;
        keySet = new HashSet<>();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = new Collection[initialSize];
        loadFactor = maxLoad;
        size = 0;
        load = initialSize;
        keySet = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key,value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return null;
    }
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    private void resize() {
        if (size / load > loadFactor) {
            int usedload = load;
            load = (int)Math.round(load * 1.5);
            Collection<Node>[] copy = buckets;
            buckets = new Collection[load];
            for (int i = 0; i< usedload; i++) {
                if (copy[i] == null) {
                    continue;
                }
                for (Node n : copy[i]) {
                    int hash = HashCode(n.key);
                    if (buckets[hash] == null) {
                        buckets[hash] = createBucket();
                    }
                    buckets[hash].add(n);
                }
            }
        }
    }
    private int HashCode(K key) {
        int Hash = key.hashCode();
        return Math.floorMod(Hash, load);
    }
    public void put(K key, V value) {
        int hash = HashCode(key);
        if (buckets[hash] == null) {
            buckets[hash] = createBucket();
        }
        Node tar = createNode(key, value);
        if (!containsKey(key)) {
            size++;
            keySet.add(key);
            buckets[hash].add(tar);
        } else {
            for (Node n : buckets[hash]) {
                if (n.key.equals(key)) {
                    n.value = value;
                }
            }
        }
        resize();
    }
    public int size() {
        return size;
    }
    public void clear() {
        buckets = new Collection[load];
        size = 0;
        keySet = new HashSet<>();
    }
    public V get(K key) {
        V val = null;
        if(!containsKey(key)) {
            return null;
        }
        int hash = HashCode(key);
        for (Node n : buckets[hash]) {
            if (n.key.equals(key)) {
                val = n.value;
            }
        }
        return val;
    }
    public Set<K> keySet() {
        return keySet;
    }
    public V remove(K key) {
        int hash = HashCode(key);
        V val = get(key);
        Node x = null;
        for (Node n : buckets[hash]) {
            if (n.key.equals(key)) {
                x = n;
                break;
            }
        }
        if (buckets[hash].remove(x)) {
            size--;
            keySet.remove(key);
            return val;
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        if (get(key).equals(value)) {
            return remove(key);
        }
        return null;
    }

    // Your code won't compile until you do so!

}
