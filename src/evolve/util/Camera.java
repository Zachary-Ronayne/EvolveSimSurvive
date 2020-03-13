package evolve.util;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A class that handles camera movement. This allows a single object to be used to keep track of all movements of a camera, 
 * and then change the location on a component's associated graphics object based on the camera position and zoom level. 
 * All methods that have the same name as those in a Graphics object do the same thing, but take double values as parameters
 */
public class Camera implements Saveable{
	
	/**
	 * The x position of the camera, upper left hand corner
	 */
	private double camX;
	/**
	 * The y position of the camera, upper left hand corner
	 */
	private double camY;
	/**
	 * The width of the space that this camera takes up, for example the width of a GUI that uses this camera
	 */
	private double camWidth;
	/**
	 * The height of the space that this camera takes up, for example the height of a GUI that uses this camera
	 */
	private double camHeight;
	/**
	 * The amount that will be zoomed in on the x axis. Negative values zoom out, positive values zoom in.
	 */
	private double xZoomFactor;
	/**
	 * The amount that will be zoomed in on the y axis. Negative values zoom out, positive values zoom in.
	 */
	private double yZoomFactor;
	
	/**
	 * The anchor x position used for panning
	 */
	private double anchorX;
	/**
	 * The anchor y position used for panning
	 */
	private double anchorY;
	/**
	 * The x position the camera was in when the camera was anchored
	 */
	private double anchorCamX;
	/**
	 * They y position the camera was in when the camera was anchored
	 */
	private double anchorCamY;
	/**
	 * true if the camera currently has an anchor point set, false otherwise
	 */
	private boolean anchored;

	/**
	 * The minimum value that camX can take
	 */
	private double minX;
	/**
	 * The minimum value that camY can take
	 */
	private double minY;
	/**
	 * The maximum value that camX can take
	 */
	private double maxX;
	/**
	 * The maximum value that camY can take
	 */
	private double maxY;
	
	/**
	 * The graphics object that this camera should draw graphics to
	 */
	private Graphics2D g;
	
	/**
	 * True if objects should only be drawn when they are in bounds of the camera, false otherwise
	 */
	private boolean drawOnlyInBounds;
	
	/**
	 * Create a new empty camera
	 */
	public Camera(double camWidth, double camHeight){
		this.camWidth = camWidth;
		this.camHeight = camHeight;
		
		camX = 0;
		camY = 0;
		
		xZoomFactor = 0;
		yZoomFactor = 0;
		
		anchored = false;
		anchorX = 0;
		anchorY = 0;
		
		minX = Integer.MIN_VALUE;
		minY = Integer.MIN_VALUE;
		maxX = Integer.MAX_VALUE;
		maxY = Integer.MAX_VALUE;
		
		g = null;
		
		drawOnlyInBounds = true;
	}
	
	public boolean isAchored(){
		return anchored;
	}
	
	public double getAnchorX(){
		return anchorX;
	}
	public double getAnchorY(){
		return anchorY;
	}
	
	/**
	 * Set the anchor position of this camera, used for panning. When anchored, 
	 * all calls to pan the camera will be made relative to this anchor point. <br>
	 * The values should be points where a screen would have been clicked, not based
	 * on camera position.<br>
	 * A new anchor position is only set when the camera is not anchored
	 * @param anchorX the x position for the anchor
	 * @param anchorY the y position for the anchor
	 */
	public void setAnchor(double anchorX, double anchorY){
		if(!anchored){
			this.anchorX = anchorX;
			this.anchorY = anchorY;
			
			this.anchorCamX = this.camX;
			this.anchorCamY = this.camY;
			
			anchored = true;
		}
	}
	
	/**
	 * If an anchor position is set, the anchor is released
	 */
	public void releaseAnchor(){
		anchored = false;
	}
	
	/**
	 * Based on the anchor position, pan the camera to the position given.<br>
	 * The position should again be given as where a screen was clicked and dragged, 
	 * not based on camera position.<br>
	 * This method does nothing if no anchor is been set
	 * @param panX the x position to pan to
	 * @param panY the y position to pan to
	 */
	public void pan(double panX, double panY){
		if(anchored){
			setX(this.anchorCamX + inverseZoom(anchorX - panX, xZoomFactor));
			setY(this.anchorCamY + inverseZoom(anchorY - panY, yZoomFactor));
		}
	}
	
	/**
	 * Set the given point as the center of the camera
	 * @param x the x position
	 * @param y the y position
	 */
	public void center(double x, double y){
		setX(x - inverseZoom(camWidth, xZoomFactor) / 2);
		setY(y - inverseZoom(camHeight, yZoomFactor) / 2);
	}
	
