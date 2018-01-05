package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * window to buy special tiles for the current player
 * 
 * @author Hizal
 *
 */
public class BuySpecials extends JPanel implements Window,MouseListener {
	private GameInfo game;
	private Font steelfish;
	private Font steelfishIt;
	private ArrayList<JLabel> icons = new ArrayList<JLabel>();
	private JButton start;
	private JLabel score;

	/**
	 * draws the window and activates action listeners
	 * 
	 * @param game gameinfo object
	 */
	public BuySpecials(final GameInfo game) {
		this.game = game;
		steelfish = Util.getFont("steelfish");
		steelfishIt = steelfish.deriveFont(Font.ITALIC);

		drawWindow(setupComponents());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int index = icons.indexOf(e.getComponent());
		game.getGame().buySpecial(game.getGame().getSpecialsList().get(index));
		Thread scoreUpdate = new Thread(new Runnable() {
			public void run() {
				String currentPlayer = game.getGame().getTurn().getName();
				int oldScore = Integer.parseInt(score.getText().replaceAll("[^\\d]", ""));
				int newScore = game.getGame().getTurn().getScore();
				int changeFactor = 0;
				if(oldScore<newScore)
					changeFactor++;		
				else if(oldScore>newScore)
					changeFactor--;
				else
					return;
				while(currentPlayer.equals(game.getGame().getTurn().getName()) && oldScore!=newScore) {
					oldScore+=changeFactor;
					score.setText("  " + oldScore + "   ");
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
				JLabel title = new JLabel("Buy Special Tiles");
				title.setHorizontalAlignment(JLabel.CENTER);
				title.setFont(steelfish.deriveFont(60f));
				title.setForeground(new Color(51, 63, 80));

				// // continue quick button
				start = new JButton("Back");
				start.setBorderPainted(false);
				start.setFocusPainted(false);
				start.setContentAreaFilled(false);
				start.setForeground(new Color(51, 63, 80));
				start.setCursor(new Cursor(Cursor.HAND_CURSOR));
				start.setFont(steelfish.deriveFont(39f));
				start.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						removeAll();
						revalidate();
						repaint();
						add(new GameWindow(game));
					}
				});

				JPanel top = new JPanel();
				top.setOpaque(false);
				top.setLayout(new BorderLayout());
				top.add(title, BorderLayout.SOUTH);
				top.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
						.getScreenSize().width, Toolkit.getDefaultToolkit()
						.getScreenSize().height / 12));

				score = new JLabel(game.getGame().getTurn().getScore() + "");
				score.setFont(steelfish.deriveFont(220f));
				score.setHorizontalAlignment(JLabel.CENTER);
				score.setVerticalAlignment(JLabel.CENTER);
				score.setForeground(new Color(51, 63, 80));

				JLabel scoreInfo = new JLabel("POINTS");
				scoreInfo.setFont(steelfish.deriveFont(30f));
				scoreInfo.setHorizontalAlignment(JLabel.CENTER);
				scoreInfo.setVerticalAlignment(JLabel.CENTER);
				scoreInfo.setForeground(new Color(51, 63, 80));

				JPanel scoreWrapper = new JPanel();
				scoreWrapper.setOpaque(false);
				scoreWrapper.setLayout(new BorderLayout());
				scoreWrapper.add(score, BorderLayout.CENTER);

				JPanel scoreInfoWrapper = new JPanel();
				scoreInfoWrapper.setOpaque(false);
				scoreInfoWrapper.setLayout(new BorderLayout());
				scoreInfoWrapper.add(scoreInfo, BorderLayout.CENTER);

				JPanel scoreWindow = new JPanel();
				scoreWindow.setLayout(new BoxLayout(scoreWindow, BoxLayout.Y_AXIS));
				scoreWindow.setOpaque(false);
				scoreWindow.setAlignmentX(Component.CENTER_ALIGNMENT);
				scoreWindow.setAlignmentY(Component.CENTER_ALIGNMENT);
				scoreWindow.add(scoreWrapper);
				scoreWindow.add(scoreInfoWrapper);

