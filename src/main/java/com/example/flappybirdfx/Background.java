package com.example.flappybirdfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.flappybirdfx.Main.boardHeight;
import static com.example.flappybirdfx.Main.boardWidth;

public class Background extends ImageView {
    private static Image bck;

  public Background(){
    setImage(bck);
    setFitHeight(boardHeight);
    setFitWidth(boardWidth);
  }
    static {
        try {
            bck = new Image(Bird.class.getResourceAsStream("/img/flappybirdbg.png"));

        } catch (Exception e) {
            System.out.println("Failed to load images.");
        }
    }
}
