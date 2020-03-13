package evolve.sim;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

import evolve.Main;
import evolve.sim.misc.CreatureGlow;
import evolve.sim.misc.Temperature;
import evolve.sim.obj.Creature;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.UserCreature;
import evolve.sim.obj.tile.FoodTile;
import evolve.sim.obj.tile.Tile;
import evolve.sim.obj.tile.WaterTile;
import evolve.sim.world.World;
import evolve.util.Camera;
import evolve.util.Saveable;

/**
 * An object that keeps track of all the objects and calculations for a simulation
 */
public class Simulation implements Saveable{
	/**
	 * True if a user creature should be added, false otherwise
	 */
	public static final boolean ADD_USER_CREATURE = false;
	
	/**
	 * The tiles used in this simulation. The indexes of the tiles directly correlate to their positions in the world
	 */
	private Tile[][] grid;
	
	/**
	 * The total number of updates this Simulation has had
	 */
	private long totalTime;
	
	/**
	 * The camera that represents the location of this simulation
	 */
	private Camera camera;
	
	/**
	 * Keeps track of if the camera should lock onto the UserCreature. 
	 * false to not lock the camera, true to lock the camera
	 */
	private boolean lockCamera;
	
	/**
	 * A creature object that the user is controlling
	 */
	private UserCreature userCreature;
	
	/**
	 * The current temperature of this simulation
	 */
	private Temperature temperature;
	
	/**
	 * A list of all the creatures that the simulation has
	 */
	private ArrayList<NeuralNetCreature> evolvingCreatures;
	
	/**
	 * The creature that has a glow that indicates that it is selected, null if none are selected
	 */
	private NeuralNetCreature selectedGlowCreature;
	/**
	 * The creature that has a glow that indicates that it is being hovered over, null if none are hovered
	 */
	private NeuralNetCreature selectedHoverCreature;
	
	/**
	 * The creatures that will be added to the evolvingCreatures list in the next update
	 */
	private ArrayList<NeuralNetCreature> toAddCreatures;
	
	/**
	 * The data object used by {@link Graph} for population history
	 */
	private ArrayList<Double[]> populationData;
	
	/**
	 * The data object used by {@link Graph} for mutability history, which has highest, lowest, and average mutability
	 */
	private ArrayList<Double[]> mutabilityData;
	
	/**
	 * The data object used by {@link Graph} for age history, which has the oldest creature, and the average creature age
	 */
	private ArrayList<Double[]> ageData;
	
	/**
	 * The next id for a creature made in this simulation
	 */
	private long nextCreatureId;
	
	/**
	 * Create an empty simulation object
	 */
	public Simulation(){
		Tile[][] grid = new Tile[Main.SETTINGS.tilesX.value()][Main.SETTINGS.tilesY.value()];
		
		temperature = new Temperature();
		updateTemperature();
		
		nextCreatureId = 0;
		
		double tSize = Main.SETTINGS.tileSize.value();
		
		//initialize grid and other World objects
		World world = new World(Main.SETTINGS.worldSeed.value());
		world.generate(this);
		
		//initialize the time to 0
		totalTime = 0;
		
		//create user object
		if(ADD_USER_CREATURE){
			reviveUserCreature();
		}
		else userCreature = null;
		
		//create evolving creatures
		evolvingCreatures = new ArrayList<NeuralNetCreature>();
		toAddCreatures = new ArrayList<NeuralNetCreature>();
		int num = Main.SETTINGS.initialCreatures.value();
		for(int i = 0; i < num; i++){
			NeuralNetCreature c = new NeuralNetCreature(this);
			c.setX(Math.random() * tSize * grid.length);
			c.setY(Math.random() * tSize * grid[0].length);
			evolvingCreatures.add(c);
		}
		
		//set up camera
		lockCamera = true;
		camera = new Camera(Main.SETTINGS.simGuiWidth.value(), Main.SETTINGS.simGuiHeight.value());
		
		selectedGlowCreature = null;
		selectedHoverCreature = null;
		
		//initialize data
		populationData = new ArrayList<Double[]>();
		mutabilityData = new ArrayList<Double[]>();
		ageData = new ArrayList<Double[]>();
	}
	
