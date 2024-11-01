package com.airline.ui;

import com.airline.manager.Manager;
import com.airline.model.Flight;
import com.airline.model.Traveller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;


public class AirlineReservationSystemFX extends Application {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private List<Flight> flights = new ArrayList<>();
    private Manager manager = new Manager("John Doe", "manager123");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Airline Reservation System");
        showMainMenu(primaryStage);
    }

    private void showMainMenu(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Are you a Manager or a Customer?");
        Button managerButton = new Button("Manager");
        Button customerButton = new Button("Customer");
        Button exitButton = new Button("Exit");

        managerButton.setOnAction(e -> showManagerLogin(stage));
        customerButton.setOnAction(e -> showCustomerMenu(stage));
        exitButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(label, managerButton, customerButton, exitButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showManagerLogin(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Enter Manager Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button backButton = new Button("Back");

        loginButton.setOnAction(e -> {
            if (manager.validatePassword(passwordField.getText())) {
                showManagerMenu(stage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid password. Access denied.");
            }
        });

        backButton.setOnAction(e -> showMainMenu(stage));

        layout.getChildren().addAll(label, passwordField, loginButton, backButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
    }

    private void showManagerMenu(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Manager Menu:");
        Button addFlightButton = new Button("Add Flight");
        Button reportButton = new Button("Generate Flight Report");
        Button statisticsButton = new Button("View Statistics"); // New button for statistics menu
        Button logoutButton = new Button("Log Out");

        addFlightButton.setOnAction(e -> showAddFlightForm(stage));
        reportButton.setOnAction(e -> generateReport());
        statisticsButton.setOnAction(e -> showStatisticsMenu(stage)); // Show statistics menu
        logoutButton.setOnAction(e -> showMainMenu(stage));

        layout.getChildren().addAll(label, addFlightButton, reportButton, statisticsButton, logoutButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
    }

        private void showAddFlightForm(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField flightIdField = new TextField();
        TextField originField = new TextField();
        TextField destinationField = new TextField();
        ComboBox<Boolean> internationalBox = new ComboBox<>();
        internationalBox.getItems().addAll(true, false);
        TextField seatsField = new TextField();
        TextField fareField = new TextField();
        TextField dateField = new TextField();

        Button addButton = new Button("Add Flight");
        Button backButton = new Button("Back");

        layout.getChildren().addAll(
                new Label("Flight ID:"), flightIdField,
                new Label("Origin:"), originField,
                new Label("Destination:"), destinationField,
                new Label("Is International:"), internationalBox,
                new Label("Total Seats:"), seatsField,
                new Label("Base Fare:"), fareField,
                new Label("Flight Date (dd/MM/yyyy):"), dateField,
                addButton, backButton
        );

        addButton.setOnAction(e -> {
            try {
                manager.addFlight(flights, flightIdField.getText(), originField.getText(),
                        destinationField.getText(), internationalBox.getValue(),
                        Integer.parseInt(seatsField.getText()), Double.parseDouble(fareField.getText()), dateField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Flight added successfully!");
            } catch (NumberFormatException | ParseException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid input for seats, fare, or date.");
            }
        });

        backButton.setOnAction(e -> showManagerMenu(stage));

        Scene scene = new Scene(layout, 400, 600);
        stage.setScene(scene);
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder();
        report.append(String.format("%-12s %-15s %-15s %-15s %-12s %-12s %-10s %-12s\n",
                "Flight ID", "Origin", "Destination", "International",
                "Seats Booked", "Base Fare", "Seats Left", "Flight Date"));
        report.append("---------------------------------------------------------------------------------------------------------\n");

        for (Flight flight : flights) {
            int seatsBooked = flight.getTotalSeats() - flight.getAvailableSeats();
            report.append(String.format("%-12s %-15s %-15s %-15s %-12d $%-11.2f %-10d %-12s\n",
                    flight.getFlightId(), flight.getOrigin(), flight.getDestination(),
                    flight.isInternational(), seatsBooked, flight.getBaseFare(),
                    flight.getAvailableSeats(), flight.getFlightDate()));
        }
        showAlert(Alert.AlertType.INFORMATION, report.toString());
    }

    private void showCustomerMenu(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Customer Menu:");
        Button searchFlightsButton = new Button("Search Flights");
        Button bookFlightButton = new Button("Book Flight");
        Button logoutButton = new Button("Log Out");

        searchFlightsButton.setOnAction(e -> showSearchFlightsForm(stage));
        bookFlightButton.setOnAction(e -> showBookFlightForm(stage));
        logoutButton.setOnAction(e -> showMainMenu(stage));

        layout.getChildren().addAll(label, searchFlightsButton, bookFlightButton, logoutButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
    }

    private void showSearchFlightsForm(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField originField = new TextField();
        TextField destinationField = new TextField();
        Button searchButton = new Button("Search");
        TextArea resultsArea = new TextArea();
        resultsArea.setEditable(false);

        searchButton.setOnAction(e -> {
            resultsArea.clear();
            String origin = originField.getText();
            String destination = destinationField.getText();
            boolean flightsFound = false;

            for (Flight flight : flights) {
                if (flight.getOrigin().equalsIgnoreCase(origin) &&
                        flight.getDestination().equalsIgnoreCase(destination) &&
                        !flight.isFullCapacity()) {
                    resultsArea.appendText("Flight ID: " + flight.getFlightId() +
                            ", Date: " + flight.getFlightDate() +
                            ", Seats Available: " + flight.getAvailableSeats() +
                            ", Base Fare: $" + flight.getBaseFare() + "\n");
                    flightsFound = true;
                }
            }

            if (!flightsFound) {
                resultsArea.setText("No flights available for this route.");
            }
        });

        layout.getChildren().addAll(
                new Label("Origin:"), originField,
                new Label("Destination:"), destinationField,
                searchButton, resultsArea
        );

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
    }

    private void showBookFlightForm(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField flightIdField = new TextField();
        TextField seatsField = new TextField();
        Button bookButton = new Button("Book");

        bookButton.setOnAction(e -> {
            String flightId = flightIdField.getText();
            int seatsToBook;
            try {
                seatsToBook = Integer.parseInt(seatsField.getText());
                Flight selectedFlight = flights.stream()
                        .filter(flight -> flight.getFlightId().equals(flightId))
                        .findFirst().orElse(null);

                if (selectedFlight == null) {
                    showAlert(Alert.AlertType.ERROR, "Flight ID not found.");
                    return;
                }

                if (selectedFlight.isFullCapacity() || seatsToBook > selectedFlight.getAvailableSeats()) {
                    showAlert(Alert.AlertType.ERROR, "Not enough seats available.");
                    return;
                }

                selectedFlight.bookSeats(seatsToBook);
                showAlert(Alert.AlertType.INFORMATION, "Booking successful!");
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Please enter a valid number of seats.");
            }
        });

        layout.getChildren().addAll(
                new Label("Flight ID:"), flightIdField,
                new Label("Seats to Book:"), seatsField,
                bookButton
        );

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showStatisticsMenu(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Flight Statistics:");

        // Get statistics from the FlightStatistics class
        Map<String, Long> flightsByOrigin = FlightStatistics.countFlightsByOrigin(flights);
        double averageFare = FlightStatistics.calculateAverageFare(flights);
        int totalAvailableSeats = FlightStatistics.countAvailableSeats(flights);

        // Display each statistic
        StringBuilder statsText = new StringBuilder("Flights by Origin:\n");
        flightsByOrigin.forEach((origin, count) ->
                statsText.append(String.format("%s: %d flights\n", origin, count))
        );

        statsText.append(String.format("\nAverage Fare: $%.2f\n", averageFare));
        statsText.append(String.format("Total Available Seats: %d\n", totalAvailableSeats));

        TextArea statsArea = new TextArea(statsText.toString());
        statsArea.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showManagerMenu(stage));

        layout.getChildren().addAll(label, statsArea, backButton);
        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
    }
}




public class AirlineReservationSystemFX extends Application {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private List<Flight> flights = new ArrayList<>();
    private Manager manager = new Manager("John Doe", "manager123");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Airline Reservation System");
        showMainMenu(primaryStage);
    }

    private void showMainMenu(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Are you a Manager or a Customer?");
        Button managerButton = new Button("Manager");
        Button customerButton = new Button("Customer");
        Button exitButton = new Button("Exit");

        managerButton.setOnAction(e -> showManagerLogin(stage));
        customerButton.setOnAction(e -> showCustomerMenu(stage));
        exitButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(label, managerButton, customerButton, exitButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showManagerLogin(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Enter Manager Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button backButton = new Button("Back");

        loginButton.setOnAction(e -> {
            if (manager.validatePassword(passwordField.getText())) {
                showManagerMenu(stage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid password. Access denied.");
            }
        });

        backButton.setOnAction(e -> showMainMenu(stage));

        layout.getChildren().addAll(label, passwordField, loginButton, backButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
    }

    private void showManagerMenu(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Manager Menu:");
        Button addFlightButton = new Button("Add Flight");
        Button reportButton = new Button("Generate Flight Report");
        Button statisticsButton = new Button("View Statistics"); // New button for statistics menu
        Button logoutButton = new Button("Log Out");

        addFlightButton.setOnAction(e -> showAddFlightForm(stage));
        reportButton.setOnAction(e -> generateReport());
        statisticsButton.setOnAction(e -> showStatisticsMenu(stage)); // Show statistics menu
        logoutButton.setOnAction(e -> showMainMenu(stage));

        layout.getChildren().addAll(label, addFlightButton, reportButton, statisticsButton, logoutButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
    }

        private void showAddFlightForm(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField flightIdField = new TextField();
        TextField originField = new TextField();
        TextField destinationField = new TextField();
        ComboBox<Boolean> internationalBox = new ComboBox<>();
        internationalBox.getItems().addAll(true, false);
        TextField seatsField = new TextField();
        TextField fareField = new TextField();
        TextField dateField = new TextField();

        Button addButton = new Button("Add Flight");
        Button backButton = new Button("Back");

        layout.getChildren().addAll(
                new Label("Flight ID:"), flightIdField,
                new Label("Origin:"), originField,
                new Label("Destination:"), destinationField,
                new Label("Is International:"), internationalBox,
                new Label("Total Seats:"), seatsField,
                new Label("Base Fare:"), fareField,
                new Label("Flight Date (dd/MM/yyyy):"), dateField,
                addButton, backButton
        );

        addButton.setOnAction(e -> {
            try {
                manager.addFlight(flights, flightIdField.getText(), originField.getText(),
                        destinationField.getText(), internationalBox.getValue(),
                        Integer.parseInt(seatsField.getText()), Double.parseDouble(fareField.getText()), dateField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Flight added successfully!");
            } catch (NumberFormatException | ParseException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid input for seats, fare, or date.");
            }
        });

        backButton.setOnAction(e -> showManagerMenu(stage));

        Scene scene = new Scene(layout, 400, 600);
        stage.setScene(scene);
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder();
        report.append(String.format("%-12s %-15s %-15s %-15s %-12s %-12s %-10s %-12s\n",
                "Flight ID", "Origin", "Destination", "International",
                "Seats Booked", "Base Fare", "Seats Left", "Flight Date"));
        report.append("---------------------------------------------------------------------------------------------------------\n");

        for (Flight flight : flights) {
            int seatsBooked = flight.getTotalSeats() - flight.getAvailableSeats();
            report.append(String.format("%-12s %-15s %-15s %-15s %-12d $%-11.2f %-10d %-12s\n",
                    flight.getFlightId(), flight.getOrigin(), flight.getDestination(),
                    flight.isInternational(), seatsBooked, flight.getBaseFare(),
                    flight.getAvailableSeats(), flight.getFlightDate()));
        }
        showAlert(Alert.AlertType.INFORMATION, report.toString());
    }

    private void showCustomerMenu(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Customer Menu:");
        Button searchFlightsButton = new Button("Search Flights");
        Button bookFlightButton = new Button("Book Flight");
        Button logoutButton = new Button("Log Out");

        searchFlightsButton.setOnAction(e -> showSearchFlightsForm(stage));
        bookFlightButton.setOnAction(e -> showBookFlightForm(stage));
        logoutButton.setOnAction(e -> showMainMenu(stage));

        layout.getChildren().addAll(label, searchFlightsButton, bookFlightButton, logoutButton);
        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
    }

    private void showSearchFlightsForm(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField originField = new TextField();
        TextField destinationField = new TextField();
        Button searchButton = new Button("Search");
        TextArea resultsArea = new TextArea();
        resultsArea.setEditable(false);

        searchButton.setOnAction(e -> {
            resultsArea.clear();
            String origin = originField.getText();
            String destination = destinationField.getText();
            boolean flightsFound = false;

            for (Flight flight : flights) {
                if (flight.getOrigin().equalsIgnoreCase(origin) &&
                        flight.getDestination().equalsIgnoreCase(destination) &&
                        !flight.isFullCapacity()) {
                    resultsArea.appendText("Flight ID: " + flight.getFlightId() +
                            ", Date: " + flight.getFlightDate() +
                            ", Seats Available: " + flight.getAvailableSeats() +
                            ", Base Fare: $" + flight.getBaseFare() + "\n");
                    flightsFound = true;
                }
            }

            if (!flightsFound) {
                resultsArea.setText("No flights available for this route.");
            }
        });

        layout.getChildren().addAll(
                new Label("Origin:"), originField,
                new Label("Destination:"), destinationField,
                searchButton, resultsArea
        );

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
    }

    private void showBookFlightForm(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField flightIdField = new TextField();
        TextField seatsField = new TextField();
        Button bookButton = new Button("Book");

        bookButton.setOnAction(e -> {
            String flightId = flightIdField.getText();
            int seatsToBook;
            try {
                seatsToBook = Integer.parseInt(seatsField.getText());
                Flight selectedFlight = flights.stream()
                        .filter(flight -> flight.getFlightId().equals(flightId))
                        .findFirst().orElse(null);

                if (selectedFlight == null) {
                    showAlert(Alert.AlertType.ERROR, "Flight ID not found.");
                    return;
                }

                if (selectedFlight.isFullCapacity() || seatsToBook > selectedFlight.getAvailableSeats()) {
                    showAlert(Alert.AlertType.ERROR, "Not enough seats available.");
                    return;
                }

                selectedFlight.bookSeats(seatsToBook);
                showAlert(Alert.AlertType.INFORMATION, "Booking successful!");
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Please enter a valid number of seats.");
            }
        });

        layout.getChildren().addAll(
                new Label("Flight ID:"), flightIdField,
                new Label("Seats to Book:"), seatsField,
                bookButton
        );

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showStatisticsMenu(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label label = new Label("Flight Statistics:");

        // Get statistics from the FlightStatistics class
        Map<String, Long> flightsByOrigin = FlightStatistics.countFlightsByOrigin(flights);
        double averageFare = FlightStatistics.calculateAverageFare(flights);
        int totalAvailableSeats = FlightStatistics.countAvailableSeats(flights);

        // Display each statistic
        StringBuilder statsText = new StringBuilder("Flights by Origin:\n");
        flightsByOrigin.forEach((origin, count) ->
                statsText.append(String.format("%s: %d flights\n", origin, count))
        );

        statsText.append(String.format("\nAverage Fare: $%.2f\n", averageFare));
        statsText.append(String.format("Total Available Seats: %d\n", totalAvailableSeats));

        TextArea statsArea = new TextArea(statsText.toString());
        statsArea.setEditable(false);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showManagerMenu(stage));

        layout.getChildren().addAll(label, statsArea, backButton);
        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
    }
}

