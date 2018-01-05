package edu.cmu.cs.cs214.hw4.core;

/**
 * An objects that stores a row and column integer
 * 
 * @author Hizal
 *
 */
public class Coord {

	private int row;
	private int col;

	/**
	 * initialize the coordinate object
	 * 
	 * @param r
	 *            row
	 * @param c
	 *            column
	 */
	public Coord(int r, int c) {
		row = r;
		col = c;
	}

	/**
	 * returns the row
	 * 
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * returns the column
	 * 
	 * @return column
	 */
	public int getCol() {
		return col;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coord))
			return false;
		if (obj == this)
			return true;
		Coord c = (Coord) obj;
		return (this.row == c.row && this.col == c.col);

	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		if (row < 0 || col < 0)
			return "(-,-)";
		return "(" + row + "," + col + ")";
	}
}
