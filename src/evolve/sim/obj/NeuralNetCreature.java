package evolve.sim.obj;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.misc.Eye;
import evolve.sim.misc.Gene;
import evolve.sim.misc.Species;
import evolve.sim.neuralNet.NeuralNet;
import evolve.sim.neuralNet.Node;
import evolve.sim.obj.tile.Tile;
import evolve.util.ArrayHandler;
import evolve.util.Camera;

/**
 * A creature that is controlled by a neural network
 */
public class NeuralNetCreature extends Creature{

	/**
	 * The number of inputs a {@link NeuralNetCreature} takes in aside from what they see with eyes
	 */
	public static final int EXTRA_INPUTS = 7;
	/**
	 * The number of outputs a {@link NeuralNetCreature} uses for thinking excluding outputs controlled by eyes
	 */
	public static final int EXTRA_OUTPUTS = 8;
	
	/**
	 * The number of nodes this {@link NeuralNetCreature} has for memory. One input node exists for each memory node, 
	 * and 2 output nodes exist for each memory node, one node to determine the output for the memory node, and another 
	 * to determine if the memory input node should be changed.<br>
	 * Memory nodes are always at the end of the input and output node list
	 */
	public static final int MEMORY_NODES = 10;
	
	
	/**
	 * The index of the output node for controlling the {@link NeuralNetCreature} angle.<br>
	 * Positive numbers turn counterclockwise, negative numbers turn clockwise, 0 means no angle change
	 */
	public static final int ANGLE_NODE = 0;
	/**
	 * The index of the output node for controlling the {@link NeuralNetCreature} speed.<br>
	 * Positive numbers mean move forward, negative numbers mean move backwards, 0 means don't move
	 */
	public static final int SPEED_NODE = 1;
	/**
	 * The index of the output node for controlling if the {@link NeuralNetCreature} should eat.<br>
	 * Numbers greater than 0 mean eat, other numbers mean don't eat
	 */
	public static final int EAT_NODE = 2;
	/**
	 * The index of the output node for controlling if the {@link NeuralNetCreature} should grow or shed fur.<br>
	 * Positive numbers mean grow fur until the maximum fur amount is reached, negative numbers mean shed fur, 0 means keep current fur
	 */
	public static final int FUR_NODE = 3;
	/**
	 * The index of the output node for controlling the wilingingness for the {@link NeuralNetCreature} to asexually reproduce.<br>
	 * Numbers greater than 0 mean the creature is willing, other numbers mean it is not willing
	 */
	public static final int ASEXUAL_NODE = 4;
	/**
	 * The index of the output node for controlling the wilingingness for the {@link NeuralNetCreature} to sexually reproduce.<br>
	 * Numbers greater than 0 mean the creature is willing, other numbers mean it is not willing
	 */
	public static final int SEXUAL_NODE = 5;
	/**
	 * The index of the output node for controlling if this {@link NeuralNetCreature} wants to fight.<br>
	 * Numbers greater than 0 mean the creature wants to fight, other numbers mean it doesn't want to fight
	 */
	public static final int FIGHT_NODE = 6;
	/**
	 * The index of the output node for controlling if this {@link NeuralNetCreature} wants to vomit.<br>
	 * Numbers greater than 0 mean the creature wants to vomit, other numbers mean it doesn't want to vomit
	 */
	public static final int VOMIT_NODE = 7;
	
	
	/**
	 * The NeuralNetwork that controls this creature
	 */
	private NeuralNet brain;
	
	/**
	 * The values output from this creatures brain from the last time they were thinking
	 */
	private double[] brainOutputs;
	
	/**
	 * An ArrayList of all the {@link Tile} objects this creature is touching
	 */
	private ArrayList<Tile> containedTiles;
	
	/**
	 * The {@link Gene} that governs how much this {@link NeuralNetCreature} can mutate
	 */
	private Gene mutability;
	
	/**
	 * The {@link Gene} that governs the maximum amount of energy this {@link NeuralNetCreature} can have
	 */
	private Gene maxEnergy;
	
	/**
	 * The {@link Gene} that governs the amount of energy this {@link NeuralNetCreature} will give to a baby when it's born
	 */
	private Gene energyToBaby;
	
	/**
	 * The {@link Gene} that governs the maximum size that this {@link NeuralNetCreature} approaches based on age and current energy
	 */
	private Gene sizeGene;
	
	/**
	 * The {@link Gene} that governs the maximum amount of fur this {@link NeuralNetCreature} can have
	 */
	private Gene furGene;
	
	/**
	 * The {@link Gene} that governs the number of eyes this {@link NeuralNetCreature} has. This gene is a double value 
	 * and is rounded to the nearest integer to determine the actual number of eyes
	 */
	private Gene numberOfEyes;
	
	/**
	 * The number of genes this creature has that are not eyes. These genes are listed first in the creatures gene list
	 */
	public static final int NUM_GENES = 7;
	
	/**
	 * The actual amount of fur this {@link NeuralNetCreature} has
	 */
	private double furAmount;
	
	/**
	 * A list of all the Genes of this creature, excluding it's brain's Genes.<br>
	 * This list is initialized on creation and should not be added or removed to after initialization.<br><br>
	 * To add a gene:
	 * <li>Add an object as an instance variable</li>
	 * <li>Initialize it in the constructor</li>
	 * <li>Add it to updateGeneValues and pdateGeneReferences</li>
	 * <li>add 1 to NUM_GENES for each new gene</li>
	 * <li>Update any loops that rely on hard coded index numbers</li>
	 */
	private ArrayList<Gene> genes;
	
	/**
	 * The remaining number of updates until this creature can have another baby
	 */
	private int babyCooldown;
	
