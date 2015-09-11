package model;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import junit.framework.TestCase;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.BitSet;

import static org.mockito.Mockito.*;

/**
 * Created by toinehartman on 11/09/15.
 */
public class InputTest extends TestCase {
    @Mock private static Scene scene;
    @InjectMocks static Input input;

    public void setUp() {
        input = new Input(scene);
    }

    public void testInput() {
        assertNotNull(input);
    }
}
