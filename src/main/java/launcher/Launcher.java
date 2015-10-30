package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utility.Logger;
import utility.Settings;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class is responsible for the launch of the game.
 */
public class Launcher extends Application {

    /**
     * The main method just launches the application.
     *
     * @param args Command line arguments.
     * @throws FileNotFoundException when the log file is not found.
     */
    public static void main(final String[] args) throws FileNotFoundException {
        Settings.initialize("game.properties");
        Settings.initializeHighscores("highscores.properties");
        Logger.setLogFile(Settings.get("LOGFILE", "gamelog.txt"));
        Logger.setTimestamp(Settings.get("TIMESTAMP", "yyyy-MM-dd hh:mm:ss: "));

        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("startscreen.fxml"));
        primaryStage.setTitle("Bubble Bobble");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


}
