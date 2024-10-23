package com.airline.model;

public class Flight {
    private String flightId;
    private String origin;
    private String destination;
    private boolean isInternational;
    private int totalSeats;
    private int availableSeats;
    private double baseFare;
    private boolean fullCapacity;

    public Flight(String flightId, String origin, String destination, boolean isInternational, int totalSeats, double baseFare) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.isInternational = isInternational;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.baseFare = baseFare;
        this.fullCapacity = false;
    }

    // Getter for totalSeats
    public int getTotalSeats() {
        return totalSeats;
    }

    // Getter for availableSeats
    public int getAvailableSeats() {
        return availableSeats;
    }

    // Getter for flightId
    public String getFlightId() {
        return flightId;
    }

    // Getter for origin
    public String getOrigin() {
        return origin;
    }

    // Getter for destination
    public String getDestination() {
        return destination;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public synchronized boolean bookSeats(int seatsRequested) {
        if (seatsRequested > availableSeats) {
            return false; // Not enough seats available
        } else {
            availableSeats -= seatsRequested;
            if (availableSeats == 0) {
                fullCapacity = true;
            }
            return true; // Seats successfully booked
        }
    }

    public double calculatePrice(int seatsRequested) {
        double dynamicFareMultiplier = (1 + (0.1 * (totalSeats - availableSeats) / totalSeats));
        return baseFare * seatsRequested * dynamicFareMultiplier;
    }

    public boolean isFullCapacity() {
        return fullCapacity;
    }

    public String isInternational() {
        return String.valueOf(isInternational);
    }
}
