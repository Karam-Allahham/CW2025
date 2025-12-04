package com.comp2042.state;

public interface GameState {
    boolean canProcessGameTick();

    boolean canAcceptInput();

    String getStateName();
}

