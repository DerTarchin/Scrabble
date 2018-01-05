package edu.cmu.cs.cs214.hw4.core;

/**
 * stores the data of a letter tile, including location, character and point
 * value
 * 
 * @author Hizal
 *
 */
public interface Tile {
	/**
	 * sets the location of the tile to the given coordinate
	 * 
	 * @param c
	 *            coordinate
	 */
	void setLoc(Coord c);

	/**
	 * sets the icon filepath
	 * 
	 * @param file
	 *            filepath
	 */
	void setIcon(String file);

	/**
	 * returns the icon filepath
	 * 
	 * @return filepath
	 */
	String getIcon();

	/**
	 * returns the location of the letter
	 * 
	 * @return coordinate
	 */
	Coord getLoc();

	/**
	 * returns the point value of the current letter
	 * 
	 * @return value
	 */
	int getValue();

	/**
	 * returns the character of the letter
	 * 
	 * @return letter character
	 */
	char getChar();

}
