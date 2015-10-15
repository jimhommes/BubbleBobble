package controller;

import java.io.File;
import java.util.ArrayList;

public class LevelControllerMethods {

	private LevelController levelController;
	
	private String pathMaps = "src/main/resources";
	
	private boolean gamePaused;
	
	public LevelControllerMethods(LevelController levelController){
		this.setLevelController(levelController);
		gamePaused = false;
	}
	
	/**
     * This function scans the resources folder for maps.
     */
    public ArrayList<String> findMaps() {
    	ArrayList<String> maps = new ArrayList<String>();
        File folder = new File(pathMaps);
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().matches("map[5-9]*.txt")) {
                maps.add(file.getName());
            }
        }
        return maps;
    }
    
    /**
     * The function that sets the path to the maps.
     * @param pathMaps The path to the maps.
     */
    public void setPathMaps(String pathMaps) {
        this.pathMaps = pathMaps;
    }
    
    /**
     * This method gets the path of the maps.
     * @return pathMaps, the path to the maps.
     */
    public String getPathMaps() {
    	return pathMaps;
    }

    /**
     * This is the boolean to check if the game is paused or not.
     * @return True if the gamePaused is true.
     */
	public boolean getGamePaused() {
		return gamePaused;
	}

	/**
	 * 
	 * @param gamePaused
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
