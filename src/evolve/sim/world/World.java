package evolve.sim.world;

import java.awt.geom.Point2D;
import java.util.Random;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.obj.tile.FoodTile;
import evolve.sim.obj.tile.Tile;
import evolve.sim.obj.tile.WaterTile;
import evolve.util.math.Circle;

/**
 * A class that uses values from Main.SETTINGS to generate objects in a Simulation based on a seed.<br>
 * Methods in this class are very costly and should not be expected to perform quickly. Instead, this class 
 * should explicitly be used for initialization, never for updating in real time.
 */
public class World{
	
	/**
	 * The seed that this {@link World} uses to generate {@link Simulation} objects
	 */
	private long seed;
	
	/**
	 * The seeded random value to use for the initial value for determining tile species
	 */
	private double tileBaseValue;
	/**
	 * The values that control the offset in each sin function that determines tile species
	 */
	private double[] tileOffsetRand;
	/**
	 * The weights that control how significant each sin function is that determines tile species
	 */
	private double[] tileWeights;
	
	/**
	 * A 3D array of all the rivers.<br>
	 * The first index is the x index of the river<br>
	 * The second index is the y index of the river<br>
	 * The third index is the specific river at those coordinate indexes
	 */
	private River[][][] rivers;
	
	/**
	 * A 3D array of all the islands that exist in the current seed
	 * The first index is the x index of the island<br>
	 * The second index is the y index of the island<br>
	 * The third index is the specific island at those coordinate indexes
	 */
	private Circle[][][] islands;
	
	/**
	 * Create a {@link World} with the given seed and set up this {@link World} object to use the random seed values from that seed
	 * @param seed
	 */
	public World(long seed){
		setSeed(seed);
	}
	
	public long getSeed(){
		return seed;
	}
	public void setSeed(long seed){
		this.seed = seed;
		calculateData();
	}
	
