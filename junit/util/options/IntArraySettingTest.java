package util.options;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.util.options.IntArraySetting;

public class IntArraySettingTest{
	
	private IntArraySetting setting;
	
	@Before
	public void setUp(){
		setting = new IntArraySetting(new Integer[]{1, 2}, "C", "Setting");
	}
	
	@Test
	public void testSetStringValue(){
		boolean good;
		
		good = setting.setStringValue(null);
		assertFalse(good);

		good = setting.setStringValue("bBb");
		assertFalse(good);
		
		good = setting.setStringValue("1.3");
		assertFalse(good);
		
		good = setting.setStringValue("4 7");
		assertTrue(good);
	}

	@Test
	public void testValidValue(){
		assertTrue(setting.validValue(null) == null);
		assertTrue(setting.validValue("4.0") == null);
		assertTrue(setting.validValue("a") == null);
		assertFalse(setting.validValue("1 4") == null);
		assertFalse(setting.validValue("2") == null);
		assertFalse(setting.validValue(" ") == null);
		assertFalse(setting.validValue("  56    9") == null);
	}
	
	@Test
	public void testToString(){
		assertFalse(setting.toString() == null);
	}
	
	@Test
	public void testGetDefaultValueString(){
		assertFalse(setting.getDefaultValueString() == null);
	}
	
}
