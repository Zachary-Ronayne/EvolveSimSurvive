package gui.layout;

import javax.swing.JPanel;

import org.junit.Test;

import evolve.gui.layout.SimLayoutHandler;

public class SimLayoutHandlerTest{
	
	@Test
	public void testCreateVerticalLayout(){
		SimLayoutHandler.createVerticalLayout(new JPanel());
	}

	@Test
	public void testCreateHorizontalLayout(){
		SimLayoutHandler.createHorizontalLayout(new JPanel());
	}
	@Test
	public void testCreateGridlLayout(){
		SimLayoutHandler.createGridlLayout(new JPanel(), 2, 2);
	}
	
}
