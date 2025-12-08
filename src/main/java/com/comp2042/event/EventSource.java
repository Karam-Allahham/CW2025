package com.comp2042.event;

/**
 * Enum representing the source that triggered a game event.
 */
public enum EventSource {
    /** Event triggered by player input (keyboard) */
    USER,
    /** Event triggered by the game loop (automatic brick falling) */
    THREAD
}