	/**
	 * Get the next creature id and increment it to the next id
	 * @return
	 */
	public long nextCreatureId(){
		return nextCreatureId++;
	}
	
	public Camera getCamera(){
		return camera;
	}
	public void setCamera(Camera camera){
		this.camera = camera;
	}
	public boolean getLockCamera(){
		return lockCamera;
	}
	public void setLockCamera(boolean lockCamera){
		this.lockCamera = lockCamera;
	}
	
	public UserCreature getUserCreature(){
		return userCreature;
	}
	public void setUserCreature(UserCreature userCreature){
		this.userCreature = userCreature;
	}
	/**
	 * Bring the user creature back to life, if they are dead or null
	 */
	public void reviveUserCreature(){
		boolean isNull = userCreature == null;
		if(!isNull && userCreature.isDead()) {
			userCreature.revive();
		}
		else if(isNull){
			double tSize = Main.SETTINGS.tileSize.value();
			userCreature = new UserCreature(this);
			userCreature.setX(Math.random() * tSize * grid.length);
			userCreature.setY(Math.random() * tSize * grid[0].length);
		}
	}
	
	/**
	 * Add the given creature to the list of evolving creatures.<br>
	 * Will only be added after the next update
	 * @param creature the creature
	 */
	public void addCreature(NeuralNetCreature creature){
		toAddCreatures.add(creature);
	}
	
	/**
	 * Set the given creature to be glowing and make all other creatures not glowing
	 * @param selected the creature to glow
	 * @param radius the given radius
	 */
	public void setSelectedCreatureGlow(NeuralNetCreature selected){
		if(selected == null){
			selectedGlowCreature = null;
		}
		else{
			if(selectedHoverCreature != null) selectedHoverCreature.setGlow(null);
			selectedHoverCreature = null;
			if(selectedGlowCreature != null) selectedGlowCreature.setGlow(null);
			selectedGlowCreature = selected;
			selectedGlowCreature.createGlow(10);
		}
	}

	/**
	 * Set the glow of the given creature as the creature being hovered over.<br>
	 * This displays which creature will be selected if the user selects a creature to view more information<br>
	 * If hover is null, no creature will have a hover glow<br>
	 */
	public void setHoveredCreature(NeuralNetCreature hover){
		//if hover is null, then the selected hover should have it's glow removed
		if(hover == null){
			//if the selected hover is null, nothing should happen
			if(selectedHoverCreature != null){
				//if the selected glow is nothing, or if the two selections are not equal, then the glow should be removed
				if(selectedGlowCreature == null || !selectedGlowCreature.equals(selectedHoverCreature)){
					selectedHoverCreature.setGlow(null);
					selectedHoverCreature = null;
				}
			}
		}
		//otherwise, the new hover glow should be set
		else{
			//only set the hover glow if the current hover glow is not the same as the selected glow
			boolean selectedNull = selectedGlowCreature == null;
			boolean hoverNull = selectedHoverCreature == null;
			if(hoverNull && (selectedNull || (!selectedNull && !hover.equals(selectedGlowCreature))) ||
			  !hoverNull && (selectedNull || !selectedGlowCreature.equals(selectedHoverCreature) && !selectedGlowCreature.equals(hover))){
				
				if(selectedHoverCreature != null) selectedHoverCreature.setGlow(null);
				selectedHoverCreature = hover;
				selectedHoverCreature.setGlow(new CreatureGlow(hover, new Color(255, 0, 255, 80), 20));
			}
		}
	}
	
	/**
	 * Remove the glow effect from every creature
	 */
	public void removeGlow(){
		for(NeuralNetCreature c : evolvingCreatures) c.setGlow(null);
	}
	
