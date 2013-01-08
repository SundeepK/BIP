package ImgMan;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
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
	ActionListener greyScaleFilter = new GreyScaleChange();
	ActionListener resizeComboBox = new ResizeImageComboBox();
	//Dialog window for filters
	FilterDialog filterDialog;
	
	GreyScaleFilter gsFilter;
	
	public IMController(IMModel model, IMView view)
	{
		this.view = view;
		this.model = model;
		
		filterDialog = new FilterDialog(view, "Filter");
		
		view.addFileBrowserLis(ShowFileBrowser);
		view.addChangeBrightnessEvent(ChangeBrightness);
		view.addChangeContrastEvent(ChangeContrast);
		view.addChangeSaturation(ChangeSaturation);
		view.addChangeHue(ChangeHue);
		view.addImagesWriteLis(writeImages);
		view.addGreyScaleFilterLis(greyScaleFilter);
		view.addResizeComboBoxLis(resizeComboBox);
		
	}
	
	

	
	
	/**
	 * 
	 * @author Sundeep
	 * Actionlistener to write images using threads
	 */
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
			
			BatchImgWriter imgWriter = new BatchImgWriter(abq,model, view);
			
			if(model.imageResizable()) {
				model.setResizeWidth(view.getWidthResizeData());
				model.setResizeHeight(view.geHeightResizeData());
				imgWriter.setImageResizable(true);
				imgWriter.setWidthHeight(view.getWidthResizeData(), view.geHeightResizeData());
			}
			imgWriter.execute();
		}
		
	}
	
	public  class ShowFileBrowser implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter   filter = new FileNameExtensionFilter("Image files", "JPEG", "JPG", "jpg", "PNG", "png","gif");
			JFileChooser fileBrowser = new JFileChooser("C:\\");
			fileBrowser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fileBrowser.setFileFilter(filter);
			int returnVal = fileBrowser.showOpenDialog(view);
			//if cancelled is not clicked, then get selected folder
			if(returnVal == JFileChooser.APPROVE_OPTION){
			model.setNewSourceFolder(fileBrowser.getSelectedFile().getAbsolutePath(), filter);
			view.getImagePanel().setImage(model.getEnhancedImage().getOriginalImage());
			view.getImagePanel().repaint();
			}

		}
		
	}
	
	public class ChangeBrightness implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(((JSlider) e.getSource()).getValueIsAdjusting() == false && model.getEnhancedImage() != null){
			JSlider slider= ((JSlider)e.getSource());
			System.out.println((float)slider.getValue());
			model.getEnhancedImage().setBrightness((float)slider.getValue());
			view.getImagePanel().setImage(model.getEnhancedImage().getEnhancedImagesHSB());
			view.getImagePanel().getParent().repaint();		
			}
		}
		
	}
	
	public class ChangeSaturation implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(((JSlider) e.getSource()).getValueIsAdjusting() == false && model.getEnhancedImage() != null){
			JSlider slider= ((JSlider)e.getSource());
			System.out.println((float)slider.getValue());
			model.getEnhancedImage().setSaturation((float)slider.getValue());
			view.getImagePanel().setImage(model.getEnhancedImage().getEnhancedImagesHSB());
			view.getImagePanel().getParent().repaint();		
			}
		}
		
	}
	
	
	public class ChangeContrast implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(((JSlider) e.getSource()).getValueIsAdjusting() == false && model.getEnhancedImage() != null){
				
			//Ajdust contrast of image
				JSlider slider= ((JSlider)e.getSource());
				model.getEnhancedImage().setContrast(slider.getValue());
//				view.getImagePanel().setImage(model.getEnhancedImage().getEnhancedImages());
				view.getImagePanel().getParent().repaint();		
			}
		}

	}
	
	public class ChangeHue implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			if(((JSlider) e.getSource()).getValueIsAdjusting() == false && model.getEnhancedImage() != null){
			//Ajdust contrast of image
				JSlider slider= ((JSlider)e.getSource());
				model.getEnhancedImage().setHue(slider.getValue());
				view.getImagePanel().setImage(model.getEnhancedImage().getEnhancedImagesHSB());
				view.getImagePanel().getParent().repaint();		
			}
		}

	}
	
	public class ResizeImageComboBox implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox resize = ((JComboBox)e.getSource());
			if(resize.getSelectedItem() == "Resize Image's"){
				view.getwidthJTextField().setEnabled(true);
				view.getheightJTextField().setEnabled(true);
				model.setIsResizable(true);
			}else{
				view.getwidthJTextField().setEnabled(false);
				view.getheightJTextField().setEnabled(false);
				model.setIsResizable(false);
			}
		}
		
	}
	
	public class GreyScaleChange implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (model.getEnhancedImage() != null){
			gsFilter = new GreyScaleFilter(view, model);
			jDialogFilter(gsFilter);
			}
		}
		
	}
	
	public void jDialogFilter(final IFilterEffect filter){
		
		int result = JOptionPane.showConfirmDialog(view, new Object[] {"Apply Greysclae filter", filter.getComponentList()}, "Filter", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION){
			filter.applyFilter();
			model.getEnhancedImage().setnewImage(filter.getFilteredImage());
			view.getImagePanel().setImage( filter.getFilteredImage());
			view.getImagePanel().getParent().repaint();	
		}else if (result == JOptionPane.CANCEL_OPTION) {
			model.getEnhancedImage().setnewImage(model.getEnhancedImage().newgetImage());
			view.getImagePanel().setImage(model.getEnhancedImage().newgetImage());
			view.getImagePanel().getParent().repaint();	
		}

	}
	

	
	

}
