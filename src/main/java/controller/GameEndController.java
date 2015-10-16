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
 */
public class GameEndController implements Initializable {

    @FXML private AnchorPane root;
    @FXML private Button restartButton;
    @FXML private Button quitButton;

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
        Parent newRoot = FXMLLoader.load(getClass().getClassLoader().getResource("startScreen.fxml"));
        stage.setScene(new Scene(newRoot));
        stage.show();
    }

}
