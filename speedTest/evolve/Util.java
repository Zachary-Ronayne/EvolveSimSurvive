package evolve;

public class Util{
	
	/**
	 * Convert nanoseconds to seconds
	 * @param nano the number of nanoseconds
	 * @return the number of seconds
	 */
	public static double getSeconds(long nano){
		return nano / 1.0e9;
	}
	
}
