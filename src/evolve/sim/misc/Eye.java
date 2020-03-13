package evolve.sim.misc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.neuralNet.Mutateable;
import evolve.sim.obj.Creature;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.tile.Tile;
import evolve.util.Camera;
import evolve.util.Saveable;
import evolve.util.math.Calc;

/**
 * An object used by Creature to see the Simulation around them
 */
public class Eye implements Saveable, Mutateable{
	
	/**
	 * The number of values this eye will take in for tiles
	 */
	public static final int TILE_QUANTITIES = 4;
	/**
	 * The number of values this eye will take in for creatures
	 */
	public static final int CREATURE_QUANTITIES = 8;
	/**
	 * The number of values this eye will take in when it looks
	 */
	public static final int NUM_QUANTITIES = TILE_QUANTITIES + CREATURE_QUANTITIES;
	
	/**
	 * The number of genes that an eye uses
	 */
	public static final int NUM_GENES = 4;
	
	/**
	 * The minimum distance that this eye can look out at. 
	 */
	private Gene minDistance;
	
	/**
	 * The maximum distance of this eye. If a creature is not seen within this range, the tile that this eye reaches should be seen
	 */
	private Gene maxDistance;

	/**
	 * The distance that this Eye is looking out at
	 * If a creature is not seen within this range, the tile that this eye reaches should be seen.
	 */
	private double distance;

	/**
	 * The minimum angle this Eye can move from the forward facing point of the creature that uses it
	 */
	private Gene minAngle;
	/**
	 * The maximum angle this Eye can move from the forward facing point of the creature that uses it
	 */
	private Gene maxAngle;
	
	/**
	 * The angle, in radians of this eye as positioned relative to the creature
	 */
	private double angle;
	
	/**
	 * The distance this eye was from the last thing it saw
	 */
	private double seenDistance;
	
	/**
	 * The values, in the range [-1, 1] that this eye sees based on what it's looking at
	 */
	private double[] seenQuantitys;
	
	/**
	 * Create a new random eye
	 */
	public Eye(){
		minDistance = new Gene(
				Main.SETTINGS.eyeDistanceMin.value(),
				Main.SETTINGS.eyeDistanceMax.value(),
				Main.SETTINGS.eyeDistanceScalar.value()
		);
		maxDistance = new Gene(
				Main.SETTINGS.eyeDistanceMin.value(),
				Main.SETTINGS.eyeDistanceMax.value(),
				Main.SETTINGS.eyeDistanceScalar.value()
		);
		minAngle = new Gene(
				-Main.SETTINGS.eyeAngleRange.value(),
				Main.SETTINGS.eyeAngleRange.value(),
				Main.SETTINGS.eyeAngleScalar.value()
		);
		maxAngle = new Gene(
				-Main.SETTINGS.eyeAngleRange.value(),
				Main.SETTINGS.eyeAngleRange.value(),
				Main.SETTINGS.eyeAngleScalar.value()
		);
		
		seenDistance = 0;
		seenQuantitys = new double[NUM_QUANTITIES];
	}
	
	/**
	 * Get a list of all the {@link Gene} objects this Eye uses
	 * @return the list of {@link Gene} objects
	 */
	public ArrayList<Gene> getAllGenes(){
		ArrayList<Gene> genes = new ArrayList<Gene>();
		genes.add(minDistance);
		genes.add(maxDistance);
		genes.add(minAngle);
		genes.add(maxAngle);
		
		return genes;
	}

	/**
	 * Get the {@link Gene} that keeps track of this minimum Eye's vision distance
	 * @return the minimum distance gene
	 */
	public Gene getMinDistance(){
		return minDistance;
	}
	/**
	 * Set the {@link Gene} that keeps track of the minimum distance for this eye.<br>
	 * If the new {@link Gene} is larger than the maximum, then the given gene's value becomes to new maximum and the old maximum becomes the new minimum
	 * @param minDistance the new {@link Gene}
	 */
	public void setMinDistance(Gene minDistance){
		double oldMax = getMaxDistance().getValue();
		double newMin = minDistance.getValue();
		this.minDistance = minDistance;
		if(newMin > oldMax){
			this.minDistance.setValue(oldMax);
			this.maxDistance.setValue(newMin);
		}
	}
	
