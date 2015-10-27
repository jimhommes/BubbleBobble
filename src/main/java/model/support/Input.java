package model.support;

import controller.MainController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utility.Logger;
import utility.Settings;

import java.util.BitSet;

/**
 * This class defines the input for the character.
 */
public class Input {

    private final int playerNumber;

    // -------------------------------------------------
    // default key codes
    // will vary when you let the user customize the key codes
    // or when you add support for a 2nd player
    // -------------------------------------------------
    private BitSet keyboardBitSet = new BitSet();

    public static final KeyCode UP_KEY = Settings.getKeyCode("UP_KEY", KeyCode.UP);
    public static final KeyCode DOWN_KEY = Settings.getKeyCode("DOWN_KEY", KeyCode.DOWN);
    public static final KeyCode LEFT_KEY = Settings.getKeyCode("LEFT_KEY", KeyCode.LEFT);
    public static final KeyCode RIGHT_KEY = Settings.getKeyCode("RIGHT_KEY", KeyCode.RIGHT);
    public static final KeyCode PRIMARY_WEAPON_KEY = Settings
            .getKeyCode("PRIMARY_WEAPON_KEY", KeyCode.SPACE);
    public static final KeyCode SECONDARY_WEAPON_KEY = Settings
            .getKeyCode("SECONDARY_WEAPON_KEY", KeyCode.CONTROL);
    public static final KeyCode W_KEY = Settings.getKeyCode("W_KEY", KeyCode.W);
    public static final KeyCode A_KEY = Settings.getKeyCode("A_KEY", KeyCode.A);
    public static final KeyCode S_KEY = Settings.getKeyCode("S_KEY", KeyCode.S);
    public static final KeyCode D_KEY = Settings.getKeyCode("D_KEY", KeyCode.D);
    public static final KeyCode SHIFT_KEY = Settings.getKeyCode("SHIFT_KEY", KeyCode.SHIFT);

    /**
     * "Key Pressed" handler for all input events: register pressed key in the bitset.
     */
    private EventHandler<KeyEvent> keyPressedEventHandler = event -> {

        // register key down
        keyboardBitSet.set(event.getCode().ordinal(), true);

        Logger.log(String.format("Key %s was pressed", event.getCode().getName()));

    };
    /**
     * "Key Released" handler for all input events: unregister released key in the bitset.
     */
    private EventHandler<KeyEvent> keyReleasedEventHandler = event -> {

        // register key up
        keyboardBitSet.set(event.getCode().ordinal(), false);

    };

    private MainController mainController;

    /**
     * The constructor. This only appoints the scene the player moves in.
     *
     * @param mainController The MainController the player moves in.
     * @param playerNumber The number of the player.
     */
    public Input(MainController mainController, int playerNumber) {
        this.mainController = mainController;
        this.playerNumber = playerNumber;
    }

    /**
     * This function adds the listeners for the keys.
     */
    public void addListeners() {

        mainController.addListeners(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        mainController.addListeners(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);

    }

    // -------------------------------------------------
    // Evaluate bitset of pressed keys and return the player input.
    // If direction and its opposite direction are pressed simultaneously,
    // then the direction isn't handled.
    // -------------------------------------------------

    /**
     * This is the boolean to check if the player moves up or not.
     *
     * @return True if the up key is pressed.
     */
    public boolean isMoveUp() {
        if (playerNumber == 1) {
            return keyboardBitSet.get(UP_KEY.ordinal()) && !keyboardBitSet.get(DOWN_KEY.ordinal());
        } else {
            return keyboardBitSet.get(W_KEY.ordinal()) && !keyboardBitSet.get(S_KEY.ordinal());
        }
    }

    /**
     * This is the boolean to check if the player moves down or not.
     *
     * @return True if the down key is pressed.
     */
    public boolean isMoveDown() {
        if (playerNumber == 1) {
            return keyboardBitSet.get(DOWN_KEY.ordinal()) && !keyboardBitSet.get(UP_KEY.ordinal());
        } else {
            return keyboardBitSet.get(S_KEY.ordinal()) && !keyboardBitSet.get(W_KEY.ordinal());
        }
    }

    /**
     * This is the boolean to check if the player moves left or not.
     *
     * @return True if the left key is pressed.
     */
    public boolean isMoveLeft() {
        if (playerNumber == 1) {
            return keyboardBitSet.get(LEFT_KEY.ordinal())
                    && !keyboardBitSet.get(RIGHT_KEY.ordinal());
        } else {
            return keyboardBitSet.get(A_KEY.ordinal()) && !keyboardBitSet.get(D_KEY.ordinal());
        }
    }


    /**
     * This is the boolean to check if the player moves right or not.
     *
     * @return True if the right key is pressed.
     */
    public boolean isMoveRight() {
        if (playerNumber == 1) {
            return keyboardBitSet.get(RIGHT_KEY.ordinal())
                    && !keyboardBitSet.get(LEFT_KEY.ordinal());
        } else {
            return keyboardBitSet.get(D_KEY.ordinal()) && !keyboardBitSet.get(A_KEY.ordinal());
        }
    }

    /**
     * This is the boolean to check if the player fires or not.
     *
     * @return True if the space key is pressed.
     */
    public boolean isFirePrimaryWeapon() {
        if (playerNumber == 1) {
            return keyboardBitSet.get(PRIMARY_WEAPON_KEY.ordinal());
        } else {
            return keyboardBitSet.get(SHIFT_KEY.ordinal());
        }
    }


    /**
     * This is the boolean to check if the player fires his secondary or not.
     *
     * @return True if the control key is pressed.
     */
    public boolean isFireSecondaryWeapon() {
        return keyboardBitSet.get(SECONDARY_WEAPON_KEY.ordinal());
    }

    /**
     * This method sets the keyboardbitset.
     * @param keyboardBitSet the BitSet that is being set.
     */
    public void setKeyboardBitSet(BitSet keyboardBitSet) {
        this.keyboardBitSet = keyboardBitSet;
    }

    /**
     * This returns the keyPressedEventHandler.
     * @return The KeyPressedEventHandler.
     */
    public EventHandler<KeyEvent> getKeyPressedEventHandler() {
        return keyPressedEventHandler;
    }

    /**
     * This returns the keyReleasedEventHandler.
     * @return The KeyReleasedEventHandler.
     */
    public EventHandler<KeyEvent> getKeyReleasedEventHandler() {
        return keyReleasedEventHandler;
    }
}