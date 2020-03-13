package evolve.sim.obj;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.Scanner;

import evolve.Main;
import evolve.gui.component.SimConstants;
import evolve.sim.Simulation;
import evolve.sim.misc.CreatureGlow;
import evolve.sim.misc.Species;
import evolve.sim.misc.Temperature;
import evolve.sim.obj.tile.FoodTile;
import evolve.sim.obj.tile.Tile;
import evolve.util.Camera;
import evolve.util.math.vector.AngleVector;
import evolve.util.math.vector.Vector;

/**
 * An object that exists in the simulation and can interact with it
 */
public abstract class Creature implements SimObject{
	
	public static final int FIGHT_DISPLAY_TIME = 10;
	
	/**
	 * The Simulation that this creature is a part of
	 */
	private Simulation simulation;
	
	/**
	 * The numerical id of this creature
	 */
	private long id;

	/**
	 * The numerical id of this creature's father
	 */
	private long fatherId;
	/**
	 * The numerical id of this creature's mother
	 */
	private long motherId;
	
	/**
	 * The angle, in radians, that this Creature is facing
	 */
	private double angle;
	
	/**
	 * The current speed of this creature as they move, ignoring other modifiers, can be positive or negative
	 */
	private double speed;
	
	/**
	 * The energy that this creature has remaining to use. If this value hits 0, the creature dies
	 */
	private double energy;
	
	/**
	 * The number of updates this creature has lived for
	 */
	private long age;
	
	/**
	 * The number of generations this creature is on, 0 if they are initially born
	 */
	private int generation;
	
	/**
	 * The number of children this creature has had with partners
	 */
	private int breedChildren;
	
	/**
	 * The number of children this creature has had asexualy
	 */
	private int asexualChildren;
	
	/**
	 * The x coordinate of the center of this creature
	 */
	private double x;
	
	/**
	 * The y coordinate of the center of this creature
	 */
	private double y;
	
	/**
	 * The current radius of this creature
	 */
	private double radius;
	
	/**
	 * true if this Creature's energy has dropped below 0, after this happens, they no longer can gain energy or move
	 */
	private boolean dead;
	
	/**
	 * The amount of time left to display a visual showing that this {@link Creature} recently attacked a creature.<br>
	 * This variable is explicitly used for rendering and should not be used for any kind of logic or test
	 */
	private int fightDisplayTimer;

	/**
	 * The amount of time left to display a visual showing that this {@link Creature} was recently attacked by a creature.<br>
	 * This variable is explicitly used for rendering and should not be used for any kind of logic or test
	 */
	private int hitDisplayTimer;
	
	/**
	 * true if this creature should display as eating after the next update, false otherwise
	 */
	private boolean shouldEat;

	/**
	 * True if this creature has eaten in the last frame, false otherwise.<br>
	 * This variable is explicitly used for rendering and should not be used for any kind of logic or test
	 */
	private boolean eating;

	/**
	 * true if this creature should display as vomiting after the next update, false otherwise
	 */
	private boolean shouldVomit;
	/**
	 * True if this creature has vomited in the last frame, false otherwise.<br>
	 * This variable is explicitly used for rendering and should not be used for any kind of logic or test
	 */
	private boolean vomiting;

	
	/**
	 * The amount of sickness this creature lost the last time they got sick, in range [0, 1], used for rendering
	 */
	private double sickAmount;
	
	/**
	 * The amount of updates that the sick overlay should be drawn for 
	 */
	private int sickTimer;
	
	/**
	 * The amount of updates until the {@link Creature} can attack again
	 */
	private int attackCooldown;
	
	/**
	 * The glow effect this creature currently has
	 */
	private CreatureGlow glow;
	
	/**
	 * The species of this {@link Creature}
	 */
	private Species species;
	
	/**
	 * The current {@link Temperature} of this {@link Creature}
	 */
	private Temperature temperature;
	