	/**
	 * Get the creature that is currently the glowing selected creature
	 * @return
	 */
	public NeuralNetCreature getSelectedGlowCreature(){
		return selectedGlowCreature;
	}
	
	/**
	 * Get the first NeuralNet creature in the list of creatures
	 * @return the creature, null if no creatures exist
	 */
	public NeuralNetCreature getEvolvingCreature(){
		if(evolvingCreatures.size() <= 0) return null;
		else return evolvingCreatures.get(0);
	}
	
	/**
	 * Get the list of creatures used in this simulation that are evolving.<br>
	 * Be careful using this method, can run into concurrent modification error.<br>
	 * Should wait to use the list until an associated clock is finished updating and rendering the creatures.
	 * @return the list of creatures
	 */
	public ArrayList<NeuralNetCreature> getEvolvingCreatures(){
		return evolvingCreatures;
	}
	
	/**
	 * Get the number of evolving creatures currently in the simulation
	 * @return
	 */
	public int getPopulation(){
		return evolvingCreatures.size();
	}

	/**
	 * Get the data used by a {@link Graph} object for population
	 * @return
	 */
	public ArrayList<Double[]> getPopulationData(){
		return populationData;
	}
	public void setPopulationData(ArrayList<Double[]> data){
		this.populationData = data;
	}
	
	/**
	 * Get the data used by a {@link Graph} object for mutability
	 * @return
	 */
	public ArrayList<Double[]> getMutabilityData(){
		return mutabilityData;
	}
	public void setMutabilityData(ArrayList<Double[]> mutabilityData){
		this.mutabilityData = mutabilityData;
	}

	/**
	 * Get the data used by a {@link Graph} object for age
	 * @return
	 */
	public ArrayList<Double[]> getAgeData(){
		return ageData;
	}
	public void setAgeData(ArrayList<Double[]> ageData){
		this.ageData = ageData;
	}
	
	/**
	 * Find the nearest creature to the given location. 
	 * @param x the x location
	 * @param y the y coordinate
	 * @return the nearest creature, null if no creature is found
	 */
	public NeuralNetCreature getNearestCreature(double x, double y){
		return getNearestCreature(x, y, null);
	}

	/**
	 * Find the nearest creature to the given location. 
	 * @param x the x location
	 * @param y the y coordinate
	 * @param creature a creature that should be ignored, if this creature is the nearest creature, then the search will continue.<br>
	 * 			If creature is the only matching creature, null is returned.<br>
	 * 			Use null to ignore this parameter
	 * @return the nearest creature, null if no creature is found
	 */
	public NeuralNetCreature getNearestCreature(double x, double y, Creature creature){
		ArrayList<NeuralNetCreature> list = new ArrayList<NeuralNetCreature>();
		list.addAll(getEvolvingCreatures());
		return getNearestCreature(list, x, y, creature);
	}
	
	/**
	 * Find the nearest evolving creature to the given NeuralNetCreature, not including the given creature.<br>
	 * This method only looks at the tiles which touch the given creature
	 * @param creature the creature to look from
	 * @return the nearest creature, null if no creature is found
	 */
	public NeuralNetCreature getNearestCreature(NeuralNetCreature creature){
		//if the given creature is null, then no search can occur
		if(creature == null) return null;
		
		//create a HashSet that stores all the unique creatures, this is to have a O(1) time contains method
		HashSet<NeuralNetCreature> creatureSet = new HashSet<NeuralNetCreature>();
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.addAll(creature.getContainingTiles());
		
		//for all the tiles the given creature is in, map an Integer index to each unique creature
		for(Tile t : tiles){
			ArrayList<NeuralNetCreature> containing = new ArrayList<NeuralNetCreature>();
			containing.addAll(t.getContainingCreatures());
			for(NeuralNetCreature c : containing){
				if(!creatureSet.contains(c)){
					creatureSet.add(c);
				}
			}
		}
		
		//now look for the creature
		return getNearestCreature(creatureSet, creature.getX(), creature.getY(), creature);
	}
	
