package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

/**
 * Data class containing information about the next rotation state of a brick.
 * Used to preview the next rotation before applying it to the brick.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a NextShapeInfo object with the specified shape and position.
     * 
     * @param shape the matrix representation of the next rotation state
     * @param position the index position of this rotation state in the brick's rotation list
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets a copy of the shape matrix for the next rotation state.
     * 
     * @return a copy of the shape matrix
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the position index of this rotation state.
     * 
     * @return the index position in the brick's rotation list (0-based)
     */
    public int getPosition() {
        return position;
    }
}
