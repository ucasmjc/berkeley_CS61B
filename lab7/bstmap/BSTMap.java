package bstmap;
import java.lang.UnsupportedOperationException;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

import edu.princeton.cs.algs4.BST;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K,V> {
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
        size = 0;
    }

    private BSTNode put(BSTNode x, K k, V v) {
        if (x == null) {
            return new BSTNode(k, v);
        }
        else {
            int m = k.compareTo(x.key);
            if (m < 0) {
                x.left = put(x.left, k, v);
            } else if (m > 0) {
                x.right = put(x.right, k, v);
            } else {
                x.value = v;
            }
        }
        return x;
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
            if (root.right != null) {
                BSTNode x = root.right;
                while (x.left != null) {
                    x = x.left;
                }
                x.left = root.left;
                root = root.right;
            } else {
                root = root.left;
            }
            size--;
            return val;
        }
        BSTNode parent = null;
        BSTNode x = root;
        while (x.key.compareTo(key) != 0) {
            parent = x;
            if (x.key.compareTo(key) < 0) {
                x = x.right;
            } else {
                x = x.left;
            }
            if (x == null) {
                return null;
            }
        }
        BSTNode right = x.right;
        BSTNode left = x.left;
        V value = x.value;
        if (parent.key.compareTo(key) < 0) {
            if (right != null) {
                parent.right = right;
                x = parent.right;
                while (x.left != null) {
                    x = x.left;
                }
                x.left = left;
            } else {
                parent.right = left;
            }
        } else {
            if (left != null) {
                parent.left = left;
                x = parent.left;
                while (x.right != null) {
                    x = x.left;
                }
                x.left = left;
            } else {
                parent.right = left;
            }
        }
        size--;
        return value;
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
        Set<K> set = new HashSet<>();
        set = getSet(set, root);
        return set;
    }
    public void printInOrder() {

    }
}
