package model.gameobject.powerup;

import model.gameobject.player.Player;

/**
 * This class creates the double speed power up.
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
