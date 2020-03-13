package evolve.util.math;

/**
 * A class that represents a matrix of values and can perform several operations on them.<br>
 * This Matrix class is intended for use with very small matrixes, row and column counts less than 10.<br>
 * Higher row and column counts should still return accurate results, but may have significant performance issues<br>
 * A note: I mainly needed this class for performing RREF operations, so that's all that's in here for now
 */
public class Matrix{
	
	/**
	 * The numerical entries in this matrix, kept track of as row, column
	 */
	private double[][] entries;

	/**
	 * Create a matrix out of the given entry matrix.<br>
	 * Must be a rectangular matrix
	 * @param entries
	 */
	public Matrix(double[][] entries){
		setEntries(entries);
	}
	
	/**
	 * Create a matrix with the given number of rows and columns.<br>
	 * All values are initialized to 0<br>
	 * r and c must be greater than 0
	 * @param r the number of rows
	 * @param c the number of columns
	 */
	public Matrix(int r, int c){
		if(r <= 0) throw new IllegalArgumentException("row count must be > 0 " + r);
		if(c <= 0) throw new IllegalArgumentException("column count must be > 0 " + c);
		
		entries = new double[r][c];
		for(int i = 0; i < entries.length; i++){
			for(int j = 0; j < entries[0].length; j++){
				entries[i][j] = 0;
			}
		}
	}

	/**
	 * Determine if the given row is in this matrix
	 * @param r the row
	 * @return true if the row is in the matrix, false otherwise
	 */
	public boolean rowIn(int r){
		return !(r < 0 || r >= getRowCount());
	}
	
	/**
	 * Determine if the given column is in this matrix
	 * @param c the row
	 * @return true if the column is in the matrix, false otherwise
	 */
	public boolean colIn(int c){
		return !(c < 0 || c >= getColCount());
	}
	
	/**
	 * Get the array of entries in this matrix.<br>
	 * The values in the returned array can be directly modified to change the values in this {@link Matrix}
	 * @return
	 */
	public double[][] getEntries(){
		return entries;
	}
	/**
	 * Set the entries of this {@link Matrix}<br>
	 * @param entries the new entries
	 * @throws IllegalArgumentException if entries is not a rectangular array or if the length of entries is 0
	 */
	public void setEntries(double[][] entries){
		if(entries.length <= 0) throw new IllegalArgumentException("row count must be > 0 ");

		int size = entries[0].length;
		if(size == 0) throw new IllegalArgumentException("column count must be > 0 ");
		
		for(int i = 0; i < entries.length; i++){
			if(entries[i].length != size) throw new IllegalArgumentException("entries must be a rectangular array");
		}
		
		this.entries = entries;
	}

	/**
	 * Get the entry of the given row and column
	 * @param r the row
	 * @param c the column
	 * @return the entry
	 */
	public double entry(int r, int c){
		return entries[r][c];
	}
	/**
	 * Set the entry of the given row and column.<br>
	 * Does nothing if r and c are outside the range of this {@link Matrix}
	 * @param value the value to set
	 * @param r the row
	 * @param c the column
	 */
	public void set(double value, int r, int c){
		if(!rowIn(r) || !colIn(c)) return;
		entries[r][c] = value;
	}
	
	/**
	 * Get the number of rows in this Matrix
	 * @return the number of rows
	 */
	public int getRowCount(){
		return entries.length;
	}
	/**
	 * Get the number of columns in this Matrix
	 * @return the number of columns
	 */
	public int getColCount() {
		return entries[0].length;
	}
	
	/**
	 * Get all the values in the last column of this {@link Matrix}
	 * @return
	 */
	public double[] getLastcolumn(){
		double[] last = new double[getRowCount()];
		for(int i = 0; i < last.length; i++){
			last[i] = entry(i, getColCount() - 1);
		}
		return last;
	}
	
	/**
	 * Sort the selected rows in descending order by absolute value of the value in the selected column.<br>
	 * The rows sorted are only the rows below and including the given startRow index.<br>
	 * This method uses selection sort, and should only be used on relatively small matrixes to maintain performance
	 * @param c the column to sort by
	 * @param startRow the row to start sorting by, will sort from this row to the bottom of the matrix
	 */
	public void sortRows(int c, int startRow){
		for(int i = startRow; i < getRowCount(); i++){
			double highValue = -1;
			int highIndex = -1;
			for(int j = i; j < getRowCount(); j++){
				double compare = entry(j, c);
				if(highIndex == -1 || Math.abs(highValue) < Math.abs(compare)){
					highIndex = j;
					highValue = compare;
				}
			}
			if(highIndex != -1) swapRows(i, highIndex);
		}
	}
	
