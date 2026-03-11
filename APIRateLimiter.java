class TokenBucket {

    int tokens;
    int maxTokens;
    int refillRate;
    long lastRefillTime;

    TokenBucket(int max, int rate) {
        maxTokens = max;
        refillRate = rate;
        tokens = max;
        lastRefillTime = System.currentTimeMillis();
    }

    void refill() {

        long now = System.currentTimeMillis();

        long seconds = (now - lastRefillTime) / 1000;

        int tokensToAdd = (int) (seconds * refillRate);

        if (tokensToAdd > 0) {

            tokens = tokens + tokensToAdd;

            if (tokens > maxTokens)
                tokens = maxTokens;

            lastRefillTime = now;
        }
    }

    boolean allowRequest() {

        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    int remaining() {
        return tokens;
    }
}

class ClientNode {

    String clientId;
    TokenBucket bucket;
    ClientNode next;

    ClientNode(String id, TokenBucket b) {
        clientId = id;
        bucket = b;
        next = null;
    }
}

class RateLimiterTable {

    int size = 10007;

    ClientNode[] table;

    RateLimiterTable() {
        table = new ClientNode[size];
    }

    int hash(String key) {

        int h = 0;

        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % size;
        }

        return h;
    }

    TokenBucket getBucket(String clientId) {

        int index = hash(clientId);

        ClientNode node = table[index];

        while (node != null) {

            if (node.clientId.equals(clientId))
                return node.bucket;

            node = node.next;
        }

        return null;
    }

    TokenBucket createBucket(String clientId) {

        int index = hash(clientId);

        TokenBucket bucket = new TokenBucket(1000, 1000 / 3600);

        ClientNode node = new ClientNode(clientId, bucket);

        node.next = table[index];

        table[index] = node;

        return bucket;
    }
}

public class APIRateLimiter {

    static RateLimiterTable clients = new RateLimiterTable();

    static void checkRateLimit(String clientId) {

        TokenBucket bucket = clients.getBucket(clientId);

        if (bucket == null)
            bucket = clients.createBucket(clientId);

        synchronized (bucket) {

            if (bucket.allowRequest()) {

                System.out.println(
                        "Allowed (" +
                                bucket.remaining() +
                                " requests remaining)");
            } else {

                System.out.println(
                        "Denied (0 requests remaining, retry later)");
            }
        }
    }

    static void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.getBucket(clientId);

        if (bucket == null) {
            System.out.println("No usage yet");
            return;
        }

        int used = bucket.maxTokens - bucket.tokens;

        System.out.println(
                "{used: " +
                        used +
                        ", limit: " +
                        bucket.maxTokens +
                        "}");
    }

    public static void main(String[] args) {

        checkRateLimit("abc123");
        checkRateLimit("abc123");
        checkRateLimit("abc123");

        getRateLimitStatus("abc123");
    }
}