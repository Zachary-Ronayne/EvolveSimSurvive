package sim.obj.tile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.tile.FoodTile;
import evolve.sim.obj.tile.Tile;
import evolve.util.Camera;
import evolve.util.options.Settings;

public class TileTest{
	
	public static final double DELTA = 0.00000001;
	
	private Tile t;
	private Simulation sim;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		sim = new Simulation();
		t = new FoodTile(0, 1, sim);
	}
	
	@Test
	public void testGetSimulation(){
		assertFalse(t.getSimulation() == null);
	}
	
	@Test
	public void testGetTemperature(){
		assertFalse(t.getTemperature() == null);
	}
	
	@Test
	public void testSetTemperature(){
		t.setTemperature(3);
		assertTrue(t.getTemperature().getTemp() == 3);
	}
	
	@Test
	public void testCacheDataUpdate(){
		t.cacheData();
		t.update();
	}
	
	@Test
	public void testRender(){
		BufferedImage buff = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D)buff.getGraphics();
		Camera cam = new Camera(100, 100);
		cam.setG(g);
		
		t.render(cam);
	}
	
	@Test
	public void testGetAddContainingCreature(){
		NeuralNetCreature c = new NeuralNetCreature(sim);
		t.addContainingCreature(c);
		assertTrue(t.getContainingCreatures().contains(c));
	}
	
	@Test
	public void testGetX(){
		assertTrue(t.getX() == 0);
	}
	
	@Test
	public void testSetX(){
		t.setX(3);
		assertTrue(t.getX() == 3);
	}
	
	@Test
	public void testGetCenterX(){
		Main.SETTINGS.tileSize.setValue(100.0);
		assertTrue(t.getCenterX() == 50.0);
		
		t.setX(3);
		assertTrue(t.getCenterX() == 350.0);
	}

	@Test
	public void testGetY(){
		assertTrue(t.getY() == 1);
	}
	
	@Test
	public void testSetY(){
		t.setY(5);
		assertTrue(t.getY() == 5);
	}

	@Test
	public void testGetCenterY(){
		Main.SETTINGS.tileSize.setValue(100.0);
		assertTrue(t.getCenterY() == 150.0);
		
		t.setY(2);
		assertTrue(t.getCenterY() == 250.0);
	}
	
	@Test
	public void testGetCenter(){
		Main.SETTINGS.tileSize.setValue(100.0);
		Point2D.Double p = t.getCenter();

		assertTrue(p.x == 50);
		assertTrue(p.y == 150);
	}
	
	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "/JUnitTileSave.txt");
		try{
			//test save
			PrintWriter write = new PrintWriter(loc);
			assertTrue(t.save(write));
			write.close();
			
			//test load
			Scanner read = new Scanner(loc);
			assertTrue(t.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
	}
}
