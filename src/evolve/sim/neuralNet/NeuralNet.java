package evolve.sim.neuralNet;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Scanner;

import evolve.util.ArrayHandler;
import evolve.util.Camera;
import evolve.util.Saveable;

/**
 * A feed forward neural network
 */
public class NeuralNet implements Saveable, Mutateable{
	
	/**
	 * The radius of a node as displayed when rendered with a Camera
	 */
	public static final double NODE_RADIUS = 8;

	/**
	 * The distance between the center of nodes on the x axis when rendered with a Camera
	 */
	public static final double NODE_X_DIST = NODE_RADIUS * 2 + 60;
	/**
	 * The distance between the center of nodes on the y axis when rendered with a Camera
	 */
	public static final double NODE_Y_DIST = NODE_RADIUS * 2 + 1;
	
	/**
	 * The layers of Nodes in the NeuralNet.<br>
	 * The first dimension is the specific layer, the second is the node within the layer<br>
	 * layers[0] is the input layer.<br>
	 * layers[layers.length - 1] is the output layer
	 */
	private Node[][] layers;
	
	/**
	 * Create a neural network with the given sizes of layers
	 * @param layerValues the length of this array is the number of layers, must be at least 2.<br>
	 * 	The value in each array index is the number of Nodes in that layer.<br>
	 * 	For example:<br>
	 * {2, 4, 3} would create a NeuralNet with 2 input nodes, 1 hidden layer with 4 nodes, and 3 output nodes<br>
	 * {5, 6} would create a NeuralNet with 5 input nodes, no hidden layers, and 6 output nodes<br>
	 * {7, 4, 3, 9} would create a NeuralNet with 7 input nodes, 2 hidden layers, the first with 4 nodes and the second with 3, and then 9 output nodes<br>
	 */
	public NeuralNet(int[] layerSizes){
		if(layerSizes.length < 2) throw new IllegalArgumentException("layerSizes must be at least length 2");
		
		//set up initial layers
		layers = new Node[layerSizes.length][0];
		
		for(int i = 0; i < layers.length; i++){
			//set the length of the the ith layer to the correct size
			layers[i] = new Node[layerSizes[i]];
			//initialize each node
			for(int j = 0; j < layers[i].length; j++){
				if(i == 0) layers[i][j] = new Node(0);
				else{
					layers[i][j] = new Node(layerSizes[i - 1]);
				}
			}
		}
	}
	
	/**
	 * Get the array of the layers of nodes in this {@link NeuralNet}
	 * @return the array of nodes
	 */
	public Node[][] getLayers(){
		return layers;
	}
	
	/**
	 * Based on the current input values, determine the output values
	 */
	public void calculateOutputs(){
		//for each layer, starting with the first layer after the input layer and ending with the output layer
		for(int i = 1; i < layers.length; i++){
			//get the nodes of the layer before the ith layer
			Node[] input = layers[i - 1];
			//for each node in that layer, calculate it's value
			for(int j = 0; j < layers[i].length; j++){
				layers[i][j].calculateValue(input);
			}
		}
	}
	
	/**
	 * Feed inputs into the first layer of the neural network
	 * @param in the inputs, must be the same length as the first layer
	 */
	public void feedInputs(double[] in){
		for(int i = 0; i < in.length; i++){
			layers[0][i].setValue(in[i]);
		}
	}
	/**
	 * Get the values of the outputs of this NeuralNet
	 * @return the values
	 */
	public double[] getOutputs(){
		Node[] out = layers[layers.length - 1];
		double[] values = new double[out.length];
		for(int i = 0; i < values.length; i++){
			values[i] = out[i].getValue();
		}
		
		return values;
	}
	
	/**
	 * Get the length of the input layer
	 * @return
	 */
	public int getInputSize(){
		return layers[0].length;
	}

	/**
	 * Get the length of the output layer
	 * @return
	 */
	public int getOutputSize(){
		return layers[layers.length - 1].length;
	}
	
	/**
	 * Get the length of the layer of nodes with the most nodes
	 * @return
	 */
	public int getLargestLayer(){
		int high = 0;
		for(int i = 1; i < layers.length; i++){
			if(layers[i].length > layers[high].length) high = i;
		}
		return layers[high].length;
	}
	
