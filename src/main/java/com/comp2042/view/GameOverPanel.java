package com.comp2042.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Panel component that displays the "GAME OVER" message.
 * Extends BorderPane to provide a container for the game over label.
 */
public class GameOverPanel extends BorderPane {

    /**
     * Constructs a GameOverPanel with a styled "GAME OVER" label.
     * The label is centered in the panel and uses the "gameOverStyle" CSS class.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}
