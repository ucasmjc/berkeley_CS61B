package deque;
import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> implements Iterable<T> {
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;
    private int capture;
    public T[] sorted;
    private int last;
    private int first;
    private Comparator<T> compear;

    public int size() {
        return size;
    }

    public MaxArrayDeque(Comparator<T> c) {
        items = (T[]) new Object[8];
        size = 0;
        nextfirst = 7;
        nextlast = 0;
        capture = 8;
        compear = c;
    }

    public T max() {
        if (size == 0) {
            return null;
        }
        int index = 0;
        for (int i = 0; i < size - 1; i++) {
            if (compear.compare(get(i), get(index)) >= 0) {
                index = i;
            }
        }
        return sorted[index];
    }

    public T max(Comparator<T> c) {
        if (size == 0) {
            return null;
        }
        int index = 0;
        for (int i = 0; i < size - 1; i++) {
            if (c.compare(get(i), get(index)) >= 0) {
                index = i;
            }
        }
        return sorted[index];
    }

    private void mask() {
        if (nextlast == 0) {
            last = capture - 1;
        } else {
            last = nextlast - 1;
        }
        if (nextfirst == capture - 1) {
            first = 0;
        } else {
            first = nextfirst + 1;
        }
    }

    public void sort() {
        T[] a = (T[]) new Object[capture];
        mask();
        if (size == 0) {
            return;
        }
        if (last > first) {
            System.arraycopy(items, first, a, 0, size);
        } else {
            System.arraycopy(items, first, a, 0, capture - first);
            System.arraycopy(items, 0, a, capture - first, last + 1);
        }
        this.sorted = a;
    }

    private void trick(double x) {
        int cap = (int) Math.round(x);
        T[] a = (T[]) new Object[cap];
        sort();
        System.arraycopy(sorted, 0, a, 0, size);
        nextfirst = cap - 1;
        nextlast = size;
        items = a;
        capture = cap;
    }

    private void resize() {
        if (nextfirst == nextlast) {
            trick(capture * 1.5);
            sorted = null;
        }
        if (size >= 16) {
            if (((nextfirst > nextlast) && ((nextfirst - nextlast + 1) / capture > 0.7)) || ((nextfirst < nextlast) && ((nextlast - nextfirst) / capture < 0.3))) {
                trick(0.6 * capture);
                sorted = null;
            }
        }
    }

    public void addFirst(T x) {
        items[nextfirst] = x;
        size++;
        if (nextfirst == 0) {
            nextfirst = capture - 1;
        } else {
            nextfirst--;
        }
        resize();
        sorted = null;
    }

    public void addLast(T x) {
        items[nextlast] = x;
        size++;
        if (nextlast == capture - 1) {
            nextlast = 0;
        } else {
            nextlast++;
        }
        resize();
        ;sorted = null;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public void printDeque() {
        if (size > 0) {
            mask();
            if (last > first) {
                for (int i = first; i <= last; i++) {
                    System.out.print(items[i]);
                    System.out.print(' ');
                }
            } else {
                for (int i = first; i < capture; i++) {
                    System.out.print(items[i]);
                    System.out.print(' ');
                }
                for (int i = 0; i <= last; i++) {
                    System.out.print(items[i]);
                    System.out.print(' ');
                }
            }

        }
        System.out.println();

    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        mask();
        nextfirst = first;
        T leo = items[first];
        items[first] = null;
        size--;
        resize();
        sorted = null;
        return leo;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        mask();
        nextlast = last;
        T leo = items[last];
        items[last] = null;
        size--;
        resize();
        sorted = null;
        return leo;
    }

    public T get(int index) {
        if (index > size - 1) {
            return null;
        }
        int ma = nextfirst + 1 + index;
        if (ma >= capture) {
            return items[ma - capture];
        } else {
            return items[ma];
        }
    }

    private class ArrayIterator implements Iterator<T> {
        private int mark;
        private T[] ite;

        public ArrayIterator() {
            mark = 0;
            sort();
            ite = sorted;
        }

        public boolean hasNext() {
            return mark < size;
        }

        public T next() {
            mark += 1;
            return ite[mark - 1];
        }
    }

    public Iterator<T> iterator() {
        return new MaxArrayDeque.ArrayIterator();
    }

    public boolean equals(Object o) {
        if (o instanceof Deque) {
            Deque p = (Deque) o;
            if (p.size() != size) {
                return false;
            }
            if (size == 0) {
                return true;
            }
            for (int i = 0; i < size; i++) {
                if (!get(i).equals(p.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;

    }
}
