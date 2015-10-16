package model.powerups;

import model.Player;

/**
 * Created by toinehartman on 15/10/15.
 *
 * @author toinehartman
 * @version 1.0
 * @since 15/10/15
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
