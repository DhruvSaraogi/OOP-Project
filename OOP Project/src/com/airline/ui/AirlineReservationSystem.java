package com.airline.ui;

import com.airline.manager.Manager;
import com.airline.model.Flight;
import com.airline.model.Traveller;
import java.util.*;

public class AirlineReservationSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Flight> flights = new ArrayList<>();
        Manager manager = new Manager("John Doe", "manager123");

        while (true) {
            System.out.println("\nAre you a Manager or a Customer?");
            System.out.println("1. Manager");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int userType = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (userType) {
                case 1:
                    System.out.print("Enter Manager Password: ");
                    String inputPassword = sc.nextLine();
                    if (manager.validatePassword(inputPassword)) {
                        System.out.println("Access granted.");
                        managerMenu(sc, flights, manager);
                    } else {
                        System.out.println("Invalid password. Access denied.");
                    }
                    break;

                case 2:
                    customerMenu(sc, flights);
                    break;

                case 3:
                    System.out.println("Exiting the program.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private static void managerMenu(Scanner sc, List<Flight> flights, Manager manager) {
        while (true) {
            System.out.println("\nManager Menu:");
            System.out.println("1. Add Flight");
            System.out.println("2. Generate Flight Report");
            System.out.println("3. Log Out");
            System.out.print("Choose an option: ");
            int option = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    System.out.print("Enter Flight ID: ");
                    String flightId = sc.nextLine();
                    System.out.print("Enter Origin: ");
                    String origin = sc.nextLine();
                    System.out.print("Enter Destination: ");
                    String destination = sc.nextLine();
                    System.out.print("Is it International (true/false): ");
                    boolean isInternational = sc.nextBoolean();
                    System.out.print("Enter Total Seats: ");
                    int totalSeats = sc.nextInt();
                    System.out.print("Enter Base Fare: ");
                    double baseFare = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter Flight Date (dd/MM/yyyy): ");
                    String flightDate = sc.nextLine();
                    manager.addFlight(flights, flightId, origin, destination, isInternational,
                            totalSeats, baseFare, flightDate);
                    break;

                case 2:
                    manager.generateReport(flights);
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private static void customerMenu(Scanner sc, List<Flight> flights) {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Search Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. Log Out");
            System.out.print("Choose an option: ");
            int option = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    System.out.print("Enter Origin: ");
                    String searchOrigin = sc.nextLine();
                    System.out.print("Enter Destination: ");
                    String searchDestination = sc.nextLine();
                    System.out.println("Available flights from " + searchOrigin + " to " + searchDestination + ":");

                    boolean flightsFound = false;
                    for (Flight flight : flights) {
                        if (flight.getOrigin().equalsIgnoreCase(searchOrigin) &&
                                flight.getDestination().equalsIgnoreCase(searchDestination) &&
                                !flight.isFullCapacity()) {
                            System.out.println("Flight ID: " + flight.getFlightId() +
                                    ", Date: " + flight.getFlightDate() +
                                    ", Seats Available: " + flight.getAvailableSeats() +
                                    ", Base Fare: $" + flight.getBaseFare());
                            flightsFound = true;
                        }
                    }
                    if (!flightsFound) {
                        System.out.println("No flights available for this route.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Flight ID to book: ");
                    String bookingFlightId = sc.nextLine();

                    Flight selectedFlight = null;
                    for (Flight flight : flights) {
                        if (flight.getFlightId().equals(bookingFlightId)) {
                            selectedFlight = flight;
                            break;
                        }
                    }

                    if (selectedFlight == null) {
                        System.out.println("Flight not found.");
                        break;
                    }

                    if (selectedFlight.isFullCapacity()) {
                        System.out.println("Sorry, this flight is full.");
                        break;
                    }

                    System.out.print("Enter number of seats to book: ");
                    int seatsToBook = sc.nextInt();
                    sc.nextLine();

                    if (seatsToBook <= 0) {
                        System.out.println("Invalid number of seats.");
                        break;
                    }

                    double totalPrice = selectedFlight.calculatePrice(seatsToBook);
                    System.out.printf("Total price for %d seats: $%.2f\n", seatsToBook, totalPrice);
                    System.out.print("Confirm booking (yes/no)? ");
                    String confirm = sc.nextLine();

                    if (confirm.equalsIgnoreCase("yes")) {
                        if (selectedFlight.bookSeats(seatsToBook)) {
                            System.out.println("Booking confirmed!");
                            System.out.printf("Total amount paid: $%.2f\n", totalPrice);
                            System.out.println("Flight details:");
                            System.out.println("Flight ID: " + selectedFlight.getFlightId());
                            System.out.println("Date: " + selectedFlight.getFlightDate());
                            System.out.println("From: " + selectedFlight.getOrigin());
                            System.out.println("To: " + selectedFlight.getDestination());
                            System.out.println("Seats booked: " + seatsToBook);
                        } else {
                            System.out.println("Sorry, not enough seats available.");
                        }
                    } else {
                        System.out.println("Booking cancelled.");
                    }
                    break;

                case 3:
                    return;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }
}
