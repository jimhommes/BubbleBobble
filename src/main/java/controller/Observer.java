package controller;

import model.Monster;
import model.SpriteBase;

/**
 * 
 * @author jeffr_000
 * Abstract class that represents the observers.
 */
public abstract class Observer {
	/**
	 * Method that updates the observers.
	 * @param spriteBase the SpriteBase that needs to be updated. 
	 * @param state the state the SpriteBase is in.
	 */
	public abstract void update(SpriteBase spriteBase, int state);

	/**
	 * Method that updates the sprite on the screen.
	 * @param sprite that needs to be updated.
	 */
	public abstract void update(SpriteBase sprite);
		
}