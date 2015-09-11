package controller;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Input;
import model.Level;
import model.Monster;
import model.Player;
import model.Settings;
import model.Wall;

import java.io.File;
import java.io.IOException;
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
    private ArrayList players;

    /**
     * The message that says "Click when ready".
     */
    @FXML
    private Text startMessage;
    
    /**
     * The message that says "Game Paused".
     */
    @FXML
    private Text pauseMessage;
    
    /**
     * The message that gives extra information when game is paused.
     */
    @FXML
    private Text pauseMessageSub;

    /**
     * The VBox that contains pauseMessage and pauseMessageSub.
     */
    @FXML
    private VBox pauseVBox;
    
    /**
     * The layer the player "moves" in.
     */
    @FXML
    private Pane playfieldLayer;

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
     * A boolean to see if the game is going on or not.
     */
    private boolean gamePaused = false;

    /**
     * KeyCode for pausing the game. 
     */
    private static final KeyCode PAUSE_KEY = KeyCode.P;

    /**
     * The screenController that handles all GUI.
     */
    private ScreenController screenController;
    
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

        AnimationTimer gameLoop = createTimer();
        this.screenController = new ScreenController(playfieldLayer);
        startLevel(gameLoop);
    }

    /**
     * This function scans the resources folder for maps.
     */
    private void findMaps() {
        File folder = new File("src/main/resources");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().matches("map[0-9]*.txt")) {
                maps.add(file.getName());
            }
        }
    }

    /**
     * This function returns the gameLoop.
     * @return The gameLoop.
     */
    private AnimationTimer createTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (((Player) players.get(0)).getGameOver()) {
                    stop();
                } else if (!isGamePaused()) {
                    ((ArrayList<Player>) players).forEach(player -> {
                        player.processInput();
                        player.move();
                        player.getBubbles().forEach(bubble -> {
                            bubble.move();
                        });
                    });
                    ((ArrayList<Monster>) currLvl.getMonsters()).forEach(monster -> {
                        ((ArrayList<Player>) players).forEach(player -> {
                            player.getBubbles().forEach(monster::checkCollision);
                            player.checkCollideMonster(monster);
                        });
                        monster.move();
                    });
                    screenController.updateUI();
                }
            }
        };
    }

    /**
     * This function initializes the level.
     * @param gameLoop is the loop of the game.
     */
    public final void startLevel(AnimationTimer gameLoop) {
        if (maps.size() > 0) {
            indexCurrLvl = 0;
            playfieldLayer.setOnMousePressed(event -> {
                if (!gameStarted) {
                    gameStarted = true;
                    createLvl();
                    createPlayer();
                    startMessage.setVisible(false);
                    playfieldLayer.getScene().addEventFilter(
                            KeyEvent.KEY_PRESSED, pauseKeyEventHandler);
                    gameLoop.start();
                }
            });
        } else {
            System.out.println("No maps found!");
        }
    }

    /**
     * This function creates the currLvl'th level.
     */
    public final void createLvl() {
        currLvl = new Level(maps.get(indexCurrLvl), this);
        screenController.addToSprites(currLvl.getWalls());
        screenController.addToSprites(currLvl.getMonsters());
    }

    /**
     * The function that is used to create the player.
     */
    private void createPlayer() {
        Input input = new Input(playfieldLayer.getScene());
        input.addListeners();

        double x = 200;
        double y = 700;

        Player player = new Player(x, y, 0, 0, 0, 0, Settings.PLAYER_SPEED, input, this);
        players.add(player);
        screenController.addToSprites(players);
    }
    
    /**
     * "Key Pressed" handler for pausing the game: register in boolean gamePaused.
     */
    private EventHandler<KeyEvent> pauseKeyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            // pause game on keypress PAUSE_KEY
            if (event.getCode() == PAUSE_KEY) {
                pauseVBox.setVisible(true);
                pauseMessage.setVisible(true);
                pauseMessageSub.setVisible(true);
                gamePaused = true;
            }
            
            //unpause game on keypress anything except PAUSE_KEY
            if (gamePaused && event.getCode() != PAUSE_KEY) {
                pauseVBox.setVisible(true);
                pauseMessage.setVisible(false);
                pauseMessageSub.setVisible(false);
                gamePaused = false;
            }

        }
    };

    /**
     * This function creates the next level.
     */
    public final void nextLevel() {
        indexCurrLvl++;
        createLvl();
    }

    /**
     * This function checks whether a set of coordinates collide with a wall.
     * @param minX The smallest X
     * @param maxX The highest X
     * @param minY The smallest Y
     * @param maxY The highest Y
     * @return True if a collision was caused.
     */
    public boolean causesCollision(double minX, double maxX, double minY, double maxY) {

        for (Wall wall : (ArrayList<Wall>) currLvl.getWalls()) {
            double wallMinX = wall.getX();
            double wallMaxX = wallMinX + wall.getWidth();
            double wallMinY = wall.getY();
            double wallMaxY = wallMinY + wall.getHeight();
            if (((minX > wallMinX && minX < wallMaxX) 
            		|| (maxX > wallMinX && maxX < wallMaxX) 
            		|| (wallMinX > minX && wallMinX < maxX) 
                    || (wallMaxX > minX && wallMaxX < maxX))
            		&& ((minY > wallMinY && minY < wallMaxY) 
                    		|| (maxY > wallMinY && maxY < wallMaxY) 
                            || (wallMinY > minY && wallMinY < maxY) 
                            || (wallMaxY > minY && wallMaxY < maxY))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the screenController.
     * @return The screencontroller.
     */
    public ScreenController getScreenController() {
        return screenController;
    }

    /**
     * This is the boolean to check if the game is paused or not.
     *
     * @return True if the gamePaused is true.
     */
    public boolean isGamePaused() {
        return this.gamePaused;
    }

    /**
     * This function is called when it's game over.
     */
    public void gameOver() {
        Stage stage = (Stage) playfieldLayer.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../gameOver.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
