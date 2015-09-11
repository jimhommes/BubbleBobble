package controller;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import model.Level;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

public class LevelControllerTest {

	private static LevelController lc;
	
	@BeforeClass
	public static void before() {
		lc = new LevelController();
	}

}
