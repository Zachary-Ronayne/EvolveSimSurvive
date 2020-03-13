package util.options;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.util.options.IntSetting;

public class IntSettingTest{
	
	private IntSetting setting;
	
	@Before
	public void setUp(){
		setting = new IntSetting(1, "B", "Big");
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
		
		good = setting.setStringValue("4");
		assertTrue(good);
		assertTrue(setting.value() == 4);
		
		good = setting.setStringValue("-21");
		assertTrue(good);
		assertTrue(setting.value() == -21);
	}

	@Test
	public void testValidValue(){
		assertTrue(setting.validValue(null) == null);
		assertTrue(setting.validValue("4.0") == null);
		assertTrue(setting.validValue("a") == null);
		assertFalse(setting.validValue("7") == null);
		assertFalse(setting.validValue("-98") == null);
	}
	
}
