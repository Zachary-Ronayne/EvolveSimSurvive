package evolve.sim.misc;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Scanner;

import evolve.util.Saveable;

/**
 * A class that keeps track of a temperature in Celsius, and handles comparing and moving to other temperatures
 */
public class Temperature implements Saveable{

	/**
	 * the minimum temperature that will shown by overlayColor
	 */
	public static final double MIN_TEMP_DISP = -100;
	/**
	 * the maximum temperature that will shown by overlayColor
	 */
	public static final double MAX_TEMP_DISP = 200;
	
	/**
	 * The value of this temperature
	 */
	private double temp;

	/**
	 * Create a new Temperature object with the given temperature
	 * @param temp the temperature
	 */
	public Temperature(double temp) {
		setTemp(temp);
	}
	/**
	 * Create a new Temperature object with a temperature of 0
	 */
	public Temperature(){
		this(0);
	}
	
	/**
	 * Cause the temperature of this temperature to approach the given temperature, based on the given rate.<br>
	 * The higher the rate, the closer this temperature will be to the given temperature.<br>
	 * The further this temperature is from the given temperature, the closer this temperature will be to the given
	 * @param t the temperature to approach
	 * @param rate the rate to approach at<br>
	 * 			0 will result in no change<br>
	 * 			Values close to 0 will approach the given t slower.<br>
	 * 			Values further from 0 will approach the given t faster.
	 */
	public void approach(Temperature t, double rate){
		//if the rate to change is 0, then don't change temperature at all
		if(rate == 0) return;
		rate = 1.0 / rate;
		
		double diff = getTemp() - t.getTemp();
		double add = 0;

		if(diff < 0) add = -diff * (1 - rate / (-diff + rate));
		else if(diff > 0) add = -diff * (1 - rate / (diff + rate));
		
		setTemp(getTemp() + add);
	}
	
	public double getTemp(){
		return temp;
	}
	public void setTemp(double temp){
		this.temp = temp;
	}
	/**
	 * Add the given amount of temperature to this temperature
	 * @param temp the temperature to add
	 */
	public void addTemp(double temp) {
		setTemp(getTemp() + temp);
	}
	
	/**
	 * Get the overlay color for displaying this temperature.<br>
	 * This color is a transparent color, meant to represent how hot or cold something is.<br>
	 * When temperature is above 0, the color is a yellow tint<br>
	 * When temperature is below 0, the color is a blue tint<br>
	 * When temperature is 0, the color is invisible.<br>
	 * Above a certain maximum and below a certain minimum, the color will not get any more significantly different in change
	 * @return the overlay color
	 */
	public Color getOverlayColor(){
		return overlayColor(getTemp());
	}
	
	public double getBrightnessRange(){
		double tempRange =  MAX_TEMP_DISP - MIN_TEMP_DISP;
		return Math.max(0, Math.min(1, getTemp() / tempRange));
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			write.println(getTemp());
			return true;
		}catch(Exception e){
			return false;
		}
	}
	@Override
	public boolean load(Scanner read){
		try{
			setTemp(read.nextDouble());
			return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * Get the overlay color for displaying a temperature with the given value.<br>
	 * This color is a transparent color, meant to represent how hot or cold something is.<br>
	 * When temperature is above 0, the color is a yellow tint<br>
	 * When temperature is below 0, the color is a blue tint<br>
	 * When temperature is 0, the color is invisible.<br>
	 * Above a certain maximum and below a certain minimum, the color will not get any more significantly different in change
	 * @param temp the temperature to get the overlay color of
	 * @return the overlay color
	 */
	public static Color overlayColor(double temp){
		//these values are purely aethetic for changing the color of tiles based on temperature
		double tempPercMin = Math.pow(Math.max(0, Math.min(1, temp / MIN_TEMP_DISP)), 1.2);
		double tempPercMax = Math.pow(Math.max(0, Math.min(1, temp /  MAX_TEMP_DISP)), 1.2);
		
		if(temp < 0) return new Color(170, 170, 255, (int)(100 * tempPercMin));
		if(temp > 0) return new Color(255, 255, 100, (int)(130 * tempPercMax));
		return new Color(0, 0, 0, 0);
	
	}
	
}
