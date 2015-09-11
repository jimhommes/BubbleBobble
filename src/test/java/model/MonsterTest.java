package model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import controller.LevelController;
import junit.framework.TestCase;
import model.Input;
import model.Monster;
import model.Settings;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jeffrey
 *
 */
public class MonsterTest extends TestCase {

	private static Monster monster;
	private static Input input;
	
	public void setUp() throws Exception{
		LevelController levelController = mock(LevelController.class);
		monster = new Monster("../resources/ZenChanLeft.png", 1, 1, 0, 1, 0, 0, Settings.MONSTER_SPEED, true);
	}
	
	public void testCheckCollision1() throws Exception {
		Bubble bubble = mock(Bubble.class);
		when(bubble.getX()).thenReturn(1.0);
        when(bubble.getY()).thenReturn(1.0);
        when(bubble.getWidth()).thenReturn(300.0);
        when(bubble.getHeight()).thenReturn(300.0);
        when(bubble.getAbleToCatch()).thenReturn(true);
        monster.checkCollision(bubble);
        when(bubble.getAbleToCatch()).thenReturn(false);
        monster.checkCollision(bubble);
        assertTrue(monster.isCaughtByBubble());
        assertEquals(monster.getPrisonBubble(), bubble);
	}
	
	public void testCheckCollision2() throws Exception {
		Bubble bubble = mock(Bubble.class);
		when(bubble.getX()).thenReturn(1.0);
        when(bubble.getY()).thenReturn(1.0);
        when(bubble.getWidth()).thenReturn(300.0);
        when(bubble.getHeight()).thenReturn(300.0);
        when(bubble.getAbleToCatch()).thenReturn(true);
        monster.checkCollision(bubble);
        monster.checkCollision(bubble);
        assertTrue(monster.isCaughtByBubble());
	}
	   
	public void testIsFacingRight() throws Exception {
		assertTrue(monster.isFacingRight());
		monster.setFacingRight(false);
		assertFalse(monster.isFacingRight());
	}

	public void testGetSpeed() throws Exception {
		assertEquals(monster.getSpeed(), Settings.MONSTER_SPEED, 0);
	}
	
	public void testMove() throws Exception {
		monster.move();
		assertEquals(monster.getX(), 2, 0);
	}
	
	public void testR() throws Exception {
    	monster.setR(10);
    	assertEquals(10.0, monster.getR());
    	monster.setDr(10);
    	assertEquals(10.0, monster.getDr());
    }

    public void testHeightWidth() throws Exception {
    	monster.setHeight(100);
    	assertEquals(100.0, monster.getHeight());
    	monster.setWidth(100);
    	assertEquals(100.0, monster.getWidth());
    }
    
    public void testSpriteChanged() throws Exception {
    	monster.setSpriteChanged(false);
    	assertFalse(monster.getSpriteChanged());
    }
    
    public void testCanMove() throws Exception {
    	monster.setCanMove(false);
    	monster.move();
    	assertEquals(1.0, monster.getX());
    }
}
