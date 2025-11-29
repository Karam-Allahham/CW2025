package com.comp2042;

import com.comp2042.ui.BoardRenderer;
import com.comp2042.ui.InputHandler;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.IntegerProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.Font;
import javafx.scene.effect.Reflection;
import javafx.scene.control.Label;

import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import com.comp2042.core.GameLoop;




public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

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

    private InputEventListener eventListener;
    private GameLoop gameLoop;
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private BoardRenderer renderer;
    private InputHandler inputHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        if (renderer == null) renderer = new BoardRenderer(gamePanel, brickPanel);

        renderer.initBoard(boardMatrix);
        renderer.initPreview(brick);

        if (inputHandler == null) {
            inputHandler = new InputHandler(gamePanel, eventListener);
            inputHandler.setPauseCallback(() -> pauseGame(null));
            inputHandler.setPreviewConsumer(viewData -> {
                if (!isPause.getValue()) renderer.refreshPreview(viewData);
            });
            inputHandler.setDownConsumer(downData -> {
                if (!isPause.getValue()) {
                    if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                        NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                        groupNotification.getChildren().add(notificationPanel);
                        notificationPanel.showScore(groupNotification.getChildren());
                    }
                    renderer.refreshPreview(downData.getViewData());
                }
                gamePanel.requestFocus();
            });
        }

        gameLoop = new GameLoop(() -> {
            // Don't process game tick if paused or game is over
            if (isPause.getValue() || isGameOver.getValue()) {
                return;
            }
            DownData downData = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            renderer.refreshPreview(downData.getViewData());
        }, 400);

        gameLoop.start();

    }

    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            // delegate preview refresh to renderer
            if (renderer != null) renderer.refreshPreview(brick);
        }
    }

    public void refreshGameBackground(int[][] board) {
        if (renderer != null) renderer.refreshBoard(board);
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
        isGameOver.setValue(Boolean.TRUE);
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
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        // Don't allow pause if game is over
        if (isGameOver.getValue()) {
            gamePanel.requestFocus();
            return;
        }

        // Toggle pause state
        isPause.setValue(!isPause.getValue());
        gamePanel.requestFocus();
    }
}
