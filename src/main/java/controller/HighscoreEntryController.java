package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

/**
 * This class is for the entry for the highscore.
 */
public class HighscoreEntryController extends GridPane {

    @FXML private Text entryNumber;
    @FXML private Text entryName;
    @FXML private Text entryScore;

    private String name;
    private int score;

    /**
     * Constructor for a highscore entry.
     * @param name Name of the player.
     * @param score Score of the player.
     */
    public HighscoreEntryController(String name, int score) {
        this.name = name;
        this.score = score;

        loadFxml();
        setEntry();
    }

    private void loadFxml() {
        URL url = this.getClass().getClassLoader().getResource("highscoreEntry.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void setEntry() {
        entryName.setText(name);
        entryScore.setText(Integer.toString(score));
    }

    /**
     * Sets the number of the entry.
     * @param number The number to be set.
     */
    public void setEntryNumber(int number) {
        entryNumber.setText(Integer.toString(number));
    }

    /**
     * Get the name.
     * @return The name of the highscore entry.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the score.
     * @return The score of the highscore entry.
     */
    public Integer getScore() {
        return score;
    }
}
