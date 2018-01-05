package edu.cmu.cs.cs214.hw4.core;

/**
 * stores data and behavior information on special tiles including name, cost
 * and the action affecting on the game
 * 
 * @author Hizal
 *
 */
public interface SpecialTile {
	/**
	 * sets the current location of the special tile
	 * 
	 * @param c
	 *            coordinate
	 */
	void setLoc(Coord c);

	/**
	 * returns the name of the special tile
	 * 
	 * @return name
	 */
	String getName();

	/**
	 * returns the current location of the special tile
	 * 
	 * @return coordinate
	 */
	Coord getLoc();

	/**
	 * returns the icon filepath
	 * 
	 * @return filepath
	 */
	String getIcon();

	/**
	 * triggers the behavior of the tile
	 * 
	 * @param game
	 *            game object for higher level implications
	 * @param turnScore
	 *            score of current turn for turn-score related implications
	 * @return a string representing an alert / notification
	 */
	String activate(Game game, Move move);

	/**
	 * sets the owner of the special tile, for hiding purposes
	 * 
	 * @param p
	 *            Player that is the owner
	 */
	void setOwner(Player p);

	/**
	 * returns the owner of the special tile, for hiding purposes
	 * 
	 * @return Player
	 */
	Player getOwner();

	/**
	 * returns the cost of the special tile
	 * 
	 * @return cost
	 */
	int getPrice();

	/**
	 * returns a description of the object
	 * 
	 * @return description
	 */
	String getDescription();
}
