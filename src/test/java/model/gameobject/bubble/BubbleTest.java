package model.gameobject.bubble;

import controller.LevelController;
import controller.LevelControllerMethods;
import controller.ScreenController;
import javafx.animation.AnimationTimer;
import model.gameobject.level.Level;
import model.gameobject.level.Wall;
import model.support.Coordinates;
import model.support.SpriteBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the bubbles.
 */
public class BubbleTest {
    private Bubble bubbleRight;
    private Bubble bubbleLeft;
    private LevelController levelController;

    /**
     * This method is run before all the tests to initialize them.
     */
    @Before
    public void setUp() {
    	levelController = mock(LevelController.class);
    	ScreenController screenController = mock(ScreenController.class);
    	Level level = mock(Level.class);
    	when(levelController.getScreenController()).thenReturn(screenController);

    	Coordinates coordinatesBubble = new Coordinates(1, 1, 0, 0, 0, 0);
    	
        bubbleRight = new PlayerBubble(coordinatesBubble, true, false, levelController);
        bubbleLeft = new PlayerBubble(coordinatesBubble, false, false, levelController);
        ArrayList<Wall> walls = new ArrayList<>();
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

            assertEquals((double) 1.f + i * 7, bubbleRight.getSpriteBase().getXCoordinate(), 0.001);
            assertEquals((double) 1.f, bubbleRight.getSpriteBase().getYCoordinate(), 0.001);
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

            assertEquals((double) 1.f + i * -7, bubbleLeft.getSpriteBase().getXCoordinate(), 0.001);
            assertEquals((double) 1.f, bubbleLeft.getSpriteBase().getYCoordinate(), 0.001);
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
    
    /**
     * This test test the get and set SpriteBase.
     */
    @Test
    public void testGetSpriteBase() {
      LevelController levelController = mock(LevelController.class);
      Coordinates coordinatesBubble = new Coordinates(1, 1, 0, 0, 0, 0);
      ScreenController screenController = mock(ScreenController.class);
      when(levelController.getScreenController()).thenReturn(screenController);

      PlayerBubble playerBubble = new PlayerBubble(coordinatesBubble,
                                            true, false, levelController);
      
      SpriteBase spriteBase = new SpriteBase("bubble.png", new Coordinates(1, 1, 0, 0, 0, 0));
      playerBubble.setSpriteBase(spriteBase);
      assertEquals(spriteBase, playerBubble.getSpriteBase());
    }

    /**
     * This tests the timer of the bubble.
     */
    @Test
    public void testTimer() {
        AnimationTimer timer = bubbleRight.createTimer();
        SpriteBase spriteBase = mock(SpriteBase.class);
        bubbleRight.setSpriteBase(spriteBase);
        LevelControllerMethods lcm = mock(LevelControllerMethods.class);
        when(levelController.getLevelControllerMethods()).thenReturn(lcm);
        when(lcm.getGamePaused()).thenReturn(false);

        timer.handle(1);

        verify(spriteBase, atLeastOnce()).move();
        verify(levelController, atLeastOnce()).update(any(Observable.class), any(Object.class));
    }

    /**
     * This tests the checkPop function.
     */
    @Test
    public void testCheckPopCounter() {
        assertFalse(bubbleRight.getIsPopped());
        bubbleRight.checkPop();
        assertFalse(bubbleRight.getIsPopped());
        bubbleRight.setCounter(350);
        bubbleRight.checkPop();
        assertTrue(bubbleRight.getIsPopped());
    }

    /**
     * This tests the checkPop function.
     */
    @Test
    public void testCheckPopPrisonBubble() {
        assertFalse(bubbleRight.getIsPopped());
        bubbleRight.setCounter(350);
        bubbleRight.setPrisonBubble(true);
        bubbleRight.checkPop();
        assertFalse(bubbleRight.getIsPopped());
    }

    /**
     * This tests the move function when there is a collision.
     */
    @Test
    public void testMoveCollisionVertical() {
        SpriteBase spriteBase = mock(SpriteBase.class);
        bubbleRight.setCounter(1000);
        bubbleRight.setSpriteBase(spriteBase);
        when(spriteBase.causesCollisionWall(anyDouble(), anyDouble(),
                anyDouble(), anyDouble(), any(LevelController.class))).thenReturn(true);
        when(spriteBase.getYCoordinate()).thenReturn(10.0);

        bubbleRight.move();

        verify(spriteBase, atLeastOnce()).setDyCoordinate(0);
    }

    /**
     * This tests the move function when there is a collision.
     */
    @Test
    public void testMoveCollisionHorizontalRight() {
        SpriteBase spriteBase = mock(SpriteBase.class);
        bubbleRight.setCounter(0);
        bubbleRight.setSpriteBase(spriteBase);
        when(spriteBase.causesCollisionWall(anyDouble(), anyDouble(),
                anyDouble(), anyDouble(), any(LevelController.class))).thenReturn(true);

        bubbleRight.move();

        verify(spriteBase, atLeastOnce()).setDxCoordinate(0);
    }

    /**
     * This tests the move function when there is a collision.
     */
    @Test
    public void testMoveCollisionHorizontalLeft() {
        SpriteBase spriteBase = mock(SpriteBase.class);
        bubbleRight.setCounter(0);
        bubbleRight.setSpriteBase(spriteBase);
        when(spriteBase.causesCollisionWall(anyDouble(), anyDouble(),
                anyDouble(), anyDouble(), any(LevelController.class))).thenReturn(true);

        bubbleRight.move();

        verify(spriteBase, atLeastOnce()).setDxCoordinate(0);
    }

    /**
     * This tests the setPrisonBubble.
     */
    @Test
    public void testSetPrisonBubble() {
        assertFalse(bubbleRight.getPrisonBubble());
        bubbleRight.setPrisonBubble(true);
        assertTrue(bubbleRight.getPrisonBubble());
    }
}