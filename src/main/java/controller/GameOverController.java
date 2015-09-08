package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class GameOverController implements Initializable{

	    /**
	     * The help screen.
	     */
	    @FXML private GridPane gameOverScreen;

	    /**
	     * The @FXML annotation links the view element to this object in the controller.
	     * The variable name of the object has to match the fx:id of the view element.
	     */
	    @FXML private AnchorPane root;

	    /**
	     * The ImageView is the logo that is shown.
	     */
	    @FXML private ImageView gameOverImage;

	    /**
	     * The start button. If you press this the game will start.
	     */
	    @FXML private Button restartButton;

	    /**
	     * The exit button. If you press this the application will close.
	     */
	    @FXML private Button quitButton;


	    /**
	     * Initializes the view.
	     *
	     * This is the place for setting onclick handlers, for example.
	     */
	    @Override
	    public final void initialize(final URL location, final ResourceBundle resources) {
	        restartButton.setOnAction(event -> {
	            try {
	            	returnToMenu();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        });
	        quitButton.setOnAction((event ->
	                System.exit(0)));
	    }

	    /**
	     * The function bound to the startbutton.
	     * @throws IOException The exception thrown.
	     */
	    private void returnToMenu() throws IOException {
	        Stage stage = (Stage) root.getScene().getWindow();
	        Parent newRoot = FXMLLoader.load(getClass().getResource("../startscreen.fxml"));
	        stage.setScene(new Scene(newRoot));
	        stage.show();
	    }
	
}
