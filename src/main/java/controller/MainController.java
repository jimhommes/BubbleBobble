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
 * Created by Jim on 9/22/2015.
 *
 * @author Jim
 * @version 1.0
 * @since 9/22/2015
 */
public class MainController implements Initializable {

    /**
     * The message that says "Click when ready".
     */
    @FXML
    private Text startMessage;
    /**
     * The message that says "Game Paused".
     */
    @FXML
    private Text pauseMessage;
    /**
     * The message that gives extra information when game is paused.
     */
    @FXML
    private Text pauseMessageSub;
    /**
     * The VBox that contains pauseMessage and pauseMessageSub.
     */
    @FXML
    private VBox pauseVBox;
    /**
     * The layer the player "moves" in.
     */
    @FXML
    private Pane playFieldLayer;
    /**
     * The number of lives left text box.
     */
    @FXML
    private Text livesText;
    /**
     * The score textbox.
     */
    @FXML
    private Text scoreText;

    /**
     * The screen controller that handles all GUI/sprite interaction.
     */
    private ScreenController screenController;

    /**
     * The initialize function.
     * @param location The URL.
     * @param resources The resourceBundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.screenController = new ScreenController(playFieldLayer);
        new LevelController(this);
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
     */
    public void showLives(int lives) {
        livesText.setVisible(true);
        livesText.setText(String.format("Lives: %d", lives));
    }

    /**
     * This function show the score of the player.
     *
     * @param score The score (number of points).
     */
    public void showScore(int score) {
        scoreText.setVisible(true);
        scoreText.setText(String.format("Score: %d", score));
    }

    /**
     * This function shows the win screen.
     */
    public void showWinScreen() {
        Stage stage = (Stage) playFieldLayer.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../win.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("../gameOver.fxml"));
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
     * @return The Input
     */
    public Input createInput() {
        return new Input(playFieldLayer.getScene());
    }
}
