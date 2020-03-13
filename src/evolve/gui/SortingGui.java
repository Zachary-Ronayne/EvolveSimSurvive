package evolve.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JPanel;

import evolve.gui.component.Padding;
import evolve.gui.component.SimButton;
import evolve.gui.component.SimConstants;
import evolve.gui.component.SimDropDownMenu;
import evolve.gui.component.SimLabel;
import evolve.gui.component.SimTextBox;
import evolve.gui.frame.SortingFrame;
import evolve.gui.layout.SimLayoutHandler;
import evolve.sim.Simulation;
import evolve.sim.obj.NeuralNetCreature;
import evolve.util.Camera;
import evolve.util.clock.ClockRenderEvent;
import evolve.util.clock.ClockUpdateEvent;
import evolve.util.clock.GameClock;

public class SortingGui extends Gui{

	/**
	 * Constant used for sorting by creature's age
	 */
	public static final String SORT_AGE = "Age";
	/**
	 * Constant used for sorting by current creature's energy
	 */
	public static final String SORT_ENERGY = "Current Energy";
	/**
	 * Constant used for sorting by current creature's generation
	 */
	public static final String SORT_GENERATION = "Generation";
	/**
	 * Constant used for sorting by current creature's total number of children
	 */
	public static final String SORT_CHILDREN = "Children";
	/**
	 * Constant used for sorting by current creature's number of children with a partner
	 */
	public static final String SORT_BREED_CHILDREN = "Breed Children";
	/**
	 * Constant used for sorting by current creature's number of children with no partner
	 */
	public static final String SORT_ASEXUAL_CHILDREN = "Asexual Children";
	/**
	 * Constant used for sorting by current creature's current size
	 */
	public static final String SORT_SIZE = "Current Size";
	/**
	 * Constant used for sorting by current creature's size gene
	 */
	public static final String SORT_SIZE_GENE = "Size Gene";
	/**
	 * Constant used for sorting by the number of eyes the current creature has
	 */
	public static final String SORT_NUM_EYES = "Numer of Eyes";
	/**
	 * Constant used for sorting by current creature's temperature
	 */
	public static final String SORT_TEMPERATURE = "Temperature";
	/**
	 * Constant used for sorting by current creature's fur amount
	 */
	public static final String SORT_FUR = "Fur";
	
	/**
	 * The list of items that the GUI can be sorted by.<br>
	 * To add an item to this list for use in the GUI:
	 * <li>Create a static final string with the display name of the sorting type, must be unique from all other sort types</li>
	 * <li>In getSortComparable() add a case in the if statement chain to create and return a new {@link Comparator} based on what the new sort type should be</li>
	 */
	public static final String[] SORT_ITEMS = new String[]{
			SORT_AGE, SORT_ENERGY, SORT_GENERATION,
			SORT_CHILDREN, SORT_BREED_CHILDREN, SORT_ASEXUAL_CHILDREN,
			SORT_SIZE, SORT_SIZE_GENE, SORT_NUM_EYES, SORT_TEMPERATURE, SORT_FUR
	};
	

	/**
	 * Constant used for searching for a creature with the specified generation.
	 */
	public static final String SEARCH_GENERATION = "Generation";
	
	/**
	 * Constant used for searching for a creature with the specified number of eyes
	 */
	public static final String SEARCH_EYES = "Eyes";

	/**
	 * The list of items that the GUI can be searched for.<br>
	 * To add an item to this list for use in the GUI
	 * <li>Create a static final string with the display name of the sorting type, must be unique from all other sort types</li>
	 * <li>In searchCreatureList() add a case in the if statement chain to sort the list of creatures in the method</li>
	 */
	public static final String[] SEARCH_ITEMS = new String[]{
			SEARCH_GENERATION, SEARCH_EYES
	};
	
	/**
	 * The number of 1/100 of a second between when this frame automatically updates the list it is showing
	 */
	public static final long REFRESH_RATE = 50;
	
	/**
	 * The number of creatures that should be displayed in the creature display panel
	 */
	public static final int LIST_CREATURE_NUM = 5;

	/**
	 * The space between each displayed creature in this GUI
	 */
	public static final int CREATURE_DISP_SPACE = 150;
	
	/**
	 * The width of the panel that displays the creatures
	 */
	public static final int CREATURE_DISP_WIDTH = CREATURE_DISP_SPACE + 20;
	/**
	 * The height of the panel that displays the creatures
	 */
	public static final int CREATURE_DISP_HEIGHT = CREATURE_DISP_SPACE * LIST_CREATURE_NUM;
	
