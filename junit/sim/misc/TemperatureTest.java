package sim.misc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.misc.Temperature;

public class TemperatureTest{
	
	private Temperature t;
	
	@Before
	public void setUp(){
		t = new Temperature(30);
		assertTrue(t.getTemp() == 30);
	}
	
	@Test
	public void testDefaultConstructor(){
		t = new Temperature();
		assertTrue(t.getTemp() == 0);
	}
	
	@Test
	public void testApproach(){
		double initial;
		double change;
		
		initial = t.getTemp();

		t.setTemp(initial);
		t.approach(new Temperature(20), 1);
		assertTrue(t.getTemp() < initial);
		assertTrue(t.getTemp() > 20);
		
		change = t.getTemp() - initial;
		
		t.setTemp(initial);
		t.approach(new Temperature(20), 2);
		assertTrue(t.getTemp() < initial);
		assertTrue(t.getTemp() > 20);
		assertTrue(Math.abs(t.getTemp() - initial) > Math.abs(change));
		
		t.setTemp(initial);
		t.approach(new Temperature(20), .5);
		assertTrue(t.getTemp() < initial);
		assertTrue(t.getTemp() > 20);
		assertTrue(Math.abs(t.getTemp() - initial) < Math.abs(change));


		t.setTemp(initial);
		t.approach(new Temperature(40), 1);
		assertTrue(t.getTemp() > initial);
		assertTrue(t.getTemp() < 40);
		
		change = t.getTemp() - initial;
		
		t.setTemp(initial);
		t.approach(new Temperature(40), 2);
		assertTrue(t.getTemp() > initial);
		assertTrue(t.getTemp() < 40);
		assertTrue(Math.abs(t.getTemp() - initial) > Math.abs(change));
		
		t.setTemp(initial);
		t.approach(new Temperature(40), .5);
		assertTrue(t.getTemp() > initial);
		assertTrue(t.getTemp() < 40);
		assertTrue(Math.abs(t.getTemp() - initial) < Math.abs(change));
	}
	
	@Test
	public void testGetTemperature(){
		assertTrue(t.getTemp() == 30);
	}
	
	@Test
	public void testSetTemperature(){
		t.setTemp(23);
		assertTrue(t.getTemp() == 23);
	}
	
	@Test
	public void testAddTemperature() {
		t.addTemp(2);
		assertTrue(t.getTemp() == 32);
	}
	
	@Test
	public void testGetOverlayColor(){
		assertFalse(t.getOverlayColor() == null);
	}
	
	@Test
	public void testGetBrightnessRange(){
		t.getBrightnessRange();
	}
	
	@Test
	public void testSaveLoad(){
		Temperature load = new Temperature(0);
		
		File loc = new File(Main.DATA_PATH + "/JUnitTemperatureTest.txt");
		
		try{
			//test save
			PrintWriter write = new PrintWriter(loc);
			assertTrue(t.save(write));
			write.close();
			
			//test load
			Scanner read = new Scanner(loc);
			assertTrue(load.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
		
		assertTrue(load.getTemp() == t.getTemp());
	}
	
	@Test
	public void testOverlayColor(){
		Color c;
		
		c = Temperature.overlayColor(Temperature.MAX_TEMP_DISP);
		assertFalse(c == null);
		assertTrue(c.getAlpha() > 0);
		assertTrue(c.getRed() > c.getBlue());
		assertTrue(c.getGreen() > c.getBlue());
		
		c = Temperature.overlayColor(Temperature.MIN_TEMP_DISP);
		assertFalse(c == null);
		assertTrue(c.getAlpha() > 0);
		assertTrue(c.getRed() < c.getBlue());
		assertTrue(c.getGreen() < c.getBlue());
		
		c = Temperature.overlayColor(0);
		assertFalse(c == null);
		assertTrue(c.getAlpha() == 0);
	}
	
}
