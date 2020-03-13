package util.math.vector;

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
import evolve.util.math.vector.PosVector;
import evolve.util.math.vector.Vector;

public class PosVectorTest{

	public static final double DELTA = 0.0001;
	
	private PosVector vector;
	
	@Before
	public void setUp(){
		vector = new PosVector(-3.56, 2.93);
	}
	
	@Test
	public void testGetX(){
		assertEquals(vector.getX(), -3.56, DELTA);
	}

	@Test
	public void testSetX(){
		vector.setX(5.67);
		assertEquals(vector.getX(), 5.67, DELTA);
		assertEquals(vector.getY(), 2.93, DELTA);
	}

	@Test
	public void testGetY(){
		assertEquals(vector.getY(), 2.93, DELTA);
	}

	@Test
	public void testSetY(){
		vector.setY(-1.23);
		assertEquals(vector.getX(), -3.56, DELTA);
		assertEquals(vector.getY(), -1.23, DELTA);
	}
	
	@Test
	public void testGetAngle(){
		assertEquals(vector.getAngle(), 2.45296, DELTA);
	}

	@Test
	public void testSetAngle(){
		vector.setAngle(1.73);
		assertEquals(vector.getAngle(), 1.73, DELTA);
	}

	@Test
	public void testGetMagnitude(){
		assertEquals(vector.getMagnitude(), 4.61069, DELTA);
	}

	@Test
	public void testSetMagnitude(){
		vector.setMagnitude(10.73);
		assertEquals(vector.getMagnitude(), 10.73, DELTA);
	}

	@Test
	public void testCopy(){
		Vector copy = vector.copy();
		assertFalse(copy == vector);
		assertEquals(vector.getX(), -3.56, DELTA);
		assertEquals(vector.getY(), 2.93, DELTA);
		assertEquals(vector.getAngle(), 2.45296, DELTA);
		assertEquals(vector.getMagnitude(), 4.61069, DELTA);
	}
	
	@Test
	public void testSaveLoad(){
		Vector load = new PosVector(0, 0);
		
		File loc = new File(Main.DATA_PATH + "PosVectorSaveTest.txt");
		try{
			PrintWriter write = new PrintWriter(loc);
			assertTrue(vector.save(write));
			write.close();
			
			Scanner read = new Scanner(loc);
			assertTrue(load.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		assertEquals(load.getX(), -3.56, DELTA);
		assertEquals(load.getY(), 2.93, DELTA);
		assertEquals(load.getAngle(), 2.45296, DELTA);
		assertEquals(load.getMagnitude(), 4.61069, DELTA);
		
		loc.delete();
	}

}
