package evolve.util.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import evolve.gui.GuiHandler;
import evolve.gui.SimGui;
import evolve.sim.Simulation;
import evolve.sim.obj.NeuralNetCreature;
import evolve.util.Camera;

public class MouseInputSim extends MouseAdapter{

	/**
	 * The handler used by this MouseInput class
	 */
	private GuiHandler handler;

	public MouseInputSim(GuiHandler handler){
		super();
		this.handler = handler;
	}
	
	public SimGui getSimGui(){
		return handler.getSimGui();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e){
		Simulation simulation = handler.getSimulation();
		if(simulation == null) return;
		
		//zoom in based on the position of the mouse
		simulation.getCamera().zoomIn(e.getX(), e.getY(), -e.getWheelRotation());
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		Simulation simulation = handler.getSimulation();
		if(simulation == null) return;
		
		//on right click, release the camera's anchor
		if(e.getButton() == MouseEvent.BUTTON3){
			simulation.getCamera().releaseAnchor();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		Simulation simulation = handler.getSimulation();
		if(simulation == null) return;
		
		//on right click, set the camera's anchor
		if(e.getButton() == MouseEvent.BUTTON3){
			simulation.getCamera().setAnchor(e.getX(), e.getY());
		}
		
		//on left click, try to select a creature
		if(e.getButton() == MouseEvent.BUTTON1){
			Camera cam = simulation.getCamera();
			double x = cam.mouseX(e.getX());
			double y = cam.mouseY(e.getY());
			NeuralNetCreature creature = nearestCreature(x, y);
			if(handler.getNeuralNetGui() != null) handler.getNeuralNetGui().setSelectedCreature(creature);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		Simulation simulation = handler.getSimulation();
		if(simulation == null) return;
		
		//pan the camera
		simulation.getCamera().pan(e.getX(), e.getY());
		
		//see if a creature should glow
		mouseOverCreature(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		//see if a creature should glow
		mouseOverCreature(e);
	}
	
	/**
	 * Run when the mouse is moved or dragged over a creature to determine if it should have a glow
	 */
	public void mouseOverCreature(MouseEvent e){
		Simulation simulation = handler.getSimulation();
		if(simulation == null) return;
		
		Camera cam = simulation.getCamera();
		NeuralNetCreature creature = nearestCreature(cam.mouseX(e.getX()), cam.mouseY(e.getY()));
		simulation.setHoveredCreature(creature);
	}
	
	/**
	 * Get the nearest creature to the given coordinates
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the nearest creature, null if no creature is found on the mouse
	 */
	public NeuralNetCreature nearestCreature(double x, double y){
		Simulation simulation = handler.getSimulation();
		if(simulation == null) return null;
		
		NeuralNetCreature creature = simulation.getNearestCreature(x, y);
		if(creature != null && creature.getPos().distance(x, y) <= creature.getRadius()) return creature;
		else return null;
	}
	
}
