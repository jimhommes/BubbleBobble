package model;

import controller.Observer;

/**
 * 
 * @author jeffr_000
 * Abstract class that represents the subject.
 */
public interface Subject {
	
	/**
	 * This method attaches an observer to the ArrayList.
	 * @param observer the added observer.
	 */
	void attach(Observer observer);	
	
	/**
     * This method notifies all the observers that something changed.
     * @param spriteBase the SpriteBase.
     * @param state the state the SpriteBase is in.
     */
	void notifyAllObservers(SpriteBase spriteBase, int state);
}