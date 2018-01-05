package edu.cmu.cs.cs214.hw4.core;

/**
 * tile representing a letter with a point value, location and icon file
 * 
 * @author Hizal
 *
 */
public class LetterTile implements Tile {

	private char letter;
	private Coord loc;
	private int value;
	private String icon;

	/**
	 * initializes the letter tile
	 * 
	 * @param l
	 *            letter value
	 * @param v
	 *            point value
	 * @param i
	 *            icon filepath
	 */
	public LetterTile(char l, int v, String i) {
		letter = l;
		value = v;
		loc  = new Coord(-1, -1);
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
	public char getChar() {
		return letter;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return letter + "[" + value + "]" + loc.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LetterTile))
			return false;
		if (obj == this)
			return true;
		LetterTile c = (LetterTile) obj;
		return (this.letter == c.letter && this.loc.equals(c.loc));
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
