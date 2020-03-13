package evolve.util.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import evolve.util.Camera;

public class LineGraph extends Graph{
	
	/**
	 * The line details of this graph
	 */
	private LineGraphDetail[] lineDetails;
	
	/**
	 * Create a new LineGraph with the given number of lines
	 * @param lines the lines, the length of this list is the number of lines, none of these entries can be null
	 */
	public LineGraph(LineGraphDetail[] lines){
		super(lines.length, -1, 100, 0, 0, 100, 500, 200);
		this.lineDetails = lines;
	}
	
	@Override
	protected void renderGraph(Camera cam, Graphics2D g){
		ArrayList<Double[]> data = getData();
		double xSpace = getPixelXSpace();
		
		for(int i = 0; i < lineDetails.length; i++){
			//set the graphics settings to the line
			LineGraphDetail line = lineDetails[i];
			g.setColor(line.getColor());
			g.setStroke(new BasicStroke(line.getWeight()));
			
			//draw all points on the graph
			for(int j = 0; j < data.size() - 1; j++){
				
				Double d1 = dataToPixel(data.get(j)[i]);
				Double d2 = dataToPixel(data.get(j + 1)[i]);
				
				cam.drawLine(j * xSpace, d1, (j + 1) * xSpace, d2);
			}
		}
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			boolean success = true;
			
			success &= super.save(write);
			
			for(LineGraphDetail d : lineDetails) d.save(write);
			
			return success;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean load(Scanner read){
		try{
			boolean success = true;
			
			success &= super.load(read);
			
			lineDetails = new LineGraphDetail[getNumberDataSets()];
			for(int i = 0; i < lineDetails.length; i++){
				lineDetails[i] = new LineGraphDetail(0, new Color(0));
				lineDetails[i].load(read);
			}
			
			return success;
		}catch(Exception e){
			return false;
		}
	}
	
}
