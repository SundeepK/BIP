package ImgMan;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.RGBImageFilter;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;

public class EnhancedImage {

	private BufferedImage image;
	private float brightness;
	private float saturation;
	private float hue;
	
	private int contrast;
	BufferedImage newImage;
	int[] originalPixels;
	int[] enhancedImagePixels;

	
	public EnhancedImage(BufferedImage image){
		this.image = image;
	
		int height = image.getHeight();
		int width = image.getWidth();
	
		newImage = new BufferedImage(width, height,  BufferedImage.TYPE_INT_ARGB  );		
	}
	
	public BufferedImage getOriginalImage(){
		return image;
	}
	
	public BufferedImage newgetImage(){
		return newImage;
	}
		
	public BufferedImage brightenUsingRescale(float scale){
		RescaleOp rs = new RescaleOp(scale, 0, null);
		return rs.filter(image, newImage);
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
	
	public static BufferedImage getEnhancedImagesRaster(BufferedImage im, float[] hsbOrg){
		WritableRaster raster = im.getRaster();
		int width = im.getWidth();
	    int height = im.getHeight();
	    float[] hsb = new float[]{0,0,0,0};
	    
		//iterate through every pixel in image
	    int lPixel,red,green,blue;
	    for(int h=0;h<height;h++)
	    	for(int w=0;w<width;w++)
	        {
	    	//get rgb value
	        lPixel = im.getRGB(w,h);
	        
	        //seperate rgb to seperate channels
	        red  = (int)((lPixel&0x00FF0000)>>>16); // Red level
	        green = (int)((lPixel&0x0000FF00)>>>8);  // Green level
	        blue  = (int) (lPixel&0x000000FF);       // Blue level
	        
	        //get HSB value from channels
			hsb = Color.RGBtoHSB(red, green, blue, hsb);
			
			//calculate new HSB values by adding in adjusted HSB values
			hsb[0] = (float)(hsb[0] +( hsbOrg[0]/360.0));//hue
			
			hsb[1] *=  (hsbOrg[1]/100);
			  if(hsb[1] > 1.0)
				  hsb[1] = (float)1f;
			
			hsb[2] *=  (hsbOrg[2]/100);
			  if(hsb[2] > 1.0) 
				  hsb[2] = (float)1f;

			int newargb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
			
	        if (((lPixel>>24)&0xFF) >0) 
	          {
	          red   = (int)((newargb&0x00FF0000)>>>16); // Red level
	          green = (int)((newargb&0x0000FF00)>>>8);  // Green level
	          blue  = (int) (newargb&0x000000FF);       // Blue level
	          // Set the pixel on the output image's raster.
	          raster.setPixel(w,h,new int[]{red,green,blue,((lPixel>>24)&0xFF)});        
	          }
	        } 
	    
	    return im;
	}
	
	
	public BufferedImage getEnhancedImagesHSB(){
		WritableRaster raster = image.getRaster();
		WritableRaster newRaster = newImage.getRaster();
		
		int width = image.getWidth();
	    int height = image.getHeight();
	    float[] hsb = new float[]{0,0,0,0};
	    
		//iterate through every pixel in image
	    int lPixel,red,green,blue;
	    for(int h=0;h<height;h++)
	    	for(int w=0;w<width;w++)
	        {
	    	//get rgb value
	        lPixel = image.getRGB(w,h);
	        
	        //seperate rgb to different channels
	        red  = (int)((lPixel&0x00FF0000)>>>16); // Red level
	        green = (int)((lPixel&0x0000FF00)>>>8);  // Green level
	        blue  = (int) (lPixel&0x000000FF);       // Blue level
	        
	        //get HSB value from channels
			hsb = Color.RGBtoHSB(red, green, blue, hsb);
			
			//calculate new HSB values by adding in adjusted HSB values
			hsb[0] = (float)(hsb[0] +( hue/360.0));//hue
			
			hsb[1] *=  (saturation/100);
			  if(hsb[1] > 1.0)
				  hsb[1] = (float)1f;
			
			hsb[2] *=  (brightness/100);
			  if(hsb[2] > 1.0) 
				  hsb[2] = (float)1f;

			int newargb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
			
	        if (((lPixel>>24)&0xFF) >0) 
	          {
	          red   = (int)((newargb&0x00FF0000)>>>16); // Red level
	          green = (int)((newargb&0x0000FF00)>>>8);  // Green level
	          blue  = (int) (newargb&0x000000FF);       // Blue level
	          // Set the pixel on the output image's raster.
	          newRaster.setPixel(w,h,new int[]{red,green,blue,((lPixel>>24)&0xFF)});        
	          }
	        } 
	    
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
			
//			cc |= (cc >> 16) & 0xFF;
//			cc |= (cc >> 8) & 0xFF;
//			cc |=  cc & 0xFF;
//			cc |=  (cc >> 24) & 0xFF;
			
//			cc = (cc & ~(0xFF << 24)) ;
//
//			cc = (cc & ~(0xFF << 16)) | contrast_lookup[red];
//
//			cc = (cc & ~(0xFF << 8)) | contrast_lookup[green];
//
//			cc = (cc & ~0xFF) | contrast_lookup[blue];
			
			int tred = contrast_lookup[red];
			int tgreen = contrast_lookup[green];
			int tblue = contrast_lookup[blue];
			Color newcol = new Color(tred, tgreen, tblue);
			enhancedImagePixels[i] = newcol.getRGB();
//			enhancedImagePixels[i] = cc;

		}
		
		newImage.setRGB(0, 0, width, height, enhancedImagePixels, 0, width);
		
		
		return newImage;
	}
	
}
