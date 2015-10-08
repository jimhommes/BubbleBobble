package model;

import controller.LevelController;

public abstract class Observer {
	//protected LevelController subject;
	protected LevelController levelController;
	public abstract void update();
}