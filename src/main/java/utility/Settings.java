package utility;

/**
 * This class is used for storing default settings.
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
    public static final int PLAYER_LIVES = 5;

    /**
     * The monster speed.
     */
    public static final double MONSTER_SPEED = 3.5;

    /**
     * The bubble speed when it is flying horizontally.
     */
    public static final double BUBBLE_INIT_SPEED = 7;

    /**
     * The bubble speed when it is flying vertically.
     */
    public static final double BUBBLE_FLY_SPEED = 3;

    /**
     * The number of loops that the bubble will be able to live.
     */
    public static final double BUBBLE_LIVE_TIME = 300;

    /**
     * The number of loops that the bubble will be flying horizontally.
     */
    public static final double BUBBLE_FLY_TIME = 30;

    /**
     * The number of loops the bubble flies horizontally during powerup.
     */
    public static final double BUBBLE_POWERUP_FLY_TIME = 3 * BUBBLE_FLY_TIME;

    /**
     * The jump speed.
     */
    public static final double JUMP_SPEED = 3 * PLAYER_SPEED;

    /**
     * The jump speed of a walker.
     */
    public static final double JUMP_SPEED_WALKER = 3 * MONSTER_SPEED;

    /**
     * The jump height of a walker.
     */
    public static final double JUMP_HEIGHT_WALKER = 200;

    /**
     * The amount of maps made available.
     */
    public static final int AMOUNT_MAPS = 5;

    public static final int POINTS_PLAYER_DIE = -25;
    public static final int POINTS_KILL_MONSTER = 10;
    public static final int POINTS_LEVEL_COMPLETE = 30;

    /**
     * The sprite size.
     */
    public static final double SPRITE_SIZE = 64;

    public static final double MONSTER_SLOWDOWN_FACTOR = 0.5;
    public static final double MONSTER_POWERUP_TIME = 450;

    public static final float GRAVITY_CONSTANT = 5.f;

    /**
     * The private constructor that does nothing.
     * This is a utility class.
     */
    private Settings() {

    }

}
