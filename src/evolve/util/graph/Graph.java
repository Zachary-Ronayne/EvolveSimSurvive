package evolve.util.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;

import evolve.util.Camera;
import evolve.util.Saveable;

/**
 * A class that keeps track of options for scaling and drawing a graph.<br>
 * Data is stored in this class as an ArrayList of double arrays<br>
 * The rendering of the data should be handled by subclasses of this class.<br>
 * For this type of graph, the x axis is always kept track of as an integer, and graphs that extend this class 
 * should be used where there is a new data point at every integer.<br>
 * This graph will only represent data where the X axis values are positive
 */
public abstract class Graph implements Saveable{

	/**
	 * The minimum amount of space between vertical scale lines on the x axis
	 */
	public static final int X_LINE_SPACE = 28;
	
	/**
	 * The minimum amount of space between horizontal scale lines on the y axis
	 */
	public static final int Y_LINE_SPACE = 20;
	
	/**
	 * The data that this graph stores.<br>
	 * Each entry represents one set of data points on the graph.<br>
	 * All entries in the ArrayList must be the same size<br>
	 * Each index of an array in the ArrayList corresponds to the same set of data.
	 */
	private ArrayList<Double[]> data;
	
	/**
	 * The number of data sets within each entry in the data ArrayList.<br>
	 * This number cannot change after the object is created
	 */
	private int numberDataSets;
	
	/**
	 * The maximum number of data points that this graph can have.<br>
	 * Use zero or a negative number to hold infinite points.<br>
	 * If the maximum number of data points exists in the graph, then when a new data point is added 
	 * the oldest data point is removed
	 */
	private int maxDataPoints;

	/**
	 * The amount of extra pixels this graph has on the left side of the y axis. This space is where y axis labels are stored
	 */
	private double leftSpace;
	/**
	 * The amount of extra pixels this graph has on the right side, after the rightmost data point is drawn
	 */
	private double rightSpace;
	/**
	 * The amount of extra pixels this graph has at the top of the graph, from the highest y axis data point
	 */
	private double topSpace;
	/**
	 * The amount of extra pixels this graph has at the bottom of the graph, from the lowest y axis data point. 
	 * This space is where the x axis labels are stored
	 */
	private double bottomSpace;
	
	/**
	 * True if the X axis labels should be displayed, false otherwise
	 */
	private boolean displayXAxisLabels;
	
	/**
	 * True if the Y axis labels should be displayed, false otherwise
	 */
	private boolean displayYAxisLabels;
	
	/**
	 * True if the horizontal x axis lines should be drawn, false otherwise
	 */
	private boolean drawHorizontalLines;
	
	/**
	 * True if the vertical y axis lines should be drawn, false otherwise
	 */
	private boolean drawVerticalLines;
	
	/*
	 * True if the data for this graph should be drawn on top of the scale lines, false otherwise
	 */
	private boolean drawDataOnTop;
	
	/**
	 * The space between vertical lines on the x axis
	 */
	private double pixelXSpace;
	
	/**
	 * The space between horizontal lines on the y axis
	 */
	private double pixelYSpace;
	
	/**
	 * The highest data point in the data set
	 */
	private double highData;
	
	/**
	 * The lowest data point in the data set
	 */
	private double lowData;
	
	/**
	 * The total distance between the highest and lowest point of the data
	 */
	private double dataSize;
	
	/**
	 * The pixel coordinate of the origin
	 */
	private double pixelOrigin;
	
	/**
	 * The ratio of pixel size to data size of the graph
	 */
	private double dataPixelRatio;
	
	/**
	 * The background color of the graph
	 */
	private Color backgroundColor;
	
	/**
	 * The color of the scale lines of this graph
	 */
	private Color scaleLinesColor;
	/**
	 * The color of the text labels of this graph
	 */
	private Color labelColor;
	
	/**
	 * The font used by the axis labels
	 */
	private Font labelFont;
	
