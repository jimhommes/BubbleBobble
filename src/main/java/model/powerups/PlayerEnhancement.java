package model.powerups;

import model.Player;

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

    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Player player;

    /**
     * Initialises the PlayerEnhancement.
     * @param player the subject
     * @param duration the duration
     */
    protected PlayerEnhancement(Player player, double duration) {
        this.duration = duration;
        this.player = player;
        this.counter = 0;
    }

    /**
     * Check if the powerup is active.
     * @return true if active, false if expired
     */
    public boolean check() {
        counter++;

        if (counter >= duration) {
            remove();
            return false;
        }

        return true;
    }

    /**
     * Restore the subject to pre-powerup state.
     */
    protected abstract void remove();
}
