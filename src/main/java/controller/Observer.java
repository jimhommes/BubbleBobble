package controller;

import model.SpriteBase;

/**
 * 
 * @author jeffr_000
 * Interface that represents the observers.
 */
public interface Observer {
	/**
	 * Method that updates the observers.
	 * @param spriteBase the SpriteBase that needs to be updated. 
	 * @param state the state the SpriteBase is in.
	 */
	public void update(SpriteBase spriteBase, int state);

	/**
	 * Method that updates the sprite on the screen.
	 * @param sprite that needs to be updated.
	 */
	public void update(SpriteBase sprite);
		
}