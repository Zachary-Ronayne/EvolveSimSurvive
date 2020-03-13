package evolve.util.clock;

public interface ClockRenderEvent{
	
	/**
	 * This method is called every time an associated GameClock performs a render action
	 */
	public void event();
	
}
