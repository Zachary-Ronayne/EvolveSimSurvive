package evolve.gui.component;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import evolve.gui.GraphGui;
import evolve.gui.layout.SimLayoutHandler;
import evolve.util.graph.Graph;
import evolve.util.input.GraphPanning;

/**
 * A class, made for use in GraphGui, that holds a graph and labels for storing and displaying a graph
 */
public class GraphHolderPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * the graph used by this object for display
	 */
	private Graph graph;
	
	/**
	 * The label that this object uses to display a title
	 */
	private SimLabel title;
	
	/**
	 * The JPanel that this object uses to draw the graph to
	 */
	private JPanel graphPanel;
	
	/**
	 * The MouseAdapter that controls graph movement
	 */
	private GraphPanning paner;
	
	/**
	 * Create a new GraphHolderPanel using the given graph and title
	 * @param title the title to use for this panel
	 * @param graph the graph that this panel will display
	 * @param graphGui the GraphGui that this object uses for updating rendering
	 */
	public GraphHolderPanel(String title, Graph graph, GraphGui graphGui){
		super();
		
		this.graph = graph;
		
		SimLayoutHandler.createVerticalLayout(this);
		
		//add title
		this.title = new SimLabel(title);
		add(this.title);
		
		//create a panel for the graph
		this.graphPanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g){
				g.drawImage(getGraph().getGraphImage(), 0, 0, null);
			}
		};
		this.graphPanel.setPreferredSize(new Dimension((int)getGraph().getTotalWidth(), (int)getGraph().getTotalHeight()));
		add(this.graphPanel);
		
		//add panning system to graph
		paner = new GraphPanning(getGraph(), graphGui);
		this.graphPanel.addMouseListener(paner);
		this.graphPanel.addMouseMotionListener(paner);
		this.graphPanel.addMouseWheelListener(paner);
	}

	public Graph getGraph(){
		return graph;
	}
	public void setGraph(Graph graph){
		this.graph = graph;
		paner.setGraph(this.graph);
	}

	public SimLabel getTitle(){
		return title;
	}
	
}
