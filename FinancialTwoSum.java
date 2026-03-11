class Transaction {

    int id;
    int amount;
    String merchant;
    int time; // minutes from start of day
    String account;

    Transaction(int id, int amount, String merchant, int time, String account) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.time = time;
        this.account = account;
    }
}

class AmountNode {

    int amount;
    int index;
    AmountNode next;

    AmountNode(int a, int i) {
        amount = a;
        index = i;
        next = null;
    }
}

class HashTable {

    int size = 1009;
    AmountNode[] table;

    HashTable() {
        table = new AmountNode[size];
    }

    int hash(int key) {
        return key % size;
    }

    void insert(int amount, int index) {

        int h = hash(amount);

        AmountNode node = new AmountNode(amount, index);
        node.next = table[h];
        table[h] = node;
    }

    int find(int amount) {

        int h = hash(amount);

        AmountNode node = table[h];

        while (node != null) {

            if (node.amount == amount)
                return node.index;

            node = node.next;
        }

        return -1;
    }
}

public class FinancialTwoSum {

    static Transaction[] transactions = new Transaction[1000];
    static int count = 0;

    static void addTransaction(int id, int amount, String merchant, int time, String account) {

        transactions[count++] =
                new Transaction(id, amount, merchant, time, account);
    }

    static void findTwoSum(int target) {

        HashTable table = new HashTable();

        for (int i = 0; i < count; i++) {

            int complement = target - transactions[i].amount;

            int index = table.find(complement);

            if (index != -1) {

                System.out.println(
                        "Two Sum Found: (" +
                        transactions[index].id +
                        ", " +
                        transactions[i].id +
                        ")");
            }

            table.insert(transactions[i].amount, i);
        }
    }

    static void findTwoSumWithinHour(int target) {

        HashTable table = new HashTable();

        for (int i = 0; i < count; i++) {

            int complement = target - transactions[i].amount;

            int index = table.find(complement);

            if (index != -1) {

                int timeDiff =
                        transactions[i].time -
                        transactions[index].time;

                if (timeDiff <= 60) {

                    System.out.println(
                            "Two Sum within 1 hour: (" +
                            transactions[index].id +
                            ", " +
                            transactions[i].id +
                            ")");
                }
            }

            table.insert(transactions[i].amount, i);
        }
    }

    static void detectDuplicates() {

        for (int i = 0; i < count; i++) {

            for (int j = i + 1; j < count; j++) {

                if (transactions[i].amount == transactions[j].amount &&
                        transactions[i].merchant.equals(transactions[j].merchant) &&
                        !transactions[i].account.equals(transactions[j].account)) {

                    System.out.println(
                            "Duplicate Payment: amount=" +
                            transactions[i].amount +
                            " merchant=" +
                            transactions[i].merchant +
                            " accounts=(" +
                            transactions[i].account +
                            "," +
                            transactions[j].account +
                            ")");
                }
            }
        }
    }

    static void findKSum(int start, int k, int target, int[] result, int depth) {

        if (k == 0 && target == 0) {

            System.out.print("K-Sum Found: ");

            for (int i = 0; i < depth; i++) {
                System.out.print(result[i] + " ");
            }

            System.out.println();
            return;
        }

        if (k == 0)
            return;

        for (int i = start; i < count; i++) {

            result[depth] = transactions[i].id;

            findKSum(
                    i + 1,
                    k - 1,
                    target - transactions[i].amount,
                    result,
                    depth + 1);
        }
    }

    public static void main(String[] args) {

        addTransaction(1, 500, "StoreA", 600, "acc1");
        addTransaction(2, 300, "StoreB", 615, "acc2");
        addTransaction(3, 200, "StoreC", 630, "acc3");
        addTransaction(4, 500, "StoreA", 640, "acc4");

        findTwoSum(500);

        findTwoSumWithinHour(500);

        detectDuplicates();

        int[] result = new int[3];
        findKSum(0, 3, 1000, result, 0);
    }
}