	/**
	 * Find the nearest {@link NeuralNetCreature} in the given {@link Collection} of {@link NeuralNetCreature} objects to the given 
	 * coordinates, that does not match the given {@link Creature}.<br>
	 * @param col the collection of {@link NeuralNetCreature} objects to look through 
	 * @param x the x coordinate to look from
	 * @param y the y coordinate to look from
	 * @param creature the {@link Creature} to ignore, use null to ignore no creatures
	 * @return the nearest creature from the given {@link Collection} to the given {@link NeuralNetCreature}, null if no creature is found
	 */
	public static NeuralNetCreature getNearestCreature(Collection<NeuralNetCreature> col, double x, double y, Creature creature){
		NeuralNetCreature near = null;
		double dist = -1;
		for(NeuralNetCreature c : col){
			if(c != null && (creature == null || !c.equals(creature))){
				double newDist = c.getPos().distance(x, y);
				if(dist == -1 || dist > newDist){
					dist = newDist;
					near = c;
				}
			}
		}
		return near;
	}
	
	public Tile[][] getGrid(){
		return grid;
	}
	
	/**
	 * Set the grid size to the given size.<br>
	 * If the new size is bigger, then new default tiles are created.<br>
	 * If the new size is smaller, then it removes tiles from the bottom left corner<br>
	 * If the size is the same, no changes are made
	 * @param width the new width
	 * @param height the new height
	 */
	public void setGridSize(int width, int height){
		World world = new World(Main.SETTINGS.worldSeed.value());
		
		Tile[][] newGrid = new Tile[width][height];
		for(int i = 0; i < newGrid.length; i++){
			for(int j = 0; j < newGrid[i].length; j++){
				//if a tile exists in the current grid, copy it
				if(i < grid.length && j < grid[i].length) newGrid[i][j] = grid[i][j];
				//otherwise make a new tile
				else newGrid[i][j] = world.generateTile(i, j, this);
			}
		}
		grid = newGrid;
	}
	
	/**
	 * Set the grid of this {@link Simulation} directly
	 * @param grid
	 */
	public void setGrid(Tile[][] grid){
		this.grid = grid;
	}
	
	/**
	 * Get the amount of updates this Simulation has ran for
	 * @return the time, in 1/100 seconds
	 */
	public long getTotalTime(){
		return totalTime;
	}
	
	/**
	 * Set the amount of time this Simulation has ran for
	 * @param time the time, in 1/100 seconds
	 */
	public void setTotalTime(long time){
		this.totalTime = time;
	}
	
	/**
	 * Get the current value of the temperature in this simulation
	 * @return the value of the current temperature
	 */
	public double getTemperatureValue(){
		return temperature.getTemp();
	}
	
	/**
	 * Get the object keeping track of this simulations temperature.<br>
	 * This is the temperature at the center of the world, use getWorldTemperature(y) 
	 * 	to get the temperature at a particular tile position
	 * @return the temperature object
	 */
	public Temperature getTemperature(){
		return temperature;
	}
	
	/**
	 * Get the temperature of the simulation at a specific tile index
	 * @param y the tile, 0 represents the highest point on the simulation, higher values move down.<br>
	 * 			If y is above the current size of this simulation, then the highest index in the simulation is used, 
	 * 			and if y is below the current size of this simulation, then the lowest index in the simulation is used.
	 * @return a temperature object based on the tile index
	 */
	public Temperature getWorldTemperature(int y){
		Temperature t = new Temperature(getTemperatureValue());
		
		int size = grid[0].length - 1;
		int index = Math.max(0, Math.min(size, y));
		
		double range = Main.SETTINGS.temperatureWorldRange.value();
		double change =  range * (((double)index / size) - .5) * 2;
		
		t.setTemp(t.getTemp() + change);
		
		return t;
	}
	
