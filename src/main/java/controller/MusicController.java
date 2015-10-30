package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import utility.Settings;

/**
 * This class controls the music that is played.
 */
public final class MusicController {

    private static MediaPlayer mediaPlayer;
    private static boolean initialised = false;

    private MusicController() {

    }

    /**
     * This function initialises the musicplayer.
     * It only does so if not initialised yet.
     */
    public static void initMusicPlayer() {
        if (!initialised) {
            setMusicSong(Settings.MUSIC_THEME_SONG);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            initialised = true;
        }
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
            Media media = new Media(MusicController.class.getClassLoader().
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
