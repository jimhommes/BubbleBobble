package controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
 * @author Jim
 * @since 9/5/2015
 * @version 0.1
 */
public class LevelController implements Initializable {

    /**
     * The list of players in the game.
     */
    private ArrayList<Player> players;

    /**
     * The image of the player.
     */
    private Image playerImage;

    /**
     * The image of the monster.
     */
    private Image monsterImage;

    /**
     * The message that says "Click when ready".
     */
    @FXML
    private Text startMessage;

    /**
     * The layer the player "moves" in.
     */
    @FXML
    private Pane playfieldLayer;

    /**
     * The StackPane the level is drawn on.
     */
    @FXML
    private StackPane root;

    /**
     * The list of maps that the user is about to play.
     */
    private ArrayList<String> maps;
    /**
     * The current index of the level the user is playing.
     */
    private int indexCurrLvl;

    /**
     * THe current level the user is playing.
     */
    private Level currLvl;

    /**
     * A boolean to see if the game is going on or not.
     */
    private boolean gameStarted = false;

    /**
     * The init function.
     *
     * @param location  The URL
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
                players.forEach(player -> {
                    player.processInput();
                    player.move();
                    player.getBubbles().forEach(bubble -> {
                        bubble.move();
                        bubble.updateUI();
                    });
                    player.updateUI();
                });
                currLvl.getMonsters().forEach(monster -> {
                    monster.move();
                    monster.updateUI();
                });
            }
        };

        startLevel(gameLoop);

    }

    /**
     * The function that is used to create the player.
     */
    private void createPlayer() {
        playerImage = new Image(getClass().getResource("../player.png").toExternalForm());
        Input input = new Input(playfieldLayer.getScene());
        input.addListeners();

        double x = (Settings.SCENE_WIDTH - playerImage.getWidth()) / 2.0;
        double y = Settings.SCENE_HEIGHT * 0.7;

        Player player = new Player(playfieldLayer,
                playerImage, x, y, 0, 0, 0, 0, Settings.PLAYER_SPEED, input);
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
        currLvl = new Level(maps.get(indexCurrLvl), playfieldLayer);
    }

    /**
     * This function creates the next level.
     */
    public final void nextLevel() {
        indexCurrLvl++;
        createLvl();
    }

    /**
     * This function initialises the level.
     */
    public final void startLevel(AnimationTimer gameLoop) {
        if (maps.size() > 0) {
            indexCurrLvl = 0;
            createLvl();
            playfieldLayer.setOnMousePressed(event -> {
                if(!gameStarted) {
                    gameStarted = true;
                    createPlayer();
                    startMessage.setVisible(false);
                    gameLoop.start();
                }
            });
        } else {
            System.out.println("No maps found!");
        }
    }

}
