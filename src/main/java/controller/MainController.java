package controller;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Input;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the Main Controller, that controls the other controllers.
 */
public class MainController implements Initializable {

    @FXML private Text startMessage;
    @FXML private Text pauseMessage;
    @FXML private Text pauseMessageSub;
    @FXML private VBox pauseVBox;
    @FXML private Pane playFieldLayer;
    @FXML private Text livesText;
    @FXML private Text scoreText;
    @FXML private Text livesTextPlayer1;
    @FXML private Text livesTextPlayer2;
    @FXML private Text scoreTextPlayer1;
    @FXML private Text scoreTextPlayer2;

    private ScreenController screenController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.screenController = new ScreenController(playFieldLayer);
        new LevelController(this, StartController.getLimitOfPlayers());
    }

    /**
     * This function returns the playFieldLayer.
     * @return The playFieldLayer.
     */
    public Pane getPlayFieldLayer() {
        return playFieldLayer;
    }

    /**
     * This function returns the screenController.
     * @return The ScreenController.
     */
    public ScreenController getScreenController() {
        return screenController;
    }

    /**
     * This function hides the start message.
     */
    public void hideStartMessage() {
        startMessage.setVisible(false);
    }

    /**
     * Show lives in the top bar.
     *
     * @param lives The number of lives.
     * @param playerNumber The number of the player (used for multiplayer).
     */
    public void showLives(int lives, int playerNumber) {

        livesText.setVisible(true);

        if (playerNumber == 1) {
            livesTextPlayer1.setVisible(true);

            if (StartController.getLimitOfPlayers() == 1) {
                livesTextPlayer1.setText(String.format("%d", lives));
            } else {
                livesTextPlayer1.setText(String.format("P1: %d", lives));
            }

        } else if (playerNumber == 2) {
            livesTextPlayer2.setVisible(true);
            livesTextPlayer2.setText(String.format("P2: %d", lives));
        }
    }

    /**
     * This function show the score of the player.
     *
     * @param score The score (number of points).
     * @param playerNumber The number of the player (used for multiplayer).
     */
    public void showScore(int score, int playerNumber) {
        scoreText.setVisible(true);

        if (playerNumber == 1) {
            scoreTextPlayer1.setVisible(true);
            scoreTextPlayer1.setText(String.format("%d", score));
        } else if (playerNumber == 2) {
            scoreTextPlayer2.setVisible(true);
            scoreTextPlayer2.setText(String.format("%d", score));
        }
    }

    /**
     * This function shows the win screen.
     */
    public void showWinScreen() {
        Stage stage = (Stage) playFieldLayer.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("win.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function shows the game over screen.
     */
    public void showGameOverScreen() {
        Stage stage = (Stage) playFieldLayer.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gameOver.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function shows the pause screen.
     */
    public void showPauseScreen() {
        pauseVBox.setVisible(true);
        pauseMessage.setVisible(true);
        pauseMessageSub.setVisible(true);
    }

    /**
     * This function hides the pause screen.
     */
    public void hidePauseScreen() {
        pauseVBox.setVisible(true);
        pauseMessage.setVisible(false);
        pauseMessageSub.setVisible(false);
    }

    /**
     * This function adds a listener.
     * @param type The type of the listener.
     * @param handler The handler of the listener.
     */
    public void addListeners(EventType<KeyEvent> type, EventHandler<KeyEvent> handler) {
        playFieldLayer.getScene().addEventFilter(type, handler);
    }

    /**
     * This creates an input for the controls.
     * @param playerNumber The number of the player.
     * @return The Input
     */
    public Input createInput(int playerNumber) {
        return new Input(playFieldLayer.getScene(), playerNumber);
    }
}
