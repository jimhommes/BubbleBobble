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

    private static final double duration = 450;
    private static final double factor = 0.5;

    public SlowMonster(Player player) {
        super(player, duration);

        player.getLevelController().getCurrLvl().getMonsters().forEach(monster -> {
            monster.factorSpeed(factor);
        });
    }

    @Override
    protected void remove() {
        player.getLevelController().getCurrLvl().getMonsters().forEach(monster -> {
            monster.factorSpeed(1.0 / factor);
        });
    }
}
