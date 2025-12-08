package com.comp2042.controller;

import com.comp2042.model.HighScore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the home screen/main menu.
 * Handles navigation between the home screen and game modes (Classic and Sprint),
 * displays high score, and manages application exit.
 */
public class HomeController {

    @FXML
    private Label highScoreDisplay;

    private final HighScore highScore = new HighScore();

    /**
     * Starts a new classic mode game.
     * Called when the "Classic Mode" button is clicked.
     * 
     * @param event the action event from the button click
     */
    @FXML
    public void startClassicGame(ActionEvent event) {
        startGameWithMode(com.comp2042.model.GameMode.CLASSIC);
    }

    /**
     * Starts a new sprint mode game.
     * Called when the "Sprint" button is clicked.
     * 
     * @param event the action event from the button click
     */
    @FXML
    public void startSprintGame(ActionEvent event) {
        startGameWithMode(com.comp2042.model.GameMode.SPRINT);
    }

    /**
     * Loads the game layout and starts a new game with the specified mode.
     * 
     * @param mode the game mode to start (CLASSIC or SPRINT)
     */
    private void startGameWithMode(com.comp2042.model.GameMode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent gameRoot = loader.load();
            GuiController guiController = loader.getController();

            Stage stage = (Stage) highScoreDisplay.getScene().getWindow();
            Scene gameScene = new Scene(gameRoot, 600, 510);
            stage.setScene(gameScene);

            new GameController(guiController, mode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the current high score on the home screen.
     * Called when the "High Score" button is clicked.
     * 
     * @param event the action event from the button click
     */
    @FXML
    public void showHighScore(ActionEvent event) {
        if (highScoreDisplay != null) {
            highScoreDisplay.setText("Best Score: " + highScore.getHighScore());
            highScoreDisplay.setVisible(true);
        }
    }

    /**
     * Exits the application.
     * Called when the "Exit" button is clicked.
     * 
     * @param event the action event from the button click
     */
    @FXML
    public void exitGame(ActionEvent event) {
        javafx.application.Platform.exit();
    }
}