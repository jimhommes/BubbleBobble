import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by toinehartman on 01/09/15.
 */

public class SampleController {
    /**
     * The @FXML annotation links the view element to this object in the controller.
     * The variable name of the object has to match the fx:id of the view element.
     */
    @FXML private Button dontPressButton;

    /**
     * Initializes the view.
     *
     * This is the place for setting onclick handlers, for example.
     */
    public void initialize() {
        dontPressButton.setOnAction((event) -> {
            System.out.println("You should not have pressed this!");
        });
    }

    /**
     * This method is run when the view is shown.
     *
     * This is where data can be put in tables, for example.
     */
    public void show() {
    }
}
