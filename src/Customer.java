import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Refactored Customer class with:
 * 1. Extracted value objects
 * 2. Encapsulated collections
 * 3. Simplified conditionals
 * 4. Proper method organization
 */
public class Customer {
    // 1.3.1 Replace Data Value with Object - Value objects for core fields
    private final CustomerId id;
    private EmailAddress email;
    private String name;
    private PhoneNumber phone;
    private Password password;
    private PostalAddress address;
    private Age age;

    // 1.3.3 Encapsulate Collection - Private field with controlled access
    private final List<FlightBooking> bookings;

    // 1.6.1 Pull Up Constructor Body - Main constructor with validation
    public Customer(String userId, String name, String email, String password) {
        this.id = new CustomerId(userId);
        this.name = validateName(name);
        this.email = new EmailAddress(email);
        this.password = new Password(password);
        this.bookings = new ArrayList<>();
    }

    // 1.1.1 Extract Method - Validation logic
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return name.trim();
    }

    // 1.5.3 Remove Setting Method - Only provide getters for immutable fields
    public CustomerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // 1.5.1 Rename Method - More descriptive name
    public boolean hasActiveBookings() {
        return !bookings.isEmpty();
    }

    // 1.2.1 Move Method - Booking logic moved from Flight class
    public void addFlightBooking(Flight flight, int tickets) {
        // 1.4.3 Replace Nested Conditional with Guard Clauses
        if (flight == null) throw new IllegalArgumentException("Flight cannot be null");
        if (tickets <= 0) throw new IllegalArgumentException("Invalid ticket count");

        bookings.add(new FlightBooking(flight, tickets));
    }

    // 1.1.3 Extract Variable - Complex calculation
    public int getTotalTicketsBooked() {
        int total = 0;
        for (FlightBooking booking : bookings) {
            total += booking.tickets();
        }
        return total;
    }

    // 1.4.2 Consolidate Conditional Expression
    public boolean canMakeBooking() {
        return isAdult() && underBookingLimit();
    }

    private boolean isAdult() {
        return age != null && age.value() >= 18;
    }

    private boolean underBookingLimit() {
        return bookings.size() < 10;
    }

    // 1.3.3 Encapsulate Collection - Safe exposure
    public List<FlightBooking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    // 1.5.2 Introduce Parameter Object - For updates
    public void updateContactInfo(ContactInfo info) {
        this.phone = info.phone();
        this.address = info.address();
    }

    // Value Object definitions (could be in separate files)
    public record CustomerId(String value) {
        public CustomerId {
            if (value == null || value.length() != 6) {
                throw new IllegalArgumentException("ID must be 6 characters");
            }
        }
    }

    public record EmailAddress(String value) {
        public EmailAddress {
            if (!value.matches("^[^@]+@[^@]+\\.[^@]+$")) {
                throw new IllegalArgumentException("Invalid email");
            }
        }
    }

    public record PhoneNumber(String value) {
        public PhoneNumber {
            if (!value.matches("^\\+?[0-9]{10,15}$")) {
                throw new IllegalArgumentException("Invalid phone number");
            }
        }
    }

    public record Password(String hash) {
        // Would contain password hashing logic
    }

    public record PostalAddress(String street, String city, String postalCode) {}

    public record Age(int value) {
        public Age {
            if (value < 0 || value > 120) {
                throw new IllegalArgumentException("Invalid age");
            }
        }
    }

    public record FlightBooking(Flight flight, int tickets) {}

    public record ContactInfo(PhoneNumber phone, PostalAddress address) {}
}