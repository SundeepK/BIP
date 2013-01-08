package ImgMan;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class ImageResizer {
	int resizedWidth,  resizedheight;

	
	public ImageResizer(int width, int height){
		this.resizedWidth = width;
		this.resizedheight = height;
	}
	
	public void setWidthHeight(int w, int h){
		resizedheight = h ;
		resizedWidth = w;
	}
	
	/**
	 * Resizes a specified Buffered image using the hintkey and hint value to produce results of varying quality
	 * 
	 * @param  src
	 * @param hintKey
	 * @param hintValue
	 * @return
	 */
	public BufferedImage scaleImage(BufferedImage src,Key hintKey, Object hintValue) {
		Graphics2D resizedImage;
		BufferedImage result = createOptimalImage(src, resizedWidth,resizedheight);
		resizedImage = result.createGraphics();

		// Scale the image to the new buffer using the specified rendering hint.
		resizedImage.setRenderingHint(hintKey,hintValue);
		
		resizedImage.drawImage(src, 0, 0, resizedWidth, resizedheight, null);
		resizedImage.dispose();
		return result;
	}
	
	private BufferedImage createOptimalImage(BufferedImage src,
			int width, int height) throws IllegalArgumentException {
		if (width < 0 || height < 0)
			throw new IllegalArgumentException("Width and height must be 0 or greater");

		return new BufferedImage(width,height,(src.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB
						: BufferedImage.TYPE_INT_ARGB));
	}
	
	/**
	 * Make sure to dispose of Graphics2D resizedImage to free up resources
	 */
//	public void disposeGraphics(){
//		resizedImage.dispose();
//	}

}
