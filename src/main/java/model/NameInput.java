package model;

import com.sun.tools.javadoc.Start;
import controller.StartController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utility.Settings;

import java.io.IOException;
import java.net.URL;
import java.text.StringCharacterIterator;
import java.util.Set;

/**
 * Input for player name.
 */
public class NameInput extends GridPane {

    @FXML private Text nameInputPrompt;
    @FXML private TextField nameInputField;
    @FXML private Button cancelButton;
    @FXML private Button enterButton;

    private int number;
    private String playerName;


    public NameInput(int playerNumber) {
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

    public String getPlayerName() {
        return playerName;
    }

    @FXML
    public void enterNameInput() {
        String input = nameInputField.getText();

        if (!input.isEmpty()) {
            this.playerName = nameInputField.getText();
        }
        this.setVisible(false);
        Settings.names[number -1] = this.playerName;
        if (number == 1 && StartController.getLimitOfPlayers() == 2) {
            namePlayer2();
        } else {

            try {
                startLevel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void namePlayer2() {
        Stage stage = (Stage) this.getScene().getWindow();
        NameInput nameInput = new NameInput(2);
        stage.setScene(new Scene(nameInput));
        stage.show();
    }

    @FXML
    public void cancelNameInput() {

    }

    /**
     * The function bound to the start button.
     * @throws IOException The exception thrown.
     */
    public void startLevel() throws IOException {
        Stage stage = (Stage) this.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getClassLoader().getResource("level.fxml"));
        stage.setScene(new Scene(newRoot));
        stage.show();
    }

}
