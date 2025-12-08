package com.comp2042.view;

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
    private Rectangle[][] ghostRectangles;

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
        renderGhost(brick);
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
        javafx.geometry.Bounds gameBounds = gamePanel.localToScene(gamePanel.getBoundsInLocal());

        javafx.geometry.Bounds brickParentBounds = brickPanel.getParent().localToScene(
                brickPanel.getParent().getBoundsInLocal());
        javafx.geometry.Point2D brickParentOrigin = brickPanel.getParent().localToScene(0, 0);

        double offsetX = gameBounds.getMinX() - brickParentOrigin.getX();
        double offsetY = gameBounds.getMinY() - brickParentOrigin.getY();

        double x = offsetX + brick.getxPosition() * (BRICK_SIZE + brickPanel.getVgap());
        double y = offsetY - 42 + (brick.getyPosition() - 2) * (BRICK_SIZE + brickPanel.getHgap());

        brickPanel.setLayoutX(x);
        brickPanel.setLayoutY(y);
    }

    private void setRectangle(Rectangle rectangle, int color) {
        rectangle.setFill(fillFor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    public void renderGhost(ViewData brick) {
        // Clear previous ghost
        if (ghostRectangles != null) {
            for (Rectangle[] row : ghostRectangles) {
                for (Rectangle rect : row) {
                    if (rect != null) {
                        gamePanel.getChildren().remove(rect);
                    }
                }
            }
        }

        int[][] brickData = brick.getBrickData();
        ghostRectangles = new Rectangle[brickData.length][brickData[0].length];

        int ghostY = brick.getGhostY();
        int xPos = brick.getxPosition();


        if (ghostY <= brick.getyPosition()) {
            return;
        }

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                if (brickData[i][j] != 0) {
                    int displayY = ghostY + i - 2;
                    int displayX = xPos + j;

                    if (displayY >= 0 && displayY < 23 && displayX >= 0 && displayX < 10) {
                        Rectangle ghost = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                        ghost.setFill(Color.GRAY);
                        ghost.setOpacity(0.3);
                        ghost.setArcHeight(9);
                        ghost.setArcWidth(9);
                        ghostRectangles[i][j] = ghost;
                        gamePanel.add(ghost, displayX, displayY);
                    }
                }
            }
        }
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
