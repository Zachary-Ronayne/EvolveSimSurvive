package evolve.util.clock;

public interface ClockUpdateEvent{
	
	/**
	 * This method is called every time an associated GameClock performs an update
	 */
	public void event();
	
}
