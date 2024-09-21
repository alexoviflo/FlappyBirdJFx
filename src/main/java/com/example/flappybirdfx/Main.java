package com.example.flappybirdfx;

import com.example.flappybirdfx.FlappyBirds;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    public static int boardWidth = 360;
   public static int boardHeight = 640;

   public static FlappyBirds flappyBirds = new FlappyBirds(boardWidth, boardHeight);

    @Override
    public void start(Stage primaryStage) {

        DbConnection.connect();
        DbConnection.printScores();


        Button showScoreBtn = new Button("Show Score");
        showScoreBtn.setOnAction(e -> {
            DbConnection.printScores();
            flappyBirds.requestFocus();

        });
        showScoreBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-border-radius: 5px;");


        HBox hBox = new HBox();

        // Legg til en Region for å fylle opp plassen til venstre
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); // Fyller opp all tilgjengelig plass

        hBox.getChildren().addAll(spacer, showScoreBtn);
        hBox.setStyle("-fx-padding: 10;");

        // Wrap the FlappyBirds canvas inside a Group, which extends Parent
        BorderPane root = new BorderPane();
        root.getChildren().addAll(flappyBirds);
         root.setRight(hBox);

        Scene scene = new Scene(root, boardWidth, boardHeight);

        primaryStage.setTitle("Flappy Bird");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        flappyBirds.requestFocus(); // Ensure the canvas captures key input
    }

    public static void main(String[] args) {
        launch(args); // Start JavaFX application
    }
}