package model;

import controller.LevelController;
import utility.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Jim
 * @since 9/5/2015
 * @version 0.1
 */
public class Level {

    /**
     * The number of rows.
     */
    protected static final int NUM_ROWS = 26;

    /**
     * Number of columns.
     */
    protected static final int NUM_COLS = 26;

    /**
     * The size in pixels of a sprite.
     */
    public static final double SPRITE_SIZE = 32.0;

    /**
     * The controller of this class.
     */
    private final LevelController levelController;

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

    /**
     * When a level is created in the levelcontroller, it is immediately drawn.
     * @param lvlTitle The title of the file.
     * @param levelController the controller that controls the level.
     */
    public Level(final String lvlTitle, 
    		final LevelController levelController) {
        this.lvlTitle = lvlTitle;
        this.walls = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.players = new ArrayList<>();
        this.levelController = levelController;
        drawMap();
    }

    /**
     * The function that draws the map.
     */
    public final void drawMap() {
        readMap();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (map[row][col] == 1) {
                    walls.add(new Wall(col * SPRITE_SIZE, row * SPRITE_SIZE, 0, 0, 0, 0));
                } else if (map[row][col] == 2) {
                    monsters.add(new Walker(col * SPRITE_SIZE - 32,
                            row * SPRITE_SIZE - 32, 0, 0, 0, 0,
                            Settings.MONSTER_SPEED, true, levelController));
                } else if (map[row][col] == 3) {
                    monsters.add(new Walker(col * SPRITE_SIZE - 32,
                            row * SPRITE_SIZE - 32, 0, 0, 0, 0,
                            Settings.MONSTER_SPEED, false, levelController));
                } else if (map[row][col] == 9) {
                    System.out.format("Player found in %d, %d\n", row, col);
                    players.add(new Player(col * SPRITE_SIZE - 32,
                            row * SPRITE_SIZE - 32, 0, 0, 0, 0,
                            Settings.PLAYER_SPEED, null, levelController));
                }
            }
        }
    }

    /**
     * This function reads the file and translates it to a 2dim array.
     */
    public final void readMap() {
        int row = 0;
        map = new Integer[NUM_ROWS][NUM_COLS];
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                  new InputStreamReader(getClass().getResourceAsStream("../" + lvlTitle), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(" ");
                if (cols.length == NUM_COLS) {
                    for (int colum = 0; colum < cols.length; colum++) {
                        map[row][colum] = Integer.parseInt(cols[colum]);
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
     * The function that returns the arraylist of monsters.
     * @return The arraylist of monsters.
     */
    @SuppressWarnings("rawtypes")
    public ArrayList getMonsters() {
        return monsters;
    }

    /**
     * This method gets the players in the game.
     * @return The players in the game.
     */
    @SuppressWarnings("rawtypes")
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * This method gets the walls in the game.
     * @return The walls in the game.
     */
    @SuppressWarnings("rawtypes")
    public ArrayList getWalls() {
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

        return monsters.size() == 0;
    }
}
