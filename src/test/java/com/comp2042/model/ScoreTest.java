package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    @DisplayName("Score should start at zero")
    void scoreShouldStartAtZero() {
        assertEquals(0, score.scoreProperty().getValue());
    }

    @Test
    @DisplayName("Add should increase score by specified amount")
    void addShouldIncreaseScore() {
        score.add(10);
        assertEquals(10, score.scoreProperty().getValue());
    }

    @Test
    @DisplayName("Add should accumulate multiple additions")
    void addShouldAccumulate() {
        score.add(10);
        score.add(25);
        score.add(5);
        assertEquals(40, score.scoreProperty().getValue());
    }

    @Test
    @DisplayName("Reset should set score back to zero")
    void resetShouldSetScoreToZero() {
        score.add(100);
        score.reset();
        assertEquals(0, score.scoreProperty().getValue());
    }

    @Test
    @DisplayName("Score property should be bindable")
    void scorePropertyShouldBeBindable() {
        assertNotNull(score.scoreProperty());
    }
}