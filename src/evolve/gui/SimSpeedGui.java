package evolve.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import evolve.gui.component.Padding;
import evolve.gui.component.SimButton;
import evolve.gui.component.SimLabel;
import evolve.gui.component.SimSpeedButton;
import evolve.gui.component.SimTextBox;
import evolve.gui.frame.SimSpeedFrame;
import evolve.gui.layout.SimLayoutHandler;
import evolve.sim.obj.Creature;
import evolve.util.clock.GameClock;

public class SimSpeedGui extends Gui{
	
	/**
	 * The frame that this GUI uses for display
	 */
	private SimSpeedFrame frame;
	
	/**
	 * The text box for the number of years to run
	 */
	private SimTextBox yearsText;
	/**
	 * The text box for the number of days to run
	 */
	private SimTextBox daysText;
	/**
	 * The text box for the number of hours to run
	 */
	private SimTextBox hoursText;
	/**
	 * The text box for the number of minutes to run
	 */
	private SimTextBox minutesText;
	/**
	 * The text box for the number of seconds to run
	 */
	private SimTextBox secondsText;
	
	/**
	 * Keeps track of if the sim looping should stop
	 */
	private boolean stopLooping;
	
	/**
	 * keeps track of if the simulation is looping currently
	 */
	private boolean looping;
	
