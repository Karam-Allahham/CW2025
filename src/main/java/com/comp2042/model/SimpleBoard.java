package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.view.ViewData;
import com.comp2042.util.MatrixOperations;

import java.awt.Point;

/**
 * Implementation of the Board interface representing the game board for Tetris.
 * Manages the game board state, falling bricks, collision detection, row clearing,
 * and brick movement operations. Uses a matrix representation where 0 represents
 * an empty cell and non-zero values represent filled cells.
 */
public class SimpleBoard implements Board {

    private static final int SPAWN_X = 4;
    private static final int SPAWN_Y = 0;
    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    /**
     * Constructs a new SimpleBoard with the specified dimensions.
     * 
     * @param width the number of rows in the board
     * @param height the number of columns in the board
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * {@inheritDoc}
     * Moves the current falling brick down by one cell if no collision is detected.
     */
    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * {@inheritDoc}
     * Moves the current falling brick left by one cell if no collision is detected.
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * {@inheritDoc}
     * Moves the current falling brick right by one cell if no collision is detected.
     */
    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * {@inheritDoc}
     * Rotates the current brick counter-clockwise with wall kick support.
     * Attempts multiple offset positions if the default rotation would cause a collision.
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        int[][] shape = nextShape.getShape();
        int x = (int) currentOffset.getX();
        int y = (int) currentOffset.getY();

        if (!MatrixOperations.intersect(currentMatrix, shape, x, y)) {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        if (!MatrixOperations.intersect(currentMatrix, shape, x + 1, y)) {
            currentOffset = new Point(x + 1, y);
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        if (!MatrixOperations.intersect(currentMatrix, shape, x - 1, y)) {
            currentOffset = new Point(x - 1, y);
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        if (!MatrixOperations.intersect(currentMatrix, shape, x, y - 1)) {
            currentOffset = new Point(x, y - 1);
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        if (!MatrixOperations.intersect(currentMatrix, shape, x + 2, y)) {
            currentOffset = new Point(x + 2, y);
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * Creates a new brick at the spawn position. Returns true if the spawn position
     * is blocked, indicating game over.
     */
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(SPAWN_X, SPAWN_Y);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * {@inheritDoc}
     * Returns a copy of the current game board matrix.
     */
    @Override
    public int[][] getBoardMatrix() {
        return MatrixOperations.copy(currentGameMatrix);
    }

    /**
     * {@inheritDoc}
     * Creates and returns ViewData containing the current brick position,
     * ghost piece position, and preview of the next two bricks.
     */
    @Override
    public ViewData getViewData() {
        Brick[] previewBricks = brickGenerator.getPreviewBricks();
        int[][] nextShape = previewBricks[0] != null ? previewBricks[0].getShapeMatrix().get(0) : new int[4][4];
        int[][] secondShape = previewBricks[1] != null ? previewBricks[1].getShapeMatrix().get(0) : new int[4][4];

        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), getGhostY(), nextShape, secondShape);
    }

    /**
     * {@inheritDoc}
     * Merges the current falling brick into the background board matrix.
     * Called when the brick can no longer move down.
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * {@inheritDoc}
     * Checks for and removes completed rows from the board.
     * Returns information about cleared rows and score bonus.
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    /**
     * {@inheritDoc}
     * Returns the Score object associated with this board.
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * {@inheritDoc}
     * Resets the board for a new game by clearing the matrix and resetting the score.
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();

    }

    /**
     * {@inheritDoc}
     * Instantly drops the current brick to its lowest possible position.
     */
    @Override
    public void hardDrop() {
        while (moveBrickDown()) {
        }
    }

    /**
     * {@inheritDoc}
     * Calculates the y-coordinate where the ghost piece should be displayed
     * by simulating the brick falling until it hits the board or existing blocks.
     */
    @Override
    public int getGhostY() {
        int ghostY = (int) currentOffset.getY();

        // Keep moving down until we hit something
        while (!MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(),
                (int) currentOffset.getX(), ghostY + 1)) {
            ghostY++;
        }

        return ghostY;
    }
}
