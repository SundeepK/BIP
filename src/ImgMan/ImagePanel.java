package ImgMan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	Image image;

	public ImagePanel(){
		this.setPreferredSize(new Dimension(400,300));
	
	}
	
	public void setImage(String imagePath){
		try {
			this.image = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setImage(Image image){
		this.image = image;
	}
	
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(Color.red);
		g.fillRect(0, 0, 400,300);
		if(image != null){
			float height = ((float)image.getHeight(null) / (float)image.getWidth(null)) * 400;
//			System.out.println(height);
			 g.drawImage(image, 0,0, this);
//			g.drawImage(image, 10, 10, 300, (int)height, null, null);
		}
		
	}

}
