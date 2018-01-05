package edu.cmu.cs.cs214.hw4.core;

/**
 * A Blank letter tile that acts like a wildcard and can take the place of any
 * letter, but has no point value
 * 
 * @author Hizal
 *
 */
public class BlankTile implements Tile {
	private char character;
	private int value;
	private Coord loc;
	private String icon;

	/**
	 * initializes blank tile class
	 * 
	 * @param i
	 *            icon filepath
	 */
	public BlankTile(String i) {
		character = '_';
		value = 0;
		loc = new Coord(-1, -1);
		icon = i;
	}

	@Override
	public void setLoc(Coord c) {
		loc = c;
	}

	@Override
	public void setIcon(String file) {
		icon = file;
	}

	@Override
	public String getIcon() {
		return icon;
	}

	@Override
	public Coord getLoc() {
		return loc;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public char getChar() {
		return character;
	}

	@Override
	public String toString() {
		return character + "[" + value + "]" + loc;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BlankTile))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
