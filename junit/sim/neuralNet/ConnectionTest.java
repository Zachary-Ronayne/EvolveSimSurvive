package sim.neuralNet;

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
import evolve.sim.misc.Gene;
import evolve.sim.neuralNet.Connection;
import evolve.util.Camera;
import evolve.util.options.Settings;

public class ConnectionTest{
	
	public static final double DELTA = 0.000000001;
	
	private Connection connection;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		connection = new Connection(.4);
	}
	
	@Test
	public void testGetSetWeight(){
		Gene g = new Gene(.3);
		connection.setWeight(g);
		assertTrue(connection.getWeight().equals(g));
	}

	@Test
	public void testRender(){
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D)img.getGraphics();
		Camera cam = new Camera(100, 100);
		cam.setG(g);
		connection.render(cam, 0);
	}
	
	@Test
	public void testMutate(){
		connection.mutate(1, Main.SETTINGS.randomizeChance.value());
	}
	
	@Test
	public void testParentCopy(){
		assertFalse(connection.parentCopy(new Connection(.4)) == null);
	}
	
	@Test
	public void testClone(){
		Connection copy = connection.copy();
		assertFalse(copy.equals(connection));
		assertTrue(copy.getWeight().getValue() == connection.getWeight().getValue());
	}
	
	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "ConnectionSaveTest");
		try{
			PrintWriter write = new PrintWriter(loc);
			assertTrue(connection.save(write));
			write.close();
			
			Scanner read = new Scanner(loc);
			assertTrue(connection.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
	}
	
}
