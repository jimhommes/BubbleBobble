import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jim on 9/5/2015.
 */
public class LevelController implements Initializable {

    @FXML
    StackPane root;
    @FXML
    Canvas canvas;

    protected static final int num_rows = 26;
    protected static final int num_cols = 26;
    protected static Integer map[][];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMap(gc);
    }

    public void drawMap(GraphicsContext gc){
        Image image = new Image(getClass().getResourceAsStream("BubbleBobbleWall.png"));
        //gc.drawImage(image, 0, 0);
        readMap();
        System.out.println(map[0][0]);
        for(int row = 0; row < num_rows; row++) {
            for(int col = 0; col < num_cols; col++) {
                if(map[row][col] == 1){
                    gc.drawImage(image, col*16, row*16);
                }
            }
        }
    }

    public void readMap(){
        int row = 0;
        map = new Integer[num_rows][num_cols];

        BufferedReader reader = null;


        try {
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("map1.txt")));
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
