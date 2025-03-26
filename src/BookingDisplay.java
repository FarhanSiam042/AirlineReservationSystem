public interface BookingDisplay {
    void displayFlightStatus(Flight.FlightNumber flightNumber);
    List<Booking> getBookingsForCustomer(CustomerId customerId);
}