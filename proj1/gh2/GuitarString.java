package gh2;

import deque.ArrayDeque;
import deque.Deque;


//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */

    private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int x = (int) Math.round(SR / frequency);
        buffer = new ArrayDeque<Double>();
        for (int i = 0; i < x; i++) {
            buffer.addLast(0.0);
        }

    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int mark = 0;
        boolean test = true;
        int size = buffer.size();
        double rand;
        double[] used = new double[size];
        for (int i = 0; i < size; i++) {
            test = true;
            while (test) {
                rand = Math.random() - 0.5;
                for (int j = 0; j < mark; j++) {
                    if (i == rand) {
                        break;
                    }
                }
                used[mark] = rand;
                mark++;
                buffer.addLast(rand);
                test = false;
            }
            buffer.removeFirst();
        }

    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double x = buffer.removeFirst();
        buffer.addLast((x + buffer.get(0)) / 2 * DECAY);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        double x = buffer.get(0);
        return x;
    }
}

