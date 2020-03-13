package evolve.util.math;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * An object that keeps track of a parabola in the form f(x) = ax^2 + bx + c<br>
 * Additionally, there are two x coordinates that represent the bounds of the parabola on the x axis
 */
public class BoundedParabola{
	
	/**
	 * The a constant in the standard form of this parabola, f(x) = ax^2 + bx + c
	 */
	private double a;
	/**
	 * The b constant in the standard form of this parabola, f(x) = ax^2 + bx + c
	 */
	private double b;
	/**
	 * The b constant in the standard form of this parabola, f(x) = ax^2 + bx + c
	 */
	private double c;

	/**
	 * The low value of the bounds of the x axis of this parabola
	 */
	private double lowXBound;
	/**
	 * The high value of the bounds of the x axis of this parabola
	 */
	private double highXBound;
	
	/**
	 * Create a parabola object with the given values, a, b, c are based on f(x) = ax^2 + bx + c
	 * @param a The a constant in the standard form of this parabola
	 * @param b The b constant in the standard form of this parabola
	 * @param c The c constant in the standard form of this parabola
	 * @param bound1 one of the bounds of this parabola of the x axis
	 * @param bound2 the other bounds of this parabola of the x axis
	 */
	public BoundedParabola(double a, double b, double c, double bound1, double bound2){
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		lowXBound = Math.min(bound1, bound2);
		highXBound = Math.max(bound1, bound2);
	}
	
	/**
	 * Create a parabola object based on the given 3 points and the bounds<br>
	 * To generate a parabola, all 3 x coordiates cannot be equal, and none of the y coordinates can be equal
	 * @param x1 x coordinate of the first point
	 * @param y1 y coordinate of the first point
	 * @param x2 x coordinate of the second point
	 * @param y2 y coordinate of the second point
	 * @param x3 x coordinate of the third point
	 * @param y3 y coordinate of the third point
	 * @param bound1 one of the bounds of this parabola of the x axis
	 * @param bound2 the other bounds of this parabola of the x axis
	 */
	public BoundedParabola(double x1, double y1, double x2, double y2, double x3, double y3, double bound1, double bound2){
		generateFrom3Points(x1, y1, x2, y2, x3, y3);
		lowXBound = Math.min(bound1, bound2);
		highXBound = Math.max(bound1, bound2);
	}
	
	/**
	 * Get the y value of this parabola at the given point x
	 * @param x the x value on the parabola
	 * @return the y value on the parabola
	 */
	public double y(double x){
		return x * x * getA() + x * getB() + getC();
	}
	
	/**
	 * Get the two x values that the given y value has on the graph.<br>
	 * If the y value is the value of the vertex, the two values should be identical.<br>
	 * Will return null if the given y value is not on the graph
	 * @param y the y value to get x values from
	 * @return an array with 2 entries, each one being the values on the graph. null if the given y coordinate is not on the parabola
	 */
	public double[] getXValues(double y){
		double a = getA();
		double b = getB();
		double c = getC() - y;
		
		double radicand = b * b - 4 * a * c;
		if(radicand < 0) return null;
		
		double x1 = (-b + Math.sqrt(radicand)) / (2 * a);
		double x2 = (-b - Math.sqrt(radicand)) / (2 * a);
		return new double[]{x1, x2};
	}
	
