package evolve.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import evolve.Main;
import evolve.gui.component.Padding;
import evolve.gui.component.SimButton;
import evolve.gui.component.SimLabel;
import evolve.gui.component.settings.BooleanBoxSetting;
import evolve.gui.component.settings.NumberBoxSetting;
import evolve.gui.component.settings.SettingsComponent;
import evolve.gui.component.settings.TextBoxSetting;
import evolve.gui.frame.SettingsFrame;
import evolve.gui.layout.SimLayoutHandler;
import evolve.sim.Simulation;
import evolve.util.clock.ClockUpdateEvent;
import evolve.util.clock.GameClock;

public class SettingsGui extends Gui{
	
	/**
	 * the number of pages of settings
	 */
	public static final int NUMBER_SEETINGS_PAGES = 7;
	
	/**
	 * The frame that this GUI uses
	 */
	private SettingsFrame frame;
	
	/**
	 * The label that holds the description
	 */
	private SimLabel descriptionLabel;
	
	private ArrayList<SettingsComponent> settingsComponents;
	
	/**
	 * The JPanel that holds the settings column of the current settings to display
	 */
	private JPanel selectedSettingsPage;
	
	/**
	 * All of the columns that hold all the SettingsComponents on each page
	 */
	private JPanel[] dataColumms;
	
	/**
	 * The current page that is displayed
	 */
	private int currentPage;
	
	/**
	 * The timer that keeps track of how long the message about an action is displayed
	 */
	private int messageTimer;
	
	/**
	 * The label that shows the message of this GUI
	 */
	private SimLabel messageLabel;
	
