package model;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */
public class Bubble extends SpriteBase {

    /**
     * The location of the image.
     */
    static final String BUBBLE_SPRITE = "../bubble.png";

    /**
     * The counter that is needed for the movement of the bubble.
     */
    private int counter;

    /**
     * This boolean indicates whether it is fired to the right.
     */
    private boolean firedRight;

    /**
     * Only in the first stage of firing the bubble, the bubble is able to catch monsters.
     * This boolean indicates if the bubble can catch a monster.
     */
    private boolean ableToCatch;

    /**
     * The bubble that will be shot to catch the monsters.
     * @param imageLoc The path of the image of the bubble
     * @param x The x coordinate 
     * @param y The y coordinate
     * @param r The rotation
     * @param dx The dx of x
     * @param dy The dy of y
     * @param dr The dr of r
     * @param firedRight If the bubble was fired to the right.
     */
    public Bubble(String imageLoc,
                  double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  boolean firedRight) {
        super(imageLoc, x, y, r, dx, dy, dr);

        counter = 0;
        this.firedRight = firedRight;
        this.ableToCatch = true;

    }

    /**
     * This method is used to move the bubble.
     */
    public void move() {
        if (counter < 30) {
            counter++;
            if (firedRight) {
                setDx(7);
            } else {
                setDx(-7);
            }
        } else {
            setDx(0);
            setDy(-5);
            ableToCatch = false;
        }

        super.move();
    }

    /**
     * This method sets the boolean to whether it is able to be caught.
     * @param bool set to the boolean of whether the monsters can be caught.
     */
    public void setAbleToCatch(final boolean bool) {
        ableToCatch = bool;
    }

    /**
     * This method checks to see if a monster is able to be caught.
     * @return true if the monster can be caught.
     */
    public boolean getAbleToCatch() {
        return ableToCatch;
    }
}
