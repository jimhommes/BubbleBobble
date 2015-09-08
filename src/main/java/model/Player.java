package model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * This is the player class. It has a sprite to display.
 */
public class Player extends GravityObject {

    /**
     * This is the minimal X coordinate.
     */
    private double playerMinX;

    /**
     * This is the maximal X coordinate.
     */
    private double playerMaxX;

    /**
     * This is the minimal Y coordinate.
     */
    private double playerMinY;

    /**
     * This is the maximal Y coordinate.
     */
    private double playerMaxY;

    /**
     * The input that defines the movement of the player.
     */
    private Input input;

    /**
     * The speed of the player.
     */
    private double speed;

    /**
     * The constructor that takes all parameters and creates a SpriteBase.
     * @param layer The layer the player moves in.
     * @param image The image the player takes.
     * @param x The start x coordinate.
     * @param y The start y coordinate.
     * @param r The r.
     * @param dx The dx.
     * @param dy The dy.
     * @param dr The dr.
     * @param speed The speed of the player.
     * @param input The input the player will use.
     */
    public Player(Pane layer,
                  Image image,
                  double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  double speed,
                  Input input) {

        super(layer, image, x, y, r, dx, dy, dr);

        this.speed = speed;
        this.input = input;

        init();
    }

    /**
     * The function that initiates the player.
     */
    private void init() {

        // calculate movement bounds of the player
        // allow half of the player to be outside of the screen
        playerMinX = 0 - image.getWidth() / 2.0;
        playerMaxX = Settings.SCENE_WIDTH - image.getWidth() / 2.0;
        playerMinY = 0 - image.getHeight() / 2.0;
        playerMaxY = Settings.SCENE_HEIGHT - image.getHeight() / 2.0;

    }

    /**
     * The function that processes the input.
     */
    public void processInput() {

        // ------------------------------------
        // movement
        // ------------------------------------

        // vertical direction
        if (input.isMoveUp()) {
            dy = -speed;
            image = new Image(getClass().getResource("/playerUp.png").toExternalForm());
        } else if (input.isMoveDown()) {
            dy = speed;
            image = new Image(getClass().getResource("/playerDown.png").toExternalForm());
        } else {
            dy = 0d;
        }

        // horizontal direction
        if (input.isMoveLeft()) {
            dx = -speed;
            image = new Image(getClass().getResource("/playerLeft.png").toExternalForm());
        } else if (input.isMoveRight()) {
            dx = speed;
            image = new Image(getClass().getResource("/playerRight.png").toExternalForm());
        } else {
            dx = 0d;
        }

    }

    /**
     * The move function that applies the movement to the player.
     */
    @Override
    public void move() {

        // IF NOT COLLIDING
        this.y -= calculateGravity();

        super.move();

        checkBounds();


    }

    /**
     * The function that checks wether the player is still within the bounds where it is allowed.
     * If it is not, move the player back in the bounds.
     */
    private void checkBounds() {

        // vertical
        if (Double.compare(y, playerMinY) < 0) {
            y = playerMinY;
        } else if (Double.compare(y, playerMaxY) > 0) {
            y = playerMaxY;
        }

        // horizontal
        if (Double.compare(x, playerMinX) < 0) {
            x = playerMinX;
        } else if (Double.compare(x, playerMaxX) > 0) {
            x = playerMaxX;
        }

    }


}