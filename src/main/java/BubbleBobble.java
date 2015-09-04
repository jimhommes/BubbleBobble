package nl.tudelft.BubbleBobble;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Input;
import model.Player;
import model.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BubbleBobble extends Application {

    Pane playfieldLayer;

    Image playerImage;

    List<Player> players = new ArrayList<>();

    Scene scene;

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
        Pane root = FXMLLoader.load(getClass().getResource("/sample.fxml"));

        playfieldLayer = new Pane();
        root.getChildren().add(playfieldLayer);

        primaryStage.setTitle("BubbleBobble");
        scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadGame();

        createPlayers();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                players.forEach(sprite -> sprite.processInput());

                players.forEach(sprite -> sprite.move());

                players.forEach(sprite -> sprite.updateUI());

            }
        };
        gameLoop.start();


    }

    private void loadGame() {
        playerImage = new Image(getClass().getResource("/player.png").toExternalForm());
    }

    private void createPlayers() {
        Input input = new Input(scene);

        input.addListeners();

        Image image = playerImage;

        double x = (Settings.SCENE_WIDTH - image.getWidth()) / 2.0;
        double y = Settings.SCENE_HEIGHT * 0.7;

        Player player = new Player(playfieldLayer, image, x, y, 0, 0, 0, 0, Settings.PLAYER_SPEED, input);

        players.add(player);
    }

}
