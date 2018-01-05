package edu.cmu.cs.cs214.hw4.gui;

import javax.swing.JFrame;

import edu.cmu.cs.cs214.hw4.core.Game;

/**
 * stores information for the current game
 * 
 * @author Hizal
 *
 */
public class GameInfo {
	private JFrame parentFrame;
	private boolean turnOver;
	private boolean hints;
	private Game game;
	private String notificationText;

	/**
	 * initializes the new round
	 * 
	 * @param frame
	 *            jframe to show
	 */
	public GameInfo(JFrame frame) {
		this.parentFrame = frame;
		notificationText = null;
		hints = true;
		turnOver = false;
		game = null;
	}

	/**
	 * sets the Game object for the new game
	 * 
	 * @param game
	 *            game
	 */
	public void setGame(final Game game) {
		this.game = game;
	}

	/**
	 * sets if the turn is over or not
	 * 
	 * @param turnOver
	 *            true or false
	 */
	public void setTurnOver(boolean turnOver) {
		this.turnOver = turnOver;
	}

	/**
	 * sets the preferrence for hints (casual mode)
	 * 
	 * @param hints
	 *            boolean for hints to be on or off
	 */
	public void setHints(boolean hints) {
		this.hints = hints;
	}

	/**
	 * sets the notificatoin for the game window screen
	 * 
	 * @param s
	 *            text to set
	 */
	public void setNotificationText(String s) {
		notificationText = s;
	}

	/**
	 * returns if the game allows hints (casual mode)
	 * 
	 * @return true if hints are allowed
	 */
	public boolean allowsHints() {
		return hints;
	}

	/**
	 * returns if the turn is over for the current player
	 * 
	 * @return true if turn is over
	 */
	public boolean isTurnOver() {
		return turnOver;
	}

	/**
	 * gets the game object
	 * 
	 * @return game object
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * gets the frame to display
	 * 
	 * @return jframe
	 */
	public JFrame getFrame() {
		return parentFrame;
	}

	/**
	 * gets the notification text to display on gamewindow
	 * 
	 * @return text
	 */
	public String getNotificationText() {
		return notificationText;
	}
}
