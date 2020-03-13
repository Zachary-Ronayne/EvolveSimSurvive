package sim.misc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.misc.Gene;

public class GeneTest{
	
	public static final double DELTA = 0.00000001;
	
	private Gene defaultGene;
	private Gene gene;
	private Gene modloGene;
	private Gene randGene;
	
	@Before
	public void setUp(){
		defaultGene = new Gene(.4);
		gene = new Gene(2, 0, 3, 2);
		randGene = new Gene(0, 3, 2);
		modloGene = new Gene(0, 0, 1, 1);
		modloGene.setModulo(true);
	}

	@Test
	public void testMutate(){
		defaultGene.mutate(1, .01);
		gene.mutate(1, .01);
		randGene.mutate(1, .01);
	}
	
	@Test
	public void testPparentCopy(){
		double v1;
		double v2;
		double v;

		defaultGene.setModulo(false);
		defaultGene.setValue(.2);
		gene.setValue(.5);
		v1 = defaultGene.getValue();
		v2 = gene.getValue();
		
		for(int i = 0; i < 100; i++){
			v = defaultGene.parentCopy(gene).getValue();
			assertTrue(v1 < v);
			assertTrue(v < v2);
		}

		modloGene.setModulo(true);
		modloGene.setMax(1);
		modloGene.setValue(.4);
		gene.setValue(.5);
		v1 = modloGene.getValue();
		v2 = gene.getValue();
		
		for(int i = 0; i < 100; i++){
			v = modloGene.parentCopy(gene).getValue();
			assertTrue(v > v1);
			assertTrue(v < v2);
		}
		
		modloGene.setValue(.1);
		gene.setValue(.9);
		v1 = modloGene.getValue();
		v2 = gene.getValue();
		
		for(int i = 0; i < 100; i++){
			v = modloGene.parentCopy(gene).getValue();
			assertTrue(v < v1 || v > v2);
		}
	}
	
	@Test
	public void testGetSetValue(){
		gene.setValue(1);
		assertTrue(gene.getValue() == 1);

		modloGene.setModulo(true);
		modloGene.setMax(1);
		
		modloGene.setValue(0);
		modloGene.setValue(1.2);
		assertEquals(modloGene.getValue(), .2, DELTA);
		
		modloGene.setValue(0);
		modloGene.setValue(-1.2);
		assertEquals(modloGene.getValue(), .8, DELTA);
		
		modloGene.setValue(0);
		modloGene.setValue(99999);
		assertEquals(modloGene.getValue(), 0, DELTA);
		
		modloGene.setValue(0);
		modloGene.setValue(-99999);
		assertEquals(modloGene.getValue(), 0, DELTA);
	}

	@Test
	public void testGetSetMin(){
		gene.setMin(-10);
		assertTrue(gene.getMin() == -10);
	}

	@Test
	public void testGetSetMax(){
		gene.setMax(10);
		assertTrue(gene.getMax() == 10);
	}

	@Test
	public void testGetSetScalar(){
		gene.setScalar(1.3);
		assertTrue(gene.getScalar() == 1.3);
	}
	
	@Test
	public void testClone(){
		Gene copy = null;
		copy = gene.copy();
		assertFalse(copy.equals(gene));
		assertTrue(copy.getValue() == gene.getValue());
		assertTrue(copy.getMin() == gene.getMin());
		assertTrue(copy.getMax() == gene.getMax());
		assertTrue(copy.getScalar() == gene.getScalar());
	}

	@Test
	public void testSaveLoad(){
		double value = gene.getValue();
		double min = gene.getMin();
		double max = gene.getMax();
		double scalar = gene.getScalar();
		
		Gene newGene = new Gene(0);
		
		File loc = new File(Main.DATA_PATH + "GeneJUnitTest.txt");
		try{
			PrintWriter write = new PrintWriter(loc);
			assertTrue(gene.save(write));
			write.close();
			
			Scanner read = new Scanner(loc);
			assertTrue(newGene.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		assertTrue(value == newGene.getValue());
		assertTrue(min == newGene.getMin());
		assertTrue(max == newGene.getMax());
		assertTrue(scalar == newGene.getScalar());
		
		loc.delete();
	}
	
}
