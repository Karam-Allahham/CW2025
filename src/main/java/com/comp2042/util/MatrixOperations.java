package com.comp2042.util;

import com.comp2042.model.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class providing static methods for matrix operations used in the game.
 * Handles collision detection, matrix copying, merging bricks into the board,
 * and row clearing logic. All methods are static as this is a utility class.
 */
public class MatrixOperations {

    private static final int BASE_SCORE_PER_LINE = 50;

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private MatrixOperations(){

    }

    /**
     * Checks if a brick would intersect with the board matrix at the specified position.
     * Returns true if the brick would overlap with existing blocks or go out of bounds.
     * 
     * @param matrix the game board matrix
     * @param brick the brick matrix to check
     * @param x the x-coordinate (row) where the brick would be placed
     * @param y the y-coordinate (column) where the brick would be placed
     * @return true if there would be a collision or out-of-bounds, false otherwise
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the specified coordinates are out of bounds for the given matrix.
     * 
     * @param matrix the matrix to check bounds against
     * @param targetX the x-coordinate (row) to check
     * @param targetY the y-coordinate (column) to check
     * @return true if the coordinates are out of bounds, false otherwise
     */
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        boolean returnValue = true;
        if (targetX >= 0 && targetY >= 0 && targetY < matrix.length && targetX < matrix[targetY].length) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Creates a deep copy of a 2D integer array.
     * 
     * @param original the matrix to copy
     * @return a new matrix that is a deep copy of the original
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges a brick into the game board matrix at the specified position.
     * Creates a copy of the board and overlays the brick onto it.
     * 
     * @param filledFields the game board matrix
     * @param brick the brick matrix to merge
     * @param x the x-coordinate (row) where the brick should be merged
     * @param y the y-coordinate (column) where the brick should be merged
     * @return a new matrix with the brick merged into the board
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }

    /**
     * Checks for completed rows and removes them from the board.
     * Calculates score bonus based on the number of lines cleared (score = BASE_SCORE * lines^2).
     * Shifts remaining rows down to fill gaps left by cleared rows.
     * 
     * @param matrix the game board matrix to check and process
     * @return ClearRow object containing information about cleared rows, new matrix, and score bonus
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRowsList = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRowsList.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = BASE_SCORE_PER_LINE * clearedRowsList.size() * clearedRowsList.size();
        int[] clearedRows = clearedRowsList.stream().mapToInt(Integer::intValue).toArray();
        return new ClearRow(clearedRowsList.size(), tmp, scoreBonus, clearedRows);
    }

    /**
     * Creates a deep copy of a list of 2D integer arrays.
     * Each matrix in the list is deep copied.
     * 
     * @param list the list of matrices to copy
     * @return a new list containing deep copies of all matrices
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
