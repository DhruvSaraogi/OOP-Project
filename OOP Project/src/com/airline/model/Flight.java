package com.airline.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Flight {
    private String flightId;
    private String origin;
    private String destination;
    private boolean isInternational;
    private int totalSeats;
    private int availableSeats;
    private double baseFare;
    private boolean fullCapacity;
    private Date flightDate;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public Flight(String flightId, String origin, String destination, boolean isInternational,
                  int totalSeats, double baseFare, String flightDate) throws ParseException {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.isInternational = isInternational;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.baseFare = baseFare;
        this.fullCapacity = false;
        this.flightDate = dateFormat.parse(flightDate);
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public String getFlightDate() {
        return dateFormat.format(flightDate);
    }

    public synchronized boolean bookSeats(int seatsRequested) {
        if (seatsRequested > availableSeats) {
            return false;
        } else {
            availableSeats -= seatsRequested;
            if (availableSeats == 0) {
                fullCapacity = true;
            }
            return true;
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