	/**
	 * Generate a parabola based on the given 3 points and store it in this object<br>
	 * @param x1 x coordinate of the first point
	 * @param y1 y coordinate of the first point
	 * @param x2 x coordinate of the second point
	 * @param y2 y coordinate of the second point
	 * @param x3 x coordinate of the third point
	 * @param y3 y coordinate of the third point
	 * @throws IllegalArgumentException if all of the x values are equal, or if at least 2 of the y values are equal
	 */
	public void generateFrom3Points(double x1, double y1, double x2, double y2, double x3, double y3){
		if(x1 == x2 && x2 == x3){
			throw new IllegalArgumentException("All x coordinates cannot be equal to generate a parabola, " + x1);
		}
		if(y1 == y2 || y1 == y3 || y2 == y3){
			throw new IllegalArgumentException("Two y coordinates cannot be equal to generate a parabola, "
					+ "y1: " + y1 + ", y2: " + y2 + ", y3: " + y3);
		}
		
		//the matrix that represents all the coefficients of each of the a, b, and c values in a 3 
		Matrix m = new Matrix(new double[][]{
			{x1 * x1, x1, 1, y1},
			{x2 * x2, x2, 1, y2},
			{x3 * x3, x3, 1, y3}
		});
		m.rref();

		setA(m.entry(0, 3));
		setB(m.entry(1, 3));
		setC(m.entry(2, 3));
	}
	
	
	/**
	 * Get the approximate distance, within several decimal places, from the given (x, y) coordinate to this {@link BoundedParabola}<br>
	 * The distance obtained from this method accounts for the x limit points of this parabola.<br>
	 * This means that, for example, even if the given coordinate is on the parabola, but not in the range of 
	 * the parabola, then the returned value is not 0, but the distance to the nearest endpoint of the parabola
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the distance
	 */
	public double aproximateDistance(double x, double y){
		/*
		 * Math from working out the derivative of the distance formula from a constant point to the equation of a parabola
		 * 
		 * 0 = (.5 / sqrt((x - x1)^2 + (f(x) - y1)^2)) * (2(x - x1) + 2(f(x) - y1)(2ax + b))
		 * 
		 * basically, solve this for v
		 * 0 = .5 * (((v - x)^2 + (a * v^2 + bv + c - y)^2))^(-.5) * (2(v - x) + 2(a * v^2 + bv + c - y)(2a * v + b))
		 * 
		 * start by separating both terms into 2 equations
		 * 0 = (.5 / ((v - x)^2 + (a * v^2 + bv + c - y)^2))^(-.5)
		 * 0 = (2(v - x) + 2(a * v^2 + bv + c - y)(2a * v + b))
		 * 
		 * the later equation
		 * 0 = (2(v - x) + 2(av^2 + bv + (c - y))(2av + b))
		 * 0 = 2v - 2x + 2(av^2 + bv + (c - y))(2av + b)
		 * 
		 * as far as I can tell, only need to find the zeros of the later equation to find the minima and maxima of the origional distance formula derivative
		 * 
		 * 0 = 2v - 2x + 2(av^2 + bv + (c - y))(2av + b)
		 * 0 = 2v - 2x + 2(2a^2v^3 + abv^2 + 2abv^2 + b^2v + (2ac - 2ay)v + (bc - by))
		 * 0 = 2v - 2x + 4a^2v^3 + 2abv^2 + 4abv^2 + 2b^2v + (4ac - 4ay)v + (2bc - 2by)
		 * 0 = (4a^2)v^3 + (6ab)v^2 + (2b^2 + 4ac - 4ay + 2)v + (-2x + 2bc - 2by)
		 */
		
		//find the roots of the cubic where the cubic is ax^3 + bx^2 + cx + d
		double a = 4 * getA() * getA();
		double b = 6 * getA() * getB();
		double c = 2 * getB() * getB() + 4 * getA() * getC() - 4 * getA() * y + 2;
		double d = -2 * x + 2 * getB() * getC() - 2 * getB() * y;

		Complex[] roots = Calc.getCubicRoots(a, b, c, d);
		
		Point2D.Double[] points = new Point2D.Double[roots.length];
		
		//based on the minima and maxima, find the closest point in this parabola
		double lowDist = -1;
		for(int i = 0; i < points.length; i++){
			//only test the distance if the root is a real root
			if(Math.abs(roots[i].getI()) <= 0.000000000001) points[i] = new Point2D.Double(roots[i].getR(), y(roots[i].getR()));
			else points[i] = null;
			
			//do a test to not include points with NaN
			if(points[i] != null && (Double.isNaN(points[i].x) || Double.isNaN(points[i].y))) points[i] = null;
			
			//only test the point if it is not null and is in range of the parabola
			if(points[i] != null && points[i].x >= getLowXBound() && points[i].x <= getHighXBound()){
				double dist = points[i].distance(x, y);
				if(lowDist == -1 || dist < lowDist) lowDist = dist;
			}
		}
		
		//keep the distance in the range of this BoundedParabola
		double dist = Point.distance(x, y, getLowXBound(), y(getLowXBound()));
		if(lowDist == -1 || dist < lowDist) lowDist = dist;
		dist = Point.distance(x, y, getHighXBound(), y(getHighXBound()));
		if(lowDist == -1 || dist < lowDist) lowDist = dist;
		
		//still need account for x range limits
		return lowDist;
	}
	
