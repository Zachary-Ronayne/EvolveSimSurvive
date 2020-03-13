package util.options;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.util.options.DoubleSetting;

public class DoubleSettingTest{
	
	private DoubleSetting setting;
	
	@Before
	public void setUp(){
		setting = new DoubleSetting(1.3, "B", "Big");
	}
	
	@Test
	public void testSetStringValue(){
		boolean good;
		
		good = setting.setStringValue(null);
		assertFalse(good);
		
		good = setting.setStringValue("bBb");
		assertFalse(good);
		
		good = setting.setStringValue("1.4");
		assertTrue(good);
		assertTrue(setting.value() == 1.4);
		
		good = setting.setStringValue("-1");
		assertTrue(good);
		assertTrue(setting.value() == -1);
	}

	@Test
	public void testValidValue(){
		assertTrue(setting.validValue(null) == null);
		assertTrue(setting.validValue("a") == null);
		assertFalse(setting.validValue("7") == null);
		assertFalse(setting.validValue("-98") == null);
		assertFalse(setting.validValue("4.0") == null);
	}
	
}
