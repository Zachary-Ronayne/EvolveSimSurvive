package evolve.util.math.vector;

import evolve.util.Saveable;

/**
 * A class that handles calculating a 2D vector.<br>
 * Subclasses of this class {@link PosVector} and {@link AngleVector} are used to define how the vector is stored.<br>
 * This abstract class holds no actual fields and has no constructor
 */
public abstract class Vector implements Saveable{
	
	/**
	 * Add the given vector to this vector
	 * @param v the vector
	 */
	public void add(Vector v){
		setX(getX() + v.getX());
		setY(getY() + v.getY());
	}
	
	/**
	 * Subtract the given vector from this vector
	 * @param v the vector
	 */
	public void sub(Vector v){
		setX(getX() - v.getX());
		setY(getY() - v.getY());
	}
	
	/**
	 * multiply this vector by the given constant
	 * @param c the constant
	 */
	public void mult(double c){
		setX(getX() * c);
		setY(getY() * c);
	}
	
	/**
	 * Get the projection of vector v onto this vector
	 * @param v the vector to project
	 * @return the projection
	 */
	public Vector projection(Vector v){
		Vector a = copy();
		a.mult(dot(v) / dot(this));
		return a;
	}
	
	/**
	 * Get the dot product of the given vector and this vector
	 * @param v the other vector
	 * @return the dot product
	 */
	public double dot(Vector v){
		return getX() * v.getX() + getY() * v.getY();
	}
	
	/**
	 * Get the x component of this {@link Vector}
	 * @return the x component
	 */
	public abstract double getX();
	
	/**
	 * Set the x component of this {@link Vector}
	 * @param the new x component
	 */
	public abstract void setX(double x);

	/**
	 * Get the y component of this {@link Vector}
	 * @return the y component
	 */
	public abstract double getY();
	
	/**
	 * Set the y component of this {@link Vector}
	 * @param the new y component
	 */
	public abstract void setY(double y);

	/**
	 * Get the magnitude of this vector of this vector
	 * @return the magnitude
	 */
	public abstract double getMagnitude();
	
	/**
	 * Set the magnitude of this vector of this vector based on it's current angle
	 * @param magnitude the new magnitude
	 */
	public abstract void setMagnitude(double magnitude);

	/**
	 * Get the angle of this {@link Vector} in radians
	 * @return the angle in radians
	 */
	public abstract double getAngle();
	
	/**
	 * Set the angle of this {@link Vector}
	 * @param angle a the new angle in radians
	 */
	public abstract void setAngle(double angle);
	
	/**
	 * Create a vector whose x, y, magnitude, and angle values will all evaluate to the same as this {@link Vector}, but is a seperate object
	 * @return the copy
	 */
	public abstract Vector copy();
	
}
