package com.comp2042.core;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

 public class GameLoop {
    private Timeline timeline;
    private Runnable tickCallback;
    private double intervalMs;

    public GameLoop(Runnable tickCallback, double initialIntervalMs) {
        this.tickCallback = tickCallback;
        this.intervalMs = initialIntervalMs;
        createTimeline();
    }

    private void createTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(intervalMs), e -> runTick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void runTick() {
        if (tickCallback != null) tickCallback.run();
    }

    public void start() {
        if (timeline == null) createTimeline();
        timeline.play();
    }

    public void stop() {
        if (timeline != null) timeline.stop();
    }

    public boolean isRunning() {
        return timeline != null && timeline.getStatus() == javafx.animation.Animation.Status.RUNNING;
    }

    public void setInterval(double intervalMs) {
        this.intervalMs = intervalMs;
        boolean running = isRunning();
        if (timeline != null) timeline.stop();
        createTimeline();
        if (running) timeline.play();
    }

    public void setTickCallback(Runnable tickCallback) {
        this.tickCallback = tickCallback;
    }
}

