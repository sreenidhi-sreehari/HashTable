class TrieNode {

    char ch;
    int frequency;
    boolean isEnd;

    TrieNode[] children = new TrieNode[26];

    TrieNode() {
        isEnd = false;
        frequency = 0;
    }
}

class Trie {

    TrieNode root = new TrieNode();

    void insert(String word) {

        TrieNode node = root;

        for (int i = 0; i < word.length(); i++) {

            char c = word.charAt(i);

            if (c < 'a' || c > 'z')
                continue;

            int index = c - 'a';

            if (node.children[index] == null)
                node.children[index] = new TrieNode();

            node = node.children[index];
        }

        node.isEnd = true;
        node.frequency++;
    }

    TrieNode findPrefix(String prefix) {

        TrieNode node = root;

        for (int i = 0; i < prefix.length(); i++) {

            char c = prefix.charAt(i);

            if (c < 'a' || c > 'z')
                continue;

            int index = c - 'a';

            if (node.children[index] == null)
                return null;

            node = node.children[index];
        }

        return node;
    }

    void collectWords(TrieNode node, String prefix, String[] words, int[] freq, int[] count) {

        if (node == null)
            return;

        if (node.isEnd) {

            words[count[0]] = prefix;
            freq[count[0]] = node.frequency;

            count[0]++;
        }

        for (int i = 0; i < 26; i++) {

            if (node.children[i] != null) {

                char c = (char) ('a' + i);

                collectWords(node.children[i], prefix + c, words, freq, count);
            }
        }
    }
}

public class AutocompleteSystem {

    static Trie trie = new Trie();

    static void updateFrequency(String query) {

        trie.insert(query);

        System.out.println("Frequency updated for: " + query);
    }

    static void search(String prefix) {

        TrieNode node = trie.findPrefix(prefix);

        if (node == null) {
            System.out.println("No suggestions found");
            return;
        }

        String[] words = new String[1000];
        int[] freq = new int[1000];
        int[] count = new int[1];

        trie.collectWords(node, prefix, words, freq, count);

        for (int i = 0; i < count[0]; i++) {

            for (int j = i + 1; j < count[0]; j++) {

                if (freq[j] > freq[i]) {

                    int temp = freq[i];
                    freq[i] = freq[j];
                    freq[j] = temp;

                    String t = words[i];
                    words[i] = words[j];
                    words[j] = t;
                }
            }
        }

        System.out.println("Top Suggestions:");

        for (int i = 0; i < 10 && i < count[0]; i++) {

            System.out.println(
                    (i + 1) + ". " +
                    words[i] +
                    " (" +
                    freq[i] +
                    " searches)");
        }
    }

    public static void main(String[] args) {

        updateFrequency("java tutorial");
        updateFrequency("javascript");
        updateFrequency("java download");
        updateFrequency("java tutorial");
        updateFrequency("java features");

        search("jav");
    }
}