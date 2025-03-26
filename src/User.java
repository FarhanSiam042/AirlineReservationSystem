/*
 * This class is intended to be the main class for this Project. All necessary methods are getting calls from this class.
 *
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {

    // ************************************************************ Fields
    // ************************************************************

    /*
     * 2D Array to store admin credentials. Default credentials are stored on [0][0]
     * index. Max num of admins can be 10....
     */
    // Replace magic numbers with named constants
    private static final int MAX_ADMIN_USERS = 10;
    private static final String DEFAULT_ADMIN_USERNAME = "root";
    private static final String DEFAULT_ADMIN_PASSWORD = "root";
    private static final int OPTION_EXIT = 0;
    private static final int OPTION_LOGIN_ADMIN = 1;
    private static final int OPTION_LOGIN_PASSENGER = 2;
    static String[][] adminUserNameAndPassword = new String[MAX_ADMIN_USERS][2];
    private static List<Customer> customersCollection = new ArrayList<>();
    private static int countNumOfUsers = 1; // Initialize to 1 for default admin user

    // Initialize default admin credentials in a constructor or static block
    static {
        adminUserNameAndPassword[0][0] = DEFAULT_ADMIN_USERNAME;
        adminUserNameAndPassword[0][1] = DEFAULT_ADMIN_PASSWORD;
    }


    // ************************************************************
    // Behaviours/Methods
    // ************************************************************

    private static void handleAdminLogin(Scanner read, RolesAndPermissions r1, Customer c1, Flight f1, FlightReservation bookingAndReserving) {
        System.out.print("\nEnter the UserName to login to the Management System :     ");
        String username = read.nextLine();
        System.out.print("Enter the Password to login to the Management System :    ");
        String password = read.nextLine();
        System.out.println();

        int adminStatus = r1.validateAdminCredentials(username, password);
        if (adminStatus == -1) {
            System.out.printf("\n%20sERROR!!! Unable to login Cannot find user with the entered credentials.... Try Creating New Credentials or get yourself register by pressing 4...\n", "");
        } else if (adminStatus == 0) {
            System.out.println("You've standard/default privileges to access the data... You can just view customers data..." + "Can't perform any actions on them....");
            c1.displayCustomersData(true);
        } else {
            handleAdminMenu(username, read, c1, f1, bookingAndReserving);
        }
    }

    private static void handlePassengerLogin(Scanner read, RolesAndPermissions r1, Customer c1, Flight f1, FlightReservation bookingAndReserving) {
        System.out.print("\n\nEnter the Email to Login : \t");
        String email = read.nextLine();
        System.out.print("Enter the Password : \t");
        String password = read.nextLine();
        String[] result = r1.validatePassengerCredentials(email, password).split("-");

        if (Integer.parseInt(result[0]) == 1) {
            handlePassengerMenu(email, result[1], read, c1, f1, bookingAndReserving);
        } else {
            System.out.printf("\n%20sERROR!!! Unable to login Cannot find user with the entered credentials.... Try Creating New Credentials or get yourself register by pressing 4...\n", "");
        }
    }

    private static void handleAdminMenu(String username, Scanner read, Customer c1, Flight f1, FlightReservation bookingAndReserving) {
        System.out.printf("%-20sLogged in Successfully as \"%s\"..... For further Proceedings, enter a value from below....", "", username);
        int desiredOption;
        Scanner read1 = new Scanner(System.in);

        do {
            System.out.printf("\n\n%-60s+++++++++ 2nd Layer Menu +++++++++%50sLogged in as \"%s\"\n", "", "", username);
            System.out.printf("%-30s (a) Enter 1 to add new Passenger....\n", "");
            System.out.printf("%-30s (b) Enter 2 to search a Passenger....\n", "");
            System.out.printf("%-30s (c) Enter 3 to update the Data of the Passenger....\n", "");
            System.out.printf("%-30s (d) Enter 4 to delete a Passenger....\n", "");
            System.out.printf("%-30s (e) Enter 5 to Display all Passengers....\n", "");
            System.out.printf("%-30s (f) Enter 6 to Display all flights registered by a Passenger...\n", "");
            System.out.printf("%-30s (g) Enter 7 to Display all registered Passengers in a Flight....\n", "");
            System.out.printf("%-30s (h) Enter 8 to Delete a Flight....\n", "");
            System.out.printf("%-30s (i) Enter 0 to Go back to the Main Menu/Logout....\n", "");
            System.out.print("Enter the desired Choice :   ");
            desiredOption = read.nextInt();

            handleAdminMenuChoice(desiredOption, read1, c1, f1, bookingAndReserving);

        } while (desiredOption != 0);
    }

    private static void handlePassengerMenu(String email, String userId, Scanner read, Customer c1, Flight f1, FlightReservation bookingAndReserving) {
        System.out.printf("\n\n%-20sLogged in Successfully as \"%s\"..... For further Proceedings, enter a value from below....", "", email);
        int desiredChoice;
        Scanner read1 = new Scanner(System.in);

        do {
            System.out.printf("\n\n%-60s+++++++++ 3rd Layer Menu +++++++++%50sLogged in as \"%s\"\n", "", "", email);
            System.out.printf("%-40s (a) Enter 1 to Book a flight....\n", "");
            System.out.printf("%-40s (b) Enter 2 to update your Data....\n", "");
            System.out.printf("%-40s (c) Enter 3 to delete your account....\n", "");
            System.out.printf("%-40s (d) Enter 4 to Display Flight Schedule....\n", "");
            System.out.printf("%-40s (e) Enter 5 to Cancel a Flight....\n", "");
            System.out.printf("%-40s (f) Enter 6 to Display all flights registered by \"%s\"....\n", "", email);
            System.out.printf("%-40s (g) Enter 0 to Go back to the Main Menu/Logout....\n", "");
            System.out.print("Enter the desired Choice :   ");
            desiredChoice = read.nextInt();

            handlePassengerMenuChoice(desiredChoice, userId, read1, c1, f1, bookingAndReserving);

        } while (desiredChoice != 0);
    }

    private static void handleAdminMenuChoice(int choice, Scanner read, Customer c1, Flight f1, FlightReservation bookingAndReserving) {
        switch (choice) {
            case 1:
                c1.addNewCustomer();
                break;
            case 2:
                c1.displayCustomersData(false);
                System.out.print("Enter the CustomerID to Search :\t");
                String customerID = read.nextLine();
                System.out.println();
                c1.searchUser(customerID);
                break;
            case 3:
                c1.displayCustomersData(false);
                System.out.print("Enter the CustomerID to Update its Data :\t");
                customerID = read.nextLine();
                if (customersCollection.size() > 0) {
                    c1.editUserInfo(customerID);
                } else {
                    System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", customerID);
                }
                break;
            case 4:
                c1.displayCustomersData(false);
                System.out.print("Enter the CustomerID to Delete its Data :\t");
                customerID = read.nextLine();
                if (customersCollection.size() > 0) {
                    c1.deleteUser(customerID);
                } else {
                    System.out.printf("%-50sNo Customer with the ID %s Found...!!!\n", " ", customerID);
                }
                break;
            case 5:
                c1.displayCustomersData(false);
                break;
            case 6:
                c1.displayCustomersData(false);
                System.out.print("\n\nEnter the ID of the user to display all flights registered by that user...");
                String id = read.nextLine();
                bookingAndReserving.displayFlightsRegisteredByOneUser(id);
                break;
            case 7:
                System.out.print("Do you want to display Passengers of all flights or a specific flight.... 'Y/y' for displaying all flights and 'N/n' to look for a specific flight.... ");
                char choice7 = read.nextLine().charAt(0);
                if ('y' == choice7 || 'Y' == choice7) {
                    bookingAndReserving.displayRegisteredUsersForAllFlight();
                } else if ('n' == choice7 || 'N' == choice7) {
                    f1.displayFlightSchedule();
                    System.out.print("Enter the Flight Number to display the list of passengers registered in that flight... ");
                    String flightNum = read.nextLine();
                    bookingAndReserving.displayRegisteredUsersForASpecificFlight(flightNum);
                } else {
                    System.out.println("Invalid Choice...No Response...!");
                }
                break;
            case 8:
                f1.displayFlightSchedule();
                System.out.print("Enter the Flight Number to delete the flight : ");
                String flightNum = read.nextLine();
                f1.deleteFlight(flightNum);
                break;
            case 0:
                System.out.println("Thanks for Using BAV Airlines Ticketing System...!!!");
                break;
            default:
                System.out.println("Invalid Choice...Looks like you're Robot...Entering values randomly...You've Have to login again...");
                choice = 0;
        }
    }

    private static void handlePassengerMenuChoice(int choice, String userId, Scanner read, Customer c1, Flight f1, FlightReservation bookingAndReserving) {
        switch (choice) {
            case 1:
                f1.displayFlightSchedule();
                System.out.print("\nEnter the desired flight number to book :\t ");
                String flightToBeBooked = read.nextLine();
                System.out.print("Enter the Number of tickets for " + flightToBeBooked + " flight :   ");
                int numOfTickets = Integer.parseInt(read.nextLine());
                while (numOfTickets > 10) {
                    System.out.print("ERROR!! You can't book more than 10 tickets at a time for single flight....Enter number of tickets again : ");
                    numOfTickets = Integer.parseInt(read.nextLine());
                }
                bookingAndReserving.bookFlight(flightToBeBooked, numOfTickets, userId);
                break;
            case 2:
                c1.editUserInfo(userId);
                break;
            case 3:
                System.out.print("Are you sure to delete your account...It's an irreversible action...Enter Y/y to confirm...");
                char confirmationChar = read.nextLine().charAt(0);
                if (confirmationChar == 'Y' || confirmationChar == 'y') {
                    c1.deleteUser(userId);
                    System.out.printf("User's account deleted Successfully...!!!");
                    choice = 0;
                } else {
                    System.out.println("Action has been cancelled...");
                }
                break;
            case 4:
                f1.displayFlightSchedule();
                f1.displayMeasurementInstructions();
                break;
            case 5:
                bookingAndReserving.cancelFlight(userId);
                break;
            case 6:
                bookingAndReserving.displayFlightsRegisteredByOneUser(userId);
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid Choice...Looks like you're Robot...Entering values randomly...You've Have to login again...");
                choice = 0;
        }
    }

    public static void main(String[] args) {
        RolesAndPermissions r1 = new RolesAndPermissions();
        Flight f1 = new Flight();
        FlightReservation bookingAndReserving = new FlightReservation();
        Customer c1 = new Customer();
        Flight.initializeFlightSchedule();
        Scanner read = new Scanner(System.in);

        System.out.println(
                "\n\t\t\t\t\t+++++++++++++ Welcome to BAV AirLines +++++++++++++\n\nTo Further Proceed, Please enter a value.");
        System.out.println(
                "\n***** Default Username && Password is root-root ***** Using Default Credentials will restrict you to just view the list of Passengers....\n");

        int desiredOption;
        do {
            displayMainMenu();
            desiredOption = read.nextInt();
            while (desiredOption < OPTION_EXIT || desiredOption > 8) {
                System.out.print("ERROR!! Please enter value between 0 - 4. Enter the value again :\t");
                desiredOption = read.nextInt();
            }

            Scanner read1 = new Scanner(System.in);

            if (desiredOption == OPTION_LOGIN_ADMIN) {
                handleAdminLogin(read1, r1, c1, f1, bookingAndReserving);
            } else if (desiredOption == 2) {
                /*
                 * If desiredOption is 2, then call the registration method to register a
                 * user......
                 */
                System.out.print("\nEnter the UserName to Register :    ");
                String username = read1.nextLine();
                System.out.print("Enter the Password to Register :     ");
                String password = read1.nextLine();
                while (r1.validateAdminCredentials(username, password) != -1) {
                    System.out.print("ERROR!!! Admin with same UserName already exist. Enter new UserName:   ");
                    username = read1.nextLine();
                    System.out.print("Enter the Password Again:   ");
                    password = read1.nextLine();
                }

                // /* Setting the credentials entered by the user..... */
                adminUserNameAndPassword[countNumOfUsers][0] = username;
                adminUserNameAndPassword[countNumOfUsers][1] = password;

                // /* Incrementing the numOfUsers */
                countNumOfUsers++;
            } else if (desiredOption == 3) {
                handlePassengerLogin(read1, r1, c1, f1, bookingAndReserving);
            } else if (desiredOption == 4) {

                c1.addNewCustomer();
            } else if (desiredOption == 5) {
                manualInstructions();
            }

            displayMainMenu();
            desiredOption = read1.nextInt();
            while (desiredOption < 0 || desiredOption > 8) {
                System.out.print("ERROR!! Please enter value between 0 - 4. Enter the value again :\t");
                desiredOption = read1.nextInt();
            }
        } while (desiredOption != 0);

    }

    static void displayMainMenu() {
        System.out.println("\n\n\t\t(a) Press 0 to Exit.");
        System.out.println("\t\t(b) Press 1 to Login as admin.");
        System.out.println("\t\t(c) Press 2 to Register as admin.");
        System.out.println("\t\t(d) Press 3 to Login as Passenger.");
        System.out.println("\t\t(e) Press 4 to Register as Passenger.");
        System.out.println("\t\t(f) Press 5 to Display the User Manual.");
        System.out.print("\t\tEnter the desired option:    ");
    }

    static void manualInstructions() {
        Scanner read = new Scanner(System.in);
        System.out.printf("%n%n%50s %s Welcome to BAV Airlines User Manual %s", "", "+++++++++++++++++",
                "+++++++++++++++++");
        System.out.println("\n\n\t\t(a) Press 1 to display Admin Manual.");
        System.out.println("\t\t(b) Press 2 to display User Manual.");
        System.out.print("\nEnter the desired option :    ");
        int choice = read.nextInt();
        while (choice < 1 || choice > 2) {
            System.out.print("ERROR!!! Invalid entry...Please enter a value either 1 or 2....Enter again....");
            choice = read.nextInt();
        }
        if (choice == 1) {
            System.out.println(
                    "\n\n(1) Admin have the access to all users data...Admin can delete, update, add and can perform search for any customer...\n");
            System.out.println(
                    "(2) In order to access the admin module, you've to get yourself register by pressing 2, when the main menu gets displayed...\n");
            System.out.println(
                    "(3) Provide the required details i.e., name, email, id...Once you've registered yourself, press 1 to login as an admin... \n");
            System.out.println(
                    "(4) Once you've logged in, 2nd layer menu will be displayed on the screen...From here on, you can select from variety of options...\n");
            System.out.println(
                    "(5) Pressing \"1\" will add a new Passenger, provide the program with required details to add the passenger...\n");
            System.out.println(
                    "(6) Pressing \"2\" will search for any passenger, given the admin(you) provides the ID from the table printing above....  \n");
            System.out.println(
                    "(7) Pressing \"3\" will let you update any passengers data given the user ID provided to program...\n");
            System.out.println("(8) Pressing \"4\" will let you delete any passenger given its ID provided...\n");
            System.out.println("(9) Pressing \"5\" will let you display all registered passenger...\n");
            System.out.println(
                    "(10) Pressing \"6\" will let you display all registered passengers...After selecting, program will ask, if you want to display passengers for all flights(Y/y) or a specific flight(N/n)\n");
            System.out.println(
                    "(11) Pressing \"7\" will let you delete any flight given its flight number provided...\n");
            System.out.println(
                    "(11) Pressing \"0\" will make you logged out of the program...You can login again any time you want during the program execution....\n");
        } else {
            System.out.println(
                    "\n\n(1) Local user has the access to its data only...He/She won't be able to change/update other users data...\n");
            System.out.println(
                    "(2) In order to access local users benefits, you've to get yourself register by pressing 4, when the main menu gets displayed...\n");
            System.out.println(
                    "(3) Provide the details asked by the program to add you to the users list...Once you've registered yourself, press \"3\" to login as a passenger...\n");
            System.out.println(
                    "(4) Once you've logged in, 3rd layer menu will be displayed...From here on, you embarked on the journey to fly with us...\n");
            System.out.println(
                    "(5) Pressing \"1\" will display available/scheduled list of flights...To get yourself booked for a flight, enter the flight number and number of tickets for the flight...Max num of tickets at a time is 10 ...\n");
            System.out.println(
                    "(7) Pressing \"2\" will let you update your own data...You won't be able to update other's data... \n");
            System.out.println("(8) Pressing \"3\" will delete your account... \n");
            System.out
                    .println("(9) Pressing \"4\" will display randomly designed flight schedule for this runtime...\n");
            System.out.println("(10) Pressing \"5\" will let you cancel any flight registered by you...\n");
            System.out.println("(11) Pressing \"6\" will display all flights registered by you...\n");
            System.out.println(
                    "(12) Pressing \"0\" will make you logout of the program...You can login back at anytime with your credentials...for this particular run-time... \n");
        }
    }

    // ************************************************************ Setters &
    // Getters ************************************************************

    public static List<Customer> getCustomersCollection() {
        return customersCollection;
    }
}