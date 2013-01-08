package ImgMan;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;

public interface IFilterEffect {

	public BufferedImage applyFilter();
	public Component[] getComponentList();
	public BufferedImage getFilteredImage();
}
