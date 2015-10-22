package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utility.Settings;

import java.io.IOException;
import java.net.URL;

/**
 * Input for player name.
 */
public class NameInputController extends GridPane {

    @FXML private Text nameInputPrompt;
    @FXML private TextField nameInputField;
    @FXML private Button cancelButton;
    @FXML private Button enterButton;

    private int number;
    private String playerName;

    /**
     * A GridPane which asks for the name of the player.
     * @param playerNumber Number of the current player.
     */
    public NameInputController(int playerNumber) {
        this.number = playerNumber;
        this.playerName = String.format("P%d", playerNumber);
        loadFxml();
        setText();
    }

    private void loadFxml() {
        URL url = this.getClass().getClassLoader().getResource("playerNameInput.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void setText() {
        nameInputPrompt.setText("Enter name " + this.playerName);
        nameInputField.setPromptText(this.playerName);
    }

    /**
     * Function to continue when the enter is pressed.
     * @param event KeyEvent that is fired.
     */
    @FXML
    public void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            enterNameInput();
        }
    }

    /**
     * Function for what happens when the enter button is pressed.
     */
    @FXML
    public void enterNameInput() {
        String input = nameInputField.getText();

        if (!input.isEmpty()) {
            this.playerName = nameInputField.getText();
        }
        this.setVisible(false);
        Settings.names[number - 1] = this.playerName;
        if (number == 1 && StartController.getLimitOfPlayers() == 2) {
            namePlayer2();
        } else {

            try {
                switchScreen("level.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void namePlayer2() {
        Stage stage = (Stage) this.getScene().getWindow();
        NameInputController nameInputController = new NameInputController(2);
        stage.setScene(new Scene(nameInputController));
        stage.show();
    }

    /**
     * Function for what happens when the cancel button is pressed.
     * @throws IOException .
     */
    @FXML
    public void cancelNameInput() throws IOException {
        switchScreen("startscreen.fxml");
    }

    private void switchScreen(String fxml) throws IOException {
        Stage stage = (Stage) this.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getClassLoader().getResource(fxml));
        stage.setScene(new Scene(newRoot));
        stage.show();
    }

}
