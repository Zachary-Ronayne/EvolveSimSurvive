package util.options;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.util.options.StringSetting;

public class StringSettingTest{
	
	private StringSetting setting;
	
	@Before
	public void setUp(){
		setting = new StringSetting("b", "B", "Big");
	}
	
	@Test
	public void testSetStringValue(){
		boolean good;
		
		good = setting.setStringValue(null);
		assertFalse(good);
		
		good = setting.setStringValue("bBb");
		assertTrue(good);
		assertTrue(setting.value().equals("bBb"));
	}
	
	@Test
	public void testValidValue(){
		assertTrue(setting.validValue(null) == null);
		assertFalse(setting.validValue("test") == null);
	}
	
}
