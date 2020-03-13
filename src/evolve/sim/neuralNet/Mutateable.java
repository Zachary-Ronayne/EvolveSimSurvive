package evolve.sim.neuralNet;

/**
 * Describes an object that can be mutated.<br>
 * Also acts as a utility class for generic mutation calculations
 */
public interface Mutateable{
	
	/**
	 * Mutate this object using the given mutability
	 * @param mutability the mutability of the object
	 * @param chance the chance to completely randomize each value of this {@link Mutatable} object
	 */
	public void mutate(double mutability, double chance);
	
	/**
	 * Get a mutated version of the given value, kept in the range [-1, 1]
	 * @param value the value to mutate
	 * @return the mutated value
	 */
	public static double mutateValue(double value){
		return mutateValue(value, 1, -1, 1);
	}

	/**
	 * Get a mutated version of the given value, kept in the range [min, max]
	 * @param value the value to mutate
	 * @param modifier a value that is multiplied onto a random value before its kept in the range
	 * @param min the minimum value this mutation can have
	 * @param max the maximum value this mutation can have
	 * @return the mutated value
	 */
	public static double mutateValue(double value, double modifier, double min, double max){
		//make the initial mutation
		double newValue = value + 2 * (Math.random() - .5) * modifier;
		//if the value is below the minimum, move it back in range by a random amount, based on how much it went out of range
		if(newValue < min){
			double diff = min - newValue;
			newValue = min + diff * Math.random();
		}
		//if the value is above the maximum, move it back in range by a random amount, based on how much it went out of range
		else if(newValue > max){
			double diff = newValue - max;
			newValue = min + diff * Math.random();
		}
		
		//return the value, keeping it in the normal range
		return Math.max(min, Math.min(max, newValue));
	}
	
	/**
	 * Take a value, and have a random chance to make the value randomized between the given range 
	 * @param min the minimum randomized value
	 * @param max the maximum randomized value
	 * @param chance the chance to randomize the value
	 * @param value the value to return if it is not randomized
	 * @return
	 */
	public static double randomValue(double min, double max, double chance, double value){
		if(Math.random() > chance) return value;
		else return min + Math.random() * (max - min);
	}
	
	/**
	 * Get a random weighted average of the given value and parent value
	 * @param value
	 * @param parentValue
	 * @return
	 */
	public static double parentCopyValue(double value, double parentValue){
		double thisWeight = Math.random();
		double parentWeight = 1 - thisWeight;
		return thisWeight * value + parentWeight * parentValue;
	}
	
	/**
	 * Compare the two given values, and return the minimum distance in a modular sense of the two species.<br><br>
	 * For example (in all examples max is 1),
	 * if one value is 0.5, and the other is 0.6, the value returned is 0.1<br>
	 * if one value is 0.1, and the other is 0.9, then the value returned is 0.2<br>
	 * if one value is 0.0, and the other is 1.0, then the value returned is 0.0<br>
	 * if one value is 0.5, and the other is 1.0, the value returned is 0.5<br>
	 * @param v1 the first value
	 * @param v2 the second value
	 * @param max the maximum value before the value is returned, the minimum value must be 0
	 * @return the minimum distance in species, always in range [0.0, max / 2]
	 */
	public static double compareModular(double v1, double v2, double max){
		//find the distance the first value from 0 and the maximum value
		double low1 = v1;
		double high1 = max - low1;

		//find the distance the second value from 0 and the maximum value
		double low2 = v2;
		double high2 = max - low2;
		
		//find the distance between the values in the normal sense
		double compare = Math.abs(v1 - v2);
		
		//find the smallest value that the two values have between them
		compare = Math.min(low1 + low2, compare);
		compare = Math.min(low1 + high2, compare);
		compare = Math.min(high1 + low2, compare);
		compare = Math.min(high1 + high2, compare);
		
		//return that value
		return compare;
	}
	
}
