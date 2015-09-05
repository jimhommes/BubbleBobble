import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
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

    /**
     * Initializes the view.
     *
     * This is the place for setting onclick handlers, for example.
     */
    @Override
    public final void initialize(final URL location, final ResourceBundle resources) {
        startButton.setOnAction(event -> {
            try {
                startLevel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        helpButton.setOnMousePressed((event ->
                helpScreen.visibleProperty().setValue(!helpScreen.isVisible())));
        root.setOnMousePressed(event -> helpScreen.visibleProperty().setValue(false));
        exitButton.setOnAction((event ->
                System.exit(0)));
    }

    private void startLevel() throws IOException {

        Stage stage = (Stage) root.getScene().getWindow();
        Parent newRoot = FXMLLoader.load(getClass().getResource("level.fxml"));

//        System.out.println(stage);
//
//        Group newRoot = new Group();
//        StackPane holder = new StackPane();
//        Canvas canvas = new Canvas(416, 416);
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//
//        drawMap(gc);
//
//        holder.getChildren().add(canvas);
//        newRoot.getChildren().add(holder);
//
//        holder.setStyle("-fx-background-color: black");
////        newRoot.setStyle("-fx-background-image: url('image1.jpg')");

        stage.setScene(new Scene(newRoot));
        stage.show();
    }

}
