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
public class HighscoreEntry extends GridPane implements Comparable<HighscoreEntry> {

    @FXML private Text entryNumber;
    @FXML private Text entryName;
    @FXML private Text entryScore;

    private String name;
    private String score;

    /**
     * Constructor for a highscore entry.
     * @param name Name of the player.
     * @param score Score of the player.
     */
    public HighscoreEntry(String name, String score) {
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
        entryScore.setText(score);
    }

    /**
     * Sets the number of the entry.
     * @param number The number to be set.
     */
    public void setEntryNumber(int number) {
        entryNumber.setText(Integer.toString(number));
    }

    /**
     * Get the score.
     * @return The score of the highscore entry.
     */
    public String getScore() {
        return score;
    }

    /**
     * Compare to function for use of sort.
     * @param o HighscoreEntry to be compared to.
     * @return Output of the string compareTo.
     */
    @Override
    public int compareTo(HighscoreEntry o) {
        return score.compareTo(o.getScore());
    }

//    @Override
//    public boolean equals(String o) {
//
//    }
}
