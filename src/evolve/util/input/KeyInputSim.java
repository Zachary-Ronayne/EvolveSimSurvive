package evolve.util.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import evolve.gui.GuiHandler;
import evolve.sim.Simulation;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.UserCreature;

/**
 * A class that keeps track of keyboard input as used by an associated simulation object
 */
public class KeyInputSim extends KeyAdapter{
	
	/**
	 * The handler used by this KeyInput class
	 */
	private GuiHandler handler;
	
	/**
	 * Keeps track if the P button is down
	 */
	private boolean downP;
	/**
	 * Keeps track if the T button is down
	 */
	private boolean downT;
	/**
	 * Keeps track if the T button is down
	 */
	private boolean downR;
	
	/**
	 * Create a KeyInputSim that uses the associated simulation
	 * @param simulation
	 */
	public KeyInputSim(GuiHandler handler){
		super();
		this.handler = handler;
		downP = false;
		downT = false;
		downR = false;
	}
	
	public GuiHandler getHandler(){
		return handler;
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		Simulation simulation = handler.getSimulation();
		if(simulation == null) return;
		
		int key = e.getKeyCode();
		
		UserCreature user = simulation.getUserCreature();
		boolean userNotNull = user != null;
		if(userNotNull){
			boolean[] pressed = user.getPressedButtons();
			
			//handle user controls
			if(!pressed[UserCreature.UP] && key == KeyEvent.VK_UP) pressed[UserCreature.UP] = true;
			else if(!pressed[UserCreature.DOWN] && key == KeyEvent.VK_DOWN) pressed[UserCreature.DOWN] = true;
			else if(!pressed[UserCreature.LEFT] && key == KeyEvent.VK_LEFT) pressed[UserCreature.LEFT] = true;
			else if(!pressed[UserCreature.RIGHT] && key == KeyEvent.VK_RIGHT) pressed[UserCreature.RIGHT] = true;
			else if(!pressed[UserCreature.EAT] && key == KeyEvent.VK_E) pressed[UserCreature.EAT] = true;
			else if(!pressed[UserCreature.FIGHT] && key == KeyEvent.VK_F) pressed[UserCreature.FIGHT] = true;
			else if(!pressed[UserCreature.VOMIT] && key == KeyEvent.VK_V) pressed[UserCreature.VOMIT] = true;
		}
		

		if(key == KeyEvent.VK_SPACE){
			if(userNotNull) simulation.getCamera().center(user.getX(), user.getY());
			else{
				NeuralNetCreature creature = handler.getNeuralNetGui().getSelectedCreature();
				if(creature != null) simulation.getCamera().center(creature.getX(), creature.getY());
			}
		}
		
		//if p is pressed, toggle the pause state
		else if(!downP && key == KeyEvent.VK_P){
			handler.togglePaused();
			downP = true;
		}
		
		//if t is pressed, toggle the camera locked position
		else if(!downT && key == KeyEvent.VK_T){
			simulation.setLockCamera(!simulation.getLockCamera());
			downT = true;
		}
		
		//if r is pressed, revive the user creature
		else if(!downR && key == KeyEvent.VK_R){
			simulation.reviveUserCreature();
			downR = true;
		}
		
	}
	
	@Override
	public void keyReleased(KeyEvent e){
		Simulation simulation = handler.getSimulation();
		if(simulation == null) return;
		
		int key = e.getKeyCode();

		UserCreature user = simulation.getUserCreature();
		if(user != null){
			boolean[] pressed = user.getPressedButtons();
			
			if(key == KeyEvent.VK_UP) pressed[UserCreature.UP] = false;
			else if(key == KeyEvent.VK_DOWN) pressed[UserCreature.DOWN] = false;
			else if(key == KeyEvent.VK_LEFT) pressed[UserCreature.LEFT] = false;
			else if(key == KeyEvent.VK_RIGHT) pressed[UserCreature.RIGHT] = false;
			else if(key == KeyEvent.VK_E) pressed[UserCreature.EAT] = false;
			else if(key == KeyEvent.VK_F) pressed[UserCreature.FIGHT] = false;
			else if(key == KeyEvent.VK_V) pressed[UserCreature.VOMIT] = false;
		}
		
		if(key == KeyEvent.VK_P) downP = false;
		else if(key == KeyEvent.VK_T) downT = false;
		else if(key == KeyEvent.VK_R) downR = false;
	}
}
