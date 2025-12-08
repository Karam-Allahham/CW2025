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


public class HomeController {

    @FXML
    private Label highScoreDisplay;

    private final HighScore highScore = new HighScore();

    @FXML
    public void startClassicGame(ActionEvent event) {
        startGameWithMode(com.comp2042.model.GameMode.CLASSIC);
    }

    @FXML
    public void startSprintGame(ActionEvent event) {
        startGameWithMode(com.comp2042.model.GameMode.SPRINT);
    }

    private void startGameWithMode(com.comp2042.model.GameMode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent gameRoot = loader.load();
            GuiController guiController = loader.getController();

            Stage stage = (Stage) highScoreDisplay.getScene().getWindow();
            Scene gameScene = new Scene(gameRoot, 600, 510);
            stage.setScene(gameScene);

            new GameController(guiController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showHighScore(ActionEvent event) {
        if (highScoreDisplay != null) {
            highScoreDisplay.setText("Best Score: " + highScore.getHighScore());
            highScoreDisplay.setVisible(true);
        }
    }

    @FXML
    public void exitGame(ActionEvent event) {
        javafx.application.Platform.exit();
    }
}