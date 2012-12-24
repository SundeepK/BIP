package ImgMan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class IMController  {

	IMView view;
	IMModel model;
	ActionListener ShowFileBrowser = new ShowFileBrowser();
	ChangeListener ChangeBrightness = new ChangeBrightness();
	ChangeListener ChangeContrast = new ChangeContrast();
	ChangeListener ChangeSaturation = new ChangeSaturation();
	ChangeListener ChangeHue = new ChangeHue();
	ActionListener writeImages = new WriteNewImages();
	public IMController(IMModel model, IMView view)
	{
		this.view = view;
		this.model = model;
		
		
		view.addFileBrowserLis(ShowFileBrowser);
		view.addChangeBrightnessEvent(ChangeBrightness);
		view.addChangeContrastEvent(ChangeContrast);
		view.addChangeSaturation(ChangeSaturation);
		view.addChangeHue(ChangeHue);
		view.addImagesWriteLis(writeImages);
		
	}
	

	public class WriteNewImages implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			((JButton)e.getSource()).setEnabled(false);
			ArrayList<File> imageFileList = model.getImageList();
			ArrayBlockingQueue<File> abq = new ArrayBlockingQueue<File>(imageFileList.size());
			for(File f : imageFileList){
				try {
					abq.put(f);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
			float[] hsb = model.getHSB();
			BatchImgWriter imgWriter = new BatchImgWriter(abq, hsb[0], hsb[1], hsb[2], view, model.getSourceFolder());
			imgWriter.execute();
		}
		
	}
	
	public  class ShowFileBrowser implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter   filter = new FileNameExtensionFilter("Image files", "JPEG", "jpg", "PNG", "png","gif");
			JFileChooser fileBrowser = new JFileChooser("C:\\");
			fileBrowser.setFileFilter(filter);
			fileBrowser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileBrowser.showOpenDialog(view);
			model.setNewSourceFolder(fileBrowser.getSelectedFile().getAbsolutePath(), filter);
			view.getImagePanel().setImage(model.getImage());

			//System.out.println(fileBrowser.getSelectedFile().getAbsolutePath());
		}
		
	}
	
	public class ChangeBrightness implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(((JSlider) e.getSource()).getValueIsAdjusting() == false && model.getImage() != null){
			JSlider slider= ((JSlider)e.getSource());
			System.out.println((float)slider.getValue());
			model.setBrightness((float)slider.getValue());
			view.getImagePanel().setImage(model.getEnhancedImagesHSB());
			view.getImagePanel().getParent().repaint();		
			}
		}
		
	}
	
	public class ChangeSaturation implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(((JSlider) e.getSource()).getValueIsAdjusting() == false && model.getImage() != null){
			JSlider slider= ((JSlider)e.getSource());
			System.out.println((float)slider.getValue());
			model.setSaturation((float)slider.getValue());
			view.getImagePanel().setImage(model.getEnhancedImagesHSB());
			view.getImagePanel().getParent().repaint();		
			}
		}
		
	}
	
	
	public class ChangeContrast implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(((JSlider) e.getSource()).getValueIsAdjusting() == false && model.getImage() != null){
				
			//Ajdust contrast of image
				JSlider slider= ((JSlider)e.getSource());
				model.setContrast(slider.getValue());
				view.getImagePanel().setImage(model.getEnhancedImages());
				view.getImagePanel().getParent().repaint();		
			}
		}

	}
	
	public class ChangeHue implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(((JSlider) e.getSource()).getValueIsAdjusting() == false && model.getImage() != null){
			//Ajdust contrast of image
				JSlider slider= ((JSlider)e.getSource());
				model.setHue(slider.getValue());
				view.getImagePanel().setImage(model.getEnhancedImagesHSB());
				view.getImagePanel().getParent().repaint();		
			}
		}

	}
	
	
	

}
