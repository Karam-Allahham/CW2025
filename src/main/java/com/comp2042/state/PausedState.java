package com.comp2042.state;

public class PausedState implements GameState {
    @Override
    public boolean canProcessGameTick() {
        return false;
    }

    @Override
    public boolean canAcceptInput() {
        return false;
    }

    @Override
    public String getStateName() {
        return "Paused";
    }

}
