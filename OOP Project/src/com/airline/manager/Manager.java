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

    public void addFlight(List<Flight> flights, String flightId, String origin, String destination,
                          boolean isInternational, int totalSeats, double baseFare, String flightDate) {
        if (totalSeats <= 0 || baseFare <= 0) {
            System.out.println("Error: Total seats and base fare must be greater than zero.");
            return;
        }
        try {
            Flight newFlight = new Flight(flightId, origin, destination, isInternational,
                    totalSeats, baseFare, flightDate);
            flights.add(newFlight);
            System.out.println("Flight added: " + flightId + " on " + flightDate);
        } catch (ParseException e) {
            System.out.println("Error: Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public void generateReport(List<Flight> flights) {
        boolean anyFullCapacity = false;

        System.out.printf("%-12s %-15s %-15s %-15s %-12s %-12s %-10s %-12s\n",
                "Flight ID", "Origin", "Destination", "International",
                "Seats Booked", "Base Fare", "Seats Left", "Flight Date");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        for (Flight flight : flights) {
            int seatsBooked = flight.getTotalSeats() - flight.getAvailableSeats();
            double fare = flight.getBaseFare();
            String internationalStatus = flight.isInternational();
            String flightDate = flight.getFlightDate();

            System.out.printf("%-12s %-15s %-15s %-15s %-12d $%-11.2f %-10d %-12s\n",
                    flight.getFlightId(), flight.getOrigin(), flight.getDestination(),
                    internationalStatus, seatsBooked, fare, flight.getAvailableSeats(),
                    flightDate);

            if (flight.isFullCapacity()) {
                anyFullCapacity = true;
            }
        }

        if (!anyFullCapacity && flights.isEmpty()) {
            System.out.println("No flights to display.");
        }
    }
}

