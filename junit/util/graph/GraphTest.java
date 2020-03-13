package util.graph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.util.Camera;
import evolve.util.graph.Graph;

public class GraphTest{
	
	private Graph baseGraph;
	private Graph graph;
	
	@Before
	public void setUp(){
		baseGraph = new Graph(){
			@Override
			protected void renderGraph(Camera cam, Graphics2D g){}
		};
		
		graph = new Graph(2, 100, 200, 0, 0, 100, 800, 500){
			@Override
			protected void renderGraph(Camera cam, Graphics2D g){}
		};
		graph.calculateGraphData();
	}
	
	@Test
	public void testRender(){
		baseGraph.render();
		graph.render();
	}

	@Test
	public void renderAndLabels(){
		graph.addDataPoint(new Double[]{1.0, 1.0});
		graph.addDataPoint(new Double[]{-1.0, -1.0});

		graph.calculateGraphData();
		
		boolean[][] test = new boolean[][]{
			{false,	false,	false,	false},
			{false,	false,	false,	true},
			{false,	false,	true,	false},
			{false,	false,	true,	true},

			{false,	true,	false,	false},
			{false,	true,	false,	true},
			{false,	true,	true,	false},
			{false,	true,	true,	true},

			{true,	false,	false,	false},
			{true,	false,	false,	true},
			{true,	false,	true,	false},
			{true,	false,	true,	true},
			
			{true,	true,	false,	false},
			{true,	true,	false,	true},
			{true,	true,	true,	false},
			{true,	true,	true,	true},
		};
		
		for(int i = 0; i < 16; i++){
			graph.setDrawHorizontalLines(test[i][0]);
			graph.setDisplayYAxisLabels(test[i][1]);
			graph.setDrawVerticalLines(test[i][2]);
			graph.setDisplayXAxisLabels(test[i][3]);
			graph.renderAndLabels();
		}
	}

	@Test
	public void testGetPixelXSpace(){
		graph.getPixelXSpace();
	}
	
	@Test
	public void testGetPixelYSpace(){
		graph.getPixelYSpace();
	}
	
	@Test
	public void testGetHighData(){
		graph.getHighData();
	}
	
	@Test
	public void testGetLowData(){
		graph.getLowData();
	}
	
	@Test
	public void testGetDataSize(){
		graph.getDataSize();
	}
	
	@Test
	public void testGetPixelOrigin(){
		graph.getPixelOrigin();
	}
	
	@Test
	public void testGetDataPixelRatio(){
		graph.getDataPixelRatio();
	}
	
	@Test
	public void testGetSetDataNumberSets(){
		ArrayList<Double[]> data = new ArrayList<Double[]>();
		data.add(new Double[]{1.0, -1.0, 2.0});
		graph.setData(data);
		assertTrue(graph.getData().equals(data));
		assertTrue(graph.getNumberDataSets() == 3);
	}
	
	@Test
	public void testGetGraphImage(){
		assertFalse(graph.getGraphImage() == null);
	}

	@Test
	public void testIsSetDrawHorizontalLines(){
		graph.setDrawHorizontalLines(true);
		assertTrue(graph.isDrawHorizontalLines());
	}

	@Test
	public void testIsSetDrawVerticalLines(){
		graph.setDrawVerticalLines(true);
		assertTrue(graph.isDrawVerticalLines());
	}
	
	@Test
	public void testIsGetDrawDataOnTop(){
		graph.setDrawDataOnTop(true);
		assertTrue(graph.isDrawDataOnTop());
	}

	@Test
	public void testGetSetBackgroundColor(){
		Color c = new Color(245, 43, 2);
		graph.setBackgroundColor(c);
		assertTrue(graph.getBackgroundColor().equals(c));
	}

	@Test
	public void testGetSetScaleLinesColor(){
		Color c = new Color(245, 43, 200);
		graph.setScaleLinesColor(c);
		assertTrue(graph.getScaleLinesColor().equals(c));
	}

	@Test
	public void testGetSetLabelColor(){
		Color c = new Color(0, 43, 0);
		graph.setLabelColor(c);
		assertTrue(graph.getLabelColor().equals(c));
	}

