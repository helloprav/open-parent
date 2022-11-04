package org.openframework.commons.utils;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ASCIIArtService {

	public static void main(String[] args) throws IOException {

		//generate banner text for Neer App
		ASCIIArtService.generateBanner("Neer");
	}

	/**
	 * This method generates the banner and saves in image format with the given fileName. This also prints the banner (text) 
	 * on the console from where it can be copied and saved in a text file to be used in the SpringBoot application.
	 * 
	 * @param bannerStr
	 * @param bannerSize
	 *
	 * @throws IOException
	 */
	public static void generateBanner(String bannerStr) throws IOException {

		String fontName = "SansSerif";
		int fontStyle = Font.BOLD;
		int fontSize = 24;
		int height = 30;
		int width = bannerStr.length()*15;

		// BufferedImage image = ImageIO.read(new File("/Users/mkyong/Desktop/logo.jpg"));
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setFont(new Font(fontName, fontStyle, fontSize));

		// Set Co-Ordinates of Text in the banner image
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.drawString(bannerStr, 1, 20);
		//graphics.drawString(bannerStr, 10, 20);

		// save this image
		//ImageIO.write(image, "png", new File(fileName));

		for (int y = 0; y < height; y++) {
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < width; x++) {

				sb.append(image.getRGB(x, y) == -16777216 ? " " : "$");

			}

			if (sb.toString().trim().isEmpty()) {
				continue;
			}

			System.out.println(sb);
		}

	}

}
