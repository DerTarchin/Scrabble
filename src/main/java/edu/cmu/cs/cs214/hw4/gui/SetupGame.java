package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw4.core.Game;

/**
 * starts the game setup and initializes the Game object
 * 
 * @author Hizal
 *
 */
public class SetupGame extends JPanel implements Window {
	private Font steelfish;
	private int boardLength = 15;
	private final GameInfo game;

	/**
	 * draws the screen with setup options
	 * 
	 * @param game
	 *            GameInfo object
	 */
	public SetupGame(final GameInfo game) {
		this.game = game;
		steelfish = Util.getFont("steelfish");

		drawWindow(setupComponents());
	}

	@Override
	public JPanel setupComponents() {
		// game logo
				JLabel logo = new JLabel();
				Image logoFull = new ImageIcon("src/main/resources/logo.png")
						.getImage();
				Dimension scaled = Util
						.getScaledDimension(new Dimension(logoFull.getWidth(this),
								logoFull.getHeight(this)), new Dimension(Toolkit
								.getDefaultToolkit().getScreenSize().width / 2, Toolkit
								.getDefaultToolkit().getScreenSize().height / 2));
				logo.setIcon(new ImageIcon(Util.getScaledImage(logoFull, scaled.width,
						scaled.height)));
				logo.setHorizontalAlignment(JLabel.CENTER);

				// quick game button
				JLabel quick = new JLabel("Quick Game");
				quick.setForeground(new Color(127, 96, 0));
				quick.setCursor(new Cursor(Cursor.HAND_CURSOR));
				quick.setFont(steelfish.deriveFont(39f));
				quick.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						removeAll();
						revalidate();
						repaint();
						Thread addGame = new Thread(new Runnable() {
							public void run() {
								game.setGame(new Game(boardLength, 9,
										"src/main/resources/"));
							}
						});
						addGame.start();
						add(new SetupHints(game));
					}
				});

				// game normal button
				JLabel normal = new JLabel("Normal Game");
				normal.setForeground(new Color(127, 96, 0));
				normal.setCursor(new Cursor(Cursor.HAND_CURSOR));
				normal.setFont(steelfish.deriveFont(39f));
				normal.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						removeAll();
						revalidate();
						repaint();
						Thread addGame = new Thread(new Runnable() {
							public void run() {
								game.setGame(new Game(boardLength, 7,
										"src/main/resources/"));
							}
						});
						addGame.start();
						add(new SetupHints(game));
					}
				});

				JLabel description = new JLabel(
						"       A quick game gives players more tiles than a normal game.");
				description.setForeground(new Color(127, 96, 0));
				description.setFont(steelfish.deriveFont(20f));
				description.setHorizontalAlignment(JLabel.CENTER);
				description.setVerticalAlignment(JLabel.NORTH);
				
				JPanel space = new JPanel();
				space.setOpaque(false);
				space.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
						.getScreenSize().width/50,1));

				
				JPanel menuItems = new JPanel();
				menuItems.setLayout(new FlowLayout(FlowLayout.CENTER, 0, Toolkit.getDefaultToolkit()
						.getScreenSize().width/200));
				menuItems.add(normal);
				menuItems.add(space);
				menuItems.add(quick);
				menuItems.setOpaque(false);
				
				JPanel menu = new JPanel();
				menu.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
						.getScreenSize().width, Toolkit.getDefaultToolkit()
						.getScreenSize().height / 3));
				menu.setOpaque(false);
				menu.setLayout(new BorderLayout());
				menu.add(menuItems, BorderLayout.NORTH);
				menu.add(description, BorderLayout.CENTER);
				
				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout());
				panel.add(logo, BorderLayout.CENTER);
				panel.add(menu, BorderLayout.SOUTH);
				panel.setBackground(Color.WHITE);
				
				return panel;
	}

	@Override
	public void drawWindow(JPanel panel) {
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		setVisible(true);	
	}
}
