package evolve.sim.obj;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Scanner;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.misc.CreatureGlow;

public class UserCreature extends Creature{

	/**
	 * The index of the pressedButtons array that keeps track of the up button
	 */
	public static final int UP = 0;
	/**
	 * The index of the pressedButtons array that keeps track of the right button
	 */
	public static final int RIGHT = 1;
	/**
	 * The index of the pressedButtons array that keeps track of the down button
	 */
	public static final int DOWN = 2;
	/**
	 * The index of the pressedButtons array that keeps track of the left button
	 */
	public static final int LEFT = 3;
	/**
	 * The index of the pressedButtons array that keeps track of the eat button
	 */
	public static final int EAT = 4;
	/**
	 * The index of the pressedButtons array that keeps track of the fight button
	 */
	public static final int FIGHT = 5;
	/**
	 * The index of the pressedButtons array that keeps track of the vomit button
	 */
	public static final int VOMIT = 6;
	
	/**
	 * An array of which buttons the user has pressed down
	 */
	private boolean[] pressedButtons;
	
	public UserCreature(Simulation simulation){
		super(simulation);
		pressedButtons = new boolean[7];
		for(int i = 0; i < pressedButtons.length; i++) pressedButtons[i] = false;
		
		setGlow(new CreatureGlow(this, new Color(255, 0, 0, 127), 20));
		setEnergy(10000);
	}
	
	@Override
	public void update(){
		double angleChange = Main.SETTINGS.creatureAngleChange.value();
		double speedChange = Main.SETTINGS.creatureSpeedChange.value();
		
		if(pressedButtons[UP]) addSpeed(speedChange);
		if(pressedButtons[DOWN]) addSpeed(-speedChange);
		if(pressedButtons[RIGHT]) addAngle(-angleChange);
		if(pressedButtons[LEFT]) addAngle(angleChange);
		if(pressedButtons[EAT]) eat();
		if(pressedButtons[FIGHT]) attackNearestCreature();
		if(pressedButtons[VOMIT]) vomit();
		setShouldEat(pressedButtons[EAT]);
		setShouldVomit(pressedButtons[VOMIT]);
		
		super.update();
	}
	
	public boolean[] getPressedButtons(){
		return pressedButtons;
	}
	
	@Override
	public double getFur(){
		return 30;
	}
	
	@Override
	public double calculateRadius(){
		return 40;
	}
	
	@Override
	public boolean save(PrintWriter write){
		return super.save(write);
	}

	@Override
	public boolean load(Scanner read){
		return super.load(read);
	}
	
}
