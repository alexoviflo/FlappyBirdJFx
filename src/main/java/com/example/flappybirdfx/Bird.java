package com.example.flappybirdfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.flappybirdfx.Main.boardHeight;
import static com.example.flappybirdfx.Main.boardWidth;

public class Bird extends ImageView {



   public static int birdX = boardWidth / 8;
    public static int birdY = boardHeight / 2;
    public static  int birdWith = 34;
    public static  int birdHeight = 24;
    static Image img;

    int x = birdX;
    int y = birdY;
    int width = birdWith;
    int height = birdHeight;

    Bird(){
        setImage(img);
        setX(x);
        setY(y);
        setFitWidth(width);
        setFitHeight(height);

    }

    static {
        try {
            img = new Image(Bird.class.getResourceAsStream("/img/flappybird.png"));

        } catch (Exception e) {
            System.out.println("Failed to load images.");
        }
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }


}
