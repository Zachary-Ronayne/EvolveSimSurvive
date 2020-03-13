package sim.world;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.sim.world.River;

public class RiverTest{
	
	private River river;
	
	@Before
	public void setUp(){
		river = new River(0, 0, 1, 1, 0, 3, -2, 4, 200);
	}

	@Test
	public void testGetSize(){
		assertTrue(river.getSize() == 200);
	}
	
	@Test
	public void testSetSize(){
		river.setSize(300);
		assertTrue(river.getSize() == 300);
	}
	
}
