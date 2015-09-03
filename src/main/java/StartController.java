import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
        startButton.setOnAction(event ->
                root.visibleProperty().setValue(false));
        helpButton.setOnMousePressed((event ->
                helpScreen.visibleProperty().setValue(!helpScreen.isVisible())));
        root.setOnMousePressed(event -> helpScreen.visibleProperty().setValue(false));
        exitButton.setOnAction((event ->
                System.exit(0)));
    }

    /**
     * Gets the help screen gridpane.
     * @return The help screen
     */
    public GridPane getHelpScreen() {
        return helpScreen;
    }

    /**
     * Gets the Anchorpane stage.
     * @return The stage
     */
    public AnchorPane getRoot() {
        return root;
    }

    /**
     * Gets the image of the logo
     * @return The logo view
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Gets the start button
     * @return Start button
     */
    public Button getStartButton() {
        return startButton;
    }

    /**
     * Returns the Exit Button
     * @return The Exit Button
     */
    public Button getExitButton() {
        return exitButton;
    }

    /**
     * Returns the help button
     * @return The Help Button
     */
    public Button getHelpButton() {
        return helpButton;
    }

    public void setHelpScreen(GridPane helpScreen) {
        this.helpScreen = helpScreen;
    }



}
