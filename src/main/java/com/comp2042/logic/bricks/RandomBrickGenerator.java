package com.comp2042.logic.bricks;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Implementation of BrickGenerator that creates random bricks using a queue system.
 * Maintains a preview of upcoming bricks to allow players to see what's coming next.
 * Uses a deque to manage the brick queue efficiently.
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    private static final int PREVIEW_COUNT = 3;

    /**
     * Constructs a RandomBrickGenerator and initializes the queue with random bricks.
     */
    public RandomBrickGenerator() {
        for (int i = 0; i < PREVIEW_COUNT; i++) {
            nextBricks.add(BrickFactory.createRandomBrick());
        }
    }

    /**
     * {@inheritDoc}
     * Returns the next brick from the queue and ensures the queue maintains
     * the minimum preview count by adding new random bricks as needed.
     */
    @Override
    public Brick getBrick() {
        while (nextBricks.size() < PREVIEW_COUNT) {
            nextBricks.add(BrickFactory.createRandomBrick());
        }
        return nextBricks.poll();
    }

    /**
     * {@inheritDoc}
     * Returns an array containing the next two bricks in the queue for preview display.
     */
    @Override
    public Brick[] getPreviewBricks() {
        Brick[] preview = new Brick[2];
        int i = 0;
        for (Brick brick : nextBricks) {
            if (i >= 2) break;
            preview[i++] = brick;
        }
        return preview;
    }

    /**
     * {@inheritDoc}
     * Returns the next brick without removing it from the queue.
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }
}
