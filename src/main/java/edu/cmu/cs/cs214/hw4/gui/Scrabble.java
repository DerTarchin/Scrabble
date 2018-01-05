package edu.cmu.cs.cs214.hw4.gui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * MAIN CLASS starts the game GUI using Java Swing in fullscreen
 * 
 * @author Hizal
 *
 */
public class Scrabble {
	/**
	 * main method to start GUI and JFrame
	 * 
	 * @param args
	 *            main args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GraphicsEnvironment env = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				GraphicsDevice device = env.getDefaultScreenDevice();
				JFrame startWindow = new JFrame("Scrabble with Stuff");
				startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				startWindow.setUndecorated(true);
				startWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
				startWindow.add(new SplashScreen(startWindow));
				startWindow.pack();
				startWindow.setResizable(false);
				startWindow.validate();

				device.setFullScreenWindow(startWindow);
				startWindow.setVisible(false); // fixes keyboard input issues on
												// some macs
				startWindow.setVisible(true);

				startWindow.setVisible(true);
			}
		});
	}
}
