package model;

import junit.framework.TestCase;

/**
 * Created by toinehartman on 11/09/15.
 */
public class BubbleTest extends TestCase {
    private static Bubble bubbleRight;
    private static Bubble bubbleLeft;

    public void setUp() {
        bubbleRight = new Bubble(1, 1, 0, 0, 0, 0, true);
        bubbleLeft = new Bubble(1, 1, 0, 0, 0, 0, false);
    }

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

    public void testSetAbleToCatch() {
        bubbleRight.setAbleToCatch(false);
        assertFalse(bubbleRight.getAbleToCatch());
    }
}
