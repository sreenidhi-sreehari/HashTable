import java.util.Scanner;

class Node {
    String key;
    int value;
    Node next;

    Node(String key, int value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }
}

class HashTable {
    int size = 10007;
    Node[] table;

    HashTable() {
        table = new Node[size];
    }

    int hash(String key) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (h * 31 + key.charAt(i)) % size;
        }
        return h;
    }

    void put(String key, int value) {
        int index = hash(key);
        Node head = table[index];

        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        Node newNode = new Node(key, value);
        newNode.next = table[index];
        table[index] = newNode;
    }

    boolean contains(String key) {
        int index = hash(key);
        Node head = table[index];

        while (head != null) {
            if (head.key.equals(key)) {
                return true;
            }
            head = head.next;
        }

        return false;
    }

    int get(String key) {
        int index = hash(key);
        Node head = table[index];

        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }

        return 0;
    }
}

public class UsernameChecker {

    static HashTable users = new HashTable();
    static HashTable attempts = new HashTable();

    static boolean checkAvailability(String username) {

        int count = attempts.get(username);
        attempts.put(username, count + 1);

        if (users.contains(username)) {
            return false;
        }

        return true;
    }

    static void registerUser(String username, int userId) {
        users.put(username, userId);
    }

    static void suggestAlternatives(String username) {

        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;

            if (!users.contains(suggestion)) {
                System.out.println(suggestion);
            }
        }

        String alt = username.replace('_', '.');

        if (!users.contains(alt)) {
            System.out.println(alt);
        }
    }

    static String getMostAttempted() {

        String maxUser = "";
        int max = 0;

        for (int i = 0; i < attempts.size; i++) {

            Node head = attempts.table[i];

            while (head != null) {

                if (head.value > max) {
                    max = head.value;
                    maxUser = head.key;
                }

                head = head.next;
            }
        }

        return maxUser;
    }

    public static void main(String[] args) {

        registerUser("john_doe", 1);
        registerUser("admin", 2);

        System.out.println(checkAvailability("john_doe"));
        System.out.println(checkAvailability("jane_smith"));

        suggestAlternatives("john_doe");

        System.out.println(getMostAttempted());
    }
}