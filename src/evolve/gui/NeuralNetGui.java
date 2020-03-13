package evolve.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;

import javax.swing.JPanel;

import evolve.Main;
import evolve.gui.component.Padding;
import evolve.gui.component.SimConstants;
import evolve.gui.component.neuralNet.NeuralNetGuiLabel;
import evolve.gui.component.neuralNet.NeuralNetGuiStringLabel;
import evolve.gui.frame.NeuralNetFrame;
import evolve.gui.layout.SimLayoutHandler;
import evolve.sim.Simulation;
import evolve.sim.misc.Eye;
import evolve.sim.neuralNet.NeuralNet;
import evolve.sim.obj.Creature;
import evolve.sim.obj.NeuralNetCreature;
import evolve.util.Camera;
import evolve.util.clock.ClockRenderEvent;
import evolve.util.clock.ClockUpdateEvent;
import evolve.util.clock.GameClock;

public class NeuralNetGui extends Gui{
	
	/**
	 * The width of the amount of space an eye display column takes up
	 */
	public static final int EYE_DISP_WIDTH = 30;

	/**
	 * The width of the memory display
	 */
	public static final int MEMORY_DISP_WIDTH = 108;
	/**
	 * The height of a single slice of the memory display
	 */
	public static final int MEMORY_DISP_SLICE = 30;
	
	/**
	 * The height of the info display
	 */
	public static final int INFO_DISP_HEIGHT = 920;
	
	/**
	 * The frame this GUI uses for display
	 */
	private NeuralNetFrame frame;
	
	/**
	 * The creature that should be displayed on this GUI, keep null to display none
	 */
	private NeuralNetCreature creature;
	
	/**
	 * The simulation that this GUI is using to display
	 */
	private Simulation simulation;
	
	/**
	 * The camera use to draw the neural net
	 */
	private Camera camera;
	
	/**
	 * The neural net being displayed by this GUI
	 */
	private JPanel neuralNetDisplay;
	
	/**
	 * The panel that keeps track of the display for the eyes of the selected creature
	 */
	private JPanel eyesDisplay;
	
	/**
	 * The panel that keeps track of the display for the memory nodes of the selected creature
	 */
	private JPanel memoryDisplay;
	
	/**
	 * The labels that show the states of the selected creature
	 */
	private NeuralNetGuiLabel[] labels;
	
	/**
	 * The number of labels that the GUI has
	 */
	public static final int NUM_LABELS = 13;
	
	/**
	 * The index of the label showing the id and parent ids of the creature
	 */
	public static final int ID_LABEL = 0;
	/**
	 * The index of the label showing how much energy the creature has
	 */
	public static final int ENERGY_LABEL = 1;
	/**
	 * The index of the label showing the temperature of the creature
	 */
	public static final int TEMPERATURE_LABEL = 2;
	/**
	 * The index of the label showing the amount of fur the creature has
	 */
	public static final int FUR_LABEL = 3;
	/**
	 * The index of the label showing the speed of the creature
	 */
	public static final int SPEED_LABEL = 4;
	/**
	 * The index of the label showing the size gene of the creature
	 */
	public static final int SIZE_GENE_LABEL = 5;
	/**
	 * The index of the label showing the angle of the creature, in degrees
	 */
	public static final int ANGLE_LABEL = 6;
	/**
	 * The index of the label showing the mutability of the creature
	 */
	public static final int MUTABILITY_LABEL = 7;
	/**
	 * The index of the label showing the age of the creature
	 */
	public static final int AGE_LABEL = 8;
	/**
	 * The index of the label showing the generation of the creature
	 */
	public static final int GENERATION_LABEL = 9;
	/**
	 * The index of the label showing the number of children the creature has
	 */
	public static final int CHILDREN_LABEL = 10;
	/**
	 * The index of the label showing the amount of energy the creature gives to its children
	 */
	public static final int BABY_ENERGY_LABEL = 11;
	/**
	 * The index of the label showing the amount of updates until the creature is able to give birth
	 */
	public static final int BABY_TIMER_LABEL = 12;
	
