package com.comp2042.state;

/**
 * Represents the game over state.
 * In this state, the game loop does not process ticks and player input is not accepted.
 * The game has ended and requires the player to start a new game.
 */
public class GameOverState implements GameState {
    /**
     * Indicates that game ticks should not be processed when the game is over.
     * 
     * @return false, as the game should not process ticks in game over state
     */
    @Override
    public boolean canProcessGameTick() {
        return false;
    }

    /**
     * Indicates that player input should not be accepted when the game is over.
     * 
     * @return false, as player input should not be processed in game over state
     */
    @Override
    public boolean canAcceptInput() {
        return false;
    }

    /**
     * Returns the name of this state for UI display.
     * 
     * @return "Game Over" as the state name
     */
    @Override
    public String getStateName() {
        return "Game Over";
    }
}
