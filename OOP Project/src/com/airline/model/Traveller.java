package com.airline.model;

public class Traveller implements Runnable {
    private String name;
    private Flight flight;
    private int seatsRequested;

    public Traveller(String name, Flight flight, int seatsRequested) {
        this.name = name;
        this.flight = flight;
        this.seatsRequested = seatsRequested;
    }

    @Override
    public void run() {
        if (flight.bookSeats(seatsRequested)) {
            double totalPrice = flight.calculatePrice(seatsRequested);
            System.out.println("Traveller " + name + " booked " + seatsRequested + " seats on Flight " + flight.getFlightId() + ". Total Price: $" + totalPrice);
            if (flight.isFullCapacity()) {
                System.out.println("Alert: Flight " + flight.getFlightId() + " is now fully booked.");
            }
        } else {
            System.out.println("Traveller " + name + " failed to book " + seatsRequested + " seats on Flight " + flight.getFlightId() + ": Not enough seats available.");
        }
    }
}
