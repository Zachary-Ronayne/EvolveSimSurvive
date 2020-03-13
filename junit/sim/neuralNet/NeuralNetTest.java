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
import evolve.sim.neuralNet.NeuralNet;
import evolve.util.Camera;

public class NeuralNetTest{
	
	private NeuralNet small;
	private NeuralNet medium;
	private NeuralNet large;
	
	@Before
	public void setUp(){
		small = new NeuralNet(new int[]{3, 4});
		medium = new NeuralNet(new int[]{6, 8, 5});
		large = new NeuralNet(new int[]{5, 9, 5, 8});
	}
	
	@Test
	public void testCalculateOutputs(){
		small.calculateOutputs();
		medium.calculateOutputs();
		large.calculateOutputs();
	}
	
	@Test
	public void testFeedInputs(){
		small.feedInputs(new double[small.getInputSize()]);
		medium.feedInputs(new double[small.getInputSize()]);
		large.feedInputs(new double[small.getInputSize()]);
	}
	
	@Test
	public void testGetOutputs(){
		small.getOutputs();
		medium.getOutputs();
		large.getOutputs();
	}
	
	@Test
	public void testGetInputSize(){
		assertTrue(small.getInputSize() == 3);
		assertTrue(medium.getInputSize() == 6);
		assertTrue(large.getInputSize() == 5);
	}
	
	@Test
	public void testGetOutputSize(){
		assertTrue(small.getOutputSize() == 4);
		assertTrue(medium.getOutputSize() == 5);
		assertTrue(large.getOutputSize() == 8);
	}
	
	@Test
	public void getLargestLayer(){
		assertTrue(small.getLargestLayer() == 4);
		assertTrue(medium.getLargestLayer() == 8);
		assertTrue(large.getLargestLayer() == 9);
	}
	
	@Test
	public void testGetNumberOfLayers(){
		assertTrue(small.getNumberOfLayers() == 2);
		assertTrue(medium.getNumberOfLayers() == 3);
		assertTrue(large.getNumberOfLayers() == 4);
	}
	
	@Test
	public void testSameSize(){
		assertTrue(small.sameSize(small.copy()));
		assertTrue(medium.sameSize(new NeuralNet(new int[]{6, 8, 5})));
		assertFalse(large.sameSize(small));
	}
	
	@Test
	public void testAddNode(){
		medium.addNode(medium.getLayers()[0][0], 1);
		assertTrue(medium.getLayers()[1].length == 9);
		for(int i = 0; i < medium.getOutputSize(); i++){
			assertTrue(medium.getLayers()[2][i].getConnections().length == 9);
		}
	}
	
	@Test
	public void testAddInputNode(){
		small.addInputNode(small.getLayers()[0][0]);
		assertTrue(small.getInputSize() == 4);
		for(int i = 0; i < small.getOutputSize(); i++){
			assertTrue(small.getLayers()[1][i].getConnections().length == 4);
		}
	}
	
	@Test
	public void testAddOutputNode(){
		try{
			large.addOutputNode(large.getLayers()[large.getNumberOfLayers() - 1][0]);
			assertTrue(large.getOutputSize() == 9);
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void testRemoveNode(){
		medium.removeNode(0, 1);
		assertTrue(medium.getLayers()[1].length == 7);
		for(int i = 0; i < medium.getOutputSize(); i++){
			assertTrue(medium.getLayers()[2][i].getConnections().length == 7);
		}
	}

	@Test
	public void testRemoveInputNode(){
		small.removeInputNode(0);
		assertTrue(small.getInputSize() == 2);
		for(int i = 0; i < small.getOutputSize(); i++){
			assertTrue(small.getLayers()[1][i].getConnections().length == 2);
		}
	}
	
	@Test
	public void testRemoveOutputNode(){
		try{
			large.removeOutputNode(0);
			assertTrue(large.getOutputSize() == 7);
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void testRender(){
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D)img.getGraphics();
		Camera cam = new Camera(100, 100);
		cam.setG(g);
		small.render(cam);
		medium.render(cam);
		large.render(cam);
	}
	
	@Test
	public void testMutate(){
		small.mutate(1, 0.0005);
		medium.mutate(-.1, 0.0005);
		large.mutate(2, 0.0005);
	}
	
	@Test
	public void testParentCopy(){
		assertFalse(small.parentCopy(small.copy()) == null);
		
		try{
			small.parentCopy(large);
			large.parentCopy(small);
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void testClone(){
		NeuralNet copy = large.copy();
		assertFalse(copy.equals(large));
	}
	
	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "NeuralNetSaveTest");
		try{
			PrintWriter write = new PrintWriter(loc);
			assertTrue(small.save(write));
			assertTrue(medium.save(write));
			assertTrue(large.save(write));
			write.close();
			
			Scanner read = new Scanner(loc);
			assertTrue(small.load(read));
			assertTrue(medium.load(read));
			assertTrue(large.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
	}
	
	@Test
	public void testGetValueColor(){
		NeuralNet.getValueColor(-1, false);
		NeuralNet.getValueColor(-1, true);
		NeuralNet.getValueColor(1, false);
		NeuralNet.getValueColor(1, true);
	}
	
}
