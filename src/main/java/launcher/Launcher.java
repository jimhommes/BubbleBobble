package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import utility.Logger;
import utility.Settings;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class is responsible for the launch of the game.
 */
public class Launcher extends Application {

    private static MediaPlayer mediaPlayer;

    /**
     * The main method just launches the application.
     *
     * @param args Command line arguments.
     * @throws FileNotFoundException when the log file is not found.
     */
    public static void main(final String[] args) throws FileNotFoundException {
        Logger.setLogFile("gamelog.txt");
        Logger.setTimestamp("[yyyy-MM-dd hh:mm:ss] - ");
        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("startscreen.fxml"));
        primaryStage.setTitle("Bubble Bobble");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        initialize();
        toggleMusic(Settings.PLAY_SOUND);
    }

    private void initialize() {
        String path = getClass().getClassLoader().getResource("themesong.mp3").toString();
        Media media = new Media(path);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /**
     * This method starts an infinite loop to play the official music of the Bubble Bobble Game.
     * @param playSound Boolean for whether the theme song should be played.
     */
    public void toggleMusic(boolean playSound) {
        if (playSound) {
            mediaPlayer.play();
        } else {
            mediaPlayer.stop();
        }
    }

}
