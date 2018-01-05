package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.xswingx.PromptSupport;

import edu.cmu.cs.cs214.hw4.core.Game;

/**
 * displays the screen to add players into the new game
 * 
 * @author Hizal
 *
 */
public class AddPlayers extends JPanel implements Window,MouseListener {
	private GameInfo game;
	private Font steelfish;
	private ArrayList<JLabel> icons = new ArrayList<JLabel>();
	private ArrayList<JTextField> textfields = new ArrayList<JTextField>();
	private JButton start;

	/**
	 * adds players to the new game
	 * 
	 * @param game
	 *            gameinfo object
	 */
	public AddPlayers(final GameInfo game) {
		this.game = game;
		steelfish = Util.getFont("steelfish");
		
		drawWindow(setupComponents());	
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		icons.get(icons.indexOf(e.getComponent())).setVisible(false);
		textfields.get(icons.indexOf(e.getComponent())).setVisible(true);
		textfields.get(icons.indexOf(e.getComponent())).requestFocus();
		textfields.get(icons.indexOf(e.getComponent())).setEditable(true);
		start.setForeground(new Color(127, 96, 0));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public JPanel setupComponents() {
		// game logo
				JLabel title = new JLabel("Add Players");
				title.setHorizontalAlignment(JLabel.CENTER);
				title.setFont(steelfish.deriveFont(60f));
				title.setForeground(new Color(127, 96, 0));

				// // continue quick button
				start = new JButton("Start Game");
				start.setBorderPainted(false);
				start.setFocusPainted(false);
				start.setContentAreaFilled(false);
				start.setForeground(new Color(171, 161, 119));
				start.setCursor(new Cursor(Cursor.HAND_CURSOR));
				start.setFont(steelfish.deriveFont(39f));
				start.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						boolean canContinue = false;
						for (int i = 0; i < 6; i++)
							if (textfields.get(i).getText().length() > 0) {
								game.getGame().addPlayer(textfields.get(i).getText());
								canContinue = true;
							}
						if (canContinue) {
							removeAll();
							revalidate();
							repaint();
							String name = game.getGame().getTurn().getName();
							String alert = name + "'s turn!";
							String message = name
									+ " has been randomly selected to go first! Pass the device over to "
									+ name + " and hit continue.";
							add(new NotificationScreen(game, alert, message, "game"));
						} else
							start.setForeground(new Color(171, 161, 119));
					}
				});

				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				JPanel top = new JPanel();
				top.setBackground(Color.WHITE);
				top.setLayout(new BorderLayout());
				top.add(title, BorderLayout.SOUTH);
				top.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
						.getScreenSize().width, Toolkit.getDefaultToolkit()
						.getScreenSize().height / 12));
				JPanel players = new JPanel();
				players.setBackground(Color.WHITE);
				players.setLayout(new GridLayout(0, 3));

				for (int i = 0; i < 6; i++) {
					JLabel icon = new JLabel();
					Image iconFull = new ImageIcon("src/main/resources/add.png")
							.getImage();
					Dimension scaled = Util
							.getScaledDimension(
									new Dimension(iconFull.getWidth(this), iconFull
											.getHeight(this)),
									new Dimension(
											Toolkit.getDefaultToolkit().getScreenSize().height / 12,
											Toolkit.getDefaultToolkit().getScreenSize().height / 12));
					icon.setIcon(new ImageIcon(Util.getScaledImage(iconFull,
							scaled.width, scaled.height)));
					icon.setHorizontalAlignment(JLabel.CENTER);
					icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
					icon.addMouseListener(this);
					icons.add(icon);
					JTextField tf = new JTextField(20);
					PromptSupport.setPrompt("Player Name", tf);
					tf.setHorizontalAlignment(JTextField.CENTER);
					tf.setVisible(false);
					tf.setFont(steelfish.deriveFont(60f));
					tf.setBorder(null);
					tf.setForeground(new Color(127, 96, 0));
					textfields.add(tf);
					JPanel main = new JPanel();
					main.setBackground(Color.WHITE);
					main.setLayout(new BorderLayout());
					JPanel mainTop = new JPanel();
					mainTop.setBackground(Color.WHITE);
					mainTop.setLayout(new BorderLayout());
					mainTop.add(icon, BorderLayout.SOUTH);
					JPanel mainBottom = new JPanel();
					mainBottom.setBackground(Color.WHITE);
					mainBottom.setLayout(new BorderLayout());
					mainBottom.setPreferredSize(new Dimension(Toolkit
							.getDefaultToolkit().getScreenSize().width / 3, Toolkit
							.getDefaultToolkit().getScreenSize().height / 5));
					mainBottom.add(tf, BorderLayout.NORTH);
					main.add(mainTop, BorderLayout.CENTER);
					main.add(mainBottom, BorderLayout.SOUTH);
					players.add(main);
				}

				JPanel bottom = new JPanel();
				bottom.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
						.getScreenSize().width, Toolkit.getDefaultToolkit()
						.getScreenSize().height / 12));
				bottom.setBackground(Color.WHITE);
				bottom.setLayout(new BorderLayout());
				bottom.add(start, BorderLayout.NORTH);
				panel.setLayout(new BorderLayout());
				panel.add(top, BorderLayout.NORTH);
				panel.add(players, BorderLayout.CENTER);
				panel.add(bottom, BorderLayout.SOUTH);
				
				return panel;
	}

	@Override
	public void drawWindow(JPanel panel) {
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		setVisible(true);
	}
}
