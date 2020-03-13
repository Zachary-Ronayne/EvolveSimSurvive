package evolve.util.math;

import java.awt.Point;

public class Circle{
	
	/**
	 * The x center of the circle
	 */
	private double x;
	/**
	 * The y center of the circle
	 */
	private double y;
	/**
	 * The radius of the circle
	 */
	private double radius;
	
	/**
	 * Create a {@link Circle} with the given values
	 * @param x the x center of the circle
	 * @param y the y center of the circle
	 * @param radius the radius of the circle
	 */
	public Circle(double x, double y, double radius){
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	/**
	 * Create a circle with radius 0 at the origin
	 */
	public Circle(){
		this(0, 0, 0);
	}
	
	/**
	 * Get the distance from the given point to this {@link Circle}
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 * @return the distance from the circle, 0 if the point is inside the circle
	 */
	public double distance(double x, double y){
		return Math.max(0, Point.distance(getX(), getY(), x, y) - getRadius());
	}
	
	/**
	 * Determine if the given point is inside this {@link Circle}
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 * @return true if the point is inside the {@link Circle}, false otherwise
	 */
	public boolean inCircle(double x, double y){
		return distance(x, y) == 0;
	}
	
	public double getX(){
		return x;
	}
	public void setX(double x){
		this.x = x;
	}
	public double getY(){
		return y;
	}
	public void setY(double y){
		this.y = y;
	}
	public double getRadius(){
		return radius;
	}
	public void setRadius(double radius){
		this.radius = radius;
	}
}
