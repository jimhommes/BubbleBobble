package model;

import controller.LevelController;
import controller.ScreenController;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

/**
 * Created by toinehartman on 11/09/15.
 */
public class BubbleTest {
    private Bubble bubbleRight;
    private Bubble bubbleLeft;

    /**
     * This method is run before all the tests to initialize them.
     */
    @Before
    public void setUp() {
    	LevelController levelController = mock(LevelController.class);
    	ScreenController screenController = mock(ScreenController.class);
    	Level level = mock(Level.class);
    	when(levelController.getScreenController()).thenReturn(screenController);
        bubbleRight = new Bubble(1, 1, 0, 0, 0, 0, true, false, levelController);
        bubbleLeft = new Bubble(1, 1, 0, 0, 0, 0, false, false, levelController);
        ArrayList<Wall> walls = new ArrayList<Wall>();
    	when(levelController.getCurrLvl()).thenReturn(level);
    	when(level.getWalls()).thenReturn(walls);
    }

    /**
     * This tests what happens when a bubble moves right.
     */
    @Test
    public void testMoveRight() {
        assertTrue(bubbleRight.isAbleToCatch());

        for (int i = 1; i < 30; i++) {
            bubbleRight.move();

            assertEquals((double) 1.f + i * 7, bubbleRight.getSpriteBase().getX(), 0.001);
            assertEquals((double) 1.f, bubbleRight.getSpriteBase().getY(), 0.001);
        }

        assertTrue(bubbleRight.isAbleToCatch());
        bubbleRight.move();
        assertFalse(bubbleRight.isAbleToCatch());
    }

    /**
     * This tests what happens when a bubble moves left.
     */
    @Test
    public void testMoveLeft() {
        assertTrue(bubbleLeft.isAbleToCatch());

        for (int i = 1; i < 30; i++) {
            bubbleLeft.move();

            assertEquals((double) 1.f + i * -7, bubbleLeft.getSpriteBase().getX(), 0.001);
            assertEquals((double) 1.f, bubbleLeft.getSpriteBase().getY(), 0.001);
        }

        assertTrue(bubbleLeft.isAbleToCatch());
        bubbleLeft.move();
        assertFalse(bubbleLeft.isAbleToCatch());
    }

    /**
     * This tests if a bubble is able to catch a monster or not.
     */
    @Test
    public void testSetAbleToCatch() {
        bubbleRight.setAbleToCatch(false);
        assertFalse(bubbleRight.isAbleToCatch());
    }

    /**
     * This tests what happens when a bubble moves left.
     */
    @Test
    public void testPop() {
        assertFalse(bubbleLeft.getIsPopped());

        for (int i = 1; i <= 300; i++) {
            bubbleLeft.move();
            assertFalse(bubbleLeft.getIsPopped());
        }
        bubbleLeft.move();

        assertTrue(bubbleLeft.getIsPopped());
    }
}
