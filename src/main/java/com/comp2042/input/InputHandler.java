package com.comp2042.input;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.MoveEvent;
import com.comp2042.view.ViewData;
import com.comp2042.event.DownData;
import com.comp2042.event.InputEventListener;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.function.Consumer;

/**
 * Handles keyboard input and translates key presses into game actions.
 * Manages key bindings for movement, rotation, hard drop, pause, and new game.
 * Uses consumers to provide callbacks for preview updates and down events.
 */
public class InputHandler {
    private final Node root;
    private InputEventListener eventListener;


    private Consumer<ViewData> previewConsumer;     // left/right/rotate results
    private Consumer<DownData> downConsumer;        // moveDown results
    private Runnable pauseCallback;              // pause toggle callback

    /**
     * Constructs an InputHandler attached to the specified node.
     * Sets up key event listeners for the root node.
     * 
     * @param root the JavaFX node to attach key listeners to
     * @param eventListener the InputEventListener to receive game events
     */
    public InputHandler(Node root, InputEventListener eventListener) {
        this.root = root;
        this.eventListener = eventListener;
        attach();
    }

    private void attach() {
        root.setOnKeyPressed(this::handleKey);
    }

    /**
     * Sets the consumer callback for preview updates (left, right, rotate events).
     * 
     * @param previewConsumer the consumer to call with ViewData for preview updates
     */
    public void setPreviewConsumer(Consumer<ViewData> previewConsumer) {
        this.previewConsumer = previewConsumer;
    }

    /**
     * Sets the consumer callback for down events (move down and hard drop).
     * 
     * @param downConsumer the consumer to call with DownData for down events
     */
    public void setDownConsumer(Consumer<DownData> downConsumer) {
        this.downConsumer = downConsumer;
    }

    /**
     * Sets the callback for pause toggle actions.
     * 
     * @param pauseCallback the runnable to call when pause is triggered
     */
    public void setPauseCallback(Runnable pauseCallback) {
        this.pauseCallback = pauseCallback;
    }

    /**
     * Handles key press events and translates them into game actions.
     * Key bindings:
     * - Left/A: Move brick left
     * - Right/D: Move brick right
     * - Up/W: Rotate brick
     * - Down/S: Move brick down
     * - Space: Hard drop
     * - P: Pause/unpause
     * - N: New game
     * 
     * @param keyEvent the key event to process
     */
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

        if (code == KeyCode.SPACE) {
            DownData d = eventListener.onHardDropEvent(new MoveEvent(EventType.DOWN, EventSource.USER));
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
