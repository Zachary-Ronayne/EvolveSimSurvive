package util.math;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;

import evolve.util.math.Calc;
import evolve.util.math.Complex;

public class CalcTest{

	public static final double DELTA = 0.0000001;
	
	@Test
	public void testPointToLine(){
		assertEquals(Calc.pointToLine(0.32, -0.12, -0.24, 1.55, 1, -0.11), 0.550771174456, DELTA);
		assertEquals(Calc.pointToLine(1.3, 0.15, 0.21, 1.65, 0.21, 0.14), 1.09, DELTA);
	}

	@Test
	public void testCircleIntersectsLine(){
		assertTrue(Calc.circleIntersectsLine(1.3, 1.3, 0.15, 0.21, 1.65, 2.63, 0.71));
		assertTrue(Calc.circleIntersectsLine(1.3, 1.3, 0.15, 0.21, 1.65, 8.29, -3.71));
		assertTrue(Calc.circleIntersectsLine(1.3, 1.3, 0.15, -5.01, 4.5, 8.29, -3.71));
		assertTrue(Calc.circleIntersectsLine(1.3, 1.3, 0.15, 1.62, 0.58, 7.65, -4.27));
		assertTrue(Calc.circleIntersectsLine(1.3, 1.3, 0.15, 0.51, -2.15, 0.51, 2.7));

		assertFalse(Calc.circleIntersectsLine(1.3, 1.3, 0.15, 2, 1.5, 3.06, 0.16));
		assertFalse(Calc.circleIntersectsLine(1.3, 1.3, 0.15, 2, 1.5, 3.31, 3.58));
		assertFalse(Calc.circleIntersectsLine(1.3, 1.3, 0.15, -0.12, -2.24, 0.4, -1.4));
		assertFalse(Calc.circleIntersectsLine(1.3, 3.3, 0.15, 0.51, -2.15, 0.51, 2.7));
	}
	
	@Test
	public void testTouchingGridPos(){
		ArrayList<Point> grid;

		grid = Calc.touchingGridPos(0.59, 0.346, 0.59, 1.424, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(0, 1)));
		assertTrue(grid.size() == 2);
		
