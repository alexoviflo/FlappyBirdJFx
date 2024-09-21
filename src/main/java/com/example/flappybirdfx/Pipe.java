package com.example.flappybirdfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

import static com.example.flappybirdfx.Main.boardHeight;
import static com.example.flappybirdfx.Main.boardWidth;

public class Pipe extends ImageView {


    private static Image topImg;
    private static Image bottomImg;

    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    boolean passed = false;


   public int x;
    public int y;
    int width = pipeWidth;
    int height = pipeHeight;


    Pipe(int initialX, int initialY, boolean isTopPipe){
        this.x = initialX;
        this.y = initialY;
        if(isTopPipe){
            setImage(topImg);
            setRotate(180);
        } else {
            setImage(bottomImg);
        }

        setY(y);
        setX(x);
        setFitHeight(pipeHeight);
        setFitWidth(pipeWidth);
    }

    static {
        try {
            topImg = new Image(Bird.class.getResourceAsStream("/img/toppipe.png"));
            bottomImg = new Image(Bird.class.getResourceAsStream("/img/bottompipe.png"));

        } catch (Exception e) {
            System.out.println("Failed to load images.");
        }
    }

    public double getWidth() {
        return getFitWidth();
    }

    public double getHeight() {
        return getFitHeight();
    }
}
