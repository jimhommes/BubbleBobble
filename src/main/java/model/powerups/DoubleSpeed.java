package model.powerups;

import model.Player;

/**
 * Created by toinehartman on 15/10/15.
 *
 * @author toinehartman
 * @version 1.0
 * @since 15/10/15
 */
public class DoubleSpeed extends PlayerEnhancement {

    private static final double DURATION = 200;
    private static final double FACTOR = 2;

    /**
     * Initializes the DoubleSpeed.
     * @param player the subject of the powerup.
     */
    public DoubleSpeed(Player player) {
        super(player, DURATION);

        player.factorSpeed(FACTOR);
    }

    @Override
    protected void remove() {
        player.factorSpeed(1.0 / FACTOR);
    }
}
