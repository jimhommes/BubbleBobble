package model;

import utility.Settings;
import java.util.Observable;

/**
 * This class represents objects affected by gravity.
 */
abstract class GravityObject extends Observable {

    /**
     * This method calculates the gravity.
     *
     * @return The gravity constant.
     */
    float calculateGravity() {
        return -Settings.GRAVITY_CONSTANT;
    }

    /**
     * Check for collision combined with jumping.
     *
     * @param jumping The variable whether a GravityObject is jumping.
     * @param ableToJump The variable whether a GravityObject is able to jump.
     * @return The ableToJump variable.
     */
    public abstract boolean moveCollisionChecker(boolean jumping, boolean ableToJump);
}