	private SortingFrame frame;
	
	/**
	 * The panel that displays the creatures
	 */
	private JPanel creaturePanel;
	
	/**
	 * The buffer used to draw the creatures
	 */
	private BufferedImage creatureImage;
	
	/**
	 * The index of the creature in creatures that the mouse is currently hovering over, -1 for not hovering over anything
	 */
	private int creatureIndex;
	
	/**
	 * A list of all the creatures displayed on the GUI
	 */
	private ArrayList<NeuralNetCreature> creatures;
	
	/**
	 * the drop down menu that keeps track of the sort criteria for the creatures
	 */
	private SimDropDownMenu sortMenu;
	
	/**
	 * The text box that keeps track of what the user has typed in to search for
	 */
	private SimTextBox searchTextBox;
	
	/**
	 * The drop down menu that keeps track of what type of data should be searched for
	 */
	private SimDropDownMenu searchDropDownMenu;
	
	/**
	 * The message that shows what is currently being searched/sorted for
	 */
	private SimLabel searchSortMessage;
	
	/**
	 * The number of updates left that an error message should be displayed
	 */
	private int searchSortErrorTime;
	
	/**
	 * True if the creature list should be updated automatically, false if it should stay as is
	 */
	private boolean updateAutomatically;
	
	/**
	 * true if this GUI is sorting or searching through a list and shouldn't to start a new operation until the current one ends, false otherwise
	 */
	private boolean working;
	
	/**
	 * true if this GUI should sort creatures in ascending order or descending order
	 */
	private boolean ascending;
	
