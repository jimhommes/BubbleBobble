package model.support;

import controller.LevelController;
import model.gameobject.level.Wall;

import java.util.Observable;

/**
 * This class will load the sprites (image).
 */
public class SpriteBase extends Observable {

    private String imagePath;
    private Coordinates coordinates;
    private double w;
    private double h;
    private boolean canMove;
    private boolean spriteChanged;
    
    /**
     * The constructor. It needs all the parameters and creates the image where planned.
     *
     * @param imagePath The path to the image to load.
     * @param  coordinates The coordinates of the Sprite.
     */
    public SpriteBase(String imagePath, Coordinates coordinates) {

        this.imagePath = imagePath;
        this.coordinates = coordinates;
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

        this.coordinates.apply();
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
    public double getXCoordinate() {
        return coordinates.getXCoordinate();
    }

    /**
     * This method return the Y coordinate.
     *
     * @return y coordinate.
     */
    public double getYCoordinate() {
        return coordinates.getYCoordinate();
    }

    /**
     * This method returns the rotation degree.
     *
     * @return The rotation degree.
     */
    public double getRotation() {
        return coordinates.getRotation();
    }

    /**
     * This function returns the height of the SpriteBase.
     * @return The height of the SpriteBase.i
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
     * This function checks if there is a collision with a set of coordinates.
     * @param minX The minimal X.
     * @param maxX The maximal X.
     * @param minY The minimal Y.
     * @param maxY The maximal Y.
     * @return True if there is a collision.
     */
    public boolean causesCollision(double minX, double maxX, double minY, double maxY) {
        double minX2 = coordinates.getXCoordinate();
        double maxX2 = minX2 + getWidth();
        double minY2 = coordinates.getYCoordinate();
        double maxY2 = minY2 + getHeight();
        return ((minX > minX2 && minX < maxX2)
                || (maxX > minX2 && maxX < maxX2)
                || (minX2 > minX && minX2 < maxX)
                || (maxX2 > minX && maxX2 < maxX))
                && ((minY > minY2 && minY < maxY2)
                || (maxY > minY2 && maxY < maxY2)
                || (minY2 > minY && minY2 < maxY)
                || (maxY2 > minY && maxY2 < maxY));
    }

    /**
     * Sets the X coordinate.
     * @param x The X coordinate.
     */
    public void setXCoordinate(double x) {
        coordinates.setXCoordinate(x);
    }

    /**
     * Sets the Y coordinate.
     * @param y The Y coordinate.
     */
    public void setYCoordinate(double y) {
    	coordinates.setYCoordinate(y);
    }

    /**
     * Sets the R coordinate.
     * @param r The R coordinate.
     */
    public void setRotation(double r) {
    	coordinates.setRotation(r);
    }

    /**
     * Sets the Dx.
     * @param dx The Dx.
     */
    public void setDxCoordinate(double dx) {
        coordinates.setDXCoordinate(dx);
    }

    /**
     * Sets the Dy.
     * @param dy The Dy.
     */
    public void setDyCoordinate(double dy) {
        coordinates.setDYCoordinate(dy);
    }

    /**
     * Sets the Dr.
     * @param dr The Dr.
     */
    public void setDRotation(double dr) {
       coordinates.setDRotation(dr);
    }

    /**
     * Gets the Dx.
     * @return The Dx.
     */
    public double getDxCoordinate() {
        return coordinates.getDXCoordinate();
    }

    /**
     * Gets the Dy.
     * @return The Dy.
     */
    public double getDyCoordinate() {
        return coordinates.getDYCoordinate();
    }

    /**
     * Gets the Dr.
     * @return The Dr.
     */
    public double getDRotation() {
        return coordinates.getDRotation();
    }
    
    /**
     * Sets the canMove.
     * @param canMove The canMove.
     */
    public void setCanMove(Boolean canMove) {
    	this.canMove = canMove;
    }

    /**
     * This function returns the player if it is out of bounds.
     * @param spriteMinX The minimal X coordinate a sprite can have.
     * @param spriteMaxX The maximal X coordinate a sprite can have.
     * @param spriteMinY The minimal Y coordinate a sprite can have.
     * @param spriteMaxY The maximal Y coordinate a sprite can have.
     * @param levelController The levelController.
     */
    public void checkBounds(double spriteMinX, double spriteMaxX,
                            double spriteMinY, double spriteMaxY,
                            LevelController levelController) {
        if (getXCoordinate() < spriteMinX) {
            setXCoordinate(spriteMinX);
        } else if (getXCoordinate() + getWidth() > spriteMaxX) {
            setXCoordinate(spriteMaxX - getWidth());
        }

        if (getYCoordinate() < spriteMinY) {
        	if (!causesCollisionWall(getXCoordinate(),
                    getXCoordinate() + getWidth(),
                    getYCoordinate(),
                    getYCoordinate() + getHeight(), levelController)) {
        		setYCoordinate(spriteMaxY - getHeight());
        	} else {
        		setYCoordinate(spriteMinY);
        	}	
        } else if (getYCoordinate() + getHeight() > spriteMaxY) {
            setYCoordinate(spriteMinY);
        }
    }
    
    /**
     * This method check if there is a collision between SpriteBase and Wall.
     * @param minX minimal x coordinate.
     * @param maxX maximal x coordinate.
     * @param minY minimal y coordinate.
     * @param maxY maximal y coordinate.
     * @param levelController the LevelController.
     * @return true if there is a collision.
     */
    public boolean causesCollisionWall(double minX, double maxX, double minY, 
			double maxY, LevelController levelController) {

        for (Wall wall : levelController.getCurrLvl().getWalls()) {
            if (wall.getSpriteBase().causesCollision(minX, maxX, minY, maxY)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * This method gets the current location and speed from the SpirteBase.
     * @return the location.
     */
    public double[] getLocation() {
      double[] location = new double[4];
      location[0] = getXCoordinate();
      location[1] = getDxCoordinate();
      location[2] = getYCoordinate();
      location[3] = getDyCoordinate();
      return location;
    }

    /**
     * After movement sets this method the new location.
     * @param location the new location.
     */
    public void setLocation(double[] location) {
      setXCoordinate(location[0]);
      setDxCoordinate(location[1]);
      setYCoordinate(location[2]);
      setDyCoordinate(location[3]);
    }
    

}