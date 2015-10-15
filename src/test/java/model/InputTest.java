package model;

import javafx.scene.Scene;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.BitSet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InputTest {
    private BitSet keyboardBitSet;
    @Mock private Scene scene;
    @InjectMocks private Input input;

    /**
     * This is run before all the tests to initialize them.
     */
    @Before
    public void setUp() {
        input = new Input(scene, 1);
        keyboardBitSet = mock(BitSet.class);
        input.setKeyboardBitSet(keyboardBitSet);
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
     * This tests if the bubbles can be shot as a secondary weapon.
     */
    @Test
    public void testIsFireSecondaryWeapon() {
        when(keyboardBitSet.get(Input.SECONDARY_WEAPON_KEY.ordinal())).thenReturn(true);
        assertTrue(input.isFireSecondaryWeapon());

        when(keyboardBitSet.get(Input.SECONDARY_WEAPON_KEY.ordinal())).thenReturn(false);
        assertFalse(input.isFireSecondaryWeapon());
    }

}