	/**
	 * Swap the values of the two given rows.<br>
	 * Does nothing if either row index is outside this {@link Matrix}, or if the rows are equal
	 * @param r1 the first row
	 * @param r2 the second row
	 */
	public void swapRows(int r1, int r2){
		if(r1 == r2) return;
		
		if(!rowIn(r1) || !rowIn(r2)) return;
		
		for(int i = 0; i < getColCount(); i++){
			swapRowEntry(r1, r2, i);
		}
	}
	
	/**
	 * Swap the entries in the two given rows at the given column.<br>
	 * Does nothing if any of the values are outside this {@link Matrix}, or if the rows are equal
	 * @param r1
	 * @param r2
	 * @param c
	 */
	public void swapRowEntry(int r1, int r2, int c){
		if(r1 == r2) return;
		if(!rowIn(r1) || !rowIn(r2) || !colIn(c)) return;

		double temp = entry(r1, c);
		set(entry(r2, c), r1, c);
		set(temp, r2, c);
	}
	
	/**
	 * Multiply the given row by the given scalar.<br>
	 * Does nothing if r is outside this {@link Matrix}
	 * @param r the row
	 * @param scalar the scalar
	 */
	public void scale(int r, double scalar){
		if(!rowIn(r)) return;
		for(int i = 0; i < getColCount(); i++){
			set(entry(r, i) * scalar, r, i);
		}
	}
	
	/**
	 * Add one row multiplied by the scalar to another row 
	 * Does nothing if either row index is outside this {@link Matrix}
	 * @param row the row to multiply by the scalar
	 * @param add the row that will be added on to
	 * @param scalar the scalar to multiply row by
	 */
	public void addRow(int row, int add, double scalar){
		if(!rowIn(row) || !rowIn(add)) return;
		
		for(int i = 0; i < getColCount(); i++){
			set(entry(add, i) + entry(row, i) * scalar, add, i);
		}
	}
	
	/**
	 * Attempt to put this matrix in ref form
	 */
	public void ref(){
		Matrix copy = copy();
		
		//now for each row, divide the first row so that its index 0 value is 1,
		//then subtract that row from the rows below it so that all of their index 0 values are 0
		for(int i = 0; i < getColCount() && i < getRowCount(); i++){
			//first sort the rows by their order, putting leftmost column values of 0 on the bottom
			sortRows(i, i);
			
			//if the value at this current 0 index is 0, then stop trying to solve
			if(entry(i, i) == 0){
				setEntries(copy.getEntries());
				return;
			}
			
			//divide the first row
			scale(i, 1 / entry(i, i));
			//ensure that the current 0 entry is exactly 2
			set(1, i, i);
			
			//now add the current row to every other row and ensure the 0 index of that row is 0
			for(int j = i + 1; j < getRowCount(); j++){
				addRow(i, j, -entry(j, i));
				set(0.0, j, i);
			}
		}
		
		//now the array should be in ref form
	}

	/**
	 * Attempt to put this matrix in reff form.<br>
	 * Does nothing if this matrix is not at least 2 columns long
	 */
	public void rref(){
		if(getColCount() < 2) return;
		
		ref();
		
		//start at the bottom of the array and subtract scaled amounts of that row from the remaining top rows
		for(int i = getRowCount() - 1; i > 0; i--){
			for(int j = 0; j < i; j++){
				addRow(i, j, -entry(j, i));
				set(0.0, j, i);
			}
		}
	}
	
	@Override
	public String toString(){
		String s = "";
		for(int i = 0; i < getRowCount(); i++){
			for(int j = 0; j < getColCount(); j++){
				s += entry(i, j) + "\t";
			}
			s += "\n";
		}
		return s;
	}
	
	/**
	 * Make an exact copy of this matrix with the same size and entries that is a separate object
	 * @return
	 */
	public Matrix copy(){
		double[][] e = new double[getRowCount()][getColCount()];
		for(int i = 0; i < e.length; i++){
			for(int j = 0; j < e[0].length; j++){
				e[i][j] = entry(i, j);
			}
		}
		return new Matrix(e);
	}
	
}