	/**
	 * Zoom in on both axis at the specified location on the camera
	 * @param x the x zoom position on the camera
	 * @param y the y zoom position on the camera
	 * @param direction the amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public void zoomIn(double x, double y, double direction){
		zoomInX(x, y, direction);
		zoomInY(x, y, direction);
	}

	/**
	 * Zoom in on only the x axis at the specified location on the camera
	 * @param x the x zoom position on the camera
	 * @param y the y zoom position on the camera
	 * @param direction the amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public void zoomInX(double x, double y, double direction){
		//save the old width of the zoomed camera before the zoom
		//inverse is used because width and height should change in opposite directions from the zoomFactors
		double oldWidth = inverseZoom(camWidth, xZoomFactor);
		
		//increase the zoom factor
		xZoomFactor += direction;
		
		//adjust the camera position based on where the zoom occurred
		
		//find the new width after the zoom
		double newWidth = inverseZoom(camWidth, xZoomFactor);
		
		//find the difference in width in distance changed
		double wDist = (oldWidth - newWidth);
		
		//move the x camera position based on the distance moved in the zoom,
		//and the ratio of the positions of the zoom position
		setX(camX + wDist * (x / camWidth));
	}

	/**
	 * Zoom in on only the y axis at the specified location on the camera
	 * @param x the x zoom position on the camera
	 * @param y the y zoom position on the camera
	 * @param direction the amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public void zoomInY(double x, double y, double direction){
		//save the old height of the zoomed camera before the zoom
		//inverse is used because width and height should change in opposite directions from the zoomFactors
		double oldHeight = inverseZoom(camHeight, yZoomFactor);
		
		//increase the zoom factor
		yZoomFactor += direction;
		
		//adjust the camera position based on where the zoom occurred
		
		//find the new width and height after the zoom
		double newHeight = inverseZoom(camHeight, yZoomFactor);
		
		//find the difference in height in distance changed
		double hDist = (oldHeight - newHeight);
		
		//move the y camera position based on the distance moved in the zoom,
		//and the ratio of the positions of the zoom position
		setY(camY + hDist * (y / camHeight));
	}
	
	/**
	 * Get the pixel coordinate of a value on the x axis as it should be used for a graphics call.<br>
	 * Generally should only be used by the inside of this class.
	 * @param value the value to find the coordinate of
	 * @return the coordinate
	 */
	public int drawX(double value){
		return (int)Math.round(zoom(value - camX, xZoomFactor));
	}
	
	/**
	 * Get the pixel coordinate of a value on the y axis as it should be used for a graphics call.<br>
	 * Generally should only be used by the inside of this class.
	 * @param value the value to find the coordinate of
	 * @return the coordinate
	 */
	public int drawY(double value){
		return (int)Math.round(zoom(value - camY, yZoomFactor));
	}

	/**
	 * Get the pixel width of a value that will be drawn to the x axis<br>
	 * @param value the value to get the pixel width of
	 * @return the pixel width
	 */
	public int drawW(double value){
		return (int)Math.round(zoom(value, xZoomFactor));
	}
	
	/**
	 * Get the pixel height of a value that will be drawn to the y axis<br>
	 * @param value the value to get the pixel height of
	 * @return the pixel height
	 */
	public int drawH(double value){
		return (int)Math.round(zoom(value, yZoomFactor));
	}

	/**
	 * Based on an x pixel coordinate, get the corresponding location based on where the camera is.
	 * @param value the pixel coordinate
	 * @return the x pixel coordinate of the camera
	 */
	public double mouseX(double value){
		return inverseZoom(value, xZoomFactor) + camX;
	}

	/**
	 * Based on an y pixel coordinate, get the corresponding location based on where the camera is.
	 * @param value the pixel coordinate
	 * @return the y pixel coordinate of the camera
	 */
	public double mouseY(double value){
		return inverseZoom(value, yZoomFactor) + camY;
	}

	public void fillRect(double x, double y, double width, double height){
		if(!inBounds(x, y, width, height)) return;
		g.fillRect(drawX(x), drawY(y), drawW(width), drawH(height));
	}
	
	public void fillOval(double x, double y, double width, double height){
		if(!inBounds(x, y, width, height)) return;
		g.fillOval(drawX(x), drawY(y), drawW(width), drawH(height));
	}
	
