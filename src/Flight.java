import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Refactored Flight class with:
 * 1. Extracted value objects
 * 2. Encapsulated collections
 * 3. Simplified conditionals
 * 4. Proper method organization
 */
public class Flight {
    // 1.3.1 Replace Data Value with Object
    private final FlightNumber flightNumber;
    private final Route route;
    private final Schedule schedule;
    private final Aircraft aircraft;

    // 1.3.3 Encapsulate Collection
    private final List<PassengerManifest> passengers;
    private int availableSeats;

    // 1.6.1 Pull Up Constructor Body
    public Flight(FlightNumber number, Route route, Schedule schedule, int totalSeats) {
        this.flightNumber = number;
        this.route = route;
        this.schedule = schedule;
        this.aircraft = new Aircraft(totalSeats);
        this.passengers = new ArrayList<>();
        this.availableSeats = totalSeats;
    }

    // 1.5.3 Remove Setting Method (immutable fields)
    public FlightNumber getFlightNumber() {
        return flightNumber;
    }

    public Route getRoute() {
        return route;
    }

    // 1.1.1 Extract Method
    public boolean isAvailable() {
        return availableSeats > 0 && !isDeparted();
    }

    private boolean isDeparted() {
        return LocalDateTime.now().isAfter(schedule.departure());
    }

    // 1.4.3 Replace Nested Conditional with Guard Clauses
    public void bookPassenger(Passenger passenger, int seats) {
        if (passenger == null) throw new IllegalArgumentException("Passenger required");
        if (seats <= 0) throw new IllegalArgumentException("Invalid seat count");
        if (!isAvailable()) throw new IllegalStateException("Flight not available");
        if (seats > availableSeats) throw new IllegalArgumentException("Not enough seats");

        passengers.add(new PassengerManifest(passenger, seats));
        availableSeats -= seats;
    }

    // 1.1.3 Extract Variable
    public double calculateRevenue() {
        final double basePrice = getBasePrice();
        final double totalSeatsBooked = getTotalSeatsBooked();
        return basePrice * totalSeatsBooked;
    }

    private int getTotalSeatsBooked() {
        return passengers.stream()
                .mapToInt(PassengerManifest::seats)
                .sum();
    }

    // 1.3.3 Encapsulate Collection
    public List<PassengerManifest> getPassengerManifest() {
        return Collections.unmodifiableList(passengers);
    }

    // 1.2.1 Move Method (from original FlightReservation)
    public boolean hasPassenger(Passenger passenger) {
        return passengers.stream()
                .anyMatch(p -> p.passenger().equals(passenger));
    }

    // 1.4.2 Consolidate Conditional Expression
    public boolean needsCateringService() {
        return isLongHaulFlight() && !isDomestic();
    }

    private boolean isLongHaulFlight() {
        return schedule.duration().toHours() > 6;
    }

    // Value Object definitions
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

    public record Route(Airport origin, Airport destination) {
        public Route {
            if (origin.equals(destination)) {
                throw new IllegalArgumentException("Origin and destination cannot be same");
            }
        }
    }

    public record Schedule(LocalDateTime departure,
                           LocalDateTime arrival,
                           Duration duration) {}

    public record PassengerManifest(Passenger passenger, int seats) {}

    public record Aircraft(int totalSeats) {
        public Aircraft {
            if (totalSeats < 50 || totalSeats > 500) {
                throw new IllegalArgumentException("Invalid seat capacity");
            }
        }
    }

    // 1.5.2 Introduce Parameter Object
    public record BookingRequest(Passenger passenger,
                                 int seats,
                                 LocalDateTime bookingTime) {}
}