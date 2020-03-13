package evolve.util.math.vector;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * An instance of {@link Vector} that stores the x and y components of the vector
 */
public class PosVector extends Vector{
	
	/**
	 * The x component of this {@link Vector}
	 */
	private double x;
	/**
	 * The y component of this {@link Vector}
	 */
	private double y;
	
	public PosVector(double x, double y){
		setX(x);
		setY(y);
	}

	@Override
	public double getX(){
		return x;
	}
	@Override
	public void setX(double x){
		this.x = x;
	}

	@Override
	public double getY(){
		return y;
	}
	@Override
	public void setY(double y){
		this.y = y;
	}
	
	@Override
	public double getAngle(){
		return Math.atan2(getY(), getX());
	}

	@Override
	public void setAngle(double angle){
		AngleVector v = new AngleVector(angle, getMagnitude());
		setX(v.getX());
		setY(v.getY());
	}
	
	public double getMagnitude(){
		return Math.sqrt(getX() * getX() + getY() * getY());
	}

	@Override
	public void setMagnitude(double magnitude){
		AngleVector v = new AngleVector(getAngle(), magnitude);
		setX(v.getX());
		setY(v.getY());
	}
	
	@Override
	public Vector copy(){
		return new PosVector(x, y);
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			write.println(getX() + " " + getY());
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
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
