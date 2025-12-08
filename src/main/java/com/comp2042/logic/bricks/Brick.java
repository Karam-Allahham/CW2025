package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Interface defining the contract for Tetris brick shapes.
 * Each brick shape is represented by a list of matrices, where each matrix
 * represents a different rotation state of the brick.
 */
public interface Brick {

    /**
     * Gets the list of shape matrices representing all possible rotation states of the brick.
     * Each matrix is a 2D array where 1 represents a filled cell and 0 represents an empty cell.
     * 
     * @return a list of 2D integer arrays, each representing a rotation state of the brick
     */
    List<int[][]> getShapeMatrix();
}