		grid = Calc.touchingGridPos(0.59, 0.346, 0.59, 0.424, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.size() == 1);

		
		grid = Calc.touchingGridPos(0.59, 0.346, 0.59, 5.424, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(0, 1)));
		assertTrue(grid.contains(new Point(0, 2)));
		assertTrue(grid.contains(new Point(0, 3)));
		assertTrue(grid.contains(new Point(0, 4)));
		assertTrue(grid.contains(new Point(0, 5)));
		assertTrue(grid.size() == 6);
		
		grid = Calc.touchingGridPos(5.68, 2.31, 0.5, 0.5, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(1, 0)));
		assertTrue(grid.contains(new Point(1, 1)));
		assertTrue(grid.contains(new Point(2, 1)));
		assertTrue(grid.contains(new Point(3, 1)));
		assertTrue(grid.contains(new Point(4, 1)));
		assertTrue(grid.contains(new Point(4, 2)));
		assertTrue(grid.contains(new Point(5, 2)));
		assertTrue(grid.size() == 8);
		
		grid = Calc.touchingGridPos(.3, 1.79, 5.675, 4.473, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 1)));
		assertTrue(grid.contains(new Point(0, 2)));
		assertTrue(grid.contains(new Point(1, 2)));
		assertTrue(grid.contains(new Point(2, 2)));
		assertTrue(grid.contains(new Point(2, 3)));
		assertTrue(grid.contains(new Point(3, 3)));
		assertTrue(grid.contains(new Point(4, 3)));
		assertTrue(grid.contains(new Point(4, 4)));
		assertTrue(grid.contains(new Point(5, 4)));
		assertTrue(grid.size() == 9);

		grid = Calc.touchingGridPos(.8, 5.81, 3.31, .51, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 5)));
		assertTrue(grid.contains(new Point(1, 5)));
		assertTrue(grid.contains(new Point(1, 4)));
		assertTrue(grid.contains(new Point(1, 3)));
		assertTrue(grid.contains(new Point(2, 3)));
		assertTrue(grid.contains(new Point(2, 2)));
		assertTrue(grid.contains(new Point(2, 1)));
		assertTrue(grid.contains(new Point(3, 1)));
		assertTrue(grid.contains(new Point(3, 0)));
		assertTrue(grid.size() == 9);
		
		grid = Calc.touchingGridPos(.8, 5.81, 4.58, 4.21, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 5)));
		assertTrue(grid.contains(new Point(1, 5)));
		assertTrue(grid.contains(new Point(2, 5)));
		assertTrue(grid.contains(new Point(2, 4)));
		assertTrue(grid.contains(new Point(3, 4)));
		assertTrue(grid.contains(new Point(4, 4)));
		assertTrue(grid.size() == 6);

		grid = Calc.touchingGridPos(2.5, 2.5, 0.5, 0.5, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(1, 1)));
		assertTrue(grid.contains(new Point(2, 2)));
		assertTrue(grid.size() == 3);

		grid = Calc.touchingGridPos(0.46, 0.39, 0.73, 1.6, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(0, 1)));
		assertTrue(grid.size() == 2);

		grid = Calc.touchingGridPos(3.45, 2.5, 1.57, 2.6, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(1, 2)));
		assertTrue(grid.contains(new Point(2, 2)));
		assertTrue(grid.contains(new Point(3, 2)));
		assertTrue(grid.size() == 3);
		
		grid = Calc.touchingGridPos(0.66, 5.5, 2.64, 5.17, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 5)));
		assertTrue(grid.contains(new Point(1, 5)));
		assertTrue(grid.contains(new Point(2, 5)));
		assertTrue(grid.size() == 3);
		
		grid = Calc.touchingGridPos(0.66, 5.5, 0.79, 3.36, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 5)));
		assertTrue(grid.contains(new Point(0, 4)));
		assertTrue(grid.contains(new Point(0, 3)));
		assertTrue(grid.size() == 3);
		
		grid = Calc.touchingGridPos(0.46, 0.39, 0.73, 1.6, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(0, 1)));
		assertTrue(grid.size() == 2);
		
		grid = Calc.touchingGridPos(0.56, 1.35, 0.5, 0.5, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(0, 1)));
		assertTrue(grid.size() == 2);
		
		grid = Calc.touchingGridPos(0.56, 1.35, 0.55, -0.95, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(0, 1)));
		assertTrue(grid.size() == 2);
		
		grid = Calc.touchingGridPos(0.43, 1.6, .73, 1.6, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 1)));
		assertTrue(grid.size() == 1);
		
		grid = Calc.touchingGridPos(6.52, 5.26, 4.4, 6.38, 1.0, 6, 6);
		assertTrue(grid.size() == 1);
		assertTrue(grid.contains(new Point(5, 5)));
		
		grid = Calc.touchingGridPos(0.46, 0.39, 0.72, 0.826, 1.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.size() == 1);
		
		
		grid = Calc.touchingGridPos(2.36, -1.29, 0.55, -0.95, 1.0, 6, 6);
		assertTrue(grid.size() == 0);
		
		grid = Calc.touchingGridPos(6.94, 5.88, 5.1, 7, 1.0, 6, 6);
		assertTrue(grid.size() == 0);
		

		grid = Calc.touchingGridPos(5, 5, 1, 1, 2.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(1, 1)));
		assertTrue(grid.contains(new Point(2, 2)));
		assertTrue(grid.size() == 3);
		
		grid = Calc.touchingGridPos(11.36, 4.62, 1, 1, 2.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(1, 0)));
		assertTrue(grid.contains(new Point(1, 1)));
		assertTrue(grid.contains(new Point(2, 1)));
		assertTrue(grid.contains(new Point(3, 1)));
		assertTrue(grid.contains(new Point(4, 1)));
		assertTrue(grid.contains(new Point(4, 2)));
		assertTrue(grid.contains(new Point(5, 2)));
		assertTrue(grid.size() == 8);

		grid = Calc.touchingGridPos(0.92, 0.78, 1.46, 3.2, 2.0, 6, 6);
		assertTrue(grid.contains(new Point(0, 0)));
		assertTrue(grid.contains(new Point(0, 1)));
		assertTrue(grid.size() == 2);
	}
	
	@Test
	public void testGetCubicRoots(){
		Complex[] roots;
		Comparator<Complex> c = new Comparator<Complex>(){
			@Override
			public int compare(Complex o1, Complex o2){
				int big = 10000000;
				int diff = (int)(o1.getR() * big - o2.getR() * big);
				if(diff < 0) return -1;
				else if(diff > 0) return 1;
				
				diff = (int)(o1.getI() * big - o2.getI() * big);
				if(diff < 0) return -1;
				else if(diff > 0) return 1;
				return 0;
			}
		};
		
		
		roots = Calc.getCubicRoots(1, 2, 3, 5);
		Arrays.sort(roots, c);
		
		assertEquals(roots[0].getR(), -1.843734277898068902704902, DELTA);
		assertEquals(roots[0].getI(), 0, DELTA);

		assertEquals(roots[1].getR(), -0.078132861050965548647549, DELTA);
		assertEquals(roots[1].getI(), -1.644926377599972184599249, DELTA);
		
		assertEquals(roots[2].getR(), -0.078132861050965548647549, DELTA);
		assertEquals(roots[2].getI(), 1.644926377599972184599249, DELTA);
		
		
		roots = Calc.getCubicRoots(1, 0, 0, 0);
		Arrays.sort(roots, c);
		
		assertEquals(roots[0].getR(), 0, DELTA);
		assertEquals(roots[0].getI(), 0, DELTA);

		assertEquals(roots[1].getR(), 0, DELTA);
		assertEquals(roots[1].getI(), 0, DELTA);
		
		assertEquals(roots[2].getR(), 0, DELTA);
		assertEquals(roots[2].getI(), 0, DELTA);
		
		
		roots = Calc.getCubicRoots(1, -22, 3, 5);
		Arrays.sort(roots, c);
		
		assertEquals(roots[0].getR(), -0.4101336123842223136313506, DELTA);
		assertEquals(roots[0].getI(), 0, DELTA);

		assertEquals(roots[1].getR(), 0.5578900405528717138615759, DELTA);
		assertEquals(roots[1].getI(), 0, DELTA);
		
		assertEquals(roots[2].getR(), 21.85224357183135059976977, DELTA);
		assertEquals(roots[2].getI(), 0, DELTA);
		
		
		roots = Calc.getCubicRoots(1, -2, 30, 5);
		Arrays.sort(roots, c);
		
		assertEquals(roots[0].getR(), -0.1647091135701096368410692, DELTA);
		assertEquals(roots[0].getI(), 0, DELTA);

		assertEquals(roots[1].getR(), 1.082354556785054818420535, DELTA);
		assertEquals(roots[1].getI(), -5.402319495609260806719365, DELTA);

		assertEquals(roots[2].getR(), 1.082354556785054818420535, DELTA);
		assertEquals(roots[2].getI(), 5.402319495609260806719365, DELTA);
		
		
		roots = Calc.getCubicRoots(-4, 12, 6.29, -1.08);
		Arrays.sort(roots, c);
		
		assertEquals(roots[0].getR(), -0.5722704656574151345431742, DELTA);
		assertEquals(roots[0].getI(), 0, DELTA);

		assertEquals(roots[1].getR(), 0.1373556274490009033077501, DELTA);
		assertEquals(roots[1].getI(), 0, DELTA);
		
		assertEquals(roots[2].getR(), 3.434914838208414231235424, DELTA);
		assertEquals(roots[2].getI(), 0, DELTA);
	}

	@Test
	public void testSigmoid(){
		Calc.sigmoid(2);
	}
}
