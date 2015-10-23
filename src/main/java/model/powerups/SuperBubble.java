package model.powerups;

import model.Player;

/**
 * This class creates the super bubble power up.
 */
public class SuperBubble extends PlayerEnhancement {

    private static final double DURATION = 400;

    /**
     * Initializes the SuperBubble.
     * @param player the subject of the powerup.
     */
    public SuperBubble(Player player) {
        super(player, DURATION);

        player.setBubblePowerup(true);

    }

    @Override
    protected void remove() {
        player.setBubblePowerup(false);
    }
}
