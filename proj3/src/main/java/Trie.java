import java.util.HashMap;
import java.util.Map;

public class Trie {
    static class TrieNode {
        private Map<Character, TrieNode> children;
        private String name;
        private boolean isWord;

        public TrieNode() {
            this.children = new HashMap<>();
        }

        public Map<Character, TrieNode> getChildren() {
            return children;
        }

        public String getName() {
            return name;
        }

        public boolean isWord() {
            return isWord;
        }
    }

    private TrieNode root;

    public void add(String cleanName, String name) {
        root = add(root, cleanName, 0, name);
    }

    private TrieNode add(TrieNode cur, String key, int i, String name) {
        if (cur == null) {
            cur = new TrieNode();
        }
        if (i == key.length()) {
            cur.isWord = true;
            cur.name = name;
            return cur;
        }
        char c = key.charAt(i);
        cur.children.put(c, add(cur.children.get(c), key, i + 1, name));
        return cur;
    }

    public TrieNode findNode(String key) {
        TrieNode cur = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            TrieNode node = cur.children.get(c);
            if (node == null) {
                return null;
            }
            cur = node;
        }
        return cur;
    }
}
