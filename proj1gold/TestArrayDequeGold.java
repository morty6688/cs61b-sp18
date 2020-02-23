import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void test() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();

        StringBuilder msg = new StringBuilder();

        int s = 0;
        for (int i = 0; i < 500; i++) {
            if (i % 5 == 0) {
                msg.append("size()\n");
                assertEquals(msg.toString(), ads.size(), sad.size());
            }

            double selector = StdRandom.uniform();
            if (selector < 0.25) {
                sad.addFirst(i);
                ads.addFirst(i);
                s++;
                msg.append("addFirst(" + i + ")\n");
                assertEquals(msg.toString(), ads.get(0), sad.get(0));
            } else if (selector < 0.5) {
                sad.addLast(i);
                ads.addLast(i);
                s++;
                msg.append("addLast(" + i + ")\n");
                assertEquals(msg.toString(), ads.get(s - 1), sad.get(s - 1));
            } else if (selector < 0.75) {
                if (ads.isEmpty()) {
                    msg.append("isEmpty()\n");
                    assertTrue(msg.toString(), sad.isEmpty());
                    continue;
                }
                Integer x = ads.removeFirst();
                Integer y = sad.removeFirst();
                s--;
                msg.append("removeFirst()\n");
                assertEquals(msg.toString(), x, y);
            } else {
                if (ads.isEmpty()) {
                    msg.append("isEmpty()\n");
                    assertTrue(msg.toString(), sad.isEmpty());
                    continue;
                }
                Integer x = ads.removeLast();
                Integer y = sad.removeLast();
                s--;
                msg.append("removeLast()\n");
                assertEquals(msg.toString(), x, y);
            }
        }
    }
}
