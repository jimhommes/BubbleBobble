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

    private static final double duration = 350;

    public Immortality(Player player) {
        super(player, duration);

        player.setImmortal(true);
    }

    @Override
    protected void remove() {
        player.setImmortal(false);
    }
}
