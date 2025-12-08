package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the game level and progression system.
 * Tracks lines cleared, calculates the current level based on lines cleared,
 * and determines the game speed (fall rate) for each level.
 */
public class Level {

    private static final int LINES_PER_LEVEL = 10;
    private static final int BASE_SPEED_MS = 400;
    private static final int SPEED_DECREASE_PER_LEVEL = 30;
    private static final int MIN_SPEED_MS = 100;

    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty linesCleared = new SimpleIntegerProperty(0);

    /**
     * Returns the level as a JavaFX property for UI binding.
     * 
     * @return the IntegerProperty representing the current level
     */
    public IntegerProperty levelProperty() {
        return level;
    }

    /**
     * Returns the lines cleared count as a JavaFX property for UI binding.
     * 
     * @return the IntegerProperty representing the total lines cleared
     */
    public IntegerProperty linesClearedProperty() {
        return linesCleared;
    }

    /**
     * Gets the current level value.
     * 
     * @return the current level (starts at 1)
     */
    public int getLevel() {
        return level.getValue();
    }

    /**
     * Gets the total number of lines cleared.
     * 
     * @return the total lines cleared
     */
    public int getLinesCleared() {
        return linesCleared.getValue();
    }

    /**
     * Adds cleared lines to the total and updates the level accordingly.
     * Level increases by 1 for every 10 lines cleared.
     * 
     * @param lines the number of lines cleared to add
     * @return true if the level increased, false otherwise
     */
    public boolean addLines(int lines) {
        int oldLevel = level.getValue();
        linesCleared.setValue(linesCleared.getValue() + lines);

        int newLevel = (linesCleared.getValue() / LINES_PER_LEVEL) + 1;
        level.setValue(newLevel);

        return newLevel > oldLevel;
    }

    /**
     * Calculates the game speed (fall rate) in milliseconds for the current level.
     * Speed decreases (gets faster) as level increases, with a minimum speed cap.
     * 
     * @return the game speed in milliseconds (lower value = faster falling)
     */
    public int getSpeedForCurrentLevel() {
        int speed = BASE_SPEED_MS - ((level.getValue() - 1) * SPEED_DECREASE_PER_LEVEL);
        return Math.max(speed, MIN_SPEED_MS);
    }

    /**
     * Resets the level and lines cleared to their initial values.
     * Used when starting a new game.
     */
    public void reset() {
        level.setValue(1);
        linesCleared.setValue(0);
    }
}
