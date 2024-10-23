package com.airline.manager

import com.airline.model.Flight;
import java.util.*;

public class Manager {
    
    private String name;
    private String password;

    public Manager(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean validatePassword(String inputPassword) {
        return password.equals(inputPassword);
    }

    public void addFlight(List<Flight> flights, String flightId, String origin, String destination, boolean isInternational, int totalSeats, double baseFare) {
        if (totalSeats <= 0 || baseFare <= 0) {
            System.out.println("Error: Total seats and base fare must be greater than zero.");
            return;
        }
        Flight newFlight = new Flight(flightId, origin, destination, isInternational, totalSeats, baseFare);
        flights.add(newFlight);
        System.out.println("Flight added: " + flightId);
    }

    public void generateReport(List<Flight> flights) {
        boolean anyFullCapacity = false;

        // Header of the table, added a column for International status
        System.out.printf("%-12s %-15s %-15s %-15s %-12s %-12s %-10s\n", "Flight ID", "Origin", "Destination", "International", "Seats Booked", "Base Fare", "Seats Left");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        for (Flight flight : flights) {
            int seatsBooked = flight.getTotalSeats() - flight.getAvailableSeats();
            double fare = flight.getBaseFare();  // Print base fare of the flight
            String internationalStatus = flight.isInternational(); // Determine international status

            // Print each flight's details in a table format, including international status
            System.out.printf("%-12s %-15s %-15s %-15s %-12d $%-11.2f %-10d\n",
                    flight.getFlightId(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    internationalStatus,
                    seatsBooked,
                    fare,
                    flight.getAvailableSeats());

            anyFullCapacity = true;
        }

        if (!anyFullCapacity) {
            System.out.println("No flights to display.");
        }
    }    
}
