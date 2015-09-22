package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

    private LevelController levelController;

    private ScreenController screenController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.levelController = new LevelController(this);
        this.screenController = new ScreenController(playfieldLayer);
    }

    public Pane getPlayfieldLayer() {
        return playfieldLayer;
    }

    public Text getStartMessage() {
        return startMessage;
    }

    public Text getPauseMessage() {
        return pauseMessage;
    }

    public Text getPauseMessageSub() {
        return pauseMessageSub;
    }

    public VBox getPauseVBox() {
        return pauseVBox;
    }

}