	public SimSpeedGui(GuiHandler handler){
		super(handler);
		
		stopLooping = false;
		looping = false;
		
		//create frame
		frame = new SimSpeedFrame();
		addOpenCloseControls();
		
		//create padding
		Padding pad = new Padding(2, 2, 2, 2);
		
		//central panel for all other objects
		JPanel central = new JPanel();
		central.setBackground(Color.WHITE);
		SimLayoutHandler.createVerticalLayout(central);
		frame.add(central);
		
		//panel for buttons to run the simulation for a set time
		JPanel buttonsPan = new JPanel();
		SimLayoutHandler.createHorizontalLayout(buttonsPan);
		central.add(pad.addPadding(buttonsPan));
		
		//create time buttons for running the simulator
		long[] times = new long[]{
			365 * 24 * 360000l,//1 year
			30 * 24 * 360000l,//30 days
			24 * 360000l,//1 day
			6 * 360000l,//6 hours
			1 * 360000l,//1 hour
			6000 * 10,//10 minutes
			6000 * 5,//5 minutes
			6000 * 2,//2 minutes
			6000//1 minute
		};
		for(int i = 0; i < times.length; i++){
			SimSpeedButton b = new SimSpeedButton(times[i]);
			b.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					loopSim(b.getTime());
				}
			});
			buttonsPan.add(b);
		}
		
		//panel for text fields for how long to run the simulation and for how long is left
		JPanel timePan = new JPanel();
		SimLayoutHandler.createHorizontalLayout(timePan);
		central.add(pad.addPadding(timePan));
		
		//grid panel for the text fields
		JPanel textPanel = new JPanel();
		textPanel.setBackground(Color.WHITE);
		GridLayout grid = new GridLayout(0, 10);
		textPanel.setLayout(grid);
		timePan.add(textPanel);
		
		//text fields for time simulation is running for
		textPanel.add(new SimLabel("Y:"));
		yearsText = getSimTextBox();
		textPanel.add(yearsText);

		textPanel.add(new SimLabel("D:"));
		daysText = getSimTextBox();
		textPanel.add(daysText);

		textPanel.add(new SimLabel("H:"));
		hoursText = getSimTextBox();
		textPanel.add(hoursText);

		textPanel.add(new SimLabel("M:"));
		minutesText = getSimTextBox();
		textPanel.add(minutesText);

		textPanel.add(new SimLabel("S:"));
		secondsText = getSimTextBox();
		textPanel.add(secondsText);

		//button to start looping
		SimButton startLoopButton = new SimButton();
		startLoopButton.setText("Start looping");
		startLoopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(!looping){
					//total time to simulate for
					long time = 0;
					//times that need to be multiplied to the total time for the specific unit
					long[] times = new long[]{365, 24, 60, 60, 100};
					//the text that needs to be converted into numbers
					String[] text = new String[]{
						yearsText.getText(), daysText.getText(), hoursText.getText(),
						minutesText.getText(), secondsText.getText()
					};
					
					//for each string, try to convert it to a number
					for(int i = 0; i < text.length; i++){
						try{
							//find the number
							long nextTime = Long.parseLong(text[i]);
							//if the time was greater than 0, then add the appropriate amount of time to the total time
							if(nextTime > 0){
								for(int j = i; j < times.length; j++){
									nextTime *= times[j];
								}
							}
							time += nextTime;
							//if the number was invalid, do nothing
						}catch(NumberFormatException err){}
					}
					loopSim(time);
				}
			}
		});
		timePan.add(pad.addPadding(startLoopButton));
		
		//button to stop looping
		SimButton stopLoopButton = new SimButton();
		stopLoopButton.setText("Stop looping");
		stopLoopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				stopLooping = true;
				looping = false;
			}
		});
		timePan.add(pad.addPadding(stopLoopButton));

		//button to loop forever
		SimButton loopButton = new SimButton();
		loopButton.setText("Run Forever");
		loopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				loopSim(-1);
			}
		});
		timePan.add(pad.addPadding(loopButton));
		
		//button to start looping the remaining time
		
		//finish setting up frame
		frame.pack();
	}
	
	/**
	 * Pause everything else in the simulator and as fast as possible, run the simulator for the given amount of time.<br>
	 * use -1 to loop forever
	 * @param time the amount of time, in 1/100 of a second
	 */
	public void loopSim(long time){
		if(looping) return;
		looping = true;
		stopLooping = false;
		
		boolean loopForever = time == -1;
		
		GameClock clock = getHandler().getClock();
		clock.setStopUpdates(true);
		clock.stopClock();
		
		//wait for the clock to finish working
		//if the waiting takes too long, kick out of the loop
		int maxKick = 10000;
		int kick = 0;
		while(kick < maxKick && (clock.isRendering() || clock.isUpdating())){
			kick++;
			try{
				Thread.sleep(1);
			}catch(InterruptedException e){e.printStackTrace();}
		}
		
		//if the looping got kicked, update everything and don't loop
		if(kick == maxKick){
			stopLooping = false;
			looping = false;
			clock.setStopUpdates(false);
			
			System.err.println("Failed to loop");
			getHandler().getSimGui().updateSimInfo();
			clock.startClock();
			return;
		}
		
		//set up gui for looping
		getHandler().getSimGui().renderBuffer();
		getHandler().getSimGui().getFrame().repaint();
		getHandler().getSimGui().updateSimInfo();
		
		//make and start the looping thread
		Thread loopingThread = new Thread(new Runnable(){
			@Override
			public void run(){
				for(long i = 0; (i < time || loopForever) && !stopLooping; i++){
					long timeLeft;
					if(loopForever){
						timeLeft = i;
					}
					else{
						timeLeft = time - i;
					}
					if(timeLeft % 100 == 99){
						long[] times = Creature.getAgeTime(timeLeft);
						yearsText.setText(times[0] + "");
						daysText.setText(times[1] + "");
						hoursText.setText(times[2] + "");
						minutesText.setText(times[3] + "");
						secondsText.setText(times[4] + "");
					}
					getHandler().updateSimFull();
				}
				
				clock.setStopUpdates(false);
				if(loopForever){
					yearsText.setText("0");
					daysText.setText("0");
					hoursText.setText("0");
					minutesText.setText("0");
					secondsText.setText("0");
				}
				stopLooping = false;
				looping = false;
				getHandler().getSimGui().updateSimInfo();
				getHandler().getClock().startClock();
			}
		});
		loopingThread.start();
	}
	
	/**
	 * Get a text box for an amount of time in this GUI
	 * @return
	 */
	public SimTextBox getSimTextBox(){
		SimTextBox text = new SimTextBox();
		text.setPreferredSize(new Dimension(40, 20));
		text.restrictInput(SimTextBox.RESTRICT_INTEGER);
		return text;
	}
	
	@Override
	public Window getFrame(){
		return frame;
	}
	
}
