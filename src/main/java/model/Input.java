package model;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utility.Logger;

import java.util.BitSet;

/**
 * This class defines the input for the character.
 */
public class Input {

    /**
     * The scene the player moves in.
     */
    private Scene scene;

    // -------------------------------------------------
    // default key codes
    // will vary when you let the user customize the key codes
    // or when you add support for a 2nd player
    // -------------------------------------------------
    /**
     * Bitset which registers if any {@link KeyCode}
     * keeps being pressed or if it is released.
     */
    private BitSet keyboardBitSet = new BitSet();
    /**
     * KeyCode for the up key.
     */
    public static final KeyCode UP_KEY = KeyCode.UP;
    /**
     * KeyCode for the down key.
     */
    public static final KeyCode DOWN_KEY = KeyCode.DOWN;
    /**
     * KeyCode for the left key.
     */
    public static final KeyCode LEFT_KEY = KeyCode.LEFT;
    /**
     * KeyCode for the right key.
     */
    public static final KeyCode RIGHT_KEY = KeyCode.RIGHT;
    /**
     * KeyCode for the space key. (fire primary weapon)
     */
    public static final KeyCode PRIMARY_WEAPON_KEY = KeyCode.SPACE;
    /**
     * KeyCode for the control key. (fire secondary weapon)
     */
    public static final KeyCode SECONDARY_WEAPON_KEY = KeyCode.CONTROL;
    /**
     * "Key Pressed" handler for all input events: register pressed key in the bitset.
     */
    private EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            // register key down
            keyboardBitSet.set(event.getCode().ordinal(), true);

            Logger.log(String.format("Key %s was pressed", event.getCode().getName()));

        }
    };
    /**
     * "Key Released" handler for all input events: unregister released key in the bitset.
     */
    private EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {

            // register key up
            keyboardBitSet.set(event.getCode().ordinal(), false);

        }
    };

    /**
     * The constructor. This only appoints the scene the player moves in.
     *
     * @param scene The scene the player moves in.
     */
    public Input(Scene scene) {
        this.scene = scene;
    }

    /**
     * This function adds the listeners for the keys.
     */
    public void addListeners() {

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);

    }

    /**
     * This function removes the listeners for the keys.
     */
    public void removeListeners() {

        scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);

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
        return keyboardBitSet.get(UP_KEY.ordinal()) && !keyboardBitSet.get(DOWN_KEY.ordinal());
    }

    /**
     * This is the boolean to check if the player moves down or not.
     *
     * @return True if the down key is pressed.
     */
    public boolean isMoveDown() {
        return keyboardBitSet.get(DOWN_KEY.ordinal()) && !keyboardBitSet.get(UP_KEY.ordinal());
    }

    /**
     * This is the boolean to check if the player moves left or not.
     *
     * @return True if the left key is pressed.
     */
    public boolean isMoveLeft() {
        return keyboardBitSet.get(LEFT_KEY.ordinal()) && !keyboardBitSet.get(RIGHT_KEY.ordinal());
    }


    /**
     * This is the boolean to check if the player moves right or not.
     *
     * @return True if the right key is pressed.
     */
    public boolean isMoveRight() {
        return keyboardBitSet.get(RIGHT_KEY.ordinal()) && !keyboardBitSet.get(LEFT_KEY.ordinal());
    }

    /**
     * This is the boolean to check if the player fires or not.
     *
     * @return True if the space key is pressed.
     */
    public boolean isFirePrimaryWeapon() {
        return keyboardBitSet.get(PRIMARY_WEAPON_KEY.ordinal());
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
}