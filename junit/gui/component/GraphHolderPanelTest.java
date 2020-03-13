package gui.component;

import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.gui.component.GraphHolderPanel;
import evolve.util.graph.Graph;
import evolve.util.graph.LineGraph;
import evolve.util.graph.LineGraphDetail;
import evolve.util.options.Settings;

public class GraphHolderPanelTest{
	
	private GraphHolderPanel holder;
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		handler = Main.crateHandler();
		Graph graph = new LineGraph(new LineGraphDetail[]{new LineGraphDetail(1f, Color.RED)});
		
		holder = new GraphHolderPanel("test", graph, handler.getGraphGui());
	}
	
	@Test
	public void testGetSetGraph(){
		Graph graph = new LineGraph(new LineGraphDetail[]{new LineGraphDetail(2f, Color.RED)});
		
		holder.setGraph(graph);
		assertTrue(holder.getGraph().equals(graph));
	}
	
	@Test
	public void testGetTitle(){
		holder.getTitle();
	}
	
	@After
	public void end(){
		handler.getSimGui().getFrame().setVisible(false);
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
	}
	
}
