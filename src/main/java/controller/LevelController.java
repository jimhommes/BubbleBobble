package controller;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.Bubble;
import model.Input;
import model.Level;
import model.Monster;
import model.Player;
import model.Powerup;
import model.Wall;
import utility.Logger;
import utility.Settings;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Jim
 * @version 0.1
 * @since 9/5/2015
 * Last Modified: Lili
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
     * The list of powerups.
     */
    private ArrayList<Powerup> powerups = new ArrayList<>();
    /**
     * The current index of the level the user is playing.
     */
    private int indexCurrLvl;
    /**
     * The current level the user is playing.
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
     * The gameLoop timer. This timer is the main timer.
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
     * The boolean preventing the pauseScreen from switching many times.
     */
    private boolean switchedPauseScreen = false;

    /**
     * "Key Pressed" handler for pausing the game: register in boolean gamePaused.
     */
    private EventHandler<KeyEvent> pauseKeyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            if (event.getCode() == PAUSE_KEY && !switchedPauseScreen) {
                switchedPauseScreen = true;
                gamePaused = !gamePaused;
                if (gamePaused) {
                    mainController.showPauseScreen();
                } else {
                    mainController.hidePauseScreen();
                }
            }

        }
    };

    /**
     * "Key Pressed" handler for pausing the game: register in boolean gamePaused.
     */
    private EventHandler<KeyEvent> pauseKeyEventHandlerRelease = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            if (event.getCode() == PAUSE_KEY) {
                switchedPauseScreen = false;
            }

        }
    };

    /**
     * The mousepress handler for when the game starts.
     */
    private EventHandler<MouseEvent> startMousePressEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (!gameStarted) {
                gameStarted = true;
                createInput();

                createLvl();

                mainController.hideStartMessage();
                mainController.addListeners(KeyEvent.KEY_PRESSED, pauseKeyEventHandler);
                mainController.addListeners(KeyEvent.KEY_RELEASED, pauseKeyEventHandlerRelease);
                gameLoop.start();
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
        startLevel();
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
                } else {
                    if (!isGamePaused()) {
                        ((ArrayList<Player>) players).forEach(player -> {
                            performPlayerCycle(player);
                            powerups.forEach(powerup -> performPowerupsCycle(powerup, player));
                            updatePowerups();
                        });
                        ((ArrayList<Monster>) currLvl.getMonsters()).forEach(monster -> {
                            ((ArrayList<Player>) players).forEach(player -> {
                                player.getBubbles().forEach(monster::checkCollision);
                                player.checkCollideMonster(monster);
                            });
                            monster.move();
                        });
                    }
                    screenController.updateUI();
                    if (currLvl.update()) {
						nextLevel();
                    }
                }
            }
        };
    }

    /**
     * This is the cycle that performs all powerups operations.
     * @param powerup The powerup actions are performed on
     * @param player The player there might be a collision with.
     */
    public void performPowerupsCycle(Powerup powerup, Player player) {
        powerup.causesCollision(player);
        powerup.move();
    }

    /**
     * This is the cycle that performs all player operations.
     * @param player The player the actions are performed on.
     */
    private void performPlayerCycle(Player player) {
        player.processInput();
        player.move();
        player.checkBubbles();
        player.getBubbles().forEach(Bubble::move);
    }

    /**
     * This function updates the powerups, and removes
     * the ones which have been picked up.
     */
    public void updatePowerups() {
        ArrayList<Powerup> nPowerups = new ArrayList<>();
        for (Powerup powerup : powerups) {
            if (!powerup.getPickedUp()) {
                nPowerups.add(powerup);
            }
        }
        powerups = nPowerups;
    }

    /**
     * This function initializes the level.
     */
    public final void startLevel() {
        if (maps.size() > 0) {
            indexCurrLvl = 0;

            Pane playFieldLayer = mainController.getPlayFieldLayer();

            playFieldLayer.setOnMousePressed(startMousePressEventHandler);
        } else {
            mainController.getPlayFieldLayer().setOnMousePressed(null);
            Logger.log("No maps found!");
        }
    }

    /**
     * This function creates the current level of currLvl.
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
        if (input == null) {
            input = mainController.createInput();
            input.addListeners();
        }
    }

    /**
     * The function that is used to create the player.
     * @param input The input.
     */
    @SuppressWarnings("unchecked")
    public void createPlayer(Input input) {
        players.clear();
        ArrayList<Player> players = currLvl.getPlayers();
        players.forEach(player -> {
            player.setInput(input);
            this.players.add(player);
        });

        screenController.addToSprites(this.players);
    }

    /**
     * This function creates the next level.
     */
    public final void nextLevel() {
        indexCurrLvl++;
        players = new ArrayList<>();
        powerups = new ArrayList<>();
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
            if (wall.causesCollision(minX, maxX, minY, maxY)) {
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
     * This function returns the playFieldLayer.
     *
     * @return The playFieldLayer.
     */
    public Pane getPlayFieldLayer() {
        return mainController.getPlayFieldLayer();
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
     * @return The screenController.
     */
    public ScreenController getScreenController() {
        return screenController;
    }

    /**
     * This sets the screen Controller.
     *
     * @param screenController The screenController to be set.
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

    /**
     * The function that gets the players.
     * @return The players.
     */
    @SuppressWarnings("rawtypes")
	public ArrayList getPlayers() {
        return players;
    }

    /**
     * The function that returns the current level.
     * @return The current level.
     */
    public Level getCurrLvl() {
        return currLvl;
    }

    /**
     * The function that sets the path to the maps.
     * @param pathMaps The path to the maps.
     */
    public void setPathMaps(String pathMaps) {
        this.pathMaps = pathMaps;
    }
    
    /**
     * This method gets the path of the maps.
     * @return pathMaps, the path to the maps.
     */
    public String getPathMaps() {
    	return pathMaps;
    }

    /**
     * The function that gets if the game is started.
     * @return True if the game is started.
     */
    public boolean getGameStarted() {
        return gameStarted;
    }

    /**
     * This function returns the input.
     * @return The input.
     */
    public Input getInput() {
        return input;
    }

    /**
     * This function sets the input.
     * @param input The input.
     */
    public void setInput(Input input) {
        this.input = input;
    }

    /**
     * This function sets if the game has started.
     * @param gameStarted True if the game has started.
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * This function sets the current index level.
     * @param indexCurrLvl The index of the curr level.
     */
    public void setIndexCurrLvl(int indexCurrLvl) {
        this.indexCurrLvl = indexCurrLvl;
    }

    /**
     * This function sets the players.
     * @param players The players.
     */
    @SuppressWarnings("rawtypes")
	public void setPlayers(ArrayList players) {
        this.players = players;
    }

    /**
     * This function sets the current level.
     * @param currLvl The current level.
     */
    public void setCurrLvl(Level currLvl) {
        this.currLvl = currLvl;
    }

    /**
     * This function gets the pauseKeyEventHandler.
     * @return The pause key event handler.
     */
    public EventHandler<KeyEvent> getPauseKeyEventHandler() {
        return pauseKeyEventHandler;
    }

    /**
     * This function returns true if the game is paused.
     * @return True if the game is paused.
     */
    public boolean getGamePaused() {
        return gamePaused;
    }

    /**
     * This function spawns a powerup when a monster dies.
     * @param monster The monster that died
     */
    public void spawnPowerup(Monster monster) {
        double randLocX = Math.random() * Settings.SCENE_WIDTH;
        double randLocY = Math.random() * Settings.SCENE_HEIGHT;

        while (causesCollision(randLocX,
                randLocX + Settings.SPRITE_SIZE, randLocY, randLocY + Settings.SPRITE_SIZE)) {
            randLocX = Math.random() * Settings.SCENE_WIDTH;
            randLocY = Math.random() * Settings.SCENE_HEIGHT;
        }

        Powerup powerup = new Powerup(Math.random(), monster.getX(),
                monster.getY(), 2, 0, 0, 0, randLocX, randLocY, this);
        powerups.add(powerup);
        screenController.addToSprites(powerup);

        Logger.log("Powerup spawned at (" + powerup.getX() + ", " + powerup.getY() + ")");
        Logger.log("Powerup going to (" + randLocX + ", " + randLocY + ")");
    }

    /**
     * This function returns the powerups.
     * @return The powerups.
     */
    public ArrayList<Powerup> getPowerups() {
        return powerups;
    }

    /**
     * This function sets the powerups.
     * @param powerups The powerups.
     */
    public void setPowerups(ArrayList<Powerup> powerups) {
        this.powerups = powerups;
    }

    /**
     * This function returns the pausekey handler for releasing.
     * @return The pauseKeyEventHandlerRelease
     */
    public EventHandler<KeyEvent> getPauseKeyEventHandlerRelease() {
        return pauseKeyEventHandlerRelease;
    }

    /**
     * This function returns the mousePressEventHandler.
     * @return The startMousePressEventHandler.
     */
    public EventHandler<MouseEvent> getStartMousePressEventHandler() {
        return startMousePressEventHandler;
    }
}
