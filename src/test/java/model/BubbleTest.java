package model;

import static org.mockito.Mockito.mock;

import controller.LevelController;
import junit.framework.TestCase;

/**
 * Created by toinehartman on 11/09/15.
 */
public class BubbleTest extends TestCase {
    private static Bubble bubbleRight;
    private static Bubble bubbleLeft;
    
    /**
     * This method is run before all the tests to initialize them.
     */
    public void setUp() {
    	LevelController levelController = mock(LevelController.class);
        bubbleRight = new Bubble(1, 1, 0, 0, 0, 0, true, levelController);
        bubbleLeft = new Bubble(1, 1, 0, 0, 0, 0, false, levelController);
    }

    /**
     * This tests what happens when a bubble moves right.
     */
    public void testMoveRight() {
        assertTrue(bubbleRight.getAbleToCatch());

        for (int i = 1; i <= 30; i++) {
            bubbleRight.move();

            assertEquals((double) 1.f + i * 7, bubbleRight.getX());
            assertEquals((double) 1.f, bubbleRight.getY());
        }

        assertTrue(bubbleRight.getAbleToCatch());
        bubbleRight.move();
        assertFalse(bubbleRight.getAbleToCatch());
    }

    /**
     * This tests what happens when a bubble moves left.
     */
    public void testMoveLeft() {
        assertTrue(bubbleLeft.getAbleToCatch());

        for (int i = 1; i <= 30; i++) {
            bubbleLeft.move();

            assertEquals((double) 1.f + i * -7, bubbleLeft.getX());
            assertEquals((double) 1.f, bubbleLeft.getY());
        }

        assertTrue(bubbleLeft.getAbleToCatch());
        bubbleLeft.move();
        assertFalse(bubbleLeft.getAbleToCatch());
    }

    /**
     * This tests if a bubble is able to catch a monster or not.
     */
    public void testSetAbleToCatch() {
        bubbleRight.setAbleToCatch(false);
        assertFalse(bubbleRight.getAbleToCatch());
    }
}
