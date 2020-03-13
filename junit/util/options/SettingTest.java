package util.options;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.util.options.Setting;
import evolve.util.options.StringSetting;

public class SettingTest{
	
	private Setting<String> setting;
	
	@Before
	public void setUp(){
		setting = new StringSetting("a", "letter a", "first letter");
	}
	
	@Test
	public void testGetSetValue(){
		setting.setValue("A");
		assertTrue(setting.value().equals("A"));
	}
	
	@Test
	public void testGetSetDefaultValue(){
		setting.setDefaultValue("AAA");
		assertTrue(setting.getDefaultValue().equals("AAA"));
	}
	
	@Test
	public void testGetDefaultValueString(){
		setting.setDefaultValue("AAA1");
		assertTrue(setting.getDefaultValueString().equals("AAA1"));
	}
	
	@Test
	public void testLoadDefaultValue(){
		setting.setDefaultValue("AaA");
		setting.loadDefaultValue();
		assertTrue(setting.value().equals("AaA"));
	}
	
	@Test
	public void testSetStringValue(){
		assertTrue(setting.setStringValue("test"));
		assertFalse(setting.setStringValue(null));
	}

	@Test
	public void testToString(){
		setting.setValue("abc");
		assertTrue(setting.toString().equals("abc"));
	}
	
	@Test
	public void testGetSetName(){
		setting.setName("A Name");
		assertTrue(setting.getName().equals("A Name"));
	}

	@Test
	public void testGetSetDescription(){
		setting.setDescription("A description");
		assertTrue(setting.getDescription().equals("A description"));
	}
	
	@Test
	public void testSaveAndLoad(){
		setting.loadDefaultValue();
		
		File loc = new File(Main.DATA_PATH + "/settingsTestSave.txt");
		try{
			//test save
			PrintWriter write = new PrintWriter(loc);
			setting.save(write);
			write.close();
			
			//test load
			Scanner read = new Scanner(loc);
			setting.load(read);
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		loc.delete();
	}
	
}
