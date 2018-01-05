package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.xswingx.PromptSupport;

import edu.cmu.cs.cs214.hw4.core.Game;
import edu.cmu.cs.cs214.hw4.core.Move;

/**
 * window to display the main game screen, including board, options and player
 * info
 * 
 * @author Hizal
 *
 */
public class GameWindow extends JPanel implements Window, MouseListener {
	private GameInfo gameInfo;
	private Game game;
	private Font steelfish;
	private Font steelfishIt;
	private JLayeredPane board;
	private JLayeredPane positionsStart = null;
	private JPanel spots = null;
	private JPanel tiles;
	private JPanel specials;
	private JPanel rackTiles;
	private JPanel rackSpecials;
	private JPanel positions = null;
	private JPanel options;
	private JLabel endTurn;
	private JLabel swapTiles;
	private JLabel buySpecials;
	private JLabel endGame;
	private JLabel notification;
	private JLabel currentScore;
	private JLabel[] specialIcons;
	private JLabel[][] startPos;
	private JLabel[][] specialSpots = null;
	private JLabel[][] specialSpotsShow;
	private JTextField input;
	private int boardStart;
	private int boardBorder;
	private int tilesStart;
	private int tilesBorder;
	private int squareSize;
	private int squareGap;
	private int specialToAdd = -1;
	private int searchIndex = 0;
	private ArrayList<JLabel> rackFull = new ArrayList<JLabel>();
	private ArrayList<Character> rackFullChars = new ArrayList<Character>();
	private ArrayList<JLabel> rackHalf = new ArrayList<JLabel>();
	private ArrayList<Character> rackHalfChars = new ArrayList<Character>();
	private ArrayList<JLabel> direction = new ArrayList<JLabel>();
	private ArrayList<Move> moves = null;
	private ArrayList<Move> movesToShow = null;
	private String word;
	private boolean shuffle = false;
	private WordChecker getMoves;

	/**
	 * initializes the window and sets up each section of the screen, updates
	 * the notification label with the needed text or blank if it is a new round
	 * Also starts the wordChecker thread for future use.
	 * 
	 * @param game
	 *            gameinfo object
	 */
	public GameWindow(final GameInfo game) {
		this.gameInfo = game;
		this.game = game.getGame();
		steelfish = Util.getFont("steelfish");
		steelfishIt = steelfish.deriveFont(Font.ITALIC);
		getMoves = new WordChecker(this);

		drawWindow(setupComponents());
	}

	@Override
	public JPanel setupComponents() {
		// side panel
		JPanel side = addSidePanel();

		// center
		JPanel center = addGamePanel();

		// player panel
		JPanel players = addPlayerPanel();

		// in case turnOver == true on return from buySpecials
		updateScreen();

		JPanel window = new JPanel();
		window.setBackground(Color.WHITE);
		window.setLayout(new BorderLayout());
		window.add(side, BorderLayout.WEST);
		window.add(center, BorderLayout.CENTER);
		window.add(players, BorderLayout.EAST);

		return window;
	}

	@Override
	public void drawWindow(JPanel panel) {
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		setVisible(true);
	}

