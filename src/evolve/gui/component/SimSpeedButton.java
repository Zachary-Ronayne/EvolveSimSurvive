package evolve.gui.component;

import java.awt.Dimension;
import java.awt.Font;

import evolve.sim.obj.Creature;

/**
 * A button, made for SimSpeedGui, that allows the speed of a Simulation to be set
 */
public class SimSpeedButton extends SimButton{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The amount of time, in 1/100 of a second, the simulation should simulate for
	 */
	private long time;
	
	/**
	 * Create a button that runs the simulation for the given amount of time
	 * @param time the amount of time, in 1/100 of a second
	 */
	public SimSpeedButton(long time){
		super();
		this.time = time;
		Font f = getFont();
		setFont(new Font(f.getFontName(), f.getStyle(), 13));
		String html = "<html><center>Run for:<br>" + Creature.getAproxTimeAmount(this.time) + "</center></html>";
		setText(html);
		setPreferredSize(new Dimension(95, 35));
	}
	
	public long getTime(){
		return time;
	}
	
}
