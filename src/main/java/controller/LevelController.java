package controller;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.*;
import utility.Logger;
import utility.Settings;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Jim
 * @version 0.1
 * @since 9/5/2015
 */

/**
 * This is the level controller.
 * Here all the interactions with the level happens.
 * It's kind of the main controller.
 */
public class LevelController {

    /**
     * KeyCode for pausing the game.
     */
    private static final KeyCode PAUSE_KEY = KeyCode.P;
    
    /**
     * The list of players in the game.
     */
    @SuppressWarnings("rawtypes")
    private ArrayList players = new ArrayList<>();
    
    /**
     * The list of maps that the user is about to play.
     */
    private ArrayList<String> maps = new ArrayList<>();
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
     * The screenController that handles all GUI.
     */
    private ScreenController screenController;

    /**
     * The gameloop timer. This timer is the main timer.
     */
    private AnimationTimer gameLoop;

    /**
     * The Main Controller.
     */
    private MainController mainController;

    /**
     * The input for the player.
     */
    private Input input;

    /**
     * The path to the maps.
     */
    private String pathMaps = "src/main/resources";

    /**
     * "Key Pressed" handler for pausing the game: register in boolean gamePaused.
     */
    private EventHandler<KeyEvent> pauseKeyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            VBox pauseVBox = mainController.getPauseVBox();
            Text pauseMessage = mainController.getPauseMessage();
            Text pauseMessageSub = mainController.getPauseMessageSub();


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
     * The constructor of this class.
     * @param mainController The main controller that creates this class.
     */
    public LevelController(MainController mainController) {
        this.mainController = mainController;
        this.screenController = mainController.getScreenController();
        findMaps();
        gameLoop = createTimer();
        startLevel(gameLoop);
    }

    /**
     * This function scans the resources folder for maps.
     */
    public void findMaps() {
        File folder = new File(pathMaps);
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
     *
     * @return The gameLoop.
     */
    public AnimationTimer createTimer() {
        return new AnimationTimer() {
            @SuppressWarnings("unchecked")
            @Override
            public void handle(long now) {
                if (((Player) players.get(0)).getGameOver()) {
                    stop();
                } else if (!isGamePaused()) {
                    ((ArrayList<Player>) players).forEach(player -> {
                        player.processInput();
                        player.move();
                        player.getBubbles().forEach(Bubble::move);
                    });
                    ((ArrayList<Monster>) currLvl.getMonsters()).forEach(monster -> {
                        ((ArrayList<Player>) players).forEach(player -> {
                            player.getBubbles().forEach(monster::checkCollision);
                            player.checkCollideMonster(monster);
                        });
                        monster.move();
                    });
                    screenController.updateUI();
                    if (currLvl.update()) {
                        nextLevel();
                    }
                }
            }
        };
    }

    /**
     * This function initializes the level.
     *
     * @param gameLoop is the loop of the game.
     */
    public final void startLevel(AnimationTimer gameLoop) {
        if (maps.size() > 0) {
            indexCurrLvl = 0;

            Pane playfieldLayer = mainController.getPlayfieldLayer();

            playfieldLayer.setOnMousePressed(event -> {
                if (!gameStarted) {
                    gameStarted = true;
                    createInput();

                    createLvl();

                    mainController.hideStartMessage();
                    playfieldLayer.addEventFilter(
                            KeyEvent.KEY_PRESSED, pauseKeyEventHandler);
                    gameLoop.start();
                }
            });
        } else {
            mainController.getPlayfieldLayer().setOnMousePressed(null);
            System.out.println("No maps found!");
        }
    }

    /**
     * This function creates the currLvl'th level.
     */
    @SuppressWarnings("unchecked")
    public final void createLvl() {
        currLvl = new Level(maps.get(indexCurrLvl), this);
        screenController.removeSprites();

        createPlayer(input);

        screenController.addToSprites(currLvl.getWalls());
        screenController.addToSprites(currLvl.getMonsters());
    }

    private void createInput() {
        if(input == null) {
            input = new Input(mainController.getPlayfieldLayer().getScene());
            input.addListeners();
        }
    }

    /**
     * The function that is used to create the player.
     */
    @SuppressWarnings("unchecked")
    public void createPlayer(Input input) {
        double x = 200;
        double y = 200;

        Player player = new Player(x, y, 0, 0, 0, 0, Settings.PLAYER_SPEED, input, this);
        players.clear();
        players.add(player);
        screenController.addToSprites(players);
    }

    /**
     * This function creates the next level.
     */
    public final void nextLevel() {
        indexCurrLvl++;
        if (indexCurrLvl < maps.size()) {
            createLvl();
        } else {
            winGame();
        }
    }

    /**
     * This function checks whether a set of coordinates collide with a wall.
     *
     * @param minX The smallest X
     * @param maxX The highest X
     * @param minY The smallest Y
     * @param maxY The highest Y
     * @return True if a collision was caused.
     */
    @SuppressWarnings("unchecked")
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
     * This function is called when it's game over.
     */
    public void gameOver() {
        Logger.log("Game over!");
        gameLoop.stop();
        mainController.showGameOverScreen();
    }

    /**
     * This method calls the win screen when the game has been won.
     */
    public void winGame() {
        Logger.log("Game won!");
        gameLoop.stop();
        mainController.showWinScreen();
    }

    /**
     * This function returns the maps.
     *
     * @return The maps.
     */
    public ArrayList<String> getMaps() {
        return maps;
    }

    /**
     * This function sets the maps.
     * @param maps The maps to be set.
     */
    public void setMaps(ArrayList<String> maps) {
        this.maps = maps;
    }

    /**
     * This function returns the playfield Layer.
     *
     * @return The playfield Layer.
     */
    public Pane getPlayfieldLayer() {
        return mainController.getPlayfieldLayer();
    }

    /**
     * This function returns the current level index.
     *
     * @return The current level index.
     */
    public int getIndexCurrLvl() {
        return indexCurrLvl;
    }

    /**
     * Gets the screenController.
     *
     * @return The screencontroller.
     */
    public ScreenController getScreenController() {
        return screenController;
    }

    /**
     * This sets the screen Controller.
     *
     * @param screenController The screencontroller to be set.
     */
    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }

    /**
     * This is the boolean to check if the game is paused or not.
     *
     * @return True if the gamePaused is true.
     */
    public boolean isGamePaused() {
        return this.gamePaused;
    }

    public ArrayList getPlayers() {
        return players;
    }

    public Level getCurrLvl() {
        return currLvl;
    }

    public void setPathMaps(String pathMaps) {
        this.pathMaps = pathMaps;
    }

    public boolean getGameStarted() {
        return gameStarted;
    }

    public Input getInput() {
        return input;
    }

    public AnimationTimer getGameLoop() {
        return gameLoop;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public void setIndexCurrLvl(int indexCurrLvl) {
        this.indexCurrLvl = indexCurrLvl;
    }
}
