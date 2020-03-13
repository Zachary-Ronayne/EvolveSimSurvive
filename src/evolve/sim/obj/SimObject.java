package evolve.sim.obj;

import evolve.util.Camera;
import evolve.util.Saveable;

/**
 * An interface that all objects that can be used by a Simulation must implement
 */
public interface SimObject extends Saveable{
	
	/**
	 * Store data in this SimObject based on its current attributes. Should always be called before update
	 */
	public void cacheData();
	
	/**
	 * Update this object to it's next state in the simulation it's located in.<br>
	 * This could be things like updating position, calculating energy changes, 
	 * or any calculation that should happen over time
	 */
	public void update();
	
	/**
	 * Renders the object with the given Camera
	 * @param cam the camera
	 */
	public void render(Camera cam);
	
}