	/**
	 * The last {@link Vector} that this creature was moved by from a tile
	 */
	private Vector tileMovedVector;
	
	/**
	 * Create an empty creature
	 */
	public Creature(Simulation simulation){
		temperature = new Temperature(simulation.getTemperatureValue());
		this.species = new Species();
		
		this.simulation = simulation;
		this.id = this.simulation.nextCreatureId();
		
		this.angle = 0;
		this.speed = 0;
		this.energy = Main.SETTINGS.initialEnergy.value();
		this.x = 0;
		this.y = 0;
		
		this.eating = false;
		this.shouldEat = false;
		this.vomiting = false;
		this.shouldVomit = false;
		this.fightDisplayTimer = 0;
		this.hitDisplayTimer = 0;
		
		this.dead = false;
		
		this.glow = null;
		
		this.age = 0;
		this.generation = 0;
		this.breedChildren = 0;
		this.asexualChildren = 0;
		
		this.sickAmount = 0;
		this.sickTimer = 0;
		
		this.attackCooldown = 0;
		
		this.tileMovedVector = null;
	}
	
	@Override
	public void cacheData(){
		radius = calculateRadius();
	}
	
	/**
	 * Called when this Creature is updated during a game update tick
	 */
	@Override
	public void update(){
		Tile tileOn = getSimulation().getContainingTile(getX(), getY());
		if(tileOn != null){
			Temperature t = new Temperature(tileOn.getTemperature().getTemp());
			t.setTemp(t.getTemp() + getFur() * Main.SETTINGS.temperatureFurWarmScalar.value());
			temperature.approach(t, Main.SETTINGS.temperatureCreatureRate.value());
		}
		
		double size = Main.SETTINGS.tileSize.value();
		
		//move creature based on it's current speed and angle
		if(!dead){
			double speed = getTotalSpeed();
			double xMove = Math.cos(getAngle()) * speed;
			double yMove = -Math.sin(getAngle()) * speed;
			setX(getX() + xMove);
			setY(getY() + yMove);
			
			//remove energy based on how far the creature moved. Energy removed is based on the distance moved raised to a power.
			//energy loss is also based on creature size
			double moveLoss =
					//start with the cost of moving
					-Main.SETTINGS.creatureMoveCost.value() * 
					//multiply it by the distance moved raised to a power
					Math.pow(Math.sqrt(Math.pow(xMove, 2) + Math.pow(yMove, 2)),
					//that power is based on a setting and also the size of the creature
					Main.SETTINGS.creatureEnergySpeedScalar.value() * getRadius());
			
			//if the creature is moving backwards, the cost of moving is doubled
			if(speed < 0) moveLoss *= 2;
			
			//add the energy lost
			addEnergy(moveLoss);
			
			//move creature based on the tile it's on
			if(tileOn != null) tileMovedVector = tileOn.getMovement(this);
			else tileMovedVector = null;
			
			if(tileMovedVector != null){
				setX(getX() + tileMovedVector.getX());
				setY(getY() - tileMovedVector.getY());
			}
		}
		
		//keep x and y in a hard coded range
		Tile[][] grid = simulation.getGrid();
		double maxW = grid.length * size - 1;
		double maxH = grid[0].length * size - 1;
		if(getX() < 0) setX(0);
		else if(getX() > maxW) setX(maxW);
		if(getY() < 0) setY(0);
		else if(getY() > maxH) setY(maxH);
		
		//update this only if they are not dead
		if(!dead){
			//remove energy that this creature spent existing
			addEnergy(-Main.SETTINGS.creatureEnergyLoss.value());
			
			//remove energy based on their distance from their preferred temperature
			double tempDiff = getTemperature().getTemp() - Main.SETTINGS.temperatureCreatureComfort.value();
			addEnergy(-Math.abs(tempDiff) * Main.SETTINGS.temperatureCreatureScalar.value());
			
			//chance to remove energy out of randomness from getting sick
			double sickScalar = Main.SETTINGS.creatureSickScalar.value();
			//increase sick sickScalar based on temperature
			if(tempDiff < 0) sickScalar *= 1 / (-tempDiff * Main.SETTINGS.temperatureCreatureSickDown.value() + 1);
			if(tempDiff > 0) sickScalar *= Math.log(tempDiff * Main.SETTINGS.temperatureCreatureSickDown.value() + 1) + 1;
			
			double sickChance = sickScalar / (sickScalar + Math.abs(getAge()));
			
			//if they get sick this update
			if(Math.random() > sickChance){
				//the amount of energy lost from getting sick is based on the sick amount scalar, times the age in seconds, times a random amount
				double sickAdd = -Main.SETTINGS.creatureSickAmountScalar.value() * (getAge() / 100.0d) * Math.random();
				addEnergy(sickAdd);
				
				//set the sick amount for rendering
				sickAmount = (-1 / (.8 * -sickAdd + 1) + 1);
				//show sickness for half a second
				sickTimer = 50;
			}
			
			//add age
			age++;
			
			//if the sickTimer is counting down, decrease it
			if(sickTimer > 0) sickTimer--;
			
			//update eating state
			if(!shouldEat) eating = false;
			//update vomiting state
			if(!shouldVomit) vomiting = false;
			
			//decrease fight and hit timers
			if(fightDisplayTimer > 0) fightDisplayTimer--;
			if(hitDisplayTimer > 0) hitDisplayTimer--;
			if(attackCooldown > 0) attackCooldown--;
		}
	}
	
