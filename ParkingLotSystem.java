class ParkingSpot {

    String plate;
    long entryTime;
    int status; 

    ParkingSpot() {
        status = 0;
        plate = null;
        entryTime = 0;
    }
}

public class ParkingLotSystem {

    static int SIZE = 500;

    static ParkingSpot[] table = new ParkingSpot[SIZE];

    static int occupied = 0;
    static int totalProbes = 0;
    static int totalParks = 0;

    static {
        for (int i = 0; i < SIZE; i++) {
            table[i] = new ParkingSpot();
        }
    }

    static int hash(String plate) {

        int h = 0;

        for (int i = 0; i < plate.length(); i++) {
            h = (h * 31 + plate.charAt(i)) % SIZE;
        }

        return h;
    }

    static void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].status == 1) {

            index = (index + 1) % SIZE;
            probes++;
        }

        table[index].plate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = 1;

        occupied++;
        totalProbes += probes;
        totalParks++;

        System.out.println(
                "Assigned spot #" +
                index +
                " (" +
                probes +
                " probes)");
    }

    static void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].status != 0) {

            if (table[index].status == 1 &&
                table[index].plate.equals(plate)) {

                long exitTime = System.currentTimeMillis();

                long duration = exitTime - table[index].entryTime;

                double hours = duration / 3600000.0;

                double fee = hours * 5.5;

                table[index].status = 2;

                occupied--;

                System.out.println(
                        "Spot #" + index +
                        " freed, Duration: " +
                        hours +
                        "h, Fee: $" +
                        fee);

                return;
            }

            index = (index + 1) % SIZE;
        }

        System.out.println("Vehicle not found");
    }

    static void getStatistics() {

        double occupancy = (occupied * 100.0) / SIZE;

        double avgProbes = 0;

        if (totalParks > 0)
            avgProbes = (double) totalProbes / totalParks;

        System.out.println(
                "Occupancy: " +
                occupancy +
                "%");

        System.out.println(
                "Avg Probes: " +
                avgProbes);
    }

    public static void main(String[] args) {

        parkVehicle("ABC1234");
        parkVehicle("ABC1235");
        parkVehicle("XYZ9999");

        exitVehicle("ABC1234");

        getStatistics();
    }
}