	/**
	 * The {@link NeuralNetCreature} that this {@link NeuralNetCreature} is attempting to breed with.<br>
	 * If this variable is null, this {@link NeuralNetCreature} is not trying to breed.<br>
	 * If either creature dies, becomes unwilling to breed, or gets far enough away that they no longer touch before the breeding completes, 
	 * then the breeding will not happen.
	 */
	private NeuralNetCreature breedingPartner;
	
	/**
	 * The amount of time this {@link NeuralNetCreature} needs to remain touching it's partner until breeding completes
	 */
	private int breedingTimer;
	
	
	/**
	 * The eyes of this creature
	 */
	private Eye[] eyes;
	
	public NeuralNetCreature(Simulation simulation){
		super(simulation);
		
		//initialize eyes
		int min = Main.SETTINGS.eyeNumMin.value();
		int max = Main.SETTINGS.eyeNumMax.value();
		int eyesNum = min + (int)(Math.random() * (max - min));
		eyes = new Eye[eyesNum];
		for(int i = 0; i < eyes.length; i++){
			eyes[i] = new Eye();
		}
		
		//create the brain
		
		//must also determine the number of hidden layers
		Integer[] hidden = Main.SETTINGS.brainSize.value();
		int[] brainSize = new int[2 + hidden.length];
		
		//set input size
		//length of eyes * quantities per eye + the extra inputs
		brainSize[0] = eyes.length * Eye.NUM_QUANTITIES + EXTRA_INPUTS + MEMORY_NODES;
		
		//set hidden layer sizes
		for(int i = 1; i < brainSize.length - 1; i++) brainSize[i] = hidden[i - 1];
		
		//set output size
		brainSize[brainSize.length - 1] = EXTRA_OUTPUTS + Eye.NUM_GENES / 2 * eyes.length + MEMORY_NODES * 2;
		brain = new NeuralNet(brainSize);
		brainOutputs = new double[brainSize[brainSize.length - 1]];
		
		//set up all genes
		genes = new ArrayList<Gene>();
		
		//mutability gene
		mutability = new Gene(
				-Main.SETTINGS.mutabilityMax.value(),
				Main.SETTINGS.mutabilityMax.value(),
				Main.SETTINGS.mutabilityScalar.value()
		);
		genes.add(mutability);
		
		//max energy gene
		maxEnergy = new Gene(0, Main.SETTINGS.creatureMaxEnergy.value(),
				Main.SETTINGS.creatureMaxEnergyScalar.value()
		);
		genes.add(maxEnergy);
		
		//baby energy gene
		energyToBaby = new Gene(Main.SETTINGS.creatureMinBabyEnergy.value(),
				Main.SETTINGS.creatureMaxBabyEnergy.value(),
				Main.SETTINGS.creatureBabyEnergyScalar.value()
		);
		genes.add(energyToBaby);
		
		//size gene
		sizeGene = new Gene(Main.SETTINGS.creatureMinSize.value(),
				Main.SETTINGS.creatureMaxSize.value(),
				Main.SETTINGS.creatureSizeScalar.value()
		);
		genes.add(sizeGene);
		
		//species gene, already initialized in super
		genes.add(getSpecies().getSpeciesGene());
		
		//fur gene
		furGene = new Gene(0.0,
				Main.SETTINGS.temperatureFurMax.value(),
				Main.SETTINGS.temperatureFurScalar.value()
		);
		genes.add(furGene);
		
		//number of eyes gene
		numberOfEyes = new Gene(Main.SETTINGS.eyeNumMin.value(),
				Main.SETTINGS.eyeNumMax.value(),
				Main.SETTINGS.eyeNumScalar.value()
		);
		numberOfEyes.setValue(eyesNum);
		genes.add(numberOfEyes);
		
		//eye genes
		for(Eye e : eyes){
			genes.addAll(e.getAllGenes());
		}
		
		//miscellaneous fields
		babyCooldown = Main.SETTINGS.creatureBabyCooldown.value();
		breedingTimer = 0;
		breedingPartner = null;
		
		furAmount = furGene.getValue();
		
		//cache data when this creature is initially created
		containedTiles = new ArrayList<Tile>();
		cacheData();
	}
	
	/**
	 * Set the min, max, and scalar values of all genes to their correct values based on Main.SETTINGS
	 */
	public void updateGeneValues(){
		//mutability
		mutability.setMin(-Main.SETTINGS.mutabilityMax.value());
		mutability.setMax(Main.SETTINGS.mutabilityMax.value());
		mutability.setScalar(Main.SETTINGS.mutabilityScalar.value());
		
		//max energy
		maxEnergy.setMin(0);
		maxEnergy.setMax(Main.SETTINGS.creatureMaxEnergy.value());
		maxEnergy.setScalar(Main.SETTINGS.creatureMaxEnergyScalar.value());
		
		//baby energy
		energyToBaby.setMin(Main.SETTINGS.creatureMinBabyEnergy.value());
		energyToBaby.setMax(Main.SETTINGS.creatureMaxBabyEnergy.value());
		energyToBaby.setScalar(Main.SETTINGS.creatureBabyEnergyScalar.value());
		
		//size
		sizeGene.setMin(Main.SETTINGS.creatureMinSize.value());
		sizeGene.setMax(Main.SETTINGS.creatureMaxSize.value());
		sizeGene.setScalar(Main.SETTINGS.creatureSizeScalar.value());
		
		//species gene
		Gene species = getSpecies().getSpeciesGene();
		species.setMin(Species.MIN_VALUE);
		species.setMax(Species.MAX_VALUE);
		species.setScalar(Main.SETTINGS.creatureSpeciesGeneScalar.value());
		
		//fur gene
		furGene.setMin(0.0);
		furGene.setMax(Main.SETTINGS.temperatureFurMax.value());
		furGene.setScalar(Main.SETTINGS.temperatureFurScalar.value());
		
		//number of eyes gene
		numberOfEyes.setMin(Main.SETTINGS.eyeNumMin.value());
		numberOfEyes.setMax(Main.SETTINGS.eyeNumMax.value());
		numberOfEyes.setScalar(Main.SETTINGS.eyeNumScalar.value());
		
		//eyes
		for(Eye e : eyes){
			e.getMinDistance().setMin(Main.SETTINGS.eyeDistanceMin.value());
			e.getMinDistance().setMax(Main.SETTINGS.eyeDistanceMax.value());
			e.getMinDistance().setScalar(Main.SETTINGS.eyeDistanceScalar.value());
			
			e.getMaxDistance().setMin(Main.SETTINGS.eyeDistanceMin.value());
			e.getMinDistance().setMax(Main.SETTINGS.eyeDistanceMax.value());
			e.getMinDistance().setScalar(Main.SETTINGS.eyeDistanceScalar.value());
			
			
			e.getMinAngle().setMin(-Main.SETTINGS.eyeAngleRange.value());
			e.getMinAngle().setMax(Main.SETTINGS.eyeAngleRange.value());
			e.getMinAngle().setScalar(Main.SETTINGS.eyeAngleScalar.value());

			e.getMaxAngle().setMin(-Main.SETTINGS.eyeAngleRange.value());
			e.getMaxAngle().setMax(Main.SETTINGS.eyeAngleRange.value());
			e.getMaxAngle().setScalar(Main.SETTINGS.eyeAngleScalar.value());
		}
	}
	
