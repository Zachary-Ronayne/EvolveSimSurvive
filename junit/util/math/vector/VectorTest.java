package util.math.vector;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import evolve.util.math.vector.AngleVector;
import evolve.util.math.vector.PosVector;
import evolve.util.math.vector.Vector;

public class VectorTest{

	public static final double DELTA = 0.0001;
	
	private Vector pVector;
	private Vector aVector;
	
	@Before
	public void setUp(){
		pVector = new PosVector(2.7, -4.9);
		aVector = new AngleVector(pVector.getAngle(), pVector.getMagnitude());
	}
	
	@Test
	public void testAdd(){
		Vector v = new PosVector(1.3, -3.5);
		pVector.add(v);
		aVector.add(v);

		assertEquals(pVector.getX(), 4, DELTA);
		assertEquals(pVector.getY(), -8.4, DELTA);
		assertEquals(pVector.getAngle(), -1.12638, DELTA);
		assertEquals(pVector.getMagnitude(), 9.30376, DELTA);

		assertEquals(aVector.getX(), 4, DELTA);
		assertEquals(aVector.getY(), -8.4, DELTA);
		assertEquals(aVector.getAngle(), -1.12638, DELTA);
		assertEquals(aVector.getMagnitude(), 9.30376, DELTA);
	}

	@Test
	public void testSub(){
		Vector v = new PosVector(1.3, -3.5);
		pVector.sub(v);
		aVector.sub(v);

		assertEquals(pVector.getX(), 1.4, DELTA);
		assertEquals(pVector.getY(), -1.4, DELTA);
		assertEquals(pVector.getAngle(), -0.785398, DELTA);
		assertEquals(pVector.getMagnitude(), 1.9799, DELTA);

		assertEquals(aVector.getX(), 1.4, DELTA);
		assertEquals(aVector.getY(), -1.4, DELTA);
		assertEquals(aVector.getAngle(), -0.785398, DELTA);
		assertEquals(aVector.getMagnitude(), 1.9799, DELTA);
	}

	@Test
	public void testMult(){
		pVector.mult(2.3);
		aVector.mult(2.3);
		
		assertEquals(pVector.getX(), 6.21, DELTA);
		assertEquals(pVector.getY(), -11.27, DELTA);
		assertEquals(pVector.getAngle(), -1.06717, DELTA);
		assertEquals(pVector.getMagnitude(), 12.8677, DELTA);

		assertEquals(aVector.getX(), 6.21, DELTA);
		assertEquals(aVector.getY(), -11.27, DELTA);
		assertEquals(aVector.getAngle(), -1.06717, DELTA);
		assertEquals(aVector.getMagnitude(), 12.8677, DELTA);
	}

	@Test
	public void testProjection(){
		Vector v = new PosVector(1.3, -3.5);
		Vector pProj = v.projection(pVector);
		Vector aProj = v.projection(aVector);
		
		assertEquals(pProj.getX(), 1.92669, DELTA);
		assertEquals(pProj.getY(), -5.18723, DELTA);
		assertEquals(pProj.getAngle(), -1.21516, DELTA);
		assertEquals(pProj.getMagnitude(), 5.53349, DELTA);

		assertEquals(aProj.getX(), 1.92669, DELTA);
		assertEquals(aProj.getY(), -5.18723, DELTA);
		assertEquals(aProj.getAngle(), -1.21516, DELTA);
		assertEquals(aProj.getMagnitude(), 5.53349, DELTA);
	}

	@Test
	public void testDot(){
		Vector v = new PosVector(1.3, -3.5);
		double pDot = pVector.dot(v);
		double aDot = aVector.dot(v);

		assertEquals(pDot, 20.66, DELTA);
		assertEquals(aDot, 20.66, DELTA);
	}
}
