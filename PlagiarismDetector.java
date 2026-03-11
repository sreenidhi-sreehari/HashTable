class GramNode {
    String gram;
    String docId;
    GramNode next;

    GramNode(String g, String d) {
        gram = g;
        docId = d;
        next = null;
    }
}

class HashTable {

    int size = 10007;
    GramNode[] table;

    HashTable() {
        table = new GramNode[size];
    }

    int hash(String key) {
        int h = 0;

        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % size;
        }

        return h;
    }

    void insert(String gram, String docId) {

        int index = hash(gram);

        GramNode node = new GramNode(gram, docId);
        node.next = table[index];
        table[index] = node;
    }

    int countMatches(String gram, String docId) {

        int index = hash(gram);
        GramNode node = table[index];

        int count = 0;

        while (node != null) {

            if (node.gram.equals(gram) && !node.docId.equals(docId)) {
                count++;
            }

            node = node.next;
        }

        return count;
    }
}

public class PlagiarismDetector {

    static HashTable index = new HashTable();
    static int N = 5;

    static String[] splitWords(String text) {

        int spaces = 1;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') spaces++;
        }

        String[] words = new String[spaces];

        int start = 0;
        int w = 0;

        for (int i = 0; i <= text.length(); i++) {

            if (i == text.length() || text.charAt(i) == ' ') {
                words[w++] = text.substring(start, i);
                start = i + 1;
            }
        }

        return words;
    }

    static void addDocument(String docId, String text) {

        String[] words = splitWords(text);

        for (int i = 0; i <= words.length - N; i++) {

            String gram = "";

            for (int j = 0; j < N; j++) {
                gram += words[i + j] + " ";
            }

            index.insert(gram, docId);
        }
    }

    static void analyzeDocument(String docId, String text) {

        String[] words = splitWords(text);

        int total = 0;
        int matches = 0;

        for (int i = 0; i <= words.length - N; i++) {

            String gram = "";

            for (int j = 0; j < N; j++) {
                gram += words[i + j] + " ";
            }

            total++;

            matches += index.countMatches(gram, docId);
        }

        double similarity = 0;

        if (total > 0) {
            similarity = (matches * 100.0) / total;
        }

        System.out.println("Extracted " + total + " n-grams");
        System.out.println("Matching n-grams: " + matches);
        System.out.println("Similarity: " + similarity + "%");

        if (similarity > 60) {
            System.out.println("PLAGIARISM DETECTED");
        }
    }

    public static void main(String[] args) {

        String essay1 = "machine learning is a powerful tool for solving real world problems using data science methods";

        String essay2 = "machine learning is a powerful tool for solving real world problems using artificial intelligence";

        addDocument("essay_089.txt", essay1);

        analyzeDocument("essay_123.txt", essay2);
    }
}