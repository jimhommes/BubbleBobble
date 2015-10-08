package controller;

public abstract class Observer {
	protected LevelController levelController;
	public abstract void update();
}