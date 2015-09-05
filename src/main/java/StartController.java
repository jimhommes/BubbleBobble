import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by toinehartman on 01/09/15.
 */

public class StartController implements Initializable {

    /**
     * The help screen.
     */
    @FXML private GridPane helpScreen;

    /**
     * The @FXML annotation links the view element to this object in the controller.
     * The variable name of the object has to match the fx:id of the view element.
     */
    @FXML private AnchorPane root;

    /**
     * The ImageView is the logo that is shown.
     */
    @FXML private ImageView imageView;

    /**
     * The start button. If you press this the game will start.
     */
    @FXML private Button startButton;

    /**
     * The exit button. If you press this the application will close.
     */
    @FXML private Button exitButton;

    /**
     * The help button. If you press this you will be shown some text that should help you.
     */
    @FXML private Button helpButton;

    protected static final int num_rows = 26;
    protected static final int num_cols = 26;
    protected static Integer map[][];

    /**
     * Initializes the view.
     *
     * This is the place for setting onclick handlers, for example.
     */
    @Override
    public final void initialize(final URL location, final ResourceBundle resources) {
        startButton.setOnAction(event ->
                startLevel());
        helpButton.setOnMousePressed((event ->
                helpScreen.visibleProperty().setValue(!helpScreen.isVisible())));
        root.setOnMousePressed(event -> helpScreen.visibleProperty().setValue(false));
        exitButton.setOnAction((event ->
                System.exit(0)));
    }

    private void startLevel() {

        Stage stage = (Stage) root.getScene().getWindow();

        System.out.println(stage);

        Group newRoot = new Group();
        StackPane holder = new StackPane();
        Canvas canvas = new Canvas(416, 416);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawMap(gc);

        holder.getChildren().add(canvas);
        newRoot.getChildren().add(holder);

        holder.setStyle("-fx-background-color: black");
//        newRoot.setStyle("-fx-background-image: url('image1.jpg')");

        stage.setScene(new Scene(newRoot));
        stage.show();
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
