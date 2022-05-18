package gh2;

/* Imports the required audio library from the
 * edu.princeton.cs.introcs package. */
import edu.princeton.cs.introcs.StdAudio;

import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the GuitarString class.
 *  @author Josh Hug
 */
public class TestGuitarString  {
    public class Animal {
        int speak(Dog a) { return 1; }
        int speak(Animal a) { return 2; }
    }
    public class Dog extends Animal {
        int speak(Animal a) { return 3; }
    }
    public class Poodle extends Dog {
        int speak(Dog a) { return 4; }

    }

    @Test
    public void testPluckTheAString() {
        Animal a =new Poodle();
        a.speak(new Animal());

    }

    @Test
    public void testSample() {
        GuitarString s = new GuitarString(100);
        assertEquals(0.0, s.sample(), 0.0);
        assertEquals(0.0, s.sample(), 0.0);
        assertEquals(0.0, s.sample(), 0.0);
        s.pluck();
        double sample = s.sample();
        assertNotEquals("After plucking, your samples should not be 0.", 0.0, sample);

        assertEquals("Sample should not change the state of your string.", sample, s.sample(), 0.0);
        assertEquals("Sample should not change the state of your string.", sample, s.sample(), 0.0);
    }


    @Test
    public void testTic() {
        GuitarString s = new GuitarString(100);
        assertEquals(0.0, s.sample(), 0.0);
        assertEquals(0.0, s.sample(), 0.0);
        assertEquals(0.0, s.sample(), 0.0);
        s.pluck();
        double sample1 = s.sample();
        assertNotEquals("After plucking, your samples should not be 0.", 0.0, sample1);

        s.tic();
        assertNotEquals("After tic(), your samples should not stay the same.", sample1, s.sample());
    }


    @Test
    public void testTicCalculations() {
        // Create a GuitarString of frequency 11025, which
        // is a Deque of length 4. 
        GuitarString s = new GuitarString(11025);
        s.pluck();

        // Record the front four values, ticcing as we go.
        double s1 = s.sample();
        s.tic();
        double s2 = s.sample();
        s.tic(); 
        double s3 = s.sample();
        s.tic();
        double s4 = s.sample();

        // If we tic once more, it should be equal to 0.996*0.5*(s1 + s2)
        s.tic();

        double s5 = s.sample();
        double expected = 0.996 * 0.5 * (s1 + s2);

        // Check that new sample is correct, using tolerance of 0.001.
        // See JUnit documentation for a description of how tolerances work
        // for assertEquals(double, double)
        assertEquals("Wrong tic value. Try running the testTic method.", expected, s5, 0.001);
    }
}

