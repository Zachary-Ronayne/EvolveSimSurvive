package gui.component.neuralNet;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.gui.component.neuralNet.NeuralNetGuiStringLabel;

public class NeuralNetGuiStringLabelTest{

	private NeuralNetGuiStringLabel label;
	
	@Before
	public void setUp(){
		label = new NeuralNetGuiStringLabel("title1", "value1");
	}
	
	@Test
	public void getValue(){
		assertTrue(label.getValue().equals("value1"));
	}

	@Test
	public void setValue(){
		label.setValue("value2");
		assertTrue(label.getValue().equals("value2"));
	}

	@Test
	public void updateValueLabel(){
		label.updateNameLabel();
	}
	
}
