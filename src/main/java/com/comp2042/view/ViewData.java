package com.comp2042.view;

import com.comp2042.util.MatrixOperations;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int ghostY;
    private final int[][] nextBrickData;
    private final int[][] secondNextBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int ghostY, int[][] nextBrickData, int[][] secondNextBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.ghostY = ghostY;
        this.nextBrickData = nextBrickData;
        this.secondNextBrickData = secondNextBrickData;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    public int getGhostY() {
        return ghostY;
    }

    public int[][] getSecondNextBrickData() {
        return MatrixOperations.copy(secondNextBrickData);
    }
}
