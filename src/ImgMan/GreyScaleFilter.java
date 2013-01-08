package ImgMan;

import java.awt.Component;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GreyScaleFilter implements IFilterEffect{

	BufferedImage greyScaleImage;

	Component[] components = new Component[1];
	IMView view; IMModel model;
	public GreyScaleFilter(IMView view, IMModel model){
		this.view = view;
		this.model = model;
		int width =  model.getEnhancedImage().getOriginalImage().getWidth();
		int height = model.getEnhancedImage().getOriginalImage().getHeight();
		greyScaleImage =  new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
	}
	

	@Override
	public Component[] getComponentList() {
		return components;
	}

	@Override
	public BufferedImage applyFilter() {
		BufferedImage image= model.getEnhancedImage().getOriginalImage();
		ColorConvertOp greyConvert = new ColorConvertOp(image.getColorModel().getColorSpace(),
				greyScaleImage.getColorModel().getColorSpace(), null);
		greyConvert.filter(image, greyScaleImage);
		return greyScaleImage;
		
	}


	@Override
	public BufferedImage getFilteredImage() {
		return greyScaleImage;
	}



}
