package sim.misc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import evolve.sim.misc.Gene;
import evolve.sim.misc.Species;

public class SpeciesTest{
	
	public static final double DELTA = 0.000001;
	
	private Species species;
	private Species speciesRand;
	
	@Before
	public void setUp(){
		species = new Species(.5);
		speciesRand = new Species();
	}
	
	@Test
	public void testGetSetSpeciesGene(){
		Gene gene = new Gene(0);
		species.setSpeciesGene(gene);
		assertTrue(species.getSpeciesGene().equals(gene));
	}
	
	@Test
	public void testGetSetSpeciesValue(){
		speciesRand.setSpeciesValue(.2);
		assertTrue(speciesRand.getSpeciesValue() == .2);
	}
	
	@Test
	public void approachSpecies(){
		double s;
		for(int i = 0; i < 1000; i++){
			species.setSpeciesValue(.5);
			speciesRand.setSpeciesValue(.4);
			species.approachSpecies(speciesRand, 1);
			s = species.getSpeciesValue();
			assertTrue(s <= .5);
			assertTrue(s >= .4);
			
			species.setSpeciesValue(.4);
			speciesRand.setSpeciesValue(.5);
			species.approachSpecies(speciesRand, 1);
			s = species.getSpeciesValue();
			assertTrue(s <= .5);
			assertTrue(s >= .4);
			
			species.setSpeciesValue(.3);
			speciesRand.setSpeciesValue(.75);
			species.approachSpecies(speciesRand, 1);
			s = species.getSpeciesValue();
			assertTrue(s <= .75);
			assertTrue(s >= .3);
			
			species.setSpeciesValue(.4001);
			speciesRand.setSpeciesValue(.4);
			species.approachSpecies(speciesRand, 10000);
			s = species.getSpeciesValue();
			assertTrue(s <= .401);
			assertTrue(s >= .4);

			species.setSpeciesValue(.1);
			speciesRand.setSpeciesValue(.9);
			species.approachSpecies(speciesRand, 1);
			s = species.getSpeciesValue();
			assertTrue(s <= .1 || s >= .9);
			
			species.setSpeciesValue(.9);
			speciesRand.setSpeciesValue(.1);
			species.approachSpecies(speciesRand, 1);
			s = species.getSpeciesValue();
			assertTrue(s <= .1 || s >= .9);
			
			species.setSpeciesValue(.00000001);
			speciesRand.setSpeciesValue(.9999999);
			species.approachSpecies(speciesRand, 1000);
			s = species.getSpeciesValue();
			assertTrue(s <= .00000001 || s >= .9999999);
		}
	}
	
	@Test
	public void testGetHue(){
		species.getHue();
	}
	
	@Test
	public void testCompareSpecies(){
		double compare;
		
		species.setSpeciesValue(.5);
		speciesRand.setSpeciesValue(.6);
		compare = species.compareSpecies(speciesRand);
		assertEquals(compare, .1, DELTA);
		compare = speciesRand.compareSpecies(species);
		assertEquals(compare, .1, DELTA);
		
		species.setSpeciesValue(.1);
		speciesRand.setSpeciesValue(.9);
		compare = species.compareSpecies(speciesRand);
		assertEquals(compare, .2, DELTA);
		compare = speciesRand.compareSpecies(species);
		assertEquals(compare, .2, DELTA);
		
		species.setSpeciesValue(0);
		speciesRand.setSpeciesValue(1);
		compare = species.compareSpecies(speciesRand);
		assertEquals(compare, 0, DELTA);
		compare = speciesRand.compareSpecies(species);
		assertEquals(compare, 0, DELTA);
		
		species.setSpeciesValue(.5);
		speciesRand.setSpeciesValue(1);
		compare = species.compareSpecies(speciesRand);
		assertEquals(compare, .5, DELTA);
		compare = speciesRand.compareSpecies(species);
		assertEquals(compare, .5, DELTA);
		
		species.setSpeciesValue(.55);
		speciesRand.setSpeciesValue(.55);
		compare = species.compareSpecies(speciesRand);
		assertEquals(compare, 0, DELTA);
		compare = speciesRand.compareSpecies(species);
		assertEquals(compare, 0, DELTA);
	}
	
	@Test
	public void testGetColor(){
		assertFalse(species.getColor(.3f, .4f) == null);
	}
	
	@Test
	public void testClone(){
		Species copy = null;
		copy = species.copy();
		assertFalse(copy.equals(species));
		assertFalse(copy.getSpeciesGene().equals(species.getSpeciesGene()));
	}
}
