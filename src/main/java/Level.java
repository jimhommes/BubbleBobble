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

    protected static final int num_rows = 26;
    protected static final int num_cols = 26;
    protected static Integer map[][];

    private String lvlTitle;
    private ArrayList<Wall> walls;

    public Level(final String lvlTitle, final Canvas canvas) {
        this.lvlTitle = lvlTitle;
        this.walls = new ArrayList<>();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc);
    }

    public void drawMap(GraphicsContext gc){
        Image image = new Image(getClass().getResourceAsStream("BubbleBobbleWall32b.png"));
        readMap();
        for(int row = 0; row < num_rows; row++) {
            for(int col = 0; col < num_cols; col++) {
                if(map[row][col] == 1){
                    walls.add(new Wall(col*32, row*32));
                    gc.drawImage(image, col*32, row*32);
                }
            }
        }
    }

    public void readMap(){
        int row = 0;
        map = new Integer[num_rows][num_cols];

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(lvlTitle)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(" ");
                if(cols.length == num_cols) {
                    for(int colum = 0; colum < cols.length; colum++) {
                        map[row][colum] = Integer.parseInt(cols[colum]);
                    }
                }
                row++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