	/**
	 * Set the objects for each gene to the correct references in the genes list
	 */
	public void updateGeneReferences(){
		mutability = genes.get(0);
		maxEnergy = genes.get(1);
		energyToBaby = genes.get(2);
		sizeGene = genes.get(3);
		getSpecies().setSpeciesGene(genes.get(4));
		furGene = genes.get(5);
		numberOfEyes = genes.get(6);
		
		for(int i = 0; i < eyes.length && NUM_GENES + 1 + i * Eye.NUM_GENES < genes.size(); i++){
			eyes[i].setMinDistance(genes.get(NUM_GENES + i * Eye.NUM_GENES));
			eyes[i].setMaxDistance(genes.get(NUM_GENES + 1 +  i * Eye.NUM_GENES));

			eyes[i].setMinAngle(genes.get(NUM_GENES + 2 + i * Eye.NUM_GENES));
			eyes[i].setMaxAngle(genes.get(NUM_GENES + 3 + i * Eye.NUM_GENES));
		}
	}
	
	@Override
	public void cacheData(){
		super.cacheData();
		
		//cache the tiles this creature is on, and put this creature in the tile's list of creatures
		
		double tSize = Main.SETTINGS.tileSize.value();
		//find the number of tiles that this creature could be touching it from the center of it
		int maxTiles = (int)(getRadius() / tSize) + 1;
		//find the tile coordinates of the creature
		int tx = (int)(getX() / tSize);
		int ty = (int)(getY() / tSize);
		
		//clear the cached list
		containedTiles.clear();
		
		//find ranges for grid iteration
		int startI = Math.max(0, tx - maxTiles);
		int maxI = tx + maxTiles;
		int startJ =  Math.max(0, ty - maxTiles);
		int maxJ = ty + maxTiles;
		
		//get the bounds of this creature
		Ellipse2D.Double bounds = getBounds();
		//set up an object for tile bounds
		Rectangle2D.Double tBounds = new Rectangle2D.Double(0, 0, tSize, tSize);
		
		//get the tile grid of this simulation
		Tile[][] grid = getSimulation().getGrid();
		
		//add all the tiles to the cached list
		//start by iterating through the relevant tile grid on the x axis, starting from left and going to the right, and skipping tiles that can't touch this creature
		for(int i = startI; i <= maxI && i < grid.length; i++){
			//now iterate through in the same way, but now on the y axis
			for(int j = startJ; j <= maxJ && j < grid[i].length; j++){
				//for each of those tiles, if they touch this creature, then add that tile to the cached list
				Tile t = grid[i][j];
				tBounds.x = tSize * t.getX();
				tBounds.y = tSize * t.getY();
				if(bounds.intersects(tBounds)){
					containedTiles.add(t);
					t.addContainingCreature(this);
				}
			}
		}
	}
	
	/**
	 * An important note about updating this creature is that they do not think unless externally called, 
	 * i.e. they don't update their NeuralNetwork unless the think() method is called
	 */
	@Override
	public void update(){
		//use this creatures output values to think
		useOutputs();
		
		//do normal update
		super.update();
		
		//update baby cooldown timer
		if(babyCooldown > 0) babyCooldown--;
		//try to have a baby, only if the output node for breeding is greater than 0, and they are allowed to have a baby
		if(babyCooldown == 0 && getBrainOutputs()[ASEXUAL_NODE] > 0){
			//have an asexual baby if the setting is turned on, the corresponding node is willing, and they have no breeding partner
			if(Main.SETTINGS.asexualReproduction.value() && breedingPartner == null && getBrainOutputs()[ASEXUAL_NODE] > 0){
				aSexualBaby();
				babyCooldown = Main.SETTINGS.creatureBabyCooldown.value();
			}
			
			//if this creature has no partner yet, attempt to find one
			if(breedingPartner == null){
				//attempt to have a baby with a partner
				//get the nearest creature, if they are willing to have a baby and are touching this creature, they will have a baby
				NeuralNetCreature partner = getSimulation().getNearestCreature(getX(), getY(), this);
				if(partner != null && partner.babyCooldown == 0 && !partner.isDead() && partner.getBrainOutputs()[SEXUAL_NODE] > 0 && touching(partner)){
					breedingPartner = partner;
					breedingPartner.breedingPartner = this;
					breedingTimer = Main.SETTINGS.creatureBreedTime.value();
					//ensure that the amount of time the partner will want to breed for is more than that of this creature
					breedingPartner.breedingTimer = breedingTimer * 2;
				}
			}
			//otherwise, see if the breeding partner needs to be removed because they don't meet a condition
			//if they are no longer able to breed, then set breedingPartner to null
			else{
				if(breedingPartner.isDead() || breedingPartner.getBrainOutputs()[SEXUAL_NODE] <= 0 || !touching(breedingPartner)){
					breedingPartner = null;
					
					breedingTimer = 0;
				}
			}
			
			//if breedingPartner is still able to breed
			if(breedingPartner != null){
				//update the timers
				if(breedingTimer > 0){
					breedingTimer--;
				}
				else{
					//have the baby
					sexualBaby(breedingPartner);
					//update cooldowns
					babyCooldown = Main.SETTINGS.creatureBabyCooldown.value();
					breedingPartner.babyCooldown = Main.SETTINGS.creatureBabyCooldown.value();
					breedingPartner = null;
				}
			}
		}
	}
	
