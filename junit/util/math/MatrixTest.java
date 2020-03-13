package util.math;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import evolve.util.math.Matrix;

public class MatrixTest{
	
	public static final double DELTA = 0.00000001;
	
	private Matrix matrix;
	
	@Before
	public void setUp(){
		matrix = new Matrix(new double[][]{
				{1,	2,	3,	4,	4},
				{0,	3,	6,	-7,	1},
				{1,	-4,	5,	7,	0},
				{0,	4,	0,	9,	2.4}
		});
	}
	
	@Test
	public void testRowIn(){
		assertFalse(matrix.rowIn(-1));
		assertFalse(matrix.rowIn(4));
		
		assertTrue(matrix.rowIn(0));
		assertTrue(matrix.rowIn(3));
	}

	@Test
	public void testcolIn(){
		assertFalse(matrix.colIn(-1));
		assertFalse(matrix.colIn(5));
		
		assertTrue(matrix.colIn(0));
		assertTrue(matrix.colIn(4));
	}
	
	@Test
	public void testGetSetEntries(){
		try{
			new Matrix(new double[][]{});
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}

		try{
			new Matrix(new double[][]{
				{}
			});
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}

		try{
			new Matrix(new double[][]{
				{1}, {1, 2}
			});
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}
		
		try{
			new Matrix(new double[][]{
				{1, 0}, {1, 2}
			});
			assertTrue(true);
		}catch(IllegalArgumentException e){
			assertTrue(false);
		}

		try{
			new Matrix(new double[][]{});
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}

		
		try{
			matrix.setEntries(new double[][]{
				{}
			});
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}

		try{
			matrix.setEntries(new double[][]{
				{1}, {1, 2}
			});
			assertTrue(false);
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}
		
		try{
			matrix.setEntries(new double[][]{
				{1, 0}, {1, 2}
			});
			assertTrue(true);
		}catch(IllegalArgumentException e){
			assertTrue(false);
		}
	}

	@Test
	public void testEntry(){
		assertTrue(matrix.entry(0, 0) == 1);
		assertTrue(matrix.entry(2, 1) == -4);
	}
	
	@Test
	public void testSet(){
		matrix.set(2, 0, 0);
		assertTrue(matrix.entry(0, 0) == 2);
	}

	@Test
	public void testGetRowCount(){
		assertTrue(matrix.getRowCount() == 4);
	}

	@Test
	public void testGetColCount(){
		assertTrue(matrix.getColCount() == 5);
	}

	@Test
	public void testGetLastcolumn(){
		double[] col = matrix.getLastcolumn();
		assertTrue(col[0] == 4);
		assertTrue(col[1] == 1);
		assertTrue(col[2] == 0);
		assertTrue(col[3] == 2.4);
		assertTrue(col.length == 4);
	}
	
	@Test
	public void testSortRows(){
		matrix.sortRows(0, 0);
		
		assertTrue(matrix.entry(0, 0) == 1);
		assertTrue(matrix.entry(1, 0) == 1);
		assertTrue(matrix.entry(2, 0) == 0);
		assertTrue(matrix.entry(3, 0) == 0);

		matrix.sortRows(1, 1);
		assertTrue(			matrix.entry(0, 1) == 2);
		assertTrue(Math.abs(matrix.entry(1, 1)) == 4);
		assertTrue(Math.abs(matrix.entry(2, 1)) == 4);
		assertTrue(			matrix.entry(3, 1) == 3);

		matrix.sortRows(4, 1);
		assertTrue(matrix.entry(0, 4) == 4);
		assertTrue(matrix.entry(1, 4) == 2.4);
		assertTrue(matrix.entry(2, 4) == 1);
		assertTrue(matrix.entry(3, 4) == 0);
	}
	
	@Test
	public void testSwapRows(){
		matrix.swapRows(0, 1);
		assertTrue(matrix.entry(0, 0) == 0);
		assertTrue(matrix.entry(0, 1) == 3);
		assertTrue(matrix.entry(0, 2) == 6);
		assertTrue(matrix.entry(0, 3) == -7);
		assertTrue(matrix.entry(0, 4) == 1);
		
		assertTrue(matrix.entry(1, 0) == 1);
		assertTrue(matrix.entry(1, 1) == 2);
		assertTrue(matrix.entry(1, 2) == 3);
		assertTrue(matrix.entry(1, 3) == 4);
		assertTrue(matrix.entry(1, 4) == 4);
	}
	
	@Test
	public void testSwapRowEntry(){
		matrix.swapRowEntry(0, 2, 2);
		assertTrue(matrix.entry(0, 2) == 5);
		assertTrue(matrix.entry(2, 2) == 3);
		
		matrix.swapRowEntry(0, 1, 0);
		assertTrue(matrix.entry(0, 0) == 0);
		assertTrue(matrix.entry(1, 0) == 1);
	}
	
