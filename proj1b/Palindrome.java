public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> res = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            res.addLast(word.charAt(i));
        }
        return res;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> d = wordToDeque(word);
        while (d.size() > 1) {
            if (d.removeFirst() != d.removeLast()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        while (d.size() > 1) {
            if (!cc.equalChars(d.removeFirst(), d.removeLast())) {
                return false;
            }
        }
        return true;
    }

    // public boolean isPalindromeRecursive(String word) {
    //     Deque<Character> d = wordToDeque(word);
    //     return isPalindromeRecursive(d);
    // }

    // private boolean isPalindromeRecursive(Deque<Character> d) {
    //     if (d.size() <= 1) {
    //         return true;
    //     }
    //     if (d.removeFirst() != d.removeLast()) {
    //         return false;
    //     }
    //     return isPalindromeRecursive(d);
    // }
}
