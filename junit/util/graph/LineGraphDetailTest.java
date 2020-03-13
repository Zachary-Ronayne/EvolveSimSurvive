package util.graph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.util.graph.LineGraphDetail;

public class LineGraphDetailTest{
	
	private LineGraphDetail detail;
	
	@Before
	public void setUp(){
		detail = new LineGraphDetail(2.3f, Color.RED);
	}
	
	@Test
	public void testGetSetWeight(){
		detail.setWeight(2.5f);
		assertTrue(detail.getWeight() == 2.5f);
	}
	
	@Test
	public void testGetSetColor(){
		detail.setColor(Color.BLUE);
		assertTrue(detail.getColor().equals(Color.BLUE));
	}

	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "JunitLineGraphDetailTest.txt");
		try{
			PrintWriter write = new PrintWriter(loc);
			assertTrue(detail.save(write));
			write.close();

			Scanner read = new Scanner(loc);
			assertTrue(detail.load(read));
			read.close();
			
		}catch(Exception e){
			e.printStackTrace();
			assertFalse(true);
		}
		loc.delete();
	}
	
}
