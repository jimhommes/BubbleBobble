package model;

/**
 * Created by Jim on 10/7/2015.
 *
 * @author Jim
 * @version 1.0
 * @since 10/7/2015
 */
public class Powerup extends SpriteBase {

    private double destx;
    private double desty;
    private boolean ableToPickup;

    /**
     * The constructor. It needs all the parameters and creates the image where planned.
     *
     * @param x         The x coordinate.
     * @param y         The y coordinate.
     * @param r         The r coordinate.
     * @param dx        The difference in x.
     * @param dy        The difference in y.
     * @param dr        The difference in r.
     */
    public Powerup(double x, double y, double r, double dx, double dy, double dr,
                   double destx, double desty) {
        super("../banana.gif", x, y, r, dx, dy, dr);
        this.ableToPickup = false;
        this.destx = destx;
        this.desty = desty;
    }

    @Override
    public void move() {
        double diffX = destx - getX();
        double diffY = desty - getY();
        if (diffX < 0.01 && diffY < 0.01) {
            setDx(0);
            setDy(0);
            ableToPickup = true;
        } else {
            setDx(diffX / 20.0);
            setDy(diffY / 20.0);
        }

        super.move();
    }

}
