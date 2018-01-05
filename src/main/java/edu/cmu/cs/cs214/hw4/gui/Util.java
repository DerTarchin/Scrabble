package edu.cmu.cs.cs214.hw4.gui;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Scrabble GUI Util class is a group of useful methods used by the classes that
 * make up the GUI
 * 
 * @author Hizal
 *
 */
public class Util {

	/**
	 * fixes PNG transparency issues if present
	 * 
	 * @param image
	 *            image to check
	 * @param label
	 *            jlabel it belongs to
	 * @param opacity
	 *            opacity to change it to
	 * @return fixed ImageIcon
	 */
	public static ImageIcon fixImage(ImageIcon image, JLabel label,
			float opacity) {
		BufferedImage buffered = new BufferedImage(image.getIconWidth(),
				image.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = buffered.createGraphics();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				opacity));
		g2.drawImage(image.getImage(), 0, 0, image.getIconWidth(),
				image.getIconHeight(), label);
		g2.dispose();

		return new ImageIcon(buffered);
	}

	/**
	 * gets the font by the filename given
	 * 
	 * @param name
	 *            filepath of the font
	 * @return Font object
	 */
	public static Font getFont(String name) {
		if (name.toLowerCase().contains("steelfish")) {
			Font font;
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, new File(
						"src/main/resources/steelfish.ttf"));
				GraphicsEnvironment genv = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				genv.registerFont(font);
				return font;
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * author: http://stackoverflow.com/questions/6714045/how-to-resize-jlabel-
	 * imageicon
	 * 
	 * scales images and returns an Image object
	 * 
	 * @param srcImg
	 *            source Image to scale
	 * @param w
	 *            width
	 * @param h
	 *            height
	 * @return new Image object
	 */
	public static Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

	/**
	 * author:
	 * http://stackoverflow.com/questions/10245220/java-image-resize-maintain
	 * -aspect-ratio
	 * 
	 * gets a scaled dimention that fits within the given boundary, fitting into
	 * the smallest side
	 * 
	 * @param imgSize
	 *            size of original image to scale
	 * @param boundary
	 *            boundary to fit inside
	 * @return scaled dimension boundaries
	 */
	public static Dimension getScaledDimension(Dimension imgSize,
			Dimension boundary) {

		int originalWidth = imgSize.width;
		int originalHeight = imgSize.height;
		int boundWidth = boundary.width;
		int boundHeight = boundary.height;
		int newWidth = originalWidth;
		int newHeight = originalHeight;

		// first check if we need to scale width
		if (originalWidth > boundWidth) {
			// scale width to fit
			newWidth = boundWidth;
			// scale height to maintain aspect ratio
			newHeight = (newWidth * originalHeight) / originalWidth;
		}

		// then check if we need to scale even with the new height
		if (newHeight > boundHeight) {
			// scale height to fit instead
			newHeight = boundHeight;
			// scale width to maintain aspect ratio
			newWidth = (newHeight * originalWidth) / originalHeight;
		}
		return new Dimension(newWidth, newHeight);
	}

	/**
	 * taken from: http://pastebin.com/i6bBn3K7
	 * 
	 * thread sleep method for non-thread operations. Not used, I believe.
	 * 
	 * @param millis
	 *            milliseconds to sleep
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
