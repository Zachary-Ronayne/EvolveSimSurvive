package evolve.gui;

import java.awt.Window;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.task.SimThreadPool;
import evolve.util.clock.ClockUpdateEvent;
import evolve.util.clock.GameClock;

/**
 * A central handler that allows each GUI of different kinds to communicate with each other
 */
public class GuiHandler{
	
	private SimGui simGui;
	private SavesGui savesGui;
	private NeuralNetGui neuralNetGui;
	private HelpGui helpGui;
	private SimSpeedGui speedGui;
	private SettingsGui settingsGui;
	private GraphGui graphGui;
	private SortingGui sortingGui;
	
	/**
	 * The simulation object used by this handler
	 */
	private Simulation simulation;
	
	/**
	 * The clock that controls the simulation in this handler
	 */
	private GameClock clock;
	
	/**
	 * The pool of threads that this {@link GuiHandler} can use for updating with multithreading
	 */
	private SimThreadPool threadPool;
	
	public GuiHandler(){
		super();
		this.simGui = null;
		this.savesGui = null;
		this.neuralNetGui = null;
		this.helpGui = null;
		this.speedGui = null;
		this.settingsGui = null;
		this.graphGui = null;
		this.sortingGui = null;
		
		//create the simulation object
		simulation = new Simulation();
		
		//set up the clock to update at 100 times a second
		clock = new GameClock(10, Main.SETTINGS.maxFrameRate.value());
		clock.addUpdateEvent(new ClockUpdateEvent(){
			@Override
			public void event(){
				updateObjectsWithThreads(Main.SETTINGS.numThreads.value());
			}
		});
	}
	
	public void setUpThreadPool(){
		int threads = Main.SETTINGS.numThreads.value();
		if(threadPool != null){
			threadPool.end();
			if(threads == 0){
				threadPool = null;
				return;
			}
		}
		
		threadPool = new SimThreadPool(getSimulation(), threads);
		threadPool.setReadyUpdate(false);
		threadPool.start();
	}
	
	/**
	 * Update the game using threads.<br>
	 * This method only updates objects, no other data
	 * @param numThreads the number of threads, if the number of threads is 0, it updates without using threads
	 */
	public void updateObjectsWithThreads(int numThreads){
		if(threadPool == null) setUpThreadPool();
		if(numThreads == 0){
			getAbsoluteSimulation().updateObjects();
		}
		else{
			//tell the threads to do one more loop iteration, i.e. one more update
			threadPool.nextLoop();
			//then wait until they are done updating
			threadPool.waitLoop();
		}
	}
	
	/**
	 * Perform all the calculations required for updating every aspect of the sim
	 */
	public void updateSimFull(){
		getAbsoluteSimulation().update();
		updateObjectsWithThreads(Main.SETTINGS.numThreads.value());
	}
	
	public SimThreadPool getThreadPool(){
		return threadPool;
	}
	
	/**
	 * Force the ThreadPool to stop running
	 */
	public void endThreadPool(){
		if(threadPool != null){
			threadPool.end();
			threadPool.join();
		}
	}
	
	/**
	 * Get the simulation that this GUI is using
	 * @return the simulation object, null if the clock is not receiving updates
	 */
	public Simulation getSimulation(){
		if(clock.getStopUpdates()) return null;
		else return simulation;
	}
	/**
	 * Get the simulation object of the SimGui. This will always return the Simulation.<br>
	 * Generally this method should not be used outside of set up steps
	 * @return the Simulation object
	 */
	public Simulation getAbsoluteSimulation(){
		return simulation;
	}
	public void setSimulation(Simulation simulation){
		this.simulation = simulation;
	}
	/**
	 * Remove the current simulation from this handler
	 */
	public void deleteSimulation(){
		this.simulation = null;
		getSimGui().updateSimInfo();
	}
	
	/**
	 * Get the clock controlling the handler
	 * @return
	 */
	public GameClock getClock(){
		return clock;
	}
	
