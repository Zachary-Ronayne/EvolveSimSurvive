package util.options;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.util.options.Setting;
import evolve.util.options.Settings;

public class SettingsTest{
	
	private Settings settings;
	
	@Before
	public void setUp(){
		settings = new Settings();
	}
	
	@Test
	public void testGetSettings(){
		ArrayList<Setting<?>> s = settings.getSettings();
		assertFalse(s == null);
	}
	
	@Test
	public void testLoadDefaultSettings(){
		settings.loadDefaultSettings();
	}

	@Test
	public void testSaveAndLoad(){
		settings.loadDefaultSettings();
		try{
			//test save
			PrintWriter write = new PrintWriter(new File(Main.DATA_PATH + "/testSave.txt"));
			settings.save(write);
			write.close();
			
			//test load
			Scanner read = new Scanner(new File(Main.DATA_PATH + "/testSave.txt"));
			settings.load(read);
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
}
