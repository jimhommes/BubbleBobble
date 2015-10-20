package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.HighscoreEntry;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the Start Screen Controller, it handles all GUI interactions.
 */
public class StartController implements Initializable {

    @FXML private GridPane helpScreen;
    @FXML private GridPane highscoreScreen;
    @FXML private AnchorPane root;
    @FXML private Button singlePlayerButton;
    @FXML private Button multiPlayerButton;
    @FXML private Button exitButton;
    @FXML private Button helpButton;
    @FXML private Button highscoreButton;

    @FXML private VBox highscores;

    private static int limitOfPlayers;

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
        initHighscoreScreen();
        helpButton.setOnMousePressed((event ->
                helpScreen.visibleProperty().setValue(!helpScreen.isVisible())));
        highscoreButton.setOnMousePressed((event ->
                highscoreScreen.visibleProperty().setValue(!highscoreScreen.isVisible())));
        root.setOnMousePressed(event -> { helpScreen.visibleProperty().setValue(false);
            highscoreScreen.visibleProperty().setValue(false); });
        exitButton.setOnAction((event ->
                System.exit(0)));
    }

    private void initHighscoreScreen() {
        highscores.getChildren().add(
                new HighscoreEntry("1", "testplayer", "20"));
    }

    /**
     * The function bound to the start button.
     * @throws IOException The exception thrown.
     */
    private void startLevel() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getClassLoader().getResource("level.fxml"));
        stage.setScene(new Scene(newRoot));
        stage.show();
    }

    /**
     * Get the limit of players.
     * @return the limit
     */
    public static int getLimitOfPlayers() {
        return StartController.limitOfPlayers;
    }
}
