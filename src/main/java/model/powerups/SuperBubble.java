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

    private static final double duration = 400;

    public SuperBubble(Player player) {
        super(player, duration);

        player.setBubblePowerup(true);

    }

    @Override
    protected void remove() {
        player.setBubblePowerup(false);
    }
}
