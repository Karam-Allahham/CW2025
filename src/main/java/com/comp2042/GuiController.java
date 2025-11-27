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
        if (inputHandler != null) {
            inputHandler = new InputHandler(gamePanel, eventListener);
        }
    }

    public void bindScore(IntegerProperty integerProperty) {
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
        gamePanel.requestFocus();
        if (gameLoop != null) gameLoop.start();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }
}
