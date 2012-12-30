package ImgMan;

import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class GreyScaleFilter implements IFilterEffect{

	BufferedImage greyScaleImage;
	
	public GreyScaleFilter(int width, int height){
		greyScaleImage =  new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
	}
	
	@Override
	public BufferedImage applyFilter(BufferedImage image, int value) {
		
		ColorConvertOp greyConvert = new ColorConvertOp(image.getColorModel().getColorSpace(),
														greyScaleImage.getColorModel().getColorSpace(), null);
		greyConvert.filter(image, greyScaleImage);
		
		return greyScaleImage;
	}



}
