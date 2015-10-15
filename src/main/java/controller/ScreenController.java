package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Bubble;
import model.Monster;
import model.Player;
import model.Powerup;
import model.SpriteBase;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

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
public class ScreenController implements Observer {

    /**
     * All the sprites that are drawn on the board.
     */
    private ArrayList<SpriteBase> sprites;

    /**
     * All the images that are linked to the sprites.
     */
    private ArrayList<ImageView> images;

    /**
     * The pane where everything is drawn in.
     */
    private Pane playFieldLayer;

	/**
     * The ScreenController which controls the screen.
     * @param layer they play field level.
     */
    public ScreenController(Pane layer) {
    	sprites = new ArrayList<>();
        images = new ArrayList<>();
        playFieldLayer = layer;
    }

    /**
     * This method adds one spriteBase.
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
        playFieldLayer.getChildren().add(imageView);
    }

    /**
     * This methods updates the UI, and updates where the sprites are in it.
     */
    public void updateUI() {
        sprites.forEach(this::update);
    }

    /**
     * This function updates all locations of the sprites.
     * @param sprite Sprite that the location is updated from.
     */
    private void update(SpriteBase sprite) {
    	int place = sprites.indexOf(sprite);
    	if (place >= 0) {
    		ImageView image = images.get(sprites.indexOf(sprite));
            image.relocate(sprite.getX(), sprite.getY());
            if (sprite.getSpriteChanged()) {
                image.setImage(new Image(
                		getClass().getResource(sprite.getImagePath()).toExternalForm()));
                sprite.setSpriteChanged(false);
            }
            image.setRotate(sprite.getR());
    	}
    	
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

    /**
     * Removes all sprites.
     *
     * Prepares the screen for loading a new level.
     */
    public void removeSprites() {
        sprites.clear();

        images.clear();
        playFieldLayer.getChildren().clear();
    }

    /**
     * This function returns the playFieldLayer.
     * @return The playFieldLayer.
     */
    public Pane getPlayFieldLayer() {
        return playFieldLayer;
    }

    /**
     * This function returns the images.
     * @return The images.
     */
    public ArrayList<ImageView> getImages() {
        return images;
    }

    /**
     * This function returns the sprites.
     * @return The sprites.
     */
    public ArrayList<SpriteBase> getSprites() {
        return sprites;
    }

    /**
     * This function sets the sprites.
     * @param sprites The sprites to be set.
     */
    public void setSprites(ArrayList<SpriteBase> sprites) {
        this.sprites = sprites;
    }

    /**
     * This function sets the images.
     * @param images The images to be set.
     */
    public void setImages(ArrayList<ImageView> images) {
        this.images = images;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Player) {
            updatePlayer((Player) o);
        } else if (o instanceof Bubble) {
            updateBubble((Bubble) o);
        } else if (o instanceof Monster) {
            updateMonster((Monster) o);
        } else if (o instanceof Powerup) {
            updatePowerup((Powerup) o);
        }
    }

    private void updatePowerup(Powerup p) {
        if (p.isPickedUp()) {
            removeSprite(p.getSpriteBase());
        }
        update(p.getSpriteBase());
    }

    private void updateMonster(Monster m) {
        if (m.isDead()) {
            removeSprite(m.getSpriteBase());
            removeSprite(m.getPrisonBubble().getSpriteBase());
        }
        update(m.getSpriteBase());
    }

    private void updateBubble(Bubble b) {
        if (b.getIsPopped()) {
            removeSprite(b.getSpriteBase());
        }
        update(b.getSpriteBase());
    }

    private void updatePlayer(Player p) {
        if (p.isDead()) {
            p.getSpriteBase().setImage("/BubbleBobbleDeath.png");
        }
        update(p.getSpriteBase());
    }
}
