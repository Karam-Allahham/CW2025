package com.comp2042.model;


import com.comp2042.view.ViewData;

/**
 * Interface defining the contract for the game board.
 * Provides methods for managing brick movement, rotation, collision detection,
 * row clearing, and game state management.
 */
public interface Board {

    /**
     * Attempts to move the current falling brick down by one cell.
     * 
     * @return true if the brick was moved successfully, false if blocked
     */
    boolean moveBrickDown();

    /**
     * Attempts to move the current falling brick left by one cell.
     * 
     * @return true if the brick was moved successfully, false if blocked
     */
    boolean moveBrickLeft();

    /**
     * Attempts to move the current falling brick right by one cell.
     * 
     * @return true if the brick was moved successfully, false if blocked
     */
    boolean moveBrickRight();

    /**
     * Attempts to rotate the current falling brick counter-clockwise.
     * 
     * @return true if the rotation was successful, false if blocked
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick at the spawn position.
     * 
     * @return true if a new brick was created successfully, false if the spawn position is blocked (game over)
     */
    boolean createNewBrick();

    /**
     * Gets the current state of the game board matrix.
     * 
     * @return a 2D array representing the board state (0 = empty, non-zero = filled cell)
     */
    int[][] getBoardMatrix();

    /**
     * Gets the view data containing information about the current brick, its position,
     * ghost piece position, and upcoming bricks.
     * 
     * @return ViewData object containing all rendering information
     */
    ViewData getViewData();

    /**
     * Merges the current falling brick into the background board.
     * Called when the brick can no longer move down.
     */
    void mergeBrickToBackground();

    /**
     * Clears completed rows from the board and returns information about the cleared rows.
     * 
     * @return ClearRow object containing information about cleared rows and score bonus
     */
    ClearRow clearRows();

    /**
     * Gets the Score object associated with this board.
     * 
     * @return the Score instance for tracking points
     */
    Score getScore();

    /**
     * Resets the board to start a new game.
     * Clears the board and resets the score.
     */
    void newGame();

    /**
     * Performs a hard drop, moving the current brick to its lowest possible position instantly.
     */
    void hardDrop();

    /**
     * Gets the y-coordinate where the ghost piece should be displayed.
     * The ghost piece indicates where the current brick will land.
     * 
     * @return the y-coordinate (row) for the ghost piece
     */
    int getGhostY();
}
