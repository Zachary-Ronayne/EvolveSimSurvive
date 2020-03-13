package evolve.util.math;

/**
 * An object which represents a complex number with a real component and imaginary component.
 */
public class Complex{
	
	/**
	 * The real component of this {@link Complex} number
	 */
	private double r;
	/**
	 * The imaginary component of this {@link Complex} number
	 */
	private double i;
	
	/**
	 * Create a {@link Complex} number with the given real and imaginary components
	 * @param r The real component
	 * @param i The imaginary component
	 */
	public Complex(double r, double i){
		this.r = r;
		this.i = i;
	}
	
	/**
	 * Create a new {@link Complex} number equal to 0
	 */
	public Complex(){
		this(0, 0);
	}
	
	/**
	 * Add the given {@link Complex} number to this {@link Complex} number
	 * @param c the {@link Complex} number to add
	 */
	public void add(Complex c){
		setR(getR() + c.getR());
		setI(getI() + c.getI());
	}
	
	/**
	 * Subtract the given {@link Complex} number from this this {@link Complex} number
	 * @param c the {@link Complex} number to subtract
	 */
	public void sub(Complex c){
		setR(getR() - c.getR());
		setI(getI() - c.getI());
	}
	
	/**
	 * Raise this {@link Complex} number to the given value
	 * @param pow the power to take this {@link Complex} to
	 */
	public void pow(double pow){
		root(1 / pow);
	}
	
	/**
	 * Multiply the given {@link Complex} number to this {@link Complex} number
	 * @param c the {@link Complex} number to multiply
	 */
	public void mult(Complex c){
		double r = getR();
		double i = getI();
		setR(r * c.getR() - i * c.getI());
		setI(r * c.getI() + i * c.getR());
	}
	
	/**
	 * Multiply this {@link Complex} number by the given real constant
	 * @param d the constant
	 */
	public void mult(double d){
		setR(getR() * d);
		setI(getI() * d);
	}
	
	/**
	 * Divide this {@link Complex} number by the given complex number
	 * @param d the constant, if this value is 0, NaN will be stored for both the imaginary and real components
	 */
	public void div(Complex c){
		double r = getR();
		double i = getI();
		double div = c.getR()*c.getR() + c.getI()*c.getI();
		setR((r*c.getR() + i*c.getI()) / div);
		setI((i*c.getR() - r*c.getI()) / div);
	}
	
	/**
	 * Divide this {@link Complex} number by the given real constant
	 * @param d the constant, if this value is 0, NaN will be stored for both the real and imaginary components
	 */
	public void div(double d){
		setR(getR() / d);
		setI(getI() / d);
	}
	
	/**
	 * Get the absolute value of this {@link Complex} number
	 * @return the absolute value
	 */
	public double abs(){
		return Math.sqrt(getR() * getR() + getI() * getI());
	}
	
	/**
	 * Get the angle of this {@link Complex} number in polar coordinates
	 * @return the angle in radians
	 */
	public double angle(){
		return Math.atan2(getI(), getR());
	}
	
	/**
	 * Set this {@link Complex} number to a root of itself
	 * @param root the root
	 */
	public void root(double degree){
		double z = Math.pow(abs(), 1.0/degree);
		double a = angle();
		
		setR(z * Math.cos(a / degree));
		setI(z * Math.sin(a / degree));
	}
	
	/**
	 * Get the real component of this {@link Complex} number
	 * @return the real component
	 */
	public double getR(){
		return r;
	}
	/**
	 * Set the real component of this {@link Complex} number
	 * @param r the new real component
	 */
	public void setR(double r){
		this.r = r;
	}
	/**
	 * Get the imaginary component of this {@link Complex} number
	 * @return the imaginary component
	 */
	public double getI(){
		return i;
	}
	/**
	 * Set the real imaginary of this {@link Complex} number
	 * @param r the new imaginary component
	 */
	public void setI(double i){
		this.i = i;
	}
	
	/**
	 * Create a copy of this {@link Complex} number that is the same number but not the same object
	 * @return the copy
	 */
	public Complex copy(){
		return new Complex(getR(), getI());
	}
	
}
