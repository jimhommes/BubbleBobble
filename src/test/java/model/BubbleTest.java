package model;

import junit.framework.TestCase;

/**
 * Created by toinehartman on 11/09/15.
 */
public class BubbleTest extends TestCase {
    private static Bubble bubble;

    public void setUp() {
        bubble = new Bubble(1, 1, 0, 0, 0, 0, true);
    }

    public void testMove() {
        assertTrue(bubble.getAbleToCatch());

        for (int i = 1; i <= 30; i++) {
            bubble.move();

            assertEquals((double) 1.f + i * 7, bubble.getX());
            assertEquals((double) 1.f, bubble.getY());
        }

        assertTrue(bubble.getAbleToCatch());
        bubble.move();
        assertFalse(bubble.getAbleToCatch());
    }

    public void testSetAbleToCatch() {
        bubble.setAbleToCatch(false);
        assertFalse(bubble.getAbleToCatch());
    }
}
