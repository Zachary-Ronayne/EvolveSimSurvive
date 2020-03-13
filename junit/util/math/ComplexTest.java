package util.math;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import evolve.util.math.Complex;

public class ComplexTest{
	
	public static final double DELTA = 0.000000001;
	
	private Complex c;
	
	@Before
	public void setUp(){
		c = new Complex(3, -4);
	}
	
	@Test
	public void testConstructor(){
		Complex c = new Complex();
		assertTrue(c.getR() == 0);
		assertTrue(c.getI() == 0);
	}
	
	@Test
	public void testAdd(){
		c.add(new Complex(1, 2));
		assertTrue(c.getR() == 4);
		assertTrue(c.getI() == -2);
	}
	
	@Test
	public void testSub(){
		c.sub(new Complex(1, 2));
		assertTrue(c.getR() == 2);
		assertTrue(c.getI() == -6);
	}

	@Test
	public void testPow(){
		Complex copy = c.copy();
		copy.pow(2);

		assertEquals(copy.getR(), -7, DELTA);
		assertEquals(copy.getI(), -24, DELTA);
		
		copy = c.copy();
		copy.pow(4);
		
		assertEquals(copy.getR(), -527, DELTA);
		assertEquals(copy.getI(), 336, DELTA);
	}
	
	@Test
	public void testMult(){
		Complex copy = c.copy();
		
		c.mult(new Complex(2, 5));

		assertTrue(c.getR() == 26);
		assertTrue(c.getI() == 7);
		
		copy.mult(4);
		assertEquals(copy.getR(), 12, DELTA);
		assertEquals(copy.getI(), -16, DELTA);
	}
	
	@Test
	public void testDiv(){
		Complex copy = c.copy();
		
		c.div(new Complex(8, 7));
		assertEquals(c.getR(), -0.0353982300884955752212389, DELTA);
		assertEquals(c.getI(), -0.4690265486725663716814159, DELTA);
		 
		copy.div(4);
		assertEquals(copy.getR(), 0.75, DELTA);
		assertEquals(copy.getI(), -1, DELTA);
	}
	
	@Test
	public void testAbs(){
		assertEquals(c.abs(), 5, DELTA);
	}
	
	@Test
	public void testAngle(){
		double a = c.angle();
		assertEquals(a, -0.9272952180016122324285124629224, DELTA);
	}
	
	@Test
	public void testRoot(){
		Complex copy = c.copy();
		c.root(3);
		assertEquals(c.getR(), 1.628937145922175875214609371, DELTA);
		assertEquals(c.getI(), -0.520174502304545839545694170, DELTA);
		
		copy.root(2);
		assertEquals(copy.getR(), 2, DELTA);
		assertEquals(copy.getI(), -1, DELTA);
	}
	
	@Test
	public void testGetR(){
		assertTrue(c.getR() == 3);
	}

	@Test
	public void testSetR(){
		c.setR(-6);
		assertTrue(c.getR() == -6);
	}

	@Test
	public void testGetI(){
		assertTrue(c.getI() == -4);
	}

	@Test
	public void testSetI(){
		c.setI(2);
		assertTrue(c.getI() == 2);
	}

	@Test
	public void testCopy(){
		Complex copy = c.copy();
		assertFalse(copy == c);
		assertTrue(copy.getR() == c.getR());
		assertTrue(copy.getI() == c.getI());
	}
	
}
