package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public abstract class SpriteBase {

    Image image;
    ImageView imageView;

    Pane layer;

    double x;
    double y;
    double r;

    double dx;
    double dy;
    double dr;

    double w;
    double h;

    boolean canMove = true;

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

    public void addToLayer() {
        this.layer.getChildren().add(this.imageView);
    }


    public void move() {

        if( !canMove)
            return;

        x += dx;
        y += dy;
        r += dr;

    }

    public void updateUI() {

        imageView.setImage(image);
        imageView.relocate(x, y);
        imageView.setRotate(r);

    }

}