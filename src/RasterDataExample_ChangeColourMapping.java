import java.awt.*;
import java.awt.geom.*; // Line2D etc.
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class RasterDataExample_ChangeColourMapping extends JPanel {


	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("C:\\Users\\Sundeep\\Pictures\\IMG_0560.JPG"));
		} catch (IOException e) {
		}

		BufferedImage filter_image = null;

		g2d.drawImage(image,0,0,this);

		// Construct a new Colour LooupTable
		LookupTable lookupTable = contrastDecLUT();
		LookupOp op = new LookupOp(lookupTable, null);
		// Replace with the new lookup table
		// There is a bug in Java Image Processing API
		// You cannot specify a different destination image!!!
		op.filter(image,image);

		g2d.drawImage(image, image.getWidth()+10,0, null);


	}

	// // Negative effect
	public static LookupTable negativeLUT() {
		short negative[] = new short[256];
		for (int i = 0; i < 256; i++) negative[i] = (short) (255 - i);
		ShortLookupTable table = new ShortLookupTable(0,negative);
		return table;
	}
	
	// Brighten image
	public static LookupTable brightenLUT() {
		short brighten[] = new short[256];
		for (int i = 0; i < 256; i++) {
			short pixelValue = (short) (i + 30);
			if (pixelValue > 255)
				pixelValue = 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;
		}
		LookupTable lookupTable = new ShortLookupTable(0, brighten);
		return lookupTable;
	}

	// Darken image
	public static LookupTable darkenLUT() {
		short brighten[] = new short[256];
		for (int i = 0; i < 256; i++) {
			short pixelValue = (short) (i - 10);
			if (pixelValue > 255)
				pixelValue = 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;
		}
		LookupTable lookupTable = new ShortLookupTable(0, brighten);
		return lookupTable;
	}

	// Increase contrast
	public static LookupTable contrastIncLUT() {
		short brighten[] = new short[256];
		for (int i = 0; i < 256; i++) {
			short pixelValue = (short) (i * 1.5);
			if (pixelValue > 255)
				pixelValue = 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;
		}
		LookupTable lookupTable = new ShortLookupTable(0, brighten);
		return lookupTable;
	}

	// Decrease contrast
	public static LookupTable contrastDecLUT() {
		short brighten[] = new short[256];
		for (int i = 0; i < 256; i++) {
			short pixelValue = (short) (i / 1.5);
			if (pixelValue > 255)
				pixelValue = 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;
		}
		LookupTable lookupTable = new ShortLookupTable(0, brighten);
		return lookupTable;
	}

	


	public static LookupTable hotmapping() {
		//Create the data for the lookup table.
		short[] red = new short[256];
		short[] green = new short[256];
		short[] blue = new short[256];

		for (int cnt = 0; cnt < 256; cnt++){
			red[cnt] = (short)(255);		  
			if (cnt > 128) {
				green[cnt] = (short)(255);		   
			} else {
				blue[cnt] = 0;
			}
		}//end for loop

		//Create the 2D array that will be used to create the
		// lookup table.
		short[][] lookupData = new short[][]{red,green,blue};

		//Create the lookup table
		LookupTable table = new ShortLookupTable(0,lookupData);
		return table;

	}


	public static void main(String args[]) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(30, 30, 800, 300);
		window.getContentPane().add(new RasterDataExample_ChangeColourMapping());
		window.setVisible(true);



	}
}