	public NeuralNetGui(GuiHandler handler){
		super(handler);
		
		simulation = getHandler().getSimulation();
		
		creature = null;
		camera = new Camera(1, 1);
		camera.setX(-NeuralNet.NODE_RADIUS);
		camera.setY(-NeuralNet.NODE_RADIUS);
		
		//create the frame
		frame = new NeuralNetFrame();
		addOpenCloseControls();
		
		//central panel to hold the components
		JPanel central = new JPanel();
		SimLayoutHandler.createHorizontalLayout(central);
		frame.add(central);
		
		//padding variable
		Padding pad = new Padding(5, 5, 5, 5);

		//create a JPanel that displays all the information about the creature
		JPanel info = new JPanel();
		SimLayoutHandler.createVerticalLayout(info);
		info.setPreferredSize(new Dimension(300, INFO_DISP_HEIGHT));
		
		//create labels for the information
		labels = new NeuralNetGuiLabel[NUM_LABELS];
		
		labels[ID_LABEL] = new NeuralNetGuiLabel("Id/FatherId/MotherId", true);
		labels[ENERGY_LABEL] = new NeuralNetGuiLabel("Energy/MaxEnergy", false);
		labels[TEMPERATURE_LABEL] = new NeuralNetGuiLabel("Temperature/comfort temperature", false);
		labels[FUR_LABEL] = new NeuralNetGuiLabel("Fur", false);
		labels[SPEED_LABEL] = new NeuralNetGuiLabel("Speed", false);
		labels[SIZE_GENE_LABEL] = new NeuralNetGuiLabel("Size Gene", false);
		labels[ANGLE_LABEL] = new NeuralNetGuiLabel("Angle", false);
		labels[MUTABILITY_LABEL] = new NeuralNetGuiLabel("Mutability", false);
		labels[AGE_LABEL] = new NeuralNetGuiStringLabel("Age", "");
		labels[GENERATION_LABEL] = new NeuralNetGuiLabel("Generation", true);
		labels[CHILDREN_LABEL] = new NeuralNetGuiLabel("Children Breed/Asexual", true);
		labels[BABY_ENERGY_LABEL] = new NeuralNetGuiLabel("Energy to children", false);
		labels[BABY_TIMER_LABEL] = new NeuralNetGuiLabel("Time until able to have baby", true);
		
		for(NeuralNetGuiLabel l : labels) info.add(l);
		
		//create a JPanel that displays the neural network of the selected creature
		neuralNetDisplay = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g){
				//draw the creature
				if(creature != null){
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, getWidth(), getHeight());
					
					camera.setG((Graphics2D)g);
					creature.getBrain().render(camera);
				}
				//draw a message stating that no creature is selected
				else{
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, getWidth(), getHeight());
					
