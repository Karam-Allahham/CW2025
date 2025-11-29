package com.comp2042.ui;

import com.comp2042.EventSource;
import com.comp2042.EventType;
import com.comp2042.MoveEvent;
import com.comp2042.ViewData;
import com.comp2042.DownData;
import com.comp2042.InputEventListener;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.function.Consumer;


public class InputHandler {
    private final Node root;
    private InputEventListener eventListener;


    private Consumer<ViewData> previewConsumer;     // left/right/rotate results
    private Consumer<DownData> downConsumer;        // moveDown results
    private Runnable pauseCallback;              // pause toggle callback

    public InputHandler(Node root, InputEventListener eventListener) {
        this.root = root;
        this.eventListener = eventListener;
        attach();
    }

    private void attach() {
        root.setOnKeyPressed(this::handleKey);
    }

    public void setPreviewConsumer(Consumer<ViewData> previewConsumer) {
        this.previewConsumer = previewConsumer;
    }

    public void setDownConsumer(Consumer<DownData> downConsumer) {
        this.downConsumer = downConsumer;
    }

    public void setPauseCallback(Runnable pauseCallback) {
        this.pauseCallback = pauseCallback;
    }

    private void handleKey(KeyEvent keyEvent) {
        if (eventListener == null) return;

        KeyCode code = keyEvent.getCode();

        if (code == KeyCode.LEFT || code == KeyCode.A) {
            ViewData v = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
            if (previewConsumer != null) previewConsumer.accept(v);
            keyEvent.consume();
            return;
        }
        if (code == KeyCode.RIGHT || code == KeyCode.D) {
            ViewData v = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
            if (previewConsumer != null) previewConsumer.accept(v);
            keyEvent.consume();
            return;
        }
        if (code == KeyCode.UP || code == KeyCode.W) {
            ViewData v = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
            if (previewConsumer != null) previewConsumer.accept(v);
            keyEvent.consume();
            return;
        }

        if (code == KeyCode.DOWN || code == KeyCode.S) {
            DownData d = eventListener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
            if (downConsumer != null) downConsumer.accept(d);
            keyEvent.consume();
            return;
        }

        if (code == KeyCode.P) {
            if (pauseCallback != null) pauseCallback.run();
            keyEvent.consume();
            return;
        }

        if (code == KeyCode.N) {
            eventListener.createNewGame();
            keyEvent.consume();
        }

    }
}
