package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class is responsible for the launch of the game.
 */
public class Launcher extends Application {

    /**
     * The main method just l   aunches the application.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) throws FileNotFoundException {
//        Logger.setLogFile("gamelog.txt");
        Logger.setTimestampFormat("[yyyy-MM-dd hh:mm:ss] - ");
        launch(args);
    }

    /**
     * The start method sets up the application window.
     * <p>
     * The view is loaded from an FXML file. A title for the window is set.
     * The loaded view is set as the current scene.
     *
     * @param primaryStage The primary stage (window).
     * @throws IOException When the FXML file is not found.
     */
    @Override
    public final void start(final Stage primaryStage) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("../startscreen.fxml"));
        primaryStage.setTitle("Bubble Bobble");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
