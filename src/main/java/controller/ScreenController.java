package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.SpriteBase;

import java.util.ArrayList;

/**
 * Created by Jim on 9/11/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/11/2015
 */
public class ScreenController {

    private ArrayList<SpriteBase> sprites;

    private ArrayList<ImageView> images;

    private Pane playfieldLayer;

    public ScreenController(Pane layer) {
        sprites = new ArrayList<>();
        images = new ArrayList<>();
        playfieldLayer = layer;
    }

    public void addToSprites(final ArrayList<SpriteBase> list) {
        sprites.addAll(list);
        list.forEach(element -> {
            ImageView imageView = new ImageView(new Image(getClass().getResource(element.getImage()).toExternalForm()));
            element.setHeight(imageView.getImage().getHeight());
            element.setWidth(imageView.getImage().getWidth());
            imageView.relocate(element.getX(), element.getY());
            imageView.setRotate(element.getR());
            images.add(imageView);
            playfieldLayer.getChildren().add(imageView);
        });
    }

    public void addToSprites(final SpriteBase sprite) {
        sprites.add(sprite);
        ImageView imageView = new ImageView(new Image(getClass().getResource(sprite.getImage()).toExternalForm()));
        sprite.setHeight(imageView.getImage().getHeight());
        sprite.setWidth(imageView.getImage().getWidth());
        imageView.relocate(sprite.getX(), sprite.getY());
        imageView.setRotate(sprite.getR());
        images.add(imageView);
        playfieldLayer.getChildren().add(imageView);
    }

    public void updateUI() {
        sprites.forEach(this::update);
    }

    private void update(SpriteBase sprite) {
        ImageView image = images.get(sprites.indexOf(sprite));
        image.relocate(sprite.getX(), sprite.getY());
        if(sprite.getSpriteChanged()) {
            image.setImage(new Image(getClass().getResource(sprite.getImage()).toExternalForm()));
            sprite.setSpriteChanged(false);
        }
        image.setRotate(sprite.getR());
    }

}
