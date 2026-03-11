class ProductNode {
    String productId;
    int stock;
    ProductNode next;

    ProductNode(String productId, int stock) {
        this.productId = productId;
        this.stock = stock;
        this.next = null;
    }
}

class HashTable {
    int size = 1009;
    ProductNode[] table;

    HashTable() {
        table = new ProductNode[size];
    }

    int hash(String key) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % size;
        }
        return h;
    }

    void put(String key, int stock) {
        int index = hash(key);

        ProductNode head = table[index];

        while (head != null) {
            if (head.productId.equals(key)) {
                head.stock = stock;
                return;
            }
            head = head.next;
        }

        ProductNode node = new ProductNode(key, stock);
        node.next = table[index];
        table[index] = node;
    }

    ProductNode getNode(String key) {
        int index = hash(key);
        ProductNode head = table[index];

        while (head != null) {
            if (head.productId.equals(key)) {
                return head;
            }
            head = head.next;
        }

        return null;
    }
}

class WaitNode {
    int userId;
    WaitNode next;

    WaitNode(int userId) {
        this.userId = userId;
        this.next = null;
    }
}

class WaitingQueue {
    WaitNode front;
    WaitNode rear;
    int size = 0;

    void enqueue(int userId) {
        WaitNode node = new WaitNode(userId);

        if (rear == null) {
            front = rear = node;
        } else {
            rear.next = node;
            rear = node;
        }

        size++;
    }

    int getPosition() {
        return size;
    }
}

public class FlashSaleInventory {

    static HashTable inventory = new HashTable();
    static WaitingQueue waitingList = new WaitingQueue();

    static void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
    }

    static void checkStock(String productId) {
        ProductNode p = inventory.getNode(productId);

        if (p == null) {
            System.out.println("Product not found");
            return;
        }

        System.out.println(p.stock + " units available");
    }

    static void purchaseItem(String productId, int userId) {

        ProductNode p = inventory.getNode(productId);

        if (p == null) {
            System.out.println("Product not found");
            return;
        }

        synchronized (p) {
            if (p.stock > 0) {
                p.stock--;
                System.out.println("Success, " + p.stock + " units remaining");
            } else {
                waitingList.enqueue(userId);
                System.out.println("Added to waiting list, position #" + waitingList.getPosition());
            }
        }
    }

    public static void main(String[] args) {

        addProduct("IPHONE15_256GB", 100);

        checkStock("IPHONE15_256GB");

        purchaseItem("IPHONE15_256GB", 12345);
        purchaseItem("IPHONE15_256GB", 67890);

        for (int i = 0; i < 100; i++) {
            purchaseItem("IPHONE15_256GB", i);
        }

        purchaseItem("IPHONE15_256GB", 99999);
    }
}