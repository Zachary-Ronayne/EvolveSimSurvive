package evolve.util.math;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import evolve.util.math.vector.PosVector;
import evolve.util.math.vector.Vector;

/**
 * A utility class used for calculating miscellaneous values
 */
public final class Calc{

	
	/**
	 * Determines the distance from the given point to the given line<br>
	 * Math comes from https://www.geeksforgeeks.org/check-line-touches-intersects-circle/
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 * @param x1 the x end point of the first line
	 * @param y1 the y end point of the first line
	 * @param x2 the x end point of the second line
	 * @param y2 the y end point of the second line
	 * @return
	 */
	public static double pointToLine(double x, double y, double x1, double y1, double x2, double y2){
		//if x1 == x2, the the line is vertical and needs to be calculated differently
		if(x1 == x2){
			return Math.abs(x - x1);
		}
		
		//determine values for line comparison
		double slope = (y2 - y1) / (x2 - x1);
		double intercept = y1 - slope * x1;
		double a = slope;
		double b = -1;
		double c = intercept;
		
		//first find distance from line to point
		return Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);
	}
	
	/**
	 * Determines if the given line segment intersects the given circle<br><br>
	 * 
	 * @param radius the radius of the circle
	 * @param x the x center of the circle
	 * @param y the y center of the circle
	 * @param x1 the x end point of the first line
	 * @param y1 the y end point of the first line
	 * @param x2 the x end point of the second line
	 * @param y2 the y end point of the second line
	 * @return true if the line and circle intersect, false otherwise
	 */
	public static boolean circleIntersectsLine(double radius, double x, double y, double x1, double y1, double x2, double y2){
		//using vector projection
		
		//find the vector from the first line point to the second point
		Vector line = new PosVector(x1 - x2, y1 - y2);
		//find the vector from the first line point to the circle's center
		Vector cirlce = new PosVector(x1 - x, y1 - y);
		//project the vector to the circle onto the line to the other line point
		Vector projection = line.projection(cirlce);
		
		//based on the projection, find the nearest point on the line to the circle
		Point2D.Double near = new Point2D.Double(x1 - projection.getX(), y1 - projection.getY());
		
		//find the length of the line segment
		double lineLength = Math.sqrt(Math.pow(x1- x2, 2) + Math.pow(y1 - y2, 2));
		
		//if the nearest point is inside the circle and the nearest point is one the line segment, then the line intersects the circle
		return near.distance(x, y) <= radius && near.distance(x1, y1) <= lineLength && near.distance(x2, y2) <= lineLength;
	}
	
	/**
	 * Determine which grid positions touch the given line segment.<br>
	 * The grid positions are between 0 and the given max for each axis.<br>
	 * @param x1 the x coordinate of the first end point on the line segment
	 * @param y1 the x coordinate of the first end point on the line segment
	 * @param x2 the y coordinate of the second end point on the line segment
	 * @param y2 the y coordinate of the second end point on the line segment
	 * @param size the size of an element in the grid on both the x and y axis
	 * @param sizeX the number of grid positions on the x axis
	 * @param sizeY the number of grid positions on the y axis
	 * @return an {@link ArrayList} of {@link Point} objects, where the x and y coordinates are positions in the grid
	 */
	public static ArrayList<Point> touchingGridPos(double x1, double y1, double x2, double y2, double size, int sizeX, int sizeY){
		ArrayList<Point> tiles = new ArrayList<Point>();
		//first check for if the x coordinates are equal and represent a vertical line, this case must be separate
		if(x1 == x2){
			//in this case, the line is a vertical line
			
			//find the length of the line, that is, the number of tiles the line touches
			int indexLength = (int)Math.round(Math.abs(Math.floor(y1) - Math.ceil(y2)) / size);
			//find the lowest index of the line to start adding points from
			int lowIndex = (int)(Math.min(y1, y2) / size);
			//find the x index of where all points will be added
			int xIndex = (int)(x1 / size);
			
			//from the calculated ranges, add the points
			for(int i = lowIndex; i < indexLength; i++){
				addPointInRange(tiles, new Point(xIndex, i), sizeX, sizeY);
			}
		}
		//if the line is not a vertical line, proceed with the normal algorithm
		else{
			//first scale everything down so that size can be treated as 1,
			//and coordinates can directly be mapped to integer tile positions, and without having to consider size
			x1 /= size;
			y1 /= size;
			x2 /= size;
			y2 /= size;
			
			//find the slope and y intercept of the line
			double m = (y1 - y2) / (x1 - x2);
			double b = y1 - m * x1;
			
			//define points based on which point should be the start and end point, the start point has the lowest x value
			Point2D.Double start;
			Point2D.Double end;
			if(x1 < x2){
				start = new Point2D.Double(x1, y1);
				end = new Point2D.Double(x2, y2);
			}
			else{
				start = new Point2D.Double(x2, y2);
				end = new Point2D.Double(x1, y1);
			}
			
			//define a point to keep track of the current position of where, on the line, is kept track of
			Point2D.Double current = new Point2D.Double(start.x, start.y);
			
			//find the distance of the line segment
			double distance = start.distance(end);
			
			//now go to the next tile on the x axis until the end point is reached or passed
			//the loop will end once all tiles have been found, meaning when the current point is further from the start point that the distance of the line
			while(distance > start.distance(current)){
				//first move the current point to the next x coordinate of the grid
				double oldX = Math.floor(current.x);
				double nextX = Math.ceil(current.x);
				if(current.x == nextX) nextX++;
				current.x = nextX;
				
				//now find the new y coordinate based on this new x coordinate
				double oldY = current.y;
				double nextY = m * nextX + b;
				//update the current y position
				current.y = nextY;
				//now do a check to make sure the nextY stays in the range of the line
				if(m > 0 && end.y < nextY) nextY -= Math.ceil(nextY) - Math.ceil(end.y);
				else if(m < 0 && end.y > nextY) nextY -= Math.floor(nextY) - Math.floor(end.y);
				
				//find the range of the tiles that now should be added to the list
				int lowY;
				int highY;
				if(oldY < nextY){
					lowY = (int)Math.floor(oldY);
					highY = (int)Math.ceil(nextY);
				}
				else{
					lowY = (int)Math.floor(nextY);
					highY = (int)Math.ceil(oldY);
				}
				
				//find the x index of the tiles that should be added
				int xIndex = (int)(Math.floor(oldX));
				
				//now iterate through that range and add all those tiles
				for(int i = lowY; i < highY; i++){
					addPointInRange(tiles, new Point(xIndex, i), sizeX, sizeY);
				}
			}
		}
		
		return tiles;
	}
	
	/**
	 * A helper method used by touchingGridPos for adding a point only when it is in the range.<br>
	 * The given point is added to the given list only if it is in range
	 * @param arr the list
	 * @param add the new point
	 * @param sizeX the number of grid positions on the x axis
	 * @param sizeY the number of grid positions on the y axis
	 */
	public static void addPointInRange(ArrayList<Point> arr, Point add, int sizeX, int sizeY){
		if(add.x >= 0 && add.x < sizeX && add.y >= 0 && add.y < sizeY) arr.add(add);
	}
	
	/**
	 * Get the {@link Complex} roots of a cubic equation based on the given equation in the form ax^3 + bx^2 + cx + d<br>
	 * This method can be costly in terms of execution time
	 * @param ca the cubed x coefficient a
	 * @param cb the squared x coefficient b
	 * @param cc the x coefficient c
	 * @param cd the constant added d
	 * @return a {@link Complex} array with the 3 roots of the given cubic function.<br>
	 * 			Some entries may be the same if the given cubic function has less than 3 unique roots
	 */
	public static Complex[] getCubicRoots(double ca, double cb, double cc, double cd){
		Complex[] roots = new Complex[3];

		Complex a = new Complex(ca, 0);
		Complex b = new Complex(cb, 0);
		Complex c = new Complex(cc, 0);
		Complex d = new Complex(cd, 0);
		
		//math for finding the roots from https://en.wikipedia.org/wiki/Cubic_function
		
		double delta0 = Math.pow(b.getR(), 2) - 3*a.getR()*c.getR();
		double delta1 = 2*Math.pow(b.getR(), 3) - 9*a.getR()*b.getR()*c.getR() + 27*Math.pow(a.getR(), 2)*d.getR();
		
		double discriminant = 
				18*a.getR()*b.getR()*c.getR()*d.getR() - 
				4*Math.pow(b.getR(), 3)*d.getR() + Math.pow(b.getR(), 2)*Math.pow(c.getR(), 2) - 
				4*a.getR()*Math.pow(c.getR(), 3) -
				27*Math.pow(a.getR(), 2)*Math.pow(d.getR(), 2);
		
		boolean discIs0 = discriminant == 0;
		boolean delta0Is0 = delta0 == 0;
		if(discIs0 && delta0Is0){
			double root = -b.getR() / (3 * a.getR());
			roots[0] = new Complex(root, 0);
			roots[1] = roots[0].copy();
			roots[2] = roots[0].copy();
		}
		else if(discIs0 && !delta0Is0){
			double root = (9*a.getR()*d.getR() - b.getR()*c.getR()) / (2*delta0);
			roots[0] = new Complex((4*a.getR()*b.getR()*c.getR() - 9*a.getR()*a.getR()*d.getR() - Math.pow(b.getR(), 3)) / (a.getR()*delta0), 0);
			roots[1] = new Complex(root, 0);
			roots[2] = roots[1].copy();
		}
		else{
			double cSqrt = -27*Math.pow(a.getR(), 2)*discriminant;
			Complex bigC = new Complex(cSqrt, 0);
			bigC.root(2);
			bigC.add(new Complex(delta1, 0));
			bigC.div(2);
			bigC.root(3);
			
			Complex r1;
			Complex r2;
			Complex r3;
			Complex delta = new Complex(delta0, 0);
			
			r2 = bigC.copy();
			r2.add(b);
			r1 = delta.copy();
			r1.div(bigC.copy());
			r2.add(r1);
			r1 = new Complex(-1/(3.0*a.getR()), 0);
			r2.mult(r1);
			
			roots[0] = r2.copy();

			
			Complex e = new Complex(-.5, .5 * Math.sqrt(3.0));
			
			r1 = e.copy();
			r1.mult(bigC);
			
			r2 = r1.copy();
			r3 = delta.copy();
			r3.div(r2);
			
			r1.add(r3);
			r1.add(b);
			r1.mult(-1/(3.0*a.getR()));
			
			roots[1] = r1;

			
			r1 = e.copy();
			r1.pow(2);
			r1.mult(bigC);
			
			r2 = r1.copy();
			r3 = delta.copy();
			r3.div(r2);
			
			r1.add(r3);
			r1.add(b);
			r1.mult(-1/(3.0*a.getR()));
			
			roots[2] = r1;
		}
		
		return roots;
	}
	
	/**
	 * Calculates the sigmoid function on x and returns it in a value of the range (-1, 1)
	 * @param x the input to the sigmoid
	 * @return the sigmoid value
	 */
	public static double sigmoid(double x){
		return (2 / (1 + Math.pow(Math.E, -x))) - 1;
	}
	
}
