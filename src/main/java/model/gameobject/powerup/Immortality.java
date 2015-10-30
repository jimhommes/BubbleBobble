package model.gameobject.powerup;

import model.gameobject.player.Player;

/**
 * This class creates the immortality power up.
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
