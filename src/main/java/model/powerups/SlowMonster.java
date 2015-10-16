package model.powerups;

import model.Player;

/**
 * Created by toinehartman on 15/10/15.
 *
 * @author toinehartman
 * @version 1.0
 * @since 15/10/15
 */
public class SlowMonster extends PlayerEnhancement {

    private static final double DURATION = 450;
    private static final double FACTOR = 0.5;

    /**
     * Initializes the SlowMonster.
     * @param player the subject of the powerup.
     */
    public SlowMonster(Player player) {
        super(player, DURATION);

        player.getLevelController().getCurrLvl().getMonsters().forEach(monster -> {
            monster.factorSpeed(FACTOR);
        });
    }

    @Override
    protected void remove() {
        player.getLevelController().getCurrLvl().getMonsters().forEach(monster -> {
            monster.factorSpeed(1.0 / FACTOR);
        });
    }
}