	public void drawLine(double x1, double y1, double x2, double y2){
		if(!inBounds(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2) + 1, Math.abs(y1 - y2) + 1)) return;
		g.drawLine(drawX(x1), drawY(y1), drawX(x2), drawY(y2));
	}
	public void drawString(String s, double x, double y){
		Font font = g.getFont();
		double fontSize = font.getSize();
		if(!inBounds(x, y - fontSize, g.getFontMetrics().stringWidth(s), fontSize)) return;
		g.drawString(s, drawX(x), drawY(y));
	}
	/**
	 * Draw a string that changes it's size based on the current scale of the camera.<br>
	 * The size is based on the current font set by this objects graphics object
	 * @param s the string to draw
	 * @param x the x position
	 * @param y the y position
	 */
	public void drawScaleString(String s, double x, double y){
		Font font = g.getFont();
		double fontSize = zoom(font.getSize(), yZoomFactor);
		g.setFont(new Font(font.getName(), font.getStyle(), (int)fontSize));
		if(!inBounds(x, y - fontSize, g.getFontMetrics().stringWidth(s), fontSize)) return;
		g.drawString(s, drawX(x), drawY(y));
	}
	
	/**
	 * Determine if the given rectangular bounds are within the size of the camera.<br>
	 * Essentially, determines if graphics the rectangular bounds will be rendered on the graphics object
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean inBounds(double x, double y, double width, double height){
		if(!isDrawOnlyInBounds()) return true;
		Rectangle2D.Double camBounds = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
		Rectangle2D.Double testBounds = new Rectangle2D.Double(
				zoom(x - camX, xZoomFactor), zoom(y - camY, yZoomFactor),
				zoom(width, xZoomFactor), zoom(height, yZoomFactor)
		);
		return camBounds.intersects(testBounds);
	}
	
	public double getX(){
		return camX;
	}
	public void setX(double x){
		this.camX = Math.max(minX, Math.min(maxX, x));
	}

	public double getY(){
		return camY;
	}
	public void setY(double y){
		this.camY = Math.max(minY, Math.min(maxY, y));
	}

	public double getWidth(){
		return camWidth;
	}
	public void setWidth(double camWidth){
		this.camWidth = camWidth;
	}
	public double getHeight(){
		return camHeight;
	}
	public void setHeight(double camHeight){
		this.camHeight = camHeight;
	}

	public double getXZoomFactor(){
		return xZoomFactor;
	}
	public void setXZoomFactor(double xZoomFactor){
		this.xZoomFactor = xZoomFactor;
	}

	public double getYZoomFactor(){
		return yZoomFactor;
	}
	public void setYZoomFactor(double yZoomFactor){
		this.yZoomFactor = yZoomFactor;
	}

	public double getMinX(){
		return minX;
	}
	public void setMinX(double minX){
		this.minX = minX;
	}

	public double getMinY(){
		return minY;
	}
	public void setMinY(double minY){
		this.minY = minY;
	}

	public double getMaxX(){
		return maxX;
	}
	public void setMaxX(double maxX){
		this.maxX = maxX;
	}

	public double getMaxY(){
		return maxY;
	}
	public void setMaxY(double maxY){
		this.maxY = maxY;
	}
	
	public Graphics2D getG(){
		return g;
	}
	public void setG(Graphics2D g){
		this.g = g;
	}

	public boolean isDrawOnlyInBounds(){
		return drawOnlyInBounds;
	}
	/**
	 * True if objects should only be drawn when they are in bounds of the camera, false otherwise
	 * @param drawOnlyInBounds
	 */
	public void setDrawOnlyInBounds(boolean drawOnlyInBounds){
		this.drawOnlyInBounds = drawOnlyInBounds;
	}
	
	/**
	 * Get the zoomed value of the given value based on the given zoom factor
	 * @param value the value to zoom
	 * @param factor the zoom factor
	 * @return the zoomed value
	 */
	public static double zoom(double value, double factor){
		return value * Math.pow(1.2, factor);
	}
	
	/**
	 * Get the zoomed value of the given value based on the given zoom factor, 
	 * where the zoom factor is inverted before it's multiplied by value
	 * @param value the value to zoom
	 * @param factor the zoom factor
	 * @return the zoomed value
	 */
	public static double inverseZoom(double value, double factor){
		return zoom(value, -factor);
	}

	@Override
	public boolean save(PrintWriter write){
		try{
			write.println(
					camX + " " + camY + " " + camWidth + " " + camHeight + " " + xZoomFactor + " " + yZoomFactor + " " + 
					minX + " " + minY + " " + maxX + " " + maxY + " " + isDrawOnlyInBounds()
			);
			
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean load(Scanner read){
		try{
			
			setX(read.nextDouble());
			setY(read.nextDouble());
			setWidth(read.nextDouble());
			setHeight(read.nextDouble());
			setXZoomFactor(read.nextDouble());
			setYZoomFactor(read.nextDouble());
			setMinX(read.nextDouble());
			setMinY(read.nextDouble());
			setMaxX(read.nextDouble());
			setMaxY(read.nextDouble());
			setDrawOnlyInBounds(read.nextBoolean());
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
