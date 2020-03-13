package util.options;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.util.options.BooleanSetting;

public class BooleanSettingTest{
	
	private BooleanSetting setting;
	
	@Before
	public void setUp(){
		setting = new BooleanSetting(true, "B", "Big");
	}
	
	@Test
	public void testSetStringValue(){
		boolean good;
		
		good = setting.setStringValue(null);
		assertFalse(good);

		good = setting.setStringValue("True");
		assertFalse(good);
		
		good = setting.setStringValue("true");
		assertTrue(good);
		assertTrue(setting.value());
		
		good = setting.setStringValue("false");
		assertTrue(good);
		assertFalse(setting.value());
	}

	@Test
	public void testValidValue(){
		assertTrue(setting.validValue(null) == null);
		assertTrue(setting.validValue("test") == null);
		assertFalse(setting.validValue("true") == null);
		assertFalse(setting.validValue("false") == null);
	}

}
