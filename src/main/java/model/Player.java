package model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Player extends SpriteBase {

    double playerMinX;
    double playerMaxX;
    double playerMinY;
    double playerMaxY;

    Input input;

    double speed;

    public Player(Pane layer, Image image, double x, double y, double r, double dx, double dy, double dr, double speed, Input input) {

        super(layer, image, x, y, r, dx, dy, dr);

        this.speed = speed;
        this.input = input;

        init();
    }


    private void init() {

        // calculate movement bounds of the player
        // allow half of the player to be outside of the screen
        playerMinX = 0 - image.getWidth() / 2.0;
        playerMaxX = Settings.SCENE_WIDTH - image.getWidth() / 2.0;
        playerMinY = 0 - image.getHeight() / 2.0;
        playerMaxY = Settings.SCENE_HEIGHT -image.getHeight() / 2.0;

    }

    public void processInput() {

        // ------------------------------------
        // movement
        // ------------------------------------

        // vertical direction
        if( input.isMoveUp()) {
            dy = -speed;
            image = new Image(getClass().getResource("/playerUp.png").toExternalForm());
        } else if( input.isMoveDown()) {
            dy = speed;
            image = new Image(getClass().getResource("/playerDown.png").toExternalForm());
        } else {
            dy = 0d;
        }

        // horizontal direction
        if( input.isMoveLeft()) {
            dx = -speed;
            image = new Image(getClass().getResource("/playerLeft.png").toExternalForm());
        } else if( input.isMoveRight()) {
            dx = speed;
            image = new Image(getClass().getResource("/playerRight.png").toExternalForm());
        } else {
            dx = 0d;
        }

    }

    @Override
    public void move() {

        super.move();

        checkBounds();


    }

    private void checkBounds() {

        // vertical
        if( Double.compare( y, playerMinY) < 0) {
            y = playerMinY;
        } else if( Double.compare(y, playerMaxY) > 0) {
            y = playerMaxY;
        }

        // horizontal
        if( Double.compare( x, playerMinX) < 0) {
            x = playerMinX;
        } else if( Double.compare(x, playerMaxX) > 0) {
            x = playerMaxX;
        }

    }




}