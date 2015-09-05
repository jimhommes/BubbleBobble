package controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import model.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Jim on 9/5/2015.
 */
public class LevelController implements Initializable {

    /**
     * The StackPane the level is drawn on.
     */
    @FXML
    private StackPane root;

    /**
     * The canvas that is in the StackPane.
     */
    @FXML
    private Canvas canvas;

    /**
     * The list of maps that the user is about to play.
     */
    private ArrayList<String> maps;

    /**
     * The current level the user is playing.
     */
    private int currLvl;

    ArrayList<Player> players;

    Image playerImage;

    @FXML
    Text startMessage;

    @FXML
    Pane playfieldLayer;

    /**
     * The init function.
     * @param location The URL
     * @param resources The ResourceBundle.
     */
    @Override
    public final void initialize(final URL location, final ResourceBundle resources) {
        maps = new ArrayList<>();
        players = new ArrayList<>();
        findMaps();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                players.forEach(Player::processInput);
                players.forEach(Player::move);
                players.forEach(SpriteBase::updateUI);
            }
        };

        if (maps.size() > 0) {
            currLvl = 0;
            createLvl();
            playfieldLayer.setOnMousePressed(event -> {
                createPlayer();
                startMessage.setVisible(false);
                gameLoop.start();
            });
        } else {
            System.out.println("No maps found!");
        }

    }

    private void createPlayer() {
        playerImage = new Image(getClass().getResource("../player.png").toExternalForm());
        Input input = new Input(playfieldLayer.getScene());
        input.addListeners();

        Image image = playerImage;

        double x = (Settings.SCENE_WIDTH - image.getWidth()) / 2.0;
        double y = Settings.SCENE_HEIGHT * 0.7;

        Player player = new Player(playfieldLayer, image, x, y, 0, 0, 0, 0, Settings.PLAYER_SPEED, input);
        players.add(player);
    }

    /**
     * This function scans the resources folder for maps.
     */
    private void findMaps() {
        File folder = new File("src/main/resources");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().matches("map[0-9]*.txt")) {
                    maps.add(file.getName());
                }
            }
        }
    }

    /**
     * This function creats the currLvl'th level.
     */
    public final void createLvl() {
        new Level(maps.get(currLvl), canvas);
    }

    /**
     * This function creates the next level.
     */
    public final void nextLevel() {
        currLvl++;
        createLvl();
    }

}
