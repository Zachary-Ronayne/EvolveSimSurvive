package sim.misc;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
import evolve.sim.misc.Eye;
import evolve.sim.misc.Gene;
import evolve.sim.obj.NeuralNetCreature;
import evolve.util.Camera;
import evolve.util.options.Settings;

public class EyeTest{
	
	public static final double DELTA = 0.0000001;
	
	private Eye eye;
	
	private Simulation sim;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();

		Gene min;
		Gene max;
		
		eye = new Eye();
		sim = new Simulation();
		
		min = new Gene(.2, -10000, 10000, 1);
		max = new Gene(.2, -10000, 10000, 1);
		eye.setMinDistance(min);
		eye.setMaxDistance(max);
		
		min = new Gene(.2, -10000, 10000, 1);
		max = new Gene(.7, -10000, 10000, 1);
		eye.setMinDistance(min);
		eye.setMaxDistance(max);
		eye.setDistance(0);
		

		min = new Gene(-1, -10000, 10000, 1);
		max = new Gene(-1, -10000, 10000, 1);
		eye.setMinAngle(min);
		eye.setMaxAngle(max);
		
		min = new Gene(-1, -10000, 10000, 1);
		max = new Gene(1.2, -10000, 10000, 1);
		eye.setMinAngle(min);
		eye.setMaxAngle(max);
		eye.setAngle(1);
	}

	@Test
	public void testGetMinDistance(){
		assertTrue(eye.getMinDistance().getValue() == .2);
	}
	
	@Test
	public void testSetMinDistance(){
		Gene min = new Gene(0, -10000, 10000, 1);
		Gene max = new Gene(.5, -10000, 10000, 1);
		eye.setMinDistance(min);
		eye.setMaxDistance(max);
		assertTrue(eye.getMinDistance() == min);
		assertTrue(eye.getMaxDistance() == max);
		
		min = new Gene(.6, -10000, 10000, 1);
		eye.setMinDistance(min);
		assertTrue(eye.getMinDistance().getValue() == .5);
		assertTrue(eye.getMaxDistance().getValue() == .6);
	}

	@Test
	public void testGetMaxDistance(){
		assertTrue(eye.getMaxDistance().getValue() == .7);
	}
	
	@Test
	public void testSetMaxDistance(){
		Gene min = new Gene(0, -10000, 10000, 1);
		Gene max = new Gene(.5, -10000, 10000, 1);
		eye.setMinDistance(min);
		eye.setMaxDistance(max);
		assertTrue(eye.getMinDistance() == min);
		assertTrue(eye.getMaxDistance() == max);
		
		max = new Gene(-.3, -10000, 10000, 1);
		eye.setMaxDistance(max);
		assertTrue(eye.getMinDistance().getValue() == -.3);
		assertTrue(eye.getMaxDistance().getValue() == .0);
	}

	@Test
	public void testGetDistance(){
		assertEquals(eye.getDistance(), .2, DELTA);
	}
	
	@Test
	public void testSetDistance(){
		eye.setDistance(0);
		assertEquals(eye.getDistance(), .2, DELTA);
		eye.setDistance(1);
		assertEquals(eye.getDistance(), .7, DELTA);
		
		eye.setDistance(-.1);
		assertEquals(eye.getDistance(), .2, DELTA);
		eye.setDistance(1.1);
		assertEquals(eye.getDistance(), .7, DELTA);
		
		eye.setDistance(.5);
		assertEquals(eye.getDistance(), .45, DELTA);
	}

	@Test
	public void testGetMinAngle(){
		assertTrue(eye.getMinAngle().getValue() == -1);
	}

	@Test
	public void testSetMinAngle(){
		Gene min = new Gene(-.2, -10000, 10000, 1);
		Gene max = new Gene(2, -10000, 10000, 1);
		eye.setMinAngle(min);
		eye.setMaxAngle(max);
		assertTrue(eye.getMinAngle() == min);
		assertTrue(eye.getMaxAngle() == max);
		
		min = new Gene(2.1, -10000, 10000, 1);
		eye.setMinAngle(min);
		assertTrue(eye.getMinAngle().getValue() == 2);
		assertTrue(eye.getMaxAngle().getValue() == 2.1);
	}

	@Test
	public void testGetMaxAngle(){
		assertTrue(eye.getMaxAngle().getValue() == 1.2);
	}

	@Test
	public void testSetMaxAngle(){
		Gene min = new Gene(-.2, -10000, 10000, 1);
		Gene max = new Gene(2, -10000, 10000, 1);
		eye.setMinAngle(min);
		eye.setMaxAngle(max);
		assertTrue(eye.getMinAngle() == min);
		assertTrue(eye.getMaxAngle() == max);
		
		max = new Gene(-1, -10000, 10000, 1);
		eye.setMaxAngle(max);
		assertTrue(eye.getMinAngle().getValue() == -1);
		assertTrue(eye.getMaxAngle().getValue() == -.2);
	}

	@Test
	public void testGetAngle(){
		assertTrue(eye.getAngle() == 1.2);
	}
	
	@Test
	public void testSetAngle(){
		eye.setAngle(-1);
		assertEquals(eye.getAngle(), -1, DELTA);
		eye.setAngle(1);
		assertEquals(eye.getAngle(), 1.2, DELTA);
		
		eye.setAngle(-1.1);
		assertEquals(eye.getAngle(), -1, DELTA);
		eye.setAngle(1.1);
		assertEquals(eye.getAngle(), 1.2, DELTA);

		eye.setAngle(0);
		assertNotEquals(eye.getAngle(), 0, DELTA);

		Gene min = new Gene(-2.2, -10000, 10000, 1);
		Gene max = new Gene(2.2, -10000, 10000, 1);
		eye.setMinAngle(min);
		eye.setMaxAngle(max);
		eye.setAngle(0);
		assertEquals(eye.getAngle(), 0, DELTA);

	}
	
	@Test
	public void testGetSeenDistance(){
		eye.look(sim, new NeuralNetCreature(sim));
		eye.getSeenDistance();
	}

	@Test
	public void getSeenQuantities(){
		eye.look(sim, new NeuralNetCreature(sim));
		eye.getSeenQuantities();
	}
	
	@Test
	public void testLook(){
		eye.look(sim, new NeuralNetCreature(sim));
	}
	
	@Test
	public void testGetBounds(){
		eye.getBounds(new NeuralNetCreature(sim));
	}
	
	@Test
	public void testGetSeenBounds(){
		NeuralNetCreature c = new NeuralNetCreature(sim);
		eye.look(sim, c);
		eye.getSeenBounds(c);
	}
	
	@Test
	public void testRender(){
		BufferedImage buff = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D)buff.getGraphics();
		Camera cam = new Camera(100, 100);
		cam.setG(g);

		NeuralNetCreature c = new NeuralNetCreature(sim);
		
		eye.render(cam, c);
	}

	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "/JUnitEyeTest.txt");
		try{
			//test save
			PrintWriter write = new PrintWriter(loc);
			assertTrue(eye.save(write));
			write.close();
			
			//test load
			Scanner read = new Scanner(loc);
			assertTrue(eye.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
	}
}
