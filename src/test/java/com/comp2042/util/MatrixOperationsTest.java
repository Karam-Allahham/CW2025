package com.comp2042.util;

import com.comp2042.model.ClearRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


class MatrixOperationsTest {

    @Test
    @DisplayName("copy should create independent copy of matrix")
    void copyShouldCreateIndependentCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        int[][] copy = MatrixOperations.copy(original);

        // Modify copy
        copy[0][0] = 99;

        // Original should be unchanged
        assertEquals(1, original[0][0]);
    }

    @Test
    @DisplayName("copy should preserve all values")
    void copyShouldPreserveValues() {
        int[][] original = {{1, 2, 3}, {4, 5, 6}};
        int[][] copy = MatrixOperations.copy(original);

        assertArrayEquals(original[0], copy[0]);
        assertArrayEquals(original[1], copy[1]);
    }

    @Test
    @DisplayName("intersect should detect collision")
    void intersectShouldDetectCollision() {
        int[][] matrix = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        // Brick overlaps with existing block at (1,1)
        boolean collision = MatrixOperations.intersect(matrix, brick, 0, 0);
        assertTrue(collision);
    }

    @Test
    @DisplayName("intersect should allow valid placement")
    void intersectShouldAllowValidPlacement() {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        boolean collision = MatrixOperations.intersect(matrix, brick, 0, 0);
        assertFalse(collision);
    }

    @Test
    @DisplayName("intersect should detect out of bounds")
    void intersectShouldDetectOutOfBounds() {
        int[][] matrix = {
                {0, 0},
                {0, 0}
        };
        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        // Brick placed outside matrix bounds
        boolean collision = MatrixOperations.intersect(matrix, brick, 5, 5);
        assertTrue(collision);
    }

    @Test
    @DisplayName("checkRemoving should detect full row")
    void checkRemovingShouldDetectFullRow() {
        int[][] matrix = {
                {0, 0, 0},
                {0, 0, 0},
                {1, 1, 1}  // Full row
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);
        assertEquals(1, result.getLinesRemoved());
    }

    @Test
    @DisplayName("checkRemoving should return zero for no full rows")
    void checkRemovingShouldReturnZeroForNoFullRows() {
        int[][] matrix = {
                {0, 0, 0},
                {1, 0, 1},
                {1, 0, 1}
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);
        assertEquals(0, result.getLinesRemoved());
    }

    @Test
    @DisplayName("checkRemoving should calculate correct score bonus")
    void checkRemovingShouldCalculateScoreBonus() {
        int[][] matrix = {
                {0, 0, 0},
                {1, 1, 1},  // Full row
                {1, 1, 1}   // Full row
        };

        ClearRow result = MatrixOperations.checkRemoving(matrix);
        assertEquals(2, result.getLinesRemoved());
        // Score = 50 * lines * lines = 50 * 2 * 2 = 200
        assertEquals(200, result.getScoreBonus());
    }
}