package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple1() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesSimple2() {
        IntList lst = IntList.of(5, 7, 13, 17, 17);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("25 -> 49 -> 169 -> 289 -> 289", lst.toString());
        assertTrue(changed);
    }
}
