package controller;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.support.SpriteBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * The tests for the Screen Controller Class.
 */

public class ScreenControllerTest {

    private ScreenController screenController;

    /**
     * The function that happens before every test.
     */
    @Before
    public void setUp() {
        screenController = new ScreenController(new Pane());
    }

    /**
     * This tests the constructor.
     */
    @Test
    public void testConstructor() {
        assertTrue(screenController.getPlayFieldLayer() != null);
        assertTrue(screenController.getImages() != null);
        assertTrue(screenController.getSprites() != null);
    }

    
    /**
     * This tests the updateUI function.
     */
    @Test
    public void testUpdateUI() {
        ArrayList<SpriteBase> sprites = new ArrayList<>();
        SpriteBase sprite = mock(SpriteBase.class);
        sprites.add(sprite);
        ArrayList<ImageView> images = new ArrayList<>();
        ImageView image = mock(ImageView.class);
        images.add(image);
        screenController.setSprites(sprites);
        screenController.setImages(images);
        screenController.updateUI();
        verify(sprite, atLeastOnce()).getYCoordinate();
        verify(sprite, atLeastOnce()).getXCoordinate();
        verify(sprite, atLeastOnce()).getSpriteChanged();
        verify(image, atLeastOnce()).relocate(any(double.class), any(double.class));
    }
    

    /**
     * This test the removeSprite function.
     */
    @Test
    public void testRemoveSprite() {
        ArrayList<SpriteBase> sprites = new ArrayList<>();
        SpriteBase sprite = mock(SpriteBase.class);
        sprites.add(sprite);
        ArrayList<ImageView> images = new ArrayList<>();
        images.add(mock(ImageView.class));
        screenController.setSprites(sprites);
        screenController.setImages(images);
        assertTrue(screenController.getImages().size() > 0);
        assertTrue(screenController.getSprites().size() > 0);

        screenController.removeSprite(sprite);
        assertTrue(screenController.getImages().size() == 0);
        assertTrue(screenController.getSprites().size() == 0);
    }

    /**
     * This test the removeSprites function.
     */
    @Test
    public void testRemoveSprites() {
        ArrayList<SpriteBase> sprites = new ArrayList<>();
        SpriteBase sprite = mock(SpriteBase.class);
        sprites.add(sprite);
        ArrayList<ImageView> images = new ArrayList<>();
        images.add(mock(ImageView.class));
        screenController.setSprites(sprites);
        screenController.setImages(images);
        assertTrue(screenController.getImages().size() > 0);
        assertTrue(screenController.getSprites().size() > 0);

        screenController.removeSprites();
        assertTrue(screenController.getImages().size() == 0);
        assertTrue(screenController.getSprites().size() == 0);
    }

}
