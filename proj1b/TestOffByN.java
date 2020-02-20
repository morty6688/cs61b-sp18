import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    @Test
    public void testOffBy5() {
        OffByN offBy5 = new OffByN(5);
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('f', 'a'));
        assertFalse(offBy5.equalChars('f', 'h'));
        assertFalse(offBy5.equalChars('f', 'f'));
    }

    @Test
    public void testOffBy1() {
        OffByN offBy1 = new OffByN(1);
        assertTrue(offBy1.equalChars('a', 'b'));
        assertTrue(offBy1.equalChars('r', 'q'));
        assertTrue(offBy1.equalChars('&', '%'));
        assertFalse(offBy1.equalChars('a', 'e'));
        assertFalse(offBy1.equalChars('z', 'a'));
        assertFalse(offBy1.equalChars('a', 'a'));
    }

    @Test
    public void testOffBy0() {
        OffByN offBy0 = new OffByN(0);
        assertTrue(offBy0.equalChars('a', 'a'));
        assertFalse(offBy0.equalChars('a', 'b'));
    }
}
