package com.comp2042.view;

import com.comp2042.util.MatrixOperations;

/**
 * Data class containing all information needed to render the game view.
 * Includes the current falling brick, its position, ghost piece position,
 * and the next two upcoming bricks for preview.
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int ghostY;
    private final int[][] nextBrickData;
    private final int[][] secondNextBrickData;

    /**
     * Constructs a ViewData object with all necessary rendering information.
     * 
     * @param brickData the matrix representation of the current falling brick
     * @param xPosition the x-coordinate (column) of the current brick on the board
     * @param yPosition the y-coordinate (row) of the current brick on the board
     * @param ghostY the y-coordinate (row) where the ghost piece should be displayed
     * @param nextBrickData the matrix representation of the next upcoming brick
     * @param secondNextBrickData the matrix representation of the second upcoming brick
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int ghostY, int[][] nextBrickData, int[][] secondNextBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.ghostY = ghostY;
        this.nextBrickData = nextBrickData;
        this.secondNextBrickData = secondNextBrickData;
    }

    /**
     * Gets a copy of the current falling brick's matrix representation.
     * 
     * @return a copy of the brick data matrix
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the x-coordinate (column) of the current brick on the game board.
     * 
     * @return the x-position of the brick
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the y-coordinate (row) of the current brick on the game board.
     * 
     * @return the y-position of the brick
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets a copy of the next upcoming brick's matrix representation.
     * 
     * @return a copy of the next brick data matrix
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Gets the y-coordinate (row) where the ghost piece should be displayed.
     * The ghost piece indicates where the current brick will land.
     * 
     * @return the y-position for the ghost piece
     */
    public int getGhostY() {
        return ghostY;
    }

    /**
     * Gets a copy of the second upcoming brick's matrix representation.
     * 
     * @return a copy of the second next brick data matrix
     */
    public int[][] getSecondNextBrickData() {
        return MatrixOperations.copy(secondNextBrickData);
    }
}
