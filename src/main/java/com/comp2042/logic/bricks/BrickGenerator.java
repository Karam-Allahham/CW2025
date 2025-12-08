package com.comp2042.logic.bricks;

/**
 * Interface defining the contract for generating Tetris bricks.
 * Implementations provide methods to get the current brick, next brick,
 * and preview of upcoming bricks for display purposes.
 */
public interface BrickGenerator {

    /**
     * Gets the current brick that should be spawned.
     * 
     * @return the current Brick instance
     */
    Brick getBrick();

    /**
     * Gets the next brick that will be spawned after the current one.
     * 
     * @return the next Brick instance
     */
    Brick getNextBrick();

    /**
     * Gets an array of preview bricks for display in the UI.
     * Typically returns the next two upcoming bricks.
     * 
     * @return an array of Brick instances representing upcoming bricks
     */
    Brick[] getPreviewBricks();
}
