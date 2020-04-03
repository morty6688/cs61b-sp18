import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    static class TrieNode {
        private Map<Character, TrieNode> children;
        private List<String> names;
        private boolean isWord;

        public TrieNode() {
            this.children = new HashMap<>();
            this.names = new ArrayList<>();
        }

        public Map<Character, TrieNode> getChildren() {
            return children;
        }

        public List<String> getNames() {
            return names;
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
            if (!cur.names.contains(name)) {
                cur.names.add(name);
            }
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
