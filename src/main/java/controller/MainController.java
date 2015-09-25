package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
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
}
