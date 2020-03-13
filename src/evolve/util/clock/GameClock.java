package evolve.util.clock;

import java.util.ArrayList;

/**
 * A class that handles updating and rendering in consistent intervals with variable maximum frame rate
 */
public class GameClock{
	
	/**
	 * the delay of this clock
	 */
	private long delay;
	
	/**
	 * The maximum frame rate of this clock, set to 0 or number less than 0 for unlimited frame rate
	 */
	private double maxFrameRate;
	
	/**
	 * While true, this clock will not update at all until this is set to false
	 */
	private boolean stopUpdates;

	/**
	 * true of this GameClock is updating the state of a simulation, false otherwise
	 */
	private boolean updating;
	/**
	 * true of this GameClock is rendering the state of a simulation, false otherwise
	 */
	private boolean rendering;
	
	/**
	 * true if this GameClock is running, false if it is not
	 */
	private boolean running;

	/**
	 * A list of events that are called every update
	 */
	private ArrayList<ClockUpdateEvent> updateEvents;
	
	/**
	 * A list of events that are called every render
	 */
	private ArrayList<ClockRenderEvent> renderEvents;
	
	/**
	 * Create a GameClock that executes an update every delay milliseconds
	 * @param delay the delay in milliseconds
	 * @param simulation the simulation this GameClock should update
	 * @param simGui the SimGui that this GameClock uses
	 */
	public GameClock(long delay, double maxFrameRate){
		this.delay = delay * 1000000l;
		this.maxFrameRate = maxFrameRate;
		
		//variables used to keep track of the state of the updating and rendering
		this.updating = false;
		this.rendering = false;

		this.stopUpdates = true;

		this.updateEvents = new ArrayList<ClockUpdateEvent>();
		
		this.renderEvents = new ArrayList<ClockRenderEvent>();
		
		this.running = false;
	}
	
	/**
	 * Causes the clock to finish the update and render operations it's currently working on, and then stop running
	 */
	public void stopClock(){
		running = false;
	}
	
	/**
	 * If the clock is not already running, the clock will start up
	 */
	public void startClock(){
		//if the clock is running, don't start it again
		if(running) return;
		
		running = true;
		stopUpdates = false;
		
		//set up render thread
		Thread render = new Thread(new Runnable(){
			@Override
			public void run(){
				//should the number of frames per second be printed
				final boolean printFrames = false;
				//the number of nanoseconds in a second
				final long nanoSecond = 1000000000l;
				
				//variables to keep track of frames per second, currently unused
				long fps = 0;
				long lastTime = -1;
				
				//the amount of time it took to render a frame
				long renderTime = -1;
				//the time that the last frame was drawn at
				long lastRender = System.nanoTime();
				
				//keep running this thread while the clock is running
				while(running){
					if(!getStopUpdates()){
						try{
							long thisTime = System.nanoTime();
							long fpsTime = thisTime - lastTime;
							if(lastTime == -1 || fpsTime >= nanoSecond){
								if(printFrames) System.out.println("FPS: " + fps / (fpsTime / (double)nanoSecond));
								lastTime = System.nanoTime();
								fps = 0;
							}
							
							//the maximum number of frames drawn per second
							double fpsLimit = getMaxFrameRate();
							boolean limited = fpsLimit > 0;
							//the number of nanoseconds in a frame of the maximum frame rate
							double frameTime;
							if(limited) frameTime = nanoSecond / fpsLimit;
							else frameTime = Long.MAX_VALUE;
							
							//determine the time it has been since the last frame was drawn
							long sinceLast = System.nanoTime() - lastRender;
							
							//render a frame if frame rate is unlimited, if it has been too long since a frame was rendered, or if enough time has passed to render a frame
							if(!limited || renderTime >= frameTime || frameTime - renderTime < sinceLast){
								long time = System.nanoTime();
								render();
								fps++;
								renderTime = System.nanoTime() - time;
								lastRender = System.nanoTime();
							}
						}catch(Exception e){
							System.err.println("GameClock encountered a render error");
							e.printStackTrace();
							lastTime = System.nanoTime();
							fps = 0;
							lastRender = System.nanoTime();
							renderTime = 0;
						}
					}
				}
			}
		});
		//set up update thread
		Thread update = new Thread(new Runnable(){
			@Override
			public void run(){
				long lastTime = -1;
				while(running){
					if(!getStopUpdates()){
						try{
							long currentTime = System.nanoTime();
							//if it has been delay time since last update, update again
							if(lastTime == -1 || currentTime - lastTime >= getDelay()){
								update();
								lastTime = System.nanoTime();
							}
						}catch(Exception e){
							System.err.println("GameClock encountered an update error");
							e.printStackTrace();
							lastTime = System.nanoTime();
						}
					}
				}
			}
		});
		update.start();
		render.start();
	}
	
	/**
	 * This is called each time the timer should update based on the delay. 
	 * This will both update the state of the game to the next frame and render the next frame
	 */
	public void update(){
		updating = true;
		ArrayList<ClockUpdateEvent> events = new ArrayList<ClockUpdateEvent>();
		events.addAll(updateEvents);
		for(ClockUpdateEvent e : events) e.event();
		updating = false;
	}
	
	public void render(){
		rendering = true;
		ArrayList<ClockRenderEvent> events = new ArrayList<ClockRenderEvent>();
		events.addAll(renderEvents);
		for(ClockRenderEvent e : events) e.event();
		rendering = false;
	}
	
	public double getMaxFrameRate(){
		return maxFrameRate;
	}
	public void setMaxFrameRate(double maxFrameRate){
		this.maxFrameRate = maxFrameRate;
	}
	
	/**
	 * The delay of this GameClock, in nanoseconds
	 * @return the delay
	 */
	public long getDelay(){
		return delay;
	}
	/**
	 * Set the delay of this GameClock in nanoseconds
	 * @param delay the new delay
	 */
	public void setDelay(long delay){
		this.delay = delay;
	}
	
	/**
	 * The delay of this GameClock, in milliseconds
	 * @return the delay
	 */
	public long getDelayMilli(){
		return getDelay() / 1000000l;
	}
	/**
	 * Set the delay of this GameClock in milliseconds
	 * @param delay the new delay
	 */
	public void setDelayMilli(long delay){
		setDelay(delay * 1000000l);
	}
	
	public boolean getStopUpdates(){
		return stopUpdates;
	}
	public void setStopUpdates(boolean stopUpdates){
		this.stopUpdates = stopUpdates;
	}
	
	public boolean isUpdating(){
		return updating;
	}
	public boolean isRendering(){
		return rendering;
	}
	
	/**
	 * Add an update event to this Clock. The event is activated every update cycle
	 * @param e the event
	 */
	public void addUpdateEvent(ClockUpdateEvent e){
		updateEvents.add(e);
	}
	
	/**
	 * Add a render event to this Clock. The event is activated every render cycle
	 * @param e the event
	 */
	public void addRenderEvent(ClockRenderEvent e){
		renderEvents.add(e);
	}
}
