package com.example.flappybirdfx;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.flappybirdfx.Bird.*;
import static com.example.flappybirdfx.Main.boardHeight;
import static com.example.flappybirdfx.Main.boardWidth;

/**
 * FlappyBirds is a game class that manages the game mechanics, including the bird, pipes, scoring, and game over state.
 */
public class FlappyBirds extends Canvas {

    /** The bird character in the game. */
    Bird bird;
    /** The vertical velocity of the bird. */
    int velocityY = 0;
    /** The horizontal velocity of the pipes. */
    int velocityX = -4;
    /** The gravitational pull affecting the bird. */
    int gravity = 1;



    public int x = birdX;
    public int y = birdY;
    public int width = birdWith;
    public int height = birdHeight;

    /** The pipe obstacle in the game. */
    Pipe pipe;
    /** The background of the game. */
    Background bck;

    /** List of pipes currently in the game. */
    private ArrayList<Pipe> pipes;

    /** Indicates if the game is over. */
    private boolean gameOver = false;
    /** The current score of the player. */
    private double score = 0;

    /** The main game loop. */
    private AnimationTimer gameLoop;
    /** Timer for placing pipes. */
    private long lastPipeTime = 0; // To manage pipe placement timing

    /**
     * Constructor for FlappyBirds.
     * Initializes the game components, including the bird, pipes, and background.
     *
     * @param width The width of the game canvas.
     * @param height The height of the game canvas.
     */
    FlappyBirds(int width, int height) {
        super(width, height);
        this.bird = new Bird();
        this.bird.setY(birdY);
        this.bck = new Background();
        pipe = new Pipe(boardWidth, boardHeight, true);
        pipes = new ArrayList<Pipe>();

        setFocusTraversable(true);
        setOnKeyPressed(this::handleKeyPress);

        startGameLoop();
    }



    /**
     * Starts the main game loop, which updates the game state and renders graphics.
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) {
                    if (now - lastPipeTime > 1_500_000_000) {
                        placePipes();
                        lastPipeTime = now;
                    }
                    move();
                    draw();
                } else {
                    stop();
                }
            }
        };
        gameLoop.start();
    }

    /**
     * Updates the positions of the bird and pipes, checks for collisions, and manages scoring.
     */
    private void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); // Prevent bird from going above the screen
        bird.setY(bird.y); // Update the bird's Y position in the UI

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
            pipe.setX(pipe.x); // Update pipe position in the UI

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; // Increment score
            }

            if (collision(bird, pipe)) {
                gameOver = true; // End game on collision
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true; // End game if bird falls below screen
        }


        // Remove pipes that are out of screen
        pipes.removeIf(pipe -> pipe.x + pipe.width < 0);
    }

    /**
     * Renders the game graphics, including the background, bird, pipes, and score.
     */
    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, boardWidth, boardHeight);
        gc.drawImage(bck.getImage(), 0, 0, boardWidth, boardHeight);
        gc.drawImage(bird.getImage(), bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight());

        for (Pipe pipe : pipes) {
            gc.drawImage(pipe.getImage(), pipe.getX(), pipe.getY(), pipe.getWidth(), pipe.getHeight());
        }

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 32));
        gc.fillText(gameOver ? "Game Over: " + (int) score : String.valueOf((int) score), 10, 35);
    }

    /**
     * Checks for collision between the bird and a pipe.
     *
     * @param a The bird object.
     * @param b The pipe object.
     * @return True if there is a collision, false otherwise.
     */
    private boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    /**
     * Places new pipes at random vertical positions.
     */
    private void placePipes() {
        // Random Y position for the gap (leaving space for the opening)
        int openingSpace = boardHeight / 4; // Space between top and bottom pipe for the bird to pass through
        int maxPipeY = boardHeight - openingSpace - 200; // Ensure enough space for pipes and the gap
        int randomPipeY = (int) (Math.random() * maxPipeY); // Randomize position of the top pipe

        // Create the top pipe (upside-down)
        Pipe topPipe = new Pipe(boardWidth, randomPipeY - pipe.pipeHeight, true); // Push it above the screen
        pipes.add(topPipe);

        // Create the bottom pipe (regular orientation, below the top pipe)
        Pipe bottomPipe = new Pipe(boardWidth, randomPipeY + openingSpace, false);
        pipes.add(bottomPipe);
    }

    /**
     * Handles key press events, specifically for jumping and restarting the game.
     *
     * @param e The key event.
     */
    private void handleKeyPress(KeyEvent e) {
        if (e.getCode() == KeyCode.SPACE) {
            velocityY = -9; // Jump action
            if (gameOver) {
                int finalScore = (int) score; // Lagre scoren i en ny variabel
                DbConnection.insertScore(finalScore); // Insert the score into the database
                bird.y = birdY; // Reset bird position
                velocityY = 0; // Reset velocity
                pipes.clear(); // Clear pipes
                score = 0; // Reset score
                gameOver = false; // Restart game
                lastPipeTime = System.nanoTime(); // Reset pipe timing
                gameLoop.start(); // Restart game loop

            }
        }
    }
}