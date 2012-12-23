package ImgMan;

import java.awt.image.BufferedImage;
import java.awt.Color;


public  class BrightnessFilter {

	private BrightnessFilter(){
		
	}
	
	public static BufferedImage BrightenImage(BufferedImage image, int amount){
		
		int height = image.getHeight();
		int width = image.getWidth();
		int pixels = height *  width;
		
		int[] originalPixels = image.getRGB(0,0, width, height, null, 0, width);
		int[] newImagePixels = new int [originalPixels.length];
		
		BufferedImage newImage = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
		
		for(int i = 0; i < originalPixels.length; i ++)
		{
			Color pixelColor = new Color(originalPixels[i]);
		
			int factor = amount;
//			int color = pixelColor.getRGB() + factor > 255? 255 : pixelColor.getRGB() + factor;
			
            int red = 0;
            if (pixelColor.getRed() + factor > 255) red = 255;
            else if (pixelColor.getRed()   +factor < 0) red = 0;
            else red = pixelColor.getRed()  + factor;

            int green = 0;
            if (pixelColor.getGreen()  + factor > 255) green = 255;
            else if (pixelColor.getGreen()  + factor < 0) green = 0;
            else green = pixelColor.getGreen() + factor;

            int blue = 0;
            if (pixelColor.getBlue()  + factor > 255) blue = 255;
            else if (pixelColor.getBlue()  + factor < 0) blue = 0;
            else blue = pixelColor.getBlue() + factor;

            Color newPixelColor = new Color(red, green, blue);
//            Color newPixelColor = new Color(color);
            newImagePixels[i] = newPixelColor.getRGB();
			
		}
//	       for (int i = 0; i<pixels.length; i++){
//	            Color pixelColor = new Color(pixels[i]);
//

//	        }

		newImage.setRGB(0, 0, width, height, newImagePixels, 0, width);
		
		
		return newImage;
	}


}