	@Override
	public void render(Camera cam){
		//draw normal creature
		super.render(cam);
		
		//draw eyes
		if(Main.SETTINGS.eyeShow.value() || getGlow() != null) for(Eye e : eyes) e.render(cam, this);
	}
	
	/**
	 * Have a baby with no partner. Asexual reproduction requires more energy and cannot happen if the creature doesn't have enough energy
	 * @return the baby that was born, null if a baby was not able to be born
	 */
	public NeuralNetCreature aSexualBaby(){
		//determine the amount of energy that will be given to this creatures child
		//if currentEnergy is greater than the energy the creature wants to give, then they will give as much energy as they can
		//otherwise they will give the desired amount of energy
		double giveEnergy;
		double costEnergy = getEnergyToBaby() * Main.SETTINGS.creatureBabyEnergyAsexual.value();
		if(getEnergy() >= costEnergy) giveEnergy = getEnergyToBaby();
		else return null;
		
		//make a copy of the baby
		NeuralNetCreature baby = copy();
		
		//mutate the baby
		baby.mutate();
		//set stats
		baby.setEnergy(giveEnergy);
		baby.setAngle(Math.random() * Math.PI * 2);
		baby.setSpeed(0);
		baby.setGeneration(getGeneration() + 1);
		baby.setId(getSimulation().nextCreatureId());
		baby.setFatherId(getId());
		baby.setMotherId(getId());
		
		setAsexualChildren(getAsexualChildren() + 1);
		addEnergy(-costEnergy);
		
		//add the baby
		getSimulation().addCreature(baby);
		
		return baby;
	}
	
	/**
	 * Have a baby with the given creature<br>
	 * This method also functions as a parentCopy function for NeuralNetCreatures
	 * @param partner the parent to have a baby with
	 * @return the baby, null if no baby was had
	 */
	public NeuralNetCreature sexualBaby(NeuralNetCreature partner){
		//see if the creatures are able to have a baby
		if(getSpecies().compareSpecies(partner.getSpecies()) > Main.SETTINGS.creatureBreedRange.value()) return null;
		
		//create a copy of this creature
		NeuralNetCreature baby;
		
		//create initial baby object
		
		//first determine the number of eyes
		NeuralNetCreature parent1Copy = copy();
		NeuralNetCreature parent2Copy = partner.copy();
		
		//the number of eyes is based on a weighted average of the number of mutated eyes in the parents
		int babyEyes = (int)Math.round(parent1Copy.getEyesNumGene().parentCopy(parent2Copy.getEyesNumGene()).getValue());
		
		//update the number of eyes in the parents
		parent1Copy.getEyesNumGene().setValue(babyEyes);
		parent2Copy.getEyesNumGene().setValue(babyEyes);
		
		//update the actual eyes in the parents
		parent1Copy.updateMutatedEyes();
		parent2Copy.updateMutatedEyes();
		
		//make the baby
		baby = parent1Copy.copy();
		//make the baby's brain a parentCopy of both parents brains
		baby.brain = parent1Copy.brain.parentCopy(parent2Copy.brain);
		
		//parentCopy all genes
		baby.genes = new ArrayList<Gene>();
		for(int i = 0; i < parent1Copy.genes.size(); i++){
			baby.genes.add(parent1Copy.genes.get(i).parentCopy(parent2Copy.genes.get(i)));
		}
		
		//old code for reproduction when brain size and number of eyes cannot be modified
		/*
		//parentCopy the brain if both parents have the same sized brain and same number of eyes, make a normal parent copy with eyes and brain
		if(getBrain().sameSize(partner.getBrain()) && eyes.length == partner.eyes.length){
			//copy this creature
			baby = copy();
			//make the baby's brain a parent copy of both parents brains
			baby.brain = brain.parentCopy(partner.brain);
			
			//parentCopy all genes
			baby.genes = new ArrayList<Gene>();
			for(int i = 0; i < genes.size(); i++) baby.genes.add(genes.get(i).parentCopy(partner.genes.get(i)));
		}
		
		//if both parents do not have the same sized brain, or they do not have the same number of eyes, pick a random parent to copy
		else{
			if(Math.random() > .5){
				baby = copy();
				
				baby.eyes = new Eye[eyes.length];
				for(int i = 0; i < eyes.length; i++) baby.eyes[i] = eyes[i].copy();
				
				baby.genes = new ArrayList<Gene>();
				for(Gene g : genes) baby.genes.add(g.copy());
			}
			else{
				baby = partner.copy();
				
				baby.eyes = new Eye[partner.eyes.length];
				for(int i = 0; i < partner.eyes.length; i++) baby.eyes[i] = partner.eyes[i].copy();
				
				baby.genes = new ArrayList<Gene>();
				for(Gene g : partner.genes) baby.genes.add(g.copy());
			}
		}
		*/
		
		
		//set up baby based on parents
		
		//ensure that all gene references are in their correct locations
		baby.updateGeneReferences();

		//mutate baby
		baby.mutate();
		
		//determine the amount of energy each parent will give
		//give energy from this NeuralNetCreature
		double giveEnergy;
		if(getEnergy() < getEnergyToBaby()) giveEnergy = getEnergy();
		else giveEnergy = getEnergyToBaby();
		baby.setEnergy(giveEnergy);
		addEnergy(-giveEnergy);
		//give energy from the partner
		if(partner.getEnergy() < partner.getEnergyToBaby()) giveEnergy = partner.getEnergy();
		else giveEnergy = partner.getEnergyToBaby();
		baby.addEnergy(giveEnergy);
		partner.addEnergy(-giveEnergy);
		
		//set stats of baby
		baby.setEnergy(giveEnergy);
		baby.setAngle(Math.random() * Math.PI * 2);
		baby.setSpeed(0);
		baby.setGeneration(Math.max(getGeneration(), partner.getGeneration()) + 1);
		baby.setId(getSimulation().nextCreatureId());
		baby.setFatherId(getId());
		baby.setMotherId(partner.getId());
		baby.getTemperature().setTemp((getTemperature().getTemp() + partner.getTemperature().getTemp()) / 2);
		
		//set stats of parents
		setBreedChildren(getBreedChildren() + 1);
		partner.setBreedChildren(getBreedChildren() + 1);
		
		//set the position of the baby to between the parents
		baby.setX((getX() + partner.getX()) / 2);
		baby.setY((getY() + partner.getY()) / 2);
		
		//add baby to simulation
		getSimulation().addCreature(baby);
		
		//return baby
		return baby;
	}
	