	/**
	 * Pause the sim if it is not paused, unpause it if it is paused
	 */
	public void togglePaused(){
		getSimGui().togglePaused();
		getSimGui().updateSimInfo();
	}
	
	public SimGui getSimGui(){
		return simGui;
	}
	public void setSimGui(SimGui simGui){
		this.simGui = simGui;
	}
	
	public SavesGui getSavesGui(){
		return savesGui;
	}
	public void setSavesGui(SavesGui savesGui){
		this.savesGui = savesGui;
	}
	
	public NeuralNetGui getNeuralNetGui(){
		return neuralNetGui;
	}
	public void setNeuralNetGui(NeuralNetGui neuralNetGui){
		this.neuralNetGui = neuralNetGui;
	}

	public HelpGui getHelpGui(){
		return helpGui;
	}
	public void setHelpGui(HelpGui helpGui){
		this.helpGui = helpGui;
	}

	public SimSpeedGui getSpeedGui(){
		return speedGui;
	}
	public void setSpeedGui(SimSpeedGui speedGui){
		this.speedGui = speedGui;
	}

	public SettingsGui getSettingsGui(){
		return settingsGui;
	}
	public void setSettingsGui(SettingsGui settingsGui){
		this.settingsGui = settingsGui;
	}

	public GraphGui getGraphGui(){
		return graphGui;
	}
	public void setGraphGui(GraphGui graphGui){
		this.graphGui = graphGui;
	}

	public SortingGui getSortingGui(){
		return sortingGui;
	}
	public void setSortingGui(SortingGui sortingGui){
		this.sortingGui = sortingGui;
	}
	
	/**
	 * Only call this when all of the GUI objects in this class are not null.<br>
	 * Sets up all the Gui objects to stay pinned to the main SimGui
	 */
	public void setUpGuiPinning(){
		addPinListener(getSavesGui().getFrame());
		addPinListener(getNeuralNetGui().getFrame());
		addPinListener(getHelpGui().getFrame());
		addPinListener(getSpeedGui().getFrame());
		addPinListener(getSettingsGui().getFrame());
		addPinListener(getGraphGui().getFrame());
		addPinListener(getSortingGui().getFrame());
	}
	
	/**
	 * Closes all the extra windows that are not the main SimGui
	 */
	public void closeAllExtraWindows(){
		getSavesGui().getFrame().setVisible(false);
		getNeuralNetGui().getFrame().setVisible(false);
		getHelpGui().getFrame().setVisible(false);
		getSpeedGui().getFrame().setVisible(false);
		getSettingsGui().getFrame().setVisible(false);
		getGraphGui().getFrame().setVisible(false);
		getSortingGui().getFrame().setVisible(false);
	}
	
	/**
	 * Disposes of every window in this {@link GuiHandler}, should only be called when the program is ending
	 */
	public void disposeAllWindows(){
		getSimGui().getFrame().dispose();
		getSavesGui().getFrame().dispose();
		getNeuralNetGui().getFrame().dispose();
		getHelpGui().getFrame().dispose();
		getSpeedGui().getFrame().dispose();
		getSettingsGui().getFrame().dispose();
		getGraphGui().getFrame().dispose();
		getSortingGui().getFrame().dispose();
	}
	
	/**
	 * Add a window listener to the given window that keeps it pinned to the simGui
	 * TODO currently does nothing, need to work on this to make it work properly
	 * @param w
	 */
	private void addPinListener(Window w){
		
		//this code is a place holder for the proper pinning
		w.setAlwaysOnTop(true);
		
		/*
		WindowListener l = new WindowAdapter(){
			private boolean focused = false;
			@Override
			public void windowActivated(WindowEvent e){
				if(!focused){
					w.setAlwaysOnTop(true);
					w.toFront();
					focused = true;
				}
			}
			@Override
			public void windowDeactivated(WindowEvent e){
				if(focused){
					w.setAlwaysOnTop(false);
					focused = false;
				}
			}
		};
		getSimGui().getFrame().addWindowListener(l);
		*/
	}
	
}
