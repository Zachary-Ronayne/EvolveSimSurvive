package evolve.sim.neuralNet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.util.Scanner;

import evolve.gui.component.SimConstants;
import evolve.sim.misc.Gene;
import evolve.util.ArrayHandler;
import evolve.util.Camera;
import evolve.util.Saveable;
import evolve.util.math.Calc;

/**
 * A node for a feed forward NeuralNet
 */
public class Node implements Saveable, Mutateable{
	
	/**
	 * The connections that feed into this Node
	 */
	private Connection[] connections;
	
	/**
	 * The bias of this node, determines the initial value before connections feed into it
	 */
	private Gene bias;
	
	/**
	 * The current value of this node based on it's last calculation
	 */
	private double value;
	
	/**
	 * Create a Node that has weights.length number of nodes feeding into it, and the given bias
	 * @param weights the weights feeding into this Node, all weights must be in the range [-1, 1] and will be adjusted autamatically if needed
	 * @param bias the bias of this node
	 */
	public Node(double[] weights, double bias){
		this.bias = new Gene(-2, 2, bias);
		connections = new Connection[weights.length];
		for(int i = 0; i < connections.length; i++){
			connections[i] = new Connection(weights[i]);
		}
	}
	
	/**
	 * Create a Node that has numConnections weights where all weights and the bias of this node are randomized
	 * @param numConnections the number of connections for this node
	 */
	public Node(int numConnections){
		this(getRandomWeights(numConnections), Math.random() * 2 - 1);
	}
	/**
	 * Get the current value of this node
	 * @return the value
	 */
	public double getValue(){
		return value;
	}
	/**
	 * Set the value of this node
	 * @param value the value, it must be in the range [-1, 1] and it will be change to be in that range if needed
	 */
	public void setValue(double value){
		this.value = Math.max(-1, Math.min(1, value));
	}
	/**
	 * Feed the given nodes into this node, find the calculated value from those nodes, and store it in value.<br>
	 * The length of nodes must be equal to the length of the connections of this Node
	 * @param nodes the given nodes
	 */
	public void calculateValue(Node[] nodes){
		double total = getBias().getValue();
		for(int i = 0; i < nodes.length; i++){
			total += nodes[i].getValue() * connections[i].getWeight().getValue();
		}
		setValue(Calc.sigmoid(total));
	}
	
	public Connection[] getConnections(){
		return connections;
	}
	
	/**
	 * Remove the connection of this {@link Node} with the given index
	 * @param index the index to remove
	 */
	public void removeConnection(int index){
		if(connections.length <= 0) return;
		connections = ArrayHandler.remove(connections, index);
	}
	
	/**
	 * Remove every connection from this Node
	 */
	public void removeAllConnections(){
		connections = new Connection[0];
	}
	
	/**
	 * Pick a random connection from this node, copies it, and adds the copy to the total number of connections.<br>
	 * If no {@link Connection} exists, then one is randomly generated
	 */
	public void addConnection(){
		Connection c;
		if(connections.length > 0) c = connections[(int)(Math.random() * connections.length)].copy();
		else c = new Connection(Math.random() * 2 - 1);
		
		connections = ArrayHandler.add(connections, c);
	}
	
	public Gene getBias(){
		return bias;
	}
	public void setBias(Gene bias){
		this.bias = bias;
	}
	
	/**
	 * Draw this Node with the given Camera object
	 * @param cam the Camera object
	 * @param down the number of nodes above this node
	 */
	public void render(Camera cam, int down){
		renderConnections(cam, down);
		renderNode(cam);
	}
	
	/**
	 * Draw this Node with the given Camera object, does not draw any connections
	 * @param cam the Camera object
	 * @param down the number of nodes above this node
	 */
	public void renderNode(Camera cam){
		Graphics g = cam.getG();
		double r = NeuralNet.NODE_RADIUS;
		g.setColor(Color.BLACK);
		cam.fillOval(-r, -r, r * 2, r * 2);
		g.setColor(NeuralNet.getValueColor(getValue(), false));
		cam.fillOval(-r + 2, -r + 2, r * 2 - 4, r * 2 - 4);
		
		double size = NeuralNet.NODE_RADIUS * .8;
		g.setColor(Color.BLACK);
		g.setFont(new Font(SimConstants.FONT_NAME, Font.BOLD, (int)size));
		cam.drawScaleString("" + (int)(getValue() * 1000), -size * .9, size / 2);
	}
	/**
	 * Draw this Node's connections with the given Camera object, does not draw the actual node
	 * @param cam the Camera object
	 * @param down the number of nodes above this node
	 */
	public void renderConnections(Camera cam, int down){
		double camX = cam.getX();
		cam.setX(camX + NeuralNet.NODE_X_DIST);
		for(int i = 0; i < connections.length; i++){
			connections[i].render(cam, i - down);
		}
		cam.setX(camX);
	}

	/**
	 * Mutate this Node.<br>
	 * Mutations change the weights and bias of this node and its connections respectively
	 * @param mutability the rate at which mutations occur
	 */
	@Override
	public void mutate(double mutability, double chance){
		bias.mutate(mutability, chance);
		for(Connection c : connections) c.mutate(mutability, chance);
	}

	/**
	 * Create a Node that is a combination of this Node and the given Node.<br>
	 * Each value in the Node is taken as a weighted average between this Node and the parent, the weights are random for each value<br>
	 * This Node and parent must be the same size
	 * @param parent the Node to combine genetic information
	 * @return the combination Node, null if one failed to generate
	 */
	public Node parentCopy(Node parent){
		Node copy = copy();
		copy.setBias(getBias().parentCopy(parent.getBias()));
		for(int i = 0; i < connections.length; i++){
			copy.connections[i] = connections[i].parentCopy(parent.connections[i]);
		}
		return copy;
	}
	
	public Node copy(){
		Node n = new Node(connections.length);
		for(int i = 0; i < n.connections.length; i++){
			n.connections[i] = connections[i].copy();
		}
		n.setBias(bias.copy());
		n.setValue(value);
		
		return n;
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			boolean success = true;

			success &= bias.save(write);
			
			write.println(value + " " + connections.length);
			for(Connection c : connections) c.save(write);
			
			return success;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean load(Scanner read){
		try{
			boolean success = true;

			success &= bias.load(read);
			
			value = read.nextDouble();
			
			connections = new Connection[read.nextInt()];
			for(int i = 0; i < connections.length; i++){
				connections[i] = new Connection(0);
				connections[i].load(read);
			}
			
			return success;
		}catch(Exception e){
			return false;
		}
	}
	
	public static double[] getRandomWeights(int numConnections){
		double[] weights = new double[numConnections];
		for(int i = 0; i < weights.length; i++){
			weights[i] = Math.random() * 2 - 1;
		}
		return weights;
	}
}
