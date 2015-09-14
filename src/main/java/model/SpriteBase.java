package model;

/**
 * The SpriteBase that will load the sprite (image).
 */
public abstract class SpriteBase {

    /**
     * Image to be loaded.
     */
    private String imagePath;

    /**
     * The x coordinate.
     */
    private double x;

    /**
     * The y coordinate.
     */
    private double y;

    /**
     * The r coordinate.
     */
    private double r;

    /**
     * The difference in x.
     */
    private double dx;

    /**
     * The difference in y.
     */
    private double dy;

    /**
     * The difference in r.
     */
    private double dr;

    /**
     * The width.
     */
    private double w;

    /**
     * The height.
     */
    private double h;

    /**
     * The boolean that resembles if the image should be able to move or not.
     */
    private boolean canMove;

    /**
     * The boolean to check if the sprite has changed or not.
     */
    private boolean spriteChanged;

    /**
     * The constructor. It needs all the paramaters and creates the image where planned.
     *
     * @param imagePath The path to the image to load.
     * @param x         The x coordinate.
     * @param y         The y coordinate.
     * @param r         The r coordinate.
     * @param dx        The difference in x.
     * @param dy        The difference in y.
     * @param dr        The difference in r.
     */
    public SpriteBase(String imagePath, double x, double y, double r,
                      double dx, double dy, double dr) {

        this.imagePath = imagePath;
        this.x = x;
        this.y = y;
        this.r = r;
        this.dx = dx;
        this.dy = dy;
        this.dr = dr;
        this.h = 0;
        this.w = 0;
        this.canMove = true;
        this.spriteChanged = false;

    }

    /**
     * The move function that moves the image if possible.
     */
    public void move() {

        if (!canMove) {
            return;
        }
        
        x += dx;
        y += dy;
        r += dr;

    }

    /**
     * This method gets the image of the level.
     *
     * @return the image.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * This method returns the X coordinate.
     *
     * @return x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * This method return the Y coordinate.
     *
     * @return y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * This method returns the rotation degree.
     *
     * @return The rotation degree.
     */
    public double getR() {
        return r;
    }

    /**
     * This function returns the height of the SpriteBase.
     * @return The height of the SpriteBase.
     */
    public double getHeight() {
        return h;
    }

    /**
     * This method sets the height of the SpriteBase.
     *
     * @param height The height to be set
     */
    public void setHeight(double height) {
        this.h = height;
    }

    /**
     * This function returns the width of the SpriteBase.
     * @return The width of the SpriteBase.
     */
    public double getWidth() {
        return w;
    }

    /**
     * This method sets the width of the SpriteBase.
     *
     * @param width The width to be set
     */
    public void setWidth(double width) {
        this.w = width;
    }

    /**
     * This function returns whether the sprite has changed or not.
     * @return The boolean if the sprite changed
     */
    public boolean getSpriteChanged() {
        return spriteChanged;
    }

    /**
     * With this function you can set whether you have changed the sprite or not.
     * @param spriteChanged The boolean whether the sprite has changed.
     */
    public void setSpriteChanged(boolean spriteChanged) {
        this.spriteChanged = spriteChanged;
    }

    /**
     * With this function you can set another image. Because the boolean spriteChanged
     * is set to true, the screenController loads it again.
     * @param imagePath The path to the image to be loaded.
     */
    public void setImage(final String imagePath) {
        this.imagePath = imagePath;
        spriteChanged = true;
    }

    /**
     * Sets the X coordinate.
     * @param x The X coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Sets the Y coordinate.
     * @param y The Y coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Sets the R coordinate.
     * @param r The R coordinate.
     */
    public void setR(double r) {
        this.r = r;
    }

    /**
     * Sets the Dx.
     * @param dx The Dx.
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /**
     * Sets the Dy.
     * @param dy The Dy.
     */
    public void setDy(double dy) {
        this.dy = dy;
    }

    /**
     * Sets the Dr.
     * @param dr The Dr.
     */
    public void setDr(double dr) {
        this.dr = dr;
    }

    /**
     * Gets the Dx.
     * @return The Dx.
     */
    public double getDx() {
        return dx;
    }

    /**
     * Gets the Dy.
     * @return The Dy.
     */
    public double getDy() {
        return dy;
    }

    /**
     * Gets the Dr.
     * @return The Dr.
     */
    public double getDr() {
        return dr;
    }
    
    /**
     * Sets the canMove.
     * @param canMove The canMove.
     */
    public void setCanMove(Boolean canMove) {
    	this.canMove = canMove;
    }
}