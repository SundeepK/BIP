package ImgMan;

import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Blur {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
	    // The color table, with 16 gray levels. 
	    int nColors = 16;
	    byte[] levels = new byte[nColors];
	    for(int c=0;c<16;c++)
	      levels[c] = (byte)(c*16);
	    int width = 400; // Dimensions of the image
	    int height = 400;
	    // Let's create the IndexColorModel for this image. The first argument for the constructor
	    // is the number of bits required for the color map. Since the image is a gray-level one, we 
	    // use the same color map arrays.
	    IndexColorModel colorModel = new IndexColorModel(4,nColors,levels,levels,levels);
	    // Let's create a BufferedImage for an indexed color image.
	    BufferedImage im = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_INDEXED,colorModel);
	    // We need its raster to set the pixels' values.
	    WritableRaster raster = im.getRaster();
	    // Put the pixels on the raster. We will make several small blocks with random gray levels.
	    for(int h=0;h<height/10;h++)
	        {
	        int[] fill = new int[100]; // A block of pixels...
	        Arrays.fill(fill,(int)(Math.random()*16));  // .. filled with one of the 16 colors.
	        raster.setSamples(10,h*10,10,10,0,fill); 
	        }
	    // Store the image. Use PNG format, it is more flexible for indexed images.
	    ImageIO.write(im,"PNG",new File("c:\\glpattern2.png"));
	}
	
	private static ColorModel createColorModel(int n) {

		 

		// Create a simple color model with all values mapping to

		// a single shade of gray

		// nb. this could be improved by reusing the byte arrays



		byte[] r = new byte[16];

		byte[] g = new byte[16];

		byte[] b = new byte[16];

		 
	
		for (int i = 0; i < r.length; i++) {

		r[i] = (byte) n;

		g[i] = (byte) n;
	
		b[i] = (byte) n;
		}

		return new IndexColorModel(4, 16, r, g, b);

		}

}
