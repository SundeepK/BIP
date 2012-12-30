package ImgMan;

import java.awt.Image;
import java.awt.image.BufferedImage;

public interface IFilterEffect {

	public BufferedImage applyFilter(BufferedImage image, int value);
}
