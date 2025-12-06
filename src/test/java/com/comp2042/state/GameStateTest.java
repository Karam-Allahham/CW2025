package com.comp2042.state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;


class GameStateTest {


    @Test
    @DisplayName("PlayingState should allow game tick processing")
    void playingStateShouldAllowGameTick() {
        GameState state = new PlayingState();
        assertTrue(state.canProcessGameTick());
    }

    @Test
    @DisplayName("PlayingState should allow input")
    void playingStateShouldAllowInput() {
        GameState state = new PlayingState();
        assertTrue(state.canAcceptInput());
    }

    @Test
    @DisplayName("PlayingState should return correct name")
    void playingStateShouldReturnCorrectName() {
        GameState state = new PlayingState();
        assertEquals("Playing", state.getStateName());
    }


    @Test
    @DisplayName("PausedState should block game tick processing")
    void pausedStateShouldBlockGameTick() {
        GameState state = new PausedState();
        assertFalse(state.canProcessGameTick());
    }

    @Test
    @DisplayName("PausedState should block input")
    void pausedStateShouldBlockInput() {
        GameState state = new PausedState();
        assertFalse(state.canAcceptInput());
    }

    @Test
    @DisplayName("PausedState should return correct name")
    void pausedStateShouldReturnCorrectName() {
        GameState state = new PausedState();
        assertEquals("Paused", state.getStateName());
    }


    @Test
    @DisplayName("GameOverState should block game tick processing")
    void gameOverStateShouldBlockGameTick() {
        GameState state = new GameOverState();
        assertFalse(state.canProcessGameTick());
    }

    @Test
    @DisplayName("GameOverState should block input")
    void gameOverStateShouldBlockInput() {
        GameState state = new GameOverState();
        assertFalse(state.canAcceptInput());
    }

    @Test
    @DisplayName("GameOverState should return correct name")
    void gameOverStateShouldReturnCorrectName() {
        GameState state = new GameOverState();
        assertEquals("Game Over", state.getStateName());
    }


    @Test
    @DisplayName("All states should implement GameState interface")
    void allStatesShouldImplementInterface() {
        assertInstanceOf(GameState.class, new PlayingState());
        assertInstanceOf(GameState.class, new PausedState());
        assertInstanceOf(GameState.class, new GameOverState());
    }
}