package ImgMan;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.RescaleOp;
import java.awt.image.renderable.ParameterBlock;
import java.io.*;
import java.util.ArrayList;
import java.util.Observable;
import javax.media.jai.*;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class IMModel extends Observable {

	private String sourceFolder;
	private ArrayList<File> imageFileList;
	
	EnhancedImage image;

	public IMModel()
	{
		imageFileList = new ArrayList<File>();
	}
	
	public EnhancedImage getEnhancedImage(){
		return image;
	}

	public void setNewSourceFolder(String folder, FileNameExtensionFilter  filter){
		sourceFolder = folder;
		imageFileList.clear();
		for(File file : new File(sourceFolder).listFiles(new ImageFileFilter())){
			imageFileList.add(file);
		}
		BufferedImage in;
		try {
			in = ImageIO.read(imageFileList.get(0).getAbsoluteFile());
			BufferedImage imagen = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = imagen.createGraphics();
			g.drawImage(in, 0, 0, null);
			g.dispose();
			
			image = new EnhancedImage(imagen);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		setChanged();
		notifyObservers();
	}
	
	public ArrayList<File> getImageList(){
		
		return imageFileList;
	}
	
	public String getSourceFolder(){
		return sourceFolder;
	}
	


}

//public static PlanarImage setColor(PlanarImage source,
//int red, int green, int blue,
//float contrast,
//float brightness)
//{
//// Time the process for performance
//long beginTime = (new java.util.Date()).getTime();
//
//// Setup the parameter block for the source image and
//// the three parameters for the mean operation
//ParameterBlock mpb = new ParameterBlock();
//mpb.addSource(source); // The source image
//mpb.add(null); // null ROI means whole image
//mpb.add(1); // check every pixel horizontally
//mpb.add(1); // check every pixel vertically
//// Perform the mean operation on the source image
//PlanarImage meanImage = JAI.create("mean", mpb, null);
//// Retrieve the mean pixel value
//double[] mean = (double[])meanImage.getProperty("mean");
//// Average the mean of all bands
//double sum = 0.0D;
//for (int i=0; i < mean.length; i++)
//{
//sum += mean[i];
//}
//int average = (int) sum / mean.length;
//
//// Create the lookup table based on the average mean
//byte[][] lut = new byte[3][256];
//for (int i = 0; i < 256; i++ )
//{
//lut[0][i] =  clamp((average, (int)((i - average) * contrast)) , red , (int)brightness);
//lut[1][i] = clamp((average + (int)((i - average) * contrast)) + green + (int)brightness);
//lut[2][i] = clamp((average + (int)((i - average) * contrast)) + blue + (int)brightness);
//}
//LookupTableJAI lookup = new LookupTableJAI(lut);
//// Setup the parameter block for the lookup operation
//ParameterBlock pb = new ParameterBlock();
//pb.addSource(source);
//pb.add(lookup);
//
//// Calculate processing time and report
//long endTime = (new java.util.Date()).getTime();
//System.out.println("Time to set colors was " + (endTime - beginTime) + " ms");
//
//// Return the resulting image
//return JAI.create("lookup", pb, null);
//}
