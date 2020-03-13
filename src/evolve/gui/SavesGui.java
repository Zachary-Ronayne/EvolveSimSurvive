package evolve.gui;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;

import evolve.Main;
import evolve.gui.component.Padding;
import evolve.gui.component.SimButton;
import evolve.gui.component.SimDropDownMenu;
import evolve.gui.component.SimLabel;
import evolve.gui.component.SimTextBox;
import evolve.gui.frame.SavesFrame;
import evolve.gui.layout.SimLayoutHandler;
import evolve.sim.Simulation;
import evolve.util.clock.ClockUpdateEvent;
import evolve.util.clock.GameClock;
import evolve.util.graph.LineGraph;
import evolve.util.graph.LineGraphDetail;

/**
 * A GUI that keeps track of the save and loading features
 */
public class SavesGui extends Gui{
	
	/**
	 * The number of updates that the status message remains for
	 */
	public static final int STATUS_MESSAGE_TIME = 200;
	
	/**
	 * The frame of this GUI
	 */
	private SavesFrame frame;
	
	/**
	 * The drop down menu that keeps track of the loaded save files
	 */
	private SimDropDownMenu saveFileSelector;
	
	/**
	 * The text box that keeps track of the name to use for the current save file
	 */
	private SimTextBox saveNameTextBox;
	
	/**
	 * The label that keeps track of what message to display for loading and saving succeeding or failing
	 */
	private SimLabel saveStatus;
	
	/**
	 * Keeps track of the time remaining until the saveStatus should be cleared
	 */
	private int saveStatusTimer;
	