	/**
	 * The camera that this graph uses for display.<br>
	 * This object should not be accessed directly, but should use methods to move it around
	 */
	private Camera camera;
	
	/**
	 * The buffer that this graph is drawn to
	 */
	private BufferedImage graphImage;
	
	/**
	 * Create a graph with default settings.<br>
	 * 1 line, 100 pixels of extra space on each axis line, show both axis lines, 600x300 size
	 */
	public Graph(){
		this(1, -1, 100, 0, 0, 100, 600, 300);
	}
	
	/**
	 * Generate a graph with the given settings
	 * @param numberDataSets
	 * @param maxDataPoints
	 * @param leftSpace
	 * @param rightSpace
	 * @param topSpace
	 * @param bottomSpace
	 * @param displayXAxisLabels
	 * @param displayYAxisLabels
	 * @param width the amount of pixels in the width of this graph when not zoomed
	 * @param height the amount of pixels in the height of this graph when not zoomed
	 */
	public Graph(int numberDataSets, int maxDataPoints, double leftSpace, double rightSpace, double topSpace, double bottomSpace,
			double width, double height){
		
		this.numberDataSets = numberDataSets;
		this.maxDataPoints = maxDataPoints;
		this.leftSpace = leftSpace;
		this.rightSpace = rightSpace;
		this.topSpace = topSpace;
		this.bottomSpace = bottomSpace;
		this.backgroundColor = Color.WHITE;
		this.scaleLinesColor = new Color(70, 70, 70);
		this.labelColor = Color.BLACK;
		this.labelFont = new Font("Calibri", Font.PLAIN, 14);
		this.displayXAxisLabels = true;
		this.displayYAxisLabels = true;
		this.drawHorizontalLines = true;
		this.drawVerticalLines = true;
		this.drawDataOnTop = true;
		
		camera = new Camera(width, height);
		resetCamera();
		camera.setDrawOnlyInBounds(false);
		data = new ArrayList<Double[]>();
		graphImage = new BufferedImage((int)getTotalWidth(), (int)getTotalHeight(), BufferedImage.TYPE_4BYTE_ABGR);
	}
	
