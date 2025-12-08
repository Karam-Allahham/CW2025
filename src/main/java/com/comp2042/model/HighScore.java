package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.*;

/**
 * Manages the high score functionality with persistent storage.
 * Loads the high score from a file on creation and saves it when a new high score is achieved.
 * Provides JavaFX property binding for UI updates.
 */
public class HighScore {

    private static final String HIGH_SCORE_FILE = "highscore.dat";
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);

    /**
     * Constructs a HighScore instance and loads the saved high score from file.
     */
    public HighScore() {
        load();
    }

    /**
     * Returns the high score as a JavaFX property for UI binding.
     * 
     * @return the IntegerProperty representing the current high score
     */
    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    /**
     * Gets the current high score value.
     * 
     * @return the current high score
     */
    public int getHighScore() {
        return highScore.getValue();
    }

    /**
     * Checks if the new score is higher than the current high score.
     * If so, updates the high score and saves it to file.
     * 
     * @param newScore the score to check against the current high score
     * @return true if the high score was updated, false otherwise
     */
    public boolean checkAndUpdate(int newScore) {
        if (newScore > highScore.getValue()) {
            highScore.setValue(newScore);
            save();
            return true;
        }
        return false;
    }

    /**
     * Saves the current high score to a file.
     * The score is persisted to "highscore.dat" for retrieval in future sessions.
     */
    private void save() {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {
            out.writeInt(highScore.getValue());
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }

    /**
     * Loads the high score from file if it exists.
     * If the file doesn't exist or an error occurs, the high score remains at 0.
     */
    private void load() {
        File file = new File(HIGH_SCORE_FILE);
        if (file.exists()) {
            try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
                highScore.setValue(in.readInt());
            } catch (IOException e) {
                System.err.println("Could not load high score: " + e.getMessage());
            }
        }
    }
}