	/**
	 * Get the number of layers in this NeuralNet, including the input and output layers
	 * @return the number of layers
	 */
	public int getNumberOfLayers(){
		return layers.length;
	}
	
	/**
	 * Determine if this NeuralNet and the given NeuralNet have the same number of nodes in each layer
	 * @param brain the NeuralNet to compare
	 * @return true if both NeuralNets have the same number of nodes in each layer, false otherwise
	 */
	public boolean sameSize(NeuralNet brain){
		if(getNumberOfLayers() != brain.getNumberOfLayers()) return false;
		
		for(int i = 0; i < layers.length; i++){
			if(layers[i].length != brain.layers[i].length) return false;
		}
		
		return true;
	}

	/**
	 * Remove a node with the given index from the given layer
	 * @param index the index
	 * @param layer the layer
	 */
	public void removeNode(int index, int layer){
		//first remove the node itself
		layers[layer] = ArrayHandler.remove(layers[layer], index);
		
		//now remove connections to the next layer based on the previously existing connections
		//only need to do this if this is not the last layer
		if(layer < layers.length - 1){
			for(int i = 0; i < layers[layer + 1].length; i++){
				layers[layer + 1][i].removeConnection(index);
			}
		}
	}

	/**
	 * Remove an input node with the given index
	 * @param index the index
	 */
	public void removeInputNode(int index){
		removeNode(index, 0);
	}
	
	/**
	 * Remove an output node with the given index
	 * @param index the index
	 */
	public void removeOutputNode(int index){
		removeNode(index, layers.length - 1);
	}
	
	/**
	 * Add the given node to the node list of the given index
	 * @param n the node to add
	 */
	public void addNode(Node n, int layer){
		layers[layer] = ArrayHandler.add(layers[layer], n);
		
		//now add random connections to the next layer based on the previously existing connections
		//only need to do this if this is not the last layer
		if(layer < layers.length - 1){
			for(int i = 0; i < layers[layer + 1].length; i++){
				layers[layer + 1][i].addConnection();
			}
		}
	}

	/**
	 * Add the given node to input node list
	 * @param n the node to add
	 */
	public void addInputNode(Node n){
		addNode(n, 0);
	}

	/**
	 * Add the given node to output node list
	 * @param n the node to add
	 */
	public void addOutputNode(Node n){
		addNode(n, layers.length - 1);
	}
	
	/**
	 * Draw this NeuralNet with the given Camera object
	 * @param cam the Camera object
	 */
	public void render(Camera cam){
		
		double camX = cam.getX();
		//draw nodes themselves
		for(int i = 0; i < layers.length; i++){
			double camY = cam.getY();
			for(int j = 0; j < layers[i].length; j++){
				layers[i][j].renderConnections(cam, j);
				cam.setY(cam.getY() - NODE_Y_DIST);
			}
			cam.setX(cam.getX() - NODE_X_DIST);
			cam.setY(camY);
		}
		cam.setX(camX);
		
		camX = cam.getX();
		//draw node connections
		for(int i = 0; i < layers.length; i++){
			double camY = cam.getY();
			for(int j = 0; j < layers[i].length; j++){
				layers[i][j].renderNode(cam);
				cam.setY(cam.getY() - NODE_Y_DIST);
			}
			cam.setX(cam.getX() - NODE_X_DIST);
			cam.setY(camY);
		}
		cam.setX(camX);
		
	}
	
	/**
	 * Mutate this NeuralNet.<br>
	 * Mutations change the weights and biases of the nodes and connections respectively
	 * @param mutability the rate at which mutations occur
	 */
	@Override
	public void mutate(double mutability, double chance){
		for(Node[] layer : layers){
			for(Node n : layer) n.mutate(mutability, chance);
		}
	}
	