	/**
	 * Find a rough guess for the distance from the given coordinate this {@link BoundedParabola}
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the distance guess
	 */
	public double guessDistance(double x, double y){
		double distance;
		
		//first see if the y value is outside the parabola
		//if it is, then return the distance from the given point to the vertex
		double[] xValues = getXValues(y);
		if(xValues == null){
			distance = getVertex().distance(x, y);
		}
		//otherwise, find the nearest point on the x or y axis and return the distance to that point
		else{
			Point2D.Double[] p = new Point2D.Double[3];
			p[0] = new Point2D.Double(x, y(x));
			p[1] = new Point2D.Double(xValues[0], y(xValues[0]));
			p[2] = new Point2D.Double(xValues[1], y(xValues[1]));
			
			double lowDist = -1;
			for(int i = 0; i < p.length; i++){
				//if the point is in range of the parabola
				if(p[i].x >= getLowXBound() && p[i].x <= getHighXBound()){
					double dist = p[i].distance(x, y);
					if(lowDist == -1 || dist < lowDist) lowDist = dist;
				}
				else{
					double dist = Point.distance(x, y, getLowXBound(), y(getLowXBound()));
					if(lowDist == -1 || dist < lowDist) lowDist = dist;
					dist = Point.distance(x, y, getHighXBound(), y(getHighXBound()));
					if(lowDist == -1 || dist < lowDist) lowDist = dist;
				}
			}
			distance = lowDist;
		}
		
		return distance;
	}
	
	/**
	 * Get the vertex of this {@link BoundedParabola}
	 * @return the vertex as a {@link Point2D.Double}
	 */
	public Point2D.Double getVertex(){
		//if a is 0 and thus the slope of the derivative is 0, then this is not a parabola, so return (0, 0) by default
		if(getA() == 0) return new Point2D.Double(0, 0);
		
		//find the derivative of this parabola as y = mx + b
		double m = 2 * getA();
		double b = getB();
		
		//find where the derivative is 0
		double x = -b / m;
		double y = y(x);
		
		return new Point2D.Double(x, y);
	}
	
	public double getA(){
		return a;
	}
	public void setA(double a){
		this.a = a;
	}
	
	public double getB(){
		return b;
	}
	public void setB(double b){
		this.b = b;
	}
	
	public double getC(){
		return c;
	}
	public void setC(double c){
		this.c = c;
	}
	
	public double getLowXBound(){
		return lowXBound;
	}
	/**
	 * Set the low x bound of this parabola, will automatically adjust both low and high bounds if this value is lower than the current low bounds
	 * @param highXBound
	 */
	public void setLowXBound(double lowXBound){
		if(lowXBound > getHighXBound()){
			this.lowXBound = this.highXBound;
			this.highXBound = lowXBound;
		}
		else{
			this.lowXBound = lowXBound;
		}
	}
	
	public double getHighXBound(){
		return highXBound;
	}
	/**
	 * Set the high x bound of this parabola, will automatically adjust both low and high bounds if this value is lower than the current low bounds
	 * @param highXBound
	 */
	public void setHighXBound(double highXBound){
		if(highXBound < getLowXBound()){
			this.highXBound = this.lowXBound;
			this.lowXBound = highXBound;
		}
		else{
			this.highXBound = highXBound;
		}
	}
	
}
