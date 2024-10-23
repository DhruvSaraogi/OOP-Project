package com.airline.ui;

import com.airline.manager.Manager;
import com.airline.model.Flight;
import com.airline.model.Traveller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AirlineReservationSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Flight> flights = new ArrayList<>();
        Manager manager = new Manager("John Doe", "manager123"); // Manager with a predefined password

        // Initial prompt to identify user type
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
                    // Manager login
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
                    // Customer menu
                    customerMenu(sc, flights);
                    break;

                case 3:
                    // Exit the program
                    System.out.println("Exiting the program.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    // Manager menu
    public static void managerMenu(Scanner sc, List<Flight> flights, Manager manager) {
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
                    // Adding a new flight
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
                    manager.addFlight(flights, flightId, origin, destination, isInternational, totalSeats, baseFare);
                    break;

                case 2:
                    // Generate report for full-capacity flights
                    manager.generateReport(flights);
                    break;

                case 3:
                    // Log out and return to main menu
                    return;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    // Customer menu
    public static void customerMenu(Scanner sc, List<Flight> flights) {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Search Flights");
            System.out.println("2. Log Out");
            System.out.print("Choose an option: ");
            int option = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    // Searching for flights
                    System.out.print("Enter Origin: ");
                    String searchOrigin = sc.nextLine();
                    System.out.print("Enter Destination: ");
                    String searchDestination = sc.nextLine();
                    boolean flightFound = false;

                    System.out.println("Available flights from " + searchOrigin + " to " + searchDestination + ":");
                    for (Flight flight : flights) {
                        if (flight.getOrigin().equalsIgnoreCase(searchOrigin) && flight.getDestination().equalsIgnoreCase(searchDestination)) {
                            flightFound = true;
                            System.out.println("Flight ID: " + flight.getFlightId() + " | International: " + flight.isInternational());

                            // Booking process
                            System.out.print("Do you want to book this flight? (yes/no): ");
                            String bookingResponse = sc.nextLine();
                            if (bookingResponse.equalsIgnoreCase("yes")) {
                                System.out.print("Enter Traveller Name: ");
                                String travellerName = sc.nextLine();
                                System.out.print("Enter Number of Seats to Book: ");
                                int seatsRequested = sc.nextInt();
                                sc.nextLine(); // Consume newline
                                if (seatsRequested <= 0) {
                                    System.out.println("Error: Number of seats must be greater than zero.");
                                    break;
                                }

                                Traveller traveller = new Traveller(travellerName, flight, seatsRequested);

                                // Ask for meal preference
                                System.out.println("Choose meal preference:");
                                System.out.println("1. Vegetarian");
                                System.out.println("2. Non-Vegetarian");
                                int mealOption = sc.nextInt();
                                sc.nextLine(); // Consume newline

                                if (mealOption == 1) {
                                    traveller.setMealPreference("Vegetarian");
                                } else if (mealOption == 2) {
                                    traveller.setMealPreference("Non-Vegetarian");
                                } else {
                                    System.out.println("Invalid option. Defaulting to Vegetarian.");
                                    traveller.setMealPreference("Vegetarian");
                                }

                                // Complete booking process (no thread for simplicity)
                                if (flight.bookSeats(seatsRequested)) {
                                    System.out.println("Booking successful for " + travellerName + " on flight " + flight.getFlightId());
                                    System.out.println("Meal Preference: " + traveller.getMealPreference());
                                } else {
                                    System.out.println("Booking failed: Not enough available seats.");
                                }
                            }
                        }
                    }

                    if (!flightFound) {
                        System.out.println("No flights available from " + searchOrigin + " to " + searchDestination);
                    }
                    break;

                case 2:
                    // Log out and return to main menu
                    return;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }
}

