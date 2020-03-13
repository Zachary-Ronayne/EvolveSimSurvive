package sim.obj.tile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Graphics2D;
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
import evolve.sim.obj.tile.WaterTile;
import evolve.util.Camera;
import evolve.util.options.Settings;

public class WaterTileTest{
	
	private WaterTile t;
	
	private Simulation sim;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		sim = new Simulation();
		t = new WaterTile(0, 0, sim);
	}
	
	@Test
	public void testUpdate(){
		t.update();
	}
	
	@Test
	public void render(){
		BufferedImage buff = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D)buff.getGraphics();
		Camera cam = new Camera(100, 100);
		cam.setG(g);
		
		t.render(cam);
	}
	
	@Test
	public void testIsHazard(){
		assertTrue(t.isHazard());
	}
	
	@Test
	public void getFood(){
		assertTrue(t.getFood() == 0);
	}
	
	@Test
	public void eat(){
		assertTrue(t.eat() == 0);
		assertTrue(t.eat(new NeuralNetCreature(sim)) == 0);
	}
	
	@Test
	public void testGetMovement(){
		double tSize = Main.SETTINGS.tileSize.value();
		
		sim.getGrid()[0][0] = t;
		NeuralNetCreature c = new NeuralNetCreature(sim);
		c.setX(tSize * .5);
		c.setY(tSize * .5);
		sim.addCreature(c);

		
		t.setTemperature(-100);
		sim.updateAll();
		assertFalse(t.getMovement(c) == null);
		
		
		t.setTemperature(100);
		sim.updateAll();
		assertTrue(t.getMovement(c) == null);
	}
	
	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "/JUnitWaterTileSave.txt");
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
