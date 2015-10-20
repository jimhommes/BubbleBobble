package model;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

/**
 * Entry for the highscore.
 */
public class HighscoreEntry extends GridPane {

    @FXML private Text entryNumber;
    @FXML private Text entryName;
    @FXML private Text entryScore;

    private String number;
    private String name;
    private String score;

    /**
     * Constructor for a highscore entry.
     * @param number Number on the highscore list.
     * @param name Name of the player.
     * @param score Score of the player.
     */
    public HighscoreEntry(String number, String name, String score) {

        this.number = number;
        this.name = name;
        this.score = score;

        loadFxml();
        setEntry();
    }

    private void loadFxml() {
        URL url = this.getClass().getClassLoader().getResource("highscoreEntry.fxml");
        System.out.println(url.toString());
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
        entryNumber.setText(number);
        entryName.setText(name);
        entryScore.setText(score);
        System.out.println(entryName.getText());
    }


}
