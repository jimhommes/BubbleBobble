package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.HighscoreEntry;
import utility.Settings;

import java.net.URL;
import java.util.ArrayList;
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
                 limitOfPlayers = 1;
                 inputNamePlayer(1);
         });
        multiPlayerButton.setOnAction(event -> {
            limitOfPlayers = 2;
                inputNamePlayer(1);
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
        ArrayList<HighscoreEntry> tempHighscores = Settings.highscores;
        int scoreIndex = 1;
        for (int i = 0; i < tempHighscores.size(); i++) {
            HighscoreEntry tempEntry = tempHighscores.get(i);
            tempEntry.setEntryNumber(scoreIndex);
            highscores.getChildren().add(tempEntry);
            scoreIndex++;
        }

        while (scoreIndex <= 10) {
            HighscoreEntry emptyEntry = new HighscoreEntry("<empty>", "0");
            emptyEntry.setEntryNumber(scoreIndex);
            highscores.getChildren().add(emptyEntry);
            scoreIndex++;
        }
    }

    /**
     * Create an input for the name of the player.
     * @param playerNumber The number of the player.
     */
    public void inputNamePlayer(int playerNumber) {
        Stage stage = (Stage) root.getScene().getWindow();
        NameInputController nameInputController = new NameInputController(playerNumber);
        stage.setScene(new Scene(nameInputController));
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
