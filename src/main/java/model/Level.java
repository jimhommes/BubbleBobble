package model;

import java.util.ArrayList;

/**
 * This class creates the levels for the game. When created it can load a
 * level from a .txt file.
 */
public class Level {


    /**
     * The list of walls that define the map.
     */
    private ArrayList<Wall> walls;

    /**
     * The list of the monsters that spawn.
     */
    private ArrayList<Monster> monsters;

    /**
     * The list of the monsters that spawn.
     */
    private ArrayList<Player> players;

    private int counter;

    /**
     * When a level is created in the levelController, it is immediately drawn.
     */
    public Level() {
        this.walls = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    /**
     * The function that returns the arrayList of monsters.
     * @return The arrayList of monsters.
     */
    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    /**
     * This method gets the players in the game.
     * @return The players in the game.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * This method gets the walls in the game.
     * @return The walls in the game.
     */
    public ArrayList<Wall> getWalls() {
        return walls;
    }

    /**
     * This function adds a player to the level.
     * @param player The player to be added.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * This function adds a monster to the level.
     * @param monster The monster to be added.
     */
    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    /**
     * This function adds a wall to the level.
     * @param wall The wall to be added.
     */
    public void addWall(Wall wall) {
        walls.add(wall);
    }

    /**
     * This method updates the monster list, to see if all the monsters have died.
     * @return true is the monster list is empty.
     */
    public boolean update() {
        ArrayList<Monster> newMonsters = new ArrayList<>();
        monsters.forEach(monster -> {
            if (!monster.isDead()) {
                newMonsters.add(monster);
            }
        });
        monsters = newMonsters;
        
        if (monsters.size() == 0) {
        	if (counter < 200) {
        		counter++;
        		return false;
        	}
        	else {
        		return true;
        	}
        }
        else {
        	return false;
        }
    }
}
