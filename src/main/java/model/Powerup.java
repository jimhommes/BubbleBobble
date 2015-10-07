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
        this.destx = destx;
        this.desty = desty;
    }

    @Override
    public void move() {
        setDx(Math.abs(getX() - destx) / 20.0);
        setDx(Math.abs(getY() - desty) / 20.0);
    }

}
