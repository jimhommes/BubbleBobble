package model;

import controller.LevelController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

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
        bubbleRight = new Bubble(1, 1, 0, 0, 0, 0, true, levelController);
        bubbleLeft = new Bubble(1, 1, 0, 0, 0, 0, false, levelController);
    }

    /**
     * This tests what happens when a bubble moves right.
     */
    @Test
    public void testMoveRight() {
        assertTrue(bubbleRight.getAbleToCatch());

        for (int i = 1; i < 30; i++) {
            bubbleRight.move();

            assertEquals((double) 1.f + i * 7, bubbleRight.getX(), 0.001);
            assertEquals((double) 1.f, bubbleRight.getY(), 0.001);
        }

        assertTrue(bubbleRight.getAbleToCatch());
        bubbleRight.move();
        assertFalse(bubbleRight.getAbleToCatch());
    }

    /**
     * This tests what happens when a bubble moves left.
     */
    @Test
    public void testMoveLeft() {
        assertTrue(bubbleLeft.getAbleToCatch());

        for (int i = 1; i < 30; i++) {
            bubbleLeft.move();

            assertEquals((double) 1.f + i * -7, bubbleLeft.getX(), 0.001);
            assertEquals((double) 1.f, bubbleLeft.getY(), 0.001);
        }

        assertTrue(bubbleLeft.getAbleToCatch());
        bubbleLeft.move();
        assertFalse(bubbleLeft.getAbleToCatch());
    }

    /**
     * This tests if a bubble is able to catch a monster or not.
     */
    @Test
    public void testSetAbleToCatch() {
        bubbleRight.setAbleToCatch(false);
        assertFalse(bubbleRight.getAbleToCatch());
    }

    /**
     * This tests what happens when a bubble moves left.
     */
    @Test
    public void testPop() {
        assertFalse(bubbleLeft.checkPop());

        for (int i = 1; i <= 300; i++) {
            bubbleLeft.move();
            assertFalse(bubbleLeft.checkPop());
        }
        bubbleLeft.move();

        assertTrue(bubbleLeft.checkPop());
    }
}
