package evolve.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JPanel;

import evolve.gui.component.Padding;
import evolve.gui.component.SimLabel;
import evolve.gui.frame.HelpFrame;
import evolve.gui.layout.SimLayoutHandler;

/**
 * A GUI used for displaying help information
 */
public class HelpGui extends Gui{
	
	private HelpFrame frame;
	
	public HelpGui(GuiHandler handler){
		super(handler);
		
		//create frame
		this.frame = new HelpFrame();
		this.frame.setVisible(false);
		frame.setAlwaysOnTop(true);
		addOpenCloseControls();
		
		//create central panel
		JPanel central = new JPanel();
		SimLayoutHandler.createVerticalLayout(central);
		this.frame.add(central);
		
		//create first panel for controls
		JPanel row1 = new JPanel();
		SimLayoutHandler.createHorizontalLayout(row1);
		central.add(row1);
		
		JPanel row2 = new JPanel();
		SimLayoutHandler.createHorizontalLayout(row2);
		central.add(row2);
		
		//create padding objects
		Padding linePad = new Padding(2, 2, 0, 0);
		Padding thinPad = new Padding(1, 1, 1, 1);
		
		//add text for information
		
		//create panel for showing controls
		JPanel controlsPanel = new JPanel();
		SimLayoutHandler.createVerticalLayout(controlsPanel);
		row1.add(linePad.addPadding(controlsPanel, Color.BLACK));
		//user creature controls text
		controlsPanel.add(makeTitle("User controled creature:"));
		controlsPanel.add(thinPad.addPadding(new SimLabel("Arrow up: accelerate forward")));
		controlsPanel.add(thinPad.addPadding(new SimLabel("Arrow down: accelerate backwards")));
		controlsPanel.add(thinPad.addPadding(new SimLabel("Arrow left: turn clock wise")));
		controlsPanel.add(thinPad.addPadding(new SimLabel("Arrow right: turn counter clock wise")));
		controlsPanel.add(thinPad.addPadding(new SimLabel("E: eat")));
		controlsPanel.add(thinPad.addPadding(new SimLabel("F: fight")));
		controlsPanel.add(thinPad.addPadding(new SimLabel("R: revive")));
		controlsPanel.add(thinPad.addPadding(new SimLabel("V: vomit")));
		
		//frame display panel
		JPanel frameDispPanel = new JPanel();
		SimLayoutHandler.createVerticalLayout(frameDispPanel);
		row1.add(linePad.addPadding(frameDispPanel, Color.BLACK));
		//frame display text
		frameDispPanel.add(makeTitle("Menu Controls"));
		frameDispPanel.add(thinPad.addPadding(new SimLabel("1: show/hide saving and loading menu")));
		frameDispPanel.add(thinPad.addPadding(new SimLabel("2: show/hide neural net display")));
		frameDispPanel.add(thinPad.addPadding(new SimLabel("3: show/hide this help menu")));
		frameDispPanel.add(thinPad.addPadding(new SimLabel("4: show/hide sim speed menu")));
		frameDispPanel.add(thinPad.addPadding(new SimLabel("5: show/hide settings menu")));
		frameDispPanel.add(thinPad.addPadding(new SimLabel("6: show/hide graph data")));
		frameDispPanel.add(thinPad.addPadding(new SimLabel("7: show/hide creature sorting menu")));
		frameDispPanel.add(thinPad.addPadding(new SimLabel("F2: close all extra windows")));
		
		//misc controls panel
		JPanel miscControlsPanel = new JPanel();
		SimLayoutHandler.createVerticalLayout(miscControlsPanel);
		row2.add(linePad.addPadding(miscControlsPanel, Color.BLACK));
		//misc controls text
		miscControlsPanel.add(makeTitle("Misc controls"));
		miscControlsPanel.add(thinPad.addPadding(new SimLabel("space: center camera on")));
		miscControlsPanel.add(thinPad.addPadding(new SimLabel("user creature/selected creature")));
		miscControlsPanel.add(thinPad.addPadding(new SimLabel("P: pause/unpause the sim")));
		miscControlsPanel.add(thinPad.addPadding(new SimLabel("T: Lock camera to user creature")));

		//panning controls panel
		JPanel panControlsPanel = new JPanel();
		SimLayoutHandler.createVerticalLayout(panControlsPanel);
		row2.add(linePad.addPadding(panControlsPanel, Color.BLACK));
		//panning controls text
		panControlsPanel.add(makeTitle("Camera controls"));
		panControlsPanel.add(thinPad.addPadding(new SimLabel("left click: select creature")));
		panControlsPanel.add(thinPad.addPadding(new SimLabel("right click: hold and drag to pan")));
		panControlsPanel.add(thinPad.addPadding(new SimLabel("mosue wheel up/down: zoom in/out")));
		panControlsPanel.add(makeTitle("Graph controls"));
		panControlsPanel.add(thinPad.addPadding(new SimLabel("left click: reset position")));
		panControlsPanel.add(thinPad.addPadding(new SimLabel("right click hold and drag to pan")));
		panControlsPanel.add(thinPad.addPadding(new SimLabel("mouse wheel up/down: zoom in/out")));
		panControlsPanel.add(thinPad.addPadding(new SimLabel("shift/ctrl + mouse wheel: zoom only x/y axis")));
		
		frame.pack();
	}
	
	/**
	 * Create a SimLabel that has the given text as a title
	 * @param text the text
	 * @return the SimLabel
	 */
	public SimLabel makeTitle(String text){
		SimLabel title = new SimLabel(text);
		Font font = title.getFont();
		font = new Font(font.getName(), Font.BOLD, font.getSize());
		title.setFont(font);
		return title;
	}

	@Override
	public Window getFrame(){
		return frame;
	}
	
}
