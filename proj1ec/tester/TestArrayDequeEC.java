package tester;
import static org.junit.Assert.*;
import student.StudentArrayDeque;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestArrayDequeEC {
    @Test
    public void randomizedTest() {
        StudentArrayDeque<Integer> L = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> b = new ArrayDequeSolution<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                b.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size1 = b.size();
                assertEquals(size,size1);
            } else if (operationNumber == 2) {
                //addFirst
                if (L.size() > 0) {
                    int randVal = StdRandom.uniform(0, 100);
                    L.addFirst(randVal);
                    b.addFirst(randVal);
                }
                } else if (operationNumber == 3) {
                    if (L.size() > 0) {
                        assertEquals(L.removeLast(),b.removeLast());
                    }
                }
            else if(operationNumber==4){
                if(L.size()>0){
                    assertEquals(L.removeFirst(),b.removeFirst());
                }

            }
            else if(operationNumber==5){
                if (L.size() > 0) {
                int randVal = StdRandom.uniform(0, L.size());
                assertEquals(L.get(randVal),b.get(randVal));
            }
            }
            // YOUR TESTS HERE
        }
    }
}
