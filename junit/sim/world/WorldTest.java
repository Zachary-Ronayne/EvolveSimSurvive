package sim.world;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.world.World;
import evolve.util.options.Settings;

public class WorldTest{
	
	private World world;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		world = new World(3);
	}
	
	@Test
	public void testSetGetSeed(){
		world.setSeed(0);
		assertTrue(world.getSeed() == 0);
	}
	
	@Test
	public void testGenerate(){
		world.generate(new Simulation());
	}

	@Test
	public void testGenerateTile(){
		world.generateTile(0, 0, new Simulation());
	}

	@Test
	public void testGenerateFoodTile(){
		world.generateFoodTile(0, 0, new Simulation());
	}
	
	@Test
	public void testIsIsland(){
		world.isIsland(0, 0);
	}
	
	@Test
	public void testIsInRiver(){
		world.isInRiver(0, 0);
	}
}
