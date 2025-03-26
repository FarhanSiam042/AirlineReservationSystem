public class FlightDetails {
    private final String flightSchedule;
    private final String flightNumber;
    private final String fromCity;
    private final String toCity;
    private final String gate;
    private final int seats;
    private final String[] distanceInfo;

    public FlightDetails(String flightSchedule, String flightNumber, int seats,
                         String[][] cities, String[] distanceInfo, String gate) {
        this.flightSchedule = flightSchedule;
        this.flightNumber = flightNumber;
        this.seats = seats;
        this.fromCity = cities[0][0];
        this.toCity = cities[1][0];
        this.distanceInfo = distanceInfo;
        this.gate = gate;
    }

    public String getFlightSchedule() {
        return flightSchedule;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public String getGate() {
        return gate;
    }

    public int getSeats() {
        return seats;
    }

    public String[] getDistanceInfo() {
        return distanceInfo;
    }
}