	/**
	 * Calculate the values that control the generation of this {@link World} based on the current seed
	 */
	private void calculateData(){
		//find tile weights
		Random rand = new Random();
		rand.setSeed(this.seed);
		
		tileBaseValue = rand.nextDouble();
		tileOffsetRand = new double[]{rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble()};
		tileWeights = new double[]{rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble()};
		
		//determine rivers
		int numRivers = Main.SETTINGS.worldRiverCount.value();
		double chunkSize = Main.SETTINGS.worldChunkSize.value();
		int riverBorderChunks = Main.SETTINGS.worldRiverBorderSize.value();
		int xChunkSize = (int)(Math.ceil(Main.SETTINGS.tilesX.value() / chunkSize) + riverBorderChunks * 2);
		int yChunkSize = (int)(Math.ceil(Main.SETTINGS.tilesY.value() / chunkSize) + riverBorderChunks * 2);
		double tSize = Main.SETTINGS.tileSize.value();
		double riverChance = Main.SETTINGS.worldRiverChance.value();
		
		rivers = new River[xChunkSize][yChunkSize][numRivers];
		
		for(int i = 0; i < rivers.length; i++){
			for(int j = 0; j < rivers[i].length; j++){
				for(int k = 0; k < rivers[i][j].length; k++){
					//seed the random number generator for making this river
					rand.setSeed(this.seed + i * 1000000l + j * 10000l + k);
					
					//randomly decide if this river should be generated
					if(rand.nextDouble() < riverChance){
						//determine range values for this river
						//points on the river parabola are always generated in it's own chunk
						double minX = i * chunkSize * tSize;
						double maxX = (i + 1) * chunkSize * tSize;
						double minY = j * chunkSize * tSize;
						double maxY = (j + 1) * chunkSize * tSize;
						
						//randomly generate 3 points for this river
						double x1 = minX + rand.nextDouble() * (maxX - minX);
						double y1 = minY + rand.nextDouble() * (maxY - minY);
						double x2 = minX + rand.nextDouble() * (maxX - minX);
						double y2 = minY + rand.nextDouble() * (maxY - minY);
						double x3 = minX + rand.nextDouble() * (maxX - minX);
						double y3 = minY + rand.nextDouble() * (maxY - minY);
						
						//if any of those y points are equal, or all 3 of the x points are equal, do not make a river
						if(y1 == y2 || y1 == y3 || y2 == y3 || x1 == x2 && x2 == x3){
							rivers[i][j][k] = null;
						}
						//otherwise make the river
						else{
							//choose a random size
							double minSize = Main.SETTINGS.worldRiverMinSize.value();
							double maxSize = Main.SETTINGS.worldRiverMaxSize.value();
							double size = minSize + (maxSize - minSize) * rand.nextDouble();
							
							//make the river
							rivers[i][j][k] = new River(x1, y1, x2, y2, x3, y3, 0, 0, size);
							River r = rivers[i][j][k];
							
							//randomly generate bounds
							r.setLowXBound(minX + rand.nextDouble() * (maxX - minX));
							r.setHighXBound(minX + rand.nextDouble() * (maxX - minX));
							
							//ensure that those bounds stay within the chunk bounds based on y coordinates
							Point2D.Double vertex = r.getVertex();
							double lowBoundY = r.y(r.getLowXBound());
							double highBoundY = r.y(r.getHighXBound());
							
							//keep track of which directions the limits need to be set
							boolean setLowLimit = false;
							boolean setHighLimit = false;
							
							//only need to check the vertex if the x coordinate of the vertex is between the two x limits
							if(vertex.x > r.getLowXBound() && vertex.x < r.getHighXBound()){
								//limit based on the vertex
								if(vertex.y < minY) setLowLimit = true;
								if(vertex.y > maxY) setHighLimit = true;
							}
							//regardless, the two end points of the bounds must be checked
							if(lowBoundY < minY || highBoundY < minY) setLowLimit = true;
							if(lowBoundY > maxY || highBoundY > maxY) setHighLimit = true;
							
							//now, based on which limits must be set from y coordinates, set the limits
							if(setLowLimit){
								double[] xValues = r.getXValues(lowBoundY);
								//xValues should always be not null because the y values exist, but keeping the check to be sure
								if(xValues != null) r.setLowXBound(Math.min(xValues[0], xValues[1]));
							}
							if(setHighLimit){
								double[] xValues = r.getXValues(highBoundY);
								//xValues should always be not null because the y values exist, but keeping the check to be sure
								if(xValues != null) r.setHighXBound(Math.max(xValues[0], xValues[1]));
							}
							
							//find new min and max values for where rivers can extend to
							minX = (i - riverBorderChunks) * chunkSize * tSize;
							maxX = (i + riverBorderChunks + 1) * chunkSize * tSize;

							//set limits based on the islands size
							double leftSpace = (r.getLowXBound() - r.getSize()) - minX;
							double rightSpace = maxX - (r.getHighXBound() + r.getSize());
							if(leftSpace < 0) r.setLowXBound(r.getLowXBound() + leftSpace);
							if(rightSpace < 0) r.setLowXBound(r.getHighXBound() + rightSpace);
						}
					}
					else{
						rivers[i][j][k] = null;
					}
				}
			}
		}
		
		
		//determine islands
		
		int islandBorderChunks = Main.SETTINGS.worldIslandBorderSize.value();
		xChunkSize = (int)(Math.ceil(Main.SETTINGS.tilesX.value() / chunkSize) + islandBorderChunks * 2);
		yChunkSize = (int)(Math.ceil(Main.SETTINGS.tilesY.value() / chunkSize) + islandBorderChunks * 2);
		int numIslands = Main.SETTINGS.worldIslandCount.value();
		double islandChance = Main.SETTINGS.worldIslandChance.value();
		double minIslandSize = Main.SETTINGS.worldIslandMinSize.value();
		double maxIslandSize = Main.SETTINGS.worldIslandMaxSize.value();
		
		islands = new Circle[xChunkSize][yChunkSize][numIslands];
		
		for(int i = 0; i < islands.length; i++){
			for(int j = 0; j < islands[i].length; j++){
				for(int k = 0; k < islands[i][j].length; k++){
					//seed the random number generator for making this island
					rand.setSeed(this.seed + i * 1034700l + j * 18780l + k);
					
					//randomly decide to make each island
					if(rand.nextDouble() < islandChance){
						//determine range values for this island's coordinates, should only be in its own chunk
						double minX = i * chunkSize * tSize;
						double maxX = (i + 1) * chunkSize * tSize;
						double minY = j * chunkSize * tSize;
						double maxY = (j + 1) * chunkSize * tSize;
						
						//randomly generate a points for the center of this island
						double x = minX + rand.nextDouble() * (maxX - minX);
						double y = minY + rand.nextDouble() * (maxY - minY);
						
						//pick a random radius for this island
						double radius = minIslandSize + rand.nextDouble() * (maxIslandSize + minIslandSize);

						//determine range values to keep this island in bounds
						minX = (i - islandBorderChunks) * chunkSize * tSize;
						maxX = (i + islandBorderChunks + 1) * chunkSize * tSize;
						minY = (j - islandBorderChunks) * chunkSize * tSize;
						maxY = (j + islandBorderChunks + 1) * chunkSize * tSize;
						double squareSize = (maxX - minX) / 2;
						
						//keep the island's size inside the chunk range
						if(radius > squareSize) radius = squareSize;
						
						//determine the amount of space between each side of the bounds of the set of chunks for this island and the island
						double leftSpace = (x - radius) - minX;
						double rightSpace = maxX - (x + radius);
						double topSpace = (y - radius) - minY;
						double botSpace = maxY - (y + radius);
						
						//keep the island inside that range if necessary
						if(leftSpace < 0) radius += leftSpace;
						if(rightSpace < 0) radius += rightSpace;
						if(topSpace < 0) radius += topSpace;
						if(botSpace < 0) radius += botSpace;
						
						//set up the island
						islands[i][j][k] = new Circle(x, y, radius);
					}
					else islands[i][j][k] = null;
				}
			}
		}
		
	}
	
