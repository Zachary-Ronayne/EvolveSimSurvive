package sim.obj;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.obj.UserCreature;
import evolve.util.options.Settings;

public class UserCreatureTest{
	
	private UserCreature creature;
	private Simulation sim;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		sim = new Simulation();
		creature = new UserCreature(sim);
	}
	
	@Test
	public void testGetFur(){
		assertTrue(creature.getFur() == 30);
	}
	
	@Test
	public void testCalculateRadius(){
		assertTrue(creature.calculateRadius() == 40);
	}
	
	@Test
	public void testCacheData(){
		creature.cacheData();
	}
	
	@Test
	public void testUpdate(){
		boolean[] b = creature.getPressedButtons();
		for(int i = 0; i < b.length; i++) b[i] = true;
		creature.update();
	}
	
	@Test
	public void testGetPressedButtons(){
		boolean[] b = creature.getPressedButtons();
		assertTrue(b.length == 6);
	}

	@Test
	public void testSaveLoad(){
		try{
			//test save
			PrintWriter write = new PrintWriter(new File(Main.DATA_PATH + "/testSave.txt"));
			assertTrue(creature.save(write));
			write.close();
			
			//test load
			Scanner read = new Scanner(new File(Main.DATA_PATH + "/testSave.txt"));
			assertTrue(creature.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
}
