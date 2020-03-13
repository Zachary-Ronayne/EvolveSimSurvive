package sim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.UserCreature;
import evolve.sim.obj.tile.Tile;
import evolve.util.Camera;
import evolve.util.options.Settings;

public class SimulationTest{
	
	public static final double DELTA = 0.000001;
	
	private Simulation sim;
	private Camera cam;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		sim = new Simulation();
		cam = sim.getCamera();
		sim.setLockCamera(false);
	}
	
	@Test
	public void testNextCreatureId(){
		long id = sim.nextCreatureId();
		assertTrue(id + 1 == sim.nextCreatureId());
	}

	@Test
	public void testGetCamera(){
		assertTrue(sim.getCamera() == cam);
	}
	
	@Test
	public void testSetCamera(){
		Camera c = new Camera(100, 100);
		sim.setCamera(c);
		assertTrue(sim.getCamera() == c);
	}
	
	@Test
	public void testGetLockCamera(){
		assertFalse(sim.getLockCamera());
	}

	@Test
	public void testSetLockCamera(){
		sim.setLockCamera(true);
		assertTrue(sim.getLockCamera());
	}
	
	@Test
	public void testGetUserCreature(){
		assertTrue(sim.getUserCreature() == null);
	}
	
	@Test
	public void testSetUserCreature(){
		UserCreature c = new UserCreature(sim);
		sim.setUserCreature(c);
		assertTrue(sim.getUserCreature() == c);
	}
	
	@Test
	public void testReviveUserCreature(){
		sim.reviveUserCreature();
		assertFalse(sim.getUserCreature().isDead());
		
		sim.setUserCreature(null);
		assertTrue(sim.getUserCreature() == null);
		
		sim.reviveUserCreature();
		assertFalse(sim.getUserCreature().isDead());
		
		sim.getUserCreature().kill();
		assertTrue(sim.getUserCreature().isDead());
		
		sim.reviveUserCreature();
		assertFalse(sim.getUserCreature().isDead());
	}

	@Test
	public void testAddCreature(){
		sim.addCreature(new NeuralNetCreature(sim));
	}
	
	@Test
	public void testSetSelectedCreatureGlow(){
		NeuralNetCreature creature = sim.getEvolvingCreature();
		sim.setSelectedCreatureGlow(creature);
		assertFalse(creature.getGlow() == null);
		
		sim.setSelectedCreatureGlow(null);
		sim.setSelectedCreatureGlow(creature);
	}
	
	@Test
	public void testSetHoveredCreature(){
		NeuralNetCreature creature = sim.getEvolvingCreature();
		
		sim.setSelectedCreatureGlow(creature);
		sim.setHoveredCreature(null);
		sim.setHoveredCreature(creature);
		
		sim.setSelectedCreatureGlow(null);
		sim.setHoveredCreature(null);
		sim.setHoveredCreature(creature);
	}
	
	@Test
	public void testRemoveGlow(){
		sim.removeGlow();
		assertTrue(sim.getEvolvingCreature().getGlow() == null);
	}

	@Test
	public void testGetSelectedGlowCreature(){
		sim.getSelectedGlowCreature();
	}

	@Test
	public void testGetEvolvingCreature(){
		sim.addCreature(new NeuralNetCreature(sim));
		assertFalse(sim.getEvolvingCreature() == null);
	}

	@Test
	public void testGetEvolvingCreatures(){
		assertFalse(sim.getEvolvingCreatures() == null);
	}
	
	@Test
	public void testGetPopulation(){
		sim.getPopulation();
	}

	@Test
	public void testGetSetPopulationData(){
		assertFalse(sim.getPopulationData() == null);
		
		ArrayList<Double[]> data = new ArrayList<Double[]>();
		data.add(new Double[]{1.0});
		
		sim.setPopulationData(data);
		assertTrue(sim.getPopulationData().equals(data));
	}
	
	@Test
	public void testGetSetMutabilityData(){
		assertFalse(sim.getMutabilityData() == null);
		
		ArrayList<Double[]> data = new ArrayList<Double[]>();
		data.add(new Double[]{1.0, 2.0, 4.6});
		
		sim.setMutabilityData(data);
		assertTrue(sim.getMutabilityData().equals(data));
	}
	
	@Test
	public void testGetSetAgeData(){
		assertFalse(sim.getAgeData() == null);
		
		ArrayList<Double[]> data = new ArrayList<Double[]>();
		data.add(new Double[]{1.0, 2.0, 4.6});
		
		sim.setAgeData(data);
		assertTrue(sim.getAgeData().equals(data));
	}
	
	@Test
	public void testGetNearestCreature(){
		sim.getNearestCreature(0, 0);
		sim.getNearestCreature(0, 0, null);
		sim.getNearestCreature(0, 0, sim.getEvolvingCreature());
		sim.getNearestCreature(sim.getEvolvingCreature());
		Simulation.getNearestCreature(sim.getEvolvingCreatures(), 0, 0, sim.getEvolvingCreature());
	}

	@Test
	public void testGetGrid(){
		sim.getGrid();
	}
	
	@Test
	public void testSetGridSize(){
		sim.setGridSize(1, 1);
		Tile[][] grid = sim.getGrid();
		assertTrue(grid.length == 1);
		assertTrue(grid[0].length == 1);
		for(Tile[] tt : grid) for(Tile t : tt) assertFalse(t == null);

		sim.setGridSize(5, 1);
		grid = sim.getGrid();
		assertTrue(grid.length == 5);
		assertTrue(grid[0].length == 1);
		for(Tile[] tt : grid) for(Tile t : tt) assertFalse(t == null);

		sim.setGridSize(6, 8);
		grid = sim.getGrid();
		assertTrue(grid.length == 6);
		assertTrue(grid[0].length == 8);
		for(Tile[] tt : grid) for(Tile t : tt) assertFalse(t == null);
	}
	
	@Test
	public void testSetGrid(){
		Tile[][] g = new Tile[5][5];
		sim.setGrid(g);
		assertTrue(sim.getGrid() == g);
	}

	@Test
	public void testGetTotalTime(){
		assertTrue(sim.getTotalTime() == 0);
	}

	@Test
	public void testSetTotalTime(){
		sim.setTotalTime(1003);
		assertTrue(sim.getTotalTime() == 1003);
	}
	
	@Test
	public void testGetTemperatureValue(){
		sim.getTemperature().setTemp(3);
		assertEquals(sim.getTemperatureValue(), 3, DELTA);
	}

	@Test
	public void testGetTemperature(){
		sim.getTemperatureValue();
		sim.getTemperature();
	}
	
	@Test
	public void testGetWorldTemperature(){
		sim.getWorldTemperature(0);
	}
	
	@Test
	public void testGetContainingTile(){
		double size = Main.SETTINGS.tileSize.value();
		
		Tile t = sim.getContainingTile(size, size);
		assertFalse(t == null);
		assertTrue(t.getX() == 1);
		assertTrue(t.getY() == 1);

		t = sim.getContainingTile(size * 2.5, size);
		assertFalse(t == null);
		assertTrue(t.getX() == 2);
		assertTrue(t.getY() == 1);
		
		t = sim.getContainingTile(-size, size);
		assertTrue(t == null);
	}
	
	@Test
	public void testUpdateTemperature(){
		double temp = sim.getTemperatureValue();
		sim.updateAll();
		sim.updateTemperature();
		assertFalse(temp == sim.getTemperatureValue());
	}

	@Test
	public void testUpdateAll(){
		sim.updateAll();
	}

	@Test
	public void testUpdate(){
		sim.update();
	}

	@Test
	public void testUpdateObjects(){
		sim.updateObjects();
	}

	@Test
	public void testUpdateTiles(){
		sim.updateTiles(0, 1);
		sim.updateTiles(0, 1000000);
	}

	@Test
	public void testCacheCreatures(){
		sim.cacheCreatures(0, 1);
		sim.cacheCreatures(0, 1000000);
	}
	
	@Test
	public void testThinkCreatures(){
		sim.thinkCreatures(0, 1);
		sim.thinkCreatures(0, 1000000);
	}
	
	@Test
	public void testUpdateCreatures(){
		sim.updateCreatures(0, 1);
		sim.updateCreatures(0, 1000000);
	}
	
	@Test
	public void testUpdateData(){
		sim.updateData();
	}

	@Test
	public void testRender(){
		BufferedImage buff = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D)buff.getGraphics();
		sim.render(g);
	}
	
	@Test
	public void testSaveLoad(){
		Main.SETTINGS.tilesX.setValue(10);
		Main.SETTINGS.tilesY.setValue(10);
		sim = new Simulation();
		
		File loc = new File(Main.DATA_PATH + "/JUnitTestSim.txt");
		try{
			//test save
			PrintWriter write = new PrintWriter(loc);
			assertTrue(sim.save(write));
			write.close();
			
			//test load
			Scanner read = new Scanner(loc);
			assertTrue(sim.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
	}
}
