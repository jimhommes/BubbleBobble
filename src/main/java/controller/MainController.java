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
    private Pane playfieldLayer;

    /**
     * The screen controller that handles all GUI/sprite interaction.
     */
    private ScreenController screenController;

    /**
     * The initialize function.
     * @param location The URL.
     * @param resources The resourcebundle.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.screenController = new ScreenController(playfieldLayer);
        new LevelController(this);
    }

    /**
     * This function returns the playfieldLayer.
     * @return The playfieldLayer.
     */
    public Pane getPlayfieldLayer() {
        return playfieldLayer;
    }

    /**
     * This function returns the screenController.
     * @return The ScreenController.
     */
    public ScreenController getScreenController() {
        return screenController;
    }

    /**
     * This function hides the startmessage.
     */
    public void hideStartMessage() {
        startMessage.setVisible(false);
    }

    /**
     * This function shows the win screen.
     */
    public void showWinScreen() {
        Stage stage = (Stage) playfieldLayer.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../win.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function shows the gameover screen.
     */
    public void showGameOverScreen() {
        Stage stage = (Stage) playfieldLayer.getScene().getWindow();
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
    public void showPausescreen() {
        pauseVBox.setVisible(true);
        pauseMessage.setVisible(true);
        pauseMessageSub.setVisible(true);
    }

    /**
     * This function hides the pause screen.
     */
    public void hidePausescreen() {
        pauseVBox.setVisible(true);
        pauseMessage.setVisible(false);
        pauseMessageSub.setVisible(false);
    }
}
