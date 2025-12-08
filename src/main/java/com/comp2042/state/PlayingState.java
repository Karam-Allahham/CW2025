package com.comp2042.state;

/**
 * Represents the normal playing state of the game.
 * In this state, the game loop processes ticks and player input is accepted.
 */
public class PlayingState implements GameState {
    /**
     * Indicates that game ticks should be processed during normal gameplay.
     * 
     * @return true, as the game should continue processing in playing state
     */
    @Override
    public boolean canProcessGameTick() {
        return true;
    }

    /**
     * Indicates that player input should be accepted during normal gameplay.
     * 
     * @return true, as player input should be processed in playing state
     */
    @Override
    public boolean canAcceptInput() {
        return true;
    }

    /**
     * Returns the name of this state for UI display.
     * 
     * @return "Playing" as the state name
     */
    @Override
    public String getStateName() {
        return "Playing";
    }
}
