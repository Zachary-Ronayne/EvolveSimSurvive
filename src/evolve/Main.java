package evolve;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import evolve.gui.GraphGui;
import evolve.gui.GuiHandler;
import evolve.gui.HelpGui;
import evolve.gui.NeuralNetGui;
import evolve.gui.SavesGui;
import evolve.gui.SettingsGui;
import evolve.gui.SimGui;
import evolve.gui.SimSpeedGui;
import evolve.gui.SortingGui;
import evolve.util.clock.GameClock;
import evolve.util.options.Settings;

public class Main{
	
	public static final String DATA_PATH = "./data/";
	public static final String SAVES_PATH = DATA_PATH + "saves/";
	
	/*
	 * Cool seeds:
	 * 
	 * 279184365
	 * 47814724
	 * 1357667006
	 * 1504850280
	 * 1628407637
	 * 9128991
	 * 681889817
	 * 
	 */
	
	/*
	 * TODO
	 * 
	 * PRIORITY:
	 * 
	 * Add a system that allows creatures to give energy
	 * 	add settings noted by TODO things
	 * 	add inputs for when a creature sees another for if they are eating or not, and if they are vomiting or not
	 * 	make test cases
	 * 
	 * Look through features and quality of life, and implement a lot of the small changes/improvements
	 * 
	 * Reorganize settings menu
	 * 	make an object for a settings page
	 * 
	 * Look through high processing time code
	 * 	like eyes looking through tiles, and try to optimize by making the common case fast
	 * 	look at Simulation.getNearestCreature
	 * 
	 * 
	 * Features:
	 * 
	 * Add a day night cycle
	 * 	tiles are darker at night and lighter during the day
	 *  some kind of sleeping mechanic, like you use very little energy while sleeping, but you can't eat or move while sleeping
	 * 	you are also very vulnerable when you sleep, like make you lose more energy if someone fights you
	 * 	need to sleep at some point otherwise you start to go crazy and cant move or eat properly?
	 * 	vision reduced at night
	 * 	other effects?
	 * 
	 * Add a crop thing that makes food grow faster
	 * 	Like the creatures could pick up the crop thing by walking over it and then have another output for placing it down
	 * 	The tiles around the crop make food grow faster
	 * 	The crop has a bias color, that color slowly changes based on the creature holding it
	 * 	The crop slightly changes the bias color of tiles when it adds food to it
	 * 
	 * Allow weights of connections and biases of nodes to have values outside the range [-1, 1], like maybe [-2, 2]
	 * 
	 * Add a graph that when you select a NeuralNetCreature, it graphs all the input values and output values of the neural network,
	 * 	with a way of knowing what each line represents
	 * 
	 * Add display info to tiles, when you hover over a tile, you can see how much food it has, it's bias color value, and so on
	 * 
	 * Some kind of heritage thing, like being able to find alive ancestors of a certain creature
	 * 	maybe give each of them an id and a parent id, and then use that to find living relatives or something
	 * 
	 * Add different kinds of tiles
	 * 	a food tile that grows food is the normal tile that already exists
	 * 	an impassable wall tile that also takes up creature vision
	 * 	mud that slows you down
	 * 	empty tile that doesn't give food or drain energy
	 * 	with this add a way of randomly generating terrain, like rivers and different regions of land with different bias colors in tiles
	 * 		also have some kind of scalars for determining how the world is generated
	 * 		make sure to always use a seed to generate a world
	 * 		higher terrain might also limit creature vision,
	 * 			like they can see tiles with a lower or equal terrain value than the tile they are on
	 * 			they can't see past tiles if they are a certain height above them, maybe based on size
	 * 		terrain is displayed as the brightness of a tile?
	 * 
	 * A way for creatures to change tiles, like maybe they can move water around somehow
	 * 	the total amount of water doesn't change, but they can move it around and make bridges or whatever
	 * 
	 * Redo world generation
	 * 	When species values are generated, make the unique for each island
	 * 	rather than one gradient across the entire world, have separate islands have their own gradients
	 * 	this likely involved completely reworking world generation
	 * 	when rivers and islands generate, place them relative to the x and y position and scalar values
	 * 
	 * A way that creatures can live in the water
	 * 	all creatures need to drink water to survive
	 * 	have an aquatic value, high means you lose only a little energy on water and lot of energy on land, and vice versa with low aquatic
	 * 	a high aquatic value also means some other stat increased, like maybe better fighting and less energy loss in general
	 * 
	 * Allow fur to shed and be removed
	 * 	an output node for determining if they should shed or not
	 * 	a setting for how fast fur can regrow and another for how fast it can shed
	 * 
	 * Add controls to change stuff in the sim, like give a creature energy, change a tile's stats, and so on
	 * 	This would be in it's own GUI probably
	 * 	Also make some way of changing the type of tile on the ground
	 * 
	 * In the speed GUI, add buttons to change the play back speed of the simulation, should be able to go faster or slower by powers of 2
	 * 
	 * Add a chance for a birth to fail, the chance starts out very high, and then approaches 0 as the creature gets older
	 * 	Maybe if they get too old then their chance to give birth starts going down, should be linked to the scalar for getting sick
	 * 	Larger size also allows for earlier births, like being bigger decreases the chance for a birth to fail
	 * 	More energy spent also decreases the chance of a birth failing
	 * 	When a birth fails, the spent energy is lost, and some percentage of it is placed on the tiles beneath the parents whose birth failed
	 * 
	 * Add settings presets, like a text file that you can load in and save so you don't have to always type every setting in every time
	 * 	Also make some default settings presets that load in automatically when there is an error in loading the presets, but the user can delete them
	 * 
	 * Add birth system where creatures can get pregnant and give birth later on
	 * 	Would need to give creatures sexes
	 * 	This could also create eggs, like the creature lays an egg after they are pregnant for a short time, then the egg is laid
	 * 	The egg takes some time to hatch, and is vulnerable to getting attacked in this time
	 * 	creatures have a pregnant time and egg lay time
	 * 		high pregnant time means low egg time and vice versa
	 * 		while pregnant, the creature loses energy from existing twice as fast and moves slower, but the baby cannot be harmed
	 * 		while in an egg, a creature cannot do anything, but they spend no energy while they slowly hatch
	 * 
	 * Change species hue to be a gradient from green to brown, rather than the entire rainbow
	 * 
	 * Allow number of layers and nodes per hidden layer to mutate, so give the layers settings for different sizes
	 * 
	 * Some kind of position statistic, like a heat map of where creatures spend the most time
	 * 
	 * Allow seeds to be input as any string, they just get converted to a number
	 * 
	 * The eyes should only be able to change distance and angle over time
	 * 
	 * 
	 * Quality of life:
	 * 
	 * Add information in the NeuralNet gui that shows what each of the nodes mean
	 * 	Make all the information for all the labels left text aligned
	 * 	Allow nodes to have their rendered connections toggled on and off so only connections from a node the mouse is hovering over display
	 * 
	 * Make a class that handles a key press, when the key is pressed, a method is called once and the key isn't pressed again until the key is released
	 * 	this should be uses instead of using a boolean variable in a class somewhere every time the key needs to be pressed
	 * 
	 * For Settings with check boxes, use different icons to change the size, just using setSize() or setPerferedSize() doesn't effect the actual size at all
	 * 
	 * Add sliders for numerical inputs for settings
	 * 
	 * When the GUI is starting up, you can click on the gui and get an error because some stuff isn't set up
	 * 	so disable the key and mouse input until it's all set up
	 * 
	 * Make the extra GUIs properly pin to the main SimGui
	 * 
	 * Make test cases save files in a different folder than the data folder
	 * 
	 * Add an expected age value for the sick scalar, like an approximate number of minutes alive based on those 2 ages
	 * 
	 * An auto save feature
	 * 
	 * When moving a camera's position during rendering, it cannot determine correctly if things should be rendered or not
	 * 	make camera have two position fields, one with which to use for rendering, and one for where the camera is located in space
	 * 
	 * Allow zooming on NeuralNetGui
	 * 
	 * Move the default settings button to somewhere that you are less likely to accidentally click on, or maybe add a confirmation prompt
	 * 
	 * Change settings based on time to be based on number of seconds, rather than per update or 1/100 of a second
	 * 
	 * If you somehow get all this other stuff done and then learn about 3D rendering, maybe find a way to render the simulation in 3D
	 * 	would need a separate render method for rendering in 3D I guess
	 * 
	 * Add an FPS and TPS output to the SimGui
	 * 
	 * Add a page tab for settings so you can go to any page from any other page
	 * 
	 * Reorganize code, especially in Creature.update()
	 * 	move code blocks to seperate methods to make it easier to navigate and modify
	 * 
	 * moving and changing angle means getting sick less to encourage more complex movement
	 * 	maybe moving in varying directions is also important to this
	 * 	energy for breeding and cooldown between breeding goes up with more complex behavior?
	 * 		simulates how humans can make more complex beings, but they take lonegr to develop, whereas single celled can just reproduce right away
	 * 
	 * In Creature, find a way to remove the instanceof marked by a todo
	 * 
	 * Make the number of memory nodes a gene
	 * 
	 * Change SimThreadPool to have all of the code that isn't part of the pooled thread stuff be in a class called PooledThread
	 * 	then all the code relying on updating the actual object would be in a seperate class,
	 * 	and PooledThread and SimThreadPool could be used for any kind of multithreading task
	 * 
	 * 
	 * Optimization ideas:
	 * 
	 * Find a way to store the nearest creature that an eye can see so that it doesn't need to recalculate each update
	 * 	maybe store the creature that the Eye saw, and then if that is not the same creature as the
	 * 		nearest creature, then recalculate which creature the eye can see
	 * 
	 * Cache creature temperature difference rather than calculating it each time
	 * 
	 * 
	 * Bug fixes:
	 * 
	 * When saving a file, if the data folder or saves folder doesn't exist, nothing works, so create those folders if one of them isn't found
	 * 
	 * Lots of issues with loading graph data
	 * 	the graphs themselves are not updating in GraphGui I think
	 * 	it doesn't seem like the loading of the graph data is the issue, but updating the displays after a load
	 * 
	 * Sometimes creatures are added to the list while null and that shouldn't happen
	 * 
	 * Sometimes when looping, it doesn't go to the black screen and keeps rendering the simulation even when it shouldn't
	 * 
	 */

