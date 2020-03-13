package evolve.sim.task;

/**
 * A class that keeps track of code for a {@link SimTask} to run, and if that state is complete yet
 */
public abstract class SimTaskAction{
	
	/**
	 * true if this object has finished it's action, false otherwise
	 */
	private boolean ready;
	
	public SimTaskAction(){
		ready = false;
	}
	
	/**
	 * This method is called by it's associated {@link SimTask} when it should be executed in it's order
	 */
	public abstract void action();
	
	public boolean isReady(){
		return ready;
	}
	public void setReady(boolean ready){
		this.ready = ready;
	}
}
