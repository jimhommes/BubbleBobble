package model;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */
public class Monster extends GravityObject {

    private final double speed;
    private boolean facingRight;
    private Bubble prisonBubble;
    private boolean caughtByBubble;

    public Monster(Pane layer, Image image, double x, double y, double r,
                   double dx, double dy, double dr, double speed, boolean facingRight) {
        super(layer,image,x,y,r,dx,dy,dr);

        this.speed = speed;
        this.facingRight = facingRight;
        this.caughtByBubble = false;
    }

    public void move() {
        if (!caughtByBubble) {
            if (facingRight) {
                dx = speed;
            } else {
                dx = -speed;
            }

            dy = -calculateGravity();
        } else {
            dx = 0;
            dy = 0;
            x = prisonBubble.getX();
            y = prisonBubble.getY();
        }

        super.move();
    }

    public void switchDirection() {
        facingRight = !facingRight;
    }

    public void checkCollision(final Bubble bubble) {
        Image bubbleImage = bubble.getImage();
        double bubbleX = bubble.getX();
        double bubbleY = bubble.getY();
        double bubbleX2 = bubbleX + bubbleImage.getWidth();
        double bubbleY2 = bubbleY + bubbleImage.getHeight();
        if(bubble.getAbleToCatch() && !caughtByBubble &&
                (bubbleX >= x && bubbleX <= x + image.getWidth()) ||
                (bubbleX2 >= x && bubbleX2 <= x + image.getWidth())) {
            if ((bubbleY >= y && bubbleY <= y + image.getHeight()) ||
                    bubbleY2 >= y && bubbleY2 <= y + image.getHeight()) {
                prisonBubble = bubble;
                caughtByBubble = true;
            }
        }
        bubble.setAbleToCatch(false);
    }

}
