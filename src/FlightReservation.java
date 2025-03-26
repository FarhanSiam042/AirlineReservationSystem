import java.util.List;
import java.util.Optional;

/**
 * Refactored FlightReservation class with:
 * 1. Extracted value objects
 * 2. Encapsulated logic
 * 3. Simplified conditionals
 * 4. Proper method organization
 */
public class FlightReservation implements BookingDisplay {
    // 1.2.2 Move Field - To dependencies
    private final FlightRepository flightRepository;
    private final CustomerRepository customerRepository;
    private final PaymentProcessor paymentProcessor;

    // 1.6.1 Pull Up Constructor Body
    public FlightReservation(FlightRepository flightRepo,
                             CustomerRepository customerRepo,
                             PaymentProcessor paymentProcessor) {
        this.flightRepository = flightRepo;
        this.customerRepository = customerRepo;
        this.paymentProcessor = paymentProcessor;
    }

    // 1.5.2 Introduce Parameter Object
    public BookingResult bookFlight(BookingRequest request) {
        // 1.4.3 Replace Nested Conditional with Guard Clauses
        if (!validateRequest(request)) {
            return BookingResult.failed("Invalid booking request");
        }

        return findFlight(request.flightNumber())
                .flatMap(flight -> findCustomer(request.customerId())
                        .map(customer -> processBooking(request, customer))
                        .orElse(BookingResult.failed("Flight or customer not found"));
    }

    // 1.1.1 Extract Method - Validation logic
    private boolean validateRequest(BookingRequest request) {
        return request.seats() > 0
                && request.flightNumber() != null
                && request.customerId() != null;
    }

    // 1.1.1 Extract Method - Flight lookup
    private Optional<Flight> findFlight(FlightNumber flightNumber) {
        return flightRepository.findByNumber(flightNumber);
    }

    // 1.1.1 Extract Method - Customer lookup
    private Optional<Customer> findCustomer(CustomerId customerId) {
        return customerRepository.findById(customerId);
    }

    // 1.1.1 Extract Method - Core booking process
    private BookingResult processBooking(BookingRequest request, Customer customer) {
        Flight flight = findFlight(request.flightNumber()).get();

        if (!flight.hasAvailableSeats(request.seats())) {
            return BookingResult.failed("Not enough available seats");
        }

        PaymentResult payment = paymentProcessor.process(
                new PaymentRequest(
                        flight.calculatePrice(request.seats()),
                        request.paymentMethod()
                )
        );

        if (!payment.isSuccess()) {
            return BookingResult.failed("Payment failed: " + payment.message());
        }

        Booking booking = new Booking(
                BookingId.generate(),
                flight,
                customer,
                request.seats(),
                LocalDateTime.now()
        );

        flight.addBooking(booking);
        customer.addBooking(booking);

        return BookingResult.success(booking);
    }

    // 1.4.4 Replace Conditional with Polymorphism
    @Override
    public void displayFlightStatus(FlightNumber flightNumber) {
        findFlight(flightNumber)
                .ifPresentOrElse(
                        flight -> System.out.println(flight.getStatus()),
                        () -> System.out.println("Flight not found")
                );
    }

    // 1.3.3 Encapsulate Collection
    @Override
    public List<Booking> getBookingsForCustomer(CustomerId customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getBookings)
                .orElse(List.of());
    }





}