package ImgMan;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
	private float brightness;
	private float saturation;
	private float hue;
	
	private int contrast;
	private BufferedImage image;
	BufferedImage newImage;
	int[] originalPixels;
	int[] enhancedImagePixels;

	public IMModel()
	{
		imageFileList = new ArrayList<File>();
	}
	
	
	//getters for HSB values - contrast
	public void setBrightness(float b){
		brightness = b;
	}
	
	public void setHue(float b){
		hue = b;
	}
	
	public void setSaturation(float b){
		saturation = b;
	}
	
	public void setContrast(int c){
		contrast = c;
	}
	
	public float[] getHSB(){
		 float[] hsb = {hue, saturation, brightness};
		 return hsb;
	}

	
	public void setNewSourceFolder(String folder, FileNameExtensionFilter  filter){
		sourceFolder = folder;
		imageFileList.clear();
		for(File file : new File(sourceFolder).listFiles(new ImageFileFilter())){
			imageFileList.add(file);
		}
		
		try {
			image = ImageIO.read(imageFileList.get(1).getAbsoluteFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int height = image.getHeight();
		int width = image.getWidth();
		newImage = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
		
		originalPixels = image.getRGB(0,0, width, height, null, 0, width);
		enhancedImagePixels = new int[width*height];
		setChanged();
		notifyObservers(sourceFolder);
	}
	
	public ArrayList<File> getImageList(){
		
		return imageFileList;
	}
	
	public String getSourceFolder(){
		return sourceFolder;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public BufferedImage newgetImage(){
		return newImage;
	}
	
	public String getFirstImage(){
	 return	imageFileList.get(0).getAbsolutePath();
	}
	
	public BufferedImage brightenUsingRescale(float scale){
		RescaleOp rs = new RescaleOp(scale, 0, null);
		return rs.filter(image, newImage);
	}
	

	
	public BufferedImage getEnhancedImagesHSB(){
		
		int height = image.getHeight();
		int width = image.getWidth();

		float[] hsb = new float[]{0,0,0};
		
		for (int i = 0; i < originalPixels.length; i++)
		{
			Color c = new Color( originalPixels[i]);
			int red =c.getRed();
			int green = c.getGreen();
			int blue = c.getBlue();

			hsb = Color.RGBtoHSB(red, green, blue, hsb);
			
			 hsb[0] = (float)(hsb[0] +( hue/360.0));//hue
			
			hsb[1] *=  (saturation/100);
			  if(hsb[1] > 1.0){
				  hsb[1] = (float)1.0;
			  }
			
			hsb[2] *=  (brightness/100);
			  if(hsb[2] > 1.0) 
				  {hsb[2] = (float)1.0;}
	
			enhancedImagePixels[i] = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);

		}
		
		newImage.setRGB(0, 0, width, height, enhancedImagePixels, 0, width);

		return newImage;
	}
	
	
	//with out HSB - old algorithm for brightness and contrast
	public BufferedImage getEnhancedImages(){
		
		int height = image.getHeight();
		int width = image.getWidth();
		int[] contrast_lookup = new int[256];
		double newValue = 0;
		double c =( (255.0 + contrast) / 255.0)  ;
		
		c *= c;

		for (int i = 0; i < 256; i++)
		{
		    newValue = (double)i;
		    newValue /= 255.0;
		    newValue -= 0.5;
		    newValue *= c;
		    newValue += 0.5;
		    newValue *= 255;
		    newValue +=brightness;

		    if (newValue < 0)
		        newValue = 0;
		    if (newValue > 255)
		        newValue = 255;
		    contrast_lookup[i] = (int)newValue;
		}
			
		for (int i = 0; i < originalPixels.length; i++)
		{
			int cc = originalPixels[i];
			int red = (cc >> 16) & 0xFF;
			int green = (cc >> 8) & 0xFF;
			int blue = cc & 0xFF;
			
			int tred = contrast_lookup[red];
			int tgreen = contrast_lookup[green];
			int tblue = contrast_lookup[blue];
			Color newcol = new Color(tred, tgreen, tblue);
			enhancedImagePixels[i] = newcol.getRGB();

		}
		
		newImage.setRGB(0, 0, width, height, enhancedImagePixels, 0, width);
		
		
		return newImage;
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
