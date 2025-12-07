package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.view.ViewData;
import com.comp2042.util.MatrixOperations;

import java.awt.Point;

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

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

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

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(SPAWN_X, SPAWN_Y);
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return MatrixOperations.copy(currentGameMatrix);
    }

    @Override
    public ViewData getViewData() {
        Brick[] previewBricks = brickGenerator.getPreviewBricks();
        int[][] nextShape = previewBricks[0] != null ? previewBricks[0].getShapeMatrix().get(0) : new int[4][4];
        int[][] secondShape = previewBricks[1] != null ? previewBricks[1].getShapeMatrix().get(0) : new int[4][4];

        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), getGhostY(), nextShape, secondShape);
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();

    }

    @Override
    public void hardDrop() {
        while (moveBrickDown()) {
        }
    }

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
