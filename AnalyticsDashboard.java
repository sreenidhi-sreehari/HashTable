class VisitorNode {
    String userId;
    VisitorNode next;

    VisitorNode(String id) {
        userId = id;
        next = null;
    }
}

class PageNode {
    String url;
    int views;
    int uniqueVisitors;

    VisitorNode visitors;
    PageNode next;

    PageNode(String u) {
        url = u;
        views = 0;
        uniqueVisitors = 0;
        visitors = null;
        next = null;
    }

    boolean hasVisitor(String userId) {

        VisitorNode v = visitors;

        while (v != null) {
            if (v.userId.equals(userId))
                return true;
            v = v.next;
        }

        return false;
    }

    void addVisitor(String userId) {

        if (!hasVisitor(userId)) {
            VisitorNode node = new VisitorNode(userId);
            node.next = visitors;
            visitors = node;
            uniqueVisitors++;
        }
    }
}

class SourceNode {
    String source;
    int count;
    SourceNode next;

    SourceNode(String s) {
        source = s;
        count = 0;
        next = null;
    }
}

class HashTablePages {

    int size = 1009;
    PageNode[] table;

    HashTablePages() {
        table = new PageNode[size];
    }

    int hash(String key) {

        int h = 0;

        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % size;
        }

        return h;
    }

    PageNode getPage(String url) {

        int index = hash(url);
        PageNode node = table[index];

        while (node != null) {
            if (node.url.equals(url))
                return node;
            node = node.next;
        }

        return null;
    }

    PageNode createPage(String url) {

        int index = hash(url);

        PageNode node = new PageNode(url);
        node.next = table[index];
        table[index] = node;

        return node;
    }
}

class HashTableSources {

    int size = 101;
    SourceNode[] table;

    HashTableSources() {
        table = new SourceNode[size];
    }

    int hash(String key) {

        int h = 0;

        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % size;
        }

        return h;
    }

    void addSource(String source) {

        int index = hash(source);

        SourceNode node = table[index];

        while (node != null) {

            if (node.source.equals(source)) {
                node.count++;
                return;
            }

            node = node.next;
        }

        SourceNode newNode = new SourceNode(source);
        newNode.count = 1;

        newNode.next = table[index];
        table[index] = newNode;
    }

    void printSources() {

        int total = 0;

        for (int i = 0; i < size; i++) {

            SourceNode n = table[i];

            while (n != null) {
                total += n.count;
                n = n.next;
            }
        }

        for (int i = 0; i < size; i++) {

            SourceNode n = table[i];

            while (n != null) {

                double percent = (n.count * 100.0) / total;

                System.out.println(n.source + ": " + percent + "%");

                n = n.next;
            }
        }
    }
}

public class AnalyticsDashboard {

    static HashTablePages pages = new HashTablePages();
    static HashTableSources sources = new HashTableSources();

    static void processEvent(String url, String userId, String source) {

        PageNode page = pages.getPage(url);

        if (page == null)
            page = pages.createPage(url);

        page.views++;

        page.addVisitor(userId);

        sources.addSource(source);
    }

    static void getDashboard() {

        PageNode[] top = new PageNode[10];

        for (int i = 0; i < pages.size; i++) {

            PageNode node = pages.table[i];

            while (node != null) {

                for (int j = 0; j < 10; j++) {

                    if (top[j] == null || node.views > top[j].views) {

                        for (int k = 9; k > j; k--) {
                            top[k] = top[k - 1];
                        }

                        top[j] = node;
                        break;
                    }
                }

                node = node.next;
            }
        }

        System.out.println("Top Pages:");

        for (int i = 0; i < 10; i++) {

            if (top[i] != null) {

                System.out.println(
                        (i + 1) + ". " +
                        top[i].url + " - " +
                        top[i].views + " views (" +
                        top[i].uniqueVisitors + " unique)");
            }
        }

        System.out.println("\nTraffic Sources:");

        sources.printSources();
    }

    public static void main(String[] args) {

        processEvent("/article/breaking-news", "user_123", "google");
        processEvent("/article/breaking-news", "user_456", "facebook");
        processEvent("/sports/championship", "user_111", "google");
        processEvent("/sports/championship", "user_222", "direct");
        processEvent("/article/breaking-news", "user_123", "google");

        getDashboard();
    }
}