	/**
	 * Tell this Creature to think about what it should do next, 
	 * and then it will decide which direction to change it's 
	 * angle, speed, and whether or not to eat
	 */
	public void think(){
		//old code for taking inputs based on food in nearby tiles
		/*
		double tSize = Main.SETTINGS.tileSize.value();
		
		//set variables based on neural network
		
		//find the food values of all surrounding tiles
		double[] inputs = new double[brain.getInputSize()];
		
		//first tile is based on the tile underneath the creature
		inputs[0] = calculateFoodInput(getX(), getY());
		
		//the next 4 are based on the position of the Creature 
		//create a list of the 4 directly adjacent tiles values, then based on the one closest to
		//	the creature's facing direction, the tiles inputs are then fed into those 4 input indexes
		double[] tileValues = new double[4];
		double[] xPos = new double[]{getX(), 			getX() + tSize,	getX(),			getX() - tSize};
		double[] yPos = new double[]{getY() - tSize,	getY(),			getY() + tSize,	getX()};
		for(int i = 0; i < tileValues.length; i++) tileValues[i] = calculateFoodInput(xPos[i], yPos[i]);
		//values to use to find the nearest tile to the direction facing
		Point2D.Double compare = new Point2D.Double(
				getX() + Math.cos(getAngle()) * tSize * 2,
				getY() - Math.sin(getAngle()) * tSize * 2
		);
		//find the closest tile value based on the location and direction the Creature is facing
		int closestIndex = 0;
		for(int i = 1; i < 4; i++){
			if(compare.distance(xPos[i], yPos[i]) < compare.distance(xPos[closestIndex], yPos[closestIndex])){
				closestIndex = i;
			}
		}
		//now start at that tile value until all 4 values are set as inputs
		for(int i = 0; i < 4; i++){
			inputs[1 + i] = tileValues[(i + closestIndex) % 4];
		}
		
		//now do the same thing but with the surrounding 8 values
		tileValues = new double[8];
		xPos = new double[]{
				getX(),				getX() + tSize, getX() + tSize * 2,	getX() + tSize,
				getX(),				getX() - tSize, getX() - tSize * 2,	getX() - tSize
		};
		yPos = new double[]{
				getY() - tSize * 2,	getY() - tSize, getY(),				getY() + tSize,
				getY() + tSize * 2,	getY() + tSize, getY(),				getY() - tSize
		};
		for(int i = 0; i < tileValues.length; i++) tileValues[i] = calculateFoodInput(xPos[i], yPos[i]);
		//find the closest tile value based on the location and direction the Creature is facing
		closestIndex = 0;
		for(int i = 1; i < 4; i++){
			if(compare.distance(xPos[i], yPos[i]) < compare.distance(xPos[closestIndex], yPos[closestIndex])){
				closestIndex = i;
			}
		}
		//now start at that tile value until all 8 values are set as inputs
		for(int i = 0; i < 8; i++){
			inputs[5 + i] = tileValues[(i + closestIndex) % 8];
		}
		
		//set the speed as a parameter
		inputs[13] = getSpeed() / Main.SETTINGS.creatureMaxSpeed.value();
		
		//set the age as a parameter, putting it through a rational function so that age can change this value forever
		inputs[14] = (-1 / (.007 * getAge() + 1) + 1);
		
		//set the energy, this is based on the maximum energy of the creature and their current energy
		inputs[15] = getEnergy() / getMaxEnergySizeBased();
		
		//set the distance and angle difference to the nearest creature as the next 2 inputs
		NeuralNetCreature nearest = getSimulation().getNearestCreature(getX(), getY(), this);
		//if there is no nearest creature, set the angle and distance to 0
		if(nearest == null){
			inputs[16] = 0;
			inputs[17] = 0;
		}
		//otherwise set the inputs based on the distance to the inputs
		else{
			//set angle
			
			//determine the angles to both creatures
			double thisAngle = getAngle();
			double nearAngle = nearest.getAngle();
			
			inputs[16] = Math.sin(thisAngle + nearAngle);
			
			//set distance
			double distance = nearest.getPos().distance(getPos());
			
			//if distance is greater than a certain distance, set it as a max
			distance = Math.min(distance, 1000.0);
			inputs[17] = distance / 1000.0;
		}
		
		//set the babyCooldown as an input variable
		inputs[18] = (double)babyCooldown / Main.SETTINGS.creatureBabyCooldown.value();
		*/

		double[] inputs = new double[brain.getInputSize()];
		
		//determine inputs based on each eye
		for(int i = 0; i < eyes.length; i++){
			//first look through the eye and determine what the eye sees
			eyes[i].look(getSimulation(), this);
			
			//get the values of what the eye saw
			double[] eyeSights = eyes[i].getSeenQuantities();
			
			//for each value that the eye saw
			for(int j = 0; j < eyeSights.length; j++){
				//the input values of the brain are set equal to the values of the each value the eye saw
				//i is the multiplied by the number of input nodes per eye, and j is the input node currently on
				inputs[i * eyeSights.length + j] = eyeSights[j];
			}
		}
		
		int eyeParams = eyes.length * Eye.NUM_QUANTITIES;
		
		//determine extra inputs from this creature
		//set the speed as a parameter
		inputs[eyeParams] = getMoveSpeed() / Main.SETTINGS.creatureMaxSpeed.value();
		
		//set the age as a parameter, putting it through a rational function so that age can change this value forever
		inputs[eyeParams + 1] = (-1 / (.007 * getAge() + 1) + 1);
		
		//set the energy, this is based on the maximum energy of the creature and their current energy
		inputs[eyeParams + 2] = getEnergy() / getMaxEnergySizeBased();

		//set the babyCooldown as an input variable
		inputs[eyeParams + 3] = (double)babyCooldown / Main.SETTINGS.creatureBabyCooldown.value();
		
		//set the species amount as an input variable
		inputs[eyeParams + 4] = getSpecies().getSpeciesValue();
		
		//set the temperature as an input
		double rangeTemp = Main.SETTINGS.temperatureWorldRange.value();
		double minTemp = Main.SETTINGS.temperatureMin.value() - rangeTemp;
		double maxTemp = Main.SETTINGS.temperatureMax.value() + rangeTemp;
		double sizeTemp = maxTemp - minTemp;
		
		double temperature = getTemperature().getTemp();
		if(temperature > 0){
			if(maxTemp <= 0) inputs[eyeParams + 5] = temperature / sizeTemp;
			else inputs[eyeParams + 5] = temperature / maxTemp;
		}
		else{
			if(minTemp >= 0) inputs[eyeParams + 5] = temperature / sizeTemp;
			else inputs[eyeParams + 5] = temperature / minTemp;
		}
		
		//set the fur as an input
		inputs[eyeParams + 6] = getFur() / Main.SETTINGS.temperatureFurMax.value();

		//set memory node inputs
		for(int i = 0; i < MEMORY_NODES; i++){
			//if the memory node is above 0, then change the input node
			if(brainOutputs[brainOutputs.length - 1 - i * 2] > 0){
				inputs[inputs.length - 1 - i] = brainOutputs[brainOutputs.length - 2 - i * 2];
			}
			else{
				inputs[inputs.length - 1 - i] = brain.getLayers()[0][inputs.length - 1 - i].getValue();
				brainOutputs[brainOutputs.length - 2 - i * 2] = brain.getLayers()[0][inputs.length - 1 - i].getValue();
			}
		}
		
		//send the inputs
		brain.feedInputs(inputs);
		//calculate outputs
		brain.calculateOutputs();
		
		brainOutputs = brain.getOutputs();
	}
	
