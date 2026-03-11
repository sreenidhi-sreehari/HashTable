class CacheNode {

    String videoId;
    String data;

    CacheNode prev;
    CacheNode next;

    CacheNode(String id, String d) {
        videoId = id;
        data = d;
    }
}

class CacheLevel {

    int capacity;
    int size = 0;

    CacheNode[] table;
    int tableSize = 200003;

    CacheNode head;
    CacheNode tail;

    int hits = 0;
    int misses = 0;

    CacheLevel(int cap) {
        capacity = cap;
        table = new CacheNode[tableSize];
    }

    int hash(String key) {

        int h = 0;

        for (int i = 0; i < key.length(); i++)
            h = (h * 31 + key.charAt(i)) % tableSize;

        return h;
    }

    CacheNode find(String key) {

        int index = hash(key);

        CacheNode node = table[index];

        while (node != null) {

            if (node.videoId.equals(key))
                return node;

            node = node.next;
        }

        return null;
    }

    void moveToFront(CacheNode node) {

        if (node == head)
            return;

        if (node.prev != null)
            node.prev.next = node.next;

        if (node.next != null)
            node.next.prev = node.prev;

        if (node == tail)
            tail = node.prev;

        node.prev = null;
        node.next = head;

        if (head != null)
            head.prev = node;

        head = node;

        if (tail == null)
            tail = node;
    }

    void insert(String id, String data) {

        if (size >= capacity)
            evict();

        CacheNode node = new CacheNode(id, data);

        int index = hash(id);

        node.next = table[index];
        table[index] = node;

        node.prev = null;
        node.next = head;

        if (head != null)
            head.prev = node;

        head = node;

        if (tail == null)
            tail = node;

        size++;
    }

    void evict() {

        if (tail == null)
            return;

        int index = hash(tail.videoId);

        CacheNode node = table[index];
        CacheNode prev = null;

        while (node != null) {

            if (node.videoId.equals(tail.videoId)) {

                if (prev == null)
                    table[index] = node.next;
                else
                    prev.next = node.next;

                break;
            }

            prev = node;
            node = node.next;
        }

        tail = tail.prev;

        if (tail != null)
            tail.next = null;

        size--;
    }

    String get(String id) {

        CacheNode node = find(id);

        if (node == null) {
            misses++;
            return null;
        }

        hits++;

        moveToFront(node);

        return node.data;
    }
}

public class MultiLevelCache {

    static CacheLevel L1 = new CacheLevel(10000);
    static CacheLevel L2 = new CacheLevel(100000);

    static String database(String videoId) {

        return "VideoData_" + videoId;
    }

    static String getVideo(String videoId) {

        String data = L1.get(videoId);

        if (data != null) {

            System.out.println("L1 Cache HIT");
            return data;
        }

        System.out.println("L1 Cache MISS");

        data = L2.get(videoId);

        if (data != null) {

            System.out.println("L2 Cache HIT → promoted to L1");

            L1.insert(videoId, data);

            return data;
        }

        System.out.println("L2 Cache MISS");

        data = database(videoId);

        System.out.println("L3 Database HIT");

        L2.insert(videoId, data);

        return data;
    }

    static void invalidate(String videoId) {

        L1.get(videoId);
        L2.get(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    static void getStatistics() {

        int total1 = L1.hits + L1.misses;
        int total2 = L2.hits + L2.misses;

        double hitRate1 = total1 == 0 ? 0 : (L1.hits * 100.0) / total1;
        double hitRate2 = total2 == 0 ? 0 : (L2.hits * 100.0) / total2;

        System.out.println("L1 Hit Rate: " + hitRate1 + "%");
        System.out.println("L2 Hit Rate: " + hitRate2 + "%");
    }

    public static void main(String[] args) {

        getVideo("video_123");

        getVideo("video_123");

        getVideo("video_999");

        getStatistics();
    }
}