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

public class GameController implements InputEventListener {

    private static final int BOARD_ROWS = 25;
    private static final int BOARD_COLS = 10;
    private Board board = new SimpleBoard(BOARD_ROWS, BOARD_COLS);
    private final HighScore highScore = new HighScore();

    private final GuiController viewGuiController;
    private final Level level = new Level();

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindHighScore(highScore.highScoreProperty());
        viewGuiController.bindLevel(level.levelProperty());
    }

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

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        level.reset();
        viewGuiController.updateGameSpeed(level.getSpeedForCurrentLevel());
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

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
        }
        if (board.createNewBrick()) {
            highScore.checkAndUpdate(board.getScore().scoreProperty().getValue());
            viewGuiController.gameOver();
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new DownData(clearRow, board.getViewData());
    }

    public HighScore getHighScore() {
        return highScore;
    }

    public Level getLevel() {
        return level;
    }

    public int[][] getBoardMatrix() {
        return board.getBoardMatrix();
    }
}
