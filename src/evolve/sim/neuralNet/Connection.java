package evolve.sim.neuralNet;

import java.io.PrintWriter;
import java.util.Scanner;

import evolve.sim.misc.Gene;
import evolve.util.Camera;
import evolve.util.Saveable;

/**
 * A connection for a NeuralNet Node
 */
public class Connection implements Saveable, Mutateable{
	
	/**
	 * The weight of this connection
	 */
	private Gene weight;
	
	/**
	 * Create a new Node connection with the given weight
	 * @param weight the weight, must be in range of [-1, 1] will automatically be adjusted if necessary
	 */
	public Connection(double weight){
		this.weight = new Gene(weight);
	}

	public Gene getWeight(){
		return weight;
	}
	public void setWeight(Gene weight){
		this.weight = weight;
	}

	/**
	 * Draw this Connection with the given Camera object
	 * @param cam the Camera object
	 * @param right the number of nodes above this node towards the output layer
	 */
	public void render(Camera cam, int left){
		cam.getG().setColor(NeuralNet.getValueColor(getWeight().getValue(), true));
		cam.drawLine(NeuralNet.NODE_X_DIST, 0, 0, left * NeuralNet.NODE_Y_DIST);
	}
	/**
	 * Mutate this Connection.<br>
	 * Mutations change the weight of this connection
	 * @param mutability the rate at which mutations occur
	 */
	@Override
	public void mutate(double mutability, double chance){
		weight.mutate(mutability, chance);
	}
	
	/**
	 * Create a Connection that is a combination of this Connection and the given Connection.<br>
	 * Each value in the Connection is taken as a weighted average between this Connection and the parent, the weights are random for each value
	 * @param parent the Connection to combine genetic information
	 * @return the combination Connection, null if one failed to generate
	 */
	public Connection parentCopy(Connection parent){
		Connection c = copy();
		c.setWeight(getWeight().parentCopy(parent.getWeight()));
		
		return c;
	}
	
	public Connection copy(){
		Connection c = new Connection(0);
		c.weight = weight.copy();
		return c;
	}
	
	@Override
	public boolean save(PrintWriter write){
		return getWeight().save(write);
	}

	@Override
	public boolean load(Scanner read){
		return getWeight().load(read);
	}
}
