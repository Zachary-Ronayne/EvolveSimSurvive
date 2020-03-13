package gui.component;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import evolve.gui.component.Padding;

public class PaddingTest{
	
	private Padding pad;
	
	@Before
	public void setUp(){
		pad = new Padding(10, 25, 4, 18);
	}
	
	@Test
	public void testAddPadding(){
		assertFalse(pad.addPadding(new JPanel()) == null);
		assertFalse(pad.addPadding(null) == null);
		assertFalse(pad.addPadding(new JPanel(), Color.WHITE) == null);
		assertFalse(pad.addPadding(null) == null);
		assertFalse(pad.addPadding(new JPanel(), null) == null);
		assertFalse(pad.addPadding(null, Color.WHITE) == null);
	}
	
	@Test
	public void testGetSetPadL(){
		pad.setPadL(1);
		assertTrue(pad.getPadL() == 1);
		
		pad.setPadL(-1);
		assertTrue(pad.getPadL() == 0);
	}
	
	@Test
	public void testGetSetPadR(){
		pad.setPadR(1);
		assertTrue(pad.getPadR() == 1);
		
		pad.setPadR(-1);
		assertTrue(pad.getPadR() == 0);
	}
	
	@Test
	public void testGetSetPadT(){
		pad.setPadT(1);
		assertTrue(pad.getPadT() == 1);
		
		pad.setPadT(-1);
		assertTrue(pad.getPadT() == 0);
	}
	
	@Test
	public void testGetSetPadB(){
		pad.setPadB(1);
		assertTrue(pad.getPadB() == 1);
		
		pad.setPadB(-1);
		assertTrue(pad.getPadB() == 0);
	}
	
}