	/**
	 * Get the {@link Gene} that keeps track of this maximum Eye's vision distance
	 * @return the maximum distance gene
	 */
	public Gene getMaxDistance(){
		return maxDistance;
	}
	/**
	 * Set the {@link Gene} that keeps track of the maximum distance for this eye.<br>
	 * If the new {@link Gene} is smaller than the minimum, then the given gene's value becomes to new minimum and the old minimum becomes the new maximum
	 * @param maxDistance the new {@link Gene}
	 */
	public void setMaxDistance(Gene maxDistance){
		double oldMin = getMinDistance().getValue();
		double newMax = maxDistance.getValue();
		this.maxDistance = maxDistance;
		if(newMax < oldMin){
			this.minDistance.setValue(newMax);
			this.maxDistance.setValue(oldMin);
		}
	}
	/**
	 * Get the current amount of distance that this eye is trying to look out at
	 * @return the distance
	 */
	public double getDistance(){
		return distance;
	}
	/**
	 * Set the distance of this eye based on a percentage of the distance that should be used.
	 * @param perc the percentage. Use 0 for minimum distance and 1 for maximum distance
	 */
	public void setDistance(double perc){
		double min = getMinDistance().getValue();
		double max = getMaxDistance().getValue();
		this.distance = Math.max(min, Math.min(max, min + (max - min) * perc));
	}

	/**
	 * Get the {@link Gene} that keeps track of this Eye's minimum angle
	 * @return the maximum angle {@link Gene}
	 */
	public Gene getMinAngle(){
		return minAngle;
	}
	/**
	 * Set the {@link Gene} that track of this Eye's minimum angle.<br>
	 * If the new {@link Gene} is larger than the maximum, then the given's value gene becomes to new maximum and the old maximum becomes the new minimum
	 * @param angle the new {@link Gene}
	 */
	public void setMinAngle(Gene minAngle){
		double oldMax = getMaxAngle().getValue();
		double newMin = minAngle.getValue();
		this.minAngle = minAngle;
		if(newMin > oldMax){
			this.minAngle.setValue(oldMax);
			this.maxAngle.setValue(newMin);
		}
	}
	/**
	 * Get the {@link Gene} that keeps track of this Eye's maximum angle
	 * @return the maximum angle {@link Gene}
	 */
	public Gene getMaxAngle(){
		return maxAngle;
	}
	/**
	 * Set the {@link Gene} that track of this Eye's maximum angle.<br>
	 * If the new {@link Gene} is smaller than the minimum, then the given's value gene becomes to new minimum and the old minimum becomes the new maximum
	 * @param angle the new {@link Gene}
	 */
	public void setMaxAngle(Gene maxAngle){
		double oldMin = getMinAngle().getValue();
		double newMax = maxAngle.getValue();
		this.maxAngle = maxAngle;
		if(newMax < oldMin){
			this.minAngle.setValue(newMax);
			this.maxAngle.setValue(oldMin);
		}
	}
	/**
	 * Get the angle that this eye is currently looking at
	 * @return the angle
	 */
	public double getAngle(){
		return angle;
	}
	/**
	 * Set the angle of this {@link Eye} based on a percentage
	 * @param perc the percentage, use values in range [-1, 1].<br>
	 * 		Use -1 for as far clockwise as possible<br>
	 * 		Use 1 for as far counterclockwise as possible<br>
	 * 		0 does not necessarily mean facing directly in front, 
	 * 		only if minimum and maximum angle have the same absolute value and one is negative and one is positive
	 */
	public void setAngle(double perc){
		double min = getMinAngle().getValue();
		double max = getMaxAngle().getValue();
		this.angle = Math.max(min, Math.min(max, min + (max - min) * (perc * .5 + .5)));
	}
	
	/**
	 * Get the distance this eye was from the last thing it saw
	 * @return the distance
	 */
	public double getSeenDistance(){
		return seenDistance;
	}
	
	/**
	 * Get the last quantities that this eye saw
	 * @return the quantities
	 */
	public double[] getSeenQuantities(){
		return seenQuantitys;
	}
	
