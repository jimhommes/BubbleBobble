package model;

import controller.LevelController;
import utility.Logger;
import utility.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This class creates the levels for the game. When created it can load a
 * level from a .txt file.
 */
public class Level {

    private static final int NUM_ROWS = 26;
    private static final int NUM_COLS = 26;
    public static final double SPRITE_SIZE = 32.0;

    private final LevelController levelController;
    private final int limitOfPlayers;

    /**
     * The map in a 2 dim array.
     */
    private Integer[][] map;

    /**
     * The title of the file that is loaded.
     */
    private String lvlTitle;

    /**
     * The list of walls that define the map.
     */
    private ArrayList<Wall> walls;

    /**
     * The list of the monsters that spawn.
     */
    private ArrayList<Monster> monsters;

    /**
     * The list of the monsters that spawn.
     */
    private ArrayList<Player> players;
    
    private int counter;

    private int playerCounter;

    /**
     * When a level is created in the levelController, it is immediately drawn.
     * @param lvlTitle The title of the file.
     * @param levelController the controller that controls the level.
     * @param limitOfPlayers The limit of players allowed by the game.
     */
    public Level(final String lvlTitle, 
    		final LevelController levelController,
                 final int limitOfPlayers) {
        this.lvlTitle = lvlTitle;
        this.walls = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.players = new ArrayList<>();
        this.levelController = levelController;
        this.limitOfPlayers = limitOfPlayers;
        this.playerCounter = 1;
        drawMap();
    }

    /**
     * The function that draws the map.
     */
    public final void drawMap() {
        readMap();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
            	Coordinates coordinatesWalker = new Coordinates(col * SPRITE_SIZE - 32,
                        row * SPRITE_SIZE - 32, 0, 0, 0, 0);
                if (map[row][col] == 1) {
                	Coordinates coordinatesWall = 
                			new Coordinates(col * SPRITE_SIZE, row * SPRITE_SIZE, 0, 0, 0, 0);
                    walls.add(new Wall(coordinatesWall));
                } else if (map[row][col] == 2) {
                    monsters.add(new Walker(coordinatesWalker,
                            Settings.MONSTER_SPEED, true, levelController));
                } else if (map[row][col] == 3) {
                    monsters.add(new Walker(coordinatesWalker,
                            Settings.MONSTER_SPEED, false, levelController));
                } else if (map[row][col] == 9) {
                    Logger.log(String.format("Player found in %d, %d%n", row, col));
                    if (players.size() < limitOfPlayers) {
                    	 Coordinates coordinatesPlayer = new Coordinates(col * SPRITE_SIZE - 32,
                                 row * SPRITE_SIZE - 32, 0, 0, 0, 0);
                        players.add(new Player(levelController, coordinatesPlayer,
                                Settings.PLAYER_SPEED, Settings.PLAYER_LIVES, null, playerCounter));
                        playerCounter++;
                    }
                }
            }
        }
    }

    /**
     * This function reads the file and translates it to a 2dim array.
     */
    private void readMap() {
        int row = 0;
        map = new Integer[NUM_ROWS][NUM_COLS];
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                  new InputStreamReader(getClass()
                          .getClassLoader()
                          .getResourceAsStream(lvlTitle), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(" ");
                if (cols.length == NUM_COLS) {
                    for (int column = 0; column < cols.length; column++) {
                        map[row][column] = Integer.parseInt(cols[column]);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * The function that returns the arrayList of monsters.
     * @return The arrayList of monsters.
     */
    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    /**
     * This method gets the players in the game.
     * @return The players in the game.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * This method gets the walls in the game.
     * @return The walls in the game.
     */
    public ArrayList<Wall> getWalls() {
        return walls;
    }

    /**
     * This method updates the monster list, to see if all the monsters have died.
     * @return true is the monster list is empty.
     */
    public boolean update() {
        ArrayList<Monster> newMonsters = new ArrayList<>();
        monsters.forEach(monster -> {
            if (!monster.isDead()) {
                newMonsters.add(monster);
            }
        });
        monsters = newMonsters;
        
        if (monsters.size() == 0) {
        	if (counter < 200) {
        		counter++;
        		return false;
        	}
        	else {
        		return true;
        	}
        }
        else {
        	return false;
        }
    }
}
