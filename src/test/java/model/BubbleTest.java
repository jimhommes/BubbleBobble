package model;

import controller.LevelController;
import junit.framework.TestCase;

import static org.mockito.Mockito.mock;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by toinehartman on 11/09/15.
 */
public class BubbleTest extends TestCase {
    private Bubble bubble;
    private LevelController levelController;

    /**
     * This method happens before all the tests.
     */
    @BeforeClass
    public void setUp() {
        levelController = mock(LevelController.class);
        bubble = new Bubble(1, 1, 0, 0, 0, 0, true, levelController);
    }

    /**
     * This tests the movement of the bubbles.
     */
    @Test
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

    /**
     * This tests if the bubble is able to catch a monster.
     */
    @Test
    public void testSetAbleToCatch() {
        bubble.setAbleToCatch(false);
        assertFalse(bubble.getAbleToCatch());
    }
}