	@Test
	public void testScale(){
		matrix.scale(0, 2);
		assertTrue(matrix.entry(0, 0) == 2);
		assertTrue(matrix.entry(0, 1) == 4);
		assertTrue(matrix.entry(0, 2) == 6);
		assertTrue(matrix.entry(0, 3) == 8);
		assertTrue(matrix.entry(0, 4) == 8);
		
		matrix.scale(3, 1.5);
		assertEquals(matrix.entry(3, 0), 0, DELTA);
		assertEquals(matrix.entry(3, 1), 6, DELTA);
		assertEquals(matrix.entry(3, 2), 0, DELTA);
		assertEquals(matrix.entry(3, 3), 13.5, DELTA);
		assertEquals(matrix.entry(3, 4), 3.6, DELTA);
	}
	
	@Test
	public void testAddRow(){
		matrix.addRow(0, 1, 2);
		assertEquals(matrix.entry(1, 0), 2, DELTA);
		assertEquals(matrix.entry(1, 1), 7, DELTA);
		assertEquals(matrix.entry(1, 2), 12, DELTA);
		assertEquals(matrix.entry(1, 3), 1, DELTA);
		assertEquals(matrix.entry(1, 4), 9, DELTA);
	}
	
	@Test
	public void testRef(){
		matrix.ref();
		assertTrue(matrix.entry(0, 0) == 1);

		assertTrue(matrix.entry(1, 0) == 0);
		assertTrue(matrix.entry(1, 1) == 1);

		assertTrue(matrix.entry(2, 0) == 0);
		assertTrue(matrix.entry(2, 1) == 0);
		assertTrue(matrix.entry(2, 2) == 1);

		assertTrue(matrix.entry(3, 0) == 0);
		assertTrue(matrix.entry(3, 1) == 0);
		assertTrue(matrix.entry(3, 2) == 0);
		assertTrue(matrix.entry(3, 3) == 1);
	}
	
	@Test
	public void testRref(){
		matrix.rref();
		
		assertTrue(matrix.entry(0, 0) == 1);
		assertTrue(matrix.entry(0, 1) == 0);
		assertTrue(matrix.entry(0, 2) == 0);
		assertTrue(matrix.entry(0, 3) == 0);
		
		assertTrue(matrix.entry(1, 0) == 0);
		assertTrue(matrix.entry(1, 1) == 1);
		assertTrue(matrix.entry(1, 2) == 0);
		assertTrue(matrix.entry(1, 3) == 0);
		
		assertTrue(matrix.entry(2, 0) == 0);
		assertTrue(matrix.entry(2, 1) == 0);
		assertTrue(matrix.entry(2, 2) == 1);
		assertTrue(matrix.entry(2, 3) == 0);
		
		assertTrue(matrix.entry(3, 0) == 0);
		assertTrue(matrix.entry(3, 1) == 0);
		assertTrue(matrix.entry(3, 2) == 0);
		assertTrue(matrix.entry(3, 3) == 1);

		assertEquals(matrix.entry(0, 4), 3.2403162055335963, DELTA);
		assertEquals(matrix.entry(1, 4), 0.6142292490118577, DELTA);
		assertEquals(matrix.entry(2, 4), -0.14782608695652172, DELTA);
		assertEquals(matrix.entry(3, 4), -0.006324110671936755, DELTA);
	}
	
	@Test
	public void testToString(){
		matrix.toString();
	}
	
	@Test
	public void testCopy(){
		Matrix copy = matrix.copy();

		assertFalse(copy.equals(matrix));
		
		assertTrue(copy.entry(0, 0) == 1);
		assertTrue(copy.entry(0, 1) == 2);
		assertTrue(copy.entry(0, 2) == 3);
		assertTrue(copy.entry(0, 3) == 4);
		assertTrue(copy.entry(0, 4) == 4);
		
		assertTrue(copy.entry(1, 0) == 0);
		assertTrue(copy.entry(1, 1) == 3);
		assertTrue(copy.entry(1, 2) == 6);
		assertTrue(copy.entry(1, 3) == -7);
		assertTrue(copy.entry(1, 4) == 1);
		
		assertTrue(copy.entry(2, 0) == 1);
		assertTrue(copy.entry(2, 1) == -4);
		assertTrue(copy.entry(2, 2) == 5);
		assertTrue(copy.entry(2, 3) == 7);
		assertTrue(copy.entry(2, 4) == 0);
		
		assertTrue(copy.entry(3, 0) == 0);
		assertTrue(copy.entry(3, 1) == 4);
		assertTrue(copy.entry(3, 2) == 0);
		assertTrue(copy.entry(3, 3) == 9);
		assertTrue(copy.entry(3, 4) == 2.4);
	}
}
