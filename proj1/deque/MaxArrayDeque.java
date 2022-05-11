package deque;
import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> compear;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        compear = c;
    }
    public T max() {
        if (size() == 0) {
            return null;
        }
        int index = 0;
        for (int i = 0; i < size() - 1; i++) {
            if (compear.compare(get(i), get(index)) >= 0) {
                index = i;
            }
        }
        return get(index);
    }

    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        int index = 0;
        for (int i = 0; i < size() - 1; i++) {
            if (c.compare(get(i), get(index)) >= 0) {
                index = i;
            }
        }
        return get(index);
    }
}
