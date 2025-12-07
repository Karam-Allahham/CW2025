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
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;




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

    @FXML
    private VBox pauseOverlay;

    @FXML
    private GridPane nextPanel1;

    @FXML
    private GridPane nextPanel2;

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
                if (currentState.canAcceptInput()) {
                    renderer.refreshPreview(viewData);
                    updateNextPiecesPreview(viewData.getNextBrickData(), viewData.getSecondNextBrickData());
                }
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
            updateNextPiecesPreview(downData.getViewData().getNextBrickData(), downData.getViewData().getSecondNextBrickData());
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
        if (pauseOverlay != null) pauseOverlay.setVisible(false);
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
            if (pauseOverlay != null) pauseOverlay.setVisible(true);
        } else if (currentState instanceof PausedState) {
            resumeGame(null);
            return;
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

    public void resumeGame(ActionEvent actionEvent) {
        if (currentState instanceof PausedState) {
            currentState = new PlayingState();
            if (pauseOverlay != null) pauseOverlay.setVisible(false);
            updateStateDisplay();
        }
        gamePanel.requestFocus();
    }

    public void exitToHome(ActionEvent actionEvent) {
        if (gameLoop != null) gameLoop.stop();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("homeScreen.fxml"));
            Parent homeRoot = loader.load();

            Stage stage = (Stage) gamePanel.getScene().getWindow();
            Scene homeScene = new Scene(homeRoot, 300, 510);
            stage.setScene(homeScene);

        } catch (Exception e) {
            e.printStackTrace();
            javafx.application.Platform.exit();
        }
    }

    public void updateNextPiecesPreview(int[][] next1, int[][] next2) {
        renderPreviewPanel(nextPanel1, next1);
        renderPreviewPanel(nextPanel2, next2);
    }

    private void renderPreviewPanel(GridPane panel, int[][] shape) {
        if (panel == null || shape == null) return;

        panel.getChildren().clear();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(12, 12);
                if (shape[i][j] != 0) {
                    rect.setFill(getColorForValue(shape[i][j]));
                    rect.setArcHeight(4);
                    rect.setArcWidth(4);
                } else {
                    rect.setFill(javafx.scene.paint.Color.TRANSPARENT);
                }
                panel.add(rect, j, i);
            }
        }
    }

    private javafx.scene.paint.Paint getColorForValue(int value) {
        return switch (value) {
            case 1 -> javafx.scene.paint.Color.AQUA;
            case 2 -> javafx.scene.paint.Color.BLUEVIOLET;
            case 3 -> javafx.scene.paint.Color.DARKGREEN;
            case 4 -> javafx.scene.paint.Color.YELLOW;
            case 5 -> javafx.scene.paint.Color.RED;
            case 6 -> javafx.scene.paint.Color.BEIGE;
            case 7 -> javafx.scene.paint.Color.BURLYWOOD;
            default -> javafx.scene.paint.Color.WHITE;
        };
    }
}
