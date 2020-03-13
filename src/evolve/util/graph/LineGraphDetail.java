package evolve.util.graph;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Scanner;

import evolve.util.Saveable;

/**
 * A class that describes details of a line on a LineGraph
 */
public class LineGraphDetail implements Saveable{
	
	/**
	 * The stroke weight of this line
	 */
	private float weight;
	/**
	 * The color of this line
	 */
	private Color color;
	
	/**
	 * Create a new detail with the given weight and color
	 * @param weight the line weight of the line
	 * @param color the color of the line
	 */
	public LineGraphDetail(float weight, Color color){
		super();
		this.weight = weight;
		this.color = color;
	}
	
	public float getWeight(){
		return weight;
	}
	public void setWeight(float weight){
		this.weight = weight;
	}
	
	public Color getColor(){
		return color;
	}
	public void setColor(Color color){
		this.color = color;
	}

	@Override
	public boolean save(PrintWriter write){
		try{
			Color c = getColor();
			write.println(getWeight() + " " + c.getRed() + " " + c.getGreen() + " " + c.getBlue() + " " + c.getAlpha());
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean load(Scanner read){
		try{
			setWeight(read.nextFloat());
			this.color = new Color(read.nextInt(), read.nextInt(), read.nextInt(), read.nextInt());
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