	/**
	 * Set up the given sim to the seed of this world
	 * @param sim
	 */
	public void generate(Simulation sim){
		//set up the world size
		Tile[][] grid = new Tile[Main.SETTINGS.tilesX.value()][Main.SETTINGS.tilesY.value()];
		
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				grid[i][j] = generateTile(i, j, sim);
			}
		}
		
		sim.setGrid(grid);
	}
	
	/**
	 * Create a {@link Tile} based on the given x and y coordinate and the given seed.<br>
	 * This will set the stats of the returned {@link Tile} based on the seed, including the Type and species value.<br>
	 * The initial food value is always the maximum
	 * @param x the x index of the tile
	 * @param y the y index of the tile
	 * @return the generated tile
	 */
	public Tile generateTile(int x, int y, Simulation sim){
		if(!isIsland(x, y) || isInRiver(x, y)) return new WaterTile(x, y, sim);
		return generateFoodTile(x, y, sim);
	}
	
	/**
	 * Create a food tile at the given tile index
	 * @param x the x index
	 * @param y the y index
	 * @return
	 */
	public FoodTile generateFoodTile(int x, int y, Simulation sim){
		Random rand = new Random();

		rand.setSeed(getSeed() + x * y + x * 1000000l + y);
		//the values of the offset of the variables in each sin function, based on the x and y coordinates
		double[] mainOffset = new double[]{rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble()};
		
		rand.setSeed(getSeed() + x * y + x * 1000000l + y + 1);
		//the values of the offset of the variables in each sin function, based on the x and y coordinates
		double[] addOffset = new double[]{rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), rand.nextDouble()};
		
		//get the tile percent values for each equation
		double[] tilePerc = new double[]{
				Main.SETTINGS.worldShapePercent1.value(), Main.SETTINGS.worldShapePercent2.value(), Main.SETTINGS.worldShapePercent3.value(),
				Main.SETTINGS.worldShapePercent4.value(), Main.SETTINGS.worldShapePercent5.value(), Main.SETTINGS.worldShapePercent6.value()
		};
		
		double[] fOffSet = new double[]{
				Main.SETTINGS.worldShapeOffset1.value(), Main.SETTINGS.worldShapeOffset2.value(), Main.SETTINGS.worldShapeOffset3.value(),
				Main.SETTINGS.worldShapeOffset4.value(), Main.SETTINGS.worldShapeOffset5.value(), Main.SETTINGS.worldShapeOffset6.value()
		};
		
		double[] shapeWeight = new double[]{
				Main.SETTINGS.worldShapeWeight1.value(), Main.SETTINGS.worldShapeWeight2.value(), Main.SETTINGS.worldShapeWeight3.value(),
				Main.SETTINGS.worldShapeWeight4.value(), Main.SETTINGS.worldShapeWeight5.value(), Main.SETTINGS.worldShapeWeight6.value()
		};
		
		double[] shapeScalar = new double[]{
				Main.SETTINGS.worldShapeScalar1.value(), Main.SETTINGS.worldShapeScalar2.value(), Main.SETTINGS.worldShapeScalar3.value(),
				Main.SETTINGS.worldShapeScalar4.value(), Main.SETTINGS.worldShapeScalar5.value(), Main.SETTINGS.worldShapeScalar6.value()
		};
		
		//set up the tile initially
		FoodTile t = new FoodTile(x, y, sim);
		
		//set up variables for shifting and scaling x and y
		double dx = x * Main.SETTINGS.worldXScalar.value() + Main.SETTINGS.worldXPos.value();
		double dy = y * Main.SETTINGS.worldYScalar.value() + Main.SETTINGS.worldYPos.value();
		
		//set the food to max
		t.setFood(Main.SETTINGS.foodMax.value());
		//determine the species
		t.setSpeciesAmount(Main.SETTINGS.worldOffset.value() +
			Main.SETTINGS.worldScalar.value() *
			(tileBaseValue +
			tileWeights[0] * shapeWeight[0] * Math.sin(tileOffsetRand[0] +
					shapeScalar[0] * (dx + 1) * (tilePerc[0] + (1 - tilePerc[0]) * mainOffset[0])
					+ fOffSet[0] * (addOffset[0] - .5)
			) +
			tileWeights[1] * shapeWeight[1] * Math.sin(tileOffsetRand[1] +
					shapeScalar[1] * (dy + 1) * (tilePerc[1] + (1 - tilePerc[1]) * mainOffset[1])
					+ fOffSet[1] * (addOffset[1] - .5)
			) +
			tileWeights[2] * shapeWeight[2] * Math.sin(tileOffsetRand[2] +
					shapeScalar[2] * (dx * dy + 1) * (tilePerc[2] + (1 - tilePerc[2]) * mainOffset[2])
					+ fOffSet[2] * (addOffset[2] - .5)
			) +
			tileWeights[3] * shapeWeight[3] * (tileOffsetRand[0] +
					shapeScalar[3] * (dx + 1) * (tilePerc[3] + (1 - tilePerc[3]) * mainOffset[3])
					+ fOffSet[3] * (addOffset[3] - .5)
			) +
			tileWeights[4] * shapeWeight[4] * (tileOffsetRand[1] +
					shapeScalar[4] * (dy + 1) * (tilePerc[4] + (1 - tilePerc[4]) * mainOffset[4])
					+ fOffSet[4] * (addOffset[4] - .5)
			) +
			tileWeights[5] * shapeWeight[5] * (tileOffsetRand[5] +
					shapeScalar[5] * (dx * dy + 1) * (tilePerc[2] + (1 - tilePerc[5]) * mainOffset[5])
					+ fOffSet[5] * (addOffset[5] - .5)
			))
		);
		return t;
	}
	
	/**
	 * Determine if the given tile is part of an island and thus is not in the ocean
	 * @param x the x index
	 * @param y the y index
	 * @return true if the tile is on an island, false otherwise
	 */
	public boolean isIsland(int x, int y){
		int chunkSize = Main.SETTINGS.worldChunkSize.value();
		int borderChunks = Main.SETTINGS.worldIslandBorderSize.value();
		double tSize = Main.SETTINGS.tileSize.value();
		
		Random rand = new Random(getSeed() + x * 14786 + y * 11003);
		double noise = Main.SETTINGS.worldIslandNoise.value();
		double noiseScalar = 1 - rand.nextDouble() * noise;
		double noisePow = Main.SETTINGS.worldIslandNoiseScalar.value();
		
		//determine the range for which islands should be used
		int baseChunkX = Math.max(0, (int)Math.floor(x / (double)chunkSize) - borderChunks);
		//the +1 is to account for the chunk the tile is on
		int endChunkX = baseChunkX + borderChunks * 2 + 1;
		
		int baseChunkY = Math.max(0, (int)Math.floor(y / (double)chunkSize) - borderChunks);
		//the +1 is to account for the chunk the tile is on
		int endChunkY = baseChunkY + borderChunks * 2 + 1;
		
		//loop through the appropriate sub section of the islands array
		for(int i = baseChunkX; i < endChunkX && i < islands.length; i++){
			for(int j = baseChunkY; j < endChunkY && j < islands[i].length; j++){
				for(int k = 0; k < islands[i][j].length; k++){
					Circle c = islands[i][j][k];
					
					//if there is an island and it is within the random range value of the tile, then it is an island
					if(c != null && c.getRadius() > 0){
						//find the distance to the island
						double dist = c.distance((x + .5) * tSize, (y + .5) * tSize);
						//if in range of the island
						if(dist <= c.getRadius()){
							//find out how close, in a percentage, the current tile is to the island
							double perc = dist / c.getRadius();
							//use noise to determine if this tile is on the island
							//the closer the tile is to the center of the island, the higher chance it is to be included
							if(dist <= c.getRadius() * (1 - noiseScalar * Math.pow(perc, noisePow))){
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Given a tile index, determine if that tile is in a river and thus should be a water tile
	 * @param x the x index
	 * @param y the y index
	 * @return true if the tile is in a river, false otherwise
	 */
	public boolean isInRiver(int x, int y){
		int chunkSize = Main.SETTINGS.worldChunkSize.value();
		int borderChunks = Main.SETTINGS.worldRiverBorderSize.value();
		double tSize = Main.SETTINGS.tileSize.value();
		
		Random rand = new Random(getSeed() + x * 12345 + y + 18432);
		double noise = Main.SETTINGS.worldRiverNoise.value();
		double noiseScalar = 1 - rand.nextDouble() * noise;
		double noisePow = Main.SETTINGS.worldRiverNoiseScalar.value();
		
		//must adjust the sub array of tiles by +borderChunks at the end to account for the array starting with rivers in negative chunks that will not exist
		
		//determine the range for which rivers should be used
		int baseChunkX = Math.max(0, (int)Math.floor(x / (double)chunkSize) - borderChunks);
		//the +1 is to account for the chunk the tile is on
		int endChunkX = baseChunkX + borderChunks * 2 + 1;
		
		int baseChunkY = Math.max(0, (int)Math.floor(y / (double)chunkSize) - borderChunks);
		//the +1 is to account for the chunk the tile is on
		int endChunkY = baseChunkY + borderChunks * 2 + 1;
		
		//loop through the appropriate sub section of the rivers array
		for(int i = baseChunkX; i < endChunkX && i < rivers.length; i++){
			for(int j = baseChunkY; j < endChunkY && j < rivers[i].length; j++){
				for(int k = 0; k < rivers[i][j].length; k++){
					River r = rivers[i][j][k];
					if(r != null && r.getSize() > 0){
						//find the distance to the river
						double dist = r.aproximateDistance((x + .5) * tSize, (y + .5) * tSize);
						//if in range of the island
						if(dist <= r.getSize()){
							//find out how close, in a percentage, the current tile is to the river
							double perc = dist / r.getSize();
							//use noise to determine if this tile is on the river
							//the closer the tile is to the center of the river, the higher chance it is to be included
							if(dist <= r.getSize() * (1 - noiseScalar * Math.pow(perc, noisePow))){
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
}
