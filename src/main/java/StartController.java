import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by toinehartman on 01/09/15.
 */

public class StartController implements Initializable {
    /**
     * The @FXML annotation links the view element to this object in the controller.
     * The variable name of the object has to match the fx:id of the view element.
     */
    @FXML private ImageView imageView;
    @FXML private Button startButton;
    @FXML private Button exitButton;
    @FXML private Button helpButton;

    /**
     * Initializes the view.
     *
     * This is the place for setting onclick handlers, for example.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startButton.setOnAction((event -> {
            System.out.println("Start!");
        }));
        helpButton.setOnAction((event -> {
            System.out.println("Help!");
        }));
        exitButton.setOnAction((event -> {
            System.out.println("Exit!");
        }));
    }

    /**
     * This method is run when the view is shown.
     *
     * This is where data can be put in tables, for example.
     */
    public void show() {
    }

}