	public SortingGui(GuiHandler handler){
		super(handler);
		
		updateAutomatically = true;
		working = false;
		ascending = false;
		creatures = new ArrayList<NeuralNetCreature>();
		creatureImage = new BufferedImage(CREATURE_DISP_WIDTH, CREATURE_DISP_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		creatureIndex = -1;
		
		//some padding to use
		Padding pad = new Padding(2, 2, 2, 2);
		
		//set up the initial frame
		this.frame = new SortingFrame();
		addOpenCloseControls();
		
		
		//set up a central panel
		JPanel central = new JPanel();
		SimLayoutHandler.createVerticalLayout(central);
		this.frame.add(central);
		
		
		//add JPanel with a drop down menu to select which item to sort by, and a button to toggle sorting ascending/descending
		JPanel sortSelector = new JPanel();
		SimLayoutHandler.createHorizontalLayout(sortSelector);
		central.add(pad.addPadding(sortSelector));
		
		//drop down menu for selecting  sorting type
		sortMenu = new SimDropDownMenu();
		sortMenu.setItems(SORT_ITEMS);
		sortMenu.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e){
				updateAutomatically = true;
				updateCreaturesList(getSortComparable(sortMenu.getSelectedItem()));
			}
		});
		sortSelector.add(sortMenu);
		
		//button for toggling sorting by ascending and descending
		SimButton toggleAscendingButton = new SimButton();
		toggleAscendingButton.setText("Sorting by descending");
		toggleAscendingButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				ascending = !ascending;
				if(ascending) toggleAscendingButton.setText("Sorting by ascending");
				else toggleAscendingButton.setText("Sorting by descending");
				updateAutomatically = true;
				updateCreaturesList(getSortComparable(sortMenu.getSelectedItem()));
			}
		});
		sortSelector.add(toggleAscendingButton);
		
		
		//add a JPanel with a text box and a search button that allows the user to type in an id to search for
		JPanel searchPanel = new JPanel();
		SimLayoutHandler.createHorizontalLayout(searchPanel);
		central.add(pad.addPadding(searchPanel));
		Padding searchPad = new Padding(1, 1, 0, 0);
		//search box for typing in values
		searchTextBox = new SimTextBox();
		searchPanel.add(searchPad.addPadding(searchTextBox));
		//button for searching
		SimButton searchButton = new SimButton();
		searchButton.setText("Search");
		searchButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				updateAutomatically = false;
				searchCreatureList();
			}
		});
		searchPanel.add(searchPad.addPadding(searchButton));
		//drop down menu for selecting what to search for
		searchDropDownMenu = new SimDropDownMenu();
		searchDropDownMenu.setItems(SEARCH_ITEMS);
		searchPanel.add(searchPad.addPadding(searchDropDownMenu));
		

		//create a space for a message that says if the creature was found
		searchSortMessage = new SimLabel("<html><br></html>");
		searchSortErrorTime = 0;
		central.add(pad.addPadding(searchSortMessage));
		
		
		//add a panel for displaying a list of creatures
		creaturePanel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g){
				super.paintComponent(g);
				g.drawImage(creatureImage, 0, 0, null);
			}
		};
		SimLayoutHandler.createVerticalLayout(creaturePanel);
		creaturePanel.setPreferredSize(new Dimension(CREATURE_DISP_WIDTH, CREATURE_DISP_HEIGHT));
		//add a mouse listener for clicking on a selected creature
		MouseAdapter creaturePanelInput = new MouseAdapter(){
			@Override
			public void mouseExited(MouseEvent e){
				creatureIndex = -1;
			}
			@Override
			public void mouseMoved(MouseEvent e){
				creatureIndex = e.getY() / CREATURE_DISP_SPACE;
			}
			@Override
			public void mouseDragged(MouseEvent e){
				creatureIndex = e.getY() / CREATURE_DISP_SPACE;
			}
			@Override
			public void mouseReleased(MouseEvent e){
				//only try to set the creature if the index is value
				if(creatureIndex >= 0 && creatureIndex < creatures.size()){
					Simulation sim = getHandler().getSimulation();
					//make sure the sim is not null before selecting the creature
					if(sim != null){
						//select the creature and center it on the camera
						NeuralNetCreature selected = creatures.get(creatureIndex);
						NeuralNetGui neuralNetGui = getHandler().getNeuralNetGui();
						neuralNetGui.setSelectedCreature(selected);
						sim.getCamera().center(selected.getX(), selected.getY());
					}
				}
			}
		};
		creaturePanel.addMouseListener(creaturePanelInput);
		creaturePanel.addMouseMotionListener(creaturePanelInput);
		creaturePanel.addMouseWheelListener(creaturePanelInput);
		central.add(pad.addPadding(creaturePanel));
		
		
		//finish setting up frame
		frame.pack();
		
		//add a clock event to update the rendering of the list of creatures
		//and to redraw the panel with the current state of the creatures
		GameClock clock = getHandler().getClock();
		clock.addUpdateEvent(new ClockUpdateEvent(){
			@Override
			public void event(){
				//only update the GUI if it is visible
				if(!getFrame().isVisible()) return;
				
				//only update the list if it should automatically update
				if(updateAutomatically){
					Simulation sim = getHandler().getSimulation();
					
					//if the simulation has reached the appropriate increment
					if(sim != null && sim.getTotalTime() % REFRESH_RATE == 0){
						//update the list based on the current sort system selected
						updateCreaturesList(getSortComparable(sortMenu.getSelectedItem()));
					}
				}
				
				//update the error search time
				if(searchSortErrorTime > 0) searchSortErrorTime--;
				//if there should be no error, update the message
				else{
					if(updateAutomatically){
						String sortString = sortMenu.getSelectedItem();
						String message = "Sorting by (" + sortString;
						message += ") in";
						if(ascending) message += " ascending";
						else message += " descending";
						message += " order.";
						setSearchSortMessage(message);
					}
					else{
						String searchName = searchTextBox.getText();
						String searchType = searchDropDownMenu.getSelectedItem();
						String message = "Searching for ";
						message += searchType + " \"" + searchName + "\".";
						setSearchSortMessage(message);
					}
				}
			}
		});
		clock.addRenderEvent(new ClockRenderEvent(){
			@Override
			public void event(){
				//only render the GUI if it is visible
				if(!getFrame().isVisible()) return;
				
				renderCreaturePanel();
			}
		});
	}
	
	/**
	 * Draw the creatures in the creatures list in the order that they exist in the list<br>
	 * Call updateCreaturesList() to select which creatures should be in the list<br>
	 * This method automatically redraws the panel and does nothing if this frame is not visible
	 */
	public void renderCreaturePanel(){
		//if the list is being sorted, do not update the render
		if(working || !getFrame().isVisible()) return;

		//draw the background
		Graphics2D g = (Graphics2D)creatureImage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, CREATURE_DISP_WIDTH, CREATURE_DISP_HEIGHT);
		
		//create a camera object to draw the creatures
		Camera cam = new Camera(CREATURE_DISP_WIDTH, CREATURE_DISP_HEIGHT);
		cam.setG(g);
		cam.setDrawOnlyInBounds(false);
		//draw the creatures
		for(int i = 0; i < creatures.size(); i++){
			//if i is the creatureIndex, draw a box around the creature
			if(i >= 0 && i == creatureIndex){
				g.setColor(new Color(200, 200, 255, 127));
				g.fillRect(0, CREATURE_DISP_SPACE * i, CREATURE_DISP_WIDTH, CREATURE_DISP_SPACE);
			}
			
			NeuralNetCreature c = creatures.get(i);
			cam.setX(c.getX() - CREATURE_DISP_WIDTH / 2);
			cam.setY(c.getY() - (i + .5) * CREATURE_DISP_SPACE);
			c.render(cam);
		}
		
		//if no creatures were drawn, display a message that creatures could not be found
		if(creatures.size() == 0){
			g.setColor(Color.BLACK);
			g.setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, 20));
			g.drawString("No creatures found", 10, 100);
		}
		
		//update the panel
		creaturePanel.repaint();
	}
	
	/**
	 * Look for creatures based on the current string in the search text box and the search type to look for in the drop down menu.<br>
	 * This will set the creatures list to the first 3 creatures in the list, sorted ascending by age.<br>
	 * When adding a new way of sorting:
	 * <li>In the lower if statement chain, test to see if searchType.equals(STRING_CONSTANT)</li>
	 * <li>From there add the first LIST_CREATURE_NUM creatures to the creatures list from the copy list</li>
	 * <li>Any more creatures than LIST_CREATURE_NUM will not be shown in the results</li>
	 */
	public void searchCreatureList(){
		if(working) return;
		working = true;

		Simulation sim = getHandler().getSimulation();
		
		//only keep going if the sim is not null
		if(sim != null){
			//create a copy of the creature list that can be used without effecting the simulation
			ArrayList<NeuralNetCreature> origional = sim.getEvolvingCreatures();
			ArrayList<NeuralNetCreature> copy = new ArrayList<NeuralNetCreature>();
			copy.addAll(origional);
			//sort the copied list by age
			copy.sort(getSortComparable(SORT_AGE));
			
			//empty the creatures list
			creatures.clear();
			
			//search for the text based on what is in the drop down menu and text box
			String searchType = searchDropDownMenu.getSelectedItem();
			String searchData = searchTextBox.getText();
			
			
			//searching chain
			
			//search for generation
			if(searchType.equals(SEARCH_GENERATION)){
				//if the string is not a valid generation number, then send an error message that the string was invalid
				try{
					int gen = Integer.parseInt(searchData);
					int found = 0;
					for(NeuralNetCreature c : copy){
						if(c.getGeneration() == gen){
							creatures.add(c);
							found++;
						}
						if(found >= LIST_CREATURE_NUM) break;
					}
				}catch(NumberFormatException e){
					setSearchSortError("Invalid generation number");
				}
			}
			
			//search for number of eyes
			else if(searchType.equals(SEARCH_EYES)){
				//if the string is not a valid eye number, then send an error message that the string was invalid
				try{
					int eyeCount = Integer.parseInt(searchData);
					int found = 0;
					for(NeuralNetCreature c : copy){
						if(c.getNumberOfEyes() == eyeCount){
							creatures.add(c);
							found++;
						}
						if(found >= LIST_CREATURE_NUM) break;
					}
				}catch(NumberFormatException e){
					setSearchSortError("Invalid eye number");
				}
			}
		}
		
		
		//end of method
		working = false;
	}
	
	/**
	 * Gets the list of creatures in the current simulation and takes LIST_CREATURE_NUM number of creatures from 
	 * either the bottom of the list or the top of the list into the main creatures list depending on if ascending or 
	 * descending is selected in this GUI.<br>
	 * does nothing if the current simulation is null
	 * @param c the Comparator used to sort the creatures, c should always sort ascending
	 */
	public void updateCreaturesList(Comparator<NeuralNetCreature> c){
		if(working) return;
		
		working = true;
		
		Simulation sim = getHandler().getSimulation();
		if(sim != null){
			//create a copy of the creature list that can be used without effecting the simulation
			ArrayList<NeuralNetCreature> origional = sim.getEvolvingCreatures();
			ArrayList<NeuralNetCreature> copy = new ArrayList<NeuralNetCreature>();
			copy.addAll(origional);
			//sort the copied list
			copy.sort(c);
			
			//reset and add the appropriate number of elements to the creatures list
			creatures.clear();
			
			for(int i = 0; i < copy.size() && i < LIST_CREATURE_NUM; i++){
				NeuralNetCreature add;
				if(ascending) add = copy.get(i);
				else add = copy.get(copy.size() - 1 - i);
				creatures.add(add);
			}
		}
		
		working = false;
	}
	
	/**
	 * Set the message of the search sort field
	 * @param message the message of the string
	 */
	public void setSearchSortMessage(String message){
		if(message == null) return;
		searchSortErrorTime = 0;
		searchSortMessage.setForeground(Color.BLACK);
		searchSortMessage.setText("<html>" + message + "</html>");
	}

	/**
	 * Set the message of the search sort field as an error
	 * @param error the error message
	 */
	public void setSearchSortError(String error){
		if(error == null) return;
		searchSortErrorTime = 100;
		searchSortMessage.setForeground(Color.RED);
		searchSortMessage.setText("<html>" + error + "</html>");
	}
	
	@Override
	public Window getFrame(){
		return frame;
	}
	
	/**
	 * Get the comparable that will sort a list of {@link NeuralNetCreature} in the appropriate order.<br>
	 * The comparable returned by this method always sorts in ascending order
	 * @param sort the string that determines which sorting comparator will be returned
	 * @return the Comparator based on the given string, automatically sorts by age if no valid sort is found, null if sort is null
	 */
	public static Comparator<NeuralNetCreature> getSortComparable(String sort){
		if(sort == null) return null;
		
		//sort by energy
		if(sort.equals(SORT_ENERGY)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					double energy1 = o1.getEnergy();
					double energy2 = o2.getEnergy();
					if(energy1 < energy2) return -1;
					else if(energy1 > energy2) return 1;
					else return 0;
				}
			};
		}
		
		//sort by generation
		else if(sort.equals(SORT_GENERATION)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					int gen1 = o1.getGeneration();
					int gen2 = o2.getGeneration();
					if(gen1 < gen2) return -1;
					else if(gen1 > gen2) return 1;
					else return 0;
				}
			};
		}

		//sort by children
		else if(sort.equals(SORT_CHILDREN)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					int children1 = o1.getTotalChildren();
					int children2 = o2.getTotalChildren();
					if(children1 < children2) return -1;
					else if(children1 > children2) return 1;
					else return 0;
				}
			};
		}

		//sort by breed children
		else if(sort.equals(SORT_BREED_CHILDREN)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					int children1 = o1.getBreedChildren();
					int children2 = o2.getBreedChildren();
					if(children1 < children2) return -1;
					else if(children1 > children2) return 1;
					else return 0;
				}
			};
		}
		
		//sort by breed children
		else if(sort.equals(SORT_ASEXUAL_CHILDREN)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					int children1 = o1.getAsexualChildren();
					int children2 = o2.getAsexualChildren();
					if(children1 < children2) return -1;
					else if(children1 > children2) return 1;
					else return 0;
				}
			};
		}
		
		//sort by current size
		else if(sort.equals(SORT_SIZE)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					double size1 = o1.getRadius();
					double size2 = o2.getRadius();
					if(size1 < size2) return -1;
					else if(size1 > size2) return 1;
					else return 0;
				}
			};
		}
		
		//sort by size gene
		else if(sort.equals(SORT_SIZE_GENE)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					double size1 = o1.getSizeGene();
					double size2 = o2.getSizeGene();
					if(size1 < size2) return -1;
					else if(size1 > size2) return 1;
					else return 0;
				}
			};
		}
		
		//sort by number of eyes
		else if(sort.equals(SORT_NUM_EYES)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					int eyes1 = o1.getNumberOfEyes();
					int eyes2 = o2.getNumberOfEyes();
					if(eyes1 < eyes2) return -1;
					else if(eyes1 > eyes2) return 1;
					else return 0;}
			};
		}
		
		//sort by temperature
		else if(sort.equals(SORT_TEMPERATURE)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					double temp1 = o1.getTemperature().getTemp();
					double temp2 = o2.getTemperature().getTemp();
					if(temp1 < temp2) return -1;
					else if(temp1 > temp2) return 1;
					else return 0;
				}
			};
		}
		
		//sort by fur
		else if(sort.equals(SORT_FUR)){
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					double fur1 = o1.getFur();
					double fur2 = o2.getFur();
					if(fur1 < fur2) return -1;
					else if(fur1 > fur2) return 1;
					else return 0;
				}
			};
		}
		
		//sort by age
		else{
			return new Comparator<NeuralNetCreature>(){
				@Override
				public int compare(NeuralNetCreature o1, NeuralNetCreature o2){
					if(o1 == null || o2 == null) return 0;
					long age1 = o1.getAge();
					long age2 = o2.getAge();
					if(age1 < age2) return -1;
					else if(age1 > age2) return 1;
					else return 0;
				}
			};
		}
	}
	
}
