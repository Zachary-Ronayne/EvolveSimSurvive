package sim.obj;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.obj.NeuralNetCreature;
import evolve.sim.obj.tile.FoodTile;
import evolve.util.options.Settings;

public class NeuralNetCreatureTest{
	
	public static final double DELTA = 0.0000001;
	
	private NeuralNetCreature creature;
	private Simulation sim;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		sim = new Simulation();
		creature = new NeuralNetCreature(sim);
	}
	
	@Test
	public void testUpdateCacheDataThink(){
		for(int i = 0; i < 100; i++){
			creature.cacheData();
			creature.think();
			creature.update();
		}
	}
	
	@Test
	public void testASexualBaby(){
		creature.cacheData();
		creature.think();
		creature.setBabyCooldown(0);
		creature.getBrainOutputs()[3] = 1;
		creature.addEnergy(1000000000000.0);
		creature.setEnergyToBaby(1);
		NeuralNetCreature baby = creature.aSexualBaby();
		baby.addEnergy(1000000.0);
		baby.cacheData();
		boolean found = false;
		Simulation sim = creature.getSimulation();
		sim.update();
		for(NeuralNetCreature c : sim.getEvolvingCreatures()){
			if(c.equals(baby)) found = true;
		}
		assertTrue(found);
	}
	
	@Test
	public void testSexualBaby(){
		creature.cacheData();
		creature.think();
		creature.setBabyCooldown(0);
		creature.getBrainOutputs()[3] = 1;
		NeuralNetCreature baby = creature.sexualBaby(creature.copy());
		baby.cacheData();
		boolean found = false;
		Simulation sim = creature.getSimulation();
		sim.update();
		for(NeuralNetCreature c : sim.getEvolvingCreatures()){
			if(c.equals(baby)) found = true;
		}
		assertTrue(found);
	}
	
	@Test
	public void testCalculateFoodInput(){
		double tSize = Main.SETTINGS.tileSize.value();
		creature.setX(tSize / 2);
		creature.setY(tSize / 2);
		
		FoodTile t = new FoodTile(0, 0, creature.getSimulation());
		sim.getGrid()[0][0] = t;
		
		t.setFood(Main.SETTINGS.foodMax.value());
		assertEquals(creature.calculateFoodInput(0, 0), 1, DELTA);
		
		t.setFood(Main.SETTINGS.foodMax.value() / 2);
		assertEquals(creature.calculateFoodInput(0, 0), .5, DELTA);
	}
	
	@Test
	public void testGetSetMutability(){
		double max = Main.SETTINGS.mutabilityMax.value();
		creature.setMutability(max / 2);
		assertEquals(creature.getMutability(), max / 2, DELTA);
	}
	
	@Test
	public void testMutate(){
		creature.mutate();
	}
	
	@Test
	public void testGetBrain(){
		creature.getBrain();
	}
	
	@Test
	public void testGetGeneration(){
		creature.addEnergy(1000000000000.0);
		creature.setEnergyToBaby(1);
		NeuralNetCreature baby = creature.aSexualBaby();
		baby.addEnergy(1000000.0);
		assertTrue(baby.getGeneration() - 1 == creature.getGeneration());
	}
	
	@Test
	public void testGetChildren(){
		creature = new NeuralNetCreature(sim);
		for(int i = 0; i < 10; i++){
			creature.addEnergy(1000000000000.0);
			creature.setEnergyToBaby(1);
			creature.aSexualBaby();
		}
		assertTrue(creature.getTotalChildren() == 10);
	}
	
	@Test
	public void testGetSetEnergyToBaby(){
		creature.setEnergyToBaby(Main.SETTINGS.creatureMinBabyEnergy.value());
		assertTrue(creature.getEnergyToBaby() == Main.SETTINGS.creatureMinBabyEnergy.value());
	}
	
	@Test
	public void testGetBabyCooldown(){
		creature.getBabyCooldown();
	}
	
	@Test
	public void testGetMaxEnergySizeBased(){
		creature.getMaxEnergySizeBased();
	}
	
	@Test
	public void testGetSetMaxEnergy(){
		creature.setMaxEnergy(Main.SETTINGS.creatureMaxEnergy.value());
		creature.getMaxEnergy();
	}
	
	@Test
	public void testGetSetSizeGene(){
		creature.setSizeGene(Main.SETTINGS.creatureMaxSize.value());
		assertTrue(creature.getSizeGene() == Main.SETTINGS.creatureMaxSize.value());
	}
	
	@Test
	public void testGetRadiusScalar(){
		creature.getRadiusScalar();
	}
	
	@Test
	public void testCalculateRadius(){
		creature.calculateRadius();
	}

	@Test
	public void testGetFur(){
		creature.getFur();
	}
	
	@Test
	public void testGetNumberOfEyes(){
		Main.SETTINGS.eyeNumMin.setValue(0);
		Main.SETTINGS.eyeNumMax.setValue(7);
		creature.updateGeneValues();
		creature.getEyesNumGene().setValue(.3);
		assertTrue(creature.getNumberOfEyes() == 0);
		
		creature.getEyesNumGene().setValue(.7);
		assertTrue(creature.getNumberOfEyes() == 1);
		
		creature.getEyesNumGene().setValue(.5);
		assertTrue(creature.getNumberOfEyes() == 1);
		
		creature.getEyesNumGene().setValue(0);
		assertTrue(creature.getNumberOfEyes() == 0);
		
		creature.getEyesNumGene().setValue(2.3);
		assertTrue(creature.getNumberOfEyes() == 2);
		
		creature.getEyesNumGene().setValue(4.9);
		assertTrue(creature.getNumberOfEyes() == 5);
	}
	
	@Test
	public void testGetEyes(){
		creature.getEyes();
	}
	
	@Test
	public void testAddSpeed(){
		creature.addSpeed(2);
	}
	
	@Test
	public void testGetMaxSpeed(){
		creature.getMaxSpeed();
	}
	
	@Test
	public void testAddAngle(){
		creature.addAngle(1);
	}
	
	@Test
	public void testGetAngleTo(){
		creature.getAngleTo(new NeuralNetCreature(creature.getSimulation()));
	}
	
	@Test
	public void testAddEnergy(){
		creature.addEnergy(1);
		creature.addEnergy(-1);
	}
	
	@Test
	public void testClone(){
		NeuralNetCreature copy = creature.copy();
		assertFalse(copy.equals(creature));
		assertTrue(copy.getMutability() == creature.getMutability());
		assertTrue(copy.getX() == creature.getX());
		assertTrue(copy.getY() == creature.getY());
		assertTrue(copy.getAngle() == creature.getAngle());
		assertTrue(copy.getMoveSpeed() == creature.getMoveSpeed());
	}
	
	@Test
	public void testSaveLoad(){
		File loc = new File(Main.DATA_PATH + "NeuralNetCreatureSaveTest.txt");
		try{
			PrintWriter write = new PrintWriter(loc);
			assertTrue(creature.save(write));
			write.close();
			
			Scanner read = new Scanner(loc);
			assertTrue(creature.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
	}
	
}
