package sim.obj.tile;

import static org.junit.Assert.assertEquals;
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
import evolve.sim.obj.Creature;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.tile.FoodTile;
import evolve.util.Camera;
import evolve.util.options.Settings;

public class FoodTileTest{

	public static final double DELTA = 0.00000001;
	
	private FoodTile t;
	private Simulation sim;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		sim = new Simulation();
		t = new FoodTile(0, 0, sim);
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
	public void testGetSetY(){
		t.setY(43);
		assertTrue(t.getY() == 43);
	}
	
	@Test
	public void testGetSetFood(){
		double max = Main.SETTINGS.foodMax.value();
		t.setFood(max / 2);
		assertEquals(t.getFood(), max / 2, DELTA);
	}
	
	@Test
	public void testAddFood(){
		double max = Main.SETTINGS.foodMax.value();
		t.setFood(max / 2);
		t.addFood(max / 4);
		assertEquals(t.getFood(), max * .75, DELTA);
	}
	
	@Test
	public void eat(){
		double food = t.getFood();
		t.eat();
		assertTrue(food > t.getFood());
		
		food = t.getFood();
		t.eat(new NeuralNetCreature(sim));
		assertTrue(food > t.getFood());
		
	}
	
	@Test
	public void testGetSetSpecies(){
		assertFalse(t.getSpecies() == null);
		t.setSpeciesAmount(.3);
		assertTrue(t.getSpecies().getSpeciesValue() == .3);
	}
	
	@Test
	public void testIsHazard(){
		assertFalse(t.isHazard());
	}
	
	@Test
	public void testGetEatPercent(){
		t.getSpecies().setSpeciesValue(0);
		
		Creature c = new Creature(sim){
			@Override
			public double calculateRadius(){
				return 10;
			}
			@Override
			public double getFur(){
				return 7;
			}
		};
		c.getSpecies().setSpeciesValue(1);
		t.getEatPercent(c);
	}
	
	@Test
	public void testGetMovement(){
		double tSize = Main.SETTINGS.tileSize.value();
		
		sim.getGrid()[0][0] = t;
		NeuralNetCreature c = new NeuralNetCreature(sim);
		c.setX(tSize * .5);
		c.setY(tSize * .5);
		sim.addCreature(c);
		
		sim.update();
		assertTrue(t.getMovement(c) == null);
	}
	
	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "/JUnitFoodTileSave.txt");
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
