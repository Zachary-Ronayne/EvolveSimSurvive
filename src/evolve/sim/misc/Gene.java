package evolve.sim.misc;

import java.io.PrintWriter;
import java.util.Scanner;

import evolve.sim.neuralNet.Mutateable;
import evolve.util.Saveable;

/**
 * An object that keeps track of a value and mutates it
 */
public class Gene implements Mutateable, Saveable{
	
	/**
	 * The value of this gene
	 */
	private double value;
	
	/**
	 * The minimum value of this gene
	 */
	private double min;
	
	/*
	 * The maximum value of this gene
	 */
	private double max;
	
	/**
	 * The scalar that determines how fast this gene can mutate
	 */
	private double scalar;
	
	/**
	 * True if this gene is a modulo gene, false otherwise
	 */
	private boolean modulo;
	
	/**
	 * Create a gene with the given values
	 * @param value
	 * @param min
	 * @param max
	 * @param scalar
	 */
	public Gene(double value, double min, double max, double scalar){
		this.scalar = scalar;
		this.min = min;
		this.max = max;
		this.modulo = false;
		setValue(value);
	}
	
	/**
	 * Create a gene with the given ranges and a random valid value in range of [min, max]
	 * @param min
	 * @param max
	 * @param scalar
	 */
	public Gene(double min, double max, double scalar){
		this(min + (max - min) * Math.random(), min, max, scalar);
	}
	
	/**
	 * Create a gene with the given value and a default min/max of [-1, 1] and a scalar of 1
	 * @param value
	 */
	public Gene(double value){
		this(value, -1, 1, 1);
	}

	@Override
	public void mutate(double mutability, double chance){
		setValue(Mutateable.randomValue(getMin(), getMax(), chance, Mutateable.mutateValue(getValue(), mutability * scalar, getMin(), getMax())));
	}
	
	/**
	 * Create a Gene that is a combination of this Gene's value and the given Gene's value.<br>
	 * Each value in the Genes are taken as a random weight average for for both genes
	 * @param parent the Gene to combine genetic information
	 * @return the combination Gene, null if one failed to generate
	 */
	public Gene parentCopy(Gene parent){
		Gene copy = copy();
		if(isModulo()){
			double normDiff = Math.abs(getValue() - parent.getValue());
			double compare = Mutateable.compareModular(getValue(), parent.getValue(), getMax());
			
			//if the difference in species is close enough to being centered, use a normal parentCopy
			if(Math.abs(normDiff - compare) < 0.00000001) copy.setValue(Mutateable.parentCopyValue(getValue(), parent.getValue()));
			//otherwise set the gene value in the modular sense
			else copy.setValue(Math.max(getValue(), parent.getValue()) + Mutateable.parentCopyValue(compare, 0));
		}
		else{
			copy.setValue(Mutateable.parentCopyValue(getValue(), parent.getValue()));
		}
		return copy;
	}
	
	public double getValue(){
		return value;
	}
	public void setValue(double value){
		if(modulo){
			double max = getMax();
			if(value > max){
				int times = (int)Math.abs(value / max);
				value -= max * times;
			}
			if(value < 0){
				int times = (int)Math.abs(value / max);
				value += max * times;
				if(value != 0) value = max + value;
			}
		}
		
		if(value > getMax()) value = getMax();
		else if(value < getMin()) value = getMin();
		this.value = value;
	}

	public double getMin(){
		return min;
	}
	public void setMin(double min){
		if(isModulo()) this.min = 0;
		else this.min = min;
	}

	public double getMax(){
		return max;
	}
	public void setMax(double max){
		if(isModulo()) this.min = 0;
		this.max = max;
	}
	
	public double getScalar(){
		return scalar;
	}
	public void setScalar(double scalar){
		this.scalar = scalar;
	}
	
	public boolean isModulo(){
		return modulo;
	}
	/**
	 * Set the modulo of this gene. If this gene is a modulo, it's min will always be 0
	 * @param modulo the new modulo state
	 */
	public void setModulo(boolean modulo){
		this.modulo = modulo;
		if(this.modulo) setMin(0);
	}
	
	public Gene copy(){
		return new Gene(getValue(), getMin(), getMax(), getScalar());
	}

	@Override
	public boolean save(PrintWriter write){
		try{
			if(getMin() == -1 && getMax() == 1) write.println(getValue() + " | " + getScalar());
			else write.println(getValue() + " / " + getMin() + " " + getMax() + " " + getScalar() + " " + isModulo());
			
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean load(Scanner read){
		try{
			value = read.nextDouble();
			
			if(read.next().equals("|")){
				min = -1;
				max = 1;
				scalar = read.nextDouble();
				modulo = false;
			}
			else{
				min = read.nextDouble();
				max = read.nextDouble();
				scalar = read.nextDouble();
				modulo = read.nextBoolean();
			}
			
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
