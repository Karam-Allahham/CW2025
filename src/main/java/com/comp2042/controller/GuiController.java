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

/**
 * Controller for the game's graphical user interface.
 * Manages UI components, rendering, input handling, game loop, and state transitions.
 * Implements Initializable to set up the FXML-injected components.
 */
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

    @FXML
    private Label sprintNotification;

    @FXML
    private Label timerLabel;

    private int sprintTarget = 0;
    private long sprintStartTime;
    private javafx.animation.Timeline sprintTimer;

    private InputEventListener eventListener;
    private GameLoop gameLoop;
    private GameState currentState = new PlayingState();

    private BoardRenderer renderer;
    private InputHandler inputHandler;
    private static final int GAME_TICK_MS = 400;

    /**
     * Initializes the GUI controller. Called automatically by JavaFX after FXML loading.
     * Sets up fonts, focus handling, and initial visibility states.
     * 
     * @param location the location used to resolve relative paths, or null if unknown
     * @param resources the resources used to localize the root object, or null if not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gameOverPanel.setVisible(false);

    }

    /**
     * Initializes the game view with the board matrix and initial brick data.
     * Sets up the board renderer, input handler, and game loop.
     * 
     * @param boardMatrix the initial game board matrix
     * @param brick the initial falling brick data
     */
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

    /**
     * Refreshes the display of the current falling brick.
     * 
     * @param brick the updated brick data to display
     */
    public void refreshBrick(ViewData brick) {
        if (currentState.canAcceptInput()) {
            // delegate preview refresh to renderer
            if (renderer != null) renderer.refreshPreview(brick);
        }
    }

    /**
     * Refreshes the game board background display.
     * 
     * @param board the updated board matrix to render
     */
    public void refreshGameBackground(int[][] board) {
        if (renderer != null) renderer.refreshBoard(board);
    }

    private void showScoreNotification(ClearRow clearRow) {
        if (clearRow != null && clearRow.getLinesRemoved() > 0) {

            int[] rows = clearRow.getClearedRows();
            flashRowsWhite(rows, true);


            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(150));
            pause.setOnFinished(e -> {

                if (renderer != null && eventListener != null) {
                    renderer.refreshBoard(((com.comp2042.controller.GameController) eventListener).getBoardMatrix());
                }
            });
            pause.play();

            NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    private void flashRowsWhite(int[] rows, boolean flash) {
        for (int row : rows) {
            int displayRow = row - 2;  // Adjust for hidden top rows
            if (displayRow >= 0) {
                for (var node : gamePanel.getChildren()) {
                    Integer rowIndex = GridPane.getRowIndex(node);
                    if (rowIndex != null && rowIndex == displayRow && node instanceof javafx.scene.shape.Rectangle rect) {
                        if (flash) {
                            rect.setFill(javafx.scene.paint.Color.WHITE);
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the input event listener to handle game logic events.
     * 
     * @param eventListener the InputEventListener to receive events
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Binds the score label to the score property for automatic UI updates.
     * 
     * @param integerProperty the score property to bind
     */
    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString());
    }

    /**
     * Handles the game over state. Stops the game loop and timer, displays the game over panel,
     * and transitions to GameOverState.
     */
    public void gameOver() {
        if (gameLoop != null) gameLoop.stop();
        if (sprintTimer != null) sprintTimer.stop();
        gameOverPanel.setVisible(true);
        currentState = new GameOverState();
        updateStateDisplay();
    }

    /**
     * Starts a new game. Resets the game loop, clears UI overlays, resets sprint mode if active,
     * and initializes a new game through the event listener.
     * 
     * @param actionEvent the action event that triggered this method (from UI button)
     */
    public void newGame(ActionEvent actionEvent) {
        if (gameLoop != null) gameLoop.stop();
        gameOverPanel.setVisible(false);
        if (pauseOverlay != null) pauseOverlay.setVisible(false);

        if (sprintTimer != null) {
            sprintTimer.stop();
            sprintTimer = null;
        }
        sprintTarget = 0;
        if (timerLabel != null) timerLabel.setVisible(false);
        if (sprintNotification != null) {
            sprintNotification.setVisible(false);
            sprintNotification.setStyle("-fx-font-size: 14; -fx-text-fill: yellow; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.8); -fx-padding: 10; -fx-background-radius: 5;");
        }

        if (highScoreLabel != null) highScoreLabel.setVisible(true);

        eventListener.createNewGame();
        ViewData newBrickData = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        if (renderer != null) {
            renderer.refreshPreview(newBrickData);
        }

        gamePanel.requestFocus();
        if (gameLoop != null) gameLoop.start();
        currentState = new PlayingState();

        if (sprintTimer != null) {
            sprintTimer.stop();
            sprintTimer = null;
        }
        sprintTarget = 0;
        if (timerLabel != null) timerLabel.setVisible(false);
        if (sprintNotification != null) sprintNotification.setVisible(false);
        updateStateDisplay();
    }

    /**
     * Toggles the pause state of the game. If playing, pauses the game.
     * If paused, resumes the game. Does nothing if the game is over.
     * 
     * @param actionEvent the action event that triggered this method (from UI button or key press)
     */
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

    /**
     * Binds the high score label to the high score property for automatic UI updates.
     * 
     * @param highScoreProperty the high score property to bind
     */
    public void bindHighScore(IntegerProperty highScoreProperty) {
        highScoreLabel.textProperty().bind(highScoreProperty.asString());
    }

    /**
     * Updates the game loop speed based on the current level.
     * 
     * @param speedMs the new speed in milliseconds (time between ticks)
     */
    public void updateGameSpeed(int speedMs) {
        if (gameLoop != null) {
            gameLoop.setInterval(speedMs);
        }
    }

    /**
     * Binds the level label to the level property for automatic UI updates.
     * Only binds if sprint mode is not active.
     * 
     * @param levelProperty the level property to bind
     */
    public void bindLevel(IntegerProperty levelProperty) {
        if (sprintTarget == 0) {
            levelLabel.textProperty().bind(levelProperty.asString());
        }
    }

    /**
     * Resumes the game from paused state. Transitions from PausedState to PlayingState
     * and hides the pause overlay.
     * 
     * @param actionEvent the action event that triggered this method (from UI button)
     */
    public void resumeGame(ActionEvent actionEvent) {
        if (currentState instanceof PausedState) {
            currentState = new PlayingState();
            if (pauseOverlay != null) pauseOverlay.setVisible(false);
            updateStateDisplay();
        }
        gamePanel.requestFocus();
    }

    /**
     * Exits the game view and returns to the home screen.
     * Stops the game loop and loads the home screen FXML.
     * 
     * @param actionEvent the action event that triggered this method (from UI button)
     */
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

    /**
     * Updates the preview panels to show the next two upcoming bricks.
     * 
     * @param next1 the matrix representation of the first next brick
     * @param next2 the matrix representation of the second next brick
     */
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

    /**
     * Sets up sprint mode with the specified target number of lines to clear.
     * Hides high score display, shows timer, and displays sprint notification.
     * 
     * @param target the target number of lines to clear in sprint mode
     */
    public void setSprintMode(int target) {
        System.out.println("setSprintMode called with target: " + target);
        this.sprintTarget = target;


        if (highScoreLabel != null) {
            highScoreLabel.textProperty().unbind();
            highScoreLabel.setVisible(false);
        }


        if (levelLabel != null) {
            levelLabel.textProperty().unbind();
            levelLabel.setText("Lines: 0/20");
            levelLabel.setVisible(true);
        }


        if (timerLabel != null) {
            timerLabel.setVisible(true);
            timerLabel.toFront();
        }


        showSprintNotification(target);
        startSprintTimer();
    }

    private void showSprintNotification(int target) {
        javafx.application.Platform.runLater(() -> {
            if (sprintNotification != null) {
                sprintNotification.setText("Race to clear 40 lines!!!!");
                sprintNotification.setVisible(true);
                sprintNotification.toFront();

                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(5));
                pause.setOnFinished(e -> sprintNotification.setVisible(false));
                pause.play();
            }
        });

    }

    private void startSprintTimer() {
        sprintStartTime = System.currentTimeMillis();

        sprintTimer = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(100), e -> updateTimerDisplay())
        );
        sprintTimer.setCycleCount(javafx.animation.Animation.INDEFINITE);
        sprintTimer.play();
    }

    private void updateTimerDisplay() {
        if (timerLabel != null && sprintTarget > 0) {
            long elapsed = System.currentTimeMillis() - sprintStartTime;
            long seconds = (elapsed / 1000) % 60;
            long minutes = (elapsed / 1000) / 60;
            long millis = (elapsed % 1000) / 10;
            timerLabel.setText(String.format("Time: %02d:%02d.%02d", minutes, seconds, millis));
        }
    }

    /**
     * Updates the sprint progress display showing lines cleared vs target.
     * 
     * @param linesCleared the number of lines cleared so far
     * @param target the target number of lines to clear
     */
    public void updateSprintProgress(int linesCleared, int target) {
        if (sprintTarget > 0 && levelLabel != null) {
            levelLabel.setText("Lines: " + linesCleared + "/" + target);
        }
    }

    /**
     * Handles the sprint mode win condition. Stops the game loop and timer,
     * displays the win message with completion time, and transitions to GameOverState.
     */
    public void gameWon() {
        if (gameLoop != null) gameLoop.stop();
        if (sprintTimer != null) sprintTimer.stop();
        currentState = new GameOverState();
        updateStateDisplay();

        long elapsed = System.currentTimeMillis() - sprintStartTime;
        long seconds = (elapsed / 1000) % 60;
        long minutes = (elapsed / 1000) / 60;
        long millis = (elapsed % 1000) / 10;
        String finalTime = String.format("%02d:%02d.%02d", minutes, seconds, millis);

        javafx.application.Platform.runLater(() -> {
            if (sprintNotification != null) {
                sprintNotification.setText("YOU WIN! Time: " + finalTime);
                sprintNotification.setVisible(true);
                sprintNotification.setStyle("-fx-font-size: 24; -fx-text-fill: yellow; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.9); -fx-padding: 15; -fx-background-radius: 5;");
            }

            gameOverPanel.setVisible(true);
        });
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
