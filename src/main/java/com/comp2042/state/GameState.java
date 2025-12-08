package com.comp2042.state;

/**
 * Interface defining the behavior for different game states.
 * Implements the State pattern to manage game state transitions and behavior.
 * Each state determines whether the game can process ticks and accept player input.
 */
public interface GameState {
    /**
     * Determines whether the game loop should process game ticks in the current state.
     * 
     * @return true if game ticks should be processed, false otherwise
     */
    boolean canProcessGameTick();

    /**
     * Determines whether the game should accept player input in the current state.
     * 
     * @return true if player input should be accepted, false otherwise
     */
    boolean canAcceptInput();

    /**
     * Gets the name of the current game state.
     * Used for display purposes in the UI.
     * 
     * @return the name of the state (e.g., "Playing", "Paused", "Game Over")
     */
    String getStateName();
}