	/**
	 * Get the tile that contains the given coordinates
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the found tile, null if no tile is found
	 */
	public Tile getContainingTile(double x, double y){
		double size = Main.SETTINGS.tileSize.value();
		int i = (int)(x / size);
		int j = (int)(y / size);
		if(i < 0 || i >= grid.length || j < 0 || j >= grid[i].length){
			return null;
		}
		else return grid[i][j];
	}
	
	/**
	 * Update the current temperature of this simulation based on the amount of time that has passed
	 */
	public void updateTemperature(){
		double maxTemp = Main.SETTINGS.temperatureMax.value();
		double minTemp = Main.SETTINGS.temperatureMin.value();
		double tempRange = maxTemp - minTemp;
		double rate = 2 * Math.PI / (Main.SETTINGS.yearLength.value() * 6000.0);
		temperature.setTemp(
				tempRange * ((Math.cos(getTotalTime() * rate) + 1) / 2) + minTemp
		);
	}
	
	/**
	 * Advances the simulation by one tick, does not use threads at all
	 */
	public void updateAll(){
		update();
		updateObjects();
	}
	
	/**
	 * Updates the state of the simulation by one frame, this does not update any of the {@link Tile} or {@link Creature} objects, only the main state
	 */
	public void update(){
		//update the temperature
		updateTemperature();
		
		//update user creature
		if(userCreature != null){
			if(userCreature.isDead()){
				userCreature = null;
			}
			else{
				userCreature.cacheData();
				userCreature.update();
				//adjust camera to user creature position
				if(lockCamera) camera.center(userCreature.getX(), userCreature.getY());
			}
		}
		
		//add to add creatures
		evolvingCreatures.addAll(toAddCreatures);
		toAddCreatures.clear();
		
		//remove the dead and null creatures
		for(int i = 0; i < evolvingCreatures.size(); i++){
			NeuralNetCreature c = evolvingCreatures.get(i);
			if(c == null || c.isDead()){
				evolvingCreatures.remove(c);
				i--;
			}
		}
		
		//if the population is below the minimum, add more creatures
		double tSize = Main.SETTINGS.tileSize.value();
		
		while(evolvingCreatures.size() < Main.SETTINGS.minimumCreatures.value()){
			NeuralNetCreature c = new NeuralNetCreature(this);
			c.setX(Math.random() * tSize * grid.length);
			c.setY(Math.random() * tSize * grid[0].length);
			c.cacheData();
			evolvingCreatures.add(c);
		}
		
		//update the data used for graphs
		updateData();
		
		//increase the total time
		totalTime++;
	}
	
	/**
	 * Update all the objects of this {@link Simulation} without using threads.<br>
	 * This method only updates objects, no other data
	 */
	public void updateObjects(){
		 //update tiles and cache their data
		updateTiles(0, grid.length);
		
		//cache data for all creatures before updating them
		cacheCreatures(0, evolvingCreatures.size());
		
		//make all creatures think after caching them and before updating them
		thinkCreatures(0, evolvingCreatures.size());
		
		//update evolvingCreatures
		updateCreatures(0, evolvingCreatures.size());
	}
	
	/**
	 * Update all rows of tiles in the given range, used for multithreading.<br>
	 * To update all normal object information, call updateObjects()
	 * @param low the low bound of the index to use
	 * @param high the high bound of the index to use
	 */
	public void updateTiles(int low, int high){
		for(int i = low; i < high && i < grid.length; i++){
			for(Tile t : grid[i]){
				t.cacheData();
				t.update();
			}
		}
	}
	
	/**
	 * Cache the data in all of the creatures in the given range, used for multithreading.<br>
	 * To update all normal object information, call updateObjects()
	 * @param low the low bound of the index to use
	 * @param high the high bound of the index to use
	 */
	public void cacheCreatures(int low, int high){
		for(int i = low; i < high && i < evolvingCreatures.size(); i++){
			evolvingCreatures.get(i).cacheData();
		}
	}
	
