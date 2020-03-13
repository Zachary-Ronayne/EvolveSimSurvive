package util.input;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.gui.GuiHandler;
import evolve.util.graph.LineGraph;
import evolve.util.graph.LineGraphDetail;
import evolve.util.input.GraphPanning;
import evolve.util.options.Settings;

public class GraphPanningTest{
	
	private LineGraph graph;
	private GraphPanning panner;
	
	private LineGraph nullGraphPan;
	private GraphPanning nullPanner;
	
	private GuiHandler handler;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		handler = Main.crateHandler();
		handler.closeAllExtraWindows();
		
		graph = new LineGraph(new LineGraphDetail[]{new LineGraphDetail(1f, Color.BLACK)});
		panner = new GraphPanning(graph, handler.getGraphGui());
		
		nullGraphPan = new LineGraph(new LineGraphDetail[]{new LineGraphDetail(1f, Color.BLACK)});
		nullPanner = new GraphPanning(nullGraphPan, null);
	}
	
	@Test
	public void testMouseWheelMoved(){
		JPanel pan = new JPanel();
		
		MouseWheelEvent e = new MouseWheelEvent(pan, MouseWheelEvent.MOUSE_WHEEL, System.nanoTime(), 0, 0, 0, 1, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, -1);
		panner.mouseWheelMoved(e);
		nullPanner.mouseWheelMoved(e);
		
		e = new MouseWheelEvent(pan, MouseWheelEvent.MOUSE_WHEEL, System.nanoTime(), MouseEvent.ALT_DOWN_MASK, 0, 0, 1, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, -1);
		panner.mouseWheelMoved(e);
		nullPanner.mouseWheelMoved(e);
		
		e = new MouseWheelEvent(pan, MouseWheelEvent.MOUSE_WHEEL, System.nanoTime(), MouseEvent.CTRL_DOWN_MASK, 0, 0, 1, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, -1);
		panner.mouseWheelMoved(e);
		nullPanner.mouseWheelMoved(e);
	}
	
	@Test
	public void testMouseReleased(){
		JPanel pan = new JPanel();
		
		MouseEvent e = new MouseEvent(pan, MouseEvent.MOUSE_RELEASED, System.nanoTime(), 0, 0, 0, 0, 0, 1, false, MouseEvent.BUTTON3);
		panner.mouseReleased(e);
		nullPanner.mouseReleased(e);
	}
	
	@Test
	public void testMousePressed(){
		JPanel pan = new JPanel();
		
		MouseEvent e = new MouseEvent(pan, MouseEvent.MOUSE_PRESSED, System.nanoTime(), 0, 101, 20, 0, 0, 1, false, MouseEvent.BUTTON3);
		panner.mousePressed(e);
		nullPanner.mousePressed(e);
		
		e = new MouseEvent(pan, MouseEvent.MOUSE_PRESSED, System.nanoTime(), 0, 0, 0, 0, 0, 1, false, MouseEvent.BUTTON1);
		panner.mouseReleased(e);
		nullPanner.mousePressed(e);
	}
	
	@Test
	public void testMouseDragged(){
		JPanel pan = new JPanel();
		
		MouseEvent e = new MouseEvent(pan, MouseEvent.MOUSE_DRAGGED, System.nanoTime(), 0, 150, 10, 0, 0, 1, false, MouseEvent.BUTTON3);
		panner.mouseDragged(e);
		nullPanner.mouseDragged(e);
	}
	
	@Test
	public void testSetGraph(){
		panner.setGraph(graph);
	}
	
	@After
	public void end(){
		handler.getSimGui().getFrame().setVisible(false);
		handler.closeAllExtraWindows();
		handler.getClock().setStopUpdates(true);
		handler.getClock().stopClock();
		handler.disposeAllWindows();
	}
	
}
