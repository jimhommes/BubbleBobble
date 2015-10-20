package utility;

/**
 * This class is used for storing default settings.
 */
public final class Settings {

    public static final double SCENE_WIDTH = 832;
    public static final double SCENE_HEIGHT = 832;
    public static final double SPRITE_SIZE = 64;

    public static final int AMOUNT_MAPS = 5;
    public static final float GRAVITY_CONSTANT = 5.f;

    public static final double BUBBLE_INIT_SPEED = 7;
    public static final double BUBBLE_FLY_SPEED = 3;
    public static final double BUBBLE_LIVE_TIME = 300;
    public static final double BUBBLE_FLY_TIME = 30;
    public static final double BUBBLE_POWERUP_FLY_TIME = 3 * BUBBLE_FLY_TIME;

    public static final double PLAYER_SPEED = 5.0;
    public static final int PLAYER_LIVES = 5;

    public static final int POINTS_PLAYER_DIE = -25;
    public static final int POINTS_KILL_MONSTER = 10;
    public static final int POINTS_LEVEL_COMPLETE = 30;

    public static final double MONSTER_SPEED = 3.5;

    public static final double JUMP_SPEED = 3 * PLAYER_SPEED;
    public static final double JUMP_SPEED_WALKER = 3 * MONSTER_SPEED;
    public static final double JUMP_HEIGHT_WALKER = 200;

    public static boolean PLAY_SOUND = true;

    /**
     * The private constructor that does nothing.
     * This is a utility class.
     */
    private Settings() {

    }

}