	/**
	 * Look in the given {@link Simulation} with the given {@link NeuralNetCreature} and see what's around this eye.<br>
	 * All relevant information that the eye sees will be stored after this method call
	 * @param sim the {@link Simulation} to look in
	 * @param creature the {@link NeuralNetCreature} that uses this eye, this creature will not be seen by the eye
	 */
	public void look(Simulation sim, NeuralNetCreature creature){
		//if the sim or the creature are null, this method should do nothing
		if(creature == null || sim == null) return;
		
		NeuralNetCreature nearCreature = null;
		Tile nearHazTile = null;
		double nearHazTileDist = 0;
		Line2D.Double bounds = getBounds(creature);

		Tile[][] grid = sim.getGrid();
		//first get the list of all the tile positions that this eye sees
		ArrayList<Point> tilePos = Calc.touchingGridPos(
				bounds.x1, bounds.y1, bounds.x2, bounds.y2,
				Main.SETTINGS.tileSize.value(), grid.length, grid[0].length);

		//make a collection of all the creatures on all of those points, not adding duplicates
		//also in this loop, determine the nearest hazard tile, this will stop vision at the nearest hazard tile if one exists
		//use linked list because it's faster than HashSet and ArrayList
		Collection<NeuralNetCreature> creatures = new LinkedList<NeuralNetCreature>();
		
		for(Point p : tilePos){
			//add to the list
			Tile t = grid[p.x][p.y];

			ArrayList<NeuralNetCreature> cs = t.getContainingCreatures();
			for(NeuralNetCreature cc : cs){
				if(cc != creature) creatures.add(cc);
			}
			
			//look for hazard tiles
			if(t.isHazard()){
				double dist = t.getCenter().distance(creature.getX(), creature.getY());
				if(nearHazTile == null || dist < nearHazTileDist){
					nearHazTile = t;
					nearHazTileDist = dist;
				}
			}
		}
		
		//now use that Collection of creatures to look for a creature that this eye sees
		double nearCreatureDist = 0;
		//first find the nearest creature to this eye that also intersects with it
		for(NeuralNetCreature c : creatures){
			//if c is not creature, see if it intersects
			if(c != null && c != creature){
				Point2D.Double center = c.getPos();
				double cDist = creature.getPos().distance(center) - c.getRadius();
				//if the distance from creature center to c's edge is less than the vision distance, then they might intersect
				if(cDist <= getDistance()){
					//determine if the eye intersects the creature
					boolean intersects = Calc.circleIntersectsLine(c.getRadius(), center.x, center.y, bounds.x1, bounds.y1, bounds.x2, bounds.y2);
					
					//if it does, then determine it's distance to the main creature
					if(intersects){
						//if near is null or nearDist > the distance to this new creature, store the current creature and the distance to it
						double newDist = cDist;
						if(nearCreature == null || nearCreatureDist > newDist){
							nearCreatureDist = newDist;
							nearCreature = c;
						}
					}
				}
			}
		}
		
		//some variables for calculating temperature quantities
		double rangeTemp = Main.SETTINGS.temperatureWorldRange.value();
		double minTemp = Main.SETTINGS.temperatureMin.value() - rangeTemp;
		double maxTemp = Main.SETTINGS.temperatureMax.value() + rangeTemp;
		double sizeTemp = maxTemp - minTemp;
		
		//if the nearest creature is not null and the nearest hazard tile either doesn't exist or is further than the creature, set the seenQuantities based on near
		if(nearCreature != null && (nearHazTile == null || nearHazTileDist > nearCreatureDist)){
			seenDistance = nearCreatureDist;
			
			//set the first quantity based on the energy of the creature
			seenQuantitys[TILE_QUANTITIES] = nearCreature.getEnergy() / nearCreature.getMaxEnergySizeBased();

			//set the next quantity based on the difference in species
			seenQuantitys[TILE_QUANTITIES + 1] = nearCreature.getSpecies().compareSpecies(creature.getSpecies()) * 2;

			//set the next quantity based on the creature temperature
			double temperature = nearCreature.getTemperature().getTemp();
			if(temperature > 0){
				if(maxTemp <= 0) seenQuantitys[TILE_QUANTITIES + 2] = temperature / sizeTemp;
				else seenQuantitys[TILE_QUANTITIES + 2] = temperature / maxTemp;
			}
			else{
				if(minTemp >= 0) seenQuantitys[TILE_QUANTITIES + 2] = temperature / sizeTemp;
				else seenQuantitys[TILE_QUANTITIES + 2] = temperature / minTemp;
			}
			
			//set the next quantity based on the angle between the creatures
			double newAngle = creature.getAngleTo(nearCreature) / Math.PI;
			seenQuantitys[TILE_QUANTITIES + 3] = newAngle;

			//set the next quantity based on the distance between the creatures
			if(getDistance() == 0) seenQuantitys[4] = 0;
			else seenQuantitys[TILE_QUANTITIES + 4] = nearCreature.getPos().distance(creature.getPos()) / getDistance();
			
			//set the next quantity based on the baby cool down
			seenQuantitys[TILE_QUANTITIES + 5] = (double)nearCreature.getBabyCooldown() / Main.SETTINGS.creatureBabyCooldown.value();

			//set the next quantity based on the size of the creature
			seenQuantitys[TILE_QUANTITIES + 6] = nearCreature.getRadius() / (Main.SETTINGS.creatureMaxSize.value() + Main.SETTINGS.creatureMinRadius.value());

			//set the next quantity based on the fight state of the creature
			seenQuantitys[TILE_QUANTITIES + 7] = nearCreature.getBrainOutputs()[NeuralNetCreature.FIGHT_NODE];
		}
		//otherwise set all the quantities to 0
		else{
			for(int i = TILE_QUANTITIES; i < NUM_QUANTITIES; i++) seenQuantitys[i] = 0;
		}
		
		//look at a tile
		Tile t;
		//if the nearest creature is null, then set the tile as the one at current maximum range or the hazard tile if one exists
		if(nearCreature == null){
			//if the nearest creature isn't null, then look at the tile under them
			if(nearHazTile == null){
				seenDistance = getDistance();
				t = sim.getContainingTile(
						creature.getX() + seenDistance * Math.cos(creature.getAngle() + getAngle()),
						creature.getY() + seenDistance * -Math.sin(creature.getAngle() + getAngle())
				);
			}
			else{
				t = nearHazTile;
				seenDistance = t.getCenter().distance(creature.getX(), creature.getY());
			}
		}
		else t = sim.getContainingTile(nearCreature.getX(), nearCreature.getY());
		
		//if t is an actual tile
		if(t != null){
			//set first quantity based on the food in the tile
			seenQuantitys[0] = t.getFood() / Main.SETTINGS.foodMax.value();
			
			//set the next quantity based on the species of the tile
			seenQuantitys[1] = t.getSpecies().compareSpecies(creature.getSpecies()) * 2;
			
			//set the next quantity based on the tile temperature
			double temperature = t.getTemperature().getTemp();
			if(temperature > 0){
				if(maxTemp <= 0) seenQuantitys[2] = temperature / sizeTemp;
				else seenQuantitys[2] = temperature / maxTemp;
			}
			else{
				if(minTemp >= 0) seenQuantitys[2] = temperature / sizeTemp;
				else seenQuantitys[2] = temperature / minTemp;
			}
			
			//set the next quantity based on if the tile is a hazard or not
			if(t.isHazard()) seenQuantitys[3] = 1;
			else seenQuantitys[3] = 0;
		}
		//otherwise set all the tiles to -1
		else{
			for(int i = 0; i < TILE_QUANTITIES; i++) seenQuantitys[i] = 0;
		}
		
		
		//keep seenQuantities in the range [-1, 1]
		for(int i = 0; i < seenQuantitys.length; i++){
			seenQuantitys[i] = Math.max(-1, Math.min(1, seenQuantitys[i]));
		}
	}
	
