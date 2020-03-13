package sim.obj;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.misc.CreatureGlow;
import evolve.sim.misc.Species;
import evolve.sim.obj.Creature;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.UserCreature;
import evolve.sim.obj.tile.FoodTile;
import evolve.sim.obj.tile.Tile;
import evolve.sim.obj.tile.WaterTile;
import evolve.util.Camera;
import evolve.util.options.Settings;

public class CreatureTest{
	
	public static final double DELTA = 0.000001;
	
	private Creature creature;
	private Simulation sim;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		sim = new Simulation();
		creature = new UserCreature(sim);
	}
	
	@Test
	public void testUpdateCacheCata(){
		creature.cacheData();
		creature.update();
		creature.setX(-1000);
		creature.update();
		creature.setX(2000);
		creature.update();
		creature.setY(-1000);
		creature.update();
		creature.setY(2000);
		creature.update();
	}
	
	@Test
	public void testRender(){
		BufferedImage buff = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D)buff.getGraphics();
		Camera cam = new Camera(100, 100);
		cam.setG(g);
		
		creature.render(cam);
	}
	
	@Test
	public void testGetSetGlow(){
		CreatureGlow glow = new CreatureGlow(creature, Color.RED, 10);
		creature.setGlow(glow);
		assertTrue(creature.getGlow().equals(glow));
	}
	
	@Test
	public void testCreateGlow(){
		creature.createGlow(8);
		assertTrue(creature.getGlow().getRadius() == 8);
	}

	@Test
	public void testKill(){
		Main.SETTINGS.tileSpeciesScalar.setValue(0.001);
		
		double[] tileS = 		new double[]{.4, .5, .1, .9};
		double[] creatureS = 	new double[]{.5, .4, .9, .1};
		
		for(int i = 0; i < 4; i++){
			creature.setX(50);
			creature.setY(50);
			creature.setSpecies(new Species(creatureS[i]));
			Species creatureSpecies = creature.getSpecies();
			
			Tile t = sim.getContainingTile(creature.getX(), creature.getY());
			t.setSpeciesAmount(tileS[i]);
			
			double oldSpecies = t.getSpecies().compareSpecies(creatureSpecies);
			
			creature.kill();
			assertTrue(creature.isDead());
			
			double newSpecies = t.getSpecies().compareSpecies(creatureSpecies);
			assertNotEquals(oldSpecies, newSpecies, DELTA);
			assertTrue(newSpecies < oldSpecies);
		}
	}
	
	@Test
	public void testIsDead(){
		creature.kill();
		assertTrue(creature.isDead());
		creature.revive();
		assertFalse(creature.isDead());
	}
	
	@Test
	public void testRevive(){
		creature.revive();
		assertFalse(creature.isDead());
	}
	
	@Test
	public void testAttackNearestCreature(){
		Simulation sim = creature.getSimulation();
		sim.getEvolvingCreatures().clear();
		NeuralNetCreature add = new NeuralNetCreature(sim);
		sim.addCreature(add);
		sim.update();

		creature.setX(0);
		creature.setY(0);
		add.setX(0);
		add.setY(0);
		creature.attackNearestCreature();
		
		creature.addEnergy(100000);
		add.addEnergy(100000);
		creature.setX(0);
		creature.setY(0);
		add.setX(2000);
		add.setY(2000);
		creature.attackNearestCreature();
	}
	
	@Test
	public void testAttack(){
		Simulation sim = creature.getSimulation();
		sim.getEvolvingCreatures().clear();
		NeuralNetCreature add = new NeuralNetCreature(sim);
		sim.addCreature(add);
		sim.update();
		
		double oldEnergyAdd = add.getEnergy();
		double oldEnergyC = creature.getEnergy();
		creature.setX(0);
		creature.setY(0);
		add.setX(0);
		add.setY(0);
		creature.attack(add);
		assertTrue(oldEnergyAdd > add.getEnergy());
		assertTrue(oldEnergyC < creature.getEnergy());
	}
	
	@Test
	public void testTakeHit(){
		Simulation sim = creature.getSimulation();
		sim.getEvolvingCreatures().clear();
		NeuralNetCreature add = new NeuralNetCreature(sim);
		sim.addCreature(add);
		sim.update();
		
		double oldEnergyAdd = add.getEnergy();
		double oldEnergyC = creature.getEnergy();
		creature.setX(0);
		creature.setY(0);
		add.setX(0);
		add.setY(0);
		add.takeHit(creature);
		assertTrue(oldEnergyAdd > add.getEnergy());
		assertTrue(oldEnergyC < creature.getEnergy());
	}
	
	@Test
	public void testEat(){
		Main.SETTINGS.creatureMinSize.setValue(1.0);
		Main.SETTINGS.creatureMaxSize.setValue(100.0);
		double size = Main.SETTINGS.tileSize.value();
		
		creature.setX(size);
		creature.setY(size);
		double energy = creature.getEnergy();
		creature.eat();
		assertNotEquals(creature.getEnergy(), energy, DELTA);

		creature.setX(size / 2);
		creature.setY(size / 2);
		creature.setSpecies(new Species(0));
		FoodTile t = new FoodTile(0, 0, creature.getSimulation());
		creature.getSimulation().getGrid()[0][0] = t;
		t.setSpeciesAmount(.5);
		t.addFood(Main.SETTINGS.foodMax.value());
		creature.cacheData();
		double eatenLow = t.eat(creature);
		t.setSpeciesAmount(.25);
		t.addFood(Main.SETTINGS.foodMax.value());
		creature.cacheData();
		double eatenMiddle = t.eat(creature);
		t.setSpeciesAmount(0);
		t.addFood(Main.SETTINGS.foodMax.value());
		creature.cacheData();
		double eatenHigh = t.eat(creature);
		assertTrue(eatenHigh > eatenMiddle);
		assertTrue(eatenMiddle > eatenLow);
		
		NeuralNetCreature netC = new NeuralNetCreature(creature.getSimulation());
		netC.addEnergy(netC.getMaxEnergy());
		for(int i = 0; i < 100; i++){
			netC.cacheData();
			netC.think();
			netC.update();
			netC.addEnergy(netC.getMaxEnergy());
		}
		netC.revive();
		
		netC.setX(size / 2);
		netC.setY(size / 2);
		netC.setSizeGene(30);
		t.addFood(Main.SETTINGS.foodMax.value());
		t.setTemperature(10);
		t.cacheData();
		netC.cacheData();
		eatenLow = t.eat(netC);
		netC.setSizeGene(400);
		t.addFood(Main.SETTINGS.foodMax.value());
		t.setTemperature(10);
		t.cacheData();
		netC.cacheData();
		eatenHigh = t.eat(netC);
		assertTrue(eatenHigh > eatenLow);
	}
	
	@Test
	public void testGetSetSimulation(){
		Simulation s = new Simulation();
		creature.setSimulation(s);
		assertTrue(creature.getSimulation().equals(s));
	}
	
	@Test
	public void testGetTileMovedVector(){
		double tSize = Main.SETTINGS.tileSize.value();
		
		Simulation s = creature.getSimulation();
		Tile t = new WaterTile(0, 0, s);
		t.getTemperature().setTemp(0);
		t.cacheData();
		t.update();
		s.getGrid()[0][0] = t;
		
		creature.setX(tSize * .5);
		creature.setY(tSize * .5);
		creature.cacheData();
		creature.update();
		assertFalse(creature.getTileMovedVector() == null);

		
		t = new FoodTile(0, 0, s);
		t.getTemperature().setTemp(0);
		t.cacheData();
		t.update();
		s.getGrid()[0][0] = t;
		
		creature.setX(tSize * .5);
		creature.setY(tSize * .5);
		creature.cacheData();
		creature.update();
		assertTrue(creature.getTileMovedVector() == null);
	}
	
	@Test
	public void testGetSetAngle(){
		creature.setAngle(1.3);
		assertEquals(creature.getAngle(), 1.3, DELTA);
		
		creature.setAngle(-Math.PI);
		assertEquals(creature.getAngle(), Math.PI, DELTA);
		
		creature.setAngle(2 * Math.PI);
		assertEquals(creature.getAngle(), 0, DELTA);
		creature.setAngle(3 * Math.PI);
		assertEquals(creature.getAngle(), Math.PI, DELTA);
	}
	
	@Test
	public void testAddAngle(){
		double angleChange = Main.SETTINGS.creatureAngleChange.value();
		
		creature.setAngle(Math.PI);
		creature.addAngle(angleChange * .5);
		assertEquals(creature.getAngle(), angleChange * .5 + Math.PI, DELTA);
		
		creature.setAngle(Math.PI);
		creature.addAngle(angleChange * 2);
		assertEquals(creature.getAngle(), angleChange + Math.PI, DELTA);
		
		creature.setAngle(Math.PI);
		creature.addAngle(angleChange * -2);
		assertEquals(creature.getAngle(), -angleChange + Math.PI, DELTA);
	}
	
	@Test
	public void testGetSetSpeed(){
		double maxSpeed = Main.SETTINGS.creatureMaxSpeed.value();
		
		creature.setSpeed(maxSpeed * .5);
		assertEquals(creature.getMoveSpeed(), maxSpeed * .5, DELTA);
		
		creature.setSpeed(maxSpeed * 2);
		assertEquals(creature.getMoveSpeed(), maxSpeed, DELTA);
		
		creature.setSpeed(-maxSpeed);
		assertEquals(creature.getMoveSpeed(), maxSpeed / -2, DELTA);
		
		creature.getTotalSpeed();
	}
	
	@Test
	public void testAddSpeed(){
		double speedChange = Main.SETTINGS.creatureSpeedChange.value();
		
		creature.setSpeed(0);
		creature.addSpeed(speedChange / 2);
		assertEquals(creature.getMoveSpeed(), speedChange / 2, DELTA);
		
		creature.setSpeed(0);
		creature.addSpeed(speedChange * 2);
		assertEquals(creature.getMoveSpeed(), speedChange, DELTA);
		
		creature.setSpeed(0);
		creature.addSpeed(-speedChange * 2);
		assertEquals(creature.getMoveSpeed(), -speedChange, DELTA);
	}
	
	@Test
	public void testGetSetEnergy(){
		creature.setEnergy(5);
		assertEquals(creature.getEnergy(), 5, DELTA);
	}
	
	@Test
	public void testAddEnergy(){
		creature.setEnergy(4);
		creature.addEnergy(44);
		assertEquals(creature.getEnergy(), 48, DELTA);
	}
	
	@Test
	public void testSetShouldEat(){
		creature.setShouldEat(true);
		assertTrue(creature.shouldEat());
	}

	@Test
	public void testGetSetX(){
		creature.setX(44);
		assertEquals(creature.getX(), 44, DELTA);
	}
	
	@Test
	public void testGetSetY(){
		creature.setY(46);
		assertEquals(creature.getY(), 46, DELTA);
	}
	
	@Test
	public void testTouching(){
		Creature testC1 = new Creature(creature.getSimulation()){
			@Override
			public double calculateRadius(){
				return 50;
			}
			@Override
			public double getFur(){
				return 0;
			}
		};
		testC1.cacheData();
		
		Creature testC2 = new Creature(creature.getSimulation()){
			@Override
			public double calculateRadius(){
				return 10;
			}
			@Override
			public double getFur(){
				return 0;
			}
		};
		testC2.cacheData();
		
		
		testC1.setX(0);
		testC1.setY(0);
		testC2.setX(0);
		testC2.setY(0);
		
		assertTrue(testC1.touching(testC2));
		assertTrue(testC2.touching(testC1));

		
		testC1.setX(0);
		testC1.setY(0);
		testC2.setX(59);
		testC2.setY(0);
		assertTrue(testC1.touching(testC2));
		assertTrue(testC2.touching(testC1));
		
		
		testC1.setX(0);
		testC1.setY(0);
		testC2.setX(60.001);
		testC2.setY(0);
		assertFalse(testC1.touching(testC2));
		assertFalse(testC2.touching(testC1));

		
		testC1.setX(0);
		testC1.setY(0);
		testC2.setX(0);
		testC2.setY(59);
		assertTrue(testC1.touching(testC2));
		assertTrue(testC2.touching(testC1));
		
		
		testC1.setX(0);
		testC1.setY(0);
		testC2.setX(0);
		testC2.setY(60.001);
		assertFalse(testC1.touching(testC2));
		assertFalse(testC2.touching(testC1));
	}
	
	@Test
	public void testGetSetAge(){
		creature = new NeuralNetCreature(sim);
		((NeuralNetCreature)creature).think();
		for(int i = 0; i < 13; i++) creature.update();
		assertTrue(creature.getAge() == 13);
		
		creature.setAge(10);
		assertTrue(creature.getAge() == 10);
	}
	
	@Test
	public void testGetSetGeneration(){
		creature.setGeneration(12);
		assertTrue(creature.getGeneration() == 12);
	}
	
	@Test
	public void testGetSetChildren(){
		creature.setBreedChildren(10);
		assertTrue(creature.getBreedChildren() == 10);
		
		creature.setAsexualChildren(5);
		assertTrue(creature.getAsexualChildren() == 5);
		
		assertTrue(creature.getTotalChildren() == 15);
	}
	
	@Test
	public void testGetPos(){
		creature.getPos();
	}
	
	@Test
	public void testGetRadius(){
		creature.getRadius();
	}
	
	@Test
	public void setGetSetSpecies(){
		Species species = new Species();
		creature.setSpecies(species);
		assertTrue(creature.getSpecies().equals(species));
	}
	
	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "/testSave.txt");
		try{
			//test save
			PrintWriter write = new PrintWriter(loc);
			assertTrue(creature.save(write));
			write.close();
			
			//test load
			Scanner read = new Scanner(loc);
			assertTrue(creature.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
	}
	
	@Test
	public void testGetAproxTimeAmount(){
		assertFalse(Creature.getAproxTimeAmount(0l).equals(""));
		assertFalse(Creature.getAproxTimeAmount(2345l).equals(""));
		assertFalse(Creature.getAproxTimeAmount(6723576l).equals(""));
	}
	
	@Test
	public void testGetTimeString(){
		long hundSec = 1;
		long sec = hundSec * 100;
		long min = sec * 60;
		long hour = min * 60;
		long day = hour * 24;
		long year = day * 365;
		
		long time = 1 * year + 1 * day + 1 * hour + 1 * min + 1 * sec + 0 * hundSec;
		assertTrue(Creature.getTimeString(time).equals("1 year, 1 day, 1 hour, 1 minute, 1.00 seconds"));
		
		time = 2 * year + 1 * day + 1 * hour + 1 * min + 2 * sec + 1 * hundSec;
		assertTrue(Creature.getTimeString(time).equals("2 years, 1 day, 1 hour, 1 minute, 2.01 seconds"));
		
		time = 2 * year + 1 * day + 3 * hour + 1 * min + 2 * sec + 10 * hundSec;
		assertTrue(Creature.getTimeString(time).equals("2 years, 1 day, 3 hours, 1 minute, 2.10 seconds"));
		
		time = 2 * year + 201 * day + 3 * hour + 1 * min + 59 * sec + 99 * hundSec;
		assertTrue(Creature.getTimeString(time).equals("2 years, 201 days, 3 hours, 1 minute, 59.99 seconds"));
	}
	
	@Test
	public void testGetAgeTime(){
		assertTrue(Creature.getAgeTime(78)[5] == 78);
		
		assertTrue(Creature.getAgeTime(178)[4] == 1);
		
		assertTrue(Creature.getAgeTime(6001)[3] == 1);
		
		assertTrue(Creature.getAgeTime(360001)[2] == 1);
		
		assertTrue(Creature.getAgeTime(8640001)[1] == 1);
		
		assertTrue(Creature.getAgeTime(3153600001l)[0] == 1);
	}
}
