package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * The SpriteBase that will load the sprite (image).
 */
public abstract class SpriteBase {

    /**
     * Image to be loaded.
     */
    public Image image;

    /**
     * ImageView to view the Image.
     */
    public ImageView imageView;

    /**
     * The layer the image will be displayed in.
     */
    public Pane layer;

    /**
     * The x coordinate.
     */
    public double x;

    /**
     * The y coordinate.
     */
    public double y;

    /**
     * The r coordinate.
     */
    public double r;

    /**
     * The difference in x.
     */
    public double dx;

    /**
     * The difference in y.
     */
    public double dy;

    /**
     * The difference in r.
     */
    public double dr;

    /**
     * The width.
     */
    public double w;

    /**
     * The height.
     */
    public double h;

    /**
     * The boolean that resembles if the image should be able to move or not.
     */
    public boolean canMove = true;

    /**
     * The constructor. It needs all the paramaters and creates the image where planned.
     * @param layer The layer the image moves in.
     * @param image The image to load.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param r The r coordinate.
     * @param dx The difference in x.
     * @param dy The difference in y.
     * @param dr The difference in r.
     */
    public SpriteBase(Pane layer, Image image, double x, double y, double r,
                      double dx, double dy, double dr) {

        this.layer = layer;
        this.image = image;
        this.x = x;
        this.y = y;
        this.r = r;
        this.dx = dx;
        this.dy = dy;
        this.dr = dr;

        this.imageView = new ImageView(image);
        this.imageView.relocate(x, y);
        this.imageView.setRotate(r);

        this.w = image.getWidth();
        this.h = image.getHeight();

        addToLayer();

    }

    /**
     * The function that adds the image view to the layer.
     */
    public void addToLayer() {
        this.layer.getChildren().add(this.imageView);
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
     * The function to update the UI.
     */
    public void updateUI() {

        imageView.setImage(image);
        imageView.relocate(x, y);
        imageView.setRotate(r);

    }

}