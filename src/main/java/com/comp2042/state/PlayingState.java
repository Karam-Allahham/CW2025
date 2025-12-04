package com.comp2042.state;

public class PlayingState implements GameState {
    @Override
    public boolean canProcessGameTick() {
        return true;
    }

    @Override
    public boolean canAcceptInput() {
        return true;
    }

    @Override
    public String getStateName() {
        return "Playing";
    }
}
