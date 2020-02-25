package synthesizer;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests the ArrayRingBuffer class.
 * 
 * @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(5);
        assertTrue(arb.isEmpty());
        for (int i = 0; i < 3; i++) {
            arb.enqueue(i);
        }
        assertEquals(0, (int) arb.peek());
        assertEquals(0, (int) arb.dequeue());
        arb.dequeue();
        for (int i = 303; i < 307; i++) {
            arb.enqueue(i);
        }
        assertTrue(arb.isFull());
        assertEquals(2, (int) arb.dequeue());

        List<Integer> expected = Arrays.asList(303, 304, 305, 306);
        List<Integer> actual = new ArrayList<>();
        for (Integer item : arb) {
            actual.add(item);
        }
        assertEquals(expected, actual);
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
}
