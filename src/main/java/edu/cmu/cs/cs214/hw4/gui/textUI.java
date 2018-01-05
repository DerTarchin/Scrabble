package edu.cmu.cs.cs214.hw4.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import edu.cmu.cs.cs214.hw4.core.*;

/**
 * LIMITED TEXT UI, NOT PART OF GUI SUBMISSION
 * 
 * @author Hizal
 *
 */
public class textUI {

	static String resources = "src/main/resources/";
	static int boardLength = 15;
	static int rackSize = 7;
	static Game game;
	static Random rand = new Random();

	/**
	 * main method
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Starting Scrabble with Stuff");

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		new textUI().run(in);

	}

	private void run(BufferedReader in) throws IOException {
		game = new Game(boardLength, rackSize, resources);
		String input = "";

		int players = 0;
		System.out
				.println("Type a name to add a player. Type 'done' to finish");
		while (players < 4) {
			System.out.print("> ");
			input = "" + in.readLine();
			if (input.toLowerCase().equals("done") && players > 0)
				break;
			else if (!input.toLowerCase().equals("done")) {
				game.addPlayer(input);
				System.out.println(input + " added!");
				players++;
			}
		}
		game.setTurn(rand.nextInt(game.getPlayerList().size()));
		System.out
				.println("Starting game!\nType '#end', '#buy', '#swap' or a word:");
		while (true) {
			input = "";
			System.out.println("\nCURRENT TURN: " + game.getTurn());
			System.out.println(game.getTurn().rackToString());

			System.out.println();
			System.out.println(game);

			while (!input.equals("#end")) {
				System.out.print("> ");
				input = in.readLine();
				if (input.equals("#end"))
					end();
				else if (input.equals("#buy"))
					buy(in);
				else if (input.equals("#swap")) {
					swap(in);
					while (!input.equals("#end") && !input.equals("#buy")) {
						System.out.print("> ");
						input = in.readLine();
						if (input.equals("#end"))
							end();
						else if (input.equals("#buy"))
							buy(in);
						else
							System.out.println("Invalid input. Try again:");
					}
				} else if (input.equals("#use")) {
					use(in);
					while (!input.equals("#end") && !input.equals("#buy")) {
						System.out.print("> ");
						input = in.readLine();
						if (input.equals("#end"))
							end();
						else if (input.equals("#buy"))
							buy(in);
						else
							System.out.println("Invalid input. Try again:");
					}
				} else {
					move(input, in);
					input = "";
					while (!input.equals("#end") && !input.equals("#buy")) {
						System.out.print("> ");
						input = in.readLine();
						if (input.equals("#end"))
							end();
						else if (input.equals("#buy"))
							buy(in);
						else
							System.out.println("Invalid input. Try again:");
					}
				}
			}
		}
	}

	private String end() {
		game.nextTurn();
		return "#end";
	}

	private void buy(BufferedReader in) throws IOException {
		System.out.println("Choose a special to buy:");
		for (int i = 0; i < game.getSpecialsList().size(); i++) {
			System.out.println(i + 1 + ": " + game.getSpecialsList().get(i));
		}
		int i = -1;
		while (i < 0 || i >= game.getSpecialsList().size()) {
			System.out.print("> ");
			i = Integer.parseInt(in.readLine()) - 1;
			if (i >= 0 && i < game.getSpecialsList().size()) {
				if (game.getSpecialsList().get(i).getPrice() <= game.getTurn()
						.getScore()) { // player score
					game.buySpecial(game.getSpecialsList().get(i));
					System.out
							.println("Bought a special! Type '#end', '#use' or a word:");
					return;
				} else
					System.out.println("Not enough points. Choose another:");
			} else
				System.out.println("Not an option. Try again:");
		}
		return;
	}

	private void swap(BufferedReader in) throws IOException {
		String input = "";
		System.out.println("Enter the letters you want swapped: ");
		System.out.print("> ");
		input = in.readLine();
		game.shufflePlayerRack(input);
		System.out.println("Swapped your tiles! Type '#end' or '#buy':");
	}

	private void use(BufferedReader in) throws IOException {
		System.out.println("Choose the special you want to use:");
		for (int i = 0; i < game.getTurn().getSpecialsList().size(); i++) {
			System.out.println(i + 1 + ": "
					+ game.getTurn().getSpecialsList().get(i));
		}
		int i = -1;
		while (i < 0 || i >= game.getTurn().getSpecialsList().size()) {
			System.out.print("> ");
			i = Integer.parseInt(in.readLine()) - 1;
			if (i >= 0 && i < game.getTurn().getSpecialsList().size()) {
				int r, c;
				System.out.println("Enter row and col (0-" + (boardLength - 1)
						+ ") in an empty square:");
				System.out.print("> row= ");
				r = Integer.parseInt(in.readLine());
				System.out.print("> col= ");
				c = Integer.parseInt(in.readLine());
				game.addSpecial(game.getTurn().getSpecialsList().get(i), r, c);
				System.out.println("Played special! Type '#end' or '#buy':");
				return;

			} else
				System.out.println("Not an option. Try again:");
		}
	}

	private void move(String input, BufferedReader in) throws IOException {
		while (true) {
			ArrayList<Move> moves = game.getMoves(input);
			if (moves.size() == 0) {
				System.out.println("No possible moves! Try again:");
				System.out.print("> ");
				input = in.readLine();
				moves = game.getMoves(input);
			} else {
				printMoves(moves);
				System.out.println("Enter your choice: ");
				int i = -1;
				while (i < 0 || i >= moves.size()) {
					System.out.print("> ");
					i = Integer.parseInt(in.readLine()) - 1;
					if (i >= 0 && i < moves.size()) {
						game.insertWord(moves.get(i));
						System.out
								.println("Added your move! Your new score is "
										+ game.getTurn().getScore() + "!"
										+ "\nType '#end' or '#buy':");
						return;
					} else
						System.out.println("Not an option. Try again:");
				}
			}
		}
	}

	private void printMoves(ArrayList<Move> list) {
		System.out.println("Choose a match:");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Option " + (i + 1));
			for (int r = 0; r < boardLength; r++) {
				for (int c = 0; c < boardLength; c++) {
					boolean hasChar = false;
					for (int j = 0; j < list.get(i).size(); j++) {
						int row = list.get(i).get(j).getLoc().getRow();
						int col = list.get(i).get(j).getLoc().getCol();
						if (r == row && c == col) {
							System.out
									.print(list.get(i).get(j).getChar() + " ");
							hasChar = true;
						}
					}
					if (!hasChar && game.getSquare(r, c).getLetter() != null)
						System.out.print(game.getSquare(r, c).getLetter()
								.getChar()
								+ " ");
					else if (!hasChar)
						System.out.print("- ");
				}
				System.out.println();
			}
		}
	}

}