	/**
	 * Create a NerualNet that is a combination of this {@link NeuralNet} and the given NeuralNet.<br>
	 * Each value in the {@link NeuralNet} is taken as a weighted average between this {@link NeuralNet} and the parent, the weights are random for each value.<br>
	 * If the {@link NeuralNet} objects are different sizes, then a random {@link NeuralNet} is selected,
	 * each with an equal change, and that {@link NeuralNet} is used as the size
	 * @param parent the NeuralNet to combine genetic information
	 * @return the combination NeuralNet, null if one failed to generate
	 */
	public NeuralNet parentCopy(NeuralNet parent){
		NeuralNet copy;
		if(sameSize(parent) || Math.random() > .5){
			copy = copy();
		}
		else copy = parent.copy();
		
		
		for(int i = 0; i < copy.layers.length; i++){
			Node[] layer;
			//find the number layers in the parents brain
			int length = parent.layers.length;
			//if the current index is inside the range, then copy with that layer from the parent
			if(i < length) layer = parent.layers[i];
			//otherwise, use the layer before the last layer, which will be the last hidden layer, or the input layer if no hidden layers exist
			else layer = parent.layers[length - 2];
			
			for(int j = 0; j < copy.layers[i].length; j++){
				Node thisNode = copy.layers[i][j].copy();
				Node pNode;
				//find the length of the number of nodes in the parents layer
				int nodeLength = layer.length;
				//if the current index is inside the range, then copy that node from the parent
				if(j < nodeLength) pNode = layer[j].copy();
				//otherwise, use the last node in the list
				else pNode = layer[nodeLength - 1].copy();
				
				//ensure that the number of connections in both nodes is the correct length
				//only need to check when the layer is after the input layer
				if(i >= 1){
					int conLength = copy.layers[i - 1].length;
					while(pNode.getConnections().length > conLength) pNode.removeConnection(pNode.getConnections().length - 1);
					while(pNode.getConnections().length < conLength) pNode.addConnection();

					while(thisNode.getConnections().length > conLength) thisNode.removeConnection(thisNode.getConnections().length - 1);
					while(thisNode.getConnections().length < conLength) thisNode.addConnection();
				}
				//if it is the input layer, then all connections should be removed
				else{
					pNode.removeAllConnections();
					thisNode.removeAllConnections();
				}
				
				//parent copy the nodes
				copy.layers[i][j] = thisNode.parentCopy(pNode);
			}
		}
		return copy;
	}
	
	public NeuralNet copy(){
		int[] layerSizes = new int[layers.length];
		for(int i = 0; i < layerSizes.length; i++){
			layerSizes[i] = layers[i].length;
		}
		NeuralNet net = new NeuralNet(layerSizes);
		
		for(int i = 0; i < net.layers.length; i++){
			for(int j = 0; j < net.layers[i].length; j++){
				net.layers[i][j] = layers[i][j].copy();
			}
		}
		
		return net;
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			boolean success = true;
			
			write.println(layers.length);
			for(Node[] layer : layers){
				write.println(layer.length);
				for(Node n : layer) success &= n.save(write);
			}
			return success;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean load(Scanner read){
		try{
			boolean success = true;
			
			layers = new Node[read.nextInt()][0];
			for(int i = 0; i < layers.length; i++){
				layers[i] = new Node[read.nextInt()];
				for(int j = 0; j < layers[i].length; j++){
					layers[i][j] = new Node(new double[]{}, 0);
					success &= layers[i][j].load(read);
				}
			}
			return success;
		}catch(Exception e){
			return false;
		}
		
	}
	
	/**
	 * Get the color associated with the given value.<br>
	 * Value should be in the range [-1, 1]<br>
	 * Negative values close to -1 return a very red color.<br>
	 * Positive values close to 1 return a very blue color<br>
	 * @param value the value to use
	 * @param transparent true if this color should get less colored based on it's alpha channel, false if it should approach white
	 * @return the color
	 */
	public static Color getValueColor(double value, boolean transparent){
		int colorChange = Math.max(0, Math.min(255, (int)(255 - 255.0 * Math.abs(value))));
		
		Color color;
		if(transparent){
			if(value > 0) color = new Color(0, 0, 255, 255 - colorChange);
			else color = new Color(255, 0, 0, 255 - colorChange);
		}
		else{
			if(value > 0) color = new Color(colorChange, colorChange, 255);
			else color = new Color(255, colorChange, colorChange);
		}
		return color;
	}
	
}