	/**
	 * Draw this creature with the given Camera
	 * @param cam the camera object
	 */
	@Override
	public void render(Camera cam){
		//draw glow effect first
		if(glow != null) glow.render(cam);
		
		Graphics g = cam.getG();
		
		//variables for rendering
		double size = getRadius();
		double x = this.x - size;
		double y = this.y - size;
		double s = size * 2;
		double eyeW = 8;
		double eyeR = size / 2;
		double outline = 1 + size * .4 * Math.max(0, Math.min(1, getFur() / Main.SETTINGS.temperatureFurMax.value()));
		Color speciesC = getSpecies().getColor((float)(.5 + .5 * (1 - sickAmount)), (float)(.6 + getTemperature().getBrightnessRange() * .3));
		
		//outline
		g.setColor(speciesC.darker().darker());
		cam.fillOval(x, y, s, s);
		
		//fill
		g.setColor(speciesC);
		cam.fillOval(x + outline, y + outline, s - outline * 2, s - outline * 2);
		g.setColor(getTemperature().getOverlayColor());
		cam.fillOval(x + outline, y + outline, s - outline * 2, s - outline * 2);
		
		
		//eye, shows facing direction

		//draw vomit state under the eye
		if(vomiting){
			g.setColor(new Color(175, 124, 73));
			double w = eyeW * 2;
			cam.fillOval(
					this.x + Math.cos(this.angle) * eyeR - w / 2,
					this.y - Math.sin(this.angle) * eyeR - w / 2,
					w, w
			);
		}
		//the eye shouldn't be drawn if the creature is vomiting
		else{
			//if the creature is eating, make the eye bigger and a different color
			if(eating){
				eyeW *= 2;
				g.setColor(new Color(100, 100, 100));
			}
			//otherwise the eye is black
			else g.setColor(Color.BLACK);
			cam.fillOval(
					this.x + Math.cos(this.angle) * eyeR - eyeW / 2,
					this.y - Math.sin(this.angle) * eyeR - eyeW / 2,
					eyeW, eyeW
			);
		}
		
		//draw effect for recently taking a hit
		if(hitDisplayTimer > 0){
			g.setColor(new Color(255, 0, 0, 100));
			cam.fillOval(x, y, s, s);
		}
		
		//draw effect for recently attacking
		if(fightDisplayTimer > 0){
			g.setColor(new Color(0, 0, 0, 100));
			cam.fillOval(x + s / 4, y + s / 4, s / 2, s / 2);
		}
		
		//amount of energy
		g.setColor(Color.BLACK);
		g.setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, 20));
		cam.drawScaleString("" + (int)getEnergy(), getX() - size * .5, getY());
	}
	
	public CreatureGlow getGlow(){
		return glow;
	}
	public void setGlow(CreatureGlow glow){
		this.glow = glow;
	}
	
	/**
	 * Set the glow of this Creature to a glow with a color based on this Creature's color and the given radius
	 * @param radius
	 */
	public void createGlow(double radius){
		setGlow(new CreatureGlow(this, new Color(0, 0, 255, 127), radius));
	}
	
	/**
	 * Kill this creature. This is automatically called when this creature runs out of energy
	 */
	public void kill(){
		dead = true;
		
		//modify the species of the tile this creature is on, based on the species of this creature
		Tile t = getSimulation().getContainingTile(getX(), getY());
		if(t != null){
			//get the two species
			Species tSpecies = t.getSpecies();
			Species cSpecies = getSpecies();
			
			tSpecies.approachSpecies(cSpecies, Main.SETTINGS.tileSpeciesScalar.value());
		}
	}
	
	/**
	 * Find and attack the nearest creature to this {@link Creature}.<br>
	 * This method call is what costs energy.<br>
	 * If no creature is touching this creature, or this creature is unable to attack, then no creature is attacked.
	 * @return true if a creature was attacked, false otherwise
	 */
	public boolean attackNearestCreature(){
		if(attackCooldown > 0) return false;
		
		double attackCost = Main.SETTINGS.fightDrainEnergy.value();
		//remove the energy it costs to attack
		addEnergy(-attackCost);
		fightDisplayTimer = FIGHT_DISPLAY_TIME;
		//if this killed the creature, do not continue
		if(isDead()) return false;
		
		//attack the nearest creature
		Creature c = getSimulation().getNearestCreature(getX(), getY(), this);
		if(c == null) return false;
		
		if(touching(c)) attack(c);
		
		attackCooldown = FIGHT_DISPLAY_TIME;
		return true;
	}

	/**
	 * Attack the given {@link NeuralNetCreature}.
	 * @param c the creature to attack
	 */
	public void attack(Creature c){
		c.takeHit(this);
		//add temperature from attacking
		getTemperature().addTemp(Main.SETTINGS.fightGainHeat.value());
	}
	
	/**
	 * Take a hit from the given {@link NeuralNetCreature}<br>
	 * This method will handle damaging the given creature, giving energy to this creature, and applying energy around this creature.
	 * @param c the creature that is attacking this creature
	 * @return the damage taken
	 */
	public double takeHit(Creature c){
		double maxSize = Main.SETTINGS.creatureMaxSize.value() + Main.SETTINGS.creatureMinRadius.value();
		double cSizeRatio = c.getRadius() / maxSize;
		double selfSizeRatio = getRadius() / maxSize;
		double selfFurRatio = getFur() / Main.SETTINGS.temperatureFurMax.value();
		
		//variables for the amount of defense this creature has against getting hit
		double sizeReduction = Main.SETTINGS.fightSizeDefense.value() * selfSizeRatio;
		double furReduction = Main.SETTINGS.fightFurDefense.value() * selfFurRatio;
		double reductionBonus = Main.SETTINGS.fightBaseDefense.value() + sizeReduction + furReduction;
		
		//the damage gained based on the size of the creature
		double cSizeBonus = Main.SETTINGS.fightSizePower.value() * cSizeRatio;
		//the amount of energy this attack will remove as a base
		double attackPower = Main.SETTINGS.fightBasePower.value() + cSizeBonus;
		
		//reduce attack power by an amount based on temperature, if the attacker's temperature is above comfort temperature
		double tempDiff = c.getTemperature().getTemp() - Main.SETTINGS.temperatureCreatureComfort.value();
		if(tempDiff > 0){
			double maxDiff = Main.SETTINGS.temperatureMax.value() - Main.SETTINGS.temperatureCreatureComfort.value();
			attackPower -= Main.SETTINGS.fightTemperatureReduction.value() * tempDiff / maxDiff;
		}
		
		//multiply the attack power by a random amount to determine how much the attack does
		attackPower *= Math.random();
		
		//multiply attack power by an amount based on this creatures defense
		attackPower *= 1 - (100.0 / (100.0 + reductionBonus));
		
		//ensure attack power is at least 0
		attackPower = Math.max(0, attackPower);
		
		double energy = getEnergy();
		double damage;
		if(energy <= attackPower){
			damage = energy;
			kill();
		}
		else damage = attackPower;
		
		//determine the random percentage of energy that is gained from the damage dealt
		double lossRatio = Main.SETTINGS.fightGainEnergyRatio.value();//percentage of energy gained when landing an attack
		double groundRatio = Main.SETTINGS.fightGroundEnergyRatio.value();//percentage of energy gained by an attack when an attack hits a nearby creature
		double removeRatio = Math.random();
		double gainEnergy = damage * removeRatio * lossRatio;
		
		//add some of the energy that was lost in the fight to the tile the attacked creature is standing on
		//TODO may want to find a way to do this without using instanceof
		Tile t = getSimulation().getContainingTile(getX(), getY());
		if(t instanceof FoodTile){
			FoodTile food = (FoodTile)t;
			food.addFood((damage - gainEnergy) * Math.random() * groundRatio);
		}
		
		c.addEnergy(gainEnergy);
		addEnergy(-damage);
		hitDisplayTimer = FIGHT_DISPLAY_TIME;
		
		return damage;
	}
	
	/**
	 * Eat from the tile directly underneath this creature
	 */
	public void eat(){
		//the creature cannot eat while vomiting
		if(dead || vomiting) return;
		
		eating = true;
		Tile t = getSimulation().getContainingTile(getX(), getY());
		if(t != null){
			addEnergy(t.eat(this));
			addEnergy(-Main.SETTINGS.creatureEatCost.value());
		}
	}
	
	/**
	 * Remove energy from the creature.<br>
	 * If the creature is touching another creature, then some of that removed energy is transfered to the nearest creature.<br>
	 * If the creature isn't touching another creature, then the energy is given to the tile below the creature, 
	 * also slightly modifying the species of the tile towards the creature.<br>
	 * If the creature cannot give energy to anything, then the energy is simply lost
	 */
	public void vomit(){
		
		vomiting = true;
		
		//determine the amount of energy to vomit
		double vomit = 20;//TODO make this a setting
		
		//determine the amount of energy to give
		double energy = vomit * .6;//TODO make this a setting, it should change based on vomit, but should have a random amount removed based on a setting
		
		//if this creature is touching another, then give them the energy
		NeuralNetCreature creature = getSimulation().getNearestCreature(getX(), getY(), this);
		if(creature != null && creature.touching(this)){
			creature.addEnergy(energy);
		}
		else{
			//if this creature is on a tile, then give that tile the energy and modify the species
			Tile t = getSimulation().getContainingTile(getX(), getY());
			if(t != null){
				//add the energy from the vomit
				t.giveEnergy(energy);
				
				//modify the species
				t.getSpecies().approachSpecies(getSpecies(), .00005);//TODO make this a setting
			}
		}
		
		//remove the vomited energy from the creature
		addEnergy(-energy);
	}
	
	public Simulation getSimulation(){
		return simulation;
	}
	public void setSimulation(Simulation simulation){
		this.simulation = simulation;
	}
	
	/**
	 * Get the {@link Vector} from when this creature last was moved by a tile
	 * @return the vector
	 */
	public Vector getTileMovedVector(){
		return tileMovedVector;
	}
	
	public double getAngle(){
		return angle;
	}
	/**
	 * Sets the angle and keeps the angle in the range [0, 2Pi)
	 * @param angle
	 */
	public void setAngle(double angle){
		if(dead) return;
		this.angle = angle;
		while(this.angle < 0) this.angle += Math.PI * 2;
		while(this.angle >= Math.PI * 2) this.angle -= Math.PI * 2;
	}
	/**
	 * Add an amount to the angle. 
	 * @param angle the angle to add. If this value exceeds a normal turning angle, the added angle is put in the normal range
	 */
	public void addAngle(double angle){
		double angleChange = Main.SETTINGS.creatureAngleChange.value();
		
		if(angle > 0) angle = Math.min(angle, angleChange);
		else angle = Math.max(angle, -angleChange);
		
		this.setAngle(this.angle + angle);
	}

	/**
	 * Get the total speed of this creature, including all modifiers
	 * @return the speed
	 */
	public double getTotalSpeed(){
		//determine speed based on temperature
		double tempDiff = getTemperature().getTemp() - Main.SETTINGS.temperatureCreatureComfort.value();
		double tempScalar = 1;
		//add speed
		if(tempDiff > 0){
			tempDiff *= Main.SETTINGS.temperatureCreatureSpeed.value();
			tempScalar = 1 + Math.log(tempDiff + 1);
		}
		else if(tempDiff < 0){
			tempDiff *= Main.SETTINGS.temperatureCreatureSlow.value();
			tempScalar = 1 / (1 - tempDiff);
		}
		
		return getMoveSpeed() * tempScalar;
	}
	
	/**
	 * Get the total speed of this creature, without modifiers
	 * @return the speed
	 */
	public double getMoveSpeed(){
		return speed;
	}
	
	/**
	 * Set the speed to the given value and ensure that the speed stays in the normal range
	 * @param speed the speed to set to
	 */
	public void setSpeed(double speed){
		if(dead) return;
		
		double maxSpeed = getMaxSpeed();
		
		this.speed = speed;
		if(this.speed > 0) this.speed = Math.min(maxSpeed, this.speed);
		else this.speed = Math.max(maxSpeed / -2, this.speed);
	}
	
	/**
	 * Get the maximum value of speed for this creature
	 * @return
	 */
	public double getMaxSpeed(){
		return Main.SETTINGS.creatureMaxSpeed.value();
	}
	
	/**
	 * Add an amount of speed to the current speed and ensure that the speed stays in the normal range. 
	 * @param speed the speed to add. If this value exceeds the normal range for changing speed, it's automatically adjusted
	 */
	public void addSpeed(double speed){
		double speedChange = Main.SETTINGS.creatureSpeedChange.value();
		
		if(speed > 0) speed = Math.min(speed, speedChange);
		else speed = Math.max(speed, -speedChange);
		
		this.setSpeed(this.speed + speed);
	}

	public double getEnergy(){
		return energy;
	}
	/**
	 * Set the current energy, if energy hits zero, the creature is killed
	 * @param energy the energy to set
	 */
	public void setEnergy(double energy){
		if(dead) return;
		this.energy = energy;
		if(this.energy <= 0) kill();
	}
	/**
	 * Add an amount of energy to the energy, can be negative or positive to remove or add energy respectively. If energy hits zero, the creature is killed
	 * @param energy the energy to add or remove
	 */
	public void addEnergy(double energy){
		this.setEnergy(this.energy + energy);
	}

	/**
	 * true if this creature should display as eating after the next update, false otherwise
	 * @return
	 */
	public boolean shouldEat() {
		return shouldEat;
	}
	public void setShouldEat(boolean shouldEat){
		this.shouldEat = shouldEat;
	}
	
	/**
	 * true if this creature should display as vomiting after the next update, false otherwise
	 * @return
	 */
	public boolean shouldVomit() {
		return shouldVomit;
	}
	public void setShouldVomit(boolean shouldVomit){
		this.shouldVomit = shouldVomit;
	}
	
	public boolean isDead(){
		return dead;
	}
	/**
	 * Make this creature not dead any more, will also need to give them energy afterwards, otherwise they will die on the next update
	 */
	public void revive(){
		dead = false;
	}
	
	/**
	 * Get the number of updates this creature has existed for
	 * @return
	 */
	public long getAge(){
		return age;
	}
	public void setAge(long age){
		this.age = age;
	}
	
	/**
	 * Get the generation of this creature
	 * @return
	 */
	public int getGeneration(){
		return generation;
	}
	public void setGeneration(int generation){
		this.generation = generation;
	}
	/**
	 * Get the number of children this creature has had with a partner
	 * @return the number of children
	 */
	public int getBreedChildren(){
		return breedChildren;
	}
	public void setBreedChildren(int breedChildren){
		this.breedChildren = breedChildren;
	}

	/**
	 * Get the number of children this creature has made asexually
	 * @return the number of children
	 */
	public int getAsexualChildren(){
		return asexualChildren;
	}
	public void setAsexualChildren(int asexualChildren){
		this.asexualChildren = asexualChildren;
	}
	
	/**
	 * Get the total number of children this creature has has
	 * @return the total number of children
	 */
	public int getTotalChildren(){
		return getBreedChildren() + getAsexualChildren();
	}
	
	public double getX(){
		return x;
	}
	public void setX(double x){
		this.x = x;
	}

	public double getY(){
		return y;
	}
	public void setY(double y){
		this.y = y;
	}
	
	/**
	 * Get the center of this creature
	 * @return
	 */
	public Point2D.Double getPos(){
		return new Point2D.Double(getX(), getY());
	}
	
	/**
	 * Get the bounds of this Creature as an ellipse
	 * @return
	 */
	public Ellipse2D.Double getBounds(){
		double r = getRadius();
		return new Ellipse2D.Double(getX() - r, getY() - r, r * 2, r * 2);
	}
	
	/**
	 * determine if the given creature is touching this creature
	 * @param c the creature to test
	 * @return true if the two creatures are touching, false otherwise
	 */
	public boolean touching(Creature c){
		return c.getRadius() + getRadius() > c.getPos().distance(getPos());
	}
	
	/**
	 * Calculate and return the current radius of this Creature
	 * @return the radius
	 */
	public abstract double calculateRadius();
	
	/**
	 * Get the amount of fur this {@link Creature} has
	 * @return the amount of fur
	 */
	public abstract double getFur();
	
	public double getRadius(){
		return radius;
	}
	
	/**
	 * Get the species of this {@link Creature}
	 * @return the species
	 */
	public Species getSpecies(){
		return species;
	}
	/**
	 * Set the species of this creature to the given species
	 * @param species the species to set
	 */
	public void setSpecies(Species species){
		this.species = species;
	}
	
	/**
	 * Get the temperature object of this Creature
	 * @return the temperature
	 */
	public Temperature getTemperature(){
		return temperature;
	}
	
	public long getId(){
		return id;
	}
	public void setId(long id){
		this.id = id;
	}

	public long getFatherId(){
		return fatherId;
	}
	public void setFatherId(long fatherId){
		this.fatherId = fatherId;
	}

	public long getMotherId(){
		return motherId;
	}
	public void setMotherId(long motherId){
		this.motherId = motherId;
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
		write.println(angle + " " + speed + " " + energy + " " + x + " " + y + " " +
				age + " " + generation + " " + breedChildren + " " + asexualChildren + " " +
				id + " " + fatherId + " " + motherId);
		
		temperature.save(write);
		
		if(tileMovedVector != null){
			write.println("no");
			tileMovedVector.save(write);
		}
		else write.println("null");
		
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean load(Scanner read){
		try{
			setAngle(read.nextDouble());
			setSpeed(read.nextDouble());
			setEnergy(read.nextDouble());
			setX(read.nextDouble());
			setY(read.nextDouble());
			setAge(read.nextLong());
			setGeneration(read.nextInt());
			setBreedChildren(read.nextInt());
			setAsexualChildren(read.nextInt());
			setId(read.nextLong());
			setFatherId(read.nextLong());
			setMotherId(read.nextLong());
			
			temperature = new Temperature();
			temperature.load(read);
			
			String next = read.next();
			if(!next.equals("null")){
				tileMovedVector = new AngleVector(0, 0);
				tileMovedVector.load(read);
			}
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Get a string representation of the given time.<br>
	 * The string is the largest unit that exists of the time, the rest is rounded off.<br>
	 * For example, if the given time was for 1 year and 330 days, then the string "1 year" would be returned
	 * @param time the time to describe
	 * @return the description
	 */
	public static String getAproxTimeAmount(long time){
		long[] times = getAgeTime(time);
		
		String s = "";
		boolean plural = false;
		if(times[0] > 0){
			s = times[0] + " year";
			plural = times[0] != 1l;
		}
		else if(times[1] > 0){
			s = times[1] + " day";
			plural = times[1] != 1l;
		}
		else if(times[2] > 0){
			s = times[2] + " hour";
			plural = times[2] != 1l;
		}
		else if(times[3] > 0){
			s = times[3] + " minute";
			plural = times[3] != 1l;
		}
		else{
			String decimal = times[5] + "";
			if(decimal.length() == 1) decimal += "0";
			s = times[4] + "." + decimal + " second";
			plural = true;
		}
	
		if(plural) s += "s";
		
		return s;
	}
	
	/**
	 * Get a string representing the given amount of time.<br>
	 * An example return value: "1 year, 267 days, 1 hour, 2 minutes, and 32.29 seconds"<br>
	 * The string will always have the correct plural for values that are not 1
	 * @param time the time, in 1/100 of a second
	 * @return the describing string
	 */
	public static String getTimeString(long time){
		long[] times = getAgeTime(time);
		String s = "";
		
		s += times[0] + " year";
		if(times[0] != 1) s += "s";
		s += ", ";
		
		s += times[1] + " day";
		if(times[1] != 1) s += "s";
		s += ", ";
		
		s += times[2] + " hour";
		if(times[2] != 1) s += "s";
		s += ", ";
		
		s += times[3] + " minute";
		if(times[3] != 1) s += "s";
		s += ", ";
		
		s += times[4] + ".";
		if(times[5] <= 9) s += "0";
		s += times[5];
		s += " seconds";
		
		return s;
	}
	
	/**
	 * Get an array representing how long this age is.<br>
	 * arr[0] = number of years (a year is always 365 days)<br>
	 * arr[1] = number of days (24 hours)<br>
	 * arr[2] = number of hours (60 minutes)<br>
	 * arr[3] = number of minutes (60 seconds)<br>
	 * arr[4] = number of seconds (100 updates)<br>
	 * arr[5] = number of updates (1/100 of a second)<br>
	 * @param age the age to convert
	 * @return the array
	 */
	public static long[] getAgeTime(long age){
		//0 = update, 1 = second, 2 = minute, 3 = hour, 4 = day, 5 = year
		long[] times = new long[6];
		times[0] = 1;
		times[1] = 100 * times[0];
		times[2] = 60 * times[1];
		times[3] = 60 * times[2];
		times[4] = 24 * times[3];
		times[5] = 365 * times[4];
		
		long[] time = new long[6];
		
		for(int i = 0; i < time.length; i++){
			long duration = times[times.length - 1 - i];
			time[i] = (int)(age / duration);
			age %= duration;
		}
		
		return time;
	}
	
}

