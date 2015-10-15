package controller;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.Bubble;
import model.Coordinates;
import model.Input;
import model.Level;
import model.Player;
import model.Monster;
import model.Powerup;
import model.Wall;
import utility.Logger;
import utility.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * This is the Level Controller, here all the interactions with the level happens.
 */
public class LevelController implements Observer {

    private static final KeyCode PAUSE_KEY = KeyCode.P;
    private static final String MAPS_PATH = "src/main/resources";

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<String> maps = new ArrayList<>();
    private ArrayList<Powerup> powerups = new ArrayList<>();

    private ArrayList<Bubble>  bubbles = new ArrayList<>();

    /**
     * The current index of the level the user is playing.
     */

    private int indexCurrLvl;
    private Level currLvl;

    private boolean gameStarted = false;
    private boolean gamePaused = false;

    private ScreenController screenController;
    private AnimationTimer gameLoop;
    private MainController mainController;

    private boolean switchedPauseScreen = false;
    private int limitOfPlayers;

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
    private EventHandler<KeyEvent> pauseKeyEventHandlerRelease = event -> {

        if (event.getCode() == PAUSE_KEY) {
            switchedPauseScreen = false;
        }

    };

    /**
     * The mouse press handler for when the game starts.
     */
    private EventHandler<MouseEvent> startMousePressEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (!gameStarted) {
                gameStarted = true;

                createLvl();

                mainController.hideStartMessage();
                mainController.addListeners(KeyEvent.KEY_PRESSED, pauseKeyEventHandler);
                mainController.addListeners(KeyEvent.KEY_RELEASED, pauseKeyEventHandlerRelease);

                if (players.size() > 0 && players.get(0) != null) {
                    mainController.showLives(players.get(0).getLives());
                    mainController.showScore(players.get(0).getScore());
                } else {
                    mainController.showLives(0);
                    mainController.showScore(0);
                }
                gameLoop.start();
            }
        }
    };

    /**
     * The constructor of this class.
     * @param mainController The main controller that creates this class.
     * @param limitOfPlayers The limit of players allowed by the game.
     */
    public LevelController(MainController mainController, int limitOfPlayers) {
        this.mainController = mainController;
        this.screenController = mainController.getScreenController();
        this.limitOfPlayers = limitOfPlayers;
        findMaps();

        gameLoop = createTimer();
        startLevel();
    }

    /**
     * This function scans the resources folder for maps.
     */
    public void findMaps() {
        File folder = new File(MAPS_PATH);
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
            @Override
            public void handle(long now) {
                boolean stop = true;
                for (Player p : players) {
                    if (!p.isGameOver()) {
                        stop = false;
                    }
                }
                if (stop) {
                    stop();
                } else {
                    if (currLvl.update()) {
						nextLevel();
                    }
                }
            }
        };
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

    private void refresh() {
        bubbles.clear();
        screenController.removeSprites();

    }

    /**
     * This function creates the current level of currLvl.
     */
    public final void createLvl() {
        refresh();
        currLvl = new Level(maps.get(indexCurrLvl), this, limitOfPlayers);

        createPlayers();

        currLvl.getWalls().forEach(wall ->
                screenController.addToSprites(wall.getSpriteBase()));
        currLvl.getMonsters().forEach(monster ->
                screenController.addToSprites(monster.getSpriteBase()));
    }

    private Input createInput(int playerNumber) {
        Input input = mainController.createInput(playerNumber);
        input.addListeners();
        return input;
    }

    /**
     * The function that is used to create the player.
     */
    public void createPlayers() {
        int[] scores = new int[this.players.size()];
        int[] lives = new int[this.players.size()];

        for (int i = 0; i < this.players.size(); i++) {
            scores[i] = this.players.get(i).getScore();
            lives[i] = this.players.get(i).getLives();
        }

        players.forEach(Player::destroy);
        players.clear();
        ArrayList<Player> p = currLvl.getPlayers();

        applyNewPlayers(p, scores, lives);

        players.forEach(player ->
                screenController.addToSprites(player.getSpriteBase()));

    }

    /**
     * This function adds the new players and takes over the previous status.
     * @param p List of players.
     * @param scores The scores.
     * @param lives The lives.
     */
    private void applyNewPlayers(ArrayList<Player> p, int[] scores, int[] lives) {
        for (int i = 0; i < p.size(); i++) {
            Player newPlayer = p.get(i);

            if (scores.length > i) {
                newPlayer.setScore(scores[i]);
                newPlayer.setLives(lives[i]);
            } else {
                newPlayer.setScore(0);
                newPlayer.setLives(Settings.PLAYER_LIVES);
            }

            newPlayer.setInput(createInput(newPlayer.getPlayerNumber()));
            players.add(newPlayer);
        }
    }

    /**
     * This function creates the next level.
     */
    public final void nextLevel() {
        indexCurrLvl++;
        powerups = new ArrayList<>();
        if (indexCurrLvl < maps.size()) {
            createLvl();
        } else {
            winGame();
        }
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
    private void winGame() {
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
    private boolean isGamePaused() {
        return this.gamePaused;
    }

    /**
     * The function that gets the players.
     * @return The players.
     */
	public ArrayList<Player> getPlayers() {
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
     * The function that gets if the game is started.
     * @return True if the game is started.
     */
    public boolean getGameStarted() {
        return gameStarted;
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
	public void setPlayers(ArrayList<Player> players) {
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

        Coordinates powerUpCoordinates = new Coordinates(monster.getSpriteBase().getX(),
                monster.getSpriteBase().getY(), 2, 0, 0, 0);
        
        Powerup powerup = new Powerup(Math.random(), powerUpCoordinates,  randLocX, randLocY, this);
        powerups.add(powerup);
        screenController.addToSprites(powerup.getSpriteBase());

        Logger.log("Powerup spawned at (" + powerup.getSpriteBase().getX() + ", "
                + powerup.getSpriteBase().getY() + ")");
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
    
    /**
     * This method checks if there are any collisions.
     * @param minX The minimum value of the X value.
     * @param maxX The maximum value of the X value.
     * @param minY The minimum value of the Y value.
     * @param maxY The maximum value of the Y value.
     * @return true is there is a collision.
     */
	private boolean causesCollision(double minX, double maxX, double minY, double maxY) {
        for (Wall wall : getCurrLvl().getWalls()) {
            if (wall.getSpriteBase().causesCollision(minX, maxX, minY, maxY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Player) {
            Player p = (Player) o;
            if (p.isDead()) {
                gameOver();
            } else {
                Logger.log(String.format("Score: %d", p.getScore()));
                mainController.showScore(p.getScore());
                mainController.showLives(p.getLives());
            }
        } else if (o instanceof Bubble) {
            Bubble b = (Bubble) o;
            if (b.getIsPopped()) {
                bubbles.remove(b);
            }
        }
    }

    /**
     * This function returns the bubbles.
     * @return The bubbles.
     */
    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    /**
     * This function adds a bubble.
     * @param bubble The bubble.
     */
    public void addBubble(Bubble bubble) {
        bubbles.add(bubble);
        screenController.addToSprites(bubble.getSpriteBase());
    }
}
