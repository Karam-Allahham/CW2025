package com.comp2042.core;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the game's timing loop using JavaFX Timeline.
 * Provides periodic execution of game logic at specified intervals,
 * with the ability to start, stop, and adjust the tick interval.
 */
public class GameLoop {
    private Timeline timeline;
    private Runnable tickCallback;
    private double intervalMs;

    /**
     * Constructs a GameLoop with the specified callback and initial interval.
     * 
     * @param tickCallback the Runnable to execute on each game tick
     * @param initialIntervalMs the initial interval between ticks in milliseconds
     */
    public GameLoop(Runnable tickCallback, double initialIntervalMs) {
        this.tickCallback = tickCallback;
        this.intervalMs = initialIntervalMs;
        createTimeline();
    }

    /**
     * Creates a new Timeline with the current interval setting.
     * The timeline runs indefinitely, calling the tick callback at each interval.
     */
    private void createTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(intervalMs), e -> runTick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Executes the tick callback if one is set.
     */
    private void runTick() {
        if (tickCallback != null) tickCallback.run();
    }

    /**
     * Starts the game loop. Creates a new timeline if one doesn't exist.
     */
    public void start() {
        if (timeline == null) createTimeline();
        timeline.play();
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        if (timeline != null) timeline.stop();
    }

    /**
     * Checks if the game loop is currently running.
     * 
     * @return true if the timeline is running, false otherwise
     */
    public boolean isRunning() {
        return timeline != null && timeline.getStatus() == javafx.animation.Animation.Status.RUNNING;
    }

    /**
     * Sets a new interval for the game loop.
     * If the loop is currently running, it restarts with the new interval.
     * 
     * @param intervalMs the new interval between ticks in milliseconds
     */
    public void setInterval(double intervalMs) {
        this.intervalMs = intervalMs;
        boolean running = isRunning();
        if (timeline != null) timeline.stop();
        createTimeline();
        if (running) timeline.play();
    }

    /**
     * Sets or changes the tick callback that executes on each game tick.
     * 
     * @param tickCallback the Runnable to execute on each tick
     */
    public void setTickCallback(Runnable tickCallback) {
        this.tickCallback = tickCallback;
    }
}

