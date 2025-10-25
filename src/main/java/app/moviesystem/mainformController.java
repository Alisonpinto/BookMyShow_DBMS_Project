package app.moviesystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class mainformController {

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
    private Label screen_no;

    @FXML
    private AnchorPane seatform;

    public void switchForm(ActionEvent event){

        if(event.getSource() == bookbtn1 || event.getSource() == bookbtn2 || event.getSource() == bookbtn3){
            homeform.setVisible(false);
            seatform.setVisible(true);
            payform.setVisible(false);
            finalform.setVisible(false);
        }else if(event.getSource() == backbtn){
            homeform.setVisible(true);
            seatform.setVisible(false);
            payform.setVisible(false);
            finalform.setVisible(false);
        }
    }

    public void handleSeatSelection(ActionEvent event) {
        Button clickedSeat = (Button) event.getSource();
        String seatLabel = clickedSeat.getText();
        int movieId = 1; // can be dynamic

        String sqlUpdate = "UPDATE seat SET Status='Booked' WHERE SeatLabel=? AND MovieID=?";
        String sqlInsert = "INSERT INTO seat (SeatLabel, Status, MovieID) VALUES (?, 'Booked', ?)";

        try (Connection conn = database.connectDb()) {
            // Try update first
            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
                stmt.setString(1, seatLabel);
                stmt.setInt(2, movieId);
                int rows = stmt.executeUpdate();

                // If no rows updated, insert new seat
                if (rows == 0) {
                    try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                        insertStmt.setString(1, seatLabel);
                        insertStmt.setInt(2, movieId);
                        insertStmt.executeUpdate();
                    }
                }
            }

            // Update UI
            clickedSeat.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            System.out.println("✅ Seat " + seatLabel + " booked for movie " + movieId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

