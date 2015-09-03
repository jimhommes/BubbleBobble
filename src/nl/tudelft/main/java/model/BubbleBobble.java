package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BubbleBobble extends Application {

    /**
     * The main method just launches the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method sets up the application window.
     *
     * The view is loaded from an FXML file. A title for the window is set.
     * The loaded view is set as the current scene.
     *
     * @param primaryStage The primary stage (window).
     * @throws IOException When the FXML file is not found.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("BubbleBobble");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
