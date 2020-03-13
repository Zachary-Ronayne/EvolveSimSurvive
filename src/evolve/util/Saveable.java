package evolve.util;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * An object that can be saved to a text file using PrintWriter and Scanner
 */
public interface Saveable{
	/**
	 * Save this object to the given PrintWriter
	 * @param write the PrintWriter
	 * @return true if the save was successful, false otherwise
	 */
	public boolean save(PrintWriter write);
	
	/**
	 * Load this object with the data from the given Scanner
	 * @param read the Scanner
	 * @return true if the load was successful, false otherwise
	 */
	public boolean load(Scanner read);
}
