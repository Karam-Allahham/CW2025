package com.comp2042.controller;

import com.comp2042.view.BoardRenderer;
import com.comp2042.input.InputHandler;
import com.comp2042.view.ViewData;
import com.comp2042.view.GameOverPanel;
import com.comp2042.view.NotificationPanel;
import com.comp2042.event.DownData;
import com.comp2042.event.MoveEvent;
import com.comp2042.event.EventType;
import com.comp2042.event.EventSource;
import com.comp2042.event.InputEventListener;
import com.comp2042.state.GameState;
import com.comp2042.state.PlayingState;
import com.comp2042.state.PausedState;
import com.comp2042.state.GameOverState;
import com.comp2042.core.GameLoop;
import com.comp2042.model.ClearRow;

import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;




public class GuiController implements Initializable {


    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label stateLabel;

    @FXML
    private Label highScoreLabel;

    @FXML
    private Label levelLabel;

    private InputEventListener eventListener;
    private GameLoop gameLoop;
    private GameState currentState = new PlayingState();

    private BoardRenderer renderer;
    private InputHandler inputHandler;
    private static final int GAME_TICK_MS = 400;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gameOverPanel.setVisible(false);

    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        if (renderer == null) renderer = new BoardRenderer(gamePanel, brickPanel);

        renderer.initBoard(boardMatrix);
        renderer.initPreview(brick);

        if (inputHandler == null) {
            inputHandler = new InputHandler(gamePanel, eventListener);
            inputHandler.setPauseCallback(() -> pauseGame(null));
            inputHandler.setPreviewConsumer(viewData -> {
                if (currentState.canAcceptInput()) renderer.refreshPreview(viewData);
            });
            inputHandler.setDownConsumer(downData -> {
                if (currentState.canAcceptInput()) {
                    showScoreNotification(downData.getClearRow());
                    renderer.refreshPreview(downData.getViewData());
                }
                gamePanel.requestFocus();
            });
        }

        gameLoop = new GameLoop(() -> {

            if (!currentState.canProcessGameTick()) {
                return;
            }
            DownData downData = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
            showScoreNotification(downData.getClearRow());
            renderer.refreshPreview(downData.getViewData());
        }, GAME_TICK_MS);

        gameLoop.start();

    }

    public void refreshBrick(ViewData brick) {
        if (currentState.canAcceptInput()) {
            // delegate preview refresh to renderer
            if (renderer != null) renderer.refreshPreview(brick);
        }
    }

    public void refreshGameBackground(int[][] board) {
        if (renderer != null) renderer.refreshBoard(board);
    }

    private void showScoreNotification(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString());
    }

    public void gameOver() {
        if (gameLoop != null) gameLoop.stop();
        gameOverPanel.setVisible(true);
        currentState = new GameOverState();
        updateStateDisplay();
    }

    public void newGame(ActionEvent actionEvent) {
        if (gameLoop != null) gameLoop.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        ViewData newBrickData = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        if (renderer != null) {
            renderer.refreshPreview(newBrickData);
        }

        gamePanel.requestFocus();
        if (gameLoop != null) gameLoop.start();
        currentState = new PlayingState();
        updateStateDisplay();
    }

    public void pauseGame(ActionEvent actionEvent) {
        // Don't allow pause if game is over
        if (currentState instanceof GameOverState) {
            gamePanel.requestFocus();
            return;
        }

        if (currentState instanceof PlayingState) {
            currentState = new PausedState();
        } else if (currentState instanceof PausedState) {
            currentState = new PlayingState();
        }
        updateStateDisplay();
        gamePanel.requestFocus();
    }


    private void updateStateDisplay() {
        if (stateLabel != null) {
            stateLabel.setText(currentState.getStateName());
        }
    }

    public void bindHighScore(IntegerProperty highScoreProperty) {
        highScoreLabel.textProperty().bind(
                javafx.beans.binding.Bindings.concat("Best: ", highScoreProperty.asString())
        );
    }

    public void updateGameSpeed(int speedMs) {
        if (gameLoop != null) {
            gameLoop.setInterval(speedMs);
        }
    }

    public void bindLevel(IntegerProperty levelProperty) {
        levelLabel.textProperty().bind(
                javafx.beans.binding.Bindings.concat("Level: ", levelProperty.asString())
        );
    }
}
