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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw4.core.Game;

/**
 * screen to choose whether or not to allow hints
 * 
 * @author Hizal
 *
 */
public class SetupHints extends JPanel implements Window {
	private final GameInfo game;
	private Font steelfish;

	/**
	 * draws the options on screen to allow hints
	 * 
	 * @param game
	 *            GameInfo object
	 */
	public SetupHints(final GameInfo game) {
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

		// casual option
		JLabel casual = new JLabel("Casual Mode");
		casual.setForeground(new Color(127, 96, 0));
		casual.setCursor(new Cursor(Cursor.HAND_CURSOR));
		casual.setFont(steelfish.deriveFont(39f));
		casual.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				removeAll();
				revalidate();
				repaint();
				game.setHints(true);
				add(new AddPlayers(game));
			}
		});

		// classic option
		JLabel classic = new JLabel("Classic Mode");
		classic.setForeground(new Color(127, 96, 0));
		classic.setCursor(new Cursor(Cursor.HAND_CURSOR));
		classic.setFont(steelfish.deriveFont(39f));
		classic.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				removeAll();
				revalidate();
				repaint();
				game.setHints(false);
				add(new AddPlayers(game));
			}
		});

		JLabel description = new JLabel(
				"        Casual Mode displays the possible locations for a word.");
		description.setHorizontalAlignment(JLabel.CENTER);
		description.setVerticalAlignment(JLabel.TOP);
		description.setForeground(new Color(127, 96, 0));
		description.setFont(steelfish.deriveFont(20f));

		JPanel space = new JPanel();
		space.setOpaque(false);
		space.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width / 50, 1));

		JPanel menuItems = new JPanel();
		menuItems.setLayout(new FlowLayout(FlowLayout.CENTER, 0, Toolkit
				.getDefaultToolkit().getScreenSize().width / 200));
		menuItems.add(casual);
		menuItems.add(space);
		menuItems.add(classic);
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
