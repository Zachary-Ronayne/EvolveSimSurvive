package evolve.sim.world;

import evolve.util.math.BoundedParabola;

public class River extends BoundedParabola{
	
	/**
	 * The maximum distance from a line on this river to the river's edge
	 */
	private double size;

	public River(double x1, double y1, double x2, double y2, double x3, double y3, double bound1, double bound2, double size){
		super(x1, y1, x2, y2, x3, y3, bound1, bound2);
		this.size = size;
	}
	
	public double getSize(){
		return size;
	}
	
	public void setSize(double size){
		this.size = size;
	}
	
}
