public record BookingResult(
        boolean success,
        Booking booking,
        String message
) {
    public static BookingResult success(Booking booking) {
        return new BookingResult(true, booking, "Booking successful");
    }

    public static BookingResult failed(String message) {
        return new BookingResult(false, null, message);
    }
}