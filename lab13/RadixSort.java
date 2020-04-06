import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxLen = 0;
        for (String s : asciis) {
            if (s.length() > maxLen) {
                maxLen = s.length();
            }
        }
        String[] res = Arrays.copyOf(asciis, asciis.length);
        // int[] num = new int[asciis.length];
        Map<String, Integer> m = new HashMap<>();
        for (int i = 0; i < res.length; i++) {
            String s = res[i];
            if (s.length() < maxLen) {
                // num[i] = maxLen - s.length();
                int num = maxLen - s.length();
                res[i] = addSth(s, num);
                m.put(res[i], num);
            } else {
                m.put(res[i], 0);
            }
        }

        for (int i = maxLen - 1; i >= 0; i--) {
            sortHelperLSD(res, i);
        }

        for (int i = 0; i < res.length; i++) {
            String s = res[i];
            res[i] = removeSth(s, m.get(s));
        }
        return res;
    }

    private static String removeSth(String s, int num) {
        if (num == 0) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s);
        for (int j = 0; j < num; j++) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private static String addSth(String s, int num) {
        StringBuilder sb = new StringBuilder(s);
        for (int j = 0; j < num; j++) {
            sb.append((char) 0);
        }
        return sb.toString();
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int[] counts = new int[256];
        for (String item : asciis) {
            char c = item.charAt(index);
            counts[c]++;
        }

        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < 256; i++) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            String item = asciis[i];
            char c = item.charAt(index);
            int place = starts[c];
            sorted[place] = item;
            starts[c]++;
        }

        for (int i = 0; i < asciis.length; i++) {
            asciis[i] = sorted[i];
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        // String[] asciis = new String[] { "356", "112", "904", "294", "209", "820", "394", "810" };
        // // RadixSort.sortHelperLSD(asciis, 0);
        // // for (String s : asciis) {
        // //     System.out.print(s + " ");
        // // }

        // String[] res = RadixSort.sort(asciis);
        // for (String s : res) {
        //     System.out.print(s + " ");
        // }

        System.out.println();

        String[] asciis2 = new String[] { "56", "112", "94", "4", "9", "82", "394", "80" };
        String[] res2 = RadixSort.sort(asciis2);
        for (String s : res2) {
            System.out.print(s + " ");
        }
    }
}
