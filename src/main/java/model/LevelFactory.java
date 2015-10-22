package model;

import controller.LevelController;
import utility.Logger;
import utility.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Jim on 10/22/2015.
 *
 * @author Jim
 * @version 1.0
 * @since 10/22/2015
 */
public class LevelFactory {

    private static final int NUM_ROWS = 26;
    private static final int NUM_COLS = 26;

    /**
     * The map in a 2 dim array.
     */
    private Integer[][] map;

    private final LevelController levelController;

    public LevelFactory(final LevelController levelController) {
        this.levelController = levelController;
    }

    public Level makeLevel(String levelTitle, int limitOfPlayers) {
        Level level = new Level();
        readMap(levelTitle);
        drawMap(level, limitOfPlayers);
        return level;
    }

    /**
     * The function that draws the map.
     */
    public final void drawMap(Level level, int limitOfPlayers) {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Coordinates coordinatesWalker = new Coordinates(col * Settings.SPRITE_SIZE - 32,
                        row * Settings.SPRITE_SIZE - 32, 0, 0, 0, 0);
                if (map[row][col] == 1) {
                    Coordinates coordinatesWall =
                            new Coordinates(col * Settings.SPRITE_SIZE / 2, row * Settings.SPRITE_SIZE / 2, 0, 0, 0, 0);
                    level.addWall(new Wall(coordinatesWall));
                } else if (map[row][col] == 2) {
                    level.addMonster(new Walker(coordinatesWalker,
                            Settings.MONSTER_SPEED, true, levelController));
                } else if (map[row][col] == 3) {
                    level.addMonster(new Walker(coordinatesWalker,
                            Settings.MONSTER_SPEED, false, levelController));
                } else if (map[row][col] == 9) {
                    Logger.log(String.format("Player found in %d, %d%n", row, col));
                    int playerCounter = level.getPlayers().size();
                    if (playerCounter < limitOfPlayers) {
                        Coordinates coordinatesPlayer = new Coordinates(col * Settings.SPRITE_SIZE / 2
                                - Settings.SPRITE_SIZE / 2,
                                row * Settings.SPRITE_SIZE / 2 - Settings.SPRITE_SIZE / 2, 0, 0, 0, 0);
                        level.addPlayer(new Player(levelController, coordinatesPlayer,
                                Settings.PLAYER_SPEED, Settings.PLAYER_LIVES,
                                levelController.createInput(playerCounter + 1), playerCounter + 1));
                    }
                }
            }
        }
    }

    /**
     * This function reads the file and translates it to a 2dim array.
     */
    private void readMap(String levelTitle) {
        int row = 0;
        map = new Integer[NUM_ROWS][NUM_COLS];
        BufferedReader reader;

        try (InputStreamReader isr = new InputStreamReader(getClass()
                .getClassLoader()
                .getResourceAsStream(levelTitle), "UTF-8")) {
            reader = new BufferedReader(isr);

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
            reader.close();
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