	/**
	 * The object where settings are stored for the entire simulation
	 */
	public static Settings SETTINGS;
	
	public static void main(String[] args){
		//load in settings
		SETTINGS = new Settings();
		loadSettings();
		
		GuiHandler handler = crateHandler();
		GameClock clock = handler.getClock();
		handler.getSimGui().setPaused(false);
		clock.setStopUpdates(false);
		
		clock.stopClock();
		handler.setUpThreadPool();
		clock.startClock();
	}
	
	/**
	 * Create a GuiHandler for this simulation
	 * @return
	 */
	public static GuiHandler crateHandler(){
		//the handler for GUis
		GuiHandler handler = new GuiHandler();
		
		//the main GUI used by the sim
		SimGui simGui = new SimGui(handler);
		handler.setSimGui(simGui);
		
		SavesGui savesGui = new SavesGui(handler);
		handler.setSavesGui(savesGui);
		
		NeuralNetGui neuralNetGui = new NeuralNetGui(handler);
		handler.setNeuralNetGui(neuralNetGui);
		
		HelpGui helpGui = new HelpGui(handler);
		handler.setHelpGui(helpGui);
		
		SimSpeedGui speedGui = new SimSpeedGui(handler);
		handler.setSpeedGui(speedGui);
		
		SettingsGui settingsGui = new SettingsGui(handler);
		handler.setSettingsGui(settingsGui);
		
		GraphGui graphGui = new GraphGui(handler);
		handler.setGraphGui(graphGui);
		
		SortingGui sortinggui = new SortingGui(handler);
		handler.setSortingGui(sortinggui);
		
		handler.setUpGuiPinning();
		
		return handler;
	}
	
	/**
	 * Save the Main Settings to the main settings file
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean saveSettings(){
		try{
			PrintWriter write = new PrintWriter(new File(DATA_PATH + "settings.txt"));
			
			boolean success = SETTINGS.save(write);
			
			write.close();
			return success;
		}catch(NoSuchElementException | FileNotFoundException e){
			e.printStackTrace();
			return false;
		}
	
	}/**
	 * Load the Main Settings from the main settings file
	 * @return true if the load was successful, false otherwise
	 */
	public static boolean loadSettings(){
		try{
			Scanner read = new Scanner(new File(DATA_PATH + "settings.txt"));
			
			boolean success = SETTINGS.load(read);
			
			read.close();
			
			return success;
		}catch(NoSuchElementException | FileNotFoundException e){
			System.err.println("Could not load settings, resetting to default settings.");
			SETTINGS.loadDefaultSettings();
			saveSettings();
			return false;
		}
	}
	
}
