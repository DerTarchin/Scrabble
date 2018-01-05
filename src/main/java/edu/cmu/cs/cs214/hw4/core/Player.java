package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

/**
 * Player class represents each player in a scrabble game, with their rack,
 * score and other info
 * 
 * @author Hizal
 *
 */
public class Player {

	private int score;
	private String name;
	private ArrayList<Tile> rack = new ArrayList<Tile>();
	private ArrayList<SpecialTile> specials = new ArrayList<SpecialTile>();

	/**
	 * Initializes player name and score (to zero)
	 * 
	 * @param n
	 *            name of the player
	 */
	public Player(String n) {
		name = n;
		score = 0;
	}

	/**
	 * returns the name of the player
	 * 
	 * @return name of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the score of the player
	 * 
	 * @param s
	 *            updated score
	 */
	public void setScore(int s) {
		score = s;
	}

	/**
	 * returns the score
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * checks if player has the letter in their rack
	 * 
	 * @param letter
	 *            character to check
	 * @return if it is in their rack
	 */
	public boolean hasLetter(char letter) {
		for (int i = 0; i < rack.size(); i++)
			if (rack.get(i).getChar() == letter)
				return true;
		return false;
	}

	/**
	 * returns the player's rack
	 * 
	 * @return rack
	 */
	public ArrayList<Tile> getRack() {
		return rack;
	}

	/**
	 * returns a string form of the rack
	 * 
	 * @return string of rack tiles
	 */
	public String rackToString() {
		String r = "";
		for (int i = 0; i < rack.size(); i++)
			r += rack.get(i).getChar() + "(" + rack.get(i).getValue() + ") ";
		return r;
	}

	/**
	 * removes the given tile from the player's rack
	 * 
	 * @param tile
	 *            tile to be removed
	 */
	public void removeFromRack(Tile tile) {
		rack.remove(tile);
	}

	/**
	 * adds the given tile to the rack
	 * 
	 * @param tile
	 *            tile to be added
	 */
	public void addToRack(Tile tile) {
		rack.add(tile);
	}

	/**
	 * adds a special tile to the player's hand
	 * 
	 * @param s
	 *            special tile to add
	 */
	public void addSpecial(SpecialTile s) {
		specials.add(s);
	}

	/**
	 * returns list of all usable special tiles the player owns
	 * 
	 * @return list of special tiles
	 */
	public ArrayList<SpecialTile> getSpecialsList() {
		return specials;
	}

	/**
	 * removes a special tile that has been used
	 * 
	 * @param s
	 *            name of special tile to remove
	 */
	public void removeSpecial(SpecialTile s) {
		specials.remove(s);
	}

	@Override
	public String toString() {
		return name + " (" + score + ")";
	}
}
