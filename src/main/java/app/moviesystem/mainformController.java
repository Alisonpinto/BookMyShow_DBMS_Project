package app.moviesystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.fxml.Initializable; // CRITICAL: Must implement this interface
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import java.util.List;

public class mainformController implements Initializable{

    @FXML
    private Button a1;

    @FXML
    private Button a2;

    @FXML
    private Button a3;

    @FXML
    private Button b1;

    @FXML
    private Button b2;

    @FXML
    private Button b3;

    @FXML
    private Button b4;

    @FXML
    private Button b5;

    @FXML
    private Button backbtn;

    @FXML
    private Button bookbtn1;

    @FXML
    private Button bookbtn2;

    @FXML
    private Button bookbtn3;

    @FXML
    private Button c1;

    @FXML
    private Button c2;

    @FXML
    private Button c3;

    @FXML
    private Button c4;

    @FXML
    private Button c5;

    @FXML
    private ImageView fimg;

    @FXML
    private AnchorPane finalform;

    @FXML
    private Label fmname;

    @FXML
    private Label fprice;

    @FXML
    private Label fseat;

    @FXML
    private AnchorPane homeform;

    @FXML
    private ImageView img1edit;

    @FXML
    private ImageView img2edit;

    @FXML
    private ImageView img3edit;

    @FXML
    private Label mlabel1;

    @FXML
    private Label mlabel2;

    @FXML
    private Label mlabel3;

    @FXML
    private Button paybtn;

    @FXML
    private AnchorPane payform;

    @FXML
    private ImageView payimg;

    @FXML
    private Label paymname;

    @FXML
    private Label paymprice;

    @FXML
    private Button paypage;

    @FXML
    private Label screen_no;

    @FXML
    private AnchorPane seatform;

    private List<Button> allSeatButtons;

    // Store the name of the currently selected movie
    private String selectedMovieName;
    // Store the ID of the currently selected movie
    private int selectedMovieId;
    // Store the labels of the seats selected by the user
    private List<String> selectedSeats = new ArrayList<>();
    // Price per seat (using your example price)
    private final double PRICE_PER_SEAT = 300.00;

// You need a way to get the logged-in UserID (assuming it's stored in getData)
// private int currentUserId; // Uncomment if you implement UserID retrieval
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Populate the list of all seat buttons
        allSeatButtons = Arrays.asList(
                a1, a2, a3,
                b1, b2, b3, b4, b5,
                c1, c2, c3, c4, c5
        );
        resetSeatStyles();

