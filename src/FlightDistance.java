/**
 * Refactored FlightDistance class with:
 * 1. Proper encapsulation
 * 2. Extracted methods
 * 3. Value objects
 * 4. Simplified calculations
 */
public abstract class FlightDistance {
    // 1.3.4 Replace Magic Literal with constants
    private static final double MILES_TO_KM = 1.609344;
    private static final double MILES_TO_NAUTICAL = 0.8684;
    private static final double EARTH_RADIUS_MILES = 3958.8;

    // 1.1.1 Extract Method - Core distance calculation
    public final Distance calculateDistance(GeoLocation origin, GeoLocation destination) {
        double distanceMiles = haversineFormula(origin, destination);
        return new Distance(
                distanceMiles,
                distanceMiles * MILES_TO_KM,
                distanceMiles * MILES_TO_NAUTICAL
        );
    }

    // 1.1.1 Extract Method - Complex calculation
    private double haversineFormula(GeoLocation origin, GeoLocation destination) {
        double lat1 = Math.toRadians(origin.latitude());
        double lon1 = Math.toRadians(origin.longitude());
        double lat2 = Math.toRadians(destination.latitude());
        double lon2 = Math.toRadians(destination.longitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // 1.1.3 Extract Variable for intermediate steps
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_MILES * c;
    }

    // 1.3.1 Replace Data Value with Object
    public record GeoLocation(double latitude, double longitude) {
        public GeoLocation {
            if (latitude < -90 || latitude > 90) {
                throw new IllegalArgumentException("Invalid latitude");
            }
            if (longitude < -180 || longitude > 180) {
                throw new IllegalArgumentException("Invalid longitude");
            }
        }
    }

    // 1.3.1 Replace Data Value with Object
    public record Distance(double miles, double kilometers, double nauticalMiles) {
        public String getFormattedDistance() {
            return String.format("%.2f mi (%.2f km)", miles, kilometers);
        }
    }

    // 1.5.1 Rename Method - More descriptive name
    public abstract Distance calculateDistanceBetween(Airport origin, Airport destination);

    // 1.4.1 Decompose Conditional - Original display method
    public final void displayMeasurementGuidelines() {
        printHeader();
        printGuidelines();
    }

    private void printHeader() {
        String symbols = "+---------------------------+";
        System.out.printf("\n%100s\n%100s", symbols, "| SOME IMPORTANT GUIDELINES |");
        System.out.printf("\n%100s\n", symbols);
    }

    // 1.1.1 Extract Method - Each guideline point
    private void printGuidelines() {
        printGuideline(1, "Distance based on airport coordinates");
        printGuideline(2, "Actual distance may vary due to routing");
        printGuideline(3, "Flight time depends on multiple factors");
        printGuideline(4, "Keep ±1 hour margin for arrival time");
        printGuideline(5, "Times include gate pushback/arrival");
    }

    // 1.1.1 Extract Method - Consistent formatting
    private void printGuideline(int num, String text) {
        System.out.printf("\n\t\t%d. %s", num, text);
    }

    // 1.6.2 Push Down Method - Child class implements airport-specific logic
    protected abstract String[] calculateAirportDistance(Airport origin, Airport destination);
}