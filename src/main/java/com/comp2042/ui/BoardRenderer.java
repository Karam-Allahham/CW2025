package com.comp2042.ui;

import com.comp2042.ViewData;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


public class BoardRenderer {

    private static final int BRICK_SIZE = 20;

    private final GridPane gamePanel;
    private final GridPane brickPanel;
    private Rectangle[][] displayMatrix;
    private Rectangle[][] previewRectangles;

    public BoardRenderer(GridPane gamePanel, GridPane brickPanel) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
    }


    public void initBoard(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }
    }


    public void initPreview(ViewData brick) {
        previewRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(fillFor(brick.getBrickData()[i][j]));
                previewRectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        updatePreviewLocation(brick);
    }


    public void refreshPreview(ViewData brick) {
        if (previewRectangles == null) return;
        updatePreviewLocation(brick);
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangle(previewRectangles[i][j], brick.getBrickData()[i][j]);
            }
        }
    }


    public void refreshBoard(int[][] board) {
        if (displayMatrix == null) return;
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangle(displayMatrix[i][j], board[i][j]);
            }
        }
    }

    private void updatePreviewLocation(ViewData brick) {
        // Position the preview grid the same way original GuiController did
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
    }

    private void setRectangle(Rectangle rectangle, int color) {
        rectangle.setFill(fillFor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private Paint fillFor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }
}
