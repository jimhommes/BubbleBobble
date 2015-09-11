package model;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import junit.framework.TestCase;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.security.Key;
import java.util.BitSet;

import static org.mockito.Mockito.*;

/**
 * Created by toinehartman on 11/09/15.
 */
public class InputTest extends TestCase {
    private static BitSet keyboardBitSet;
    @Mock private static Scene scene;
    @InjectMocks static Input input;

    public void setUp() {
        input = new Input(scene);
        keyboardBitSet = mock(BitSet.class);
        input.setKeyboardBitSet(keyboardBitSet);
    }

    public void testInput() {
        assertNotNull(input);
    }

    public void testIsMoveUpDown() {
        when(keyboardBitSet.get(Input.UP_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.DOWN_KEY.ordinal())).thenReturn(false);

        assertTrue(input.isMoveUp());
        assertFalse(input.isMoveDown());

        when(keyboardBitSet.get(Input.UP_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.DOWN_KEY.ordinal())).thenReturn(false);

        assertFalse(input.isMoveUp());
        assertFalse(input.isMoveDown());

        when(keyboardBitSet.get(Input.UP_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.DOWN_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveUp());
        assertTrue(input.isMoveDown());

        when(keyboardBitSet.get(Input.UP_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.DOWN_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveUp());
        assertFalse(input.isMoveDown());
    }

    public void testIsMoveLeftRight() {
        when(keyboardBitSet.get(Input.LEFT_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.RIGHT_KEY.ordinal())).thenReturn(false);

        assertTrue(input.isMoveLeft());
        assertFalse(input.isMoveRight());

        when(keyboardBitSet.get(Input.LEFT_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.RIGHT_KEY.ordinal())).thenReturn(false);

        assertFalse(input.isMoveLeft());
        assertFalse(input.isMoveRight());

        when(keyboardBitSet.get(Input.LEFT_KEY.ordinal())).thenReturn(false);
        when(keyboardBitSet.get(Input.RIGHT_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveLeft());
        assertTrue(input.isMoveRight());

        when(keyboardBitSet.get(Input.LEFT_KEY.ordinal())).thenReturn(true);
        when(keyboardBitSet.get(Input.RIGHT_KEY.ordinal())).thenReturn(true);

        assertFalse(input.isMoveLeft());
        assertFalse(input.isMoveRight());
    }

    public void testIsFirePrimaryWeapon() {
        when(keyboardBitSet.get(Input.PRIMARY_WEAPON_KEY.ordinal())).thenReturn(true);
        assertTrue(input.isFirePrimaryWeapon());

        when(keyboardBitSet.get(Input.PRIMARY_WEAPON_KEY.ordinal())).thenReturn(false);
        assertFalse(input.isFirePrimaryWeapon());
    }

    public void testIsFireSecondaryWeapon() {
        when(keyboardBitSet.get(Input.SECONDARY_WEAPON_KEY.ordinal())).thenReturn(true);
        assertTrue(input.isFireSecondaryWeapon());

        when(keyboardBitSet.get(Input.SECONDARY_WEAPON_KEY.ordinal())).thenReturn(false);
        assertFalse(input.isFireSecondaryWeapon());
    }

}
