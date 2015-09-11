package model;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WalkerTest {

	private static Walker walker;
	private static Monster monster;
	
	
	@BeforeClass
	public static void before() {
		monster = mock(Monster.class);
		walker = new Walker(0, 0, 0, 10, 0, 0, 10, true);
	}
	
	@Test
	public void testSwitchingDirectionFalse() {
		walker.switchDirection();
		assertEquals(false, walker.isFacingRight());
	}
	
	@Test
	public void testSwitchingDirectionTrue() {
		Walker walker2 = new Walker(0, 0, 0, 10, 0, 0, 10, false);
		walker2.switchDirection();
		assertEquals(true, walker2.isFacingRight());
	}

}
