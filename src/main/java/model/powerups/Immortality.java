package model.powerups;

import model.Player;

/**
 * Created by toinehartman on 15/10/15.
 *
 * @author toinehartman
 * @version 1.0
 * @since 15/10/15
 */
public class Immortality extends PlayerEnhancement {

    private static final double DURATION = 350;

    /**
     * Initializes the Immortality.
     * @param player the subject of the powerup.
     */
    public Immortality(Player player) {
        super(player, DURATION);

        player.setImmortal(true);
    }

    @Override
    protected void remove() {
        player.setImmortal(false);
    }
}