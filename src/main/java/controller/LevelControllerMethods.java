package controller;

import utility.Settings;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Methods for LevelController.
 */
public class LevelControllerMethods {

	private LevelController levelController;
	private boolean gamePaused;

	/**
	 * Constructor.
	 * @param levelController the controller
     */
	public LevelControllerMethods(LevelController levelController) {
	  this.setLevelController(levelController);
		gamePaused = false;
	}
	
	/**
     * This function scans the resources folder for maps.
	 * @return list of maps filenames.
     */
    public ArrayList<String> findMaps() {
        ArrayList<String> maps = new ArrayList<>();
        for (int i = 1; i <= Settings.AMOUNT_MAPS; i++) {
            String s = String.format("map%d.txt", i);
            URL u = this.getClass().getClassLoader().getResource(s);
            String p = u.getPath();
            Path tempPath = Paths.get(p.replace(":", "%3A"));
            maps.add(tempPath.getFileName().toString());
        }
        return maps;
    }
    
    /**
     * This is the boolean to check if the game is paused or not.
     * @return True if the gamePaused is true.
     */
	public boolean getGamePaused() {
		return gamePaused;
	}

	/**
	 * Set the game paused.
	 * @param gamePaused if paused
	 */
	public void setGamePaused(boolean gamePaused) {
		this.gamePaused = gamePaused;
	}

	/**
	 * This method returns the LevelController.
	 * @return the levelController.
	 */
	public LevelController getLevelController() {
		return levelController;
	}

	/**
	 * This method sets the LevelController.
	 * @param levelController the new LevelController.
	 */
	public void setLevelController(LevelController levelController) {
		this.levelController = levelController;
	}
}
