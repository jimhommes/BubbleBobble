package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import utility.Logger;
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
        Logger.setLogFile("gamelog.txt");
        Logger.setTimestamp("[yyyy-MM-dd hh:mm:ss] - ");
        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../startScreen.fxml"));
        primaryStage.setTitle("Bubble Bobble");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        startMusic();
    }

    /**
     * This method starts an infinite loop to play the official music of the Bubble Bobble Game.
     */
    private void startMusic() {
        String path = getClass().getResource("../themeSong.mp3").toString();
        Media media = new Media(path);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }
}
