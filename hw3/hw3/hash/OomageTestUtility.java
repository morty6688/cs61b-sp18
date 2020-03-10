package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int[] nums = new int[M];
        for (Oomage o : oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            nums[bucketNum]++;
        }
        for (int num : nums) {
            if (num > oomages.size() / 2.5 || num < oomages.size() / 50.0) {
                return false;
            }
        }
        return true;
    }
}
