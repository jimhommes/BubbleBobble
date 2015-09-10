package model;

import controller.LevelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

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
     * The bubbles the player fired.
     */
    private ArrayList<Bubble> bubbles;

    /**
     * The boolean for which dir the player is facing.
     */
    private boolean facingRight;

    private int counter;

    private boolean isDead;
    
    private boolean gameOver;

    private LevelController levelController;

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
                  Input input,
                  LevelController levelController) {

        super(layer, image, x, y, r, dx, dy, dr);

        this.speed = speed;
        this.input = input;
        this.bubbles = new ArrayList<>();
        this.counter = 16;
        this.isDead = false;
        this.gameOver = false;
        this.levelController = levelController;

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

    	if (!isDead) {
    		// vertical direction
            // If isMoveUp AND does not cause a collision
    		if (input.isMoveUp()) {
                if (!levelController.causesCollision(x, x + image.getWidth(), y - speed, y + image.getHeight())) {
                    dy = -speed;
                } else {
                    dy = 0;
                }

    			if (facingRight) {
    				image = new Image(getClass().getResource("/BubRight.png").toExternalForm());
    			} else {
    				image = new Image(getClass().getResource("/BubLeft.png").toExternalForm());
    			}  
    		} else if (input.isMoveDown()) {

                if (!levelController.causesCollision(x, x + image.getWidth(), y + speed, y + image.getHeight())) {
                    dy = speed;
                } else {
                    dy = 0;
                }

    			if (facingRight) {
    				image = new Image(getClass().getResource("/BubRight.png").toExternalForm());
    			} else {
    				image = new Image(getClass().getResource("/BubLeft.png").toExternalForm());
    			}
    		} else {
    			dy = 0d;
    		}

            // horizontal direction
            if (input.isMoveLeft()) {
                if (!levelController.causesCollision(x - speed, x + image.getWidth(), y, y + image.getHeight())) {
                    dx = -speed;
                } else {
                    dx = 0;
                }

                image = new Image(getClass().getResource("/BubLeft.png").toExternalForm());
                facingRight = false;
            } else if (input.isMoveRight()) {
                if (!levelController.causesCollision(x + speed, x + image.getWidth(), y, y + image.getHeight())) {
                    dx = speed;
                } else {
                    dx = 0;
                }

                image = new Image(getClass().getResource("/BubRight.png").toExternalForm());
                facingRight = true;
            } else {
                dx = 0d;
            }

            if (input.isFirePrimaryWeapon() && counter > 30) {
                bubbles.add(new Bubble(layer,
                        new Image(getClass().getResource(Bubble.BUBBLE_SPRITE).toExternalForm()),
                        x, y, 0, 0, 0, 0, facingRight));
                counter = 0;
            } else {
                counter++;
            }
        } else {
            if (counter > 50) {
                gameOver = true;
                Stage stage = (Stage) layer.getScene().getWindow();
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("../gameOver.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                counter++;
            }
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

    /**
     * This function returns the bubble list.
     * @return the bubbles.
     */
    public ArrayList<Bubble> getBubbles() {
        return bubbles;
    }

    /**
     * This method checks if the monster has collides with the character.
     * @param monster is the monster that is being checked for collisions.
     */
    public void checkCollideMonster(final Monster monster) {
        double monsterX = monster.getX();
        double monsterMaxX = monsterX + monster.getImage().getWidth();
        double monsterY = monster.getY();
        double monsterMaxY = monsterY + monster.getImage().getHeight();

        if ((monsterX > x && monsterX < x + image.getWidth())
        		|| (monsterMaxX > x && monsterMaxX < x + image.getWidth())) {
            if ((monsterY > y && monsterY < y + image.getHeight()) 
            		|| (monsterMaxY > y && monsterMaxX < y + image.getHeight())) {
                die();
            }
        }
    }

    /**
     * This method is used when the character is killed.
     */
    public void die() {
        this.isDead = true;
        counter = 0;
        image = new Image(getClass().getResource("/BubbleBobbleLogo.png").toExternalForm());
    }


    /**
     * This method checks if the character is dead or not.
     * @return isDead when the character is dead.
     */
    public boolean getDead() {
        return isDead;
    }

    /**
     * This method  checks if there is a game over.
     * @return gameOver if the game is over.
     */
    public boolean getGameOver() {
        return gameOver;
    }
}