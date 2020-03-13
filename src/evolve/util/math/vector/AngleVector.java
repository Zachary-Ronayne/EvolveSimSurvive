package evolve.util.math.vector;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * An instance of {@link Vector} that stores the angle and magnitude of a vector
 */
public class AngleVector extends Vector{
	
	/**
	 * The angle of this {@link Vector}
	 */
	private double angle;
	/**
	 * The magnitude of this {@link Vector}
	 */
	private double magnitude;
	
	public AngleVector(double angle, double magnitude){
		setAngle(angle);
		setMagnitude(magnitude);
	}
	
	@Override
	public double getX(){
		return Math.cos(getAngle()) * getMagnitude();
	}

	@Override
	public void setX(double x){
		PosVector v = new PosVector(x, getY());
		setMagnitude(v.getMagnitude());
		setAngle(v.getAngle());
	}

	@Override
	public double getY(){
		return Math.sin(getAngle()) * getMagnitude();
	}

	@Override
	public void setY(double y){
		PosVector v = new PosVector(getX(), y);
		setMagnitude(v.getMagnitude());
		setAngle(v.getAngle());
	}

	@Override
	public double getMagnitude(){
		return magnitude;
	}

	@Override
	public void setMagnitude(double magnitude){
		this.magnitude = magnitude;
	}

	@Override
	public double getAngle(){
		return angle;
	}

	@Override
	public void setAngle(double angle){
		this.angle = angle;
	}

	@Override
	public Vector copy(){
		return new AngleVector(getAngle(), getMagnitude());
	}
	
	@Override
	public boolean save(PrintWriter write){
		try{
			write.println(getAngle() + " " + getMagnitude());
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean load(Scanner read){
		try{
			setAngle(read.nextDouble());
			setMagnitude(read.nextDouble());
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
