package model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

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
    private static final int SPRITE_SIZE = 32;

    /**
     * The map in a 2 dim array.
     */
    private static Integer[][] map;
    private final Pane playfieldLayer;

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
     * When a level is created in the levelcontroller, it is immediately drawn.
     * @param lvlTitle The title of the file.
     * @param playfieldLayer The field where the play will happen.
     */
    public Level(final String lvlTitle, final Pane playfieldLayer) {
        this.lvlTitle = lvlTitle;
        this.walls = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.playfieldLayer = playfieldLayer;

        drawMap();
    }

    /**
     * The function that draws the map.
     */
    public final void drawMap() {
        Image image = new Image(getClass().getResourceAsStream("../BubbleBobbleWall32b.png"));
        readMap();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (map[row][col] == 1) {
                    walls.add(new Wall(playfieldLayer, 
                    		new Image(getClass().getResourceAsStream(Wall.WALL_SPRITE)), 
                    		col * SPRITE_SIZE, row * SPRITE_SIZE, 0, 0, 0, 0));
                } else if (map[row][col] == 2) {
                    monsters.add(new Walker(playfieldLayer, 
                    		new Image(getClass().getResourceAsStream(Walker.WALKER_IMAGE)), 
                    		col * SPRITE_SIZE - 32, row * SPRITE_SIZE - 32, 0, 0, 0, 0, 
                    		Settings.MONSTER_SPEED, true));
                } else if (map[row][col] == 3) {
                    monsters.add(new Walker(playfieldLayer, 
                    		new Image(getClass().getResourceAsStream(Walker.WALKER_IMAGE)),
                    		col * SPRITE_SIZE - 32, row * SPRITE_SIZE - 32, 0, 0, 0, 0, 
                    		Settings.MONSTER_SPEED, false));
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
                    new InputStreamReader(getClass().getResourceAsStream("../" + lvlTitle)));
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
    public ArrayList<Monster> getMonsters() {
        return monsters;
    }
}
