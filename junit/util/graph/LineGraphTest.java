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
import evolve.util.graph.LineGraph;
import evolve.util.graph.LineGraphDetail;

public class LineGraphTest{
	
	private LineGraph graph;
	
	@Before
	public void setUp(){
		graph = new LineGraph(new LineGraphDetail[]{
				new LineGraphDetail(1, Color.RED),
				new LineGraphDetail(2, Color.BLUE),
				new LineGraphDetail(4.6f, Color.YELLOW)
		});
	}
	
	@Test
	public void testRenderGraph(){
		graph.render();
	}

	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "JunitLineGraphTest.txt");
		try{
			PrintWriter write = new PrintWriter(loc);
			assertTrue(graph.save(write));
			write.close();

			Scanner read = new Scanner(loc);
			assertTrue(graph.load(read));
			read.close();
			
		}catch(Exception e){
			e.printStackTrace();
			assertFalse(true);
		}
		loc.delete();
	}
	
}
