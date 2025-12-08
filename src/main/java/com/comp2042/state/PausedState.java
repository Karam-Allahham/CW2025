package com.comp2042.state;

/**
 * Represents the paused state of the game.
 * In this state, the game loop does not process ticks and player input is not accepted,
 * effectively freezing the game until resumed.
 */
public class PausedState implements GameState {
    /**
     * Indicates that game ticks should not be processed while paused.
     * 
     * @return false, as the game should not process ticks in paused state
     */
    @Override
    public boolean canProcessGameTick() {
        return false;
    }

    /**
     * Indicates that player input should not be accepted while paused.
     * 
     * @return false, as player input should not be processed in paused state
     */
    @Override
    public boolean canAcceptInput() {
        return false;
    }

    /**
     * Returns the name of this state for UI display.
     * 
     * @return "Paused" as the state name
     */
    @Override
    public String getStateName() {
        return "Paused";
    }

}