					g.setColor(Color.WHITE);
					g.setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, 30));
					g.drawString("No Selected Creature", 10, 60);
				}
			}
		};
		SimLayoutHandler.createHorizontalLayout(neuralNetDisplay);
		
		//set up the eyes display
		eyesDisplay = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g){
				drawEyeDisplay((Graphics2D)g);
			}
		};
		
		//set up the memory display
		memoryDisplay = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g){
				drawMemoryDisplay((Graphics2D)g);
			}
		};
		Dimension size = new Dimension(MEMORY_DISP_WIDTH, MEMORY_DISP_SLICE * NeuralNetCreature.MEMORY_NODES);
		memoryDisplay.setPreferredSize(size);
		memoryDisplay.setSize(size);
		
		//set up panel for other graphics stuff
		JPanel extraGraphics = new JPanel();
		SimLayoutHandler.createHorizontalLayout(extraGraphics);
		extraGraphics.add(eyesDisplay);
		extraGraphics.add(memoryDisplay);
		
		//add the panels to the central panel
		central.add(pad.addPadding(neuralNetDisplay, Color.WHITE));
		central.add(pad.addPadding(info));
		central.add(pad.addPadding(extraGraphics));
		
		updateNeuralNetDisplay();

		frame.pack();
		
		//add an event to the simGui clock to update the display
		GameClock clock = getHandler().getClock();
		clock.addUpdateEvent(new ClockUpdateEvent(){
			@Override
			public void event(){
				//only update this menu if the frame is showing
				if(!getFrame().isVisible()) return;
				
				Simulation testSim = getHandler().getSimulation();
				if(getSimulation() != null && testSim != null && !getSimulation().equals(testSim)){
					setSimulation(testSim);
					setSelectedCreature(null);
				}
				
				if(testSim != null){
					NeuralNetCreature selectedGlow = testSim.getSelectedGlowCreature();
					NeuralNetCreature selectedCreature = getSelectedCreature();
					if(selectedCreature == null && selectedGlow != null ||
						selectedCreature != null && selectedGlow != null && !selectedCreature.equals(selectedGlow)){
						
						setSelectedCreature(selectedGlow);
					}
				}
				
				if(getSelectedCreature() != null && getSelectedCreature().isDead()) setSelectedCreature(null);
			}
		});
		clock.addRenderEvent(new ClockRenderEvent(){
			@Override
			public void event(){
				//only render this menu if the frame is showing
				if(!getFrame().isVisible()) return;
				
				updateNeuralNetDisplay();
			}
		});
	}
	
	/**
	 * Get the creature that this GUI is displaying information for
	 * @return the creature
	 */
	public NeuralNetCreature getSelectedCreature(){
		return creature;
	}
	
	/**
	 * Set the selected creature to the given creature
	 * @param creature
	 */
	public void setSelectedCreature(NeuralNetCreature creature){
		if(this.creature == null || creature == null || !this.creature.equals(creature)){
			this.creature = creature;
			if(this.creature != null){
				getHandler().getSimulation().setSelectedCreatureGlow(this.creature);
			}
		}
	}
	
	/**
	 * Update the state of the JPanel that draw the NeuralNet and eye visualization of the current creature
	 */
	public void updateNeuralNetDisplay(){
		int width;
		int height;

		int oldWidth = (int)neuralNetDisplay.getPreferredSize().getWidth();
		int oldHeight = (int)neuralNetDisplay.getPreferredSize().getHeight();
		
		NeuralNetCreature creature = this.creature;
		
		//determine the size of what the NeuralNetDisplay should be
		if(creature == null){
			width = 300;
			height = 100;

			for(NeuralNetGuiLabel l : labels) l.setValueString("");
		}
		else{
			NeuralNet brain = creature.getBrain();
			width = (int)((brain.getNumberOfLayers() - 1) * NeuralNet.NODE_X_DIST + NeuralNet.NODE_RADIUS * 2);
			height = (int)((brain.getLargestLayer() - 1) * NeuralNet.NODE_Y_DIST + NeuralNet.NODE_RADIUS * 2);
			
			
			labels[ID_LABEL].setValues(creature.getId(), creature.getFatherId(), creature.getMotherId());
			labels[ENERGY_LABEL].setValues(creature.getEnergy(), creature.getMaxEnergy());
			labels[TEMPERATURE_LABEL].setValues(creature.getTemperature().getTemp(), Main.SETTINGS.temperatureCreatureComfort.value());
			labels[FUR_LABEL].setValues(creature.getFur());
			labels[SPEED_LABEL].setValues(creature.getMoveSpeed());
			labels[SIZE_GENE_LABEL].setValues(creature.getSizeGene());
			labels[ANGLE_LABEL].setValues(Math.toDegrees(creature.getAngle()));
			labels[MUTABILITY_LABEL].setValues(creature.getMutability());
			labels[GENERATION_LABEL].setValues(creature.getGeneration());
			labels[CHILDREN_LABEL].setValues(creature.getBreedChildren(), creature.getAsexualChildren());
			labels[BABY_ENERGY_LABEL].setValues(creature.getEnergyToBaby());
			labels[BABY_TIMER_LABEL].setValues(creature.getBabyCooldown());
			
			String age = Creature.getTimeString(creature.getAge());
			int midIndex = age.indexOf("hours, ");
			
			//if the string wasn't found, look for the case with no s
			if(midIndex < 0){
				midIndex = age.indexOf("hour, ");
				//if the string wasn't found at all, just use index 0, meaning don't split the string
				if(midIndex < 0) midIndex = 0;
				else midIndex += 5;
			}
			else midIndex += 6;
			
			((NeuralNetGuiStringLabel)labels[AGE_LABEL]).setValue(age.substring(0, midIndex) + "<br>" + age.substring(midIndex));
		}
		
		//update the GUI based on the new height and width
		camera.setWidth(width);
		camera.setHeight(height);
		neuralNetDisplay.setPreferredSize(new Dimension(width, height));
		neuralNetDisplay.setSize(new Dimension(width, height));
		
		if(oldWidth != width || oldHeight != height){
			frame.pack();
		}
		
		//update the state of the eye display
		if(creature != null){
			int oldSize = (int)eyesDisplay.getPreferredSize().getWidth();
			//get the eyes to use for rendering
			Eye[] eyes = creature.getEyes();
			//make a dimension based on the number of eyes in the selected creature
			Dimension d = new Dimension(EYE_DISP_WIDTH * eyes.length + 2, INFO_DISP_HEIGHT);
			//update the size
			eyesDisplay.setSize(d);
			eyesDisplay.setPreferredSize(d);
			
			//pack the frame if needed
			if(oldSize != (int)d.getWidth()) frame.pack();
		}
		
		frame.repaint();
	}
	
	/**
	 * Get the simulation that this GUI is using to display
	 * @return the simulation
	 */
	public Simulation getSimulation(){
		return simulation;
	}

	/**
	 * Set the simulation that this GUI is using to display
	 * @param sim the simulation
	 */
	public void setSimulation(Simulation sim){
		this.simulation = sim;
	}
	
	@Override
	public Window getFrame(){
		return frame;
	}
	
	/**
	 * Draw the eye display based on the selected creature
	 * @param g the graphics to draw to, if this is null or the selected creature is null, nothing happens
	 */
	public void drawEyeDisplay(Graphics2D g){
		Camera cam = new Camera(0, 0);
		cam.setDrawOnlyInBounds(false);
		cam.setG(g);
		
		//ensure that the variables are valid
		NeuralNetCreature creature = getSelectedCreature();
		if(creature == null || g == null) return;
		
		Eye[] eyes = creature.getEyes();
		int width =  eyesDisplay.getWidth();
		int height = eyesDisplay.getHeight() / 2;
		int size = EYE_DISP_WIDTH;
		
		//draw a background
		g.setColor(new Color(100, 100, 100));
		cam.fillRect(0, 0, width, height);
		
		//draw each eye bar
		for(int i = 0; i < eyes.length; i++){
			Eye e = eyes[i];
			double maxAngle = Math.PI * 2;
			double maxDist = Main.SETTINGS.eyeDistanceMax.value() - Main.SETTINGS.eyeDistanceMin.value();
			
			//variables for sizes of bars
			int x = 2 + i * size;
			int y = 2;
			int w = size - 2;
			int w0 = w / 2;
			int w1 = w - w0;
			int h = height - 4;
			
			//draw the background of the bar
			g.setColor(Color.BLACK);
			cam.fillRect(x, y, w, h);
			
			
			//draw the angle and distance bars representing the range the angle and distance of the eyes can take
			
			//draw the angle bar
			g.setColor(new Color(0, 0, 200));
			double max = e.getMaxAngle().getValue() + Math.PI;
			double min = e.getMinAngle().getValue() + Math.PI;
			cam.fillRect(
					x + 1,	(maxAngle - max) / maxAngle * h,
					w0 - 1,	(max - min) / maxAngle * h
			);
			
			//draw the distance bar
			g.setColor(new Color(0, 200, 0));
			max = e.getMaxDistance().getValue() - Main.SETTINGS.eyeDistanceMin.value();
			min = e.getMinDistance().getValue() - Main.SETTINGS.eyeDistanceMin.value();
			cam.fillRect(
					x + w0,	(maxDist - max) / maxDist * h,
					w1 - 1,	(max - min) / maxDist * h
			);

			
			//draw a line showing the current level of the angle and distance
			
			//draw the angle label
			g.setColor(new Color(100, 100, 255));
			double angle = e.getAngle() + Math.PI;
			cam.fillRect(
					x - 2, -2 + (maxAngle - angle) / maxAngle * h,
					w0 + 4, 4);
			
			//draw the distance label
			g.setColor(new Color(100, 255, 100));
			double distance = e.getDistance() - Main.SETTINGS.eyeDistanceMin.value();
			cam.fillRect(
					x + w0 - 2,	-2 + (maxDist - distance) / maxDist * h,
					w1 + 4, 4);
		}
	}
	/**
	 * Draw the memory display based on the selected creature
	 * @param g the graphics to draw to, if this is null or the selected creature is null, nothing happens
	 */
	public void drawMemoryDisplay(Graphics2D g){
		Camera cam = new Camera(0, 0);
		cam.setDrawOnlyInBounds(false);
		cam.setG(g);
		
		//ensure that the variables are valid
		NeuralNetCreature creature = getSelectedCreature();
		if(creature == null || g == null) return;
		
		double[] outputs = creature.getBrainOutputs();
		
		
		//draw the memory display
		
		//background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, MEMORY_DISP_WIDTH, MEMORY_DISP_SLICE * NeuralNetCreature.MEMORY_NODES);
		
		//memory cells
		for(int i = 0; i < NeuralNetCreature.MEMORY_NODES; i++){
			double decide = outputs[outputs.length - 1 - i * 2];
			double x = 2;
			double y = 2 + MEMORY_DISP_SLICE * i;
			g.setColor(NeuralNet.getValueColor(decide, false));
			cam.fillRect(x, y, 50, MEMORY_DISP_SLICE - 2);
			g.setColor(Color.BLACK);
			g.setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, 20));
			String s;
			if(decide > 0) s = "?";
			else s = "|";
			cam.drawString(s, x, y + 20);

			x = 54;
			g.setColor(NeuralNet.getValueColor(outputs[outputs.length - 2 - i * 2], false));
			cam.fillRect(x, y, 50, MEMORY_DISP_SLICE - 2);
			g.setColor(Color.BLACK);
			g.setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, 20));
			cam.drawString("Value", x, y + 20);
		}
	}
	
}
