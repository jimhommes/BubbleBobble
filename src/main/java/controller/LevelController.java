package controller;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import launcher.Launcher;
import model.Bubble;
import model.Coordinates;
import model.Input;
import model.Level;
import model.LevelFactory;
import model.Monster;
import model.Player;
import model.Powerup;
import model.Wall;
import model.FinalEnemy;
import utility.Logger;
import utility.Settings;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * This is the Level Controller, here all the interactions with the level happens.
 */
public class LevelController implements Observer {

    private static final KeyCode PAUSE_KEY = KeyCode.P;

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

    private ScreenController screenController;
    private AnimationTimer gameLoop;
    private MainController mainController;
    private LevelFactory levelFactory;
    private LevelControllerMethods levelControllerMethods;

    private boolean switchedPauseScreen = false;
    private boolean muteKeyPressed = false;

    private int limitOfPlayers;

    /**
     * "Key Pressed" handler for pausing the game: register in boolean gamePaused.
     */
    private EventHandler<KeyEvent> pauseKeyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            if (event.getCode() == PAUSE_KEY && !switchedPauseScreen) {
                switchedPauseScreen = true;
                levelControllerMethods.setGamePaused(!levelControllerMethods.getGamePaused());
                if (levelControllerMethods.getGamePaused()) {
                    mainController.showPauseScreen();
                } else {
                    mainController.hidePauseScreen();
                }
            }

        }
    };

    /**
     * "Key Released" handler for pausing the game: register in boolean gamePaused.
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

               addMuteListeners();

                if (players.size() > 0 && players.get(0) != null) {
                    Player player = players.get(0);
                    mainController.showLives(player.getLives(), player.getPlayerNumber());
                    mainController.showScore(player.getScore(), player.getPlayerNumber());
                } else {
                    mainController.showLives(0, 0);
                    mainController.showScore(0, 0);
                }
                for (Monster monster : currLvl.getMonsters()) {
                  if (monster instanceof FinalEnemy) {
                    mainController.showEnemyLives(((FinalEnemy) monster).showLives());
                  }
                  
                }
                gameLoop.start();
            }
        }
    };

    private void addMuteListeners() {
        mainController.addListeners(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (!muteKeyPressed
            		&& keyEvent.getCode() == Settings.getKeyCode("MUTE_KEY", KeyCode.M)) {
                muteKeyPressed = true;
                Settings.setBoolean("PLAY_MUSIC", !Settings.getBoolean("PLAY_MUSIC", false));
                Launcher.playMusic(Settings.getBoolean("PLAY_MUSIC", true));
            }
        });

        mainController.addListeners(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == Settings.getKeyCode("MUTE_KEY", KeyCode.M)) {
                muteKeyPressed = false;
            }
        });
    }

    /**
     * The constructor of this class.
     * @param mainController The main controller that creates this class.
     * @param limitOfPlayers The limit of players allowed by the game.
     */
    public LevelController(MainController mainController, int limitOfPlayers) {
        this.mainController = mainController;
        this.screenController = mainController.getScreenController();
        this.levelControllerMethods = new LevelControllerMethods(this);
        this.limitOfPlayers = limitOfPlayers;
        this.levelFactory = new LevelFactory(this);
        maps = levelControllerMethods.findMaps();

        gameLoop = createTimer();
        startLevel();
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
                    if (!p.isDead()) {
                        stop = false;
                    }
                }
                if (stop) {
                    stop();
                    gameOver();
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
        bubbles.forEach(Bubble::destroy);
        bubbles.clear();
        currLvl.getMonsters().forEach(Monster::destroy);
        currLvl.getMonsters().clear();
        powerups.forEach(Powerup::destroy);
        powerups.clear();
        screenController.removeSprites();
    }

    /**
     * This function creates the current level of currLvl.
     */
    public final void createLvl() {
        currLvl = levelFactory.makeLevel(maps.get(indexCurrLvl), limitOfPlayers);

        setMusic();
        createPlayers();

        currLvl.getWalls().forEach(wall ->
                screenController.addToSprites(wall.getSpriteBase()));
        currLvl.getMonsters().forEach(monster ->
                screenController.addToSprites(monster.getSpriteBase()));
    }

    /**
     * This function creates an Input for a playernumber.
     * @param playerNumber The number of the player.
     * @return The input for the player.
     */
    public Input createInput(int playerNumber) {
        Input input = mainController.createInput(playerNumber);
        input.addListeners();
        return input;
    }

    /**
     * This function checks if the last level is reached and if so, plays the boss-song.
     */
    public void setMusic() {

        if (indexCurrLvl == (maps.size() - 1)) {
            Launcher.changeMusicSong(Settings.MUSIC_BOSS_SONG);
        }

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

            if (i < scores.length) {
                newPlayer.setScore(scores[i]);
                newPlayer.setLives(lives[i] + 1);
            } else {
                newPlayer.setScore(0);
                newPlayer.setLives(Settings.PLAYER_LIVES);
            }

            players.add(newPlayer);
        }
    }

    /**
     * This function creates the next level.
     */
    public final void nextLevel() {
        indexCurrLvl++;
        refresh();
        if (indexCurrLvl < maps.size()) {
            createLvl();
        } else {
            for (Player player : players) {
                player.addHighscore();
            }
            winGame();
        }
    }

    /**
     * This function is called when it's game over.
     */
    public void gameOver() {
        Logger.log("Game over!");
        Launcher.changeMusicSong(Settings.MUSIC_GAMEOVER_SONG);
        gameLoop.stop();
        mainController.showGameOverScreen();
    }

    /**
     * This method calls the win screen when the game has been won.
     */
    private void winGame() {
        Logger.log("Game won!");
        Launcher.changeMusicSong(Settings.MUSIC_GAMEWON_SONG);
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
                Logger.log(String.format("Score: %d", p.getScore()));
                mainController.showScore(p.getScore(), p.getPlayerNumber());
                mainController.showLives(p.getLives(), p.getPlayerNumber());
        } else if (o instanceof Bubble) {
            Bubble b = (Bubble) o;
            if (b.getIsPopped()) {
                bubbles.remove(b);
            }
        } else if (o instanceof FinalEnemy) {
          mainController.showEnemyLives(((FinalEnemy) o).showLives());
        }
    }
    
    /**
     * This method return the current LevelControlerMethods.
     * @return the LevelControllerMethod.
     */
    public LevelControllerMethods getLevelControllerMethods() {
    	return levelControllerMethods;
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