package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

/**
 * represents each square on the board grid, storing information on the letters,
 * score modifiers and special tiles placed on the square
 * 
 * @author Hizal
 *
 */
public class Square {

	private String scoreModifier;
	private LetterTile letter;
	private ArrayList<SpecialTile> specials = new ArrayList<SpecialTile>();
	private int turnPlaced = 0;

	/**
	 * initializes the square, to default of no score modifiers, special tiles
	 * or letters
	 */
	public Square() {
		scoreModifier = null;
		letter = null;
	}
	
	/**
	 * sets the turn on which the letter was placed
	 * 
	 * @param turnPlaced turn number
	 */
	public void setTurnPlaced(int turnPlaced) {
		this.turnPlaced = turnPlaced;
	}
	
	/**
	 * gets the turn the letter tile was placed
	 * 
	 * @return turn
	 */
	public int getTurnPlaced() {
		return turnPlaced;
	}

	/**
	 * sets the score modifier to the given type
	 * 
	 * @param modifier
	 *            type of score modifier
	 */
	public void setScoreModifier(String modifier) {
		scoreModifier = modifier;
	}

	/**
	 * returns the score modifier
	 * 
	 * @return score modifier
	 */
	public String getScoreModifier() {
		return scoreModifier;
	}

	/**
	 * updates the given value or score based on the score modifier
	 * 
	 * @param value
	 *            either a score multiplier or score itself, to be updated
	 * @param type
	 *            letter or word score
	 * @return updated value
	 */
	public int updateScore(int value, String type) {
		// check the type of score to be modified, and the modifier amount
		if (scoreModifier == null)
			return value;
		else if (type.equals("letter")) {
			if (scoreModifier.equals("2xl"))
				return value * 2;
			else if (scoreModifier.equals("3xl"))
				return value * 3;
		} else if (type.equals("word"))
			if (scoreModifier.equals("2xw"))
				return value * 2;
			else if (scoreModifier.equals("3xw"))
				return value * 3;
		return value;
	}

	/**
	 * adds a letter to the square
	 * 
	 * @param tile
	 *            letter tile to add
	 */
	public void setLetter(LetterTile tile) {
		letter = tile;
	}

	/**
	 * removes the set letter of the square, setting to null
	 */
	public void removeLetter() {
		letter = null;
		turnPlaced = 0;
	}

	/**
	 * returns the letter tile on the square
	 * 
	 * @return letter, or null if none exists
	 */
	public LetterTile getLetter() {
		return letter;
	}

	/**
	 * adds a special tile to the board
	 * 
	 * @param s
	 *            special tile
	 */
	public void addSpecial(SpecialTile s) {
		if (letter == null)
			specials.add(s);
	}

	/**
	 * triggers any of the special tiles on the board, in order of addition,
	 * then sends any alerts if specials are triggered
	 * 
	 * @param game
	 *            game object for higher level interactions
	 * @param move
	 *            Move object that stores the info for the current turn
	 * @return a list of alerts/notifications from special tiles, or null
	 */
	public ArrayList<String> activateSpecials(Game game, Move move) {
		ArrayList<String> alerts = new ArrayList<String>();
		for (int i = 0; i < specials.size(); i++)
			alerts.add(specials.get(i).activate(game, move));
		// reset specials
		specials = new ArrayList<SpecialTile>();
		return alerts;
	}

	/**
	 * returns a list of special tiles on the square
	 * 
	 * @return list
	 */
	public ArrayList<SpecialTile> getSpecials() {
		return specials;
	}

	@Override
	public String toString() {
		if (letter != null)
			return letter.getChar() + " ";
		else if (specials.size() != 0)
			return "# ";
		else if (scoreModifier != null) {
			if (scoreModifier.equals("2xl"))
				return 1 + " ";
			else if (scoreModifier.equals("3xl"))
				return 2 + " ";
			else if (scoreModifier.equals("2xw"))
				return 3 + " ";
			else if (scoreModifier.equals("3xw"))
				return 4 + " ";
		}
		return "- ";
	}
}
