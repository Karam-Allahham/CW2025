package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Main entry point for the Tetris game application.
 * Extends JavaFX Application to launch the game window and load the home screen.
 */
public class Main extends Application {

    /**
     * Initializes and starts the JavaFX application.
     * Loads the home screen FXML file and displays the main window.
     * 
     * @param primaryStage the primary stage for the application window
     * @throws Exception if there is an error loading the FXML file or initializing the application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("homeScreen.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 600, 510);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main method that launches the JavaFX application.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
