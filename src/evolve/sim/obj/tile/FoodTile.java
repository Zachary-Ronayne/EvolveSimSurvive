package evolve.sim.obj.tile;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.util.Scanner;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.obj.Creature;
import evolve.util.Camera;
import evolve.util.math.vector.Vector;

public class FoodTile extends Tile{
	/**
	 * The amount of food in this tile that can be consumed.
	 */
	private double food;
	
	public FoodTile(int x, int y, Simulation sim){
		super(x, y, sim);

		setFood(Main.SETTINGS.foodMax.value());
	}

	@Override
	public void update(){
		super.update();
		
		//find the values for determining how much food to add or remove from this tile
		double temperature = getTemperature().getTemp();
		double perferedTemp = Main.SETTINGS.foodGrowTemperature.value();
		double distance = temperature - perferedTemp;
		double modifier = 0;
		double food = 0;
		
		//grow food
		if(distance > 0){
			modifier = distance * Main.SETTINGS.foodGrowthScalar.value();
			food = Main.SETTINGS.foodGrowth.value();
		}
		//or decay food
		else if(distance < 0){
			modifier = distance * Main.SETTINGS.foodDecayScalar.value() * getFood() / Main.SETTINGS.foodMax.value();
			food = -Main.SETTINGS.foodDecay.value();
		}
		//add the food
		if(distance != 0) addFood(food + modifier);
	}
	
	@Override
	public void render(Camera cam){
		double size = Main.SETTINGS.tileSize.value();
		double maxFood = Main.SETTINGS.foodMax.value();

		Graphics g = cam.getG();
		
		//set the color of this tile based on the species
		g.setColor(Color.BLACK);
		cam.fillRect(size * getX(), size * getY(), size, size);
		g.setColor(getSpecies().getColor(
				//saturation based on food
				(float)(.1 + .9 * (getFood() / maxFood)),
				//brightness based on temperature
				(float)(0.45 + 0.3 * getTemperature().getBrightnessRange()))
		);
		cam.fillRect(size * getX() + 2, size * getY() + 2, size - 4, size - 4);
		
		g.setColor(getTemperature().getOverlayColor());
		cam.fillRect(size * getX() + 2, size * getY() + 2, size - 4, size - 4);
	}
	
	@Override
	public double getFood(){
		return food;
	}
	/**
	 * Sets the amount of food in this tile to the given amount, food will always be in the range of [0, 1000]
	 * @param food
	 */
	public void setFood(double food){
		this.food = Math.max(0, Math.min(Main.SETTINGS.foodMax.value(), food));
	}
	public void addFood(double food){
		setFood(this.food + food);
	}
	/**
	 * Eat from this tile, removing the eaten food from this tile, and returning the eaten amount
	 * @return
	 */
	@Override
	public double eat(){
		double amount = this.food * Main.SETTINGS.foodEatPercent.value();
		addFood(-amount);
		return amount;
	}
	
	/**
	 * Compare the given {@link Creature} to this tile based on their species, and get the percentage of food that the given creature 
	 * will be able to eat if they eat from this tile.<br>
	 * Low differences in species result in high percents, high difference in species result in low percents
	 * @param c
	 */
	public double getEatPercent(Creature c){
		return 1 - getSpecies().compareSpecies(c.getSpecies()) * 2;
	}
	
	/**
	 * Eat from this tile with the given {@link Creature}, and return the amount of food that the given {@link Creature} 
	 * was able to consume.<br>
	 * This method also makes the creatures size into account
	 * @param c the {@link Creature}
	 * @return the amount of food the creature was able to eat
	 */
	@Override
	public double eat(Creature c){
		//first get the percentage they can eat
		double perc = getEatPercent(c);
		
		//now multiply the percentage by the percentage of the creatures size that exists
		//this makes perc change by a very small amount when creatures are small, and a large amount when creatures are large
		perc *= c.getRadius() / (Main.SETTINGS.creatureMaxSize.value() + Main.SETTINGS.creatureMinRadius.value());
		
		//ensure the percentage stays in the range[0, 1]
		perc = Math.max(0, Math.min(1, perc));
		
		//return the amount of food eaten
		return eat() * perc;
	}
	
	@Override
	public void giveEnergy(double energy){
		addFood(energy);
	}
	
	@Override
	public boolean isHazard(){
		return false;
	}
	
	@Override
	public Vector getMovement(Creature c){
		return null;
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			boolean success = super.save(write);
			write.println(getFood());
			return success;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean load(Scanner read){
		try{
			boolean success = super.load(read);
			setFood(read.nextDouble());
			return success;
		}catch(Exception e){
			return false;
		}
	}
}