	/**
	 * Make the creatures think for those in the given range, used for multithreading.<br>
	 * To update all normal object information, call updateObjects()
	 * @param low the low bound of the index to use
	 * @param high the high bound of the index to use
	 */
	public void thinkCreatures(int low, int high){
		for(int i = low; i < high && i < evolvingCreatures.size(); i++){
			evolvingCreatures.get(i).think();
		}
	}
	
	/**
	 * Update all creatures in the given range, used for multithreading.<br>
	 * To update all normal object information, call updateObjects()
	 * @param low the low bound of the index to use
	 * @param high the high bound of the index to use
	 */
	public void updateCreatures(int low, int high){
		for(int i = low; i < high && i < evolvingCreatures.size(); i++){
			NeuralNetCreature c = evolvingCreatures.get(i);
			c.update();
		}
	}
	
	/**
	 * Adds one more set of data to the lists of data in this {@link Simulation}
	 */
	public void updateData(){
		//add population data to data list
		if(totalTime % Main.SETTINGS.populationGraphUpdateRate.value() == 0){
			populationData.add(new Double[]{(double)getPopulation()});
			int maxPop = Main.SETTINGS.populationGraphMaxPoints.value();
			while(maxPop > 0 && populationData.size() > maxPop && populationData.size() > 0) populationData.remove(0);
		}

		//add mutability data
		if(totalTime % Main.SETTINGS.mutabilityGraphUpdateRate.value() == 0){
			int low = 0;
			int high = 0;
			double total = 0;
			for(int i = 0; i < evolvingCreatures.size(); i++){
				//get mutability for relevant indexes
				double m = evolvingCreatures.get(i).getMutability();
				double mLow = evolvingCreatures.get(low).getMutability();
				double mHigh = evolvingCreatures.get(high).getMutability();
				
				//add to total mutability
				total += m;
				
				//check for if the ith mutability should be the highest or lowest
				if(mLow > m) low = i;
				if(mHigh < m) high = i;
			}
			
			//determine the values for the data
			double lowData = 0;
			double highData = 0;
			double averageData = 0;
			if(evolvingCreatures.size() > 0){
				lowData = evolvingCreatures.get(low).getMutability();
				highData = evolvingCreatures.get(high).getMutability();
				averageData = total / evolvingCreatures.size();
			}
			
			//add the actual data
			mutabilityData.add(new Double[]{lowData, averageData, highData});
			int maxPop = Main.SETTINGS.mutabilityGraphMaxPoints.value();
			while(maxPop > 0 && populationData.size() > maxPop && mutabilityData.size() > 0) mutabilityData.remove(0);
		}
		
		
		//add age data
		if(totalTime % Main.SETTINGS.ageGraphUpdateRate.value() == 0){
			int low = 0;
			int high = 0;
			long total = 0;
			for(int i = 0; i < evolvingCreatures.size(); i++){
				//get age for relevant indexes
				long a = evolvingCreatures.get(i).getAge();
				long aLow = evolvingCreatures.get(low).getAge();
				long aHigh = evolvingCreatures.get(high).getAge();
				
				//add to total age
				total += a;
				
				//check for if the ith age should be the highest or lowest
				if(aLow > a) low = i;
				if(aHigh < a) high = i;
			}
			
			//determine the values for the data
			double lowData = 0;
			double highData = 0;
			double averageData = 0;
			if(evolvingCreatures.size() > 0){
				//set the data and convert the values to minutes
				lowData = evolvingCreatures.get(low).getAge() / 6000.0;
				highData = evolvingCreatures.get(high).getAge() / 6000.0;
				averageData = (total / 6000.0) / evolvingCreatures.size();
			}
			
			//add the actual data
			ageData.add(new Double[]{lowData, averageData, highData});
			int maxPop = Main.SETTINGS.ageGraphMaxPoints.value();
			while(maxPop > 0 && populationData.size() > maxPop && ageData.size() > 0) ageData.remove(0);
		}
	}
	
