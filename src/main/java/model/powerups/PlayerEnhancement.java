package model.powerups;

import model.Player;

import java.util.Observable;

/**
 * This class creates the different types of power ups that are used in the game.
 * Created by Jim on 10/7/2015.
 *
 * @author Jim
 * @version 1.0
 * @since 10/7/2015
 */
public abstract class PlayerEnhancement {

    private double counter;
    private double duration;
    protected Player player;

    protected PlayerEnhancement(Player player, double duration) {
        this.duration = duration;
        this.player = player;
        this.counter = 0;
    }

    public boolean check() {
        counter++;

        if (counter >= duration) {
            remove();
            return false;
        }

        return true;
    }

    protected abstract void remove();
}
