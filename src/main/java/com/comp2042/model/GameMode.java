package com.comp2042.model;

/**
 * Enum representing the different game modes available in the Tetris game.
 */
public enum GameMode {
    /**
     * Classic endless mode where the game continues until the player loses.
     * The player aims to achieve the highest score possible.
     */
    CLASSIC,
    
    /**
     * Sprint mode where the player must clear a target number of lines as fast as possible.
     * The game ends when the target is reached or when the player loses.
     */
    SPRINT
}