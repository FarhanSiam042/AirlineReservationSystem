import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Refactored RandomGenerator with:
 * 1. Singleton pattern
 * 2. Extracted methods
 * 3. Value objects
 * 4. Encapsulated data
 */
public final class RandomGenerator {
    // 1.3.4 Replace Magic Literal with constants
    private static final int MIN_CUSTOMER_ID = 200_000;
    private static final int MAX_CUSTOMER_ID = 999_999;
    private static final int MIN_SEATS = 75;
    private static final int MAX_SEATS = 500;
    private static final List<String> AIRLINE_CODES = List.of("BA", "AF", "LH", "AA", "DL");

    // 1.2.3 Extract Class - Destination data
    private final DestinationProvider destinationProvider;

    // 1.6.1 Pull Up Constructor Body - Singleton instance
    private static final RandomGenerator INSTANCE = new RandomGenerator(new DestinationProvider());

    private RandomGenerator(DestinationProvider destinationProvider) {
        this.destinationProvider = destinationProvider;
    }

    public static RandomGenerator getInstance() {
        return INSTANCE;
    }

    // 1.1.1 Extract Method - ID generation
    public CustomerId generateCustomerId() {
        int id = ThreadLocalRandom.current()
                .nextInt(MIN_CUSTOMER_ID, MAX_CUSTOMER_ID + 1);
        return new CustomerId(String.valueOf(id));
    }

    // 1.1.1 Extract Method - Flight number generation
    public FlightNumber generateFlightNumber() {
        Random random = ThreadLocalRandom.current();
        String airlineCode = AIRLINE_CODES.get(random.nextInt(AIRLINE_CODES.size()));
        int number = 1000 + random.nextInt(9000);
        return new FlightNumber(airlineCode, number);
    }

    // 1.1.1 Extract Method - Seat generation
    public int generateSeatCount() {
        return ThreadLocalRandom.current()
                .nextInt(MIN_SEATS, MAX_SEATS + 1);
    }

    // 1.3.1 Replace Data Value with Object
    public record FlightNumber(String airlineCode, int number) {
        public FlightNumber {
            if (!airlineCode.matches("[A-Z]{2}")) {
                throw new IllegalArgumentException("Invalid airline code");
            }
            if (number < 1000 || number > 9999) {
                throw new IllegalArgumentException("Invalid flight number");
            }
        }

        @Override
        public String toString() {
            return String.format("%s-%04d", airlineCode, number);
        }
    }

    // 1.2.3 Extract Class - Destination handling
    private static final class DestinationProvider {
        private static final List<Destination> DESTINATIONS = List.of(
                new Destination("New York", 40.7128, -74.0060),
                new Destination("London", 51.5074, -0.1278),
                // ... other destinations
        );

        // 1.1.1 Extract Method - Random destination
        public DestinationPair getRandomDestinationPair() {
            Random random = ThreadLocalRandom.current();
            int originIndex = random.nextInt(DESTINATIONS.size());
            int destIndex;

            do {
                destIndex = random.nextInt(DESTINATIONS.size());
            } while (destIndex == originIndex);

            return new DestinationPair(
                    DESTINATIONS.get(originIndex),
                    DESTINATIONS.get(destIndex)
            );
        }
    }

    // 1.3.1 Replace Data Value with Object
    public record Destination(String city, double latitude, double longitude) {}

    // 1.5.2 Introduce Parameter Object
    public record DestinationPair(Destination origin, Destination destination) {
        public DestinationPair {
            if (origin.equals(destination)) {
                throw new IllegalArgumentException("Origin and destination must differ");
            }
        }
    }

    // 1.5.1 Rename Method - More descriptive name
    public DestinationPair generateRandomRoute() {
        return destinationProvider.getRandomDestinationPair();
    }

    // 1.4.3 Replace Nested Conditional with Guard Clauses
    public String generateGateNumber() {
        Random random = ThreadLocalRandom.current();
        char gateLetter = (char) ('A' + random.nextInt(26));
        int gateNumber = 1 + random.nextInt(30);

        if (gateNumber > 26) {
            gateLetter = (char) ('A' + random.nextInt(8));
        }

        return String.format("%c%d", gateLetter, gateNumber);
    }
}