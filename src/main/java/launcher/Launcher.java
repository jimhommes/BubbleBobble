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
        initMusicPlayer();
        playMusic(Settings.getBoolean("PLAY_MUSIC", true));
    }

    private static void initMusicPlayer() {
        setMusicSong(Settings.MUSIC_THEME_SONG);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /**
     * This method starts an infinite loop to play the official music of the Bubble Bobble Game.
     * @param playSound Boolean for whether the theme song should be played.
     */
    public static void playMusic(boolean playSound) {
        if (playSound) {
            mediaPlayer.play();
        } else {
            mediaPlayer.stop();
        }
    }

    /**
     * This method sets the path to the song which will be played in the mediaPlayer.
     * @param songPath String with the relative path to the song.
     */
    public static void setMusicSong(String songPath) {
        try {
            Media media = new Media(Launcher.class.getClassLoader().
                    getResource(songPath).toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method changes the song which is currently being played to a different one.
     * @param themeName String with the relative path to the song.
     */
    public static void changeMusicSong(String themeName) {
        if (Settings.getBoolean("PLAY_MUSIC", true) && mediaPlayer != null) {
            mediaPlayer.stop();
            setMusicSong(themeName);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }

    }
}
