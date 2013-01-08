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
	private boolean resizeImage= false;
	private ArrayList<File> imageFileList;
	private int resizeWidth, resizeHeight;
	
	EnhancedImage image;

	public IMModel()
	{
		imageFileList = new ArrayList<File>();
	}
	
	public EnhancedImage getEnhancedImage(){
		return image;
	}
	
	public void setResizeWidth(int w){
		resizeWidth = w;
	}
	
	public void setResizeHeight(int h){
		resizeHeight = h;
	}
	
	public int getResizeWidth(){
		return resizeWidth;
	}
	
	public int getResizeHeight(){
		return resizeHeight;
	}
	
	public boolean imageResizable()
	{
		return resizeImage;
	}
	
	public void setIsResizable(boolean bool){
		resizeImage = bool;
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


