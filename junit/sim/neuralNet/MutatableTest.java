package sim.neuralNet;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import evolve.sim.neuralNet.Mutateable;

public class MutatableTest{
	
	public static final double DELTA = 0.0000001;
	
	@Test
	public void testMutateValue(){
		double value = Mutateable.mutateValue(2);
		assertTrue(value >= -1 && value <= 1);
		
		value = Mutateable.mutateValue(-34, 3, -1, 1);
		assertTrue(value >= -1 && value <= 1);
	}
	
	@Test
	public void parentCopyValue(){
		double value = Mutateable.parentCopyValue(3, 7);
		assertTrue(value >= 3);
		assertTrue(value <= 7);
	}

	@Test
	public void testCompareModular(){
		double compare;
		
		compare = Mutateable.compareModular(.4, .5, 1);
		assertEquals(compare, .1, DELTA);
		compare = Mutateable.compareModular(.5, .4, 1);
		assertEquals(compare, .1, DELTA);

		compare = Mutateable.compareModular(.1, .9, 1);
		assertEquals(compare, .2, DELTA);
		compare = Mutateable.compareModular(.9, .1, 1);
		assertEquals(compare, .2, DELTA);

		compare = Mutateable.compareModular(.1, .9, 2);
		assertEquals(compare, .8, DELTA);
		compare = Mutateable.compareModular(.9, .1, 2);
		assertEquals(compare, .8, DELTA);

		compare = Mutateable.compareModular(0, 1, 1);
		assertEquals(compare, 0, DELTA);
		compare = Mutateable.compareModular(1, 0, 1);
		assertEquals(compare, 0, DELTA);

		compare = Mutateable.compareModular(.5, 1, 1);
		assertEquals(compare, .5, DELTA);
		compare = Mutateable.compareModular(1, .5, 1);
		assertEquals(compare, .5, DELTA);

		compare = Mutateable.compareModular(.55, .55, 1);
		assertEquals(compare, 0, DELTA);
	}
	
}
