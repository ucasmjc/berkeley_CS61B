package bstmap;
import java.lang.UnsupportedOperationException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Set;

import edu.princeton.cs.algs4.BST;

public class BSTMap<K extends Comparable, V> implements Map61B<K,V>, Iterable<K> {
    private int size;
    private BSTNode root;

    private class BSTNode {
        BSTNode left;
        BSTNode right;
        K key;
        V value;
        public BSTNode (K ke, V val) {
            left = null;
            right = null;
            key = ke;
            value = val;
        }
    }
    public BSTMap() {
        root = null;
        size = 0;
    }
    public void clear() {
        root = null;
    }

    private void put(BSTNode x, K k, V v) {
        if (x == null) {
            x = new BSTNode(k, v);
        }
        else {
            int m = k.compareTo(x.key);
            if (m < 0) {
                put(x.left, k, v);
            } else if (m > 0) {
                put(x.right, k, v);
            } else {
                x.value = v;
            }
        }
    }
    public void put(K k, V v) {
        if (root == null) {
            root = new BSTNode(k, v);
        } else {
            put(root, k, v);
        }
        size++;
    }
    private V get(BSTNode x, K k) {
        V target = null;
        if (x == null) {
            return null;
        }
        if (k.compareTo(x.key) == 0) {
            target = x.value;
        } else if (k.compareTo(x.key) < 0) {
            target = get(x.left, k);
        } else if (k.compareTo(x.key) > 0) {
            target = get(x.right,k);
        }
        return target;
    }

    public V get(K k) {
        return get(root, k);
    }
    public boolean containsKey(K k) {
        boolean mark = false;
        BSTNode x = root;
        while (x != null) {
            if (k.compareTo(x.key) == 0) {
                mark = true;
                break;
            } else if (k.compareTo(x.key) < 0) {
                x = x.left;
            } else if (k.compareTo(x.key) > 0) {
                x = x.right;
            }
        }
        return mark;
    }
    public int size() {
        return size;
    }
    public V remove(K key, V value) {
       if (get(key) == value) {
           return remove(key);
       }
       return null;
    }
    public V remove(K key){
        if (root.key.compareTo(key) == 0) {
            V val = root.value;
            root = null;
            return val;
        }
        BSTNode parent = null;
        BSTNode x = root;
        while (x.key.compareTo(key) != 0) {
            parent = x;
            if (x.key.compareTo(key) < 0) {
                x = x.right;
            } else {
                x = x.right;
            }
        }
        if (parent.key.compareTo(key) < 0) {
            parent.right = x.right;
            parent.right.left = x.left;
        } else {
            parent.left = x.left;
            parent.left.right = x.right;
        }
        return x.value;
    }
    private class KeyIterator implements Iterator<K>{
        Set<K> set;
        K[] keys;
        int mark;
        int size;
        KeyIterator() {
            mark = 0;
            set = keySet();
            keys = (K[])set.toArray();
            size = set.size();
        }
        public boolean hasNext() {
            return mark < size;
        }
        public K next() {
            mark++;
            return keys[mark - 1];
        }
    }
    public Iterator<K> iterator() {
        return new KeyIterator();
    }
    private Set<K> getSet(Set<K> Set, BSTNode x) {
        if (x == null) {
            return Set;
        }
        Set = getSet(Set, x.left);
        Set.add(x.key);
        Set = getSet(Set, x.right);
        return Set;
    }
    public Set<K> keySet() {
        Set<K> set = null;
        set = getSet(set, root);
        return set;
    }
}
