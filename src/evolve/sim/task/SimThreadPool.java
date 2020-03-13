package evolve.sim.task;

import java.util.ArrayList;

import evolve.sim.Simulation;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.tile.Tile;

public class SimThreadPool{
	
	/**
	 * The number of actions that a {@link SimThreadPool} uses
	 */
	public static final int NUM_ACTIONS = 4;
	
	/**
	 * The {@link Simulation} that this {@link SimThreadPool} will update
	 */
	private Simulation simulation;
	
	/**
	 * The number of threads this {@link SimThreadPool} will use for updating
	 */
	private int threadCount;
	
	/**
	 * The array of {@link SimTask} objects that this {@link SimThreadPool} object uses to control its {@link Simulation}
	 */
	private SimTask[] tasks;
	
	/**
	 * The {@link Thread} objects that this {@link SimThreadPool} uses for running its threads
	 */
	private Thread[] threads;
	
	/**
	 * An array that keeps track of if each {@link SimTask} is read to move onto the finished state
	 */
	private boolean[] finishedList;
	
	/**
	 * true if this {@link SimThreadPool} is ready to update
	 */
	private boolean readyUpdate;
	
	public SimThreadPool(Simulation simulation, int threadCount){
		this.simulation = simulation;
		this.threadCount = threadCount;
		
		reset();
	}
	
	/**
	 * Reset the {@link SimThreadPool} based on the current settings in the object
	 */
	public void reset(){
		tasks = new SimTask[this.threadCount];
		threads = new Thread[this.threadCount];
		finishedList = new boolean[this.threadCount];
		
		for(int i = 0; i < tasks.length; i++){
			SimTaskAction[] readyStates = new SimTaskAction[NUM_ACTIONS];
			double low = (double)i / tasks.length;
			double high = (double)(i + 1) / tasks.length;
			tasks[i] = new SimTask(this, simulation, readyStates, i, low, high);

			final int ii = i;
			readyStates[0] = new SimTaskAction(){
				@Override
				public void action(){
					Simulation sim = getSimulation();
					if(sim == null) return;
					Tile[][] grid = sim.getGrid();
					getSimulation().updateTiles(
							(int)(grid.length * getTasks()[ii].getLowBound()),
							(int)(grid.length * getTasks()[ii].getHighBound()));
				}
			};
			readyStates[1] = new SimTaskAction(){
				@Override
				public void action(){
					Simulation sim = getSimulation();
					if(sim == null) return;
					ArrayList<NeuralNetCreature> creatures = sim.getEvolvingCreatures();
					getSimulation().cacheCreatures(
							(int)(creatures.size() * getTasks()[ii].getLowBound()),
							(int)(creatures.size() * getTasks()[ii].getHighBound()));
				}
			};
			readyStates[2] = new SimTaskAction(){
				@Override
				public void action(){
					Simulation sim = getSimulation();
					if(sim == null) return;
					ArrayList<NeuralNetCreature> creatures = sim.getEvolvingCreatures();
					getSimulation().thinkCreatures(
							(int)(creatures.size() * getTasks()[ii].getLowBound()),
							(int)(creatures.size() * getTasks()[ii].getHighBound()));
				}
			};
			readyStates[3] = new SimTaskAction(){
				@Override
				public void action(){
					Simulation sim = getSimulation();
					if(sim == null) return;
					ArrayList<NeuralNetCreature> creatures = sim.getEvolvingCreatures();
					getSimulation().updateCreatures(
							(int)(creatures.size() * getTasks()[ii].getLowBound()),
							(int)(creatures.size() * getTasks()[ii].getHighBound()));
				}
			};
			
			finishedList[i] = false;
		}
	}
	
	/**
	 * Start up all of the {@link SimTask} objects used by this {@link SimThreadPool}
	 */
	public void start(){
		for(int i = 0; i < getThreadCount(); i++){
			threads[i] = new Thread(tasks[i]);
			tasks[i].setRunning(true);
			threads[i].start();
		}
		setReadyUpdate(false);
	}

	/**
	 * Determine if all of the {@link SimTask} objects have finished processing the action of the given index
	 * @param i the index of the action to check for finished
	 * @return
	 */
	public boolean doneAction(int i){
		boolean result = true;
		for(SimTask t : tasks) result &= t.getReadyStates()[i].isReady();
		return result;
	}

	/**
	 * Determine if all threads are done, meaning the entire system is done
	 * @return
	 */
	public boolean finished(){
		boolean result = true;
		for(SimTask t : tasks){
			result &= t.isFinished();
		}
		return result;
	}
	
	/**
	 * Determine if all threads have started, meaning the entire system is ready to begin processing
	 * @return
	 */
	public boolean started(){
		boolean result = true;
		for(SimTask t : tasks){
			result &= t.isStarted();
		}
		return result;
	}
	
	public SimTask[] getTasks() {
		return tasks;
	}
	
	public Simulation getSimulation(){
		return simulation;
	}
	public void setSimulation(Simulation simulation){
		this.simulation = simulation;
	}

	public int getThreadCount(){
		return threadCount;
	}
	public void setThreadCount(int threadCount){
		this.threadCount = threadCount;
		end();
		reset();
		start();
	}

	public boolean isReadyUpdate(){
		return readyUpdate;
	}
	public void setReadyUpdate(boolean readyUpdate){
		this.readyUpdate = readyUpdate;
	}
	
	/**
	 * Determine if all of the {@link SimTask} objects are finished their current operation
	 * @return true if every task is finished, false otherwise
	 */
	public boolean loopDone(){
		boolean result = true;
		for(boolean b : finishedList){
			result &= b;
		}
		return result;
	}
	
	/**
	 * Tell the pool that the {@link SimTask} with the given index has finished
	 * @param index the index of the {@link SimTask} that finished
	 */
	public void taskFinished(int index){
		finishedList[index] = true;
	}
	
	/**
	 * Determine if the {@link SimTask} with the given index is finished
	 * @param index the index to check
	 * @return true if the index is finished, false otherwise
	 */
	public boolean taskIsFinished(int index){
		return finishedList[index];
	}
	
	/**
	 * Tells each {@link SimTask} to perform the next loop
	 */
	public void nextLoop(){
		for(int i = 0; i < finishedList.length; i++){
			finishedList[i] = false;
			tasks[i].setStarted(false);
		}
		setReadyUpdate(true);
		
		while(!started()){
			try{Thread.sleep(0, 1);}catch(Exception e){}
		}

		setReadyUpdate(false);
	}
	
	/**
	 * Wait for each {@link SimTask} to finish the current loop they are on
	 */
	public void waitLoop(){
		int kick = 0;
		int maxKick = 3000;
		while(!loopDone() && kick < maxKick){
			kick++;
			try{Thread.sleep(0, 1);}catch(Exception e){}
		}
		if(kick == maxKick){
			System.err.println("SimThreadPool took too long to wait");
			end();
			join();
			start();
		}
	}
	
	/**
	 * End all of the threads in this {@link SimThreadPool} and wait until they finish their final loop
	 */
	public void end(){
		for(SimTask t : tasks){
			t.setRunning(false);
			t.resetReadyStates();
			t.setStarted(true);
			t.setFinished(true);
		}
		for(int i = 0; i < finishedList.length; i++) finishedList[i] = true;
	}
	
	/**
	 * Join every thread in this pool and wait until they finish
	 */
	public void join(){
		for(Thread t : threads) {
			try{
				if(t != null) t.join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
