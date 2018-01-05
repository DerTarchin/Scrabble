package edu.cmu.cs.cs214.hw4.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 * This is the class in charge of running and maintinaining the game, its
 * players, board, tiles and conditions
 * 
 * @author Hizal
 *
 */
public class Game {
	private ArrayList<Tile> tileBag = new ArrayList<Tile>();
	private ArrayList<Player> players = new ArrayList<Player>();
	private Board board;
	private String filePath;
	private String tilesFile = "tiles.txt";
	private String iconEnding = ".png";
	private int turn = 0; // default
	private Random rand = new Random();
	private int rackSize;
	private ArrayList<SpecialTile> specials = new ArrayList<SpecialTile>();
	private int turnIndex = 1;
	private boolean gameOver = false; // default

	/**
	 * initializes the game
	 * 
	 * @param boardSize
	 *            length of the board sides
	 * @param rackSize
	 *            amount of tiles each player holds
	 * @param path
	 *            path to resources folder
	 */
	public Game(int boardSize, int rackSize, String path) {
		filePath = path;
		board = new Board(boardSize, filePath);
		try {
			initTileBag();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filePath + tilesFile);
		}
		initSpecialsList();
		this.rackSize = rackSize;
	}

	/**
	 * initializes the bag of tiles, read from a file
	 * 
	 * @throws FileNotFoundException
	 */
	private void initTileBag() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(filePath + tilesFile));
		while (scanner.hasNext()) {
			String name = scanner.next();
			int value = Integer.parseInt(scanner.next());
			// add a blank tile or a letter tile to the tileBag
			if (name.equals("BLANK"))
				tileBag.add(new BlankTile(filePath + "blank" + iconEnding));
			else {
				char letter = name.charAt(0);
				tileBag.add(new LetterTile(letter, value, filePath + letter
						+ iconEnding));
			}
		}
		scanner.close();
	}

	/**
	 * initializes the special tiles
	 */
	private void initSpecialsList() {
		ArrayList<SpecialTile> temp = new ArrayList<SpecialTile>();
		temp.add(new BoomTile(filePath + "boom" + iconEnding));
		temp.add(new ReverseTile(filePath + "reverse" + iconEnding));
		temp.add(new ShuffleTile(filePath + "shuffle" + iconEnding));
		temp.add(new NegativeTile(filePath + "negative" + iconEnding));
		temp.add(new FlipTile(filePath + "flip" + iconEnding));
		for (int i = 0; i < temp.size(); i++) {
			if (!specials.contains(temp.get(i)))
				specials.add(i, temp.get(i));
		}
	}

	/**
	 * adds a player to the list of players, and initializes their rack
	 * 
	 * @param name
	 *            Player's name
	 */
	public void addPlayer(String name) {
		Player p = new Player(name);
		players.add(p);
		refillPlayerRack(p);
		turn = rand.nextInt(players.size());
	}

	/**
	 * returns the list of active players in order
	 * 
	 * @return player list
	 */
	public ArrayList<Player> getPlayerList() {
		return players;
	}

	/**
	 * adds tiles to the player rack until it hits the size limit
	 * 
	 * @param p
	 *            player to add tiles to
	 */
	private void refillPlayerRack(Player p) {
		while (p.getRack().size() < rackSize)
			if (tileBag.size() > 0)
				p.addToRack(getTile());
			else
				return;
	}

	/**
	 * retrieves a random tile from the tile bag
	 * 
	 * @return tile
	 */
	private Tile getTile() {
		int r = rand.nextInt(tileBag.size());
		Tile tile = tileBag.get(r);
		tileBag.remove(r);
		return tile;
	}

	/**
	 * adds to player's rack new tiles to replace the selected existing tiles
	 * doesn't do anything if given letters dont exist in player's rack
	 * 
	 * @param letters
	 *            string of letters to shuffle
	 */
	public void shufflePlayerRack(String letters) {
		letters = letters.toUpperCase();
		ArrayList<Tile> rack = getTurn().getRack();
		for (int j = 0; j < letters.length(); j++) {
			for (int i = rack.size() - 1; i >= 0; i--) {
				if (letters.charAt(j) == rack.get(i).getChar()) {
					// return to tile bag
					tileBag.add(rack.get(i));
					// remove from tile bag
					getTurn().removeFromRack(rack.get(i));
					break;
				}
			}
		}
		refillPlayerRack(getTurn());
	}

	/**
	 * returns the current player during a turn
	 * 
	 * @return player
	 */
	public Player getTurn() {
		if (players.size() == 0)
			return null;
		return players.get(turn);
	}

	/**
	 * sets the current turn to a specific player
	 * 
	 * @param i
	 *            index of player in the player list
	 */
	public void setTurn(int i) {
		turn = i;
	}

	/**
	 * advances the turn to the next player in the list
	 */
	public void nextTurn() {
		turn = (turn + 1) % players.size();
		turnIndex++;
	}

	/**
	 * returns the square on the board at a givel coordinate
	 * 
	 * @param r
	 *            row
	 * @param c
	 *            column
	 * @return square
	 */
	public Square getSquare(int r, int c) {
		return board.getSquare(r, c);
	}

	/**
	 * returns a list of possible moves for the given word
	 * 
	 * @param word
	 *            string containing the letters to check for moves
	 * @return list of moves, or null if there are none
	 */
	public ArrayList<Move> getMoves(String word) {
		// no moves if there is no input
		if (word == null)
			return new ArrayList<Move>();

		// create a list of letter tiles from the givel string
		Move letters = new Move();
		for (int i = 0; i < word.length(); i++)
			letters.add(new LetterTile(word.toUpperCase().charAt(i), -1,
					filePath));

		// check if there are any letters that can be added with a blank tile
		//
		// int blanks keeps track of number of letters in the word that are not
		// on the board or in the player's rack
		//
		// int availableBlanks is the number of blank tiles in the player rack
		//
		// nonRackLetters are the number of letters that are not in the player's
		// rack but are actually on the board already
		int nonRackLetters = 0;
		ArrayList<Character> blanks = new ArrayList<Character>();
		ArrayList<Tile> rack = new ArrayList<Tile>();
		for (int i = 0; i < getTurn().getRack().size(); i++)
			rack.add(getTurn().getRack().get(i));
		int availableBlanks = 0;
		for (int i = 0; i < letters.size(); i++) {
			if (!rack.contains(letters.get(i))
					&& !board.contains(letters.get(i)))
				nonRackLetters++;
			else if (rack.contains(letters.get(i))
					&& !board.contains(letters.get(i)))
				rack.remove(rack.indexOf(letters.get(i)));
		}
		rack.clear();
		for (int i = 0; i < getTurn().getRack().size(); i++)
			rack.add(getTurn().getRack().get(i));
		for (int i = 0; i < letters.size(); i++) {
			if (!rack.contains(letters.get(i))) {
				blanks.add(letters.get(i).getChar());
			} else
				rack.remove(rack.indexOf(letters.get(i)));
		}

		for (int i = 0; i < getTurn().getRack().size(); i++)
			if (getTurn().getRack().get(i).getChar() == '_')
				availableBlanks++;
		if (nonRackLetters > availableBlanks) {
			for (int i = 0; i < blanks.size(); i++)
				if (board.contains(new LetterTile(blanks.get(i), -1, null)))
					return board.getPossibilities(letters, getTurn().getRack());
			return new ArrayList<Move>();
		}
		return board.getPossibilities(letters, getTurn().getRack());
	}

	/**
	 * inserts the given word at the given letter coordinates also activates any
	 * score modifiers or special tiles
	 * 
	 * @param locs
	 *            list of letters
	 * 
	 * @return string list containing alert messages from special tiles, or null
	 */
	public ArrayList<String> insertWord(Move locs) {
		// direction to be added onto the board, by default = "vertical"
		String dir = "single";

		// pre-move rack size
		int rackSize = getTurn().getRack().size();

		// arraylist of letters:
		// list of letters to add to board, excluding ones that are already on
		// the board in the given locations, and changing any blanks into their
		// respective letter tiles
		Move move = new Move();
		ArrayList<Tile> rack = getTurn().getRack();

		for (int i = 0; i < locs.size(); i++) {
			int row = locs.get(i).getLoc().getRow();
			int col = locs.get(i).getLoc().getCol();
			// checks consecutive letters to see if word is horizontal
			if (i + 1 < locs.size())
				if (row == locs.get(i + 1).getLoc().getRow())
					dir = "horizontal";
				else if (col == locs.get(i + 1).getLoc().getCol())
					dir = "vertical";

			// check if letter should take place of a blank tile
			if (!getTurn().hasLetter(locs.get(i).getChar())
					&& board.getSquare(row, col).getLetter() == null) {
				for (int j = 0; j < rack.size(); j++)
					if (rack.get(j).getChar() == '_') {
						// creates a new LetterTile based on the letter the
						// blank tile will be used for
						LetterTile letter = new LetterTile(locs.get(i)
								.getChar(), 0, rack.get(j).getIcon());
						letter.setLoc(locs.get(i).getLoc());
						move.add(letter);
						getTurn().removeFromRack(rack.get(j));
						break;
					}
			}
			// add any letter that isn't already on the board
			else if (board.getSquare(row, col).getLetter() == null) {
				for (int j = 0; j < rack.size(); j++)
					if (rack.get(j).getChar() == locs.get(i).getChar()) {
						LetterTile letter = new LetterTile(locs.get(i)
								.getChar(), rack.get(j).getValue(), rack.get(j)
								.getIcon());
						letter.setLoc(locs.get(i).getLoc());
						move.add(letter);
						getTurn().removeFromRack(rack.get(j));
						break;
					}
			}
		}

		// 50 bonus points if every tile in rack was used
		if (getTurn().getRack().size() == 0 && rackSize == this.rackSize)
			move.setScore(move.getScore() + 50);

		// activate any special tiles that may have existed on the squares in
		// the play, collecting any alerts in the form of a string (to notify
		// the players that they have triggered special tile(s)
		ArrayList<String> alerts = new ArrayList<String>();
		for (int i = 0; i < move.size(); i++) {
			int row = move.get(i).getLoc().getRow();
			int col = move.get(i).getLoc().getCol();
			ArrayList<String> alert = null;
			alert = board.getSquare(row, col).activateSpecials(this, move);
			for (int j = 0; j < alert.size(); j++)
				if (alert.get(j) != null)
					alerts.add(alert.get(j));
		}

		// insert letters in move
		board.insertWord(move, turnIndex);

		ArrayList<Integer> gapIndex = new ArrayList<Integer>();
		gapIndex.add(0);
		if (alerts.size() > 0 && move.size() > 1) {
			int prevR = move.get(0).getLoc().getRow();
			int prevC = move.get(0).getLoc().getCol();
			for (int i = 1; i < move.size(); i++)
				if (move.get(i).getLoc().getRow() - prevR > 1
						|| move.get(i).getLoc().getCol() - prevC > 1)
					gapIndex.add(i);
		}

		// tally the score for each individual word created by the move
		// also triggers any score modifiers (in square.java)
		for (int i = 0; i < move.size(); i++) {
			int row = move.get(i).getLoc().getRow();
			int col = move.get(i).getLoc().getCol();
			// if the move was placed in the vertical direction...
			if (dir.equals("vertical")) {
				// to avoid repeated scoring, only check the score of the
				// created word in the vertical direction once, or at the start
				// of every gap
				for (int g = 0; g < gapIndex.size(); g++)
					if (i == gapIndex.get(g))
						move.setScore(move.getScore()
								+ board.getWordScore(move.get(i).getLoc(), dir,
										"vertical"));
				// check the score of the word created in the horizontal
				// direction, for the current coordinate
				if (board.hasNeighbor(move.get(i).getLoc())[1])
					move.setScore(move.getScore()
							+ board.getWordScore(move.get(i).getLoc(), dir,
									"horizontal"));
			}
			// if the move was placed in the horizontal direction...
			if (dir.equals("horizontal")) {
				// to avoid repeated scoring, only check the score of the
				// created word in the horizontal direction once
				if (i == 0)
					move.setScore(move.getScore()
							+ board.getWordScore(move.get(i).getLoc(), dir,
									"horizontal"));
				// check the score of the word created in the horizontal
				// direction, for the current coordinate
				if (board.hasNeighbor(move.get(i).getLoc())[0])
					move.setScore(move.getScore()
							+ board.getWordScore(move.get(i).getLoc(), dir,
									"vertical"));
			}
			// if the move contains a single direction, add the score only for
			// the directions that have adjacent letters on the board
			if (dir.equals("single")) {
				int toAdd;
				toAdd = board.getWordScore(move.get(i).getLoc(), dir,
						"horizontal");
				if (toAdd > move.get(i).getValue())
					move.setScore(move.getScore() + toAdd);
				toAdd = board.getWordScore(move.get(i).getLoc(), dir,
						"vertical");
				if (toAdd > move.get(i).getValue())
					move.setScore(move.getScore() + toAdd);
			}
			// removes any score modifier used during the move
			board.getSquare(row, col).setScoreModifier(null);
		}
		getTurn().setScore(getTurn().getScore() + move.getFactoredScore());

		refillPlayerRack(getTurn());

		checkGameOver();

		return alerts;
	}

	/**
	 * returns the lsit of special tiles available in the game
	 * 
	 * @return list of special tiles
	 */
	public ArrayList<SpecialTile> getSpecialsList() {
		return specials;
	}

	/**
	 * gives a selected special tile to the current player, replenishes the
	 * selected tile in the special tile list
	 * 
	 * @param s
	 *            special tile to get
	 */
	public void buySpecial(SpecialTile s) {
		if (getTurn().getScore() >= s.getPrice()) {
			// if(50 >= s.getPrice()) { // debug mode, free special tiles for
			// everyone yay!
			specials.remove(s);
			s.setOwner(getTurn());
			getTurn().addSpecial(s);
			getTurn().setScore(getTurn().getScore() - s.getPrice());
			// replenish the special tile type that was bought
			initSpecialsList();
		}
	}

	/**
	 * adds a special tile to the given square on the board, only if it contains
	 * no letters
	 * 
	 * @param s
	 *            special tile to add
	 * @param row
	 *            row to add to
	 * @param col
	 *            column to add to
	 */
	public void addSpecial(SpecialTile s, int row, int col) {
		if (board.getSquare(row, col).getLetter() == null) {
			getTurn().removeSpecial(s);
			s.setLoc(new Coord(row, col));
			board.getSquare(row, col).addSpecial(s);
		}
	}

	/**
	 * checks if the tile bag is empty
	 * 
	 * @return true if empty
	 */
	public boolean isTileBagEmpty() {
		if (tileBag.size() > 0)
			return false;
		return true;
	}

	/**
	 * checks if the game is over, if it is, applies final scoring
	 * 
	 * @return true if over
	 */
	private void checkGameOver() {
		if (getTurn().getRack().size() == 0)
			refillPlayerRack(getTurn());
		if (isTileBagEmpty() && getTurn().getRack().size() == 0) {
			for (int i = 0; i < players.size(); i++)
				if (players.get(i) != getTurn()) {
					int total = 0;
					for (int j = 0; j < players.get(i).getRack().size(); j++)
						total += players.get(i).getRack().get(j).getValue();
					players.get(i).setScore(players.get(i).getScore() - total);
					getTurn().setScore(getTurn().getScore() + total);
				}
			gameOver = true;
		}
	}

	/**
	 * returns true if the game is over Game over man, game over!
	 * 
	 * @return true if game over
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	@Override
	public String toString() {
		return board.toString();
	}
}
