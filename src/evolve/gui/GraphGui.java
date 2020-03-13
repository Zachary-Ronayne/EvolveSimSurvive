package evolve.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import evolve.Main;
import evolve.gui.component.GraphHolderPanel;
import evolve.gui.component.SimConstants;
import evolve.gui.component.SimDropDownMenu;
import evolve.gui.frame.GraphFrame;
import evolve.gui.layout.SimLayoutHandler;
import evolve.sim.Simulation;
import evolve.util.clock.ClockUpdateEvent;
import evolve.util.clock.GameClock;
import evolve.util.graph.Graph;
import evolve.util.graph.LineGraph;
import evolve.util.graph.LineGraphDetail;

public class GraphGui extends Gui{
	
	/**
	 * The name for the population graph
	 */
	public static String OPTION_POPULATION = "Population";
	
	/**
	 * The name for the mutability graph
	 */
	public static String OPTION_MUTABILITY = "Mutability";
	
	/**
	 * The name for the age graph
	 */
	public static String OPTION_AGE = "Age";
	
	/**
	 * The frame used by this GUI
	 */
	private GraphFrame frame;
	
	/**
	 * The GraphHolderPanel that holds all the components used by the population graph
	 */
	private GraphHolderPanel populationHolder;
	/**
	 * The GraphHolderPanel that holds all the components used by the mutability graph
	 */
	private GraphHolderPanel mutabilityHolder;
	/**
	 * The GraphHolderPanel that holds all the components used by the age graph
	 */
	private GraphHolderPanel ageHolder;
	/**
	 * The selected holder that is displaying a graph, null if none is selected
	 */
	private GraphHolderPanel selectedHolder;
	
	/**
	 * The JPanel that holds the graph panel
	 */
	private JPanel graphHolder;
	
	/**
	 * The SimDropDownMenu used to select graphs
	 */
	private SimDropDownMenu graphSelector;

	/**
	 * True if this GUI is currently updating or rendering a graph and should wait to change the selected graph until it is done
	 */
	private boolean updating;
	
