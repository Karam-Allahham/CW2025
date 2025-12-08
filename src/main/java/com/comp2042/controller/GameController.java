package com.comp2042.controller;

import com.comp2042.model.Board;
import com.comp2042.model.SimpleBoard;
import com.comp2042.model.ClearRow;
import com.comp2042.event.DownData;
import com.comp2042.event.MoveEvent;
import com.comp2042.event.EventSource;
import com.comp2042.event.InputEventListener;
import com.comp2042.view.ViewData;
import com.comp2042.model.HighScore;
import com.comp2042.model.Level;
import com.comp2042.model.GameMode;

/**
 * Main controller that coordinates the game logic and view.
 * Implements InputEventListener to handle player input events and manages
 * the interaction between the game board, score, level, and GUI components.
 * Supports both classic and sprint game modes.
 */
public class GameController implements InputEventListener {

    private static final int BOARD_ROWS = 25;
    private static final int BOARD_COLS = 10;
    private Board board = new SimpleBoard(BOARD_ROWS, BOARD_COLS);
    private final HighScore highScore = new HighScore();

    private final GuiController viewGuiController;
    private final Level level = new Level();
    private final GameMode gameMode;
    private static final int SPRINT_TARGET = 3;

    /**
     * Constructs a GameController with default classic game mode.
     * 
     * @param c the GUI controller to coordinate with
     */
    public GameController(GuiController c) {
        this(c, GameMode.CLASSIC);
    }

    /**
     * Constructs a GameController with the specified game mode.
     * Initializes the board, sets up UI bindings, and configures sprint mode if applicable.
     * 
     * @param c the GUI controller to coordinate with
     * @param mode the game mode to use (CLASSIC or SPRINT)
     */
    public GameController(GuiController c, GameMode mode) {
        this.gameMode = mode;
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindHighScore(highScore.highScoreProperty());

        if (gameMode == GameMode.SPRINT) {
            viewGuiController.setSprintMode(SPRINT_TARGET);
        }

        viewGuiController.bindLevel(level.levelProperty());
    }

    /**
     * {@inheritDoc}
     * Handles the down movement event. Moves the brick down, merges it when blocked,
     * clears rows, updates score and level, and checks for game over or sprint win conditions.
     * 
     * @param event the move event containing event type and source information
     * @return DownData containing information about cleared rows and updated view data
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
                highScore.checkAndUpdate(board.getScore().scoreProperty().getValue());
                if (level.addLines(clearRow.getLinesRemoved())) {
                    viewGuiController.updateGameSpeed(level.getSpeedForCurrentLevel());
                }

                if (gameMode == GameMode.SPRINT) {
                    viewGuiController.updateSprintProgress(level.getLinesCleared(), SPRINT_TARGET);
                    if (level.getLinesCleared() >= SPRINT_TARGET) {
                        viewGuiController.gameWon();
                        return new DownData(clearRow, board.getViewData());
                    }
                }
            }
            if (board.createNewBrick()) {
                highScore.checkAndUpdate(board.getScore().scoreProperty().getValue());
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * {@inheritDoc}
     * Handles the left movement event by moving the current brick left.
     * 
     * @param event the move event containing event type and source information
     * @return ViewData containing the updated brick position and view information
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * {@inheritDoc}
     * Handles the right movement event by moving the current brick right.
     * 
     * @param event the move event containing event type and source information
     * @return ViewData containing the updated brick position and view information
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * {@inheritDoc}
     * Handles the rotation event by rotating the current brick counter-clockwise.
     * 
     * @param event the move event containing event type and source information
     * @return ViewData containing the updated brick rotation and view information
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * {@inheritDoc}
     * Resets the game to start a new game. Clears the board, resets the level,
     * and updates the game speed.
     */
    @Override
    public void createNewGame() {
        board.newGame();
        level.reset();
        viewGuiController.updateGameSpeed(level.getSpeedForCurrentLevel());
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * {@inheritDoc}
     * Handles the hard drop event. Instantly drops the brick to its lowest position,
     * merges it, clears rows, and checks for game over or sprint win conditions.
     * 
     * @param event the move event containing event type and source information
     * @return DownData containing information about cleared rows and updated view data
     */
    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        board.hardDrop();
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            highScore.checkAndUpdate(board.getScore().scoreProperty().getValue());
            if (level.addLines(clearRow.getLinesRemoved())) {
                viewGuiController.updateGameSpeed(level.getSpeedForCurrentLevel());
            }

            if (gameMode == GameMode.SPRINT) {
                viewGuiController.updateSprintProgress(level.getLinesCleared(), SPRINT_TARGET);
                if (level.getLinesCleared() >= SPRINT_TARGET) {
                    viewGuiController.gameWon();
                    return new DownData(clearRow, board.getViewData());
                }
            }
        }
        if (board.createNewBrick()) {
            highScore.checkAndUpdate(board.getScore().scoreProperty().getValue());
            viewGuiController.gameOver();
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Gets the HighScore instance for this game session.
     * 
     * @return the HighScore object
     */
    public HighScore getHighScore() {
        return highScore;
    }

    /**
     * Gets the Level instance for this game session.
     * 
     * @return the Level object
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Gets the current state of the game board matrix.
     * 
     * @return a 2D array representing the board state
     */
    public int[][] getBoardMatrix() {
        return board.getBoardMatrix();
    }
}
