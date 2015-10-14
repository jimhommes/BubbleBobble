package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the Game Over Controller, which controls what happens when the 
 * character is killed and there is a game over.
 * @author Lili de Bree
 *
 */
public class GameEndController implements Initializable {

    /**
     * The @FXML annotation links the view element to this object in the controller.
     * The variable name of the object has to match the fx:id of the view element.
     */
    @FXML private AnchorPane root;

    /**
     * The restart button. When pressed the game will restart.
     */
    @FXML private Button restartButton;

    /**
     * The quit button. If pressed the application will close.
     */
    @FXML private Button quitButton;

    /**
     * Initializes the view.
     * This is the place for setting onclick handlers, for example.
     */
    @Override
    public final void initialize(final URL location, final ResourceBundle resources) {
        restartButton.setOnAction(event -> {
            try {
                returnToMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        quitButton.setOnAction((event ->
                System.exit(0)));
    }

    /**
     * The function bound to the start button.
     * @throws IOException The exception thrown.
     */
    private void returnToMenu() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource("../startScreen.fxml"));
        stage.setScene(new Scene(newRoot));
        stage.show();
    }

}