        // 2. Load Home Page Movie Posters
        loadHomePosters();
    }

    public void switchForm(ActionEvent event){

        if(event.getSource() == bookbtn1 || event.getSource() == bookbtn2 || event.getSource() == bookbtn3){

            // 1. Determine which movie was clicked (Keep existing logic)
            Button clickedButton = (Button) event.getSource();

            if (clickedButton == bookbtn1) {
                selectedMovieName = mlabel1.getText();
                selectedMovieId = 1;
            } else if (clickedButton == bookbtn2) {
                selectedMovieName = mlabel2.getText();
                selectedMovieId = 2;
            } else if (clickedButton == bookbtn3) {
                selectedMovieName = mlabel3.getText();
                selectedMovieId = 3;
            }

            // 2. Reset UI and switch scene
            selectedSeats.clear();
            resetSeatStyles(); // Reset all seats to AVAILABLE visually

            // 3. Load Booked Status for the selected movie
            loadBookedSeats(); // <--- NEW METHOD CALL

            homeform.setVisible(false);
            seatform.setVisible(true);
            payform.setVisible(false);
            finalform.setVisible(false);

        } else if(event.getSource() == backbtn){
            // Go back, clear selections, and reset styles
            selectedSeats.clear();
            resetSeatStyles();

            homeform.setVisible(true);
            seatform.setVisible(false);
            payform.setVisible(false);
            finalform.setVisible(false);
        }
    }

    public void handleSeatSelection(ActionEvent event) {
        Button clickedSeat = (Button) event.getSource();
        String seatLabel = clickedSeat.getText();

        if (selectedSeats.contains(seatLabel)) {
            // Seat is already selected, so deselect it
            selectedSeats.remove(seatLabel);
            // Reset to default style (assuming default is a light gray)
            clickedSeat.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: black;");
            System.out.println("❌ Seat " + seatLabel + " deselected.");
        } else {
            // Seat is not selected, so select it
            selectedSeats.add(seatLabel);
            // Apply selected style
            clickedSeat.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
            System.out.println("✅ Seat " + seatLabel + " selected.");
        }

        // You might want to update a label on the seat form showing selected seats/price
        // e.g., screen_no.setText("Selected Seats: " + String.join(", ", selectedSeats) +
        //                         " | Total Price: ₹" + (selectedSeats.size() * PRICE_PER_SEAT));
    }

    public void goToPayment(ActionEvent event) {

        if (selectedSeats.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection Required");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one seat before proceeding to payment.");
            alert.showAndWait();
            return;
        }

        // 1. Calculate price
        double totalPrice = selectedSeats.size() * PRICE_PER_SEAT;

        // 2. Update Pay Form UI elements
        paymname.setText("Movie : " + selectedMovieName);
        paymprice.setText("Price : ₹ " + String.format("%.2f", totalPrice));

        // --- ADD POSTER IMAGE LOADING HERE ---
        String imagePath = getMoviePoster(selectedMovieId);
        if (imagePath != null) {
            payimg.setImage(new Image(getClass().getResourceAsStream("/app/moviesystem/images/" + imagePath)));
        }

        // 3. Switch to Pay Form
        homeform.setVisible(false);
        seatform.setVisible(false);
        payform.setVisible(true); // Show the payform
        finalform.setVisible(false);
    }

    public void handleFinalPayment(ActionEvent event) {

        // Assuming a successful 'payment' (since this is a project)

        double totalPrice = selectedSeats.size() * PRICE_PER_SEAT;
        int userId = getData.userId; // **CRITICAL:** Replace with the actual logged-in user ID from getData

        try (Connection conn = database.connectDb()) {

            // 1. Insert into Booking table
            String sqlBooking = "INSERT INTO Booking (No_of_Seats, UserID, MovieID) VALUES (?, ?, ?)";
            int bookingId = -1;

            try (PreparedStatement stmtBooking = conn.prepareStatement(sqlBooking, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtBooking.setInt(1, selectedSeats.size());
                stmtBooking.setInt(2, userId);
                stmtBooking.setInt(3, selectedMovieId);
                stmtBooking.executeUpdate();

                ResultSet rs = stmtBooking.getGeneratedKeys();
                if (rs.next()) {
                    bookingId = rs.getInt(1); // Get the newly created BookingID
                } else {
                    throw new SQLException("Failed to retrieve generated booking ID.");
                }
            }

            // 2. Insert into Payment table
            String sqlPayment = "INSERT INTO Payment (Amount, BookingID) VALUES (?, ?)";
            try (PreparedStatement stmtPayment = conn.prepareStatement(sqlPayment)) {
                stmtPayment.setDouble(1, totalPrice);
                stmtPayment.setInt(2, bookingId);
                stmtPayment.executeUpdate();
            }

            // 3. Update Seat status (using the combined SeatLabel + MovieID to represent a unique screening seat)
            // **IMPORTANT:** Your current Seat table design has SeatID as PK, not SeatLabel.
            // For this to work, you MUST change the Seat table to:
            // CREATE TABLE Seat (
            //   SeatLabel VARCHAR(5) NOT NULL,
            //   MovieID INT NOT NULL,
            //   Status ENUM('Available', 'Booked') NOT NULL,
            //   PRIMARY KEY (SeatLabel, MovieID),
            //   FOREIGN KEY (MovieID) REFERENCES Movie(MovieID)
            // );

            String sqlSeatUpdate = "INSERT INTO Seat (SeatLabel, Status, MovieID) VALUES (?, 'Booked', ?) ON DUPLICATE KEY UPDATE Status='Booked'";
            try (PreparedStatement stmtSeat = conn.prepareStatement(sqlSeatUpdate)) {
                for (String seat : selectedSeats) {
                    stmtSeat.setString(1, seat);
                    stmtSeat.setInt(2, selectedMovieId);
                    stmtSeat.addBatch(); // Add to batch for efficiency
                }
                stmtSeat.executeBatch();
            }

            // 4. Update Final Form UI
            fmname.setText("Movie : " + selectedMovieName);
            fseat.setText("Seats : " + selectedSeats.size());
            fprice.setText("Price : ₹ " + String.format("%.2f", totalPrice));
            // Update fimg as well

            // --- ADD FINAL POSTER IMAGE LOADING HERE ---
            String imagePath = getMoviePoster(selectedMovieId);
            if (imagePath != null) {
                fimg.setImage(new Image(getClass().getResourceAsStream("/app/moviesystem/images/" + imagePath)));
            }

            // 5. Switch to Final Form
            homeform.setVisible(false);
            seatform.setVisible(false);
            payform.setVisible(false);
            finalform.setVisible(true);

            System.out.println("✅ Booking successful! Booking ID: " + bookingId);

        } catch (SQLException e) {
            e.printStackTrace();
            // Show an error to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Booking Failed");
            alert.setContentText("An error occurred during the booking process. Please try again.");
            alert.showAndWait();
        }
    }
    // Use the MovieID to get the Poster filename from the database
    private String getMoviePoster(int movieId) {
        String sql = "SELECT Poster FROM Movie WHERE MovieID = ?";
        try (Connection conn = database.connectDb();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Poster");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found
    }

    // Loads the posters onto the Home Screen (using MovieIDs 1, 2, 3)
    private void loadHomePosters() {
        // Note: If you want to make the movie IDs dynamic, you'd query the entire Movie table here.
        try {
            String poster1 = getMoviePoster(1); // JAWs
            String poster2 = getMoviePoster(2); // ROCKY
            String poster3 = getMoviePoster(3); // 2001

            if (poster1 != null) img1edit.setImage(new Image(getClass().getResourceAsStream("/images/" + poster1)));
            if (poster2 != null) img2edit.setImage(new Image(getClass().getResourceAsStream("/images/" + poster2)));
            if (poster3 != null) img3edit.setImage(new Image(getClass().getResourceAsStream("/images/" + poster3)));

        } catch (Exception e) {
            System.err.println("ERROR: Could not find movie poster file(s). Check path /images/ and filenames.");
            e.printStackTrace();
        }
    }
    private void loadBookedSeats() {
        // Query the database for seats already booked for this movie
        String sql = "SELECT SeatLabel FROM Seat WHERE MovieID = ? AND Status = 'Booked'";

        try (Connection conn = database.connectDb();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, selectedMovieId);
            ResultSet rs = stmt.executeQuery();

            // Find the booked seat labels and apply the 'Booked' style
            while (rs.next()) {
                String bookedSeatLabel = rs.getString("SeatLabel");

                // Iterate over all UI buttons to find the match
                for (Button seatButton : allSeatButtons) {
                    if (seatButton.getText().equals(bookedSeatLabel)) {
                        // Seat is booked in the DB, set its style to red and disable it
                        seatButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                        seatButton.setDisable(true);
                        break; // Found the button, move to the next result set row
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to Load Seats");
            alert.setContentText("Could not retrieve seat availability from the database.");
            alert.showAndWait();
        }
    }
    private void resetSeatStyles() {
        // Reset the style of all buttons to the default available look
        if (allSeatButtons != null) {
            for (Button button : allSeatButtons) {
                // Default style (light gray/silver background with black text)
                button.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: black;");
                // Also re-enable the button, as it might have been disabled if previously booked
                button.setDisable(false);
            }
        }
    }
}

