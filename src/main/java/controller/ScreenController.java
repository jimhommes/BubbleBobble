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

/**
 * This is the Screen Controller, which handles all GUI interactions.
 * If there is a change in coordinates, this controller draws it on the screen.
 */
public class ScreenController {

    private ArrayList<SpriteBase> sprites;

    private ArrayList<ImageView> images;

    private Pane playfieldLayer;

    /**
     * The ScreenController which controls the screen.
     * @param layer they play field level.
     */
    public ScreenController(Pane layer) {
        sprites = new ArrayList<>();
        images = new ArrayList<>();
        playfieldLayer = layer;
    }

    /**
     * This method adds a list of sprite bases.
     * @param list the list of all the sprites.
     */
    public void addToSprites(final ArrayList<SpriteBase> list) {
        sprites.addAll(list);
        list.forEach(element -> {
            ImageView imageView = new ImageView(
            		new Image(getClass().getResource(element.getImagePath()).toExternalForm()));
            element.setHeight(imageView.getImage().getHeight());
            element.setWidth(imageView.getImage().getWidth());
            imageView.relocate(element.getX(), element.getY());
            imageView.setRotate(element.getR());
            images.add(imageView);
            playfieldLayer.getChildren().add(imageView);
        });
    }

    /**
     * This method adds one spritebase.
     * @param sprite the sprite that is being added.
     */
    public void addToSprites(final SpriteBase sprite) {
        sprites.add(sprite);
        ImageView imageView = new ImageView(
        		new Image(getClass().getResource(sprite.getImagePath()).toExternalForm()));
        sprite.setHeight(imageView.getImage().getHeight());
        sprite.setWidth(imageView.getImage().getWidth());
        imageView.relocate(sprite.getX(), sprite.getY());
        imageView.setRotate(sprite.getR());
        images.add(imageView);
        playfieldLayer.getChildren().add(imageView);
    }

    /**
     * This methods updates the UI, and updates where the sprites are in it.
     */
    public void updateUI() {
        sprites.forEach(this::update);
    }

    private void update(SpriteBase sprite) {
        ImageView image = images.get(sprites.indexOf(sprite));
        image.relocate(sprite.getX(), sprite.getY());
        if (sprite.getSpriteChanged()) {
            image.setImage(new Image(
            		getClass().getResource(sprite.getImagePath()).toExternalForm()));
            sprite.setSpriteChanged(false);
        }
        image.setRotate(sprite.getR());
    }

    /**
     * This method removes a sprite.
     * @param sprite the sprite that is being removed.
     */
    public void removeSprite(SpriteBase sprite) {
        int index = sprites.indexOf(sprite);
        images.get(index).setVisible(false);
        images.remove(index);
        sprites.remove(index);
    }
}
