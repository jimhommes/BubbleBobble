import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Jim on 9/5/2015.
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

    /**
     * The title of the file that is loaded.
     */
    private String lvlTitle;

    /**
     * The list of walls that define the map.
     */
    private ArrayList<Wall> walls;

    /**
     * When a level is created in the levelcontroller, it is immediately drawn.
     * @param lvlTitle The title of the file.
     * @param canvas The canvas the level should be drawn in.
     */
    public Level(final String lvlTitle, final Canvas canvas) {
        this.lvlTitle = lvlTitle;
        this.walls = new ArrayList<>();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc);
    }

    /**
     * The function that draws the map.
     * @param gc The GraphicsContext it will use.
     */
    public final void drawMap(final GraphicsContext gc) {
        Image image = new Image(getClass().getResourceAsStream("BubbleBobbleWall32b.png"));
        readMap();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (map[row][col] == 1) {
                    walls.add(new Wall(col * SPRITE_SIZE, row * SPRITE_SIZE));
                    gc.drawImage(image, col * SPRITE_SIZE, row * SPRITE_SIZE);
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
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(lvlTitle)));
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
}
