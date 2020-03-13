package sim.neuralNet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import evolve.sim.neuralNet.Node;
import evolve.util.Camera;
import evolve.util.options.Settings;

public class NodeTest{
	
	public static final double DELTA = 0.00000001;
	
	private Node node;
	private Node random;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		node = new Node(new double[]{.4, .6, -1, 0}, .3);
		random = new Node(7);
	}
	
	@Test
	public void testGetSetValue(){
		node.setValue(.4);
		assertEquals(node.getValue(), .4, DELTA);
		
		node.setValue(-2);
		assertEquals(node.getValue(), -1, DELTA);
	}
	
	@Test
	public void testCalculateValue(){
		Node[] nodes = new Node[4];
		for(int i = 0; i < nodes.length; i++) nodes[i] = new Node(10);
		node.calculateValue(nodes);
		
		nodes = new Node[7];
		for(int i = 0; i < nodes.length; i++) nodes[i] = new Node(10);
		random.calculateValue(nodes);
	}
	
	@Test
	public void testGetConnections(){
		assertFalse(node.getConnections() == null);
		assertFalse(random.getConnections() == null);
	}
	
	@Test
	public void testRemoveConnection(){
		assertTrue(node.getConnections().length == 4);
		node.removeConnection(0);
		assertTrue(node.getConnections().length == 3);
		Connection[] cons = node.getConnections();
		assertTrue(cons[0].getWeight().getValue() == .6);
		assertTrue(cons[1].getWeight().getValue() == -1);
		assertTrue(cons[2].getWeight().getValue() == 0);
		
		node.removeAllConnections();
		assertTrue(node.getConnections().length == 0);
		
		try{
			node.removeConnection(0);
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void testRemoveAllConnections(){
		assertTrue(node.getConnections().length == 4);
		node.removeAllConnections();
		assertTrue(node.getConnections().length == 0);
	}
	
	@Test
	public void testAddConnection(){
		assertTrue(node.getConnections().length == 4);
		node.addConnection();
		assertTrue(node.getConnections().length == 5);
		
		node.removeAllConnections();
		assertTrue(node.getConnections().length == 0);
		
		try{
			node.addConnection();
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			assertFalse(true);
		}

		assertTrue(node.getConnections().length == 1);
	}
	
	@Test
	public void testGetSetBias(){
		Gene g = new Gene(.2, -1, 1, 1);
		node.setBias(g);
		assertTrue(node.getBias().equals(g));
	}
	
	@Test
	public void testRender(){
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D)img.getGraphics();
		Camera cam = new Camera(100, 100);
		cam.setG(g);
		node.render(cam, 0);
		node.renderNode(cam);
		node.renderConnections(cam, 0);
	}
	
	@Test
	public void testMutate(){
		node.mutate(1, Main.SETTINGS.randomizeChance.value());
		random.mutate(-.3, Main.SETTINGS.randomizeChance.value());
	}
	
	@Test
	public void testParentCopy(){
		assertFalse(node.parentCopy(new Node(node.getConnections().length)) == null);
	}
	
	@Test
	public void testCopy(){
		Node copy = node.copy();
		assertFalse(copy.equals(node));
		assertTrue(copy.getBias().getValue() == node.getBias().getValue());
	}
	
	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "NodeSaveTest");
		try{
			PrintWriter write = new PrintWriter(loc);
			assertTrue(node.save(write));
			assertTrue(random.save(write));
			write.close();
			
			Scanner read = new Scanner(loc);
			assertTrue(node.load(read));
			assertTrue(random.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
	}
	
	@Test
	public void testGetRandomWeights(){
		Node.getRandomWeights(4);
	}
	
}