	/**
	 * creates the side panel
	 * 
	 * @return panel
	 */
	private JPanel addSidePanel() {
		// key
		JPanel key = new JPanel();
		key.setOpaque(false);
		JLabel keyIcon = createJLabelImg("key", Toolkit.getDefaultToolkit()
				.getScreenSize().width / 6, Toolkit.getDefaultToolkit()
				.getScreenSize().height / 3, 1f);
		keyIcon.setHorizontalAlignment(JLabel.LEFT);
		key.add(keyIcon);

		// options
		options = new JPanel();
		options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));

		// end turn option
		endTurn = createJLabelImg("endturn", Toolkit.getDefaultToolkit()
				.getScreenSize().width / 15, Toolkit.getDefaultToolkit()
				.getScreenSize().height, 1f);
		endTurn.setHorizontalAlignment(JLabel.RIGHT);
		endTurn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		endTurn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				removeAll();
				revalidate();
				repaint();
				if (game.isGameOver()) {
					int score = 0;
					ArrayList<String> winners = new ArrayList<String>();
					for (int i = 0; i < game.getPlayerList().size(); i++)
						if (game.getPlayerList().get(i).getScore() == score)
							winners.add(game.getPlayerList().get(i).getName());
						else if (game.getPlayerList().get(i).getScore() > score) {
							score = game.getPlayerList().get(i).getScore();
							winners.clear();
							winners.add(game.getPlayerList().get(i).getName());
						}
					String alert = "";
					String message = "";
					if (winners.size() == 1) {
						alert = winners.get(0) + " Wins!";
						message = winners.get(0) + " wins with " + score
								+ " points! Hit continue to end game.";
					} else {
						alert = "Tie Game!";
						for (int i = 0; i < winners.size(); i++) {
							if (i == 0)
								message += winners.get(i);
							else if (i == winners.size() - 1)
								message += " and " + winners.get(i);
							else
								message += ", " + winners.get(i);
						}
						message += " tie with " + score
								+ " points! Hit continue to end game.";
					}
					add(new NotificationScreen(gameInfo, alert, message, "menu"));
				} else {
					game.nextTurn();
					gameInfo.setTurnOver(false);
					String name = game.getTurn().getName();
					String alert = name + "'s turn!";
					String message = "Pass the device over to " + name
							+ " and hit continue.";
					add(new NotificationScreen(gameInfo, alert, message, "game"));
				}
			}
		});

		// swap tiles option
		swapTiles = createJLabelImg("swaptiles", Toolkit.getDefaultToolkit()
				.getScreenSize().width / 15, Toolkit.getDefaultToolkit()
				.getScreenSize().height, 1f);
		swapTiles.setHorizontalAlignment(JLabel.RIGHT);
		swapTiles.setCursor(new Cursor(Cursor.HAND_CURSOR));
		swapTiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!shuffle && !gameInfo.isTurnOver()) {
					if (positions != null) {
						positions.removeAll();
						positions.validate();
						positions.repaint();
						board.remove(positions);
						board.validate();
						board.repaint();
					}
					swapTiles.setIcon(changeImgOpacity("swaptiles", Toolkit
							.getDefaultToolkit().getScreenSize().width / 15,
							Toolkit.getDefaultToolkit().getScreenSize().height,
							.5f));
					swapTiles.validate();
					swapTiles.repaint();
					options.validate();
					options.repaint();
					shuffle = true;
					startCheck();
					input.setEditable(true);
					input.requestFocus();
					notification
							.setText("Type letters to swap. Use '_' for blanks.");
					input.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							game.shufflePlayerRack(input.getText());
							shuffle = false;
							gameInfo.setTurnOver(true);
							updateScreen();
							notification.setText("Swapped tiles!");
							swapTiles.setCursor(Cursor.getDefaultCursor());
						}
					});
				} else if (shuffle) {
					notification.setText("Type letters to input.");
					shuffle = false;
					swapTiles.setIcon(changeImgOpacity("swaptiles", Toolkit
							.getDefaultToolkit().getScreenSize().width / 15,
							Toolkit.getDefaultToolkit().getScreenSize().height,
							1f));
					swapTiles.validate();
					swapTiles.repaint();
					options.validate();
					options.repaint();
				}
			}
		});

		// buy specials option
		buySpecials = createJLabelImg("buyspecial", Toolkit.getDefaultToolkit()
				.getScreenSize().width / 15, Toolkit.getDefaultToolkit()
				.getScreenSize().height, 1f);
		buySpecials.setHorizontalAlignment(JLabel.RIGHT);
		buySpecials.setCursor(new Cursor(Cursor.HAND_CURSOR));
		buySpecials.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				removeAll();
				revalidate();
				repaint();
				add(new BuySpecials(gameInfo));
			}
		});

		// end game option
		endGame = createJLabelImg("endgame", Toolkit.getDefaultToolkit()
				.getScreenSize().width / 15, Toolkit.getDefaultToolkit()
				.getScreenSize().height, 1f);
		endGame.setHorizontalAlignment(JLabel.RIGHT);
		endGame.setCursor(new Cursor(Cursor.HAND_CURSOR));
		endGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				removeAll();
				revalidate();
				repaint();
				int score = 0;
				ArrayList<String> winners = new ArrayList<String>();
				for (int i = 0; i < game.getPlayerList().size(); i++)
					if (game.getPlayerList().get(i).getScore() == score)
						winners.add(game.getPlayerList().get(i).getName());
					else if (game.getPlayerList().get(i).getScore() > score) {
						score = game.getPlayerList().get(i).getScore();
						winners.clear();
						winners.add(game.getPlayerList().get(i).getName());
					}
				String alert = "";
				String message = "";
				if (winners.size() == 1) {
					alert = winners.get(0) + " Wins!";
					message = winners.get(0) + " wins with " + score
							+ " points! Hit continue to end game.";
				} else {
					alert = "Tie Game!";
					for (int i = 0; i < winners.size(); i++) {
						if (i == 0)
							message += winners.get(i);
						else if (i == winners.size() - 1)
							message += " and " + winners.get(i);
						else
							message += ", " + winners.get(i);
					}
					message += " tie with " + score
							+ " points! Hit continue to end game.";
				}
				add(new NotificationScreen(gameInfo, alert, message, "menu"));
			}
		});

		int dividerSize = Toolkit.getDefaultToolkit().getScreenSize().width / 136;

		JPanel divider = new JPanel();
		divider.setOpaque(false);
		divider.setPreferredSize(new Dimension(dividerSize, dividerSize));
		JPanel divider2 = new JPanel();
		divider2.setOpaque(false);
		divider2.setPreferredSize(new Dimension(dividerSize, dividerSize));
		JPanel divider3 = new JPanel();
		divider3.setOpaque(false);
		divider3.setPreferredSize(new Dimension(dividerSize, dividerSize));

		options.add(endTurn);
		options.add(divider);
		options.add(swapTiles);
		options.add(divider2);
		options.add(buySpecials);
		options.add(divider3);
		options.add(endGame);

		options.setOpaque(false);
		JPanel optionsGroup = new JPanel();
		optionsGroup.setOpaque(false);
		optionsGroup.setLayout(new BorderLayout());
		optionsGroup.add(options, BorderLayout.SOUTH);

		JPanel optionsWrapper = new JPanel();
		optionsWrapper.setLayout(new BorderLayout());
		optionsWrapper.setOpaque(false);

		JPanel optionsWrapperLeft = new JPanel();
		optionsWrapperLeft.setOpaque(false);
		optionsWrapperLeft.setPreferredSize(new Dimension(Toolkit
				.getDefaultToolkit().getScreenSize().width / 16, Toolkit
				.getDefaultToolkit().getScreenSize().height / 8));

		JPanel optionsWrapperRight = new JPanel();
		optionsWrapperRight.setOpaque(false);

		optionsWrapper.add(optionsWrapperLeft, BorderLayout.WEST);
		optionsWrapper.add(optionsGroup, BorderLayout.CENTER);
		optionsWrapper.add(optionsWrapperRight, BorderLayout.EAST);

		// empty footer
		JPanel footer = new JPanel();
		footer.setOpaque(false);
		footer.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width / 6, (Toolkit.getDefaultToolkit()
				.getScreenSize().height / 4)
				+ Toolkit.getDefaultToolkit().getScreenSize().height / 60));

		JPanel side = new JPanel();
		side.setOpaque(false);
		side.setLayout(new BorderLayout());
		side.add(key, BorderLayout.NORTH);
		side.add(optionsWrapper, BorderLayout.CENTER);
		side.add(footer, BorderLayout.SOUTH);
		return side;
	}

	/**
	 * draws the board and cretes the panel for text input and notifications
	 * 
	 * @return panel
	 */
	private JPanel addGamePanel() {
		boardStart = (Toolkit.getDefaultToolkit().getScreenSize().height / 40) / 2;
		boardBorder = Toolkit.getDefaultToolkit().getScreenSize().width / 3
				+ Toolkit.getDefaultToolkit().getScreenSize().height / 4
				- (Toolkit.getDefaultToolkit().getScreenSize().height / 30) / 2;
		JLabel sqSpace = createJLabelImg("line_h", boardBorder, boardBorder, 1f);
		squareGap = sqSpace.getIcon().getIconHeight();
		squareSize = (boardBorder - squareGap * 16) / 17;
		tilesStart = boardStart + squareSize + squareGap;
		tilesBorder = boardBorder - (squareSize - squareGap) * 2;

		board = new JLayeredPane();
		board.setBounds(0, 0, this.getPreferredSize().width,
				this.getPreferredSize().height);

		// score modifiers
		JPanel scoreModifiers = new JPanel();
		scoreModifiers.setLayout(new BorderLayout());
		// scoreModifiers.setBounds(start, start, boardBorder, boardBorder);
		scoreModifiers.setBounds(tilesStart, tilesStart, tilesBorder,
				tilesBorder);
		scoreModifiers.setOpaque(false);
		scoreModifiers.add(scoreModifiers(), BorderLayout.CENTER);
		// horizontal lines
		JPanel hLines = new JPanel();
		hLines.setLayout(new BorderLayout());
		hLines.setBounds(boardStart, boardStart, boardBorder, boardBorder);
		hLines.setOpaque(false);
		hLines.add(hLines(), BorderLayout.CENTER);
		// vertical lines
		JPanel vLines = new JPanel();
		vLines.setLayout(new BorderLayout());
		vLines.setBounds(boardStart, boardStart, boardBorder, boardBorder);
		vLines.setOpaque(false);
		vLines.add(vLines(), BorderLayout.CENTER);
		// specials
		specials = new JPanel();
		specials.setLayout(new BorderLayout());
		specials.setBounds(tilesStart, tilesStart, tilesBorder, tilesBorder);
		specials.setOpaque(false);
		specials.add(specials(), BorderLayout.CENTER);
		// tiles
		tiles = new JPanel();
		tiles.setLayout(new BorderLayout());
		tiles.setBounds(tilesStart, tilesStart, tilesBorder, tilesBorder);
		tiles.setOpaque(false);
		tiles.add(tiles(), BorderLayout.CENTER);

		board.add(scoreModifiers, new Integer(0), 0);
		board.add(hLines, new Integer(1), 0);
		board.add(vLines, new Integer(2), 0);
		board.add(specials, new Integer(3), 0);
		board.add(tiles, new Integer(4), 0);

		// input field
		input = new JTextField(15);
		Thread requestFocus = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				input.requestFocusInWindow();
			}
		});
		requestFocus.start();
		input.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width, Toolkit.getDefaultToolkit()
				.getScreenSize().height / 25));
		PromptSupport.setPrompt(" ", input);
		input.setHorizontalAlignment(JTextField.CENTER);
		input.setFont(steelfishIt.deriveFont(40f));
		input.setBorder(null);
		input.setForeground(new Color(127, 127, 127));
		input.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		input.setEditable(true);
		input.setFocusable(true);
		input.requestFocusInWindow();
		input.requestFocus();
		input.getDocument().addDocumentListener(new DocumentListener() {

			/**
			 * when the user types a new letter into input, automatically
			 * changes the letter image in the player rack to 50% opacity if it
			 * exists in the rack
			 * 
			 * @param de
			 *            document event
			 */
			@Override
			public void insertUpdate(DocumentEvent de) {
				// word in the textfield to be checked
				word = input.getText().toUpperCase();
				// start a thread to check the word thats created
				startCheck();
				// letter inserted
				char l = word.charAt(word.length() - 1);
				// if inserted letter is full apacity, change to 50%, only if it
				// exists in the player rack, which is checked in this loop
				if (rackFullChars.contains(l)) {
					int index = rackFullChars.indexOf(l);
					rackFull.get(index).setIcon(
							changeImgOpacity(l + "", rackFull.get(index)
									.getIcon().getIconWidth(),
									rackFull.get(index).getIcon()
											.getIconHeight(), .5f));
					rackHalf.add(rackFull.get(index));
					rackHalfChars.add(rackFullChars.get(index));
					rackFull.remove(index);
					rackFullChars.remove(index);
				} else {
					for (int r = 0; r < 15; r++)
						for (int c = 0; c < 15; c++)
							if (game.getSquare(r, c).getLetter() != null
									&& game.getSquare(r, c).getLetter()
											.getChar() == l)
								return;
					if (rackFullChars.contains('_')) {
						int index = rackFullChars.indexOf('_');
						rackFull.get(index).setIcon(
								changeImgOpacity("blank", rackFull.get(index)
										.getIcon().getIconWidth(), rackFull
										.get(index).getIcon().getIconHeight(),
										.5f));
						rackHalf.add(rackFull.get(index));
						rackHalfChars.add(rackFullChars.get(index));
						rackFull.remove(index);
						rackFullChars.remove(index);
					}
				}
			}

			/**
			 * when the user types a removes letter from input, automatically
			 * changes the letter image in the player rack to full opacity if it
			 * exists in the rack
			 * 
			 * @param de
			 *            document event
			 */
			@Override
			public void removeUpdate(DocumentEvent de) {
				// letter deleted
				char l = word.charAt(word.length() - 1);
				// word in the textfield to be checked
				word = input.getText().toUpperCase();
				// start a thread to check the word thats left
				startCheck();
				// if letter is already half transparent, change back, only if
				// it exists in the player rack, which is checked in this loop
				if (rackHalfChars.contains(l)) {
					int index = rackHalfChars.indexOf(l);
					rackHalf.get(index).setIcon(
							changeImgOpacity(l + "", rackHalf.get(index)
									.getIcon().getIconWidth(),
									rackHalf.get(index).getIcon()
											.getIconHeight(), 1f));
					rackFull.add(rackHalf.get(index));
					rackFullChars.add(rackHalfChars.get(index));
					rackHalf.remove(index);
					rackHalfChars.remove(index);
				} else {
					for (int r = 0; r < 15; r++)
						for (int c = 0; c < 15; c++)
							if (game.getSquare(r, c).getLetter() != null
									&& game.getSquare(r, c).getLetter()
											.getChar() == l)
								return;
					if (rackHalfChars.contains('_')) {
						int index = rackHalfChars.indexOf('_');
						rackHalf.get(index).setIcon(
								changeImgOpacity("blank", rackHalf.get(index)
										.getIcon().getIconWidth(), rackHalf
										.get(index).getIcon().getIconHeight(),
										1f));
						rackFull.add(rackHalf.get(index));
						rackFullChars.add(rackHalfChars.get(index));
						rackHalf.remove(index);
						rackHalfChars.remove(index);
					}
				}
			}

			@Override
			public void changedUpdate(DocumentEvent de) {
			}
		});

		notification = new JLabel("Type letters to input.");
		if (gameInfo.getNotificationText() != null)
			notification.setText(gameInfo.getNotificationText());
		gameInfo.setNotificationText(null);
		notification.setFont(steelfish.deriveFont(27f));
		notification.setForeground(new Color(191, 191, 191));
		notification.setHorizontalAlignment(JLabel.CENTER);
		notification.setVerticalAlignment(JLabel.TOP);

		JLabel divider = createJLabelImg("typeline", Toolkit
				.getDefaultToolkit().getScreenSize().width
				/ 2
				- (Toolkit.getDefaultToolkit().getScreenSize().width / 15),
				Toolkit.getDefaultToolkit().getScreenSize().height, 1f);
		divider.setHorizontalAlignment(JLabel.CENTER);

		JLabel space = new JLabel();
		space.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width / 210, Toolkit.getDefaultToolkit()
				.getScreenSize().width / 190));

		JLabel footer = new JLabel();
		footer.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().height / 10, Toolkit.getDefaultToolkit()
				.getScreenSize().height / 10));

		JPanel notificationWrapper = new JPanel();
		notificationWrapper.setOpaque(false);
		notificationWrapper.setLayout(new BorderLayout());
		notificationWrapper.add(notification, BorderLayout.CENTER);

		JPanel dividerWrapper = new JPanel();
		dividerWrapper.setOpaque(false);
		dividerWrapper.setLayout(new BorderLayout());
		dividerWrapper.add(divider, BorderLayout.CENTER);

		JPanel inputArea = new JPanel();
		inputArea.setOpaque(false);
		inputArea.setLayout(new BoxLayout(inputArea, BoxLayout.Y_AXIS));
		inputArea.add(dividerWrapper);
		inputArea.add(space);
		inputArea.add(notificationWrapper);
		inputArea.add(footer);

		JPanel inputWrapper = new JPanel();
		inputWrapper.setOpaque(false);
		inputWrapper.setLayout(new BorderLayout());
		inputWrapper.add(input, BorderLayout.NORTH);
		inputWrapper.add(inputArea, BorderLayout.CENTER);

		JPanel center = new JPanel();
		center.setOpaque(false);
		center.setLayout(new BorderLayout());

		center.add(board, BorderLayout.CENTER);
		center.add(inputWrapper, BorderLayout.SOUTH);
		return center;
	}

	/**
	 * cretes the player panel, showing info for the current and inactive
	 * players
	 * 
	 * @return panel
	 */
	private JPanel addPlayerPanel() {
		// current player

		// score
		currentScore = new JLabel("  " + game.getTurn().getScore() + "   ");
		currentScore.setFont(steelfishIt.deriveFont(80f));
		currentScore.setForeground(new Color(64, 64, 64));
		currentScore.setVerticalAlignment(JLabel.BOTTOM);
		currentScore.setVerticalTextPosition(JLabel.BOTTOM);
		currentScore.setAlignmentX(JLabel.BOTTOM_ALIGNMENT);

		// specials tiles
		rackSpecials = new JPanel();
		rackSpecials.setOpaque(false);
		rackSpecials.setLayout(new BorderLayout());
		rackSpecials.add(rackSpecials(), BorderLayout.SOUTH);

		// divider bar
		JLabel darkBar = createJLabelImg("separator", Toolkit
				.getDefaultToolkit().getScreenSize().width / 3, Toolkit
				.getDefaultToolkit().getScreenSize().width, 1f);
		darkBar.setVerticalAlignment(JLabel.TOP);
		darkBar.setHorizontalAlignment(JLabel.LEFT);

		JPanel darkBarWrapper = new JPanel();
		darkBarWrapper.setOpaque(false);
		darkBarWrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
		darkBarWrapper.add(darkBar);

		JPanel top = new JPanel();
		top.setOpaque(false);
		top.setLayout(new BorderLayout());
		top.add(currentScore, BorderLayout.WEST);
		top.add(rackSpecials, BorderLayout.EAST);

		rackTiles = new JPanel();
		rackTiles.setOpaque(false);
		rackTiles.setLayout(new BorderLayout());
		rackTiles.add(rackTiles(), BorderLayout.WEST);

		JPanel divider = new JPanel();
		divider.setOpaque(false);
		divider.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width / 6, Toolkit.getDefaultToolkit()
				.getScreenSize().width / 136));
		JPanel divider2 = new JPanel();
		divider2.setOpaque(false);
		divider2.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width / 6, Toolkit.getDefaultToolkit()
				.getScreenSize().width / 136));

		JPanel header = new JPanel();
		header.setOpaque(false);
		header.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width / 6, Toolkit.getDefaultToolkit()
				.getScreenSize().height / 21));

		JPanel activePlayer = new JPanel();
		activePlayer.setLayout(new BoxLayout(activePlayer, BoxLayout.Y_AXIS));
		activePlayer.setOpaque(false);
		activePlayer.add(header);
		activePlayer.add(top);
		activePlayer.add(divider);
		activePlayer.add(darkBarWrapper);
		activePlayer.add(divider2);
		activePlayer.add(rackTiles);

		JPanel activePlayerWrapper = new JPanel();
		activePlayerWrapper.setOpaque(false);
		activePlayerWrapper.setLayout(new BorderLayout());
		activePlayerWrapper.setPreferredSize(new Dimension(Toolkit
				.getDefaultToolkit().getScreenSize().width / 5, Toolkit
				.getDefaultToolkit().getScreenSize().height / 3));
		activePlayerWrapper.add(activePlayer, BorderLayout.NORTH);

		// all other players
		JPanel inactivePlayers = new JPanel();
		inactivePlayers.setOpaque(false);
		inactivePlayers.setLayout(new BoxLayout(inactivePlayers,
				BoxLayout.Y_AXIS));
		int playerCount = 5 - (game.getPlayerList().size() - 1);
		while (playerCount > 0) {
			// player info
			JLabel blank = new JLabel(" ");

			blank.setFont(steelfishIt.deriveFont(53f));
			blank.setVerticalAlignment(JLabel.BOTTOM);

			// divider bar
			JLabel blankBar = createJLabelImg("separator_blank", Toolkit
					.getDefaultToolkit().getScreenSize().width / 4, Toolkit
					.getDefaultToolkit().getScreenSize().width / 4, 1f);
			blankBar.setVerticalAlignment(JLabel.TOP);
			JPanel blankBarWrapper = new JPanel();
			blankBarWrapper.setOpaque(false);
			blankBarWrapper.add(blankBar);

			// put all together
			JPanel blankWrapper = new JPanel();
			blankWrapper.setOpaque(false);
			blankWrapper.setLayout(new BorderLayout());
			blankWrapper.setPreferredSize(new Dimension(Toolkit
					.getDefaultToolkit().getScreenSize().width / 5, Toolkit
					.getDefaultToolkit().getScreenSize().height / 5));
			blankWrapper.add(blankBarWrapper, BorderLayout.SOUTH);
			blankWrapper.add(blank, BorderLayout.WEST);
			inactivePlayers.add(blankWrapper);
			playerCount--;
		}
		for (int i = 0; i < game.getPlayerList().size(); i++) {
			int index = (game.getPlayerList().indexOf(game.getTurn()) + i)
					% game.getPlayerList().size();
			if (!game.getPlayerList().get(index).equals(game.getTurn())) {
				// player info
				JLabel playerName = new JLabel("   "
						+ game.getPlayerList().get(index).getName() + "   ");
				playerName.setFont(steelfishIt.deriveFont(53f));
				playerName.setForeground(new Color(127, 127, 127));
				playerName.setVerticalAlignment(JLabel.BOTTOM);
				JLabel playerScore = new JLabel(game.getPlayerList().get(index)
						.getScore()
						+ "   ");
				playerScore.setFont(steelfishIt.deriveFont(43f));
				playerScore.setForeground(new Color(191, 191, 191));
				playerScore.setVerticalAlignment(JLabel.BOTTOM);

				// divider bar
				JLabel bar = createJLabelImg("separator_light", Toolkit
						.getDefaultToolkit().getScreenSize().width / 4, Toolkit
						.getDefaultToolkit().getScreenSize().width / 4, 1f);
				bar.setVerticalAlignment(JLabel.TOP);
				JPanel barWrapper = new JPanel();
				barWrapper.setOpaque(false);
				barWrapper.add(bar);

				// put all together
				JPanel inactivePlayer = new JPanel();
				inactivePlayer.setOpaque(false);
				inactivePlayer.setLayout(new BorderLayout());
				inactivePlayer.setPreferredSize(new Dimension(Toolkit
						.getDefaultToolkit().getScreenSize().width / 5, Toolkit
						.getDefaultToolkit().getScreenSize().height / 5));
				inactivePlayer.add(barWrapper, BorderLayout.SOUTH);
				inactivePlayer.add(playerName, BorderLayout.WEST);
				inactivePlayer.add(playerScore, BorderLayout.EAST);
				inactivePlayers.add(inactivePlayer);
			}
		}

		// empty footer
		JPanel footer = new JPanel();
		footer.setOpaque(false);
		footer.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width / 5, (Toolkit.getDefaultToolkit()
				.getScreenSize().height / 4)
				+ Toolkit.getDefaultToolkit().getScreenSize().height / 80));

		// contains all of above
		JPanel playersWrapper = new JPanel();
		playersWrapper.setOpaque(false);
		playersWrapper.setPreferredSize(new Dimension(Toolkit
				.getDefaultToolkit().getScreenSize().width / 4, Toolkit
				.getDefaultToolkit().getScreenSize().height));
		playersWrapper.setLayout(new BorderLayout());
		playersWrapper.add(activePlayerWrapper, BorderLayout.NORTH);
		playersWrapper.add(inactivePlayers, BorderLayout.CENTER);
		playersWrapper.add(footer, BorderLayout.SOUTH);

		JPanel players = new JPanel();
		players.setLayout(new BorderLayout());
		players.setOpaque(false);
		players.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width / 3, Toolkit.getDefaultToolkit()
				.getScreenSize().height));
		players.add(playersWrapper, BorderLayout.WEST);
		return players;
	}

	/**
	 * creates and returns a JLabel with an icon instead of text
	 * 
	 * @param file
	 *            file to draw icon
	 * @param w
	 *            width to scale icon to
	 * @param h
	 *            height to scale icon to
	 * @param opacity
	 *            opacity to draw icon at
	 * @return jlabel with icon
	 */
	private JLabel createJLabelImg(String file, int w, int h, float opacity) {
		String path = "src/main/resources/";
		String type = ".png";
		JLabel label = new JLabel();
		if (file.contains(path))
			file = file.replace(path, "");
		if (file.contains(type))
			file = file.replace(type, "");
		Image image = new ImageIcon(path + file + type).getImage();
		Dimension scaled = Util.getScaledDimension(
				new Dimension(image.getWidth(this), image.getHeight(this)),
				new Dimension(w, h));
		label.setIcon(Util.fixImage(
				new ImageIcon(Util.getScaledImage(image, scaled.width,
						scaled.height)), label, opacity));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setOpaque(false);
		return label;
	}

	/**
	 * creates a new ImageIcon with the given opacity
	 * 
	 * @param file
	 *            file to draw icon
	 * @param w
	 *            width to scale icon to
	 * @param h
	 *            height to scale icon to
	 * @param opacity
	 *            opacity to draw icon at
	 * @return ImageIcon
	 */
	private ImageIcon changeImgOpacity(String file, int w, int h, float opacity) {
		String path = "src/main/resources/";
		String type = ".png";
		JLabel label = new JLabel();
		if (file.contains(path))
			file = file.replace(path, "");
		if (file.contains(type))
			file = file.replace(type, "");
		Image image = new ImageIcon(path + file + type).getImage();
		Dimension scaled = Util.getScaledDimension(
				new Dimension(image.getWidth(this), image.getHeight(this)),
				new Dimension(w, h));
		return Util.fixImage(
				new ImageIcon(Util.getScaledImage(image, scaled.width,
						scaled.height)), label, opacity);
	}

	/**
	 * draws the vertical lines on the board grid
	 * 
	 * @return Jpanel
	 */
	private JPanel vLines() {
		JPanel v = new JPanel();
		v.setOpaque(false);
		v.setLayout(new BoxLayout(v, BoxLayout.X_AXIS));

		for (int i = 0; i < 16; i++) {
			if (i == 0)
				v.add(createJLabelImg("score_blank", squareSize, squareSize, 1f));
			v.add(createJLabelImg("line_v", boardBorder, boardBorder, 1f));
			if (i == 15)
				v.add(createJLabelImg("score_blank", squareSize, squareSize, 1f));
			else
				v.add(createJLabelImg("score_blank", squareSize, squareSize, 1f));
		}
		return v;
	}

	/**
	 * draws the horizontal lines on the board grid
	 * 
	 * @return Jpanel
	 */
	private JPanel hLines() {
		JPanel h = new JPanel();
		h.setOpaque(false);
		h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));

		for (int i = 0; i < 16; i++) {
			if (i == 0)
				h.add(createJLabelImg("score_blank", squareSize, squareSize, 1f));
			h.add(createJLabelImg("line_h", boardBorder, boardBorder, 1f));
			if (i == 15)
				h.add(createJLabelImg("score_blank", squareSize, squareSize, 1f));
			else
				h.add(createJLabelImg("score_blank", squareSize, squareSize, 1f));
		}
		return h;
	}

	/**
	 * draws the special tiles on the board grid
	 * 
	 * @return Jlayeredpane
	 */
	private JLayeredPane specials() {
		JLayeredPane s = new JLayeredPane();

		int paneIndex = 0;
		for (int r = 0; r < 15; r++)
			for (int c = 0; c < 15; c++)
				for (int i = game.getSquare(r, c).getSpecials().size() - 1; i >= 0; i--)
					if (game.getSquare(r, c).getSpecials().get(i).getOwner()
							.equals(game.getTurn())) {
						JPanel special = new JPanel();
						special.setLayout(new BorderLayout());
						int startR = r * (squareSize + squareGap);
						int startC = c * (squareSize + squareGap);
						int size = squareSize - (squareGap * 4);
						special.setBounds(startC, startR, squareSize,
								squareSize);
						special.setOpaque(false);
						String file = game.getSquare(r, c).getSpecials().get(i)
								.getIcon();
						special.add(createJLabelImg(file, size, size, 1f),
								BorderLayout.CENTER);
						s.add(special, new Integer(paneIndex), 0);
						paneIndex++;
						break;
					}
		return s;
	}

	/**
	 * draws the score modifiers on the board grid
	 * 
	 * @return jlayeredpane
	 */
	private JLayeredPane scoreModifiers() {
		JLayeredPane s = new JLayeredPane();

		int paneIndex = 0;

		for (int r = 0; r < 15; r++)
			for (int c = 0; c < 15; c++)
				if (game.getSquare(r, c).getScoreModifier() != null) {
					JPanel mod = new JPanel();
					mod.setLayout(new BorderLayout());
					int startR = r * (squareSize + squareGap);
					int startC = c * (squareSize + squareGap);
					mod.setBounds(startC, startR, squareSize, squareSize);
					mod.setOpaque(false);
					String file = game.getSquare(r, c).getScoreModifier();
					if (r == 7 && c == 7)
						file = "star";
					mod.add(createJLabelImg(file, squareSize, squareSize, 1f),
							BorderLayout.CENTER);
					s.add(mod, new Integer(paneIndex), 0);
					paneIndex++;
				}
		return s;
	}

	/**
	 * draws the "add icons" at the start of each word in the list of current
	 * possible move locations. Does not display a visible icon if hints are
	 * turned off
	 * 
	 * @return jlayeredpane
	 */
	private JLayeredPane positionsStart() {
		startPos = new JLabel[15][15];

		JLayeredPane p = new JLayeredPane();

		int paneIndex = 0;

		for (int r = 0; r < 15; r++)
			for (int c = 0; c < 15; c++)
				for (int i = 0; i < moves.size(); i++)
					if (moves.get(i).get(0).getLoc().getRow() == r
							&& moves.get(i).get(0).getLoc().getCol() == c) {
						boolean added = false;
						JPanel pos = new JPanel();
						pos.setLayout(new BorderLayout());
						int startR = r * (squareSize + squareGap);
						int startC = c * (squareSize + squareGap);
						pos.setBounds(startC, startR, squareSize, squareSize);
						pos.setOpaque(false);
						float opacity = .8f;
						if (game.getSquare(r, c).getLetter() != null
								|| (r == 7 && c == 7))
							opacity = .5f;
						String file = "position";
						if (!gameInfo.allowsHints())
							file = "score_blank";
						JLabel posIcon = createJLabelImg(file, squareSize,
								squareSize, opacity);
						posIcon.addMouseListener(this);
						if (!added) {
							startPos[r][c] = posIcon;
							pos.add(posIcon, BorderLayout.CENTER);
							p.add(pos, new Integer(paneIndex), 0);
							paneIndex++;
						}
						added = true;
					}
		return p;
	}

	/**
	 * draws the played tiles on the board grid
	 * 
	 * @return jlayeredpane
	 */
	private JLayeredPane tiles() {
		JLayeredPane t = new JLayeredPane();

		int paneIndex = 0;

		for (int r = 0; r < 15; r++)
			for (int c = 0; c < 15; c++)
				if (game.getSquare(r, c).getLetter() != null) {
					JPanel tile = new JPanel();
					tile.setLayout(new BorderLayout());
					int startR = r * (squareSize + squareGap);
					int startC = c * (squareSize + squareGap);
					tile.setBounds(startC, startR, squareSize, squareSize);
					tile.setOpaque(false);
					String file = game.getSquare(r, c).getLetter().getIcon();
					tile.add(createJLabelImg(file, squareSize, squareSize, 1f),
							BorderLayout.CENTER);
					t.add(tile, new Integer(paneIndex), 0);
					paneIndex++;
				}
		return t;
	}

	/**
	 * draws the tiles of the active player's rack to the active player panel
	 * 
	 * @return jpanel
	 */
	private JPanel rackTiles() {
		rackFull.clear();
		rackFullChars.clear();
		rackHalf.clear();
		rackHalfChars.clear();
		JPanel tiles = new JPanel();
		tiles.setOpaque(false);
		tiles.setLayout(new BorderLayout());
		JPanel row1 = new JPanel();
		row1.setLayout(new FlowLayout(FlowLayout.LEFT, Toolkit
				.getDefaultToolkit().getScreenSize().width / 210, 0));
		row1.setOpaque(false);
		JPanel row2 = new JPanel();
		row2.setLayout(new FlowLayout(FlowLayout.LEFT, Toolkit
				.getDefaultToolkit().getScreenSize().width / 210, Toolkit
				.getDefaultToolkit().getScreenSize().width / 210));
		row1.setOpaque(false);
		row2.setOpaque(false);
		JLabel space = new JLabel("  ");
		JPanel spaceRow1 = new JPanel();
		spaceRow1.setPreferredSize(new Dimension(
				space.getPreferredSize().width, 0));
		row1.add(spaceRow1);
		JPanel spaceRow2 = new JPanel();
		spaceRow2.setPreferredSize(new Dimension(
				space.getPreferredSize().width, 0));
		row2.add(spaceRow2);
		for (int i = 0; i < game.getTurn().getRack().size(); i++) {
			JLabel tile = createJLabelImg(
					(game.getTurn().getRack().get(i).getIcon()).toLowerCase(),
					Toolkit.getDefaultToolkit().getScreenSize().width / 36,
					Toolkit.getDefaultToolkit().getScreenSize().width / 36, 1f);
			if (i < 7)
				row1.add(tile);
			else
				row2.add(tile);
			rackFull.add(tile);
			rackFullChars.add(game.getTurn().getRack().get(i).getChar());
		}

		JPanel row2Wrapper = new JPanel();
		row2Wrapper.setOpaque(false);
		row2Wrapper.setLayout(new BorderLayout());
		row2Wrapper.add(row2, BorderLayout.NORTH);
		tiles.add(row1, BorderLayout.NORTH);
		tiles.add(row2Wrapper, BorderLayout.CENTER);
		return tiles;
	}

	/**
	 * draws the special tiles by the active player panel, setting the opacity
	 * to half for each tile the player does not currently own
	 * 
	 * @return jpanel
	 */
	private JPanel rackSpecials() {
		specialIcons = new JLabel[game.getTurn().getSpecialsList().size()];
		JPanel specials = new JPanel();
		specials.setOpaque(false);
		specials.setLayout(new FlowLayout(FlowLayout.LEFT, Toolkit
				.getDefaultToolkit().getScreenSize().width / 230, 0));
		for (int i = 0; i < game.getSpecialsList().size(); i++) {
			float opacity = .5f;
			boolean ownsSpecial = false;
			int index = -1;
			for (int j = 0; j < game.getTurn().getSpecialsList().size(); j++)
				if (game.getSpecialsList().get(i)
						.equals(game.getTurn().getSpecialsList().get(j))) {
					opacity = 1f;
					ownsSpecial = true;
					index = j;
					break;
				}
			JLabel special = createJLabelImg(game.getSpecialsList().get(i)
					.getIcon(),
					Toolkit.getDefaultToolkit().getScreenSize().width / 38,
					Toolkit.getDefaultToolkit().getScreenSize().width / 38,
					opacity);
			special.setVerticalAlignment(JLabel.BOTTOM);
			if (ownsSpecial) {
				special.setCursor(new Cursor(Cursor.HAND_CURSOR));
				special.addMouseListener(this);
				specialIcons[index] = special;
			}
			specials.add(special);
		}
		specials.add(new JLabel(""));
		JPanel specialsWrapper = new JPanel();
		specialsWrapper.setOpaque(false);
		specialsWrapper.setLayout(new BorderLayout());
		specialsWrapper.add(specials, BorderLayout.SOUTH);
		return specialsWrapper;
	}

	/**
	 * adds mouse listeners to all the spots a special tile can be placed by a
	 * player and displays the tile's icon wherever the mouse is.
	 * 
	 * @return jlayeredpane
	 */
	private JLayeredPane specialSpots() {
		specialSpots = new JLabel[15][15];
		specialSpotsShow = new JLabel[15][15];
		JLayeredPane s = new JLayeredPane();

		int paneIndex = 0;

		for (int r = 0; r < 15; r++)
			for (int c = 0; c < 15; c++) {
				int startR = r * (squareSize + squareGap);
				int startC = c * (squareSize + squareGap);
				float opacity = .5f;

				JPanel showSpot = new JPanel();
				showSpot.setLayout(new BorderLayout());
				showSpot.setBounds(startC, startR, squareSize, squareSize);
				showSpot.setOpaque(false);
				String file = game.getTurn().getSpecialsList()
						.get(specialToAdd).getIcon();
				int iconSize = squareSize - (4 * squareGap);
				JLabel spotIcon = createJLabelImg(file, iconSize, iconSize,
						opacity);
				spotIcon.setVisible(false);
				showSpot.add(spotIcon, BorderLayout.CENTER);
				s.add(showSpot, new Integer(paneIndex), 0);
				paneIndex++;

				JPanel emptySpot = new JPanel();
				emptySpot.setLayout(new BorderLayout());

				emptySpot.setBounds(startC, startR, squareSize, squareSize);
				emptySpot.setOpaque(false);
				JLabel icon = createJLabelImg("score_blank", squareSize,
						squareSize, .01f);
				icon.addMouseListener(this);
				if (game.getSquare(r, c).getLetter() == null)
					icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
				emptySpot.add(icon, BorderLayout.CENTER);
				s.add(emptySpot, new Integer(paneIndex), 0);
				paneIndex++;

				specialSpots[r][c] = icon;
				specialSpotsShow[r][c] = spotIcon;
			}
		return s;
	}

	/**
	 * draws the arrows for a corner tile with two move options/directions on
	 * the board grid
	 * 
	 * @return jlayeredpane
	 */
	private JLayeredPane doubleOptions() {
		direction.clear();

		JLayeredPane p = new JLayeredPane();

		int paneIndex = 0;

		for (int i = 0; i < movesToShow.size(); i++) {
			String dir = "down";

			for (int j = 0; j < movesToShow.get(i).size(); j++) {
				if (i + 1 < movesToShow.get(i).size())
					if (movesToShow.get(i).get(i).getLoc().getRow() == movesToShow
							.get(i).get(i + 1).getLoc().getRow())
						dir = "right";

				JPanel tile = new JPanel();
				tile.setLayout(new BorderLayout());
				int startR = movesToShow.get(i).get(j).getLoc().getRow()
						* (squareSize + squareGap);
				int startC = movesToShow.get(i).get(j).getLoc().getCol()
						* (squareSize + squareGap);
				tile.setBounds(startC, startR, squareSize, squareSize);
				tile.setOpaque(false);
				String file = movesToShow.get(i).get(j).getChar() + "";
				JLabel posIcon = createJLabelImg(file, squareSize, squareSize,
						.5f);
				tile.add(posIcon, BorderLayout.CENTER);
				p.add(tile, new Integer(paneIndex), 0);
				paneIndex++;
			}

			JPanel arrow = new JPanel();
			arrow.setLayout(new BorderLayout());
			int startR = movesToShow.get(i).get(0).getLoc().getRow()
					* (squareSize + squareGap);
			int startC = movesToShow.get(i).get(0).getLoc().getCol()
					* (squareSize + squareGap);
			if (dir.equals("right"))
				startC += squareSize + squareGap;
			else if (dir.equals("down"))
				startR += squareSize + squareGap;
			arrow.setBounds(startC, startR, squareSize, squareSize);
			arrow.setOpaque(false);
			int size = squareSize - (squareSize / 3);
			JLabel arrowIcon = createJLabelImg(dir, size, size, 1f);
			arrowIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
			arrowIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for (int i = 0; i < direction.size(); i++)
						// insert the word at the given direction
						if (e.getComponent() == direction.get(i)) {
							insertWord(i);
							gameInfo.setTurnOver(true);
							swapTiles
									.setIcon(changeImgOpacity(
											"swaptiles",
											Toolkit.getDefaultToolkit()
													.getScreenSize().width / 15,
											Toolkit.getDefaultToolkit()
													.getScreenSize().height,
											.5f));
							swapTiles.setCursor(Cursor.getDefaultCursor());
							swapTiles.validate();
							swapTiles.repaint();
							options.validate();
							options.repaint();
							updateScreen();
							if (game.isGameOver())
								notification.setText("Game over! "
										+ game.getTurn().getName() + " wins!");
							else
								notification.setText("Added tiles!");
						}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					positions.removeAll();
					positions.add(positionsStart, BorderLayout.CENTER);
					positions.validate();
					positions.repaint();
				}
			});
			direction.add(arrowIcon);
			arrow.add(arrowIcon, BorderLayout.CENTER);
			p.add(arrow, new Integer(paneIndex), 0);
			paneIndex++;
		}
		return p;
	}

	/**
	 * draws tiles at a specific location a move can be added to on the board
	 * grid
	 * 
	 * @param row
	 *            row to begin drawing the word at
	 * @param col
	 *            column to begin drawing thw word at
	 * @return jlayeredpane
	 */
	private JLayeredPane positionsSelect(int row, int col) {
		movesToShow = new ArrayList<Move>();
		for (int j = 0; j < moves.size(); j++)
			if (moves.get(j).get(0).getLoc().getRow() == row
					&& moves.get(j).get(0).getLoc().getCol() == col)
				movesToShow.add(moves.get(j));

		JLayeredPane p = new JLayeredPane();

		int paneIndex = 0;

		for (int i = 0; i < movesToShow.size(); i++)
			for (int j = 0; j < movesToShow.get(i).size(); j++) {
				JPanel tile = new JPanel();
				tile.setLayout(new BorderLayout());
				int startR = movesToShow.get(i).get(j).getLoc().getRow()
						* (squareSize + squareGap);
				int startC = movesToShow.get(i).get(j).getLoc().getCol()
						* (squareSize + squareGap);
				tile.setBounds(startC, startR, squareSize, squareSize);
				tile.setOpaque(false);
				String file = movesToShow.get(i).get(j).getChar() + "";
				JLabel posIcon = createJLabelImg(file, squareSize, squareSize,
						.5f);
				posIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
				posIcon.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// if there are multiple moves at the location, display
						// the right/down arrows
						if (movesToShow.size() > 1) {
							positions.removeAll();
							positions.add(doubleOptions(), BorderLayout.CENTER);
							positions.validate();
							positions.repaint();
						}
						// otherwise submit the current move
						else {
							insertWord(0);
							gameInfo.setTurnOver(true);
							swapTiles
									.setIcon(changeImgOpacity(
											"swaptiles",
											Toolkit.getDefaultToolkit()
													.getScreenSize().width / 15,
											Toolkit.getDefaultToolkit()
													.getScreenSize().height,
											.5f));
							swapTiles.setCursor(Cursor.getDefaultCursor());
							swapTiles.validate();
							swapTiles.repaint();
							options.validate();
							options.repaint();
							updateScreen();
							if (game.isGameOver())
								notification.setText("Game over! "
										+ game.getTurn().getName() + " wins!");
							else
								notification.setText("Added tiles!");
						}
					}

					@Override
					public void mouseExited(MouseEvent e) {
						positions.removeAll();
						positions.add(positionsStart, BorderLayout.CENTER);
						positions.validate();
						positions.repaint();
					}
				});
				tile.add(posIcon, BorderLayout.CENTER);
				p.add(tile, new Integer(paneIndex), 0);
				paneIndex++;
			}
		return p;
	}

	/**
	 * updates the screen and variable info to a natural/dormant state, usually
	 * called after a big function such as adding tiles or changing player info
	 */
	private void updateScreen() {
		// clear searched postion variables
		if (positions != null) {
			positions.removeAll();
			positions.validate();
			rackFull.clear();
			rackFullChars.clear();
			rackHalf.clear();
			rackHalfChars.clear();
			board.remove(positions);
			positions = null;
		}
		// update board
		board.validate();
		board.repaint();
		tiles.removeAll();
		tiles.add(tiles(), BorderLayout.CENTER);
		tiles.validate();
		tiles.repaint();
		specials.removeAll();
		specials.add(specials(), BorderLayout.CENTER);
		specials.validate();
		specials.repaint();
		// update text field
		input.setText("");
		if (gameInfo.isTurnOver()) {
			PromptSupport.setPrompt("", input);
			input.setEnabled(false);
		}
		input.validate();
		input.repaint();
		// update player panel
		// currentScore.setText("  " + game.getTurn().getScore() + "   ");
		// currentScore.validate();
		// currentScore.repaint();
		rackTiles.removeAll();
		rackTiles.add(rackTiles());
		rackTiles.validate();
		rackTiles.repaint();
		rackSpecials.removeAll();
		rackSpecials.add(rackSpecials(), BorderLayout.CENTER);
		rackSpecials.validate();
		rackSpecials.repaint();
		// refresh screen
		validate();
		repaint();
	}

	/**
	 * starts a new thread to check for the currently inputted word
	 */
	private void startCheck() {
		Thread startCheck = new Thread(new Runnable() {
			public void run() {
				searchIndex++;
				getMoves.run(word, searchIndex);
			}
		});
		if (!shuffle)
			startCheck.start();
	}

	/**
	 * inserts the move at the given index in the list of moves to the board.
	 * 
	 * @param index
	 *            index for the list of moves
	 */
	private void insertWord(int index) {
		ArrayList<String> alerts = game.insertWord(movesToShow.get(index));
		if (alerts != null && alerts.size() > 0) {
			String alert = "";
			if (alerts.size() > 1) {
				gameInfo.setNotificationText("Added tiles while triggering "
						+ alerts.size() + " special tiles!");
				alert = "You triggered " + alerts.size() + " special tiles!";
			} else {
				gameInfo.setNotificationText("Added tiles while triggering a special tile!");
				alert = "You triggered a special tile!";
			}
			removeAll();
			revalidate();
			repaint();
			alerts.add("Hit continue.");
			add(new NotificationScreen(gameInfo, alert, alerts, "game"));
		}
		Thread scoreUpdate = new Thread(new Runnable() {
			public void run() {
				String currentPlayer = game.getTurn().getName();
				int oldScore = Integer.parseInt(currentScore.getText().replaceAll("[^\\d]", ""));
				int newScore = game.getTurn().getScore();
				int changeFactor = 0;
				if(oldScore<newScore)
					changeFactor++;		
				else if(oldScore>newScore)
					changeFactor--;
				else
					return;
				while(currentPlayer.equals(game.getTurn().getName()) && oldScore!=newScore) {
					oldScore+=changeFactor;
					currentScore.setText("  " + oldScore + "   ");
					try {
						Thread.sleep(75);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		scoreUpdate.start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// if hints are not allowed, display the move only if the user clicks
		// the starting point
		if (startPos != null && !gameInfo.allowsHints())
			for (int r = 0; r < startPos.length; r++)
				for (int c = 0; c < startPos.length; c++)
					if (startPos[r][c] != null
							&& e.getComponent() == startPos[r][c]) {
						positions.removeAll();
						positions.add(positionsSelect(r, c),
								BorderLayout.CENTER);
						positions.validate();
						positions.repaint();
						return;
					}
		// insert the selected special tile onto the selected square location
		if (specialSpots != null)
			for (int r = 0; r < specialSpots.length; r++)
				for (int c = 0; c < specialSpots.length; c++)
					if (e.getComponent() == specialSpots[r][c]
							&& game.getSquare(r, c).getLetter() == null) {
						game.addSpecial(
								game.getTurn().getSpecialsList()
										.get(specialToAdd), r, c);
						// clear special spot variables
						if (spots != null) {
							spots.removeAll();
							spots.validate();
							board.remove(spots);
							spots = null;
							specialSpots = null;
							specialToAdd = -1;
							input.setEnabled(true);
						}
						updateScreen();

						notification.setText("Added special tile!");
						return;
					}
		// if a special icon is clicked, change view mode to insert that icon.
		// if same icon is clicked again, disable insert mode
		for (int i = 0; i < specialIcons.length; i++)
			if (e.getComponent() == specialIcons[i]) {
				if (specialSpots != null) {
					spots.removeAll();
					spots.validate();
					board.remove(spots);
					spots = null;
					specialSpots = null;
					notification.setText("Type letters to input.");
					input.requestFocus();
					input.setEnabled(true);
				}
				// if the newly clicked special tile is not the currently
				// selected one,
				// switch focus to the newly selected one
				if (i != specialToAdd) {
					specialToAdd = i;
					notification
							.setText("Choose a square to add the special tile.");
					PromptSupport.setPrompt("", input);
					input.setEnabled(false);
					spots = new JPanel();
					spots.setLayout(new BorderLayout());
					spots.setBounds(tilesStart, tilesStart, tilesBorder,
							tilesBorder);
					spots.setOpaque(false);
					spots.add(specialSpots(), BorderLayout.CENTER);
					board.add(spots, new Integer(6), 0);
					updateScreen();
					return;
				} else
					specialToAdd = -1;
			}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// if hints are allowed, auto-show the move location on hover
		if (startPos != null && gameInfo.allowsHints())
			for (int r = 0; r < startPos.length; r++)
				for (int c = 0; c < startPos.length; c++)
					if (startPos[r][c] != null
							&& e.getComponent() == startPos[r][c]) {
						positions.removeAll();
						positions.add(positionsSelect(r, c),
								BorderLayout.CENTER);
						positions.validate();
						positions.repaint();
						return;
					}
		// if in special-tile-insert mode, display the tile icon at the location
		// of the mouse (or the square where the mouse is over)
		if (specialSpots != null)
			for (int r = 0; r < specialSpots.length; r++)
				for (int c = 0; c < specialSpots.length; c++)
					if (specialSpots[r][c] != null)
						if (e.getComponent() == specialSpots[r][c]
								&& game.getSquare(r, c).getLetter() == null) {
							specialSpotsShow[r][c].setVisible(true);

						} else
							specialSpotsShow[r][c].setVisible(false);
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * get GameInfo object
	 * 
	 * @return gameInfo
	 */
	public GameInfo threadGetGameInfo() {
		return gameInfo;
	}

	/**
	 * get notification JLabel
	 * 
	 * @return notification
	 */
	public JLabel threadGetNotification() {
		return notification;
	}

	/**
	 * get search index
	 * 
	 * @return index
	 */
	public int threadGetSearchIndex() {
		return searchIndex;
	}

	/**
	 * get tilesStart
	 * 
	 * @return tilesStart
	 */
	public int threadGetTilesStart() {
		return tilesStart;
	}

	/**
	 * get tilesBorder
	 * 
	 * @return tilesBorder
	 */
	public int threadGetTilesBorder() {
		return tilesBorder;
	}

	/**
	 * get board
	 * 
	 * @return board
	 */
	public JLayeredPane threadGetBoard() {
		return board;
	}

	/**
	 * get positions
	 * 
	 * @return positions
	 */
	public JPanel threadGetPositions() {
		return positions;
	}

	/**
	 * set positions
	 * 
	 * @param positions
	 *            to be set
	 */
	public void threadSetPositions(JPanel positions) {
		this.positions = positions;
	}

	/**
	 * get positionsStart
	 * 
	 * @return positionsStart
	 */
	public JLayeredPane threadGetPositionsStart() {
		return positionsStart;
	}

	/**
	 * set positionsStart
	 */
	public void threadSetPositionsStart() {
		positionsStart = positionsStart();
	}

	/**
	 * get moves list
	 * 
	 * @return moves
	 */
	public ArrayList<Move> threadGetMoves() {
		return moves;
	}

	/**
	 * set new list of moves
	 * 
	 * @param moves
	 *            list of moves
	 */
	public void threadSetMoves(ArrayList<Move> moves) {
		this.moves = moves;
	}
}