	@Test
	public void testGetSetLabelFont(){
		Font f = new Font("Times New Roman", Font.BOLD, 20);
		graph.setLabelFont(f);
		assertTrue(graph.getLabelFont().equals(f));
	}

	@Test
	public void testAddDataPoint(){
		baseGraph.addDataPoint(new Double[]{1.0});
		graph.addDataPoint(new Double[]{1.0, 1.4, 3.6});
	}
	
	@Test
	public void testGetSetData(){
		ArrayList<Double[]> data = new ArrayList<Double[]>();
		data.add(new Double[]{1.0, 2.0});
		graph.setData(data);
		assertTrue(graph.getData().equals(data));
	}
	
	@Test
	public void testClearData(){
		graph.clearData();
	}
	
	@Test
	public void testGetNumberDataSets(){
		assertTrue(graph.getNumberDataSets() == 2);
	}
	
	@Test
	public void testGetSetMaxDataPoints(){
		baseGraph.setMaxDataPoints(50);
		assertTrue(baseGraph.getMaxDataPoints() == 50);
	}

	@Test
	public void testGetSetLeftSpace(){
		baseGraph.setLeftSpace(208);
		assertTrue(baseGraph.getLeftSpace() == 208);
		baseGraph.setLeftSpace(-1);
		assertTrue(baseGraph.getLeftSpace() == 0);
	}

	@Test
	public void testGetSetRightSpace(){
		baseGraph.setRightSpace(20);
		assertTrue(baseGraph.getRightSpace() == 20);
		baseGraph.setRightSpace(-2);
		assertTrue(baseGraph.getRightSpace() == 0);
	}

	@Test
	public void testGetSetTopSpace(){
		baseGraph.setTopSpace(25);
		assertTrue(baseGraph.getTopSpace() == 25);
		baseGraph.setTopSpace(-25);
		assertTrue(baseGraph.getTopSpace() == 0);
	}

	@Test
	public void testGetSetBottomSpace(){
		baseGraph.setBottomSpace(27);
		assertTrue(baseGraph.getBottomSpace() == 27);
		baseGraph.setBottomSpace(-7);
		assertTrue(baseGraph.getBottomSpace() == 0);
	}
	
	@Test
	public void testIsSetDisplayXAxisLabels(){
		graph.setDisplayXAxisLabels(true);
		assertTrue(graph.isDisplayXAxisLabels());
	}

	@Test
	public void testIsSetDisplayYAxisLabels(){
		graph.setDisplayYAxisLabels(true);
		assertTrue(graph.isDisplayYAxisLabels());}

	@Test
	public void testGetSetWidth(){
		graph.setWidth(100);
		assertTrue(graph.getWidth() == 100);
	}

	@Test
	public void testGetTotalWidth(){
		graph.setWidth(400);
		graph.setLeftSpace(100);
		graph.setRightSpace(20);
		assertTrue(graph.getTotalWidth() == 520);
	}

	@Test
	public void testGetSetHeight(){
		graph.setHeight(107);
		assertTrue(graph.getHeight() == 107);
	}

	@Test
	public void testGetTotalHeight(){
		graph.setHeight(300);
		graph.setTopSpace(40);
		graph.setBottomSpace(60);
		assertTrue(graph.getTotalHeight() == 400);
	}
	
	@Test
	public void testSetAnchorPos(){
		graph.setAnchorPos(0, 0);
	}

	@Test
	public void testRemoveAnchor(){
		graph.removeAnchor();
	}

	@Test
	public void testPan(){
		graph.setAnchorPos(0, 0);
		graph.pan(1, 3);
	}
	
	@Test
	public void testZoom(){
		graph.zoom(20, 50, -1, false, false);
		graph.zoom(20, 50, 1, false, true);
		graph.zoom(20, 50, 2.65, true, false);
		graph.zoom(20, 50, -1.4, true, true);
	}

	@Test
	public void testResetCamera(){
		graph.resetCamera();
	}

	@Test
	public void testSaveLoad(){
		graph.setLabelFont(new Font("Times New Roman", Font.PLAIN, 40));
		for(int i = 0; i < 100; i++) graph.addDataPoint(new Double[]{Math.random(), Math.random()});
		
		File loc = new File(Main.DATA_PATH + "JunitGraphTest.txt");
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
