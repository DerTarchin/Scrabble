package edu.cmu.cs.cs214.hw4.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Board class contains a 2d array of Squares and can operate on the board by
 * adding, removing or modifiying letters or squares
 * 
 * @author Hizal
 *
 */
public class Board {

	private int len;
	private ArrayList<LetterTile> lettersInPlay;
	private Dictionary dict;
	private String filePath;
	private Square[][] board;

	/**
	 * initializes the board. SIZE SHOULD BE AN ODD NUMBER FOR NORMAL BOARDS
	 * 
	 * @param size
	 *            size of board length and width
	 * @param path
	 *            filepath for resources
	 */
	public Board(int size, String path) {
		filePath = path;
		dict = new Dictionary(filePath + "words_scrabble.txt");
		len = size;

		// initialize 2d array of Squares
		board = new Square[len][len];
		for (int r = 0; r < len; r++)
			for (int c = 0; c < len; c++)
				board[r][c] = new Square();

		// create a list that contains any letters on the board
		lettersInPlay = new ArrayList<LetterTile>();

		try {
			initScoreModifiers();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filePath
					+ "scoremodifiers.txt");
		}
	}

	/**
	 * inserts the given letters onto the board and updates the list of letters
	 * on the board
	 * 
	 * @param letters
	 *            list of letters
	 */
	public void insertWord(Move letters, int turn) {
		for (int i = 0; i < letters.size(); i++) {
			int row = letters.get(i).getLoc().getRow();
			int col = letters.get(i).getLoc().getCol();
			if (board[row][col].getLetter() == null)
				board[row][col].setTurnPlaced(turn);
			board[row][col].setLetter(letters.get(i));
			lettersInPlay.add(letters.get(i));
		}
	}

	/**
	 * returns a square at the given coordinate
	 * 
	 * @param row
	 *            row of board
	 * @param col
	 *            column of board
	 * @return Square at the location
	 */
	public Square getSquare(int row, int col) {
		if (row < 0 || col < 0 || row >= len || col >= len)
			return null;
		return board[row][col];
	}

	/**
	 * checks if the board contains the given letter
	 * 
	 * @param letter
	 *            the lettertile to be checked
	 * @return true if on the board
	 */
	public boolean contains(LetterTile letter) {
		for (int i = 0; i < lettersInPlay.size(); i++)
			if (letter.getChar() == lettersInPlay.get(i).getChar())
				return true;
		return false;
	}

	/**
	 * checks if the board contains any letters
	 * 
	 * @return true if there are no letters
	 */
	public boolean isEmpty() {
		if (lettersInPlay.size() > 0)
			return false;
		return true;
	}

	/**
	 * returns a list of possible moves for the first word on the board
	 * 
	 * @param letters
	 *            list of letters to try
	 * @return list of possible letter locations
	 */
	private ArrayList<Move> getFirstMoves(Move letters, ArrayList<Tile> rack) {
		// check if the word is a one-letter word
		if (letters.size() == 1 && !isWord(letters.get(0).getChar() + ""))
			return new ArrayList<Move>();
		// create list of possible moves
		ArrayList<Move> moves = new ArrayList<Move>();
		// create a coordinate at the center of the board
		Coord coord = new Coord(len / 2, len / 2);
		// move is a singular possible move for a list of letters
		Move move;
		// for each letter, check vertical and horizontal directions that the
		// word can be placed, starting at the given coordinate (the center)
		// add the move to the list of moves, if it hasn't already been added
		for (int j = 0; j < letters.size(); j++) {
			move = findVerticalAdj(letters, j, coord, rack);
			if (move != null && !moves.contains(move))
				moves.add(move);
			move = findHorizontalAdj(letters, j, coord, rack);
			if (move != null && !moves.contains(move))
				moves.add(move);
		}
		return moves;
	}

	/**
	 * return a list of possible locations for each word, which is defined by a
	 * list of letters
	 * 
	 * @param letters
	 *            list of letters in the move
	 * @param rack
	 *            list of letters in a player rack
	 * @return the list of possible moves
	 */
	public ArrayList<Move> getPossibilities(Move letters, ArrayList<Tile> rack) {
		// check for an empty letter list (invalid input)
		if (letters == null || letters.size() == 0)
			return new ArrayList<Move>();
		// check if the move is the first move of the game
		if (isEmpty())
			return getFirstMoves(letters, rack);
		// create list of possible moves
		ArrayList<Move> moves = new ArrayList<Move>();
		int r = 0;
		int c = 0;
		// create a list of empty squares adjacent to letters on the board
		ArrayList<Coord> adj = new ArrayList<Coord>();
		// for each letter on the board, check if the word can be added in the
		// vertical or horizontal direction using the letter already on the
		// board, which is compared with every letter in the word
		for (int i = 0; i < lettersInPlay.size(); i++) {
			for (int j = 0; j < letters.size(); j++) {
				if (lettersInPlay.get(i).getChar() == letters.get(j).getChar()) {
					// move is a singular possible move for a list of letters
					Move move;
					if (letters.size() > 1) {
						move = findVerticalIntersections(letters, j, i, rack);
						if (move != null && !moves.contains(move))
							moves.add(move);
						move = findHorizontalIntersections(letters, j, i, rack);
						if (move != null && !moves.contains(move))
							moves.add(move);
					}
				}
			}
		}
		// if there are enough blanks to cover for the letters that aren't in
		// the rack of the player, initialize the list of empty tiles adjacent
		// to tiles already on the board
		for (int i = 0; i < lettersInPlay.size(); i++) {
			r = lettersInPlay.get(i).getLoc().getRow();
			c = lettersInPlay.get(i).getLoc().getCol();
			if (r - 1 >= 0 && board[r - 1][c].getLetter() == null
					&& !adj.contains(new Coord(r - 1, c)))
				adj.add(new Coord(r - 1, c));
			if (c - 1 >= 0 && board[r][c - 1].getLetter() == null
					&& !adj.contains(new Coord(r, c - 1)))
				adj.add(new Coord(r, c - 1));
			if (r + 1 < len && board[r + 1][c].getLetter() == null
					&& !adj.contains(new Coord(r + 1, c)))
				adj.add(new Coord(r + 1, c));
			if (c + 1 < len && board[r][c + 1].getLetter() == null
					&& !adj.contains(new Coord(r, c + 1)))
				adj.add(new Coord(r, c + 1));
		}
		// move is a singular possible move for a list of letters
		Move move;
		// for every coordinate in the list of adjacent sqaures, check if
		// the word can be added in the vertical or horizontal direction,
		// using the coordinate as the starting point for each letter in the
		// word being checked
		for (int i = 0; i < adj.size(); i++) {
			for (int j = 0; j < letters.size(); j++) {
				move = findVerticalAdj(letters, j, adj.get(i), rack);
				if (move != null && !moves.contains(move))
					moves.add(move);
				move = findHorizontalAdj(letters, j, adj.get(i), rack);
				if (move != null && !moves.contains(move))
					moves.add(move);
			}
		}
		if (moves.size() == 0)
			return new ArrayList<Move>();
		return moves;
	}

	/**
	 * find any words in the vertical direction, with at least one letter
	 * intersecting with an existing letter
	 * 
	 * @param letters
	 *            list of letters to check
	 * @param letterIndex
	 *            index of the starting point in the list of letters
	 * @param coord
	 *            coordinate of the board to start at
	 * @return list of letters with coordinates on the board
	 */
	private Move findVerticalIntersections(Move letters, int letterIndex,
			int tileIndex, ArrayList<Tile> rack) {
		Move move = new Move();
		Coord intersection = lettersInPlay.get(tileIndex).getLoc();
		if ((intersection.getRow() - letterIndex >= 0)
				&& (intersection.getRow() - letterIndex + letters.size() < len)) {
			int r = intersection.getRow() - letterIndex;
			int c = intersection.getCol();
			for (int i = 0; i < letters.size(); i++) {
				if (board[r + i][c].getLetter() == null
						|| (board[r + i][c].getLetter() != null && board[r + i][c]
								.getLetter().getChar() == letters.get(i)
								.getChar())) {
					LetterTile temp = new LetterTile(letters.get(i).getChar(),
							-1, null);
					temp.setLoc(new Coord(r + i, c));
					move.add(temp);
				} else
					return null;
			}
			if (!isLegalMove(move, "vertical", rack))
				return null;
		}
		if (move.size() == 0)
			return null;
		return move;
	}

	/**
	 * find any words in the vertical direction, without any letter intersecting
	 * with an existing letter
	 * 
	 * @param letters
	 *            list of letters to check
	 * @param letterIndex
	 *            index of the starting point in the list of letters
	 * @param coord
	 *            coordinate of the board to start at
	 * @return list of letters with coordinates on the board
	 */
	private Move findVerticalAdj(Move letters, int letterIndex, Coord coord,
			ArrayList<Tile> rack) {
		Move move = new Move();
		if ((coord.getRow() - letterIndex >= 0)
				&& (coord.getRow() - letterIndex + letters.size() < len)) {
			int r = coord.getRow() - letterIndex;
			int c = coord.getCol();
			for (int i = 0; i < letters.size(); i++) {
				if (board[r + i][c].getLetter() == null) {
					LetterTile temp = new LetterTile(letters.get(i).getChar(),
							-1, null);
					temp.setLoc(new Coord(r + i, c));
					move.add(temp);
				} else
					return null;
			}
			if (!isLegalMove(move, "vertical", rack))
				return null;
		}
		if (move.size() == 0)
			return null;
		return move;
	}

	/**
	 * find any words in the horizontal direction, with at least one letter
	 * intersecting with an existing letter
	 * 
	 * @param letters
	 *            list of letters to check
	 * @param letterIndex
	 *            index of the starting point in the list of letters
	 * @param coord
	 *            coordinate of the board to start at
	 * @return list of letters with coordinates on the board
	 */
	private Move findHorizontalIntersections(Move letters, int letterIndex,
			int tileIndex, ArrayList<Tile> rack) {
		Move move = new Move();
		Coord intersection = lettersInPlay.get(tileIndex).getLoc();
		if ((intersection.getCol() - letterIndex >= 0)
				&& (intersection.getCol() - letterIndex + letters.size() < len)) {
			int r = intersection.getRow();
			int c = intersection.getCol() - letterIndex;
			for (int i = 0; i < letters.size(); i++) {
				if (board[r][c + i].getLetter() == null
						|| (board[r][c + i].getLetter() != null && board[r][c
								+ i].getLetter().getChar() == letters.get(i)
								.getChar())) {
					LetterTile temp = new LetterTile(letters.get(i).getChar(),
							-1, null);
					temp.setLoc(new Coord(r, c + i));
					move.add(temp);
				} else
					return null;
			}
			if (!isLegalMove(move, "horizontal", rack))
				return null;
		}
		if (move.size() == 0)
			return null;
		return move;
	}

	/**
	 * find any words in the horizontal direction, without any letter
	 * intersecting with an existing letter
	 * 
	 * @param letters
	 *            list of letters to check
	 * @param letterIndex
	 *            index of the starting point in the list of letters
	 * @param coord
	 *            coordinate of the board to start at
	 * @return list of letters with coordinates on the board
	 */
	private Move findHorizontalAdj(Move letters, int letterIndex, Coord coord,
			ArrayList<Tile> rack) {
		Move move = new Move();
		if ((coord.getCol() - letterIndex >= 0)
				&& (coord.getCol() - letterIndex + letters.size() < len)) {
			int r = coord.getRow();
			int c = coord.getCol() - letterIndex;
			for (int i = 0; i < letters.size(); i++) {
				if (board[r][c + i].getLetter() == null) {
					LetterTile temp = new LetterTile(letters.get(i).getChar(),
							-1, null);
					temp.setLoc(new Coord(r, c + i));
					move.add(temp);
				} else
					return null;
			}
			if (!isLegalMove(move, "horizontal", rack))
				return null;
		}
		if (move.size() == 0)
			return null;
		return move;
	}

	/**
	 * check if a move creates a legal word in every possible direction
	 * 
	 * @param move
	 *            list of letters with coordinates
	 * @param dir
	 *            direction the letters would be added to the board
	 * @return
	 */
	private boolean isLegalMove(Move move, String dir, ArrayList<Tile> rack) {
		// one of the possibly many words created by the move, to be checked
		// with the supplied dictionary
		String word = "";
		int r = 0;
		int c = 0;
		// check the word created in the vertical direction only once, then for
		// every letter in the move, check the word created in the horizontal
		// direction
		if (dir.equals("vertical")) {
			// add the letters in the move to the word created
			for (int i = 0; i < move.size(); i++)
				word += move.get(i).getChar();
			// add any adjacent letters on the board above the first letter in
			// move, adds the letters to the beginning of the word created
			r = move.get(0).getLoc().getRow() - 1;
			c = move.get(0).getLoc().getCol();
			while (r >= 0 && board[r][c].getLetter() != null) {
				word = board[r][c].getLetter().getChar() + word;
				r--;
			}
			// add any adjacent letters on the board below the last letter in
			// move, adds the letters to the end of the word created
			r = move.get(move.size() - 1).getLoc().getRow() + 1;
			while (r < len && board[r][c].getLetter() != null) {
				word += board[r][c].getLetter().getChar();
				r++;
			}
			// check if the created word is in the dictionary provided if
			// the word created is longer than one letter
			if (word.length() > 1 && !isWord(word))
				return false;

			// add any adjacent letters on the board that are left and right of
			// the current letter of move at the given coordinate, adds the
			// letters to the beginning and end of the word created,
			// respectively
			for (int i = 0; i < move.size(); i++) {
				// clear the word to be checked
				word = "";
				// add the current letter to the word to be checked
				word += move.get(i).getChar();
				r = move.get(i).getLoc().getRow();
				c = move.get(i).getLoc().getCol() - 1;
				// add letters left of the current letter
				while (c >= 0 && board[r][c].getLetter() != null) {
					word = board[r][c].getLetter().getChar() + word;
					c--;
				}
				// add letters right of the current letter
				c = move.get(move.size() - 1).getLoc().getCol() + 1;
				while (c < len && board[r][c].getLetter() != null) {
					word += board[r][c].getLetter().getChar();
					c++;
				}
				// check if the created word is in the dictionary provided if
				// the word created is longer than one letter
				if (word.length() > 1 && !isWord(word))
					return false;
			}
		}
		if (dir.equals("horizontal")) {
			// add the letters in the move to the word created
			for (int i = 0; i < move.size(); i++) {
				word += move.get(i).getChar();
			}
			// add any adjacent letters on the board left of the first letter in
			// move, adds the letters to the beginning of the word created
			r = move.get(0).getLoc().getRow();
			c = move.get(0).getLoc().getCol() - 1;
			while (c >= 0 && board[r][c].getLetter() != null) {
				word = board[r][c].getLetter().getChar() + word;
				c--;
			}
			// add any adjacent letters on the board right of the last letter in
			// move, adds the letters to the end of the word created
			c = move.get(move.size() - 1).getLoc().getCol() + 1;
			while (c < len && board[r][c].getLetter() != null) {
				word += board[r][c].getLetter().getChar();
				c++;
			}
			// check if the created word is in the dictionary provided if
			// the word created is longer than one letter
			if (word.length() > 1 && !isWord(word))
				return false;

			// add any adjacent letters on the board that are above and below
			// the current letter of move at the given coordinate, adds the
			// letters to the beginning and end of the word created,
			// respectively
			for (int i = 0; i < move.size(); i++) {
				// clear the word to be checked
				word = "";
				// add the current letter to the word to be checked
				word += move.get(i).getChar();
				r = move.get(i).getLoc().getRow() - 1;
				c = move.get(i).getLoc().getCol();
				// add letters above the current letter
				while (r >= 0 && board[r][c].getLetter() != null) {
					word = board[r][c].getLetter().getChar() + word;
					r--;
				}
				// add letters below the current letter
				r = move.get(move.size() - 1).getLoc().getRow() + 1;
				while (r < len && board[r][c].getLetter() != null) {
					word += board[r][c].getLetter().getChar();
					r++;
				}
				// check if the created word is in the dictionary provided if
				// the word created is longer than one letter
				if (word.length() > 1 && !isWord(word))
					return false;
			}
		}
		if (isReplacing(move) || hasMissingLetters(move, rack))
			return false;
		return true;
	}

	private boolean isReplacing(Move move) {
		for (int i = 0; i < move.size(); i++) {
			int r = move.get(i).getLoc().getRow();
			int c = move.get(i).getLoc().getCol();
			if (board[r][c].getLetter() == null)
				return false;
		}
		return true;
	}

	private boolean hasMissingLetters(Move move, ArrayList<Tile> rack) {
		ArrayList<Character> available = new ArrayList<Character>();
		for (int i = 0; i < rack.size(); i++)
			available.add(rack.get(i).getChar());
		for (int i = 0; i < move.size(); i++) {
			int row = move.get(i).getLoc().getRow();
			int col = move.get(i).getLoc().getCol();
			if (board[row][col].getLetter() == null
					&& !available.contains(move.get(i).getChar())
					&& !available.contains('_'))
				return true;
			else if (board[row][col].getLetter() == null
					&& !available.contains(move.get(i).getChar())
					&& available.contains('_'))
				available.remove(available.indexOf('_'));
			else if (board[row][col].getLetter() == null)
				available.remove(available.indexOf(move.get(i).getChar()));
		}
		return false;
	}

	/**
	 * returns the total score earned for the placed letters, for any word made
	 * in the given direction
	 * 
	 * @param coord
	 *            location on the board to start the score counting
	 * @param dir
	 *            direction to check for the word
	 * @return the total score earned for the word
	 */
	public int getWordScore(Coord coord, String dirPlaced, String dirCheck) {
		if (coord.getRow() < 0 || coord.getCol() < 0 || coord.getRow() >= len
				&& coord.getCol() >= len)
			return 0;

		int score = 0;
		// the factor by which the final score of the word is multipled by
		int wordScoreModifier = 1;
		// variables that change the column or row depending on the orientation
		// of the added word
		int rowChange = 0;
		int colChange = 0;
		int r = coord.getRow();
		int c = coord.getCol();

		// set the direction of the coordinate change variables
		if (dirCheck.equals("vertical"))
			rowChange++;
		else if (dirCheck.equals("horizontal"))
			colChange++;

		// for each letter left of or above (depending on direction)
		// the given coordinate, add the score value and trigger any unused
		// scoreModifiers on the newly placed tiles' squares
		while (r >= 0 && c >= 0 && board[r][c].getLetter() != null) {
			// update the score for any letter-based score modifiers
			score += board[r][c].updateScore(
					board[r][c].getLetter().getValue(), "letter");
			// update the word score factor for any word score modifiers
			wordScoreModifier = board[r][c].updateScore(wordScoreModifier,
					"word");
			r -= rowChange;
			c -= colChange;
		}

		r = coord.getRow() + rowChange;
		c = coord.getCol() + colChange;

		// for each letter right of or below (depending on direction)
		// the given coordinate, add the score value and trigger any unused
		// scoreModifiers on the newly placed tiles' squares
		while (r < len && c < len && board[r][c].getLetter() != null) {
			score += board[r][c].updateScore(
					board[r][c].getLetter().getValue(), "letter");
			wordScoreModifier = board[r][c].updateScore(wordScoreModifier,
					"word");
			r += rowChange;
			c += colChange;
		}
		if (!dirCheck.equals(dirPlaced) && !makesNewWord(coord, dirCheck))
			return 0;
		// return the word score multiplied by any word score modifiers
		return score * wordScoreModifier;
	}

	/**
	 * checks if a new word is made in the given direction
	 * 
	 * @param coord
	 *            coordinate to start from
	 * @param dir
	 *            direction to check
	 * @return true if a new word is made
	 */
	private boolean makesNewWord(Coord coord, String dir) {
		// variables that change the column or row depending on the orientation
		// of the added word
		int rowChange = 0;
		int colChange = 0;
		int r = coord.getRow();
		int c = coord.getCol();
		int turn = board[r][c].getTurnPlaced();

		// set the direction of the coordinate change variables
		if (dir.equals("vertical"))
			rowChange++;
		else if (dir.equals("horizontal"))
			colChange++;

		// check if the letter above or to the left of the current letter was
		// placed before this turn, meaning this turn creates a new word in that
		// direction (depends on given direction)
		if (r - rowChange >= 0 && c - colChange >= 0
				&& board[r - rowChange][c - colChange].getLetter() != null) {
			if (board[r - rowChange][c - colChange].getTurnPlaced() < turn)
				return true;
		}

		// check if the letter below or to the right of the current letter was
		// placed before this turn, meaning this turn creates a new word in that
		// direction (depends on given direction)
		if (r + rowChange >= 0 && c + colChange >= 0
				&& board[r + rowChange][c + colChange].getLetter() != null) {
			if (board[r + rowChange][c + colChange].getTurnPlaced() < turn)
				return true;
		}
		return false;
	}

	/**
	 * returns a boolean if the square coordinate has an adjacent square
	 * containing a letter, in eithe rof the four directions
	 * 
	 * @param coord
	 *            location of the square
	 * @return an array of booleans
	 */
	public boolean[] hasNeighbor(Coord coord) {
		// first index repreests the vertical neighbors and the second
		// index represents the horizontal neighbors
		boolean[] hasAdj = new boolean[2];
		int r = coord.getRow();
		int c = coord.getCol();
		// check if a letter exists above or below the given coordinate
		if ((r - 1 >= 0 && board[r - 1][c].getLetter() != null)
				|| (r + 1 < len && board[r + 1][c].getLetter() != null))
			hasAdj[0] = true;
		// check if a letter exists left or right of the given coordinate
		if ((c - 1 >= 0 && board[r][c - 1].getLetter() != null)
				|| (c + 1 < len && board[r][c + 1].getLetter() != null))
			hasAdj[1] = true;
		return hasAdj;
	}

	/**
	 * checks if the given word is part of the dictionary
	 * 
	 * @param word
	 *            the given word to check
	 * @return true if word is in the dictionary
	 */
	private boolean isWord(String word) {
		return dict.isWord(word);
	}

	/**
	 * initialize the scoremodifiers on the board using the data from the
	 * scoremodifiers.txt file
	 */
	private void initScoreModifiers() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(filePath + "scoremodifiers.txt"));
		while (scanner.hasNext()) {
			// type of score modifier
			String modifier = scanner.next();
			// row location
			int r = Integer.parseInt(scanner.next());
			// column location
			int c = Integer.parseInt(scanner.next());
			// if score modifier is within the board limits
			if (r >= 0 && c >= 0 && r < board.length && c < board.length)
				board[r][c].setScoreModifier(modifier);
		}
		scanner.close();
	}

	@Override
	public String toString() {
		String s = "";
		for (int r = 0; r < len; r++) {
			for (int c = 0; c < len; c++) {
				s += board[r][c];
			}
			s += "\n";
		}
		return s;
	}
}
