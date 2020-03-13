package util.math;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import evolve.util.math.Circle;

public class CircleTest{
	
	public static final double DELTA = 0.000000001;
	
	private Circle circle;
	
	@Before
	public void setUp(){
		circle = new Circle(10, 8, 2);
	}
	
	@Test
	public void testDefaultConstructor(){
		Circle c = new Circle();
		assertTrue(c.getX() == 0);
		assertTrue(c.getY() == 0);
		assertTrue(c.getRadius() == 0);
	}
	
	@Test
	public void testDistance(){
		assertEquals(circle.distance(10, 8), 0, DELTA);
		assertEquals(circle.distance(11, 8), 0, DELTA);
		assertEquals(circle.distance(13, 8), 1, DELTA);
	}

	@Test
	public void testInCircle(){
		assertTrue(circle.inCircle(10, 8));
		assertTrue(circle.inCircle(11, 8));
		assertTrue(circle.inCircle(12, 8));
		assertFalse(circle.inCircle(13, 8));
	}

	@Test
	public void testGetX(){
		assertTrue(circle.getX() == 10);
	}
	
	@Test
	public void testSetX(){
		circle.setX(5);
		assertTrue(circle.getX() == 5);
	}
	
	@Test
	public void testGetY(){
		assertTrue(circle.getY() == 8);
	}
	
	@Test
	public void testSetY(){
		circle.setY(6);
		assertTrue(circle.getY() == 6);
	}
	
	@Test
	public void testGetRadius(){
		assertTrue(circle.getRadius() == 2);
	}
	
	@Test
	public void testSetRadius(){
		circle.setRadius(3);
		assertTrue(circle.getRadius() == 3);
	}
}
