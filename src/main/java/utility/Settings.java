package utility;

/**
 * Class used for storing default settings.
 */
public final class Settings {

    /**
     * The width of the scene the player can move around in.
     */
    public static final double SCENE_WIDTH = 832;

    /**
     * The height of the scene the player can move around in.
     */
    public static final double SCENE_HEIGHT = 832;

    /**
     * The player speed.
     */
    public static final double PLAYER_SPEED = 5.0;

    /**
     * The player health (if used).
     */
    public static final double PLAYER_HEALTH = 100.0;

    /**
     * The monster speed.
     */
    public static final double MONSTER_SPEED = 5;

    /**
     * The bubble speed when it is flying horizontally.
     */
    public static final double BUBBLE_INIT_SPEED = 7;

    /**
     * The bubble speed when it is flying vertically.
     */
    public static final double BUBBLE_FLY_SPEED = 3;

    /**
     * The jump speed.
     */
    public static final double JUMP_SPEED = 3 * PLAYER_SPEED;

    /**
     * The private constructor that does nothing.
     * This is a utility class.
     */
    private Settings() {

    }

}