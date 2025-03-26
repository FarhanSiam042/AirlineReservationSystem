public record BookingRequest(
        Flight.FlightNumber flightNumber,
        CustomerId customerId,
        int seats,
        PaymentMethod paymentMethod
) {}