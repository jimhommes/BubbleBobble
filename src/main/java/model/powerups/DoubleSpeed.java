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

    private static final double duration = 200;
    private static final double factor = 2;

    public DoubleSpeed(Player player) {
        super(player, duration);

        player.factorSpeed(factor);
    }

    @Override
    protected void remove() {
        player.factorSpeed(1.0 / factor);
    }
}