				JPanel scoreWindowWrapper = new JPanel();
				scoreWindowWrapper.setLayout(new BorderLayout());
				// scoreWindowWrapper.add(scoreWindow, BorderLayout.CENTER);

				JPanel specialsWindow = new JPanel();
				specialsWindow.setOpaque(false);
				specialsWindow
						.setLayout(new BoxLayout(specialsWindow, BoxLayout.Y_AXIS));
				for (int i = 0; i < game.getGame().getSpecialsList().size(); i++) {
					JLabel icon = new JLabel();
					Image iconFull = new ImageIcon(game.getGame().getSpecialsList()
							.get(i).getIcon()).getImage();
					Dimension scaled = Util
							.getScaledDimension(
									new Dimension(iconFull.getWidth(this), iconFull
											.getHeight(this)),
									new Dimension(
											Toolkit.getDefaultToolkit().getScreenSize().height / 10,
											Toolkit.getDefaultToolkit().getScreenSize().height / 10));
					icon.setIcon(new ImageIcon(Util.getScaledImage(iconFull,
							scaled.width, scaled.height)));
					icon.setHorizontalAlignment(JLabel.CENTER);
					icon.setCursor(new Cursor(Cursor.HAND_CURSOR));
					icon.addMouseListener(this);
					JLabel name = new JLabel(game.getGame().getSpecialsList().get(i)
							.getName());
					name.setFont(steelfish.deriveFont(45f));
					name.setForeground(new Color(51, 63, 80));
					JLabel description = new JLabel(game.getGame().getSpecialsList()
							.get(i).getDescription());
					description.setFont(steelfishIt.deriveFont(20f));
					description.setForeground(new Color(51, 63, 80));
					JLabel price = new JLabel(game.getGame().getSpecialsList().get(i)
							.getPrice()
							+ " pts");
					price.setFont(steelfish.deriveFont(20f));
					price.setForeground(new Color(51, 63, 80));
					icons.add(icon);

					JPanel text = new JPanel();
					text.setOpaque(false);
					text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
					text.setAlignmentY(Component.LEFT_ALIGNMENT);
					text.setAlignmentX(Component.LEFT_ALIGNMENT);
					text.add(name);
					text.add(price);
					text.add(description);

					JPanel special = new JPanel();
					special.setOpaque(false);
					special.setLayout(new FlowLayout(FlowLayout.LEFT, Toolkit
							.getDefaultToolkit().getScreenSize().width / 40, 0));
					special.add(icon, BorderLayout.WEST);
					special.add(text, BorderLayout.EAST);

					JPanel space = new JPanel();
					space.setOpaque(false);
					space.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
							.getScreenSize().width / 50, Toolkit.getDefaultToolkit()
							.getScreenSize().width / 50));
					specialsWindow.add(special);
					specialsWindow.add(space);
				}

				JPanel space = new JPanel();
				space.setOpaque(false);
				space.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
						.getScreenSize().width / 10, (Toolkit.getDefaultToolkit()
						.getScreenSize().height / 2)
						+ Toolkit.getDefaultToolkit().getScreenSize().height / 3));

				JPanel window = new JPanel();
				window.setOpaque(false);
				window.add(scoreWindow);
				window.add(space);
				window.add(specialsWindow);

				JPanel bottom = new JPanel();
				bottom.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
						.getScreenSize().width, Toolkit.getDefaultToolkit()
						.getScreenSize().height / 12));
				bottom.setOpaque(false);
				bottom.setLayout(new BorderLayout());
				bottom.add(start, BorderLayout.NORTH);

				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				panel.setLayout(new BorderLayout());
				panel.add(top, BorderLayout.NORTH);
				panel.add(window, BorderLayout.CENTER);
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