	public SettingsGui(GuiHandler handler){
		super(handler);
		
		settingsComponents = new ArrayList<SettingsComponent>();
		
		//set up frame
		frame = new SettingsFrame();
		
		//set up central panel
		JPanel central = new JPanel();
		SimLayoutHandler.createVerticalLayout(central);
		Padding centralPad = new Padding(2, 2, 2, 2);
		frame.add(centralPad.addPadding(central));

		//add a title
		SimLabel mainTitle = new SimLabel("Settings");
		mainTitle.setBackground(Color.WHITE);
		mainTitle.setFontSize(40);
		central.add(mainTitle);
		JPanel titleLine = new JPanel();
		titleLine.setPreferredSize(new Dimension(mainTitle.getWidth(), 2));
		titleLine.setBackground(Color.BLACK);
		central.add(titleLine);
		
		//set up panel for settings columns
		JPanel settingColumns = new JPanel();
		SimLayoutHandler.createHorizontalLayout(settingColumns);
		central.add(settingColumns);
		dataColumms = new JPanel[NUMBER_SEETINGS_PAGES];
		for(int i = 0; i < dataColumms.length; i++){
			dataColumms[i] = new JPanel();
			SimLayoutHandler.createVerticalLayout(dataColumms[i]);
		}
		
		//add page 0 as the initially loaded page
		selectedSettingsPage = new JPanel();
		SimLayoutHandler.createVerticalLayout(selectedSettingsPage);
		settingColumns.add(selectedSettingsPage);
		currentPage = 0;
		setSelectedPageNumber(currentPage);
		
		
		//set up panel for creature settings
		JPanel creatureSettings = new JPanel();
		SimLayoutHandler.createVerticalLayout(creatureSettings);
		dataColumms[0].add(creatureSettings);
		creatureSettings.add(new SimLabel("Creature Settings"));
		JPanel creatureSettingsGrid = new JPanel();
		SimLayoutHandler.createGridlLayout(creatureSettingsGrid, 0, 1);
		creatureSettings.add(creatureSettingsGrid);
		
		//add each of the creature settings
		//creature angle change
		NumberBoxSetting creatureAngleChange = new NumberBoxSetting(Main.SETTINGS.creatureAngleChange, this, false, 0, Math.PI * 2);
		creatureSettingsGrid.add(creatureAngleChange);
		settingsComponents.add(creatureAngleChange);
		
		//creature speed change
		NumberBoxSetting creatureSpeedChange = new NumberBoxSetting(Main.SETTINGS.creatureSpeedChange, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureSpeedChange);
		settingsComponents.add(creatureSpeedChange);
		
		//creature max speed
		NumberBoxSetting creatureMaxSpeed = new NumberBoxSetting(Main.SETTINGS.creatureMaxSpeed, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureMaxSpeed);
		settingsComponents.add(creatureMaxSpeed);
		
		//creature energy speed scalar
		NumberBoxSetting creatureEnergySpeedScalar = new NumberBoxSetting(Main.SETTINGS.creatureEnergySpeedScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureEnergySpeedScalar);
		settingsComponents.add(creatureEnergySpeedScalar);
		
		//creature size slow scalar
		NumberBoxSetting creatureSizeSlowScalar = new NumberBoxSetting(Main.SETTINGS.creatureSizeSlowScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureSizeSlowScalar);
		settingsComponents.add(creatureSizeSlowScalar);
		
		//creature reverse ratio
		NumberBoxSetting creatureReverseRatio = new NumberBoxSetting(Main.SETTINGS.creatureReverseRatio, this, false, 0, 1);
		creatureSettingsGrid.add(creatureReverseRatio);
		settingsComponents.add(creatureReverseRatio);
		
		//creature min radius
		NumberBoxSetting creatureMinRadius = new NumberBoxSetting(Main.SETTINGS.creatureMinRadius, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureMinRadius);
		settingsComponents.add(creatureMinRadius);
		
		//creature min size gene
		NumberBoxSetting creatureMinSize = new NumberBoxSetting(Main.SETTINGS.creatureMinSize, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureMinSize);
		settingsComponents.add(creatureMinSize);

		//creature max size gene
		NumberBoxSetting creatureMaxSize = new NumberBoxSetting(Main.SETTINGS.creatureMaxSize, this, false, 1, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureMaxSize);
		settingsComponents.add(creatureMaxSize);
		
		//creature size scalar
		NumberBoxSetting creatureSizeScalar = new NumberBoxSetting(Main.SETTINGS.creatureSizeScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureSizeScalar);
		settingsComponents.add(creatureSizeScalar);
		
		//creature size age scalar
		NumberBoxSetting creatureSizeAgeScalar = new NumberBoxSetting(Main.SETTINGS.creatureSizeAgeScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureSizeAgeScalar);
		settingsComponents.add(creatureSizeAgeScalar);
		
		//creature size max energy scalar
		NumberBoxSetting creatureSizeMaxEnergyScalar = new NumberBoxSetting(Main.SETTINGS.creatureSizeMaxEnergyScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureSizeMaxEnergyScalar);
		settingsComponents.add(creatureSizeMaxEnergyScalar);
		
		//creature breed scalar
		NumberBoxSetting creatureSpeciesGeneScalar = new NumberBoxSetting(Main.SETTINGS.creatureSpeciesGeneScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureSpeciesGeneScalar);
		settingsComponents.add(creatureSpeciesGeneScalar);
		
		//creature breed range
		NumberBoxSetting creatureBreedRange = new NumberBoxSetting(Main.SETTINGS.creatureBreedRange, this, false, 0, 1);
		creatureSettingsGrid.add(creatureBreedRange);
		settingsComponents.add(creatureBreedRange);
		
		//creature eat cost
		NumberBoxSetting creatureEatCost = new NumberBoxSetting(Main.SETTINGS.creatureEatCost, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureEatCost);
		settingsComponents.add(creatureEatCost);
		
		//creature energy loss
		NumberBoxSetting creatureEnergyLoss = new NumberBoxSetting(Main.SETTINGS.creatureEnergyLoss, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureEnergyLoss);
		settingsComponents.add(creatureEnergyLoss);

		//creature move cost
		NumberBoxSetting creatureMoveCost = new NumberBoxSetting(Main.SETTINGS.creatureMoveCost, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureMoveCost);
		settingsComponents.add(creatureMoveCost);

		//creature max energy
		NumberBoxSetting creatureMaxEnergy = new NumberBoxSetting(Main.SETTINGS.creatureMaxEnergy, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureMaxEnergy);
		settingsComponents.add(creatureMaxEnergy);

		//creature max energy
		NumberBoxSetting creatureMaxEnergyScalar = new NumberBoxSetting(Main.SETTINGS.creatureMaxEnergyScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureMaxEnergyScalar);
		settingsComponents.add(creatureMaxEnergyScalar);
		
		//creature sick amount
		NumberBoxSetting creatureSickAmountScalar = new NumberBoxSetting(Main.SETTINGS.creatureSickAmountScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureSickAmountScalar);
		settingsComponents.add(creatureSickAmountScalar);
		
		//creature sick scalar
		NumberBoxSetting creatureSickScalar = new NumberBoxSetting(Main.SETTINGS.creatureSickScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureSickScalar);
		settingsComponents.add(creatureSickScalar);

		//creature min baby energy
		NumberBoxSetting creatureMinBabyEnergy = new NumberBoxSetting(Main.SETTINGS.creatureMinBabyEnergy, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureMinBabyEnergy);
		settingsComponents.add(creatureMinBabyEnergy);

		//creature max baby energy
		NumberBoxSetting creatureMaxBabyEnergy = new NumberBoxSetting(Main.SETTINGS.creatureMaxBabyEnergy, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureMaxBabyEnergy);
		settingsComponents.add(creatureMaxBabyEnergy);
		
		//creature baby energy asexual
		NumberBoxSetting creatureBabyEnergyAsexual = new NumberBoxSetting(Main.SETTINGS.creatureBabyEnergyAsexual, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureBabyEnergyAsexual);
		settingsComponents.add(creatureBabyEnergyAsexual);
		
		//creature baby energy scalar
		NumberBoxSetting creatureBabyEnergyScalar = new NumberBoxSetting(Main.SETTINGS.creatureBabyEnergyScalar, this, false, 0, Double.MAX_VALUE);
		creatureSettingsGrid.add(creatureBabyEnergyScalar);
		settingsComponents.add(creatureBabyEnergyScalar);

		//creature baby cooldown
		NumberBoxSetting creatureBabyCooldown = new NumberBoxSetting(Main.SETTINGS.creatureBabyCooldown, this, true, 0, Integer.MAX_VALUE);
		creatureSettingsGrid.add(creatureBabyCooldown);
		settingsComponents.add(creatureBabyCooldown);
		
		//creature breed time energy
		NumberBoxSetting creatureBreedTime = new NumberBoxSetting(Main.SETTINGS.creatureBreedTime, this, true, 0, Integer.MAX_VALUE);
		creatureSettingsGrid.add(creatureBreedTime);
		settingsComponents.add(creatureBreedTime);
		
		
		//set up panel for tile settings
		JPanel tileSettings = new JPanel();
		SimLayoutHandler.createVerticalLayout(tileSettings);
		dataColumms[1].add(tileSettings);
		tileSettings.add(new SimLabel("Tile Settings"));
		JPanel tileSettingsGrid = new JPanel();
		SimLayoutHandler.createGridlLayout(tileSettingsGrid, 0, 1);
		tileSettings.add(tileSettingsGrid);
		
		//add each of the panel settings
		//tile size
		NumberBoxSetting tileSize = new NumberBoxSetting(Main.SETTINGS.tileSize, this, false, 10, Integer.MAX_VALUE);
		tileSettingsGrid.add(tileSize);
		settingsComponents.add(tileSize);
		
		//tiles x
		NumberBoxSetting tilesX = new NumberBoxSetting(Main.SETTINGS.tilesX, this, true, 1, 1000){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean setSetting(){
				Simulation sim = getHandler().getSimulation();
				if(super.setSetting() && sim != null){
					sim.setGridSize((int)getSetting().value(), sim.getGrid()[0].length);
					updateTitleValue();
					return true;
				}
				return false;
			}
		};
		tileSettingsGrid.add(tilesX);
		settingsComponents.add(tilesX);
		
		//tiles y
		NumberBoxSetting tilesY = new NumberBoxSetting(Main.SETTINGS.tilesY, this, true, 1, 1000){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean setSetting(){
				Simulation sim = getHandler().getSimulation();
				if(super.setSetting() && sim != null){
					sim.setGridSize(sim.getGrid().length, (int)getSetting().value());
					updateTitleValue();
					return true;
				}
				return false;
			}
		};
		tileSettingsGrid.add(tilesY);
		settingsComponents.add(tilesY);

		//tile species scalar
		NumberBoxSetting tileSpeciesScalar = new NumberBoxSetting(Main.SETTINGS.tileSpeciesScalar, this, false, 0, Double.MAX_VALUE);
		tileSettingsGrid.add(tileSpeciesScalar);
		settingsComponents.add(tileSpeciesScalar);
		
		//tile water damage
		NumberBoxSetting tileWaterDamage = new NumberBoxSetting(Main.SETTINGS.tileWaterDamage, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		tileSettingsGrid.add(tileWaterDamage);
		settingsComponents.add(tileWaterDamage);
		
		//food max
		NumberBoxSetting foodMax = new NumberBoxSetting(Main.SETTINGS.foodMax, this, false, Double.MIN_VALUE, Double.MAX_VALUE);
		tileSettingsGrid.add(foodMax);
		settingsComponents.add(foodMax);
		
		//food eat percent
		NumberBoxSetting foodEatPercent = new NumberBoxSetting(Main.SETTINGS.foodEatPercent, this, false, 0, 1);
		tileSettingsGrid.add(foodEatPercent);
		settingsComponents.add(foodEatPercent);
		
		
		//set up panel for neural net settings
		JPanel neuralNetSettings = new JPanel();
		SimLayoutHandler.createVerticalLayout(neuralNetSettings);
		dataColumms[1].add(neuralNetSettings);
		neuralNetSettings.add(new SimLabel("Neural Net Settings"));
		JPanel neuralNetSettingsGrid = new JPanel();
		SimLayoutHandler.createGridlLayout(neuralNetSettingsGrid, 0, 1);
		neuralNetSettings.add(neuralNetSettingsGrid);
		
		//add each of the neural net settings
		//max mutability
		NumberBoxSetting mutabilityMax = new NumberBoxSetting(Main.SETTINGS.mutabilityMax, this, false, 0, Double.MAX_VALUE);
		neuralNetSettingsGrid.add(mutabilityMax);
		settingsComponents.add(mutabilityMax);
		
		//mutability scalar
		NumberBoxSetting mutabilityScalar = new NumberBoxSetting(Main.SETTINGS.mutabilityScalar, this, false, 0, Double.MAX_VALUE);
		neuralNetSettingsGrid.add(mutabilityScalar);
		settingsComponents.add(mutabilityScalar);

		//mutability change
		NumberBoxSetting mutabilityChange = new NumberBoxSetting(Main.SETTINGS.mutabilityChange, this, false, 0, Double.MAX_VALUE);
		neuralNetSettingsGrid.add(mutabilityChange);
		settingsComponents.add(mutabilityChange);

		//brain mutation scalar
		NumberBoxSetting brainMutationScalar = new NumberBoxSetting(Main.SETTINGS.brainMutationScalar, this, false, 0, Double.MAX_VALUE);
		neuralNetSettingsGrid.add(brainMutationScalar);
		settingsComponents.add(brainMutationScalar);
		
		//brain size
		TextBoxSetting brainSize = new TextBoxSetting(Main.SETTINGS.brainSize, this);
		neuralNetSettingsGrid.add(brainSize);
		settingsComponents.add(brainSize);
		
		//randomize chance
		NumberBoxSetting randomizeChance = new NumberBoxSetting(Main.SETTINGS.randomizeChance, this, false, 0, 1);
		neuralNetSettingsGrid.add(randomizeChance);
		settingsComponents.add(randomizeChance);
		
		//set up panel for misc settings
		JPanel miscSettings = new JPanel();
		SimLayoutHandler.createVerticalLayout(miscSettings);
		dataColumms[1].add(miscSettings);
		miscSettings.add(new SimLabel("Misc Settings"));
		JPanel miscSettingsGrid = new JPanel();
		SimLayoutHandler.createGridlLayout(miscSettingsGrid, 0, 1);
		miscSettings.add(miscSettingsGrid);
		
		//add each of the misc settings
		//sim gui width
		NumberBoxSetting simGuiWidth = new NumberBoxSetting(Main.SETTINGS.simGuiWidth, this, true, 200, Integer.MAX_VALUE){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean setSetting(){
				Simulation sim = getHandler().getSimulation();
				if(super.setSetting() && sim != null){
					getHandler().getSimGui().setSimWidth((int)getSetting().value());
					updateTitleValue();
					return true;
				}
				return false;
			}
		};
		miscSettingsGrid.add(simGuiWidth);
		settingsComponents.add(simGuiWidth);
		
		//sim gui height
		NumberBoxSetting simGuiHeight = new NumberBoxSetting(Main.SETTINGS.simGuiHeight, this, true, 100, Integer.MAX_VALUE){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean setSetting(){
				Simulation sim = getHandler().getSimulation();
				if(super.setSetting() && sim != null){
					getHandler().getSimGui().setSimHeight((int)getSetting().value());
					updateTitleValue();
					return true;
				}
				return false;
			}
		};
		miscSettingsGrid.add(simGuiHeight);
		settingsComponents.add(simGuiHeight);
		
		//population graph max data
		NumberBoxSetting populationGraphMaxPoints = new NumberBoxSetting(Main.SETTINGS.populationGraphMaxPoints, this, true, Integer.MIN_VALUE, Integer.MAX_VALUE){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean setSetting(){
				getHandler().getGraphGui().setPopulationGraphMax((int)getSetting().value());
				return super.setSetting();
			}
		};
		miscSettingsGrid.add(populationGraphMaxPoints);
		settingsComponents.add(populationGraphMaxPoints);
		
		//population graph update rate
		NumberBoxSetting populationGraphUpdateRate = new NumberBoxSetting(Main.SETTINGS.populationGraphUpdateRate, this, true, 1, Integer.MAX_VALUE);
		miscSettingsGrid.add(populationGraphUpdateRate);
		settingsComponents.add(populationGraphUpdateRate);
		
		//mutability graph max data
		NumberBoxSetting mutabilityGraphMaxPoints = new NumberBoxSetting(Main.SETTINGS.mutabilityGraphMaxPoints, this, true, Integer.MIN_VALUE, Integer.MAX_VALUE){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean setSetting(){
				getHandler().getGraphGui().setMutabilityGraphMax((int)getSetting().value());
				return super.setSetting();
			}
		};
		miscSettingsGrid.add(mutabilityGraphMaxPoints);
		settingsComponents.add(mutabilityGraphMaxPoints);
		
		//mutability graph update rate
		NumberBoxSetting mutabilityGraphUpdateRate = new NumberBoxSetting(Main.SETTINGS.mutabilityGraphUpdateRate, this, true, 1, Integer.MAX_VALUE);
		miscSettingsGrid.add(mutabilityGraphUpdateRate);
		settingsComponents.add(mutabilityGraphUpdateRate);
		
		//age graph max data
		NumberBoxSetting ageGraphMaxPoints = new NumberBoxSetting(Main.SETTINGS.ageGraphMaxPoints, this, true, Integer.MIN_VALUE, Integer.MAX_VALUE){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean setSetting(){
				getHandler().getGraphGui().setAgeGraphMax((int)getSetting().value());
				return super.setSetting();
			}
		};
		miscSettingsGrid.add(ageGraphMaxPoints);
		settingsComponents.add(ageGraphMaxPoints);
		
		//age graph update rate
		NumberBoxSetting ageGraphUpdateRate = new NumberBoxSetting(Main.SETTINGS.ageGraphUpdateRate, this, true, 1, Integer.MAX_VALUE);
		miscSettingsGrid.add(ageGraphUpdateRate);
		settingsComponents.add(ageGraphUpdateRate);
		
		//asexual reproduction
		BooleanBoxSetting asexualReproduction = new BooleanBoxSetting(Main.SETTINGS.asexualReproduction, this);
		miscSettingsGrid.add(asexualReproduction);
		settingsComponents.add(asexualReproduction);
		
		//initial creature count
		NumberBoxSetting initialCreatures = new NumberBoxSetting(Main.SETTINGS.initialCreatures, this, true, 0, Integer.MAX_VALUE);
		miscSettingsGrid.add(initialCreatures);
		settingsComponents.add(initialCreatures);
		
		//initial energy
		NumberBoxSetting initialEnergy = new NumberBoxSetting(Main.SETTINGS.initialEnergy, this, false, 0, Double.MAX_VALUE);
		miscSettingsGrid.add(initialEnergy);
		settingsComponents.add(initialEnergy);
		
		//minimum creature count
		NumberBoxSetting minimumCreatures = new NumberBoxSetting(Main.SETTINGS.minimumCreatures, this, true, 0, Integer.MAX_VALUE);
		miscSettingsGrid.add(minimumCreatures);
		settingsComponents.add(minimumCreatures);
		
		
		//set up panel for eye settings
		JPanel eyeSettings = new JPanel();
		SimLayoutHandler.createVerticalLayout(eyeSettings);
		dataColumms[2].add(eyeSettings);
		eyeSettings.add(new SimLabel("Eye Settings"));
		JPanel eyeSettingsGrid = new JPanel();
		SimLayoutHandler.createGridlLayout(eyeSettingsGrid, 0, 1);
		eyeSettings.add(eyeSettingsGrid);
		
		//add each of the misc settings

		//number of eyes min
		NumberBoxSetting eyeNumMin = new NumberBoxSetting(Main.SETTINGS.eyeNumMin, this, true, 0, Integer.MAX_VALUE);
		eyeSettingsGrid.add(eyeNumMin);
		settingsComponents.add(eyeNumMin);
		
		//number of eyes max
		NumberBoxSetting eyeNumMax = new NumberBoxSetting(Main.SETTINGS.eyeNumMax, this, true, 0, Integer.MAX_VALUE);
		eyeSettingsGrid.add(eyeNumMax);
		settingsComponents.add(eyeNumMax);
		
		//number of eyes scalar
		NumberBoxSetting eyeNumScalar = new NumberBoxSetting(Main.SETTINGS.eyeNumScalar, this, false, 0, Double.MAX_VALUE);
		eyeSettingsGrid.add(eyeNumScalar);
		settingsComponents.add(eyeNumScalar);
		
		//minimum eye distance
		NumberBoxSetting eyeDistanceMin = new NumberBoxSetting(Main.SETTINGS.eyeDistanceMin, this, false, 0, Double.MAX_VALUE);
		eyeSettingsGrid.add(eyeDistanceMin);
		settingsComponents.add(eyeDistanceMin);
		
		//maximum eye distance
		NumberBoxSetting eyeDistanceMax = new NumberBoxSetting(Main.SETTINGS.eyeDistanceMax, this, false, 0, Double.MAX_VALUE);
		eyeSettingsGrid.add(eyeDistanceMax);
		settingsComponents.add(eyeDistanceMax);
		
		//eye distance scalar
		NumberBoxSetting eyeDistanceScalar = new NumberBoxSetting(Main.SETTINGS.eyeDistanceScalar, this, false, 0, Double.MAX_VALUE);
		eyeSettingsGrid.add(eyeDistanceScalar);
		settingsComponents.add(eyeDistanceScalar);
		
		//eye angle range
		NumberBoxSetting eyeAngleRange = new NumberBoxSetting(Main.SETTINGS.eyeAngleRange, this, false, 0, Math.PI);
		eyeSettingsGrid.add(eyeAngleRange);
		settingsComponents.add(eyeAngleRange);
		
		//eye angle change
		NumberBoxSetting eyeAngleScalar = new NumberBoxSetting(Main.SETTINGS.eyeAngleScalar, this, false, 0, Double.MAX_VALUE);
		eyeSettingsGrid.add(eyeAngleScalar);
		settingsComponents.add(eyeAngleScalar);
		
		
		//set up panel for video settings
		JPanel videoSettings = new JPanel();
		SimLayoutHandler.createVerticalLayout(videoSettings);
		dataColumms[2].add(videoSettings);
		videoSettings.add(new SimLabel("Performance Settings"));
		JPanel videoSettingsGrid = new JPanel();
		SimLayoutHandler.createGridlLayout(videoSettingsGrid, 15, 1);
		videoSettings.add(videoSettingsGrid);
		
		//add each of the panel settings
		//max frame rate
		NumberBoxSetting maxFrameRate = new NumberBoxSetting(Main.SETTINGS.maxFrameRate, this, false, 0, Double.MAX_VALUE){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean setSetting(){
				boolean success = super.setSetting();
				if(success){
					getHandler().getClock().setMaxFrameRate((double)getSetting().value());
				}
				return success;
			}
		};
		videoSettingsGrid.add(maxFrameRate);
		settingsComponents.add(maxFrameRate);
		
		NumberBoxSetting numThreads = new NumberBoxSetting(Main.SETTINGS.numThreads, this, true, 0, 10){
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean setSetting(){
				//save the old setting value
				int old = (int)(getSetting().value());
				//try to change the setting and see if it was changed successfully
				boolean success = super.setSetting();
				//if it was, then update the number of threads used by the simulation
				if(success){
					//if the number of threads has changed, continued
					if(old != (int)(getSetting().value())){
						Simulation sim = getHandler().getSimulation();
						if(sim != null){
							GameClock clock = getHandler().getClock();
							clock.stopClock();
							
							int kick = 0;
							int maxKick = 1000;
							
							//wait for brief time to wait for the click to stop
							while(kick < maxKick && (clock.isRendering() || clock.isUpdating())){
								kick++;
								try{
									Thread.sleep(1);
								}catch(InterruptedException e){
									e.printStackTrace();
								}
							}
							
							//if the clock stopped in time, update the number of threads
							if(kick < maxKick) getHandler().setUpThreadPool();
							
							clock.startClock();
						}
					}
				}
				return success;
			}
		};
		videoSettingsGrid.add(numThreads);
		settingsComponents.add(numThreads);
		
		//show eyes
		BooleanBoxSetting eyeShow = new BooleanBoxSetting(Main.SETTINGS.eyeShow, this);
		videoSettingsGrid.add(eyeShow);
		settingsComponents.add(eyeShow);
		

		//Seasons settings
		JPanel seasonSettings = new JPanel();
		SimLayoutHandler.createVerticalLayout(seasonSettings);
		dataColumms[3].add(seasonSettings);
		seasonSettings.add(new SimLabel("Season/temperature Settings"));
		JPanel seasonSettingsGrid = new JPanel();
		SimLayoutHandler.createGridlLayout(seasonSettingsGrid, 20, 1);
		seasonSettings.add(seasonSettingsGrid);

		//temperature min
		NumberBoxSetting temperatureMin = new NumberBoxSetting(Main.SETTINGS.temperatureMin, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureMin);
		settingsComponents.add(temperatureMin);
		
		//temperature max
		NumberBoxSetting temperatureMax = new NumberBoxSetting(Main.SETTINGS.temperatureMax, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureMax);
		settingsComponents.add(temperatureMax);
		
		//temperature creature ratio
		NumberBoxSetting temperatureCreatureRate = new NumberBoxSetting(Main.SETTINGS.temperatureCreatureRate, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureCreatureRate);
		settingsComponents.add(temperatureCreatureRate);

		//temperature tile ratio
		NumberBoxSetting temperatureTileRate = new NumberBoxSetting(Main.SETTINGS.temperatureTileRate, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureTileRate);
		settingsComponents.add(temperatureTileRate);

		//temperature range
		NumberBoxSetting temperatureWorldRange = new NumberBoxSetting(Main.SETTINGS.temperatureWorldRange, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureWorldRange);
		settingsComponents.add(temperatureWorldRange);
		
		//temperature creature comfort
		NumberBoxSetting temperatureCreatureComfort = new NumberBoxSetting(Main.SETTINGS.temperatureCreatureComfort, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureCreatureComfort);
		settingsComponents.add(temperatureCreatureComfort);
		
		//temperature creature comfort
		NumberBoxSetting temperatureCreatureScalar = new NumberBoxSetting(Main.SETTINGS.temperatureCreatureScalar, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureCreatureScalar);
		settingsComponents.add(temperatureCreatureScalar);

		//temperature creature slow
		NumberBoxSetting temperatureCreatureSlow = new NumberBoxSetting(Main.SETTINGS.temperatureCreatureSlow, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureCreatureSlow);
		settingsComponents.add(temperatureCreatureSlow);

		//temperature creature speed
		NumberBoxSetting temperatureCreatureSpeed = new NumberBoxSetting(Main.SETTINGS.temperatureCreatureSpeed, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureCreatureSpeed);
		settingsComponents.add(temperatureCreatureSpeed);
		
		//temperature creature sick up
		NumberBoxSetting temperatureCreatureSickUp = new NumberBoxSetting(Main.SETTINGS.temperatureCreatureSickUp, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureCreatureSickUp);
		settingsComponents.add(temperatureCreatureSickUp);

		//temperature creature sick down
		NumberBoxSetting temperatureCreatureSickDown = new NumberBoxSetting(Main.SETTINGS.temperatureCreatureSickDown, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureCreatureSickDown);
		settingsComponents.add(temperatureCreatureSickDown);
		
		//temperature ice slide rate
		NumberBoxSetting temperatureIceSlideRate = new NumberBoxSetting(Main.SETTINGS.temperatureIceSlideRate, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureIceSlideRate);
		settingsComponents.add(temperatureIceSlideRate);

		//temperature ice slide decay
		NumberBoxSetting temperatureIceSlideDecay = new NumberBoxSetting(Main.SETTINGS.temperatureIceSlideDecay, this, false, 0, 1);
		seasonSettingsGrid.add(temperatureIceSlideDecay);
		settingsComponents.add(temperatureIceSlideDecay);
		
		//temperature fur max
		NumberBoxSetting temperatureFurMax = new NumberBoxSetting(Main.SETTINGS.temperatureFurMax, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureFurMax);
		settingsComponents.add(temperatureFurMax);
		
		//temperature fur scalar
		NumberBoxSetting temperatureFurScalar = new NumberBoxSetting(Main.SETTINGS.temperatureFurScalar, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureFurScalar);
		settingsComponents.add(temperatureFurScalar);
		
		//temperature fur scalar
		NumberBoxSetting temperatureFurWarmScalar = new NumberBoxSetting(Main.SETTINGS.temperatureFurWarmScalar, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureFurWarmScalar);
		settingsComponents.add(temperatureFurWarmScalar);
		
		//temperature fur scalar
		NumberBoxSetting temperatureFurSizeScalar = new NumberBoxSetting(Main.SETTINGS.temperatureFurSizeScalar, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureFurSizeScalar);
		settingsComponents.add(temperatureFurSizeScalar);
		
		//temperature fur growth rate
		NumberBoxSetting temperatureFurGrowthRate = new NumberBoxSetting(Main.SETTINGS.temperatureFurGrowthRate, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureFurGrowthRate);
		settingsComponents.add(temperatureFurGrowthRate);

		//temperature fur shed rate
		NumberBoxSetting temperatureFurShedRate = new NumberBoxSetting(Main.SETTINGS.temperatureFurShedRate, this, false, 0, Double.MAX_VALUE);
		seasonSettingsGrid.add(temperatureFurShedRate);
		settingsComponents.add(temperatureFurShedRate);
		
		//year length
		NumberBoxSetting yearLength = new NumberBoxSetting(Main.SETTINGS.yearLength, this, false, 0.01, Double.MAX_VALUE);
		seasonSettingsGrid.add(yearLength);
		settingsComponents.add(yearLength);
		
		
		//set up panel for food settings
		JPanel foodSettings = new JPanel();
		SimLayoutHandler.createVerticalLayout(foodSettings);
		dataColumms[3].add(foodSettings);
		foodSettings.add(new SimLabel("Food Settings"));
		JPanel foodSettingsGrid = new JPanel();
		SimLayoutHandler.createGridlLayout(foodSettingsGrid, 0, 1);
		foodSettings.add(foodSettingsGrid);

		//food growth
		NumberBoxSetting foodGrowTemperature = new NumberBoxSetting(Main.SETTINGS.foodGrowTemperature, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		foodSettingsGrid.add(foodGrowTemperature);
		settingsComponents.add(foodGrowTemperature);
		
		//food growth
		NumberBoxSetting foodGrowth = new NumberBoxSetting(Main.SETTINGS.foodGrowth, this, false, 0, Double.MAX_VALUE);
		foodSettingsGrid.add(foodGrowth);
		settingsComponents.add(foodGrowth);
		
		//food decay
		NumberBoxSetting foodDecay = new NumberBoxSetting(Main.SETTINGS.foodDecay, this, false, 0, Double.MAX_VALUE);
		foodSettingsGrid.add(foodDecay);
		settingsComponents.add(foodDecay);
		
		//food growth scalar
		NumberBoxSetting foodGrowthScalar = new NumberBoxSetting(Main.SETTINGS.foodGrowthScalar, this, false, 0, Double.MAX_VALUE);
		foodSettingsGrid.add(foodGrowthScalar);
		settingsComponents.add(foodGrowthScalar);
		
		//food decay scalar
		NumberBoxSetting foodDecayScalar = new NumberBoxSetting(Main.SETTINGS.foodDecayScalar, this, false, 0, Double.MAX_VALUE);
		foodSettingsGrid.add(foodDecayScalar);
		settingsComponents.add(foodDecayScalar);

		
		//set up panel for world settings1
		JPanel worldSettings1 = new JPanel();
		SimLayoutHandler.createVerticalLayout(worldSettings1);
		dataColumms[4].add(worldSettings1);
		worldSettings1.add(new SimLabel("World Settings (1)"));
		JPanel worldSettingsGrid1 = new JPanel();
		SimLayoutHandler.createGridlLayout(worldSettingsGrid1, 0, 1);
		worldSettings1.add(worldSettingsGrid1);

		//add each of the panel settings
		//world seed
		NumberBoxSetting worldSeed = new NumberBoxSetting(Main.SETTINGS.worldSeed, this, true, Integer.MIN_VALUE, Integer.MAX_VALUE){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean setSetting(){
				boolean success = super.setSetting();
				if(success && (int)getSetting().value() == 0){
					String text = (int)(Integer.MAX_VALUE * Math.random()) + "";
					if(getSetting().setStringValue(text)) getInput().setText(text);
				}
				return success;
			}
		};
		worldSettingsGrid1.add(worldSeed);
		settingsComponents.add(worldSeed);

		//world shape weight 1
		NumberBoxSetting worldShapeWeight1 = new NumberBoxSetting(Main.SETTINGS.worldShapeWeight1, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeWeight1);
		settingsComponents.add(worldShapeWeight1);
		
		//world shape weight 2
		NumberBoxSetting worldShapeWeight2 = new NumberBoxSetting(Main.SETTINGS.worldShapeWeight2, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeWeight2);
		settingsComponents.add(worldShapeWeight2);

		//world shape weight 3
		NumberBoxSetting worldShapeWeight3 = new NumberBoxSetting(Main.SETTINGS.worldShapeWeight3, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeWeight3);
		settingsComponents.add(worldShapeWeight3);
		
		//world shape weight 4
		NumberBoxSetting worldShapeWeight4 = new NumberBoxSetting(Main.SETTINGS.worldShapeWeight4, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeWeight4);
		settingsComponents.add(worldShapeWeight4);
		
		//world shape weight 5
		NumberBoxSetting worldShapeWeight5 = new NumberBoxSetting(Main.SETTINGS.worldShapeWeight5, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeWeight5);
		settingsComponents.add(worldShapeWeight5);

		//world shape weight 6
		NumberBoxSetting worldShapeWeight6 = new NumberBoxSetting(Main.SETTINGS.worldShapeWeight6, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeWeight6);
		settingsComponents.add(worldShapeWeight6);
		
		//world shape scalar 1
		NumberBoxSetting worldShapeScalar1 = new NumberBoxSetting(Main.SETTINGS.worldShapeScalar1, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeScalar1);
		settingsComponents.add(worldShapeScalar1);
		
		//world shape scalar 2
		NumberBoxSetting worldShapeScalar2 = new NumberBoxSetting(Main.SETTINGS.worldShapeScalar2, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeScalar2);
		settingsComponents.add(worldShapeScalar2);
		
		//world shape scalar 3
		NumberBoxSetting worldShapeScalar3 = new NumberBoxSetting(Main.SETTINGS.worldShapeScalar3, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeScalar3);
		settingsComponents.add(worldShapeScalar3);
		
		//world shape scalar 4
		NumberBoxSetting worldShapeScalar4 = new NumberBoxSetting(Main.SETTINGS.worldShapeScalar4, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeScalar4);
		settingsComponents.add(worldShapeScalar4);
		
		//world shape scalar 5
		NumberBoxSetting worldShapeScalar5 = new NumberBoxSetting(Main.SETTINGS.worldShapeScalar5, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeScalar5);
		settingsComponents.add(worldShapeScalar5);
		
		//world shape scalar 6
		NumberBoxSetting worldShapeScalar6 = new NumberBoxSetting(Main.SETTINGS.worldShapeScalar6, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeScalar6);
		settingsComponents.add(worldShapeScalar6);
		
		//world shape percent 1
		NumberBoxSetting worldShapePercent1 = new NumberBoxSetting(Main.SETTINGS.worldShapePercent1, this, false, 0, 1);
		worldSettingsGrid1.add(worldShapePercent1);
		settingsComponents.add(worldShapePercent1);
		
		//world shape percent 2
		NumberBoxSetting worldShapePercent2 = new NumberBoxSetting(Main.SETTINGS.worldShapePercent2, this, false, 0, 1);
		worldSettingsGrid1.add(worldShapePercent2);
		settingsComponents.add(worldShapePercent2);
		
		//world shape percent 3
		NumberBoxSetting worldShapePercent3 = new NumberBoxSetting(Main.SETTINGS.worldShapePercent3, this, false, 0, 1);
		worldSettingsGrid1.add(worldShapePercent3);
		settingsComponents.add(worldShapePercent3);
		
		//world shape percent 4
		NumberBoxSetting worldShapePercent4 = new NumberBoxSetting(Main.SETTINGS.worldShapePercent4, this, false, 0, 1);
		worldSettingsGrid1.add(worldShapePercent4);
		settingsComponents.add(worldShapePercent4);
		
		//world shape percent 5
		NumberBoxSetting worldShapePercent5 = new NumberBoxSetting(Main.SETTINGS.worldShapePercent5, this, false, 0, 1);
		worldSettingsGrid1.add(worldShapePercent5);
		settingsComponents.add(worldShapePercent5);
		
		//world shape percent 6
		NumberBoxSetting worldShapePercent6 = new NumberBoxSetting(Main.SETTINGS.worldShapePercent6, this, false, 0, 1);
		worldSettingsGrid1.add(worldShapePercent6);
		settingsComponents.add(worldShapePercent6);
		
		//world shape offset 1
		NumberBoxSetting worldShapeOffset1 = new NumberBoxSetting(Main.SETTINGS.worldShapeOffset1, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeOffset1);
		settingsComponents.add(worldShapeOffset1);
		
		//world shape offset 2
		NumberBoxSetting worldShapeOffset2 = new NumberBoxSetting(Main.SETTINGS.worldShapeOffset2, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeOffset2);
		settingsComponents.add(worldShapeOffset2);
		
		//world shape offset 3
		NumberBoxSetting worldShapeOffset3 = new NumberBoxSetting(Main.SETTINGS.worldShapeOffset3, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeOffset3);
		settingsComponents.add(worldShapeOffset3);
		
		//world shape offset 4
		NumberBoxSetting worldShapeOffset4 = new NumberBoxSetting(Main.SETTINGS.worldShapeOffset4, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeOffset4);
		settingsComponents.add(worldShapeOffset4);
		
		//world shape offset 5
		NumberBoxSetting worldShapeOffset5 = new NumberBoxSetting(Main.SETTINGS.worldShapeOffset5, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeOffset5);
		settingsComponents.add(worldShapeOffset5);

		//world shape offset 6
		NumberBoxSetting worldShapeOffset6 = new NumberBoxSetting(Main.SETTINGS.worldShapeOffset6, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldShapeOffset6);
		settingsComponents.add(worldShapeOffset6);
		
		//world scalar
		NumberBoxSetting worldScalar = new NumberBoxSetting(Main.SETTINGS.worldScalar, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldScalar);
		settingsComponents.add(worldScalar);
		
		//world offset
		NumberBoxSetting worldOffset = new NumberBoxSetting(Main.SETTINGS.worldOffset, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldOffset);
		settingsComponents.add(worldOffset);

		//world x pos
		NumberBoxSetting worldXPos = new NumberBoxSetting(Main.SETTINGS.worldXPos, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldXPos);
		settingsComponents.add(worldXPos);
		
		//world x scalar
		NumberBoxSetting worldXScalar = new NumberBoxSetting(Main.SETTINGS.worldXScalar, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldXScalar);
		settingsComponents.add(worldXScalar);

		//world y pos
		NumberBoxSetting worldYPos = new NumberBoxSetting(Main.SETTINGS.worldYPos, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldYPos);
		settingsComponents.add(worldYPos);
		
		//world y scalar
		NumberBoxSetting worldYScalar = new NumberBoxSetting(Main.SETTINGS.worldYScalar, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid1.add(worldYScalar);
		settingsComponents.add(worldYScalar);
		
		
		//set up panel for world settings2
		JPanel worldSettings2 = new JPanel();
		SimLayoutHandler.createVerticalLayout(worldSettings2);
		dataColumms[5].add(worldSettings2);
		worldSettings2.add(new SimLabel("World Settings (2)"));
		JPanel worldSettingsGrid2 = new JPanel();
		SimLayoutHandler.createGridlLayout(worldSettingsGrid2, 25, 1);
		worldSettings2.add(worldSettingsGrid2);

		//add each of the panel settings
		
		//world river count
		NumberBoxSetting worldRiverCount = new NumberBoxSetting(Main.SETTINGS.worldRiverCount, this, true, 0, Integer.MAX_VALUE);
		worldSettingsGrid2.add(worldRiverCount);
		settingsComponents.add(worldRiverCount);
		
		//world river chance
		NumberBoxSetting worldRiverChance = new NumberBoxSetting(Main.SETTINGS.worldRiverChance, this, false, 0, 1);
		worldSettingsGrid2.add(worldRiverChance);
		settingsComponents.add(worldRiverChance);
		
		//world river border size
		NumberBoxSetting worldRiverBorderSize = new NumberBoxSetting(Main.SETTINGS.worldRiverBorderSize, this, true, 0, Integer.MAX_VALUE);
		worldSettingsGrid2.add(worldRiverBorderSize);
		settingsComponents.add(worldRiverBorderSize);
		
		//world river min size
		NumberBoxSetting worldRiverMinSize = new NumberBoxSetting(Main.SETTINGS.worldRiverMinSize, this, false, 0, Double.MAX_VALUE);
		worldSettingsGrid2.add(worldRiverMinSize);
		settingsComponents.add(worldRiverMinSize);
		
		//world river max size
		NumberBoxSetting worldRiverMaxSize = new NumberBoxSetting(Main.SETTINGS.worldRiverMaxSize, this, false, 0, Double.MAX_VALUE);
		worldSettingsGrid2.add(worldRiverMaxSize);
		settingsComponents.add(worldRiverMaxSize);

		//world river noise
		NumberBoxSetting worldRiverNoise = new NumberBoxSetting(Main.SETTINGS.worldRiverNoise, this, false, 0, Double.MAX_VALUE);
		worldSettingsGrid2.add(worldRiverNoise);
		settingsComponents.add(worldRiverNoise);
		
		//world river noise scalar
		NumberBoxSetting worldRiverNoiseScalar = new NumberBoxSetting(Main.SETTINGS.worldRiverNoiseScalar, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid2.add(worldRiverNoiseScalar);
		settingsComponents.add(worldRiverNoiseScalar);
		
		//world island count
		NumberBoxSetting worldIslandCount = new NumberBoxSetting(Main.SETTINGS.worldIslandCount, this, true, 0, Integer.MAX_VALUE);
		worldSettingsGrid2.add(worldIslandCount);
		settingsComponents.add(worldIslandCount);
		
		//world island chance
		NumberBoxSetting worldIslandChance = new NumberBoxSetting(Main.SETTINGS.worldIslandChance, this, false, 0, 1);
		worldSettingsGrid2.add(worldIslandChance);
		settingsComponents.add(worldIslandChance);
		
		//world island min chance
		NumberBoxSetting worldIslandMinSize = new NumberBoxSetting(Main.SETTINGS.worldIslandMinSize, this, false, 0, Double.MAX_VALUE);
		worldSettingsGrid2.add(worldIslandMinSize);
		settingsComponents.add(worldIslandMinSize);
		
		//world island max chance
		NumberBoxSetting worldIslandMaxSize = new NumberBoxSetting(Main.SETTINGS.worldIslandMaxSize, this, false, 0, Double.MAX_VALUE);
		worldSettingsGrid2.add(worldIslandMaxSize);
		settingsComponents.add(worldIslandMaxSize);
		
		//world island border size
		NumberBoxSetting worldIslandBorderSize = new NumberBoxSetting(Main.SETTINGS.worldIslandBorderSize, this, true, 0, Integer.MAX_VALUE);
		worldSettingsGrid2.add(worldIslandBorderSize);
		settingsComponents.add(worldIslandBorderSize);

		//world island noise
		NumberBoxSetting worldIslandNoise = new NumberBoxSetting(Main.SETTINGS.worldIslandNoise, this, false, 0, Double.MAX_VALUE);
		worldSettingsGrid2.add(worldIslandNoise);
		settingsComponents.add(worldIslandNoise);

		//world island noise scalar
		NumberBoxSetting worldIslandNoiseScalar = new NumberBoxSetting(Main.SETTINGS.worldIslandNoiseScalar, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		worldSettingsGrid2.add(worldIslandNoiseScalar);
		settingsComponents.add(worldIslandNoiseScalar);
		
		//world chunk size
		NumberBoxSetting worldChunkSize = new NumberBoxSetting(Main.SETTINGS.worldChunkSize, this, true, 0, Integer.MAX_VALUE);
		worldSettingsGrid2.add(worldChunkSize);
		settingsComponents.add(worldChunkSize);
		
		
		//set up panel for fight settings
		JPanel fightSettings = new JPanel();
		SimLayoutHandler.createVerticalLayout(fightSettings);
		dataColumms[6].add(fightSettings);
		fightSettings.add(new SimLabel("Fight Settings"));
		JPanel fightSettingsGrid = new JPanel();
		SimLayoutHandler.createGridlLayout(fightSettingsGrid, 25, 1);
		fightSettings.add(fightSettingsGrid);

		//add each of the panel settings
		
		//world river count
		
		//fight drain energy
		NumberBoxSetting fightDrainEnergy = new NumberBoxSetting(Main.SETTINGS.fightDrainEnergy, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightDrainEnergy);
		settingsComponents.add(fightDrainEnergy);
		
		//fight gain heat
		NumberBoxSetting fightGainHeat = new NumberBoxSetting(Main.SETTINGS.fightGainHeat, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightGainHeat);
		settingsComponents.add(fightGainHeat);
		
		//fight base defense
		NumberBoxSetting fightBaseDefense = new NumberBoxSetting(Main.SETTINGS.fightBaseDefense, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightBaseDefense);
		settingsComponents.add(fightBaseDefense);
		
		//fight size defense
		NumberBoxSetting fightSizeDefense = new NumberBoxSetting(Main.SETTINGS.fightSizeDefense, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightSizeDefense);
		settingsComponents.add(fightSizeDefense);
		
		//fight fur defense
		NumberBoxSetting fightFurDefense = new NumberBoxSetting(Main.SETTINGS.fightFurDefense, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightFurDefense);
		settingsComponents.add(fightFurDefense);
		
		//fight base power
		NumberBoxSetting fightBasePower = new NumberBoxSetting(Main.SETTINGS.fightBasePower, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightBasePower);
		settingsComponents.add(fightBasePower);
		
		//fight size power
		NumberBoxSetting fightSizePower = new NumberBoxSetting(Main.SETTINGS.fightSizePower, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightSizePower);
		settingsComponents.add(fightSizePower);
		
		//fight temperature reduction
		NumberBoxSetting fightTemperatureReduction = new NumberBoxSetting(Main.SETTINGS.fightTemperatureReduction, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightTemperatureReduction);
		settingsComponents.add(fightTemperatureReduction);
		
		//fight gain energy ratio
		NumberBoxSetting fightGainEnergyRatio = new NumberBoxSetting(Main.SETTINGS.fightGainEnergyRatio, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightGainEnergyRatio);
		settingsComponents.add(fightGainEnergyRatio);
		
		//fight ground energy ratio
		NumberBoxSetting fightGroundEnergyRatio = new NumberBoxSetting(Main.SETTINGS.fightGroundEnergyRatio, this, false, -Double.MAX_VALUE, Double.MAX_VALUE);
		fightSettingsGrid.add(fightGroundEnergyRatio);
		settingsComponents.add(fightGroundEnergyRatio);
		
		
		//set up panel for buttons
		JPanel buttonMenu = new JPanel();
		SimLayoutHandler.createVerticalLayout(buttonMenu);
		settingColumns.add(centralPad.addPadding(buttonMenu));
		
		//add button to save settings
		SimButton saveSettingsButton = new SimButton();
		saveSettingsButton.setText("Save Last Updated Settings");
		saveSettingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				boolean error = !Main.saveSettings();
				if(error) setMessage("Settings Failed to Save", error);
				else setMessage("Settings Saved", error);
			}
		});
		buttonMenu.add(saveSettingsButton);
		
		//add button to update sim settings
		SimButton updateSettingsButton = new SimButton();
		updateSettingsButton.setText("Update Settings");
		updateSettingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				updateSettings();
				setMessage("Updated Settings", false);
				getFrame().pack();
				getFrame().repaint();
			}
		});
		buttonMenu.add(updateSettingsButton);
		
