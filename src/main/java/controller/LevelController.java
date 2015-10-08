package controller;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import utility.Logger;
import model.Bubble;
import model.Input;
import model.Level;
import model.Player;
import model.SpriteBase;
import model.Monster;
import model.Wall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
public class LevelController extends Observer {

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
     * "Key Pressed" handler for pausing the game: register in boolean gamePaused.
     */
    private EventHandler<KeyEvent> pauseKeyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            // pause game on key press PAUSE_KEY
            if (event.getCode() == PAUSE_KEY) {
                mainController.showPauseScreen();
                gamePaused = true;
            }

            //un-pause game on key press anything except PAUSE_KEY
            if (gamePaused && event.getCode() != PAUSE_KEY) {
                mainController.hidePauseScreen();
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
                        player.checkBubbles();
                        player.getBubbles().forEach(Bubble::move);
                    });
                    ((ArrayList<Monster>) currLvl.getMonsters()).forEach(monster -> {
                        ((ArrayList<Player>) players).forEach(player -> {
                            player.getBubbles().forEach(monster::checkCollision);
                            player.checkCollideMonster(monster);
                        });
                        monster.move();
                    });
                    //screenController.updateUI();
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

            Pane playFieldLayer = mainController.getPlayFieldLayer();

            playFieldLayer.setOnMousePressed(event -> {
                if (!gameStarted) {
                    gameStarted = true;
                    createInput();

                    createLvl();

                    mainController.hideStartMessage();
                    playFieldLayer.addEventFilter(
                            KeyEvent.KEY_PRESSED, pauseKeyEventHandler);
                    gameLoop.start();
                }
            });
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
        ArrayList<Monster> monsters = currLvl.getMonsters();
    }

    private void createInput() {
        if (input == null) {
            input = new Input(mainController.getPlayFieldLayer().getScene());
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
    
	@Override
	public void update(SpriteBase sprite) {
				
	}

	@Override
	public void update(SpriteBase spriteBase, int state) {
		if (state == 1 && (spriteBase instanceof Player)) {
			gameOver();
		}
		
	}
}
