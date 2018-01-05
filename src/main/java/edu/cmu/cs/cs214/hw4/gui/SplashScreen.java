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
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.cmu.cs.cs214.hw4.core.Game;

/**
 * game splash screen. displays logo, quit and start options
 * 
 * @author Hizal
 *
 */
public class SplashScreen extends JPanel implements Window {
	private Font steelfish;
	private final GameInfo game;

	/**
	 * initializes the gameInfo object
	 * 
	 * @param frame
	 *            JFrame to dispaly on
	 */
	public SplashScreen(JFrame frame) {
		this.game = new GameInfo(frame);
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

		// game play button
		JLabel start = new JLabel("Play");
		start.setForeground(new Color(127, 96, 0));
		start.setCursor(new Cursor(Cursor.HAND_CURSOR));
		start.setFont(steelfish.deriveFont(40f));
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				removeAll();
				revalidate();
				repaint();
				add(new SetupGame(game));
			}
		});

		// game quit button
		JLabel quit = new JLabel("Quit");
		quit.setForeground(new Color(127, 96, 0));
		quit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		quit.setFont(steelfish.deriveFont(40f));
		quit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});

		JPanel space = new JPanel();
		space.setOpaque(false);
		space.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width / 50, 1));

		JPanel menuItems = new JPanel();
		menuItems.setLayout(new FlowLayout(FlowLayout.CENTER, 0, Toolkit
				.getDefaultToolkit().getScreenSize().width / 200));
		menuItems.add(start);
		menuItems.add(space);
		menuItems.add(quit);
		menuItems.setOpaque(false);

		JPanel menu = new JPanel();
		menu.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width, Toolkit.getDefaultToolkit()
				.getScreenSize().height / 3));
		menu.setOpaque(false);
		menu.setLayout(new BorderLayout());
		menu.add(menuItems, BorderLayout.NORTH);

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
