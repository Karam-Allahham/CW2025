package com.comp2042.model;

import com.comp2042.util.MatrixOperations;

/**
 * Represents the result of clearing completed rows from the game board.
 * Contains information about the number of lines removed, the updated board matrix,
 * the score bonus awarded, and which specific rows were cleared.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;
    private final int[] clearedRows;

    /**
     * Constructs a ClearRow object with the specified row clearing information.
     * 
     * @param linesRemoved the number of rows that were cleared
     * @param newMatrix the updated game board matrix after rows were cleared
     * @param scoreBonus the score points awarded for clearing these rows
     * @param clearedRows array of row indices that were cleared
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus, int[] clearedRows) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
        this.clearedRows = clearedRows;
    }

    /**
     * Gets the number of rows that were cleared.
     * 
     * @return the number of lines removed
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets a copy of the updated game board matrix after rows were cleared.
     * 
     * @return a copy of the new board matrix
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus awarded for clearing these rows.
     * 
     * @return the score points earned
     */
    public int getScoreBonus() {
        return scoreBonus;
    }

    /**
     * Gets a copy of the array containing the indices of cleared rows.
     * 
     * @return a copy of the cleared rows array, or an empty array if no rows were cleared
     */
    public int[] getClearedRows() {
        return clearedRows != null ? clearedRows.clone() : new int[0];
    }
}
