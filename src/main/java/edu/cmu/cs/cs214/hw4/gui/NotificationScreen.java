package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * a screen that displays a notification, alert or message called by another
 * class.
 * 
 * @author Hizal
 *
 */
public class NotificationScreen extends JPanel implements Window {
	private GameInfo game;
	private Font steelfish;
	private String message = null;
	private ArrayList<String> messages = null;
	private String title;
	private String destination;

	/**
	 * initializes a notification screen for an alert with one message
	 * 
	 * @param game
	 *            gameInfo object
	 * @param title
	 *            name of alert
	 * @param message
	 *            description of alert
	 * @param destination
	 *            return destination
	 */
	public NotificationScreen(final GameInfo game, String title,
			String message, String destination) {
		this.game = game;
		steelfish = Util.getFont("steelfish");
		this.title = title;
		this.destination = destination;
		this.message = message;

		drawWindow(setupComponents());
	}

	/**
	 * initializes a notification screen for an alert with multiple messages
	 * 
	 * @param game
	 *            gameInfo object
	 * @param title
	 *            name of alert
	 * @param messages
	 *            multiple messages to display
	 * @param destination
	 *            return destination
	 */
	public NotificationScreen(final GameInfo game, String title,
			ArrayList<String> messages, String destination) {
		this.game = game;
		steelfish = Util.getFont("steelfish");
		this.title = title;
		this.destination = destination;
		this.messages = messages;

		drawWindow(setupComponents());
	}

	private JPanel messageGroup() {
		JPanel messageGroup;
		if (messages != null) {
			messageGroup = new JPanel();
			messageGroup.setOpaque(false);
			messageGroup
					.setLayout(new BoxLayout(messageGroup, BoxLayout.Y_AXIS));

			for (int i = 0; i < messages.size(); i++) {
				JLabel messageLabel = new JLabel(messages.get(i));
				messageLabel.setForeground(new Color(127, 96, 0));
				messageLabel.setFont(steelfish.deriveFont(30f));
				messageLabel.setHorizontalAlignment(JLabel.CENTER);
				messageLabel.setVerticalAlignment(JLabel.TOP);
				messageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				messageGroup.add(messageLabel);
			}
		} else {
			JLabel messageLabel = new JLabel(message);
			messageLabel.setForeground(new Color(127, 96, 0));
			messageLabel.setFont(steelfish.deriveFont(30f));
			messageLabel.setHorizontalAlignment(JLabel.CENTER);
			messageLabel.setVerticalAlignment(JLabel.TOP);
			messageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

			messageGroup = new JPanel();
			messageGroup.setOpaque(false);
			messageGroup
					.setLayout(new BoxLayout(messageGroup, BoxLayout.Y_AXIS));
			messageGroup.add(messageLabel);
		}

		return messageGroup;
	}

	/**
	 * draws the notificatoin screen
	 * 
	 * @param title
	 *            name of alert
	 * @param messageGroup
	 *            panel of messages
	 * @param destination
	 *            return destination
	 */
	private JPanel createNotification(String title, JPanel messageGroup,
			String destination) {

		JLabel alert = new JLabel(title);
		alert.setForeground(new Color(127, 96, 0));
		alert.setFont(steelfish.deriveFont(160f));
		alert.setHorizontalAlignment(JLabel.CENTER);
		alert.setVerticalAlignment(JLabel.BOTTOM);

		// game quick button
		JButton cont = new JButton("Continue");
		cont.setBorderPainted(false);
		cont.setFocusPainted(false);
		cont.setContentAreaFilled(false);
		cont.setForeground(new Color(127, 96, 0));
		cont.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cont.setFont(steelfish.deriveFont(39f));
		if (destination.equals("game"))
			cont.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removeAll();
					revalidate();
					repaint();
					add(new GameWindow(game));
				}
			});
		else if (destination.equals("menu"))
			cont.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removeAll();
					revalidate();
					repaint();
					add(new SplashScreen(game.getFrame()));
				}
			});
		JPanel messageWrapper = new JPanel();
		JLabel blankSpace = new JLabel(" ");
		blankSpace.setFont(steelfish.deriveFont(20f));
		messageWrapper.setBackground(Color.WHITE);
		messageWrapper.setPreferredSize(new Dimension(Toolkit
				.getDefaultToolkit().getScreenSize().width, Toolkit
				.getDefaultToolkit().getScreenSize().height / 3));
		messageWrapper.setLayout(new BorderLayout());
		messageWrapper.add(blankSpace, BorderLayout.NORTH);
		messageWrapper.add(messageGroup, BorderLayout.CENTER);

		JPanel textWrapper = new JPanel();
		textWrapper.setBackground(Color.WHITE);
		textWrapper.setLayout(new BorderLayout());
		textWrapper.add(alert, BorderLayout.CENTER);
		textWrapper.add(messageWrapper, BorderLayout.SOUTH);
		textWrapper.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize().width, Toolkit.getDefaultToolkit()
				.getScreenSize().height / 2));

		JPanel buttonWrapper = new JPanel();
		buttonWrapper.setBackground(Color.WHITE);
		buttonWrapper.setPreferredSize(new Dimension(Toolkit
				.getDefaultToolkit().getScreenSize().width, Toolkit
				.getDefaultToolkit().getScreenSize().height / 12));
		buttonWrapper.setLayout(new BorderLayout());
		buttonWrapper.add(cont, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.add(textWrapper, BorderLayout.CENTER);
		panel.add(buttonWrapper, BorderLayout.SOUTH);

		return panel;
	}

	@Override
	public JPanel setupComponents() {
		return createNotification(title, messageGroup(), destination);
	}

	@Override
	public void drawWindow(JPanel panel) {
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		setVisible(true);
	}
}
