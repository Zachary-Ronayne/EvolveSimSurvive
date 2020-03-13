package evolve.util.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import evolve.gui.GraphGui;
import evolve.util.graph.Graph;

/**
 * An object that handles panning and zooming a graph based on mouse inputs
 */
public class GraphPanning extends MouseAdapter{ 
	
	/**
	 * The GraphGui that this MouseAdapter uses for updating graph renderings
	 */
	private GraphGui graphGui;
	
	/**
	 * The graph that this object controls
	 */
	private Graph graph;
	
	/**
	 * Create a new MouseAdapter to control a graph
	 * @param graph the graph to control
	 * @param graphGui the graphGui that should be repainted when the graph moves
	 */
	public GraphPanning(Graph graph, GraphGui graphGui){
		this.graph = graph;
		this.graphGui = graphGui;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e){
		//zoom in based on the position of the mouse
		if(e.isShiftDown()) graph.zoom(e.getX(), e.getY(), -e.getPreciseWheelRotation(), true, false);
		else if(e.isControlDown()) graph.zoom(e.getX(), e.getY(), -e.getPreciseWheelRotation(), false, true);
		else graph.zoom(e.getX(), e.getY(), -e.getPreciseWheelRotation(), true, true);
		updateRender();
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		//on right click, release the graph anchor
		if(e.getButton() == MouseEvent.BUTTON3) graph.removeAnchor();
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		//on left click, reset the camera
		if(e.getButton() == MouseEvent.BUTTON1){
			graph.resetCamera();
			updateRender();
		}
		
		//on right click, set the camera's anchor
		else if(e.getButton() == MouseEvent.BUTTON3) graph.setAnchorPos(e.getX(), e.getY());
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		//pan the graph
		graph.pan(e.getX(), e.getY());
		updateRender();
	}
	
	private void updateRender(){
		if(graphGui != null) graphGui.renderSelectedGraph();
	}
	
	/**
	 * Set the graph that this MouseAdpater uses to control a graph
	 */
	public void setGraph(Graph graph){
		this.graph = graph;
	}
	
}
