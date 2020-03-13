package gui.component.neuralNet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.gui.component.neuralNet.NeuralNetGuiLabel;

public class NeuralNetGuiLabelTest{
	
	private NeuralNetGuiLabel label;
	
	@Before
	public void setUp(){
		label = new NeuralNetGuiLabel("test", true, 0, -1.2, 2, 3.4);
	}
	
	@Test
	public void testConstructor(){
		new NeuralNetGuiLabel();
		new NeuralNetGuiLabel("", true);
	}
	
	@Test
	public void testGetLabelName(){
		assertTrue(label.getLabelName().equals("test"));
	}

	@Test
	public void testSetLabelName(){
		label.setLabelName("testing");
		assertTrue(label.getLabelName().equals("testing"));
	}

	@Test
	public void testGetValues(){
		double[] values = label.getValues();
		assertTrue(values.length == 4);
		assertTrue(values[0] == 0);
		assertTrue(values[1] == -1.2);
		assertTrue(values[2] == 2);
		assertTrue(values[3] == 3.4);
	}

	@Test
	public void testSetValues(){
		label.setValues(2);
		double[] values = label.getValues();
		assertTrue(values.length == 1);
		assertTrue(values[0] == 2);
	}

	@Test
	public void testIsInteger(){
		assertTrue(label.isInteger());
	}

	@Test
	public void testSetInteger(){
		label.setInteger(true);
		assertTrue(label.isInteger());
		label.setInteger(false);
		assertFalse(label.isInteger());
	}

	@Test
	public void testUpdateNameLabel(){
		label.setName("test3");
		label.updateNameLabel();
	}

	@Test
	public void testUpdateValueLabel(){
		label.setInteger(false);
		label.updateValueLabel();
		label.setInteger(true);
		label.updateValueLabel();
	}

	@Test
	public void testSetNameString(){
		label.setNameString("testing");
	}

	@Test
	public void testSetValueString(){
		label.setValueString("testing");
	}
	

}
