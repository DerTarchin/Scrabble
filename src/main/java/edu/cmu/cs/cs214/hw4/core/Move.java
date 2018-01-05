package edu.cmu.cs.cs214.hw4.core;

import java.util.ArrayList;

/**
 * stores letter, score and other information for a move
 * 
 * @author Hizal
 *
 */
public class Move {
	private ArrayList<LetterTile> letters = new ArrayList<LetterTile>();
	private int score;
	private int mulFactor;
	private int addFactor;

	/**
	 * initializes move object and sets default variables
	 */
	public Move() {
		score = 0;
		mulFactor = 1;
		addFactor = 0;
	}

	/**
	 * sets the score
	 * 
	 * @param newScore
	 *            score to set
	 */
	public void setScore(int newScore) {
		score = newScore;
	}

	/**
	 * gets the current turn's score
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * gets the score with factors computed, wich are caused by special tiles
	 * 
	 * @return factored score
	 */
	public int getFactoredScore() {
		return (score + addFactor) * mulFactor;
	}
/**
 * sets multiplication factor for final score
 * @param factor amount to multiply score by
 */
	public void setMulFactor(int factor) {
		mulFactor = factor;
	}
/**
 * gets multiplication factor
 * @return multiplication factor
 */
	public int getMulFactor() {
		return mulFactor;
	}
/**
 * sets the factor to add to final score
 * @param factor amount to add to score
 */
	public void setAddFactor(int factor) {
		addFactor = factor;
	}
/**
 * gets the factor for addition
 * @return factor
 */
	public int getAddFactor() {
		return addFactor;
	}
/**
 * adds a letter tile to move's list
 * @param letter letter tile to add
 */
	public void add(LetterTile letter) {
		letters.add(letter);
	}
/**
 * removes letter tile from the given index
 * @param index index to remove
 */
	public void remove(int index) {
		letters.remove(index);
	}

	/**
	 * removes the letter tile given
	 * @param letter letter tile to remove
	 */
	public void remove(LetterTile letter) {
		letters.remove(letters.indexOf(letter));
	}
/**
 * checks if move contains the given letter
 * @param letter letter tile to check
 * @return true if contained
 */
	public boolean contains(LetterTile letter) {
		return letters.contains(letter);
	}
/**
 * get the size of the move's letters list
 * @return size
 */
	public int size() {
		return letters.size();
	}
/**
 * gets the letter tile at the given index
 * @param index index to check
 * @return letter tile
 */
	public LetterTile get(int index) {
		return letters.get(index);
	}
/**
 * clears the move of all letters
 */
	public void clear() {
		letters = new ArrayList<LetterTile>();
	}

	@Override
	public String toString() {
		return letters.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Move))
			return false;
		if (obj == this)
			return true;
		Move m = (Move) obj;
		if (this.letters.size() != m.letters.size())
			return false;
		for (int i = 0; i < this.letters.size(); i++)
			if (!this.letters.get(i).equals(m.letters.get(i)))
				return false;
		return true;
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
