package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class Level {

    private static final int LINES_PER_LEVEL = 10;
    private static final int BASE_SPEED_MS = 400;
    private static final int SPEED_DECREASE_PER_LEVEL = 30;
    private static final int MIN_SPEED_MS = 100;

    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty linesCleared = new SimpleIntegerProperty(0);

    public IntegerProperty levelProperty() {
        return level;
    }

    public IntegerProperty linesClearedProperty() {
        return linesCleared;
    }

    public int getLevel() {
        return level.getValue();
    }

    public int getLinesCleared() {
        return linesCleared.getValue();
    }


    public boolean addLines(int lines) {
        int oldLevel = level.getValue();
        linesCleared.setValue(linesCleared.getValue() + lines);

        int newLevel = (linesCleared.getValue() / LINES_PER_LEVEL) + 1;
        level.setValue(newLevel);

        return newLevel > oldLevel;
    }


    public int getSpeedForCurrentLevel() {
        int speed = BASE_SPEED_MS - ((level.getValue() - 1) * SPEED_DECREASE_PER_LEVEL);
        return Math.max(speed, MIN_SPEED_MS);
    }


    public void reset() {
        level.setValue(1);
        linesCleared.setValue(0);
    }
}
