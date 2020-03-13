package evolve.sim.task;

import evolve.sim.Simulation;

/**
 * A class that keeps track of and performs a task on a {@link Simulation} to update it as a part of a thread pool.<br>
 * This class should only be used by {@link SimThreadPool} to use them with each other
 */
public class SimTask implements Runnable{
	
	/**
	 * The {@link Simulation} that this {@link SimTask} updates
	 */
	private Simulation simulation;
	
	/**
	 * The {@link SimThreadPool} that this {@link SimTask} is running on
	 */
	private SimThreadPool pool;
	
	/**
	 * The index of this {@link SimTask} as used by its {@link SimThreadPool}
	 */
	private int index;
	
	/**
	 * The low percentage position of the list of objects that this {@link SimTask} will update.<br>
	 * For example, if this is 0.5, then this task will start updating halfway through the list
	 */
	private double lowBound;
	
	/**
	 * The high percentage position of the list of objects that this {@link SimTask} will update
	 * For example, if this is 1, then this task will end updating at the end of the list
	 */
	private double highBound;
	
	/**
	 * Keeps track of all the states for if this {@link SimTask} is ready to move onto the text stage
	 */
	private SimTaskAction[] readyState;
	/**
	 * True if this {@link SimTask} has started a loop, false if it is waiting to start a new loop,<br>
	 * false otherwise, meaning other tasks must wait until this value is true to continue
	 */
	private boolean started;
	
	/**
	 * True if this {@link SimTask} is done performing it's current loop iteration<br>
	 * false otherwise, meaning other tasks will wait until this value is true to continue
	 */
	private boolean finished;
	
	
	/**
	 * True if the task should keep running, false otherwise
	 */
	private boolean running;

	public SimTask(SimThreadPool pool, Simulation simulation, SimTaskAction[] actions, int index, double lowBound, double highBound){
		this.pool = pool;
		this.simulation = simulation;
		this.index = index;
		this.lowBound = lowBound;
		this.highBound = highBound;
		this.readyState = actions;
		
		this.started = true;
		this.finished = false;
		
		this.running = true;
	}
	
	@Override
	public void run(){
		while(running){
			//wait until the pool has started
			while(!pool.started()){
				setStarted(true);
				if(!isRunning()) return;
				try{Thread.sleep(0, 1);}catch(Exception e){}
			}
			
			setFinished(false);
			
			//set up all variables for keeping track of what part of the task this task can move onto
			resetReadyStates();

			//go through all of the readyState objects in their correct order
			for(int i = 0; i < readyState.length; i++){
				SimTaskAction a = readyState[i];
				a.action();
				while(!pool.doneAction(i)){
					a.setReady(true);
					try{Thread.sleep(0, 1);}catch(Exception e){}
				}
			}
			
			//wait for all of the threads in the pool to finish
			pool.taskFinished(getIndex());
			
			while(!pool.finished()){
				setFinished(true);
				try{Thread.sleep(0, 1);}catch(Exception e){}
			}
			
			//if the thread is still running, the thread should wait until the pool says to start the next loop iteration
			while(isRunning() && !pool.isReadyUpdate()){
				try{Thread.sleep(0, 1);}catch(Exception e){}
				if(!pool.loopDone()) pool.taskFinished(getIndex());
			}
		}
	}

	/**
	 * Reset all of the ready states to false
	 */
	public void resetReadyStates(){
		for(SimTaskAction a : readyState) a.setReady(false);
	}
	
	/**
	 * Get the array of {@link SimTaskAction} objects used by this SimTask
	 * @return
	 */
	public SimTaskAction[] getReadyStates(){
		return readyState;
	}

	public Simulation getSimulation(){
		return simulation;
	}
	public void setSimulation(Simulation simulation){
		this.simulation = simulation;
	}

	public double getLowBound(){
		return lowBound;
	}
	public void setLowBound(double lowBound){
		this.lowBound = lowBound;
	}

	public double getHighBound(){
		return highBound;
	}
	public void setHighBound(double highBound){
		this.highBound = highBound;
	}
	
	public boolean isStarted(){
		return started;
	}
	public void setStarted(boolean started){
		this.started = started;
	}

	public boolean isFinished(){
		return finished;
	}
	public void setFinished(boolean finished){
		this.finished = finished;
	}
	
	public boolean isRunning(){
		return running;
	}
	public void setRunning(boolean running){
		this.running = running;
	}
	
	public int getIndex(){
		return index;
	}
	
}