	public SavesGui(GuiHandler handler){
		super(handler);
		
		//create frame
		frame = new SavesFrame();
		addOpenCloseControls();
		
		//create a central panel for the entire frame
		Padding outsidePadding = new Padding(100, 100, 10, 10);
		Padding buttonPadding = new Padding(10, 10, 5, 5);
		Padding dropDownPadding = new Padding(10, 10, 5, 10);
		JPanel central = new JPanel();
		central.setBackground(Color.WHITE);
		frame.add(outsidePadding.addPadding(central));
		
		//create layout
		SimLayoutHandler.createVerticalLayout(central);
		
		//create new sim button
		SimButton createNewSimButton = new SimButton();
		createNewSimButton.setText("Create new Sim");
		createNewSimButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				handleCreateNewSimuButtonPress(e);
			}
		});
		central.add(buttonPadding.addPadding(createNewSimButton));
		
		//load sim button
		SimButton loadSimButton = new SimButton();
		loadSimButton.setText("Load Sim");
		loadSimButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				handleLoadButtonPress(e);
			}
		});
		central.add(buttonPadding.addPadding(loadSimButton));
		
		//scan for new files button
		SimButton scanFilesButton = new SimButton();
		scanFilesButton.setText("Scan Files");
		scanFilesButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				handleScanFilesButtonPress(e);
			}
		});
		central.add(buttonPadding.addPadding(scanFilesButton));
		
		//drop down menu for selecting save files
		String[] saves = scanFiles();
		saveFileSelector = new SimDropDownMenu();
		saveFileSelector.setItems(saves);
		central.add(dropDownPadding.addPadding(saveFileSelector));
		
		//text box for typing in the save name
		//create a panel to hold the text box and label
		JPanel textBoxPanel = new JPanel();
		SimLayoutHandler.createHorizontalLayout(textBoxPanel);
		central.add(buttonPadding.addPadding(textBoxPanel));
		//create label for the text box
		SimLabel saveLabel = new SimLabel("Save name: ");
		textBoxPanel.add(saveLabel);
		//create the text box
		saveNameTextBox = new SimTextBox();
		saveNameTextBox.restrictInput(SimTextBox.RESTRICT_FILE);
		textBoxPanel.add(saveNameTextBox);
		
		//save sim button
		SimButton saveButton = new SimButton();
		saveButton.setText("Save Current Sim");
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				handleSaveButtonPress(e);
			}
		});
		central.add(buttonPadding.addPadding(saveButton));
		
		//delete selected save button
		SimButton deleteSaveButton = new SimButton();
		deleteSaveButton.setText("Delete Selected Save");
		deleteSaveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				handleDeleteSaveButton(e);
			}
		});
		central.add(buttonPadding.addPadding(deleteSaveButton));
		
		//label for showing status
		saveStatus = new SimLabel("<html><br></html>");
		central.add(buttonPadding.addPadding(saveStatus));
		
		//pack frame
		frame.pack();
		
		//add timer event to get rid of notification
		saveStatusTimer = 0;
		GameClock clock = getHandler().getClock();
		clock.addUpdateEvent(new ClockUpdateEvent(){
			@Override
			public void event(){
				if(saveStatusTimer > 0){
					saveStatusTimer--;
					if(saveStatusTimer == 0) saveStatus.setText("");
				}
			}
		});
	}
	
	/**
	 * Load in all the save files in the saves folder and returns all their names
	 * @return the array of save names, these only contain the file name, not the file extension
	 */
	public String[] scanFiles(){
		ArrayList<String> fileNames = new ArrayList<String>();
		
		try{
			File saveLoc = new File(Main.SAVES_PATH);
			File[] saves = saveLoc.listFiles();
			for(File f : saves){
				//if f is a file
				if(f.isFile()){
					String name = f.getName();
					//if f is a .txt
					if(name.length() >= 4 && name.contains(".txt")){
						name = name.replace(".txt", "");
						fileNames.add(name);
					}
				}
			}
			
		}catch(Exception e){
			System.err.println("Couldn't load save files");
		}
		
		String[] namesArr = new String[fileNames.size()];
		for(int i = 0; i < namesArr.length; i++){
			namesArr[i] = fileNames.get(i);
		}
			
		return namesArr;
	}
	
	/**
	 * Update the list of files in the saveFileSelector drop down menu
	 */
	public void updateSaveFileSelectorList(){
		saveFileSelector.setItems(scanFiles());
	}
	
	public void handleScanFilesButtonPress(ActionEvent e){
		updateSaveFileSelectorList();
	}

	/**
	 * Called when the button for creating a new simulation is pressed.
	 * @param e the event associated with the button press
	 */
	public void handleCreateNewSimuButtonPress(ActionEvent e){
		saveStatus.setText("");
		
		getHandler().setSimulation(new Simulation());
		getHandler().getSettingsGui().updateSettingsLabels();
		getHandler().setUpThreadPool();
		
		saveStatus.setForeground(Color.BLUE);
		saveStatus.setText("<html>Created new sim</html>");
		saveStatusTimer = STATUS_MESSAGE_TIME;
	}
	
	/**
	 * Called when the button for saving a simulation is pressed.
	 * @param e the event associated with the button press
	 */
	public void handleSaveButtonPress(ActionEvent e){
		saveStatus.setText("");
		
		Simulation sim = getHandler().getSimulation();
		
		//pause the simulation before saving
		GameClock clock = getHandler().getClock();
		clock.setStopUpdates(true);
		clock.stopClock();

		String selectedName = saveNameTextBox.getText();
		
		//wait until the clock is done updating and rendering before saving
		int maxKick = 10000;
		int kick = 0;
		while(kick < maxKick && (clock.isRendering() || clock.isUpdating())){
			kick++;
			try{
				Thread.sleep(1);
			}catch(InterruptedException e1){e1.printStackTrace();}
		}
		if(kick == maxKick){
			//unpause the clock
			clock.setStopUpdates(false);
			saveStatus.setForeground(Color.RED);
			saveStatus.setText("<html>Failed to save sim:<br>" + selectedName + "</html>");
			System.err.println("Failed to save sim: " + selectedName);
			saveStatusTimer = STATUS_MESSAGE_TIME;
			clock.startClock();
			return;
		}
		
		//when this button is pressed, save the current sim if one exists
		if(sim != null){
			boolean success = true;
			try{
				PrintWriter write = new PrintWriter(new File(Main.SAVES_PATH + selectedName + ".txt"));
				
				GraphGui graphGui = getHandler().getGraphGui();
				
				success &= sim.save(write);
				success &= graphGui.getPopulationGraph().save(write);
				success &= graphGui.getMutabilityGraph().save(write);
				success &= graphGui.getAgeGraph().save(write);
				
				write.close();
				
				updateSaveFileSelectorList();
				
			}catch(FileNotFoundException e1){
				System.err.println("Failed to save sim \"" + selectedName + "\"");
				success = false;
			}
			
			if(success){
				saveStatus.setForeground(Color.BLUE);
				saveStatus.setText("<html>Save successful for:<br>" + selectedName + "</html>");
				saveStatusTimer = STATUS_MESSAGE_TIME;
			}
			else{
				saveStatus.setForeground(Color.RED);
				saveStatus.setText("<html>Failed to save sim:<br>" + selectedName + "</html>");
				saveStatusTimer = STATUS_MESSAGE_TIME;
			}
		}
		
		//unpause the clock
		clock.setStopUpdates(false);
		clock.startClock();
	}
	
	/**
	 * Called when the button for loading a simulation is pressed.
	 * @param e the event associated with the button press
	 */
	@SuppressWarnings("unchecked")
	public void handleLoadButtonPress(ActionEvent e){
		saveStatus.setText("");
		
		//if no loaded files exist, do nothing
		if(saveFileSelector.getItemCount() <= 0) return;
		
		Simulation sim = new Simulation();
		LineGraph populationGraph = new LineGraph(new LineGraphDetail[1]);
		LineGraph mutabilityGraph = new LineGraph(new LineGraphDetail[1]);
		LineGraph ageGraph = new LineGraph(new LineGraphDetail[1]);
		
		//pause the simulation before saving
		GameClock clock = getHandler().getClock();
		clock.stopClock();

		//attempt to load, if the save fails, the reset the simulation
		String selectedName = saveFileSelector.getSelectedItem();
		
		//wait until the clock is done updating and rendering before loading
		int maxKick = 10000;
		int kick = 0;
		while(kick < maxKick && (clock.isRendering() || clock.isUpdating())){
			kick++;
			try{
				Thread.sleep(1);
			}catch(InterruptedException e1){e1.printStackTrace();}
		}
		if(kick == maxKick){
			//unpause the clock
			clock.setStopUpdates(false);
			saveStatus.setForeground(Color.RED);
			saveStatus.setText("<html>Failed to load sim:<br>" + selectedName + "</html>");
			System.err.println("Failed to load sim: " + selectedName);
			saveStatusTimer = STATUS_MESSAGE_TIME;
			return;
		}
		
		boolean success = true;
		try{
			Scanner read = new Scanner(new File(Main.SAVES_PATH + selectedName + ".txt"));
			
			success &= sim.load(read);
			success &= populationGraph.load(read);
			success &= mutabilityGraph.load(read);
			success &= ageGraph.load(read);
			
			read.close();
		}catch(FileNotFoundException e1){
			System.err.println("Failed to load sim \"" + selectedName + "\"");
			getHandler().deleteSimulation();
			success = false;
		}
		
		if(success){
			getHandler().setSimulation(sim);
			GraphGui graphGui = getHandler().getGraphGui();
			graphGui.getPopulationGraph().setData((ArrayList<Double[]>) populationGraph.getData().clone());
			graphGui.getMutabilityGraph().setData((ArrayList<Double[]>) mutabilityGraph.getData().clone());
			graphGui.getAgeGraph().setData((ArrayList<Double[]>) ageGraph.getData().clone());
			graphGui.renderSelectedGraph();
			
			getHandler().getSettingsGui().updateSettingsLabels();
			sim.setPopulationData(populationGraph.getData());
			sim.setMutabilityData(mutabilityGraph.getData());
			sim.setAgeData(ageGraph.getData());
			
			saveNameTextBox.setText(selectedName);
			
			saveStatus.setForeground(Color.BLUE);
			saveStatus.setText("<html>Load successful for:<br>" + selectedName + "</html>");
			saveStatusTimer = STATUS_MESSAGE_TIME;
		}
		else{
			saveStatus.setForeground(Color.RED);
			saveStatus.setText("<html>Failed to load sim:<br>" + selectedName + "</html>");
			saveStatusTimer = STATUS_MESSAGE_TIME;
		}
		
		//unpause the clock
		clock.setStopUpdates(false);
		getHandler().setUpThreadPool();
		clock.startClock();
	}
	
	/**
	 * Called when the button deleting the loaded save file is pressed.
	 * @param e the event associated with the button press
	 */
	public void handleDeleteSaveButton(ActionEvent e){
		saveStatus.setText("");
		
		String selectedName = saveFileSelector.getSelectedItem();
		File toDelete = new File(Main.SAVES_PATH + selectedName + ".txt");
		if(toDelete.exists()){
			toDelete.delete();

			saveStatus.setForeground(Color.BLUE);
			saveStatus.setText("<html>Deleted sim:<br>" + selectedName + "</html>");
			saveStatusTimer = STATUS_MESSAGE_TIME;
		}
		updateSaveFileSelectorList();
	}
	
	/**
	 * Get the text box holding the value of the save name
	 * @return
	 */
	public SimTextBox getSaveNameTextBox(){
		return saveNameTextBox;
	}
	
	@Override
	public Window getFrame(){
		return frame;
	}
	
}
