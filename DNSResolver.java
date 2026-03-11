class DNSEntry {
    String domain;
    String ip;
    long expiry;
    long lastAccess;

    DNSEntry next; 
    DNSEntry prevLRU;
    DNSEntry nextLRU;

    DNSEntry(String d, String ip, long ttl) {
        this.domain = d;
        this.ip = ip;
        long now = System.currentTimeMillis();
        this.expiry = now + ttl * 1000;
        this.lastAccess = now;
    }
}

class DNSCache {

    int size = 101;
    DNSEntry[] table;

    DNSEntry head;
    DNSEntry tail;

    int capacity = 10;
    int count = 0;

    int hits = 0;
    int misses = 0;

    DNSCache() {
        table = new DNSEntry[size];
    }

    int hash(String key) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % size;
        }
        return h;
    }

    DNSEntry find(String domain) {
        int index = hash(domain);
        DNSEntry node = table[index];

        while (node != null) {
            if (node.domain.equals(domain)) {
                return node;
            }
            node = node.next;
        }

        return null;
    }

    void put(String domain, String ip, long ttl) {

        if (count >= capacity) {
            removeLRU();
        }

        DNSEntry entry = new DNSEntry(domain, ip, ttl);

        int index = hash(domain);

        entry.next = table[index];
        table[index] = entry;

        addLRU(entry);

        count++;
    }

    void addLRU(DNSEntry node) {

        node.nextLRU = head;
        node.prevLRU = null;

        if (head != null) {
            head.prevLRU = node;
        }

        head = node;

        if (tail == null) {
            tail = node;
        }
    }

    void moveToFront(DNSEntry node) {

        if (node == head) return;

        if (node.prevLRU != null) {
            node.prevLRU.nextLRU = node.nextLRU;
        }

        if (node.nextLRU != null) {
            node.nextLRU.prevLRU = node.prevLRU;
        }

        if (node == tail) {
            tail = node.prevLRU;
        }

        node.nextLRU = head;
        node.prevLRU = null;

        if (head != null) {
            head.prevLRU = node;
        }

        head = node;
    }

    void removeLRU() {

        if (tail == null) return;

        int index = hash(tail.domain);

        DNSEntry node = table[index];
        DNSEntry prev = null;

        while (node != null) {
            if (node.domain.equals(tail.domain)) {
                if (prev == null) {
                    table[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                break;
            }
            prev = node;
            node = node.next;
        }

        tail = tail.prevLRU;

        if (tail != null) {
            tail.nextLRU = null;
        }

        count--;
    }

    String queryUpstream(String domain) {

        int num = (int)(Math.random() * 200);
        return "172.217.14." + num;
    }

    String resolve(String domain) {

        long now = System.currentTimeMillis();

        DNSEntry entry = find(domain);

        if (entry != null) {

            if (entry.expiry > now) {
                hits++;
                entry.lastAccess = now;
                moveToFront(entry);
                return "Cache HIT → " + entry.ip;
            } else {
                misses++;
                entry.ip = queryUpstream(domain);
                entry.expiry = now + 300 * 1000;
                moveToFront(entry);
                return "Cache EXPIRED → " + entry.ip;
            }
        }

        misses++;

        String ip = queryUpstream(domain);
        put(domain, ip, 300);

        return "Cache MISS → " + ip;
    }

    void getCacheStats() {

        int total = hits + misses;

        double rate = 0;

        if (total != 0) {
            rate = (hits * 100.0) / total;
        }

        System.out.println("Hit Rate: " + rate + "%");
    }
}

public class DNSResolver {

    public static void main(String[] args) throws Exception {

        DNSCache cache = new DNSCache();

        System.out.println(cache.resolve("google.com"));
        System.out.println(cache.resolve("google.com"));

        Thread.sleep(2000);

        System.out.println(cache.resolve("openai.com"));

        cache.getCacheStats();
    }
}