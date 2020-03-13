package util.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import evolve.util.math.BoundedParabola;

public class BoundedParabolaTest{

	public static final double SMALL_DELTA = 0.001;
	public static final double DELTA = 0.000000001;
	
	private BoundedParabola p;
	
	@Before
	public void setUp(){
		p = new BoundedParabola(1, -2, 3, 1, -2);
	}
	
	@Test
	public void testConstructor(){
		BoundedParabola p = new BoundedParabola(-1, 1, 4, 6, 3, 4, 2, 5);
		assertEquals(p.getA(), .25, DELTA);
		assertEquals(p.getB(), .25, DELTA);
		assertEquals(p.getC(), 1, DELTA);
		assertEquals(p.getLowXBound(), 2, DELTA);
		assertEquals(p.getHighXBound(), 5, DELTA);
	}
	
	@Test
	public void testY(){
		assertTrue(p.y(0) == 3);
		assertTrue(p.y(2) == 3);
	}
	
	@Test
	public void testGetXValues(){
		double[] values;
		
		values = p.getXValues(2);
		assertFalse(values == null);
		assertEquals(values[0], 1, DELTA);
		assertEquals(values[1], 1, DELTA);
		
		values = p.getXValues(0);
		assertTrue(values == null);

		values = p.getXValues(5.4);
		assertFalse(values == null);
		if(values[0] < values[1]){
			assertEquals(values[0], -0.843908891459, DELTA);
			assertEquals(values[1], 2.84390889146, DELTA);
		}
		else{
			assertEquals(values[0], 2.84390889146, DELTA);
			assertEquals(values[1], -0.843908891459, DELTA);
		}
	}
	
	@Test
	public void testAproximateDistance(){
		double d;
		
		d = p.aproximateDistance(1.33, 2.11);
		assertEquals(d, 0.347850542619, SMALL_DELTA);

		d = p.aproximateDistance(-1.53, 12.74);
		assertEquals(d, 1.80235956457, SMALL_DELTA);
		
		d = p.aproximateDistance(-1.92, 9.28);
		assertEquals(d, 0.218164732771, SMALL_DELTA);
		
		
		p.setLowXBound(-100000);
		p.setHighXBound(100000);
		
		d = p.aproximateDistance(-.37, 2.5);
		assertEquals(d, 0.561592156085, SMALL_DELTA);

		d = p.aproximateDistance(-.88, 4.36);
		assertEquals(d, 0.327216584812, SMALL_DELTA);

		d = p.aproximateDistance(0.98, 4.12);
		assertEquals(d, 1.34886131495, SMALL_DELTA);
	}
	
	@Test
	public void testGuessDistance(){
		p.guessDistance(0, 0);
	}
	
	@Test
	public void testGetVertex(){
		Point2D.Double v = p.getVertex();
		assertEquals(v.getX(), 1, DELTA);
		assertEquals(v.getY(), 2, DELTA);
	}
	
	@Test
	public void generateFrom3Points(){
		p.generateFrom3Points(2, 4, 1, -2, 3, 6);
		assertEquals(p.getA(), -2, DELTA);
		assertEquals(p.getB(), 12, DELTA);
		assertEquals(p.getC(), -12, DELTA);
		
		p.generateFrom3Points(2.1, 4, 1, -2.5, 0, 6);
		assertEquals(p.getA(), 6.861471861471862, DELTA);
		assertEquals(p.getB(), -15.36147186147186, DELTA);
		assertEquals(p.getC(), 6, DELTA);
		
		try{
			p.generateFrom3Points(0, 0, 1, 0, 2, 2);
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}

		try{
			p.generateFrom3Points(0, 0, 1, 1, 2, 1);
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}

		try{
			p.generateFrom3Points(0, 0, 1, 1, 2, 0);
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}
		
		try{
			p.generateFrom3Points(2, 0, 2, 1, 2, 2);
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}
	}

	@Test
	public void testGetSetA(){
		assertTrue(p.getA() == 1);
		p.setA(2);
		assertTrue(p.getA() == 2);
	}

	@Test
	public void testGetSetB(){
		assertTrue(p.getB() == -2);
		p.setB(4);
		assertTrue(p.getB() == 4);
	}

	@Test
	public void testGetSetC(){
		assertTrue(p.getC() == 3);
		p.setC(7);
		assertTrue(p.getC() == 7);
	}

	@Test
	public void testGetSetLowXBound(){
		assertTrue(p.getLowXBound() == -2);
		
		p.setLowXBound(-2);
		assertTrue(p.getLowXBound() == -2);
		
		p.setLowXBound(5);
		assertTrue(p.getLowXBound() == 1);
	}

	@Test
	public void testGetSetHighXBound(){
		assertTrue(p.getHighXBound() == 1);
		
		p.setHighXBound(8);
		assertTrue(p.getHighXBound() == 8);
		
		p.setHighXBound(-3);
		assertTrue(p.getHighXBound() == -2);
	}
}
