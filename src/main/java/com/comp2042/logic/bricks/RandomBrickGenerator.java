package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.Deque;


public class RandomBrickGenerator implements BrickGenerator {

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    private static final int PREVIEW_COUNT = 3;

    public RandomBrickGenerator() {
        for (int i = 0; i < PREVIEW_COUNT; i++) {
            nextBricks.add(BrickFactory.createRandomBrick());
        }
    }

    @Override
    public Brick getBrick() {
        while (nextBricks.size() < PREVIEW_COUNT) {
            nextBricks.add(BrickFactory.createRandomBrick());
        }
        return nextBricks.poll();
    }

    public Brick[] getPreviewBricks() {
        Brick[] preview = new Brick[2];
        int i = 0;
        for (Brick brick : nextBricks) {
            if (i >= 2) break;
            preview[i++] = brick;
        }
        return preview;
    }

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}