	public GraphGui(GuiHandler handler){
		super(handler);
		
		updating = true;
		
		//initialize all graphs
		LineGraph populationGraph = new LineGraph(new LineGraphDetail[]{new LineGraphDetail(2.5f, new Color(100, 100, 230))});
		LineGraph mutabilityGraph = new LineGraph(new LineGraphDetail[]{
				new LineGraphDetail(1.5f, new Color(100, 100, 230)),
				new LineGraphDetail(1.5f, new Color(230, 100, 230)),
				new LineGraphDetail(2.5f, new Color(230, 100, 100))
		});
		LineGraph ageGraph = new LineGraph(new LineGraphDetail[]{
				new LineGraphDetail(1.5f, new Color(100, 100, 230)),
				new LineGraphDetail(1.5f, new Color(230, 100, 230)),
				new LineGraphDetail(2.5f, new Color(230, 100, 100))
		});
		//add all the graphs to a list
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		graphs.add(populationGraph);
		graphs.add(mutabilityGraph);
		graphs.add(ageGraph);
		for(Graph g : graphs){
			//set up graph settings
			g.setWidth(800);
			g.setHeight(400);
			g.setBackgroundColor(new Color(40, 40, 40));
			g.setScaleLinesColor(new Color(200, 200, 200));
			g.setLabelColor(new Color(230, 230, 230));
			g.setLabelFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, 16));
			g.setDisplayXAxisLabels(false);
			g.setDisplayYAxisLabels(true);
			g.setDrawHorizontalLines(true);
			g.setDrawVerticalLines(false);
			g.setLeftSpace(70);
			g.setRightSpace(10);
			g.setTopSpace(10);
			g.setBottomSpace(35);
			g.setDrawDataOnTop(true);
		}
		populationGraph.setMaxDataPoints(Main.SETTINGS.populationGraphMaxPoints.value());
		mutabilityGraph.setMaxDataPoints(Main.SETTINGS.mutabilityGraphMaxPoints.value());
		ageGraph.setMaxDataPoints(Main.SETTINGS.ageGraphMaxPoints.value());
		
		//set up frame
		this.frame = new GraphFrame();
		addOpenCloseControls();
		
		//set up central panel
		JPanel central = new JPanel();
		SimLayoutHandler.createVerticalLayout(central);
		this.frame.add(central);

		//create the drop down menu for selecting a graph
		graphSelector = new SimDropDownMenu();
		graphSelector.setItems(new String[]{OPTION_POPULATION, OPTION_MUTABILITY, OPTION_AGE});
		//add a listener to detect when the item is changed
		graphSelector.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e){
				selectGraph(graphSelector.getSelectedItem());
				renderSelectedGraph();
			}
		});
		central.add(graphSelector);
		
		//create a panel used to display a graph
		graphHolder = new JPanel();
		SimLayoutHandler.createHorizontalLayout(graphHolder);
		central.add(graphHolder);
		
		//set the selected graph holder to none initially
		selectedHolder = null;
		
		//create the population graph panel
		populationHolder = new GraphHolderPanel("Population", populationGraph, this);
		
		//create the mutability graph panel
		mutabilityHolder = new GraphHolderPanel("Mutability, highest, average, lowest", mutabilityGraph, this);
		
		//create the age graph panel
		ageHolder = new GraphHolderPanel("Age (minutes), highest, average, lowest", ageGraph, this);
		
		
		//select the population graph by default
		selectedHolder = null;
		selectGraph(OPTION_POPULATION);
		
		//finish frame set up
		frame.pack();
		absoluteRenderSelectedGraph();
		
		//add clocks event to update and render this graph
		GameClock clock = getHandler().getClock();
		clock.addUpdateEvent(new ClockUpdateEvent(){
			@Override
			public void event(){
				//only update the GUI if this GUI is visible
				if(!getFrame().isVisible()) return;
				
				//if no graph is selected, select the graph from the list
				if(getSelectedHolder() == null) selectGraph(graphSelector.getSelectedItem());
				
				//update the state of the selected graph
				updateSelectedGraph();
			}
		});

		updating = false;
	}
	
	/**
	 * Update the selected graph with the current values in the simulation and redraw it if applicable
	 */
	private void updateSelectedGraph(){
		if(updating) return;
		updating = true;
		
		Thread updateThread = new Thread(new Runnable(){
			@Override
			@SuppressWarnings("unchecked")
			public void run(){
				Simulation sim = getHandler().getSimulation();
				GraphHolderPanel hold = getSelectedHolder();
				ArrayList<Double[]> oldData = null;
				
				//if the selected graph panel holder is not null, copy the data from it's graph
				if(hold != null) oldData = (ArrayList<Double[]>)hold.getGraph().getData().clone();
				//if the holder is null, the select the graph from the list
				else{
					selectGraph(graphSelector.getSelectedItem());
					hold = getSelectedHolder();
				}
				
				//if the handler's simulation is not null, update the simulation data for each corresponding graph, depending on which is selected
				if(sim != null){
					if(hold.equals(populationHolder)){
						//set population data
						ArrayList<Double[]> data = sim.getPopulationData();
						if(data != null && data.size() != 0) getPopulationGraph().setData(data);
					}
					else if(hold.equals(mutabilityHolder)){
						//set mutability data
						ArrayList<Double[]> data = sim.getMutabilityData();
						if(data != null && data.size() != 0) getMutabilityGraph().setData(data);
					}
					else if(hold.equals(ageHolder)){
						//set age data
						ArrayList<Double[]> data = sim.getAgeData();
						if(data != null && data.size() != 0) getAgeGraph().setData(data);
					}
				}
				
				ArrayList<Double[]> holdData = hold.getGraph().getData();
				//if the old data didn't exist, the sizes of the old and new data sizes are not equal, or the data objects are not equal, update the graph's render
				if(oldData == null || oldData.size() != holdData.size() || !oldData.equals(holdData)){
					absoluteRenderSelectedGraph();
				}
				
				updating = false;
			}
		});
		updateThread.start();
	}
	
	/**
	 * Renders the graph ignoring if the graph is updating, should only be used in GraphGui and should be used very carefully
	 */
	synchronized private void absoluteRenderSelectedGraph(){
		Simulation sim = getHandler().getSimulation();
		GraphHolderPanel hold = selectedHolder;
		
		if(sim != null){
			//if the selected graph is not null, render the graph and update the display
			if(hold != null){
				hold.getGraph().render();
				graphHolder.repaint();
			}
		}
	}
	
	/**
	 * Draw the selected graph and repaint the panel holding it
	 */
	public void renderSelectedGraph(){
		if(!updating) absoluteRenderSelectedGraph();
	}
	
	/**
	 * Select a graph to display
	 * @param name the graph, should use constants from this class to select
	 */
	public void selectGraph(String name){
		//reset the graphHolder
		graphHolder.removeAll();
		SimLayoutHandler.createHorizontalLayout(graphHolder);
		
		//add the correct graph frame
		if(name.equals(OPTION_POPULATION)){
			graphHolder.add(populationHolder);
			selectedHolder = populationHolder;
		}
		else if(name.equals(OPTION_MUTABILITY)){
			graphHolder.add(mutabilityHolder);
			selectedHolder = mutabilityHolder;
		}
		else if(name.equals(OPTION_AGE)){
			graphHolder.add(ageHolder);
			selectedHolder = ageHolder;
		}
		
		//pack the frame again
		getFrame().pack();
	}
	
	public GraphHolderPanel getSelectedHolder(){
		return selectedHolder;
	}
	
	public Graph getPopulationGraph(){
		return populationHolder.getGraph();
	}
	/**
	 * Set the maximum number of saved data points for the population graph
	 * @param max
	 */
	public void setPopulationGraphMax(int max){
		getPopulationGraph().setMaxDataPoints(max);
	}

	public Graph getMutabilityGraph(){
		return mutabilityHolder.getGraph();
	}
	/**
	 * Set the maximum number of saved data points for the population graph
	 * @param max
	 */
	public void setMutabilityGraphMax(int max){
		getMutabilityGraph().setMaxDataPoints(max);
	}

	public Graph getAgeGraph(){
		return ageHolder.getGraph();
	}
	/**
	 * Set the maximum number of saved data points for the population graph
	 * @param max
	 */
	public void setAgeGraphMax(int max){
		getAgeGraph().setMaxDataPoints(max);
	}
	
	@Override
	public Window getFrame(){
		return frame;
	}
	
}
