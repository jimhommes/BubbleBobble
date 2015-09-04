import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class BubbleBobble extends Application {
	
	protected static final int num_rows = 26;
	protected static final int num_cols = 26;
	protected static Integer map[][];

    /**
     * The main method just launches the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method sets up the application window.
     *
     * The view is loaded from an FXML file. A title for the window is set.
     * The loaded view is set as the current scene.
     *
     * @param primaryStage The primary stage (window).
     * @throws IOException When the FXML file is not found.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
   	
    	primaryStage.setTitle("BubbleBobble");
        Group root = new Group();
        StackPane holder = new StackPane();
        Canvas canvas = new Canvas(416, 416);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        drawMap(gc);
        
        holder.getChildren().add(canvas);
        root.getChildren().add(holder);
        
        holder.setStyle("-fx-background-color: black");
        root.setStyle("-fx-background-image: url('image1.jpg')");  
        
    	primaryStage.setScene(new Scene(root));
        primaryStage.show();
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
