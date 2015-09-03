
import org.junit.Before;

/**
 * Created by Jim on 9/3/2015.
 */
public class StartControllerTest {

    static StartController startController;

    @Before
    public void setUp() throws Exception {
        startController = new StartController();
    }

//    @Test
//    public void testStartButton() throws Exception {
//        assertEquals(startController.getRoot().isVisible(), true);
//        startController.getStartButton().fire();
//        assertEquals(startController.getRoot().isVisible(), false);
//    }
//
//    @Test
//    public void testHelpButton() throws Exception {
//        startController.setHelpScreen(new GridPane());
//        startController.getHelpButton().fire();
//        System.out.println("Hoi");
//        assertEquals(startController.getHelpScreen().isVisible(), true);
//        System.out.println("Hoi");
//        startController.getHelpButton().fire();
//        System.out.println("Hoi");
//        assertEquals(startController.getHelpScreen().isVisible(), false);
//    }
}