public class ArrayDequeTest {
    /* Utility method for printing out empty checks. */
    public static boolean checkEmpty(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /* Utility method for printing out empty checks. */
    public static <T> boolean checkSize(T expected, T actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    /*
     * Prints a nice message based on whether a test passed. The \n means newline.
     */
    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    public static void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");

        ArrayDeque<String> ad1 = new ArrayDeque<>();
        boolean passed = checkEmpty(true, ad1.isEmpty());
        ad1.addFirst("front");
        passed = checkSize(1, ad1.size()) && passed;
        ad1.addLast("middle");
        passed = checkSize(2, ad1.size()) && passed;

        ad1.addLast("back");
        passed = checkSize(3, ad1.size()) && passed;

        passed = checkSize("front", ad1.get(0)) && passed;
        passed = checkSize("middle", ad1.get(1)) && passed;
        passed = checkSize("back", ad1.get(2)) && passed;

        passed = checkSize(null, ad1.get(3)) && passed;

        System.out.println("Printing out deque: ");
        ad1.printDeque();

        printTestStatus(passed);
    }

    public static void addRemoveTest() {

        System.out.println("Running add/remove test.");

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // should be empty
        boolean passed = checkEmpty(true, ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        passed = checkEmpty(false, ad1.isEmpty()) && passed;

        ad1.removeFirst();
        // should be empty
        passed = checkEmpty(true, ad1.isEmpty()) && passed;

        printTestStatus(passed);

    }

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        // addIsEmptySizeTest();
        // addRemoveTest();
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        int s = 0;
        for (int i = 0; i < 500; i++) {
            int flag = (int) (4 * Math.random());
            switch (flag) {
                case 0:
                    ad.addFirst(i);
                    s++;
                    break;
                case 1:
                    ad.addLast(i);
                    s++;
                    break;
                case 2:
                    Integer res = ad.removeFirst();
                    if (res != null) {
                        s--;
                    }
                    break;
                default:
                    Integer res2 = ad.removeLast();
                    if (res2 != null) {
                        s--;
                    }
            }
            if (s == 0) {
                System.out.println(ad.isEmpty());
            }
        }

    }
}
