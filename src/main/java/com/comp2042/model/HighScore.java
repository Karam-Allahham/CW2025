package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.*;


public class HighScore {

    private static final String HIGH_SCORE_FILE = "highscore.dat";
    private final IntegerProperty highScore = new SimpleIntegerProperty(0);

    public HighScore() {
        load();
    }

    public IntegerProperty highScoreProperty() {
        return highScore;
    }

    public int getHighScore() {
        return highScore.getValue();
    }


    public boolean checkAndUpdate(int newScore) {
        if (newScore > highScore.getValue()) {
            highScore.setValue(newScore);
            save();
            return true;
        }
        return false;
    }


    private void save() {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {
            out.writeInt(highScore.getValue());
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }


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