	/**
	 * Use the outputs from the brain to determine what actions it should take
	 */
	public void useOutputs(){
		//use the outputs to control the movement
		addAngle(Main.SETTINGS.creatureAngleChange.value() * brainOutputs[ANGLE_NODE]);
		
		//set the speed of the creature based on the output node
		double speedChange = Main.SETTINGS.creatureSpeedChange.value();
		double speedMax = Main.SETTINGS.creatureMaxSpeed.value();
		//find the speed that the creature wants to go, based on the output node
		double speedOut = brainOutputs[SPEED_NODE];
		if(speedOut < 0) speedOut *= speedMax / 2;
		else speedOut *= speedMax;
		double speedDiff = Math.abs(speedOut - getMoveSpeed());
		
		//if the creature's speed is within the speed change range, then set the speed
		if(speedDiff < speedChange){
			setSpeed(speedOut);
		}
		//otherwise move the speed in the direction that the creature wants to go
		else if(speedOut < getMoveSpeed()) addSpeed(-speedMax);
		else addSpeed(speedMax);
		
		//if the eat output is greater than 0, eat
		boolean shouldEat = brainOutputs[EAT_NODE] > 0;
		if(shouldEat) eat();
		setShouldEat(shouldEat);
		
		//determine if fur should be shed or grown
		//grow fur
		if(brainOutputs[FUR_NODE] > 0){
			furAmount += brainOutputs[FUR_NODE] * Main.SETTINGS.temperatureFurGrowthRate.value();
		}
		//shed fur
		else{
			furAmount += brainOutputs[FUR_NODE] * Main.SETTINGS.temperatureFurShedRate.value();
		}
		//keep fur in normal range
		furAmount = Math.max(0, Math.min(furGene.getValue(), furAmount));
		
		//fight or don't fight
		if(brainOutputs[FIGHT_NODE] > 0){
			attackNearestCreature();
		}
		
		//vomit or don't vomit
		boolean shouldVomit = brainOutputs[VOMIT_NODE] > 0;
		if(shouldVomit) vomit();
		setShouldVomit(shouldVomit);
		
		int eyeOutputIndex = EXTRA_OUTPUTS;
		
		//set the distances and angles for the eyes
		for(int i = 0; i < eyes.length; i++){
			eyes[i].setDistance(brainOutputs[eyeOutputIndex + i * 2] * .5 + .5);
			eyes[i].setAngle(brainOutputs[eyeOutputIndex + i * 2 + 1] * .5 + .5);
		}
	}
	
