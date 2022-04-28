package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */

public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing a = new AListNoResizing();
        BuggyAList b = new BuggyAList();
        a.addLast(4);
        a.addLast(5);
        a.addLast(6);
        b.addLast(4);
        b.addLast(5);
        b.addLast(6);
        assertEquals(a.size(), b.size());
        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> b = new BuggyAList<Integer>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                b.addLast(randVal);


            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size1 = b.size();

            } else if (operationNumber == 2) {
                if (L.size() > 0) {
                    int cat = L.getLast();
                    int cat1 = b.getLast();


                } else if (operationNumber == 3) {
                    if (L.size() > 0) {
                        int dog = L.removeLast();
                        int dog1 = b.removeLast();

                    }
                }
            }
            // YOUR TESTS HERE
        }
    }
}