	/**
	 * Get the bounds of the distance that this line can see, relative to the given coordinates
	 * @param c the creature that is looking with these eyes
	 * @return the line representing the bounds of what this line can see
	 */
	public Line2D.Double getBounds(Creature c){
		return new Line2D.Double(c.getX(), c.getY(),
				c.getX() + getDistance() * Math.cos(c.getAngle() + getAngle()),
				c.getY() + getDistance() * -Math.sin(c.getAngle() + getAngle()));
	}
	
	/**
	 * Get the bounds of the distance that this line last saw, relative to the given coordinates
	 * @param c the creature that is looking with these eyes
	 * @return the line representing the bounds of what this line can see
	 */
	public Line2D.Double getSeenBounds(Creature c){
		return new Line2D.Double(c.getX(), c.getY(),
				c.getX() + getSeenDistance() * Math.cos(c.getAngle() + getAngle()),
				c.getY() + getSeenDistance() * -Math.sin(c.getAngle() + getAngle()));
	}
	
	/**
	 * Draw this eye based on the position of the given camera
	 * @param cam
	 * @param c the creature that uses these eyes
	 */
	public void render(Camera cam, NeuralNetCreature c){
		Graphics g = cam.getG();
		g.setColor(new Color(0, 0, 0, 200));
		Line2D.Double bounds = getSeenBounds(c);
		cam.drawLine(bounds.x1, bounds.y1, bounds.x2, bounds.y2);
	}

	@Override
	public void mutate(double mutability, double chance){
		minDistance.mutate(mutability, chance);
		maxDistance.mutate(mutability, chance);
		minAngle.mutate(mutability, chance);
		maxAngle.mutate(mutability, chance);
	}
	
	
	/**
	 * Create a copy of this Eye which is a different object but has all the same genes
	 * @return the copy
	 */
	public Eye copy(){
		Eye copy = new Eye();

		copy.minDistance = minDistance.copy();
		copy.maxDistance = maxDistance.copy();
		copy.minAngle = minAngle.copy();
		copy.maxAngle = maxAngle.copy();
		
		return copy;
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			boolean success = true;
			success &= minDistance.save(write);
			success &= maxDistance.save(write);
			success &= maxAngle.save(write);
			success &= minAngle.save(write);
			return success;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean load(Scanner read){
		try{
			boolean success = true;
			success &= minDistance.load(read);
			success &= maxDistance.load(read);
			success &= minAngle.load(read);
			success &= maxAngle.load(read);
			return success;
		}catch(Exception e){
			return false;
		}
	}

}
