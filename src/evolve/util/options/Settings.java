package evolve.util.options;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import evolve.sim.Simulation;
import evolve.util.Saveable;

/**
 * A class that contains Settings objects for every setting in the simulation. <br>
 * To add a new setting to this file: <br>
 * 	1. add it as a field in this class. Fields should be public as this class should be accessed as a static object <br>
 * 	2. add it to the settings ArrayList <br>
 */
public class Settings implements Saveable{
	
	private ArrayList<Setting<?>> settings;
	
	//misc settings
	/**
	 * The width of the content of the simulation GUI
	 */
	public IntSetting simGuiWidth;
	/**
	 * The height of the content of the simulation GUI
	 */
	public IntSetting simGuiHeight;
	/**
	 * The maximum number of points that the population graph will hold, 0 or a negative number for infinite points
	 */
	public IntSetting populationGraphMaxPoints;
	/**
	 * The number of 1/100 of a second between each time that a population data point is recorded
	 */
	public IntSetting populationGraphUpdateRate;
	/**
	 * The maximum number of points that the mutability graph will hold, 0 or a negative number for infinite points
	 */
	public IntSetting mutabilityGraphMaxPoints;
	/**
	 * The number of 1/100 of a second between each time that a mutability data point is recorded
	 */
	public IntSetting mutabilityGraphUpdateRate;
	/**
	 * The maximum number of points that the age graph will hold, 0 or a negative number for infinite points
	 */
	public IntSetting ageGraphMaxPoints;
	/**
	 * The number of 1/100 of a second between each time that a age data point is recorded
	 */
	public IntSetting ageGraphUpdateRate;
	/**
	 * True if creatures should reproduce with just themselves, or false if they should require a partner to evolve
	 */
	public BooleanSetting asexualReproduction;
	/**
	 * The number of initial creatures when a new simulation begins
	 */
	public IntSetting initialCreatures;
	/**
	 * The energy a creature has when they are randomly generated
	 */
	public DoubleSetting initialEnergy;
	/**
	 * The minimum number of creatures that will exist, if the population drops below this value, a new creature is generated
	 */
	public IntSetting minimumCreatures;
	
	
	//creature settings
	/**
	 * The maximum amount a creature can change their angle per tick, can be negative or positive of this value
	 */
	public DoubleSetting creatureAngleChange;
	/**
	 * The maximum amount a creature can change their speed per tick, can be negative or positive of this value
	 */
	public DoubleSetting creatureSpeedChange;
	/**
	 * The maximum speed of a creature
	 */
	public DoubleSetting creatureMaxSpeed;
	/**
	 * The amount of energy lost on movement is based on the distance moved raised to a power equal to the radius 
	 * of a creature multiplied by this value
	 */
	public DoubleSetting creatureEnergySpeedScalar;
	/**
	 * Value that defines how overall speed is reduced by size
	 */
	public DoubleSetting creatureSizeSlowScalar;
	/**
	 * The percentage of creatureMaxSpeed that can be used while moving in reverse. 
	 * Use values in the range [0, 1]. 
	 * 0 stops reversing. 
	 * 1 means reverse is the same speed as forwards. 
	 */
	public DoubleSetting creatureReverseRatio;
	/**
	 * The minimum size radius for a creature's radius
	 */
	public DoubleSetting creatureMinRadius;
	/**
	 * The minimum size radius for a creature's size gene
	 */
	public DoubleSetting creatureMinSize;
	/**
	 * The maximum size radius for a creature's size gene
	 */
	public DoubleSetting creatureMaxSize;
	/**
	 * The scalar that defines how fast the size gene changes
	 */
	public DoubleSetting creatureSizeScalar;
	/**
	 * The ratio of age that is used for determining a creatures size based on age. 
	 * Low values make maximum size reached later in life, 
	 * High values make maximum size reached earlier in life,
	 */
	public DoubleSetting creatureSizeAgeScalar;
	/**
	 * This value is multiplied by the size gene before being added to the maximum energy
	 */
	public DoubleSetting creatureSizeMaxEnergyScalar;
	/**
	 * The amount of energy it costs to eat each update
	 */
	public DoubleSetting creatureEatCost;
	/**
	 * The amount of energy a creature loses per update
	 */
	public DoubleSetting creatureEnergyLoss;
	/**
	 * The base amount of energy that can be lost while moving. The faster it move, the more energy it loses
	 */
	public DoubleSetting creatureMoveCost;
	/**
	 * The maximum amount of energy this creature can have
	 */
	public DoubleSetting creatureMaxEnergy;
	/**
	 * This value is multiplied to the amount of a creature's maximum energy on a mutation for that gene
	 */
	public DoubleSetting creatureMaxEnergyScalar;
	/**
	 * The scalar that determines how fast the species gene mutates
	 */
	public DoubleSetting creatureSpeciesGeneScalar;
	/**
	 * The maximum space between species that creatures can be to still breed
	 */
	public DoubleSetting creatureBreedRange;
	/**
	 * This scalar is multiplied by the number of seconds a creature has lived for to determine the amount of energy lost when getting sick
	 */
	public DoubleSetting creatureSickAmountScalar;
	/**
	 * The scalar that controls the degree that age effects the chance of getting sick. 
	 * 60000 gives a 50% of getting sick at 10 minutes old. 
	 * Low values make sickness more likely sooner in life. 
	 * High values make sickness more likely later in life.
	 */
	public DoubleSetting creatureSickScalar;
	/**
	 * The minimum amount of energy a baby can gain from a parent when it is born
	 */
	public DoubleSetting creatureMinBabyEnergy;
	/**
	 * The maximum amount of energy a baby can gain from a parent when it is born
	 */
	public DoubleSetting creatureMaxBabyEnergy;
	/**
	 * This value is multiplied to the amount of energy required to be given to a baby when a baby is born asexually
	 */
	public DoubleSetting creatureBabyEnergyAsexual;
	/**
	 * This value is multiplied to the amount of energy given to a baby on a mutation for that gene
	 */
	public DoubleSetting creatureBabyEnergyScalar;
	/**
	 * The number of updates a creature must wait before it can have another baby
	 */
	public IntSetting creatureBabyCooldown;
	/**
	 * The number of updates a creature must touch another creature in order to have a baby
	 */
	public IntSetting creatureBreedTime;
	
	
	//eye settings
	/**
	 * The minimum number of eyes a creature has
	 */
	public IntSetting eyeNumMin;
	/**
	 * The maximum number of eyes a creature has
	 */
	public IntSetting eyeNumMax;
	/**
	 * The scalar that effects mutations for the number of eyes
	 */
	public DoubleSetting eyeNumScalar;
	/**
	 * The minimum distance that an eye can see
	 */
	public DoubleSetting eyeDistanceMin;
	/**
	 * The maximum distance that an eye can see
	 */
	public DoubleSetting eyeDistanceMax;
	/**
	 * The scalar that effects eye distance mutations
	 */
	public DoubleSetting eyeDistanceScalar;
	/**
	 * The angle distance, in radians, in both directions from where a creature is looking
	 */
	public DoubleSetting eyeAngleRange;
	/**
	 * The scalar that effects eye angle mutations
	 */
	public DoubleSetting eyeAngleScalar;
	
	
	//tile settings
	/**
	 * The size of the tiles in both width and height
	 */
	public DoubleSetting tileSize;
	/**
	 * The number of tiles on the x axis
	 */
	public IntSetting tilesX;
	/**
	 * The number of tiles on the y axis
	 */
	public IntSetting tilesY;
	/**
	 * The scalar that determines how fast the deaths of creatures change the species (color) of tiles. 
	 * Set to 0 to disable creature deaths changing tile species, set to low or high values for low or high 
	 * changes in species amount, respectively.
	 */
	public DoubleSetting tileSpeciesScalar;
	/**
	 * The amount of energy creatures lose each tick while on water
	 */
	public DoubleSetting tileWaterDamage;
	/**
	 * The maximum amount of food a tile can have
	 */
	public DoubleSetting foodMax;
	/**
	 * The percentage of the food in a tile that is eaten when a tile is eaten
	 */
	public DoubleSetting foodEatPercent;
	
	
	//world generation settings
	/**
	 * The seed that the {@link Simulation} should use to generate worlds
	 */
	public IntSetting worldSeed;
	/**
	 * The weight of the first kind of shape in the world generation
	 */
	public DoubleSetting worldShapeWeight1;
	/**
	 * The weight of the second kind of shape in the world generation
	 */
	public DoubleSetting worldShapeWeight2;
	/**
	 * The weight of the third kind of shape in the world generation
	 */
	public DoubleSetting worldShapeWeight3;
	/**
	 * The weight of the fourth kind of shape in the world generation
	 */
	public DoubleSetting worldShapeWeight4;
	/**
	 * The weight of the fifth kind of shape in the world generation
	 */
	public DoubleSetting worldShapeWeight5;
	/**
	 * The weight of the sixth kind of shape in the world generation
	 */
	public DoubleSetting worldShapeWeight6;
	/**
	 * The main scalar of the first kind of shape in the world generation
	 */
	public DoubleSetting worldShapeScalar1;
	/**
	 * The main scalar of the second kind of shape in the world generation
	 */
	public DoubleSetting worldShapeScalar2;
	/**
	 * The main scalar of the third kind of shape in the world generation
	 */
	public DoubleSetting worldShapeScalar3;
	/**
	 * The main scalar of the fourth kind of shape in the world generation
	 */
	public DoubleSetting worldShapeScalar4;
	/**
	 * The main scalar of the fifth kind of shape in the world generation
	 */
	public DoubleSetting worldShapeScalar5;
	/**
	 * The main scalar of the sixth kind of shape in the world generation
	 */
	public DoubleSetting worldShapeScalar6;
	/**
	 * The percentage of an offset that is used for the main scalar of the first shape
	 */
	public DoubleSetting worldShapePercent1;
	/**
	 * The percentage of an offset that is used for the main scalar of the second shape
	 */
	public DoubleSetting worldShapePercent2;
	/**
	 * The percentage of an offset that is used for the main scalar of the third shape
	 */
	public DoubleSetting worldShapePercent3;
	/**
	 * The percentage of an offset that is used for the main scalar of the fourth shape
	 */
	public DoubleSetting worldShapePercent4;
	/**
	 * The percentage of an offset that is used for the main scalar of the fifth shape
	 */
	public DoubleSetting worldShapePercent5;
	/**
	 * The percentage of an offset that is used for the main scalar of the sixth shape
	 */
	public DoubleSetting worldShapePercent6;
	/**
	 * The scalar that determines how much each tile will be offset by it's random amount, for the first shape
	 */
	public DoubleSetting worldShapeOffset1;
	/**
	 * The scalar that determines how much each tile will be offset by it's random amount, for the second shape
	 */
	public DoubleSetting worldShapeOffset2;
	/**
	 * The scalar that determines how much each tile will be offset by it's random amount, for the third shape
	 */
	public DoubleSetting worldShapeOffset3;
	/**
	 * The scalar that determines how much each tile will be offset by it's random amount, for the fourth shape
	 */
	public DoubleSetting worldShapeOffset4;
	/**
	 * The scalar that determines how much each tile will be offset by it's random amount, for the fifth shape
	 */
	public DoubleSetting worldShapeOffset5;
	/**
	 * The scalar that determines how much each tile will be offset by it's random amount, for the sixth shape
	 */
	public DoubleSetting worldShapeOffset6;
	/**
	 * The overall scalar for how quickly the world changes
	 */
	public DoubleSetting worldScalar;
	/**
	 * The starting point for determining color of species
	 */
	public DoubleSetting worldOffset;
	/**
	 * The overall scalar for the position of the world on the x axis
	 */
	public DoubleSetting worldXPos;
	/**
	 * The overall scalar for the scale of the world on the x axis
	 */
	public DoubleSetting worldXScalar;
	/**
	 * The overall scalar for the position of the world on the y axis
	 */
	public DoubleSetting worldYPos;
	/**
	 * The overall scalar for the scale of the world on the y axis
	 */
	public DoubleSetting worldYScalar;
	/**
	 * The number of rivers that attempt to generate in each chunk
	 */
	public IntSetting worldRiverCount;
	/**
	 * The chance for each river in a chunk to generate
	 */
	public DoubleSetting worldRiverChance;
	/**
	 * The number of chunks around a chunk that a river can generate into
	 */
	public IntSetting worldRiverBorderSize;
	/**
	 * The approximate minimum radius of a river
	 */
	public DoubleSetting worldRiverMinSize;
	/**
	 * The approximate maximum radius of a river
	 */
	public DoubleSetting worldRiverMaxSize;
	/**
	 * The percentage of a river that is noise. Use values close to 0 for smooth curves, 
	 * use values close to 1 for lots of noise, and beyond 1 for maximum noise
	 */
	public DoubleSetting worldRiverNoise;
	/**
	 * The value that determines how important distance is for river noise. 
	 * Use low positive values for distance to be not very important. 
	 * Use high positive values for distance to be very important
	 */
	public DoubleSetting worldRiverNoiseScalar;
	/**
	 * The number of land masses that can generate in a single chunk
	 */
	public IntSetting worldIslandCount;
	/**
	 * The chance for each island in a chunk to generate
	 */
	public DoubleSetting worldIslandChance;
	/**
	 * The minimum radius of an island
	 */
	public DoubleSetting worldIslandMinSize;
	/**
	 * The maximum radius of an island
	 */
	public DoubleSetting worldIslandMaxSize;
	/**
	 * The number of chunks around a chunk that an island can generate into
	 */
	public IntSetting worldIslandBorderSize;
	/**
	 * The percentage of an island that is noise. Use values close to 0 for smooth curves, 
	 * use values close to 1 for lots of noise, and beyond 1 for maximum noise
	 */
	public DoubleSetting worldIslandNoise;
	/**
	 * The value that determines how important distance is for island noise. 
	 * Use low positive values for distance to be not very important. 
	 * Use high positive values for distance to be very important
	 */
	public DoubleSetting worldIslandNoiseScalar;
	/**
	 * The number of tiles in a chunk
	 */
	public IntSetting worldChunkSize;
	
	
	//season settings
	/**
	 * The minimum base temperature, in celcius, that can exist in the simulation. 
	 * This is the temperature of tiles at the north most point of the simulation at the midpoint of summer and winter
	 */
	public DoubleSetting temperatureMin;
	/**
	 * The maximum base temperature, in celcius, that can exist in the simulation. 
	 * This is the temperature of tiles at the south most point of the simulation at the midpoint of summer and winter
	 */
	public DoubleSetting temperatureMax;
	/**
	 * The rate at which creatures change their temperature based on the temperature of the tile they are standing on
	 */
	public DoubleSetting temperatureCreatureRate;
	/**
	 * The rate at which tiles change their temperature based on the temperature of the simulation
	 */
	public DoubleSetting temperatureTileRate;
	/**
	 * The range of temperatures from the center of the simulation. 
	 * The north most point can reach this temperature below min temperature. 
	 * The south most point can reach this temperature above max temperature. 
	 */
	public DoubleSetting temperatureWorldRange;
	/**
	 * The temperature that creatures are most comfortable at
	 */
	public DoubleSetting temperatureCreatureComfort;
	/**
	 * A scalar that defines how much energy creatures lose from temperature
	 */
	public DoubleSetting temperatureCreatureScalar;
	/**
	 * A scalar that defines how much creatures are slowed when they are too cold. 
	 * Use low values to make temperature effect creature speed less, 
	 * use high values to make temperature effect creature speed more. 
	 */
	public DoubleSetting temperatureCreatureSlow;
	/**
	 * A scalar that defines how much creatures are sped up when they are hot. 
	 * Use low values to make temperature effect creature speed less, 
	 * use high values to make temperature effect creature speed more. 
	 */
	public DoubleSetting temperatureCreatureSpeed;
	/**
	 * A scalar that defines how much a creature's chance of getting sick increases when they are hot. 
	 * Use low values to make temperature effect creature sick chance less, 
	 * use high values to make temperature effect creature sick chance more. 
	 */
	public DoubleSetting temperatureCreatureSickUp;
	/**
	 * A scalar that defines how much a creature's chance of getting sick decreases when they are cold. 
	 * Use low values to make temperature effect creature sick chance less, 
	 * use high values to make temperature effect creature sick chance more. 
	 */
	public DoubleSetting temperatureCreatureSickDown;
	/**
	 * The percentage of speed that is converted into sliding speed when sliding on ice
	 */
	public DoubleSetting temperatureIceSlideRate;
	/**
	 * The value that sliding speed is multiplied by while a creature is on ice
	 */
	public DoubleSetting temperatureIceSlideDecay;
	/**
	 * The maximum amount of fur a creature can have
	 */
	public DoubleSetting temperatureFurMax;
	/**
	 * The scalar that defines how fast fur mutations happen
	 */
	public DoubleSetting temperatureFurScalar;
	/**
	 * A scalar that defines how much fur warms up creatures
	 */
	public DoubleSetting temperatureFurWarmScalar;
	/**
	 * A scalar that defines how much fur increases creature size
	 */
	public DoubleSetting temperatureFurSizeScalar;
	/**
	 * The rate at which fur grows when a creature wants to grow their fur
	 */
	public DoubleSetting temperatureFurGrowthRate;
	/**
	 * The rate at which fur sheds when a creature wants to shed their fur
	 */
	public DoubleSetting temperatureFurShedRate;
	/**
	 * The number of minutes in a simulated year cycle
	 */
	public DoubleSetting yearLength;

	
	//food settings
	/**
	 * The minimum temperature a tile must be in order to grow food
	 */
	public DoubleSetting foodGrowTemperature;
	/**
	 * The base amount of food a tile gains in an update when growing
	 */
	public DoubleSetting foodGrowth;
	/**
	 * The base amount of food a tile looses in an update when decaying
	 */
	public DoubleSetting foodDecay;
	/**
	 * A scalar that defines how much extra food is grown when food grows. 
	 * Use low values to make temperature effect food growth less, 
	 * Use high values to make temperature effect food growth more, 
	 */
	public DoubleSetting foodGrowthScalar;
	/**
	 * A scalar that defines how much extra food is lost when food decays. 
	 * Use low values to make temperature effect food decay less, 
	 * Use high values to make temperature effect food decay more, 
	 */
	public DoubleSetting foodDecayScalar;
	
	
	//mutability settings
	/**
	 * The highest value mutability cane have, mutability can be negative or positive of this value
	 */
	public DoubleSetting mutabilityMax;
	/**
	 * This value is multiplied to the mutability given to a baby on a mutation for that gene
	 */
	public DoubleSetting mutabilityScalar;
	/**
	 * The maximum amount mutability can change 
	 */
	public DoubleSetting mutabilityChange;
	/**
	 * The scalar that defines how neural network mutations occur
	 */
	public DoubleSetting brainMutationScalar;
	/**
	 * The numbers of nodes in the hidden layers of creature's neural networks
	 */
	public IntArraySetting brainSize;
	/**
	 * The chance for a gene or NeuralNet value to completely get randomized
	 */
	public DoubleSetting randomizeChance;
	
	
	//fight settings
	/**
	 * The base amount of energy a creature loses when they perform an attack
	 */
	public DoubleSetting fightDrainEnergy;
	/**
	 * The base temperature a creature gains when they perform an attack
	 */
	public DoubleSetting fightGainHeat;
	/**
	 * The base amount of defense a creature has, increasing total defense as a rational function
	 */
	public DoubleSetting fightBaseDefense;
	/**
	 * The maximum amount of defense a creature can gain from their size, higher size gives higher defense, increasing total defense as a rational function
	 */
	public DoubleSetting fightSizeDefense;
	/**
	 * The maximum amount of defense a creature can gain from their fur, higher fur gives higher defense, increasing total defense as a rational function
	 */
	public DoubleSetting fightFurDefense;
	/**
	 * The base attack power of a creature
	 */
	public DoubleSetting fightBasePower;
	/**
	 * The maximum attack power gained from creature size
	 */
	public DoubleSetting fightSizePower;
	/**
	 * The maximum amount of attack power that is lost from an attack based on temperature. Temperature above comfort level reduces attack power, temperature below comfort level has no effect
	 */
	public DoubleSetting fightTemperatureReduction;
	/**
	 * The ratio of energy that a creature gains from landing an attack
	 */
	public DoubleSetting fightGainEnergyRatio;
	/**
	 * The ratio of energy that a tile gains from the lost energy from an attack
	 */
	public DoubleSetting fightGroundEnergyRatio;
	
	
	//video settings
	/**
	 * The maximum frame rate of the simulation
	 */
	public DoubleSetting maxFrameRate;
	/**
	 * The number of threads to run the updating on
	 */
	public IntSetting numThreads;
	/**
	 * True if the eyes of creatures should be drawn, false otherwise
	 */
	public BooleanSetting eyeShow;
	
	
	public Settings(){
		settings = new ArrayList<Setting<?>>();
		
		simGuiWidth = new IntSetting(1800, "Sim Gui Width",
				"The width of the content of the Simulation GUI.");
		settings.add(simGuiWidth);
		
		simGuiHeight = new IntSetting(950, "Sim Gui Height",
				"The height of the content of the Simulation GUI.");
		settings.add(simGuiHeight);
		
		populationGraphMaxPoints = new IntSetting(2000, "Pop. Graph Max",
				"The maximum number of points that the population graph will hold, use zero or a negative number for infinite points.");
		settings.add(populationGraphMaxPoints);
		
		populationGraphUpdateRate = new IntSetting(1000, "Pop. Graph Rate",
				"The number of 1/100 of a second between each time that a population data point is recorded.");
		settings.add(populationGraphUpdateRate);
		
		mutabilityGraphMaxPoints = new IntSetting(2000, "Mut. Graph Max",
				"The maximum number of points that the mutability graph will hold, use zero or a negative number for infinite points.");
		settings.add(mutabilityGraphMaxPoints);
		
		mutabilityGraphUpdateRate = new IntSetting(1000, "Mut. Graph Rate",
				"The number of 1/100 of a second between each time that a mutability data point is recorded.");
		settings.add(populationGraphUpdateRate);
		
		ageGraphMaxPoints = new IntSetting(2000, "Age Graph Max",
				"The maximum number of points that the age graph will hold, use zero or a negative number for infinite points.");
		settings.add(ageGraphMaxPoints);
		
		ageGraphUpdateRate = new IntSetting(1000, "Age Graph Rate",
				"The number of 1/100 of a second between each time that a age data point is recorded.");
		settings.add(ageGraphUpdateRate);
		
		asexualReproduction = new BooleanSetting(true, "Asexual reproduction",
				"True if creatures should be allowed to reproduce with just themselves, "
				+ "or false if they should require a partner to evolve. Even when this setting is true "
				+ "the creatures can still reproduce with a partner.");
		settings.add(asexualReproduction);
		
		initialCreatures = new IntSetting(300, "Initital Creature Count",
				"The number of initial creatures when a new simulation begins.");
		settings.add(initialCreatures);
		
		initialEnergy = new DoubleSetting(1000.0, "Initital Energy",
				"The energy a creature has when they are randomly generated");
		settings.add(initialEnergy);
		
		minimumCreatures = new IntSetting(200, "Minimum Creature Count",
				"The minimum number of creatures that will exist, if the population drops below this value, a new random creature is generated.");
		settings.add(minimumCreatures);
		

		creatureAngleChange = new DoubleSetting(0.1, "Creature Angle Change",
				"The maximum amount a creature can change their angle per tick, can be negative or positive of this value.");
		settings.add(creatureAngleChange);
		
		creatureSpeedChange = new DoubleSetting(0.2, "Creature Speed Change",
				"The maximum amount a creature can change their speed per tick, can be negative or positive of this value.");
		settings.add(creatureSpeedChange);
		
		creatureReverseRatio = new DoubleSetting(0.5, "Creature Reverse Ratio",
				"The percentage of creatureMaxSpeed that can be used while moving in reverse.");
		settings.add(creatureReverseRatio);
		
		creatureMaxSpeed = new DoubleSetting(7.0, "Creature Max Speed",
				"The maximum forward speed of a creature.");
		settings.add(creatureMaxSpeed);
		
		creatureEnergySpeedScalar = new DoubleSetting(0.02, "Creature Energy speed Scalar",
				"The amount of energy lost on movement is based on the size of the creature. "
				+ "Increasing this value makes energy lost higher and decreasing it makes energy lost lower");
		settings.add(creatureEnergySpeedScalar);
		
		creatureSizeSlowScalar = new DoubleSetting(1.2, "Creature Size Slow Scalar",
				"Value that defines how overall speed is reduced by size");
		settings.add(creatureSizeSlowScalar);

		creatureMinRadius = new DoubleSetting(6.0, "Creature Min Radius",
				"The minimum radius for a creature. " +
				"Larger size decreases movement and angle acceleration, max movement speed, and increases energy consumption.");
		settings.add(creatureMinRadius);

		creatureMinSize = new DoubleSetting(5.0, "Creature Min Radius Gene",
				"The minimum size radius for a creature's size gene. " +
				"Larger size decreases movement and angle acceleration, max movement speed, and increases energy consumption.");
		settings.add(creatureMinSize);
		
		creatureMaxSize = new DoubleSetting(20.0, "Creature Max Radius Gene",
				"The maximum size radius for a creature's size gene. " + 
				"Larger size decreases movement and angle acceleration, max movement speed, and increases energy consumption.");
		settings.add(creatureMaxSize);
		
		creatureSizeScalar = new DoubleSetting(5.0, "Creature Size Scalar",
				"The scalar that defines how fast the size gene changes");
		settings.add(creatureSizeScalar);
		
		creatureSizeAgeScalar = new DoubleSetting(5.0E-4, "Creature Size Age Scalar",
				"The ratio of age that is used for determining a creatures size based on age. " + 
				"Low values make maximum size reached later in life, " + 
				"High values make maximum size reached earlier in life.");
		settings.add(creatureSizeAgeScalar);
		
		creatureSizeMaxEnergyScalar = new DoubleSetting(4.0, "Creature Size Energy Scalar",
				"This value is multiplied by the size gene and added to get the maximum energy");
		settings.add(creatureSizeMaxEnergyScalar);

		creatureSpeciesGeneScalar = new DoubleSetting(0.01, "Creature Species Scalar",
				"The scalar that determines how fast the species gene mutates.");
		settings.add(creatureSpeciesGeneScalar);

		creatureBreedRange = new DoubleSetting(0.1, "Creature Breed Range",
				"The maximum space between species that creatures can be to still breed. "
				+ "Values close to 0 make breeding require very similar species. "
				+ "Use 0.5 to make breeding unrestricted from species. "
				+ "Do not use 0, it will make breeding practically impossible. "
				+ "This setting has no effect on asexual reproduction.");
		settings.add(creatureBreedRange);
		
		creatureEatCost = new DoubleSetting(11.0, "Creature Eat Cost",
				"The cost of eating each update");
		settings.add(creatureEatCost);
		
		creatureEnergyLoss = new DoubleSetting(0.4, "Creature Energy Loss",
				"The amount of energy lost per update by a creature");
		settings.add(creatureEnergyLoss);
		
		creatureMoveCost = new DoubleSetting(0.1, "Creature Move Cost",
				"The amount of energy lost while moving, moving faster removes more energy");
		settings.add(creatureMoveCost);
		
		creatureMaxEnergy = new DoubleSetting(20000.0, "Creature Max Eenrgy",
				"The maximum amount of energy a creature can have");
		settings.add(creatureMaxEnergy);
		
		creatureMaxEnergyScalar = new DoubleSetting(300.0, "Creature Max Energy Scalar",
				"This value is multiplied to the amount of a creature's maximum energy on a mutation for that gene");
		settings.add(creatureMaxEnergyScalar);
		
		creatureSickAmountScalar = new DoubleSetting(0.001, "Creature Sick Amount Scalar",
				"This scalar is multiplied by the number of seconds a creature has lived for to determine the amount of energy lost when getting sick");
		settings.add(creatureSickAmountScalar);

		creatureSickScalar = new DoubleSetting(1.0E7, "Creature Sick Scalar",
				"The scalar that controls the degree that age effects the chance of getting sick. " + 
				"60000 gives a 50% of getting sick at 10 minutes old. " + 
				"Low values make sickness more likely sooner in life. " + 
				"High values make sickness more likely later in life.");
		settings.add(creatureSickScalar);
		
		creatureMinBabyEnergy = new DoubleSetting(0.0, "Min Creature Baby Energy",
				"The minimum amount of energy that a creature can give to a baby.");
		settings.add(creatureMinBabyEnergy);
		
		creatureMaxBabyEnergy = new DoubleSetting(2000.0, "Max Creature Baby Energy",
				"The maximum amount of energy that a creature can give to a baby.");
		settings.add(creatureMaxBabyEnergy);
		
		creatureBabyEnergyAsexual = new DoubleSetting(10.0, "Asexual Energy Ratio",
				"This value is multiplied by the amount of energy needed to have a baby asexually. "
				+ "This value doesn't effect the amount of energy required for breeding with a partner.");
		settings.add(creatureBabyEnergyAsexual);
		
		creatureBabyEnergyScalar = new DoubleSetting(100.0, "Creature Baby Energy Scalar",
				"The base amount for mutations to the creature baby energy gene.");
		settings.add(creatureBabyEnergyScalar);

		creatureBabyCooldown = new IntSetting(200, "Creature Baby Cooldown",
				"The number of 1/100 of a second a creature must wait before it can have another baby");
		settings.add(creatureBabyCooldown);
		
		creatureBreedTime = new IntSetting(0, "Creature Breed Time",
				"The number of 1/100 of a second a creature must touch another creature in order to have a baby");
		settings.add(creatureBreedTime);
		
		
		eyeNumMin = new IntSetting(1, "Min Eyes",
				"The minimum number of eyes a creature has.");
		settings.add(eyeNumMin);
		
		eyeNumMax = new IntSetting(6, "Max Eyes",
				"The maximum number of eyes a creature has.");
		settings.add(eyeNumMax);

		eyeNumScalar = new DoubleSetting(0.2, "Eye Num Scalar",
				"The scalar that effects mutations for the number of eyes in a creature.");
		settings.add(eyeNumScalar);

		eyeDistanceMin = new DoubleSetting(0.0, "Min Eye Distance",
				"The minimum distance that an eye can see.");
		settings.add(eyeDistanceMin);

		eyeDistanceMax = new DoubleSetting(2000.0, "Max Eye Distance",
				"The maximum distance that an eye can see.");
		settings.add(eyeDistanceMax);

		eyeDistanceScalar = new DoubleSetting(70.0, "Eye Distance Scalar",
				"The scalar that effects eye distance mutations.");
		settings.add(eyeDistanceScalar);

		eyeAngleRange = new DoubleSetting(3.1415, "Eye Angle Range",
				"The angle distance, in radians, in both directions from where a creature is looking.");
		settings.add(eyeAngleRange);
		
		eyeAngleScalar = new DoubleSetting(0.05, "Eye Angle Scalar",
				"The scalar that effects eye angle mutations");
		settings.add(eyeAngleScalar);
		
		
		tileSize = new DoubleSetting(100.0, "Size",
				"The size of the tiles in both width and height.");
		settings.add(tileSize);
		
		tilesX = new IntSetting(100, 
				"Num Tiles X", "The number of tiles on the X axis.");
		settings.add(tilesX);
		
		tilesY = new IntSetting(100, 
				"Num Tiles Y", "The number of tiles on the Y axis.");
		settings.add(tilesY);
		
		tileSpeciesScalar = new DoubleSetting(0.1, "Species Scalar",
				"The scalar that determines how fast the deaths of creatures change the species (color) of tiles. "
				+ "Set to 0 to disable creature deaths changing tile species, set to low or high values for low or "
				+ "highchanges in species amount, respectively.");
		settings.add(tileSpeciesScalar);

		tileWaterDamage = new DoubleSetting(50.0, "Water Damage",
				"The amount of energy creatures lose each 1/100 of a second while on water");
		settings.add(tileWaterDamage);
		
		foodMax = new DoubleSetting(6000.0, "Max Food",
				"The maximum amount of food in a tile.");
		settings.add(foodMax);
		
		foodEatPercent = new DoubleSetting(0.1, "Eating Percent",
				"The percentage of the food in a tile that is eaten when a tile is eaten.");
		settings.add(foodEatPercent);
		
		
		worldSeed = new IntSetting(0, "Seed",
				"The number that determines the world the creatures live in. "
				+ "When all world settings are the same and with the same seed, the same world is generated each time. "
				+ "Use seed 0 to generate a random seed when settings are updated.");
		settings.add(worldSeed);

		worldShapeWeight1 = new DoubleSetting(2.0, "Shape Weight 1",
				"The weight of the first kind of shape in the world generation.");
		settings.add(worldShapeWeight1);

		worldShapeWeight2 = new DoubleSetting(2.0, "Shape Weight 2",
				"The weight of the second kind of shape in the world generation.");
		settings.add(worldShapeWeight2);
		
		worldShapeWeight3 = new DoubleSetting(2.0, "Shape Weight 3",
				"The weight of the third kind of shape in the world generation.");
		settings.add(worldShapeWeight3);
		
		worldShapeWeight4 = new DoubleSetting(0.02, "Shape Weight 4",
				"The weight of the fourth kind of shape in the world generation.");
		settings.add(worldShapeWeight4);

		worldShapeWeight5 = new DoubleSetting(0.02, "Shape Weight 5",
				"The weight of the fifth kind of shape in the world generation.");
		settings.add(worldShapeWeight5);
		
		worldShapeWeight6 = new DoubleSetting(0.02, "Shape Weight 6",
				"The weight of the sixth kind of shape in the world generation.");
		settings.add(worldShapeWeight6);

		worldShapeScalar1 = new DoubleSetting(0.005, "Shape Scalar 1",
				"The main scalar of the first kind of shape in the world generation.");
		settings.add(worldShapeScalar1);
		
		worldShapeScalar2 = new DoubleSetting(0.005, "Shape Scalar 2",
				"The main scalar of the second kind of shape in the world generation.");
		settings.add(worldShapeScalar2);

		worldShapeScalar3 = new DoubleSetting(0.00005, "Shape Scalar 3",
				"The main scalar of the third kind of shape in the world generation.");
		settings.add(worldShapeScalar3);
		
		worldShapeScalar4 = new DoubleSetting(0.005, "Shape Scalar 4",
				"The main scalar of the fourth kind of shape in the world generation.");
		settings.add(worldShapeScalar4);
		
		worldShapeScalar5 = new DoubleSetting(0.005, "Shape Scalar 5",
				"The main scalar of the fifth kind of shape in the world generation.");
		settings.add(worldShapeScalar5);

		worldShapeScalar6 = new DoubleSetting(0.00005, "Shape Scalar 6",
				"The main scalar of the sixth kind of shape in the world generation.");
		settings.add(worldShapeScalar6);

		worldShapePercent1 = new DoubleSetting(0.99, "Shape Percent 1",
				"The percentage of an offset that is used for the main scalar of the first shape.");
		settings.add(worldShapePercent1);
		
		worldShapePercent2 = new DoubleSetting(0.99, "Shape Percent 2",
				"The percentage of an offset that is used for the main scalar of the second shape.");
		settings.add(worldShapePercent2);
		
		worldShapePercent3 = new DoubleSetting(0.99, "Shape Percent 3",
				"The percentage of an offset that is used for the main scalar of the third shape.");
		settings.add(worldShapePercent3);

		worldShapePercent4 = new DoubleSetting(0.99, "Shape Percent 4",
				"The percentage of an offset that is used for the main scalar of the fourth shape.");
		settings.add(worldShapePercent4);
		
		worldShapePercent5 = new DoubleSetting(0.99, "Shape Percent 5",
				"The percentage of an offset that is used for the main scalar of the fifth shape.");
		settings.add(worldShapePercent5);
		
		worldShapePercent6 = new DoubleSetting(0.99, "Shape Percent 6",
				"The percentage of an offset that is used for the main scalar of the sixth shape.");
		settings.add(worldShapePercent6);

		worldShapeOffset1 = new DoubleSetting(0.05, "Shape Offset 1",
				"The scalar that determines how much each tile will be offset by it's random amount, for the first shape.");
		settings.add(worldShapeOffset1);

		worldShapeOffset2 = new DoubleSetting(0.05, "Shape Offset 2",
				"The scalar that determines how much each tile will be offset by it's random amount, for the second shape.");
		settings.add(worldShapeOffset2);

		worldShapeOffset3 = new DoubleSetting(0.05, "Shape Offset 3",
				"The scalar that determines how much each tile will be offset by it's random amount, for the third shape.");
		settings.add(worldShapeOffset3);

		worldShapeOffset4 = new DoubleSetting(0.05, "Shape Offset 4",
				"The scalar that determines how much each tile will be offset by it's random amount, for the fourth shape.");
		settings.add(worldShapeOffset4);

		worldShapeOffset5 = new DoubleSetting(0.05, "Shape Offset 5",
				"The scalar that determines how much each tile will be offset by it's random amount, for the fifth shape.");
		settings.add(worldShapeOffset5);

		worldShapeOffset6 = new DoubleSetting(0.05, "Shape Offset 6",
				"The scalar that determines how much each tile will be offset by it's random amount, for the sixth shape.");
		settings.add(worldShapeOffset6);

		worldScalar = new DoubleSetting(1.0, "Main Scalar",
				"The overall scalar for how quickly the world changes.");
		settings.add(worldScalar);
		
		worldOffset = new DoubleSetting(0.0, "Main Offset",
				"The starting point for determining color of species.");
		settings.add(worldOffset);
		
		worldXPos = new DoubleSetting(0.0, "X Pos",
				"The overall scalar for the position of the world on the x axis.");
		settings.add(worldXPos);
		
		worldXScalar = new DoubleSetting(1.0, "X Scalar",
				"The overall scalar for the scale of the world on the x axis.");
		settings.add(worldXScalar);
		
		worldYPos = new DoubleSetting(0.0, "Y Pos",
				"The overall scalar for the position of the world on the y axis.");
		settings.add(worldYPos);
		
		worldYScalar = new DoubleSetting(1.0, "Y Scalar",
				"The overall scalar for the scale of the world on the y axis.");
		settings.add(worldYScalar);
		
		worldRiverCount = new IntSetting(2, "River Count",
				"The number of rivers that attempt to generate in each chunk.");
		settings.add(worldRiverCount);

		worldRiverChance = new DoubleSetting(0.25, "River Chance",
				"The chance for each river in a chunk to generate.");
		settings.add(worldRiverChance);

		worldRiverBorderSize = new IntSetting(3, "River Range",
				"The number of chunks around a chunk that a river can generate into.");
		settings.add(worldRiverBorderSize);

		worldRiverMinSize = new DoubleSetting(150.0, "River Min size",
				"The approximate minimum radius of a river.");
		settings.add(worldRiverMinSize);
		
		worldRiverMaxSize = new DoubleSetting(500.0, "River Max size",
				"The approximate maximum radius of a river.");
		settings.add(worldRiverMaxSize);
		
		worldRiverNoise = new DoubleSetting(0.3, "River Noise",
				"The percentage of a river that is noise. "
				+ "Use values close to 0 for smooth curves, "
				+ "use values close to 1 for lots of noise, "
				+ "and beyond 1 for maximum noise");
		settings.add(worldRiverNoise);
		
		worldRiverNoiseScalar = new DoubleSetting(1.2, "River Noise Scalar",
				"The value that determines how important distance is for river noise. "
				+ "Use low positive values for distance to be not very important. "
				+ "Use high positive values for distance to be very important.");
		settings.add(worldRiverNoiseScalar);
		
		worldIslandCount = new IntSetting(20, "Island Count",
				"The number of land masses that can generate in a single chunk.");
		settings.add(worldIslandCount);
		
		worldIslandChance = new DoubleSetting(0.3, "Island Chance",
				"The chance for each island in a chunk to generate.");
		settings.add(worldIslandChance);

		worldIslandMinSize = new DoubleSetting(0.0, "Min Island Size",
				"The minimum radius of an island.");
		settings.add(worldIslandMinSize);

		worldIslandMaxSize = new DoubleSetting(1000.0, "Min Island Size",
				"The maximum radius of an island.");
		settings.add(worldIslandMaxSize);

		worldIslandBorderSize = new IntSetting(5, "Island Range",
				"The number of chunks around a chunk that an island can generate into.");
		settings.add(worldIslandBorderSize);
		
		worldIslandNoise = new DoubleSetting(0.4, "Island Noise",
				"The percentage of an island that is noise. Use values close to 0 for smooth curves, " + 
				"use values close to 1 for lots of noise, and beyond 1 for maximum noise");
		settings.add(worldIslandNoise);
		
		worldIslandNoiseScalar = new DoubleSetting(1.2, "Island Noise Scalar",
				"The value that determines how important distance is for island noise. "
				+ "Use low positive values for distance to be not very important. "
				+ "Use high positive values for distance to be very important.");
		settings.add(worldIslandNoiseScalar);
		
		worldChunkSize = new IntSetting(20, "Chunk Size",
				"The number of tiles in a chunk.");
		settings.add(worldChunkSize);
		
		
		temperatureMin = new DoubleSetting(10.0, "Min Temperature",
				"The minimum base temperature, in celcius, that can exist in the simulation. "
				+ "This is the temperature of tiles at the north most point of the simulation at the midpoint between summer and winter.");
		settings.add(temperatureMin);
		
		temperatureMax = new DoubleSetting(50.0, "Max Temperature",
				"The maximum base temperature, in celcius, that can exist in the simulation. "
				+ "This is the temperature of tiles at the south most point of the simulation at the midpoint between summer and winter.");
		settings.add(temperatureMax);
		
		temperatureCreatureRate = new DoubleSetting(0.1, "Creature Temperature Rate",
				"The rate at which creatures change their temperature based on the temperature of the tile they are standing on.");
		settings.add(temperatureCreatureRate);
		
		temperatureTileRate = new DoubleSetting(0.1, "Tile Temperature Rate",
				"The rate at which tiles change their temperature based on the temperature of the simulation.");
		settings.add(temperatureTileRate);
		
		temperatureWorldRange = new DoubleSetting(40.0, "Temperature Range",
				"The range of temperatures from the center of the simulation. " + 
				"The north most point can reach this temperature below min temperature. " + 
				"The south most point can reach this temperature above max temperature.");
		settings.add(temperatureWorldRange);
		
		temperatureCreatureComfort = new DoubleSetting(40.0, "Creature Comfort",
				"The temperature that creatures are most comfortable at. "
				+ "The further a creature's temperature is from this value, the more energy they lose. "
				+ "Creatures with temperatures higher than this value have an increased chance of getting sick, and move faster."
				+ "Creatures with temperatures lower than this value have a reduced chance of getting sick, and move slower.");
		settings.add(temperatureCreatureComfort);
		
		temperatureCreatureScalar = new DoubleSetting(0.04, "Creature Loss Scalar",
				"A scalar that defines how much energy creatures lose from temperature.");
		settings.add(temperatureCreatureScalar);
		
		temperatureCreatureSlow = new DoubleSetting(0.005, "Creature Slow",
				"A scalar that defines how much creatures are slowed when they are too cold. "
				+ "Use low values to make temperature effect creature speed less, "
				+ "use high values to make temperature effect creature speed more.");
		settings.add(temperatureCreatureSlow);
		
		temperatureCreatureSpeed = new DoubleSetting(0.005, "Creature Speed Up",
				"A scalar that defines how much creatures are sped up when they are hot. "
				+ "Use low values to make temperature effect creature speed less, "
				+ "use high values to make temperature effect creature speed more.");
		settings.add(temperatureCreatureSpeed);
		
		temperatureCreatureSickUp = new DoubleSetting(0.1, "Creature Sick Up",
				"A scalar that defines how much a creature's chance of getting sick increases when they are hot. "
				+ "Use low values to make temperature effect creature sick chance less, "
				+ "Use high values to make temperature effect creature sick chance more");
		settings.add(temperatureCreatureSickUp);
		
		temperatureCreatureSickDown = new DoubleSetting(0.1, "Creature Sick Down",
				"A scalar that defines how much a creature's chance of getting sick decreases when they are cold. "
				+ "Use low values to make temperature effect creature sick chance less, u"
				+ "use high values to make temperature effect creature sick chance more");
		settings.add(temperatureCreatureSickDown);

		temperatureIceSlideRate = new DoubleSetting(1.5, "Ice Slide Rate",
				"The percentage of speed that is converted into sliding speed when sliding on ice.");
		settings.add(temperatureIceSlideRate);

		temperatureIceSlideDecay = new DoubleSetting(0.995, "Ice Slide Rate",
				"The value that sliding speed is multiplied by while a creature is on ice.");
		settings.add(temperatureIceSlideDecay);

		temperatureFurMax = new DoubleSetting(100.0, "Max Fur",
				"The maximum amount of fur a creature can have.");
		settings.add(temperatureFurMax);

		temperatureFurScalar = new DoubleSetting(2.0, "Fur Scalar",
				"The scalar that defines how fast fur mutations happen.");
		settings.add(temperatureFurScalar);

		temperatureFurWarmScalar = new DoubleSetting(0.5, "Fur Warm Scalar",
				"A scalar that defines how much fur warms up creatures.");
		settings.add(temperatureFurWarmScalar);

		temperatureFurSizeScalar = new DoubleSetting(0.25, "Fur Size Scalar",
				"A scalar that defines how much fur increases creature size.");
		settings.add(temperatureFurSizeScalar);
		
		temperatureFurGrowthRate = new DoubleSetting(0.1, "Fur Growth Rate",
				"The rate at which fur grows when a creature wants to grow their fur.");
		settings.add(temperatureFurGrowthRate);
		
		temperatureFurShedRate = new DoubleSetting(0.1, "Fur Shed Rate",
				"The rate at which fur sheds when a creature wants to shed their fur.");
		settings.add(temperatureFurShedRate);
		
		yearLength = new DoubleSetting(60.0, "Year Length",
				"The number of minutes in a simulated year cycle. 1/4th of this is the duration of each season.");
		settings.add(yearLength);
		
		
		foodGrowTemperature = new DoubleSetting(-5.0, "Growing Temperature",
				"The minimum temperature a tile must be in order to grow food. "
				+ "Food tiles with a lower temperature than this value will decay, "
				+ "and food tiles with a higher temperature than this value will grow. "
				+ "The rate of decay and growth is effected by how far the food is from "
				+ "this value.");
		settings.add(foodGrowTemperature);
		
		foodGrowth = new DoubleSetting(1.5, "Growth Rate",
				"The base amount of food a tiles gains each 1/100 second at a growing temperature.");
		settings.add(foodGrowth);
		
		foodDecay = new DoubleSetting(0.0, "Decay Rate",
				"The base amount of food a tiles loses each 1/100 second when below growing temperature.");
		settings.add(foodDecay);
		
		foodGrowthScalar = new DoubleSetting(5.0E-4, "Growth Scalar",
				"A scalar that defines how much extra food is grown when food grows. "
				+ "Use low values to make temperature effect food growth less, "
				+ "Use high values to make temperature effect food growth more.");
		settings.add(foodGrowthScalar);
		
		foodDecayScalar = new DoubleSetting(0.0, "Decay Scalar",
				 "A scalar that defines how much extra food is lost when food decays. "
				 + "Use low values to make temperature effect food decay less, "
				 + "Use high values to make temperature effect food decay more.");
		settings.add(foodDecayScalar);
		
		
		mutabilityMax = new DoubleSetting(4.0, "Max Mutability",
				"The highest value mutability can be. Mutability effects the rate at which weights, biases, and genes change.");
		settings.add(mutabilityMax);
		
		mutabilityScalar = new DoubleSetting(1.0, "Mutability Scalar",
				"This value is multiplied to the mutability given to a baby on a mutation for that gene");
		settings.add(mutabilityScalar);
		
		mutabilityChange = new DoubleSetting(0.01, "Mutability Change",
				"The rate at whicih mutability changes");
		settings.add(mutabilityChange);
		
		brainMutationScalar = new DoubleSetting(0.3, "Brain Mutation Scalar",
				"The scalar that defines how neural network mutations occur");
		settings.add(brainMutationScalar);
		
		brainSize = new IntArraySetting(new Integer[]{30}, "Brain Size",
				"The numbers of nodes in the hidden layers of creature's neural networks");
		settings.add(brainSize);
		
		randomizeChance = new DoubleSetting(0.003, "Randomize Chance",
				"The chance for a gene or neural net value to completely get randomized");
		settings.add(randomizeChance);
		
		
		fightDrainEnergy = new DoubleSetting(20.0, "Lose Energy",
				"The base amount of energy a creature loses when they perform an attack.");
		settings.add(fightDrainEnergy);
		
		fightGainHeat = new DoubleSetting(0.1, "Temperature Gain",
				"The base temperature a creature gains when they perform an attack.");
		settings.add(fightGainHeat);
		
		fightBaseDefense = new DoubleSetting(50.0, "Base Defense",
				"The base amount of defense a creature has, increasing total defense as a rational function.");
		settings.add(fightBaseDefense);
		
		fightSizeDefense = new DoubleSetting(20.0, "Size Defense",
				"The maximum amount of defense a creature can gain from their size, "
				+ "higher size gives higher defense, "
				+ "increasing total defense as a rational function.");
		settings.add(fightSizeDefense);
		
		fightFurDefense = new DoubleSetting(0.0, "Fur Defense",
				"The maximum amount of defense a creature can gain from their size, "
				+ "higher size gives higher defense, "
				+ "increasing total defense as a rational function.");
		settings.add(fightFurDefense);

		fightBasePower = new DoubleSetting(800.0, "Base Power",
				"The base attack power of a creature.");
		settings.add(fightBasePower);
		
		fightSizePower = new DoubleSetting(500.0, "Size Power",
				"The maximum attack power gained from creature size.");
		settings.add(fightSizePower);
		
		fightTemperatureReduction = new DoubleSetting(100.0, "Temperature Power",
				"The maximum amount of attack power that is lost from an attack based on temperature. "
				+ "Temperature above comfort level reduces attack power, "
				+ "temperature below comfort level has no effect");
		settings.add(fightTemperatureReduction);
		
		fightGainEnergyRatio = new DoubleSetting(1.0, "Gain Energy Ratio",
				"The ratio of energy that a creature gains from landing an attack.");
		settings.add(fightGainEnergyRatio);
		
		fightGroundEnergyRatio = new DoubleSetting(1.0, "Ground Energy Ratio",
				"The ratio of energy that a tile gains from the lost energy from an attack.");
		settings.add(fightGroundEnergyRatio);
		
		
		maxFrameRate = new DoubleSetting(100.0, "Max Frame Rate",
				"The maximum frame rate of the simulation, set to 0 for unlimited frame rate.");
		settings.add(maxFrameRate);

		numThreads = new IntSetting(2, "Num Threads",
				"The number of threads to run the updating on. Higher thread count uses more CPU power and runs faster when running as "
				+ "fast as possible. Use 0 to not use multithreading for updates.");
		settings.add(numThreads);
		
		eyeShow = new BooleanSetting(false, "Show Eyes",
				"True if the eyes of all creatures should be drawn, false otherwise. Enabling this can cause significant framerate lag.");
		settings.add(eyeShow);
	}
	
	/**
	 * Get a list of all the settings
	 * @return the list
	 */
	public ArrayList<Setting<?>> getSettings(){
		return settings;
	}
	
	/**
	 * Load the default setting for every setting
	 */
	public void loadDefaultSettings(){
		for(Setting<?> s : settings) s.loadDefaultValue();
	}
	
	/**
	 * Save this Settings object to the given PrintWriter
	 * @param write the PrintWriter that will save the Settings
	 */
	@Override
	public boolean save(PrintWriter write) throws NoSuchElementException{
		boolean success = true;
		for(Setting<?> s : settings){
			success &= s.save(write);
		}
		return success;
	}
	
	/**
	 * Loads the Settings in from the given Scanner object
	 * @param read the Scanner that will load the settings
	 */
	@Override
	public boolean load(Scanner read) throws NoSuchElementException{
		boolean success = true;
		for(Setting<?> s : settings){
			success &= s.load(read);
		}
		return success;
	}
	
}
