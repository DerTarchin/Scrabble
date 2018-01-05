package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * The heart of the GUI Uses a thread to check all possible positions of the
 * given word on the board and repaints the board with the possible locations,
 * if hints are turned on. Automatically cancels itself if it is an outdated
 * search.
 * 
 * @author Hizal
 *
 */
public class WordChecker extends Thread {
	private GameWindow gameWindow;
	private GameInfo game;

	/**
	 * starts thread
	 * 
	 * @param gameWindow
	 *            gameWindow object with public variables. variables are public
	 *            because secirity is not an issue for the GUI like it is for
	 *            core
	 */
	public WordChecker(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
		game = gameWindow.threadGetGameInfo();
		start();
	}

	/**
	 * runs the main checker. automatically cancels if it is an outdated search
	 * call
	 * 
	 * @param word
	 *            word to search for
	 * @param index
	 *            index in the order of called methods.
	 */
	public void run(String word, int index) {
		
		// check if the game's notification is set to swap, which overrides this
		// call;
		if (!gameWindow.threadGetNotification().getText().toLowerCase().contains("swap")
				&& !game.isTurnOver())
			gameWindow.threadGetNotification().setText("Checking validity...");
		// sleep helps with fast typers, preventing useless search calls
		try {
			sleep(100);
		} catch (InterruptedException e) {
		}

		// check if current thread is not the latest search call
		if (index != gameWindow.threadGetSearchIndex())
			return;

		// clear previous search results
		if (gameWindow.threadGetPositions() != null) {
			gameWindow.threadGetPositions().removeAll();
			gameWindow.threadGetPositions().validate();
			gameWindow.threadGetPositions().repaint();
			gameWindow.threadGetBoard().remove(gameWindow.threadGetPositions());
			gameWindow.threadGetBoard().validate();
			gameWindow.threadGetBoard().repaint();
		}

		// check if the word is not empty
		if (word != null && word.length() > 0) {
			// get the possible moves for the given word
			gameWindow.threadSetMoves(game.getGame().getMoves(word));

			// check again if the search call should be cancelled
			if (index != gameWindow.threadGetSearchIndex())
				return;

			// if no results were found, repaint board and set notification text
			if (gameWindow.threadGetMoves().size() == 0) {
				gameWindow.threadGetNotification().setText("No possible moves!");
				if (gameWindow.threadGetPositions() != null) {
					gameWindow.threadGetPositions().removeAll();
					gameWindow.threadGetPositions().validate();
					gameWindow.threadGetPositions().repaint();
					gameWindow.threadGetBoard().remove(gameWindow.threadGetPositions());
					gameWindow.threadGetBoard().validate();
					gameWindow.threadGetBoard().repaint();
				}
				return;
			}
			// if results were found, repaint board and set notification text
			else {
				gameWindow.threadGetNotification().setText("Select a start position!");
				if (gameWindow.threadGetPositions() == null)
					gameWindow.threadSetPositions(new JPanel());
				gameWindow.threadGetPositions().removeAll();
				gameWindow.threadGetPositions().setLayout(new BorderLayout());
				gameWindow.threadGetPositions().setBounds(gameWindow.threadGetTilesStart(),
						gameWindow.threadGetTilesStart(), gameWindow.threadGetTilesBorder(),
						gameWindow.threadGetTilesBorder());
				gameWindow.threadGetPositions().setOpaque(false);
				gameWindow.threadSetPositionsStart();
				gameWindow.threadGetPositions().add(gameWindow.threadGetPositionsStart(),
						BorderLayout.CENTER);
				gameWindow.threadGetBoard().remove(gameWindow.threadGetPositions());
				gameWindow.threadGetBoard().validate();
				gameWindow.threadGetBoard().repaint();
				gameWindow.threadGetBoard().add(gameWindow.threadGetPositions(), new Integer(5), 0);
				gameWindow.threadGetBoard().validate();
				gameWindow.threadGetBoard().repaint();
				return;
			}
		}
		// if the game is not over, mode is not set to swap and word length is 0
		// or word is null, set notification text to blank
		if (!gameWindow.threadGetNotification().getText().toLowerCase().contains("swap")
				&& !game.isTurnOver())
			gameWindow.threadGetNotification().setText(" ");
	}
}