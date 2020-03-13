package evolve.sim.obj.tile;

import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.misc.Species;
import evolve.sim.misc.Temperature;
import evolve.sim.obj.Creature;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.SimObject;
import evolve.util.math.vector.Vector;

/**
 * One spot on a grid in a simulation. This object will keep track of all the data 
 * and calculations for the tile used by the simulation
 */
public abstract class Tile implements SimObject{
	
	/**
	 * The x index location of this tile
	 */
	private int x;
	/**
	 * The y index location of this tile
	 */
	private int y;
	
	/**
	 * The simulation that this tile is located in
	 */
	private Simulation sim;
	
	/**
	 * The species of this tile
	 */
	private Species foodSpecies;
	
	/**
	 * A list of all the NeuralNet creatures this Tile contains
	 */
	private ArrayList<NeuralNetCreature> containingCreatures;
	
	/**
	 * The current temperature in this tile
	 */
	private Temperature temperature;
	
	/**
	 * Create a new tile with the given index values
	 * @param x
	 * @param y
	 * @param sim the simulation object that this {@link Tile} is in
	 */
	public Tile(int x, int y, Simulation sim){
		this.temperature = new Temperature(sim.getTemperatureValue());
		this.x = x;
		this.y = y;
		this.sim = sim;
		
		this.foodSpecies = new Species();
		
		containingCreatures = new ArrayList<NeuralNetCreature>();
	}
	
	@Override
	public void update(){
		//set temperature based on the temperature of this tile
		getTemperature().approach(getSimulation().getWorldTemperature(getY()), Main.SETTINGS.temperatureTileRate.value());
	}
	
	/**
	 * Get the simulation that this Tile is in
	 * @return the {@link Simulation}
	 */
	public Simulation getSimulation(){
		return sim;
	}
	
	/**
	 * Get the {@link Temperature} object that keeps track of this Tiles temperature
	 * @return the {@link Temperature}
	 */
	public Temperature getTemperature(){
		return temperature;
	}
	/**
	 * Set the value of the temperature of this tile
	 * @param temperature the new temperature value
	 */
	public void setTemperature(double temperature){
		this.getTemperature().setTemp(temperature);
	}
	
	@Override
	public void cacheData(){
		containingCreatures.clear();
	}
	
	/**
	 * Get the list of creatures this tile contains
	 * @return the list
	 */
	public ArrayList<NeuralNetCreature> getContainingCreatures(){
		return containingCreatures;
	}
	
	/**
	 * Add the given NeuralNetCreature as a creature contained in this tile
	 * @param c
	 */
	public void addContainingCreature(NeuralNetCreature c){
		this.containingCreatures.add(c);
	}
	
	public int getX(){
		return x;
	}
	public void setX(int x){
		this.x = x;
	}
	/**
	 * Get the center x position of this tile as positioned in the simulation
	 * @return the x position
	 */
	public double getCenterX(){
		return (getX() + .5) * Main.SETTINGS.tileSize.value();
	}
	
	public int getY(){
		return y;
	}
	public void setY(int y){
		this.y = y;
	}
	/**
	 * Get the center y position of this tile as positioned in the simulation
	 * @return the y position
	 */
	public double getCenterY(){
		return (getY() + .5) * Main.SETTINGS.tileSize.value();
	}
	
	public Point2D.Double getCenter(){
		return new Point2D.Double(getCenterX(), getCenterY());
	}
	
	/**
	 * Get the amount of food in this tile
	 * @return
	 */
	public abstract double getFood();
	/**
	 * Eat from this tile, removing the eaten food from this tile, and returning the eaten amount.<br>
	 * @return
	 */
	public abstract double eat();
	
	/**
	 * Eat from this tile using the given creature.<br>
	 * @param c the creature to eat with
	 * @return the food the creature ate
	 */
	public abstract double eat(Creature c);
	
	/**
	 * Give energy to this tile
	 * @param energy the amount of energy to give
	 */
	public abstract void giveEnergy(double energy);
	
	/**
	 * Determines if this tile should be considered a hazard and is harmful to be on
	 * @return true if this tile is a hazard, false otherwise
	 */
	public abstract boolean isHazard();
	
	/**
	 * Get a {@link Vector} that will apply to the given {@link Creature} when they stand on the tile
	 * @param c the creature that will be effected
	 * @return the vector that will move the creature, null if this Tile shouldn't move creatures
	 */
	public abstract Vector getMovement(Creature c);
	
	/**
	 * Get the Species object of this tile, which represents the color of this tile, and how effectively a creature can eat from this tile
	 * @return
	 */
	public Species getSpecies(){
		return foodSpecies;
	}
	
	/**
	 * Set the species value of this time
	 * @param amount the new species amount
	 */
	public void setSpeciesAmount(double amount){
		this.getSpecies().setSpeciesValue(amount);
	}
	
	@Override
	public boolean save(PrintWriter write){
		try {
			write.println(x + " " + y + " " + foodSpecies.getSpeciesValue());
			temperature.save(write);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean load(Scanner read){
		try{
			setX(read.nextInt());
			setY(read.nextInt());
			setSpeciesAmount(read.nextDouble());
			temperature.load(read);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