		//add button to load default settings
		SimButton defaultSettingsButton = new SimButton();
		defaultSettingsButton.setText("Default Settings");
		defaultSettingsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				loadAllDefaultSettings();
				setMessage("Loaded Defaults", false);
			}
		});
		buttonMenu.add(defaultSettingsButton);
		
		
		//add buttons for going to the next page
		
		//add panel for page buttons
		JPanel pageButtonPanel = new JPanel();
		SimLayoutHandler.createHorizontalLayout(pageButtonPanel);
		buttonMenu.add(centralPad.addPadding(pageButtonPanel));
		
		//create previous page button
		SimButton prevPageButton = new SimButton();
		prevPageButton.setText("Prev Page");
		prevPageButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				setSelectedPageNumber(currentPage - 1);
			}
		});
		pageButtonPanel.add(prevPageButton);
		
		//create next page button
		SimButton nextPageButton = new SimButton();
		nextPageButton.setText("Next Page");
		nextPageButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				setSelectedPageNumber(currentPage + 1);
			}
		});
		pageButtonPanel.add(nextPageButton);
		
		
		//add message label
		messageLabel = new SimLabel("");
		messageLabel.setForeground(Color.BLUE);
		buttonMenu.add(messageLabel);
		
		//add description information
		JPanel descriptionPanel = new JPanel();
		SimLayoutHandler.createVerticalLayout(descriptionPanel);
		buttonMenu.add(descriptionPanel);
		descriptionPanel.add(new SimLabel("Setting Description:"));
		descriptionLabel = new SimLabel("");
		descriptionLabel.setPreferredSize(new Dimension(400, 500));
		descriptionPanel.add(descriptionLabel);
		
		
		//finish setting up frame
		frame.pack();
		addOpenCloseControls();
		
		//add clock event to update the timer
		GameClock clock = getHandler().getClock();
		clock.addUpdateEvent(new ClockUpdateEvent(){
			@Override
			public void event(){
				if(messageTimer > 0){
					messageTimer--;
					if(messageTimer == 0) setMessage("", false);
				}
			}
		});
	}
	
	/**
	 * Updates the settings based on the values in the current values in the text fields.<br>
	 * Only updates settings with valid values typed in
	 */
	public void updateSettings(){
		for(SettingsComponent c : settingsComponents){
			c.setSetting();
			c.updateTitleValue();
		}
	}
	
	/**
	 * Update the values for the text boxes to the current values in the setting
	 */
	public void updateSettingsLabels(){
		for(SettingsComponent c : settingsComponents){
			c.setInputValue(c.getSetting().toString());
			c.updateTitleValue();
		}
	}
	
	/**
	 * Set every setting to it's default value
	 */
	public void loadAllDefaultSettings(){
		for(SettingsComponent c : settingsComponents){
			c.getSetting().loadDefaultValue();
			c.updateTitleValue();
		}
		updateSettingsLabels();
	}
	
	/**
	 * Set the text of the description to the given text
	 * @param text the text
	 */
	public void setDescriptionText(String text){
		descriptionLabel.setText("<html>" + text + "</html>");
	}
	
	/**
	 * Set the selected settings page number to the given page, will automatically loop around if outside the normal range
	 * @param pageNumber the page
	 */
	private void setSelectedPageNumber(int pageNumber){
		if(pageNumber < 0) pageNumber = dataColumms.length - 1;
		else if(pageNumber > dataColumms.length - 1) pageNumber = 0;

		this.currentPage = pageNumber;
		
		selectedSettingsPage.removeAll();
		SimLayoutHandler.createHorizontalLayout(selectedSettingsPage);
		selectedSettingsPage.add(dataColumms[this.currentPage]);
		getFrame().pack();
		getFrame().repaint();
	}
	
	/**
	 * Set the text of the message label to the given message
	 * @param error true if the message should be an error, false otherwise
	 * @param message the message
	 */
	private void setMessage(String message, boolean error){
		if(message.equals("")){
			messageTimer = 0;
			messageLabel.setText("<html><br></html>");
		}
		else{
			messageTimer = 200;
			messageLabel.setText(message);
		}
		
		if(error) messageLabel.setForeground(Color.RED);
		else messageLabel.setForeground(Color.BLUE);
	}
	
	@Override
	public Window getFrame(){
		return frame;
	}
	
}