	/**
	 * Find the value for the neural net input based on the food in the tile of the given position.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the food value, 0 if no tile is found
	 */
	public double calculateFoodInput(double x, double y){
		Tile t = getSimulation().getContainingTile(x, y);
		if(t != null) return Math.max(0, Math.min(1, t.getFood() / Main.SETTINGS.foodMax.value()));
		else return 0;
	}
	
	/**
	 * Mutate this NeuralNet creature, including it's genes
	 */
	public void mutate(){
		//update mix, max, and scalar values for all genes
		updateGeneValues();
		
		//do a special mutation for mutability where it can change a random amount, unrelated to it's scalar
		mutability.mutate(Main.SETTINGS.mutabilityChange.value() / mutability.getScalar(), 0);
		
		//mutate all genes
		for(Gene g : genes) g.mutate(getMutability(), Main.SETTINGS.randomizeChance.value());
		
		//update the eyes to ensure the correct number of eyes exist
		updateMutatedEyes();
		
		//mutate the brain
		brain.mutate(getMutability() * Main.SETTINGS.brainMutationScalar.value(), Main.SETTINGS.randomizeChance.value());
		
	}
	
	/**
	 * Update the number of eyes this {@link NeuralNetCreature} has based on the number of eyes in their gene
	 */
	public void updateMutatedEyes(){
		//add eyes
		while(getNumberOfEyes() > eyes.length){
			Eye e;
			if(eyes.length == 0) e = new Eye();
			else e = eyes[(int)(Math.random() * eyes.length)].copy();
			e.mutate(getMutability(), Main.SETTINGS.randomizeChance.value());
			addEye(e);
		}
		
		//remove eyes
		while(getNumberOfEyes() < eyes.length){
			removeEye((int)(Math.random() * eyes.length));
		}
		
		updateGeneReferences();
		updateGeneValues();
	}
	
	/**
	 * Modify this creature so that the eye with the given index is removed.<br>
	 * Does nothing if this creature has the minimum number of eyes or has 0 eyes.
	 * @param index the index to remove
	 */
	public void removeEye(int index){
		if(eyes.length <= Main.SETTINGS.eyeNumMin.value() || eyes.length <= 0) return;

		eyes = ArrayHandler.remove(eyes, index);
		
		//remove appropriate eye related genes
		int start = NUM_GENES + index * Eye.NUM_GENES;
		for(int i = 0; i < Eye.NUM_GENES; i++){
			genes.remove(start);
		}
		
		//remove appropriate eye input nodes
		start = Eye.NUM_QUANTITIES * index;
		for(int i = 0; i < Eye.NUM_QUANTITIES; i++){
			brain.removeInputNode(start);
		}
		//remove appropriate eye output nodes
		start = EXTRA_OUTPUTS + Eye.NUM_GENES / 2 * index;
		for(int i = 0; i < Eye.NUM_GENES / 2; i++){
			brain.removeOutputNode(start);
		}
		
		updateGeneReferences();
	}
	
	/**
	 * Add the given eye to this creatures eye list.<br>
	 * Does nothing if this creature has the maximum number of eyes.
	 * @param eye
	 */
	public void addEye(Eye eye){
		if(eyes.length >= Main.SETTINGS.eyeNumMax.value()) return;
		
		int oldEyeSize = eyes.length;
		eyes = ArrayHandler.add(eyes, eye);
		
		//add appropriate eye related genes
		genes.addAll(eye.getAllGenes());
		//add input nodes
		Node[][] layers = brain.getLayers();
		for(int i = 0; i < Eye.NUM_QUANTITIES; i++){
			Node n = layers[0][(int)(Math.random() * oldEyeSize) * Eye.NUM_QUANTITIES + i].copy();
			brain.addInputNode(n);
		}
		//add output nodes
		for(int i = 0; i < Eye.NUM_GENES / 2; i++){
			Node n = layers[layers.length - 1][(int)(Math.random() * oldEyeSize) * Eye.NUM_GENES / 2 + i].copy();
			brain.addOutputNode(n);
		}
		
		updateGeneReferences();
	}
	
	/**
	 * Get the {@link Gene} that governs the number of eyes in this {@link NeuralNetCreature}
	 * @return the {@link Gene}
	 */
	public Gene getEyesNumGene(){
		return numberOfEyes;
	}
	
	/**
	 * Get the actual number of eyes this {@link NeuralNetCreature} has
	 * @return the number of eyes as an integer
	 */
	public int getNumberOfEyes(){
		return (int)Math.round(numberOfEyes.getValue());
	}
	
	/**
	 * Get the array of eyes that this {@link NeuralNetCreature} uses
	 * @return the array of eyes
	 */
	public Eye[] getEyes(){
		return eyes;
	}
	
	public double getMutability(){
		return this.mutability.getValue();
	}
	/**
	 * Set mutability to the given value and keep it in the valid mutability range
	 * @param mutability
	 */
	public void setMutability(double mutability){
		this.mutability.setValue(mutability);
	}
	
	public double getEnergyToBaby(){
		return this.energyToBaby.getValue();
	}
	public void setEnergyToBaby(double energyToBaby){
		this.energyToBaby.setValue(energyToBaby);
	}

	/**
	 * Get the maximum energy this creature can hold, based on size gene
	 * @return the maximum energy of this creature, based on size
	 */
	public double getMaxEnergySizeBased(){
		return getMaxEnergy() + getSizeGene() * Main.SETTINGS.creatureSizeMaxEnergyScalar.value();
	}
	/**
	 * Get the maximum energy this creature can hold, before size is taken into account
	 * @return the maximum energy of this creature
	 */
	public double getMaxEnergy(){
		return this.maxEnergy.getValue();
	}
	
