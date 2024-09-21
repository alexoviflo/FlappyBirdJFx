package com.example.flappybirdfx;

import javafx.scene.control.Alert;

import java.awt.*;
import java.sql.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import static com.example.flappybirdfx.Main.flappyBirds;


public class DbConnection {

    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:flappybird.db");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return con;
    }

    public static void printScores() {
        StringBuilder query = new StringBuilder();
        try (Connection con = connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM scores ORDER BY score DESC LIMIT 10")) {

            System.out.println("Top 10 scores");
            while (rs.next()) {
                int id = rs.getInt("id");
                int score = rs.getInt("score");
                query.append("ID: ").append(id).append(", Score: ").append(score).append("\n");
            }
        } catch (SQLException e) {
            System.out.print("Error: " + e.getMessage());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Top 10 Scores");
        alert.setHeaderText(null);
        alert.setContentText(query.toString());

        // Legg til en stil
        alert.getDialogPane().setStyle("-fx-background-color: #f0f0f0; -fx-font-size: 14px;");
        alert.getDialogPane().getStylesheets().add("your_stylesheet.css"); // Legg til en CSS-fil om ønskelig

        Button closeButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        closeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 5px;");

        alert.setOnHidden(event -> {
            flappyBirds.requestFocus();
        });

        alert.showAndWait();
    }


    public static void insertScore(int score) {
        String query = "INSERT INTO scores (score) VALUES (?)";
        try (Connection con = connect();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Error: " + e.getMessage());
        }
    }

}
