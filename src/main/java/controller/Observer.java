package controller;

import model.Monster;
import model.SpriteBase;

public abstract class Observer {
	protected LevelController levelController;
	public abstract void update(SpriteBase spriteBase);
}