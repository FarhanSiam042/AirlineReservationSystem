import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java. util. Scanner;

public class FlightScheduler {
    private final RandomGenerator randomGenerator;

    public FlightScheduler() {
        this.randomGenerator = new RandomGenerator();
    }

    public void generateFlightSchedule(List<Flight> flightList, int numOfFlights) {
        for (int i = 0; i < numOfFlights; i++) {
            String[][] chosenDestinations = randomGenerator.randomDestinations();
            Flight tempFlight = new Flight();
            String[] distanceBetweenTheCities = tempFlight.calculateDistance(
                    Double.parseDouble(chosenDestinations[0][1]),
                    Double.parseDouble(chosenDestinations[0][2]),
                    Double.parseDouble(chosenDestinations[1][1]),
                    Double.parseDouble(chosenDestinations[1][2])
            );
            String flightSchedule = createNewFlightsAndTime();
            String flightNumber = randomGenerator.randomFlightNumbGen(2, 1).toUpperCase();
            int numOfSeatsInTheFlight = randomGenerator.randomNumOfSeats();
            String gate = randomGenerator.randomFlightNumbGen(1, 30).toUpperCase();

            FlightDetails details = new FlightDetails(flightSchedule, flightNumber, numOfSeatsInTheFlight,
                    chosenDestinations, distanceBetweenTheCities, gate);
            flightList.add(new Flight(details));
        }
    }

    private String createNewFlightsAndTime() {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format for date and time display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a ");

        // Add random hours (0-23) and minutes (0-59) to current time
        LocalDateTime departureTime = now.plusHours(randomGenerator.nextInt(24))
                .plusMinutes(randomGenerator.nextInt(60));

        // Calculate arrival time based on random duration (1-12 hours)
        int flightDuration = randomGenerator.nextInt(12) + 1;
        LocalDateTime arrivalTime = departureTime.plusHours(flightDuration);

        // Format the schedule string
        return departureTime.format(formatter);
    }
}