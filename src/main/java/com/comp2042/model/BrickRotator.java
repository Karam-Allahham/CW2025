package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;

/**
 * Manages the rotation state of the current brick.
 * Tracks which rotation state the brick is in and provides methods to get
 * the current shape and preview the next rotation state.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Gets information about the next rotation state of the current brick.
     * Cycles through all rotation states, wrapping around to the first state after the last.
     * 
     * @return NextShapeInfo containing the next shape matrix and its position index
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Gets the matrix representation of the brick's current rotation state.
     * 
     * @return a 2D array representing the current shape of the brick
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation state of the brick.
     * 
     * @param currentShape the index of the rotation state to set (0-based)
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets a new brick and resets the rotation state to the first state.
     * 
     * @param brick the new brick to manage rotation for
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

}
