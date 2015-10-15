package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by toinehartman on 01/09/15.
 */

/**
 * This is the StartScreen controller. This controller handles all GUI interactions.
 */
public class StartController implements Initializable {

    /**
     * The help screen.
     */
    @FXML private GridPane helpScreen;

    /**
     * The @FXML annotation links the view element to this object in the controller.
     * The variable name of the object has to match the fx:id of the view element.
     */
    @FXML private AnchorPane root;

    /**
     * The singleplayer button. If you press this the game will start for a single player.
     */
    @FXML private Button singlePlayerButton;

    /**
     * The multiplayer button. If you press this the multiplayer game will start.
     */
    @FXML private Button multiPlayerButton;

    /**
     * The exit button. If you press this the application will close.
     */
    @FXML private Button exitButton;

    /**
     * The help button. If you press this you will be shown some text that should help you.
     */
    @FXML private Button helpButton;

    protected static int limitOfPlayers;

    @Override
    public final void initialize(final URL location, final ResourceBundle resources) {
        singlePlayerButton.setOnAction(event -> {
             try {
                 limitOfPlayers = 1;
                 startLevel();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         });
        multiPlayerButton.setOnAction(event -> {
            limitOfPlayers = 2;
            try {
                startLevel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        helpButton.setOnMousePressed((event ->
                helpScreen.visibleProperty().setValue(!helpScreen.isVisible())));
        root.setOnMousePressed(event -> helpScreen.visibleProperty().setValue(false));
        exitButton.setOnAction((event ->
                System.exit(0)));
    }

    /**
     * The function bound to the start button.
     * @throws IOException The exception thrown.
     */
    private void startLevel() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource("../level.fxml"));
        stage.setScene(new Scene(newRoot));
        stage.show();
    }

}