	/**
	 * Draw the graph to the buffer. Call this to update the state of the graph
	 */
	public void render(){
		//save the old render and zoom positions
		double oldX = camera.getX();
		double oldY = camera.getY();
		double oldZoomX = camera.getXZoomFactor();
		double oldZoomY = camera.getYZoomFactor();
		double oldWidth = camera.getWidth();
		double oldHeight = camera.getHeight();

		//set the camera position based on the origin
		camera.setX(oldX - getLeftSpace());
		camera.setY(oldY - getTopSpace());
		
		//calculate graph data for this render
		calculateGraphData();
		
		//create a new image of the correct size 
		graphImage = new BufferedImage((int)getTotalWidth(), (int)getTotalHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		//get the graphics object of the image and give it to the camera
		Graphics2D g = (Graphics2D)(graphImage.getGraphics());
		camera.setG(g);
		
		//draw the background
		g.setColor(getBackgroundColor());
		g.fillRect(0, 0, graphImage.getWidth(), graphImage.getHeight());
		
		//render the graph data if it should be on the bottom
		if(!isDrawDataOnTop()) renderGraph(camera, g);
		
		//render the background, scale lines, and data labels of the graph
		renderAndLabels();
		
		//render the graph data if it should be on top
		if(isDrawDataOnTop()) renderGraph(camera, g);
		
		//set the render and zoom positions back
		camera.setX(oldX);
		camera.setY(oldY);
		camera.setXZoomFactor(oldZoomX);
		camera.setYZoomFactor(oldZoomY);
		camera.setWidth(oldWidth);
		camera.setHeight(oldHeight);
	}
	
	/**
	 * Use the current camera to render the scale lines and data point labels
	 * @param origin
	 * @param xSpace
	 * @param ySpace
	 * @param ratio
	 */
	public void renderAndLabels(){
		Graphics2D g = (Graphics2D)camera.getG();
		if(g == null){
			g = (Graphics2D)graphImage.getGraphics();
			camera.setG(g);
		}
		g.setFont(getLabelFont());
		
		//draw horizontal lines along the y axis and labels if at least one of them should be drawn
		if(isDrawHorizontalLines() || isDisplayYAxisLabels()){
			//find the number of lines from the top of the screen to the origin draw position
			int topLines = (int)(getPixelOrigin() / getPixelYSpace());
			//find the number of lines from the bottom of the screen to the origin draw position
			int bottomLines = (int)((getHeight() - getPixelOrigin()) / getPixelYSpace());
			
			//find the total number of lines
			//add 2 to have an extra line on the top and bottom of the graph to ensure all lines are rendered if rounding errors happen
			int totalLines = topLines + bottomLines + 2;

			//iterate through each of the lines to see if it should be drawn
			//start at -1 to account for the extra line at the top and bottom of the lines
			for(int i = -1; i < totalLines; i++){
				//the index of the line based on it's position to the origin line
				//this is to ensure that the origin line is always drawn if applicable, and that lines above and below the origin are opposites
				int lineIndex = i - topLines;

				//the location on the graph, in pixel coordinates, that the line and label must be drawn at
				double y = getPixelOrigin() + lineIndex * getPixelYSpace();
				
				//the y location in actual pixels that the line will be drawn at
				int pixelY = (int)Math.round(y);
				
				//only draw the line if it is inside the graph's normal range for line positions
				if(pixelY >= getTopSpace() && pixelY <= getTotalHeight() - getBottomSpace()){
					
					//if horizontal lines should be drawn, draw them
					if(isDrawHorizontalLines()){
						g.setColor(getScaleLinesColor());
						//this line is the origin
						if(lineIndex == 0) g.setStroke(new BasicStroke(3));
						//this line is not the origin
						else g.setStroke(new BasicStroke(1));
						
						g.drawLine(
								(int)Math.round(getLeftSpace()), pixelY,
								(int)Math.round(getTotalWidth() - getRightSpace()), pixelY
						);
					}

					//if vertical lines should be drawn, draw them
					if(isDisplayYAxisLabels()){
						g.setColor(getLabelColor());
						//an object used for rounding decimal places
						BigDecimal rounder = new BigDecimal(
								//zoom in the pixel value of the amount of space a single line takes up based on the zoom level
								Camera.inverseZoom(
										-lineIndex * getPixelYSpace(), camera.getYZoomFactor()
								//then divide that by the ratio of data to pixels to get the actual data value of that line
								) / getDataPixelRatio()
						);
						rounder = rounder.setScale(4, RoundingMode.HALF_UP);
						
						//get the string
						String s = "" + rounder.doubleValue();
						
						g.drawString(s,
								(int)Math.round(getLeftSpace() - g.getFontMetrics().stringWidth(s) - 4),
								(int)Math.round(pixelY + g.getFont().getSize() * .45)
						);
					}
				}
			}
		}
		
		double xSpace = getPixelXSpace();
		
		//draw vertical lines along the x axis and labels if at least one of them should be drawn
		if(isDrawVerticalLines() || isDisplayXAxisLabels()){
			//the last position a line was drawn at
			double lastX = 0;
			
			//start at 0 and increment through each data point to see if each line needs to be drawn
			for(int i = 0; i < data.size(); i++){
				//the distance from the origin that this line would be drawn
				double x = i * xSpace;
				
				//the x camera pixel coordinate of the next line that would be drawn
				double newX = camera.drawX(x);
				
				//draw the line if this is the first line, or if the space it will take up is more than a set number of pixels
				if(i == 0 || newX - lastX >= X_LINE_SPACE){
					//the location the last line was drawn
					lastX = newX;
					
					//only draw the line if it is after the y axis label space
					if(lastX >= getLeftSpace()){
						//if the vertical lines should be drawn, draw them
						if(isDrawVerticalLines()){
							//this line is the origin
							if(i == 0) g.setStroke(new BasicStroke(3));
							//this line is not the origin
							else g.setStroke(new BasicStroke(1));
							
							//set the line color
							g.setColor(getScaleLinesColor());
							
							//draw the line
							g.drawLine(
									(int)Math.round(lastX), (int)Math.round(getTopSpace()),
									(int)Math.round(lastX), (int)Math.round(getTotalHeight() - getBottomSpace())
							);
						}
						
						//if the x axis labels should be drawn, draw them
						if(isDisplayXAxisLabels()){
							g.setColor(getLabelColor());
							String s = "" + i;
							g.drawString(s,
									(int)Math.round(lastX - g.getFontMetrics().stringWidth(s) / 2.0),
									(int)(getTotalHeight() - getBottomSpace() + g.getFont().getSize()));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Render the graph to the graphImage of this class with the given camera.<br>
	 * This method should only be used by the subclasses of this class.<br>
	 * This method generally should just handle drawing data, scale lines and the like are drawn automatically.<br>
	 * Use {@link dataToPixel()} to convert data points to their correct y coordinate as should be sent as parameters to camera draw functions<br>
	 * All values for calculating render values are automatically called before this method
	 * @param cam the camera to use while drawing, should draw graph assuming the camera is at (0, 0)
	 * @param g the graphics object to modify graphics settings
	 */
	protected abstract void renderGraph(Camera cam, Graphics2D g);
	
	/**
	 * Convert the given data to a pixel location that should be given to a camera object to render
	 * @param data the data
	 * @return the data as a pixel location
	 */
	public double dataToPixel(double data){
		//the amount of distance the data takes up
		double dataSize = getHighData() - getLowData();

		double dataPercent = (getHighData() - data) / dataSize;
		double pixelSize = getHeight();
		return pixelSize * dataPercent;
	}
	
	/**
	 * Calculate all values that must be calculated for rendering
	 */
	public void calculateGraphData() {
		//find highest and lowest values
		double low = 0;
		double high = 0;
		boolean foundHigh = false;
		boolean foundLow = false;
		for(int i = 0; i < data.size(); i++){
			Double[] set = data.get(i);
			for(int j = 0; j < set.length; j++){
				double d = set[j];
				if(low > d || !foundLow){
					low = d;
					foundLow = true;
				}
				if(high < d || !foundHigh){
					high = d;
					foundHigh = true;
				}
			}
		}
		lowData = low;
		highData = high;
		
		if(highData == lowData){
			highData++;
			lowData--;
		}
		
		//find the size of the graph
		dataSize = high - low;
		
		//find the origin as it would be drawn to the screen with the camera
		pixelOrigin = camera.drawY(dataToPixel(0));
		
		//find the ratio
		dataPixelRatio = getHeight() / getDataSize();
		
		//find x space
		if(data.size() == 0) pixelXSpace = getWidth();
		pixelXSpace = getWidth() / data.size();
		
		//find y space
		pixelYSpace = Y_LINE_SPACE;
	}

	/**
	 * Must call calculateGraphData() before this method will return an accurate value
	 * @return The space between vertical lines on the x axis
	 */
	public double getPixelXSpace(){
		return pixelXSpace;
	}
	/**
	 * Must call calculateGraphData() before this method will return an accurate value
	 * @return The space between horizontal lines on the y axis
	 */
	public double getPixelYSpace(){
		return pixelYSpace;
	}
	/**
	 * Must call calculateGraphData() before this method will return an accurate value
	 * @return The highest data point in the data set
	 */
	public double getHighData(){
		return highData;
	}
	/**
	 * Must call calculateGraphData() before this method will return an accurate value
	 * @return The lowest data point in the data set
	 */
	public double getLowData(){
		return lowData;
	}
	/**
	 * Must call calculateGraphData() before this method will return an accurate value
	 * @return The total distance between the highest and lowest point of the data
	 */
	public double getDataSize(){
		return dataSize;
	}
	/**
	 * Must call calculateGraphData() before this method will return an accurate value
	 * @return The pixel coordinate of the origin as would be drawn with the camera
	 */
	public double getPixelOrigin(){
		return pixelOrigin;
	}
	/**
	 * Must call calculateGraphData() before this method will return an accurate value
	 * @return The ratio of pixel size to data size of the graph
	 */
	public double getDataPixelRatio(){
		return dataPixelRatio;
	}
	
	/**
	 * Get the BufferedImage that the graph is drawn on. Must call render() to update it to the current state of the graph
	 * @return
	 */
	public BufferedImage getGraphImage(){
		return graphImage;
	}

	public boolean isDrawHorizontalLines(){
		return drawHorizontalLines;
	}
	public void setDrawHorizontalLines(boolean drawHorizontalLines){
		this.drawHorizontalLines = drawHorizontalLines;
	}

	public boolean isDrawVerticalLines(){
		return drawVerticalLines;
	}
	public void setDrawVerticalLines(boolean drawVerticalLines){
		this.drawVerticalLines = drawVerticalLines;
	}

	public boolean isDrawDataOnTop(){
		return drawDataOnTop;
	}
	public void setDrawDataOnTop(boolean drawDataOnTop){
		this.drawDataOnTop = drawDataOnTop;
	}
	
	public Color getBackgroundColor(){
		return backgroundColor;
	}
	public void setBackgroundColor(Color backgroundColor){
		this.backgroundColor = backgroundColor;
	}
	
	public Color getScaleLinesColor(){
		return scaleLinesColor;
	}
	public void setScaleLinesColor(Color scaleLinesColor){
		this.scaleLinesColor = scaleLinesColor;
	}
	
	public Color getLabelColor(){
		return labelColor;
	}
	public void setLabelColor(Color labelColor){
		this.labelColor = labelColor;
	}
	
	public Font getLabelFont(){
		return labelFont;
	}
	public void setLabelFont(Font labelFont){
		this.labelFont = labelFont;
	}
	
	/**
	 * Add the given data point to the graph.
	 * @param data the data to add
	 * @return true if the data was added, false if the data was the incorrect size and was not added
	 */
	public boolean addDataPoint(Double[] data){
		if(data.length != numberDataSets) return false;
		
		this.data.add(data);
		keepDataInRange();
		return true;
	}
	
	/**
	 * Removes data points from the beginning of this grpah's data list until the amount of data points 
	 * in the graph is less than or equal to the maximum number of data points.<br>
	 * Does nothing if this graph holds unlimited data points, or if the number of data points is already less than 
	 * or equal to the maximum number of data points.
	 */
	private void keepDataInRange(){
		while(getMaxDataPoints() > 0 && data.size() > getMaxDataPoints()){
			data.remove(0);
		}
	}
	
	/**
	 * Get the data of this graph
	 * @return
	 */
	public ArrayList<Double[]> getData(){
		return data;
	}
	
	/**
	 * Set the data of this graph, will also reset the number of data sets.<br>
	 * Should override this method if other data in subclasses would be effected by this change<br>
	 * This does not store the reference to the data object, but the references of each data array in the data ArrayList of this object
	 * @param data the new data
	 */
	public void setData(ArrayList<Double[]> data){
		if(data == null) data = new ArrayList<Double[]>();
		
		if(this.data == null) this.data = new ArrayList<Double[]>();
		else this.data.clear();
		this.data.addAll(data);
		
		if(this.data.size() != 0) this.numberDataSets = this.data.get(0).length;
		keepDataInRange();
	}
	
	/**
	 * Remove all the data from the graph
	 */
	public void clearData(){
		data.clear();
	}
	
	/**
	 * Get the total number of sets of data in the data set
	 * @return the number of data sets
	 */
	public int getNumberDataSets(){
		return numberDataSets;
	}
	
	public int getMaxDataPoints(){
		return maxDataPoints;
	}
	/**
	 * Set the maximum number of data points that this graph can display.<br>
	 * Use zero or a negative number to have an infinite number of data points
	 * @param maxDataPoints the number of points
	 */
	public void setMaxDataPoints(int maxDataPoints){
		this.maxDataPoints = maxDataPoints;
	}
	
	public double getLeftSpace(){
		return leftSpace;
	}
	/**
	 * Set the space on the left side, cannot be negative
	 * @param leftSpace the new space value
	 */
	public void setLeftSpace(double leftSpace){
		this.leftSpace = Math.max(0, leftSpace);
	}
	
	public double getRightSpace(){
		return rightSpace;
	}
	/**
	 * Set the space on the right side, cannot be negative
	 * @param rightSpace the new space value
	 */
	public void setRightSpace(double rightSpace){
		this.rightSpace = Math.max(0, rightSpace);
	}

	public double getTopSpace(){
		return topSpace;
	}
	/**
	 * Set the space on the top side, cannot be negative
	 * @param topSpace the new space value
	 */
	public void setTopSpace(double topSpace){
		this.topSpace = Math.max(0, topSpace);
	}

	public double getBottomSpace(){
		return bottomSpace;
	}
	/**
	 * Set the space on the bottom side, cannot be negative
	 * @param bottomSpace the new space value
	 */
	public void setBottomSpace(double bottomSpace){
		this.bottomSpace = Math.max(0, bottomSpace);
	}

	public boolean isDisplayXAxisLabels(){
		return displayXAxisLabels;
	}
	public void setDisplayXAxisLabels(boolean displayXAxisLabels){
		this.displayXAxisLabels = displayXAxisLabels;
	}

	public boolean isDisplayYAxisLabels(){
		return displayYAxisLabels;
	}
	public void setDisplayYAxisLabels(boolean displayYAxisLabels){
		this.displayYAxisLabels = displayYAxisLabels;
	}
	
	/**
	 * Get the width of the space that this graph takes up, not including the space on the sides
	 * @return
	 */
	public double getWidth(){
		return camera.getWidth();
	}
	/**
	 * Get the total width that this graph takes up
	 * @return
	 */
	public double getTotalWidth(){
		return getWidth() + getLeftSpace() + getRightSpace();
	}
	/**
	 * Set the width of this graph. This width is the space the graph itself takes up, not the extra space on the sides.
	 * @param width 
	 */
	public void setWidth(double width){
		camera.setWidth(width);
	}
	
	/**
	 * Get the height of the space that this graph takes up, not including the space on the sides
	 * @return
	 */
	public double getHeight(){
		return camera.getHeight();
	}
	/**
	 * Get the total height that this graph takes up
	 * @return
	 */
	public double getTotalHeight(){
		return getHeight() + getTopSpace() + getBottomSpace();
	}
	/**
	 * Set the height of this graph. This height is the space the graph itself takes up, not the extra space on the sides.
	 * @param height 
	 */
	public void setHeight(double height){
		camera.setHeight(height);
	}
	
	/**
	 * Set the anchor positions for the camera
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void setAnchorPos(double x, double y){
		camera.setAnchor(x, y);
	}
	
	/**
	 * Remove the anchor position of the camera
	 */
	public void removeAnchor(){
		camera.releaseAnchor();
	}
	
	/**
	 * Pan the camera to the given coordinates based on the anchor position
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void pan(double x, double y){
		camera.pan(x, y);
	}
	
	/**
	 * Zoom in the camera at the given coordinates
	 * @param x the x coordinate to zoom in at
	 * @param y the y coordinate to zoom in at
	 * @param direction the direction, a constant is raised to this power to zoom. 
	 * Use negative numbers to zoom out, use positive numbers to zoom in
	 * @param zoomX true if the x axis should be zoomed in, false otherwise
	 * @param zoomY true if the y axis should be zoomed in, false otherwise
	 */
	public void zoom(double x, double y, double direction, boolean zoomX, boolean zoomY){
		if(zoomX && !zoomY) camera.zoomInX(x, y, direction);
		else if(!zoomX && zoomY) camera.zoomInY(x, y, direction);
		else if(zoomX && zoomY) camera.zoomIn(x, y, direction);
	}
	
	/**
	 * Reset the camera to a default state so that the entire graph is displayed
	 */
	public void resetCamera(){
		camera.setX(0);
		camera.setY(0);
		camera.setXZoomFactor(0);
		camera.setYZoomFactor(0);
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			boolean success = true;

			//save misc data
			write.println(
					numberDataSets + " " + getMaxDataPoints() + " " +
					getLeftSpace() + " " + getRightSpace() + " " + getTopSpace() + " " + getBottomSpace() + " " +
					isDisplayXAxisLabels() + " " + isDisplayYAxisLabels() + " " + isDrawHorizontalLines() + " " + isDrawVerticalLines() + " " + 
					getBackgroundColor().getRed() + " " + getBackgroundColor().getGreen() + " " + getBackgroundColor().getBlue() + " " + getBackgroundColor().getAlpha() + " " +
					getScaleLinesColor().getRed() + " " + getScaleLinesColor().getGreen() + " " + getScaleLinesColor().getBlue() + " " + getScaleLinesColor().getAlpha() + " " +
					getLabelColor().getRed() + " " + getLabelColor().getGreen() + " " + getLabelColor().getBlue() + " " + getLabelColor().getAlpha() + "\n" +
					getLabelFont().getName() + "\n" + getLabelFont().getStyle() + " " + getLabelFont().getSize());
			
			//save camera
			success &= camera.save(write);
			
			//save data
			write.println(data.size());
			for(Double[] list : data){
				for(Double d : list) write.print(d + " ");
				write.println();
			}
			
			return success;
		}catch(Exception e){
			System.err.println("Failed to save graph");
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean load(Scanner read){
		try{
			boolean success = true;
			
			//load misc data
			numberDataSets = read.nextInt();
			setMaxDataPoints(read.nextInt());
			setLeftSpace(read.nextDouble());
			setRightSpace(read.nextDouble());
			setTopSpace(read.nextDouble());
			setBottomSpace(read.nextDouble());
			setDisplayXAxisLabels(read.nextBoolean());
			setDisplayYAxisLabels(read.nextBoolean());
			setDrawHorizontalLines(read.nextBoolean());
			setDrawVerticalLines(read.nextBoolean());
			setBackgroundColor(new Color(read.nextInt(), read.nextInt(), read.nextInt(), read.nextInt()));
			setScaleLinesColor(new Color(read.nextInt(), read.nextInt(), read.nextInt(), read.nextInt()));
			setLabelColor(new Color(read.nextInt(), read.nextInt(), read.nextInt(), read.nextInt()));
			read.nextLine();
			String name = read.nextLine();
			int style = read.nextInt();
			int fSize = read.nextInt();
			setLabelFont(new Font(name, style, fSize));
			
			//load camera
			camera = new Camera(0, 0);
			success &= camera.load(read);
			
			//load data
			data = new ArrayList<Double[]>();
			int size = read.nextInt();
			for(int i = 0; i < size; i++){
				Double[] entry = new Double[numberDataSets];
				for(int j = 0; j < entry.length; j++){
					entry[j] = read.nextDouble(); 
				}
				data.add(entry);
			}
			
			return success;
		}catch(Exception e){
			System.err.println("Failed to load graph");
			e.printStackTrace();
			return false;
		}
	}
	
}
