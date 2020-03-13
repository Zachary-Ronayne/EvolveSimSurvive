package evolve.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import evolve.Main;
import evolve.gui.component.SimConstants;
import evolve.gui.component.SimLabel;
import evolve.gui.frame.SimFrame;
import evolve.gui.layout.SimLayoutHandler;
import evolve.sim.Simulation;
import evolve.sim.obj.Creature;
import evolve.util.clock.ClockRenderEvent;
import evolve.util.clock.ClockUpdateEvent;
import evolve.util.clock.GameClock;
import evolve.util.input.KeyInputSim;
import evolve.util.input.MouseInputSim;

/**
 * A GUI object used for a Simulation
 */
public class SimGui extends Gui{
	
	/**
	 * The frame that this GUI object uses for display
	 */
	private SimFrame frame;
	/**
	 * The panel that this GUI uses for rendering it's graphics
	 */
	private JPanel simContainer;
	/**
	 * The label that this GUI uses to display information about the sim
	 */
	private SimLabel simInfo;
	/**
	 * The image that this object uses to keep track of its graphics before they are rendered to the GUI
	 */
	private BufferedImage buffer;

	/**
	 * The KeyAdapter used by this class for input
	 */
	private KeyInputSim keyInput;
	
	/**
	 * The MouseAdapter used by this class for input
	 */
	private MouseInputSim mouseInput;
	
	/**
	 * true if the sim is paused and should not update, false otherwise
	 */
	private boolean paused;
	
	/**
	 * Create a new SimFrame, initialized with no simulation to display
	 */
	public SimGui(GuiHandler handler){
		super(handler);
		
		paused = false;
		
		//create the frame
		frame = new SimFrame();
		frame.setVisible(false);
		addOpenCloseControls();
		
		//create a central panel for the frame
		JPanel central = new JPanel();
		SimLayoutHandler.createVerticalLayout(central);
		frame.add(central);
		
		//create the panel and buffer for the frame
		int width = Main.SETTINGS.simGuiWidth.value();
		int height = Main.SETTINGS.simGuiHeight.value();
		
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		simContainer = new JPanel(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paint(Graphics g){
				render(g);
			}
		};
		simContainer.setPreferredSize(new Dimension(width, height));
		central.add(simContainer);
		
		//add a label for displaying information
		simInfo = new SimLabel("");
		central.add(simInfo);
		
		//add the KeyAdapter
		keyInput = new KeyInputSim(getHandler());
		frame.addKeyListener(keyInput);
		
		//add the MouseAdapter
		mouseInput = new MouseInputSim(getHandler());
		simContainer.addMouseListener(mouseInput);
		simContainer.addMouseMotionListener(mouseInput);
		simContainer.addMouseWheelListener(mouseInput);
		
		//set the size of the frame based on the panel and display it at the appropriate location
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		//add a clock event to update and render the simulation
		GameClock clock = getHandler().getClock();
		clock.addUpdateEvent(new ClockUpdateEvent(){
			@Override
			public void event(){
				Simulation sim = getHandler().getSimulation();
				if(sim != null){
					if(!paused) sim.update();
					updateSimInfo();
				}
			}
		});
		updateSimInfo();
		clock.addRenderEvent(new ClockRenderEvent(){
			@Override
			public void event(){
				renderBuffer();
			}
		});
	}
	
	/**
	 * Update the buffer based on the current state of the simulation
	 */
	public void renderBuffer(){
		int width = Main.SETTINGS.simGuiWidth.value();
		int height = Main.SETTINGS.simGuiHeight.value();
		BufferedImage tempbuffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D)(tempbuffer.getGraphics());
		
		boolean isNull = getHandler().getAbsoluteSimulation() == null;
		if(isNull || getHandler().getClock().getStopUpdates()){
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, 100));
			String s;
			if(isNull) s = "No simulation loaded";
			else s = "Simulation is Looping";
			g.drawString(s, (width - g.getFontMetrics().stringWidth(s)) / 2, height / 2 - 50);
		}
		//draw the buffer
		else{
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			
			getHandler().getAbsoluteSimulation().render(g);
		}
		simContainer.repaint();
		
		this.buffer = tempbuffer;
	}
	
	/**
	 * Render the current state of the simulation and draw the buffer to the frame
	 * @param gBuff the graphics object to draw the buffer to
	 */
	private void render(Graphics gBuff){
		gBuff.drawImage(buffer, 0, 0, buffer.getWidth(), buffer.getHeight(), null);
	}
	
	/**
	 * Update the information of the simulation based on the state of the sim
	 */
	public void updateSimInfo(){
		Simulation sim = getHandler().getAbsoluteSimulation();
		GameClock clock = getHandler().getClock();
		//if the clock is stopping updates, don't update this information
		if(clock.getStopUpdates()) return;
		
		String str = "<html>3: bring up help menu. Sim is ";
		if(sim == null) str += "not loaded";
		else if(clock.getStopUpdates()) str += "running as fast as possible";
		else if(isPaused()) str += "paused";
		else str += "running";
		
		if(sim != null )str += "<br>Simulated time: " + Creature.getTimeString(sim.getTotalTime()) + "</html>";
		
		simInfo.setText(str);
	}
	
	public boolean isPaused(){
		return paused;
	}
	public void setPaused(boolean paused){
		GuiHandler handler = getHandler();
		this.paused = paused;
		if(handler == null) return;
		if(!this.paused){
			handler.setUpThreadPool();
			handler.getClock().startClock();
		}
		else{
			if(handler.getThreadPool() == null) return;
			handler.getThreadPool().end();
			handler.getThreadPool().join();
			
		}
	}
	public void togglePaused(){
		setPaused(!isPaused());
	}
	
	@Override
	public Window getFrame(){
		return frame;
	}
	
	/**
	 * Set the width of the panel that the SImulation is displayed in
	 * @param width the new width
	 */
	public void setSimWidth(int width){
		setSimSize(width, (int)simContainer.getPreferredSize().getHeight());
	}
	
	/**
	 * Set the height of the panel that the Simulation is displayed in
	 * @param height the new height
	 */
	public void setSimHeight(int height){
		setSimSize((int)simContainer.getPreferredSize().getWidth(), height);
	}

	/**
	 * Set the size of the panel that the Simulation is displayed in
	 * @param width the new width
	 * @param height the new height
	 */
	public void setSimSize(int width, int height){
		simContainer.setPreferredSize(new Dimension(width, height));
		simContainer.setSize(new Dimension(width, height));
		getHandler().getAbsoluteSimulation().getCamera().setWidth(width);
		getHandler().getAbsoluteSimulation().getCamera().setHeight(height);
		getFrame().setLocationRelativeTo(null);
		
		getFrame().pack();
	}
	
}