	public void setMaxEnergy(double maxEnergy){
		this.maxEnergy.setValue(maxEnergy);
	}

	public double getSizeGene(){
		return this.sizeGene.getValue();
	}
	public void setSizeGene(double sizeGene){
		this.sizeGene.setValue(sizeGene);
	}
	
	@Override
	public double getFur(){
		return furAmount;
	}
	
	/**
	 * Get the radius of this creature based on its current age and amount of energy
	 */
	@Override
	public double calculateRadius(){
		//start with the minimum creature size
		return Main.SETTINGS.creatureMinRadius.value() +
				//add an amount based on the size gene and fur
				(getSizeGene() + getFur() * Main.SETTINGS.temperatureFurSizeScalar.value()) *
				//only use a percentage of that size based on the age
				((-1 / (Main.SETTINGS.creatureSizeAgeScalar.value() * getAge() + 1) + 1)) 
				//increase size based on current energy
				* (.5 + .5 * getEnergy() / getMaxEnergySizeBased());
	}
	
	/**
	 * Get a scalar based on the radius of the creature. High radius results in lower values, low radius results in higher values
	 * @return the ratio based on the radius, scalar is always in the range [0, 1]
	 */
	public double getRadiusScalar(){
		return 1 - Math.min(1, Math.max(0, getRadius() / (Main.SETTINGS.creatureMinRadius.value() + Main.SETTINGS.creatureMaxSize.value())));
	}
	
	/**
	 * Speed added is automatically decreased based current size
	 */
	@Override
	public void addSpeed(double speed){
		super.addSpeed(speed * getRadiusScalar());
	}
	
	/**
	 * Maximum speed is automatically reduced based on creature radius
	 */
	@Override
	public double getMaxSpeed(){
		return super.getMaxSpeed() * Math.pow(getRadiusScalar(), Main.SETTINGS.creatureSizeSlowScalar.value());
	}
	
	/**
	 * Angle added is automatically decreased based current size
	 */
	@Override
	public void addAngle(double angle){
		super.addAngle(angle * getRadiusScalar());
	}
	
	/**
	 * Get the angle from this creature to the given creature
	 * @param c the creature
	 * @return the angle
	 */
	public double getAngleTo(NeuralNetCreature c){
		return Math.atan2(c.getY() - getY(), c.getX() - getX());
	}
	
	/**
	 * When losing energy, more energy is lost when size is large
	 */
	@Override
	public void addEnergy(double energy){
		if(energy < 0) super.addEnergy(energy / (.5 + .5 * getRadiusScalar()));
		else super.addEnergy(energy);
	}
	
	public NeuralNet getBrain(){
		return brain;
	}
	
	/**
	 * Get the outputs of this creatures brain the last time they thought
	 * @return the outputs
	 */
	public double[] getBrainOutputs(){
		return brainOutputs;
	}
	
	/**
	 * Get a list of all the tiles that this NeuralNetCreature is touching
	 * @return
	 */
	public ArrayList<Tile> getContainingTiles(){
		return containedTiles;
	}
	
	@Override
	public void setEnergy(double energy){
		super.setEnergy(energy);
		if(getEnergy() > getMaxEnergy()) super.setEnergy(getMaxEnergy());
	}

	public int getBabyCooldown(){
		return babyCooldown;
	}
	public void setBabyCooldown(int cooldown){
		if(cooldown < 0) cooldown = 0;
		this.babyCooldown = cooldown;
	}
	
	/**
	 * Copy this NeuralNetCreature, this copy will be considered to be in the same simulation
	 */
	public NeuralNetCreature copy(){
		NeuralNetCreature c = new NeuralNetCreature(getSimulation());
		c.revive();
		
		//copy data
		c.setX(getX());
		c.setY(getY());
		c.setSpeed(getMoveSpeed());
		c.setAngle(getAngle());
		c.setFatherId(getFatherId());
		c.setMotherId(getMotherId());
		c.getTemperature().setTemp(getTemperature().getTemp());
		
		//copy and brain
		c.brain = brain.copy();
		
		//copy genes
		c.genes = new ArrayList<Gene>();
		for(Gene g : genes) c.genes.add(g.copy());
		c.updateGeneReferences();
		
		//copy eyes
		c.eyes = new Eye[eyes.length];
		for(int i = 0; i < c.eyes.length; i++){
			c.eyes[i] = eyes[i].copy();
		}
		
		return c;
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			boolean success = true;
			//save miscellaneous fields
			write.println(babyCooldown + " " + furAmount);
			
			//save genes
			write.println(genes.size());
			for(Gene g : genes) g.save(write);
			
			//save brain
			success &= brain.save(write);
			
			//save eyes
			write.println(eyes.length);
			for(Eye eye : eyes) eye.save(write);
			
			//save super class
			success &= super.save(write);
			return success;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean load(Scanner read){
		try{
			boolean success = true;
			//load miscellaneous fields
			babyCooldown = read.nextInt();
			furAmount = read.nextDouble();
			
			//load genes
			int num = read.nextInt();
			genes = new ArrayList<Gene>();
			for(int i = 0; i < num; i++){
				Gene g = new Gene(0);
				success &= g.load(read);
				genes.add(g);
			}
			updateGeneReferences();
			
			//load brain
			success &= brain.load(read);
			
			//load eyes
			num = read.nextInt();
			eyes = new Eye[num];
			for(int i = 0; i < eyes.length; i++){
				eyes[i] = new Eye();
				eyes[i].load(read);
			}

			//load super class
			success &= super.load(read);
			return success;
		}catch(Exception e){
			System.err.println("A NeuralNetCreature failed to load");
			e.printStackTrace();
			return false;
		}
	}
	
}