	/**
	 * Draw the current state of the simulation to the given graphics object
	 * @param g the graphics object
	 */
	public void render(Graphics2D g){
		//set up camera
		camera.setG(g);
		
		//draw tile grid
		g.setColor(Color.BLACK);
		double size = Main.SETTINGS.tileSize.value();
		camera.fillRect(-2, -2, size * grid.length + 4, size * grid[0].length + 4);
		for(Tile[] tt : grid){
			for(Tile t : tt) t.render(camera);
		}
		
		//draw user creature
		if(userCreature != null) userCreature.render(camera);
		
		//draw NeuralNetCreatures
		ArrayList<NeuralNetCreature> creatures = new ArrayList<NeuralNetCreature>();
		creatures.addAll(evolvingCreatures);
		for(NeuralNetCreature c : creatures){
			if(c != null) c.render(camera);
		}
	}
	
	/**
	 * Save this simulation in Main.SAVES_PATH with the given filename, do not include the file extension
	 * @param fileName
	 * @return true if the save was successful, false otherwise
	 */
	@Override
	public boolean save(PrintWriter write){
		try{
			boolean succeess = true;
			
			//save settings
			succeess &= Main.SETTINGS.save(write);
			
			//save misc info
			write.println(getTotalTime() + " " + nextCreatureId);
			
			//save camera
			succeess &= camera.save(write);
			
			//save user creature
			if(userCreature == null) write.println("null");
			else{
				write.println("not");
				succeess &= userCreature.save(write);
			}
			
			//save size of grid
			write.println(grid.length + " " + grid[0].length);
			//save grid
			for(Tile[] tt : grid){
				for(Tile t : tt){
					write.println(t.getClass().getSimpleName());
					succeess &= t.save(write);
				}
			}
			
			write.println(evolvingCreatures.size());
			for(NeuralNetCreature c : evolvingCreatures) succeess &= c.save(write);
			
			return succeess;
			
		}catch(Exception e){
			System.err.println("Sim encountered an error in saving");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Load this simulation in from the given filename. It will be loaded from Main.SAVES_PATH
	 * @param fileName
	 * @return true if the load was successful, false otherwise
	 */
	@Override
	public boolean load(Scanner read){
		//keep track of if any loads failed
		try{
			boolean success = true;
			
			//load settings
			success &= Main.SETTINGS.load(read);
			
			//load misc info
			setTotalTime(read.nextLong());
			nextCreatureId = read.nextLong();
			
			//load camera
			if(camera == null) camera = new Camera(1, 1);
			success &= camera.load(read);
			
			//load user creature
			if(!read.next().equals("null")){
				userCreature = new UserCreature(this);
				success &= userCreature.load(read);
			}

			//initialize grid
			int width = read.nextInt();
			int height = read.nextInt();
			if(width == 0 || height == 0) success = false;

			//load grid
			grid = new Tile[width][height];
			for(int i = 0; i < grid.length && success; i++){
				for(int j = 0; j < grid[i].length && success; j++){
					String type = read.next();
					if(type.equals("FoodTile")){
						grid[i][j] = new FoodTile(0, 0, this);
						success &= grid[i][j].load(read);
					}
					else if(type.equals("WaterTile")){
						grid[i][j] = new WaterTile(0, 0, this);
						success &= grid[i][j].load(read);
					}
					else success = false;
				}
			}
			
			evolvingCreatures = new ArrayList<NeuralNetCreature>();
			int numCreatures = read.nextInt();
			for(int i = 0; i < numCreatures; i++){
				NeuralNetCreature c = new NeuralNetCreature(this);
				success &= c.load(read);
				evolvingCreatures.add(c);
			}

			return success;
			
		}catch(Exception e){
			System.err.println("Sim encountered an error in loading");
			e.printStackTrace();
			return false;
		}
	}
	
}
