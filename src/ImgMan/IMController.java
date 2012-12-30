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
			
			BatchImgWriter imgWriter = new BatchImgWriter(abq,model.getEnhancedImage(),  view, model.getSourceFolder());
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
	
	public class GreyScaleChange implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (model.getEnhancedImage() != null){
			BufferedImage im =  model.getEnhancedImage().getOriginalImage();
			gsFilter = new GreyScaleFilter(im.getWidth(), im.getHeight());
			jDialogFilter(gsFilter);
			}
		}
		
	}
	
	public void jDialogFilter(final IFilterEffect filter){
		
//		JDialog filterDialog = new JDialog(view, "Filter");
//		filterDialog.setSize(new Dimension(300,150));
//		JOptionPane optionPane = new JOptionPane();
//		filterDialog.setSize(300,150);
//		
		JSlider filterSlider = new JSlider(0,100);
//		
//		optionPane.setMessage(new Object[] { "Select a value: ", filterSlider });
//		optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
//		optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
//		filterDialog.setContentPane(optionPane);

//		int value = ((Integer)optionPane.getValue()).intValue();
//		if(value == JOptionPane.CANCEL_OPTION)

		
		ChangeListener filterListener = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = ((JSlider)e.getSource());
			       if (!slider.getValueIsAdjusting()) {
			    	   System.out.println(slider.getValue());
			    	   view.getImagePanel().setImage(filter.applyFilter(model.getEnhancedImage().getOriginalImage(), 0));
			    	   view.getImagePanel().getParent().repaint();	
			         }  
			}
		};
		
		filterSlider.addChangeListener(filterListener);
//		filterDialog.setVisible(true);
		
		int result = JOptionPane.showConfirmDialog(view, new Object[]{"Select value:", filterSlider}, "Filter", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION)
			System.out.println("works");

	}
	

	
	

}
