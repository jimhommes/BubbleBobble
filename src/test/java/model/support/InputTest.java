package model.support;

import controller.MainController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import utility.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests the Input class.
 */
public class InputTest {
    private BitSet keyboardBitSet;
    private MainController mainController;
    @InjectMocks private Input input;

    /**
     * This is run before all the tests to initialize them.
     */
    @Before
    public void setUp() {
        Settings.initialize("test.properties");

        mainController = mock(MainController.class);
        input = new Input(mainController, 1);
        keyboardBitSet = mock(BitSet.class);
        input.setKeyboardBitSet(keyboardBitSet);
    }

    /**
     * Remove the properties file if it exists.
     */
    @After
    public void breakDown() {
        try {
            Files.delete(Paths.get("test.properties"));
        } catch (IOException e) {
            return;
        }
    }

    /**
     * This tests that the input isn't null.
     */
    @Test
    public void testInput() {
        assertNotNull(input);
    }

    
    /**
     * This tests what happens when the input is either the up or the down button.
     */
    @Test
    public void testIsMoveUpDown() {
        when(keyboardBitSet.get(Input.UP_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.DOWN_KEY.ordinal())).thenReturn(false);

        assertTrue(input.isMoveUp());
        assertFalse(input.isMoveDown());

        when(keyboardBitSet.get(Input.UP_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.DOWN_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveUp());
        assertTrue(input.isMoveDown());
    }

    /**
     * This tests what happens when the input is either the up or the down button
     * for player 2.
     */
    @Test
    public void testIsMoveUpDownPlayerTwo() {
        input = new Input(mainController, 2);
        input.setKeyboardBitSet(keyboardBitSet);

        when(keyboardBitSet.get(Input.W_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.S_KEY.ordinal())).thenReturn(false);

        assertTrue(input.isMoveUp());
        assertFalse(input.isMoveDown());

        when(keyboardBitSet.get(Input.W_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.S_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveUp());
        assertTrue(input.isMoveDown());
    }

    /**
     * This tests when the input for up and down is counter intuitive.
     */
    @Test
    public void testIsMoveUpDownCounter() {
        when(keyboardBitSet.get(Input.UP_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.DOWN_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveUp());
        assertFalse(input.isMoveDown());

        when(keyboardBitSet.get(Input.UP_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.DOWN_KEY.ordinal())).thenReturn(false);

        assertFalse(input.isMoveUp());
        assertFalse(input.isMoveDown());
    }

    /**
     * This tests when the input for up and down is counter intuitive.
     */
    @Test
    public void testIsMoveUpDownCounterPlayerTwo() {
        input = new Input(mainController, 2);
        input.setKeyboardBitSet(keyboardBitSet);

        when(keyboardBitSet.get(Input.W_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.S_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveUp());
        assertFalse(input.isMoveDown());

        when(keyboardBitSet.get(Input.W_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.S_KEY.ordinal())).thenReturn(false);

        assertFalse(input.isMoveUp());
        assertFalse(input.isMoveDown());
    }

    /**
     * This tests what happens when the input is either the left or the right button.
     */
    @Test
    public void testIsMoveLeftRight() {
        when(keyboardBitSet.get(Input.LEFT_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.RIGHT_KEY.ordinal())).thenReturn(false);

        assertTrue(input.isMoveLeft());
        assertFalse(input.isMoveRight());

        when(keyboardBitSet.get(Input.LEFT_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.RIGHT_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveLeft());
        assertTrue(input.isMoveRight());
    }

    /**
     * This tests what happens when the input is either the left or the right button
     * for player two.
     */
    @Test
    public void testIsMoveLeftRightPlayerTwo() {
        input = new Input(mainController, 2);
        input.setKeyboardBitSet(keyboardBitSet);

        when(keyboardBitSet.get(Input.A_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.D_KEY.ordinal())).thenReturn(false);

        assertTrue(input.isMoveLeft());
        assertFalse(input.isMoveRight());

        when(keyboardBitSet.get(Input.A_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.D_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveLeft());
        assertTrue(input.isMoveRight());
    }

    /**
     * This test happens when the input right and left is counter intuitive.
     */
    @Test
    public void testIsMoveLeftRightCounter() {
        when(keyboardBitSet.get(Input.LEFT_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.RIGHT_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveLeft());
        assertFalse(input.isMoveRight());

        when(keyboardBitSet.get(Input.LEFT_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.RIGHT_KEY.ordinal())).thenReturn(false);

        assertFalse(input.isMoveLeft());
        assertFalse(input.isMoveRight());
    }

    /**
     * This test happens when the input right and left is counter intuitive.
     */
    @Test
    public void testIsMoveLeftRightCounterPlayerTwo() {
        input = new Input(mainController, 2);
        input.setKeyboardBitSet(keyboardBitSet);

        when(keyboardBitSet.get(Input.A_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.D_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveLeft());
        assertFalse(input.isMoveRight());

        when(keyboardBitSet.get(Input.A_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.D_KEY.ordinal())).thenReturn(false);

        assertFalse(input.isMoveLeft());
        assertFalse(input.isMoveRight());
    }

    /**
     * This tests if the bubbles can be shot.
     */
    @Test
    public void testIsFirePrimaryWeapon() {
        when(keyboardBitSet.get(Input.PRIMARY_WEAPON_KEY.ordinal())).thenReturn(true);
        assertTrue(input.isFirePrimaryWeapon());

        when(keyboardBitSet.get(Input.PRIMARY_WEAPON_KEY.ordinal())).thenReturn(false);
        assertFalse(input.isFirePrimaryWeapon());
    }

    /**
     * This tests if the bubbles can be shot.
     */
    @Test
    public void testIsFirePrimaryWeaponPlayerTwo() {
        input = new Input(mainController, 2);
        input.setKeyboardBitSet(keyboardBitSet);

        when(keyboardBitSet.get(Input.SHIFT_KEY.ordinal())).thenReturn(true);
        assertTrue(input.isFirePrimaryWeapon());

        when(keyboardBitSet.get(Input.SHIFT_KEY.ordinal())).thenReturn(false);
        assertFalse(input.isFirePrimaryWeapon());
    }


    /**
     * This tests if the bubbles can be shot as a secondary weapon.
     */
    @Test
    public void testIsFireSecondaryWeapon() {
        when(keyboardBitSet.get(Input.SECONDARY_WEAPON_KEY.ordinal())).thenReturn(true);
        assertTrue(input.isFireSecondaryWeapon());

        when(keyboardBitSet.get(Input.SECONDARY_WEAPON_KEY.ordinal())).thenReturn(false);
        assertFalse(input.isFireSecondaryWeapon());
    }

    /**
     * This tests if the listeners are added.
     */
    @Test
    public void testAddListeners() {
        input.addListeners();

        verify(mainController, atLeastOnce()).addListeners(
                KeyEvent.KEY_PRESSED, input.getKeyPressedEventHandler());
    }

    /**
     * This tests the keyPressedEventHandler.
     */
    @Test
    public void testKeyPressedEventHandler() {
        EventHandler<KeyEvent> handler = input.getKeyPressedEventHandler();
        handler.handle(new KeyEvent(null, null,
                null, "a", "a", KeyCode.A, false, false, false, false));
        verify(keyboardBitSet, atLeastOnce()).set((new KeyEvent(null, null,
                null, "a", "a", KeyCode.A, false, false, false, false)).getCode().ordinal(), true);
    }

    /**
     * This tests the keyReleasedEventHandler.
     */
    @Test
    public void testKeyReleasedEventHandler() {
        EventHandler<KeyEvent> handler = input.getKeyReleasedEventHandler();
        handler.handle(new KeyEvent(null, null,
                KeyEvent.KEY_RELEASED, "a", "a", KeyCode.A, false, false, false, false));
        verify(keyboardBitSet, atLeastOnce()).set((new KeyEvent(null, null,
                KeyEvent.KEY_RELEASED, "a", "a", KeyCode.A, false,
                false, false, false)).getCode().ordinal(), false);
    }

    /**
     * This function validates if mockito is correctly used.
     */
    @After
    public void validate() {
        validateMockitoUsage();
    }

}
