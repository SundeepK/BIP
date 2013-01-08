package ImgMan;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class CopyOfIMView extends JFrame  implements Observer {

	JButton selectIMGFolder = new JButton("Select Folder");
	JTextField inputDirectoryTxt = new JTextField(25);
	
	JButton saveIMGFolder = new JButton("Ouput Folder");
	JTextField saveDirectoryTxt = new JTextField(25);
	
	ImagePanel originalImage;
	//Labels and sliders to control image
	JLabel brightnessLabel = new JLabel("Brightness");
    JSlider brightnessSlider = new JSlider(0,200);
    //make sure to make all sliders 100% extra
    JLabel contrastLabel = new JLabel("Contrast");
    JSlider contrastSlider = new JSlider(-255,255,0);
    
    JLabel hueLabel = new JLabel("Hue");
    JSlider hueSlider = new JSlider(0,360);
    
    JLabel saturationLabel = new JLabel("Saturation");
    JSlider saturationhueSlider = new JSlider(0,200);
    
	JLabel randomLabel = new JLabel("Random");
	
	JButton processButton = new JButton("Process Images");
	JButton maximizeButton = new JButton("Maximize Image");
	
	JLabel progressBarLabel = new JLabel("Progress: ");
	JProgressBar progressBar = new JProgressBar();
	JLabel totalTimeLabel = new JLabel("Process Time: ");
	
	//resize combo box and textfields
	JComboBox resizeComboBox = new JComboBox(new String[]{"Don't Resize", "Resize Image's"});
	JTextField widthTxtField = new JTextField(10);
	JTextField heightTxtField = new JTextField(10);
	JLabel widthLabel = new JLabel("Width: ");
	JLabel heightLabel = new JLabel("Height: ");
	
	//output format combo box
	JLabel outputLabel = new JLabel("Select output format ");
	JComboBox outputFormatComboBox = new JComboBox(new String[]{"PNG", "JPG", "GIF"});
	
	//Thread options
	JTextField threadTxtField = new JTextField(10);
	JLabel threadLabel = new JLabel("Threads: ");
	
    //menubar used to show submenus for different filters
	JMenuBar menubar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu filter = new JMenu("Filter");
	JMenuItem greyscale = new JMenuItem("Greyscale");
	
	HashMap<String, Component> componentMap;
	
    IMModel model;
	public CopyOfIMView (IMModel model){
		super("Image manipulation");
		this.model = model;
		JPanel mainJPanel = new JPanel();
		
		mainJPanel.setLayout(new GridBagLayout());
		
		
		
		brightnessSlider.setMajorTickSpacing(50);
		brightnessSlider.setPaintLabels(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		Hashtable<Integer, JLabel> labelTableHue = new Hashtable<Integer, JLabel>();
		
		labelTable.put( new Integer( 0 ), new JLabel("0%") );
		labelTable.put( new Integer( 100 ), new JLabel("50%") );
		labelTable.put( new Integer( 200 ), new JLabel("100%") );
		
		brightnessSlider.setLabelTable( labelTable );
		saturationhueSlider.setMajorTickSpacing(50);
		saturationhueSlider.setPaintLabels(true);
		saturationhueSlider.setLabelTable(labelTable);

		labelTableHue.put( new Integer( 0 ), new JLabel("0") );
		labelTableHue.put( new Integer( 360 ), new JLabel("360") );
		hueSlider.setLabelTable(labelTableHue);
		hueSlider.setPaintLabels(true);
		hueSlider.setMajorTickSpacing(180);
		hueSlider.setPaintTicks(true);
		
		progressBar.setStringPainted(true);
		inputDirectoryTxt.setEnabled(false);
		
		originalImage = new ImagePanel();
		
		//Resize componenets
		widthTxtField.setPreferredSize(new Dimension(10, 10));
		heightTxtField.setPreferredSize(new Dimension(10,10));
		resizeComboBox.setPreferredSize(new Dimension(113,10));

		widthTxtField.setEnabled(false);
		heightTxtField.setEnabled(false);
		
		outputFormatComboBox.setPreferredSize(new Dimension(113,10));
		
		//Define left panel grifBagContraints
		GridBagConstraints gc = new GridBagConstraints();
	    JPanel savePanel = new JPanel();

		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.weightx = 0.5;
        gc.gridx = 0;
        gc.gridy = 0;
        savePanel.add(selectIMGFolder, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        savePanel.add(inputDirectoryTxt, gc);
//
   
        gc.gridx = 0;
        gc.gridy = 1;
        mainJPanel.add(originalImage, gc);
        
//        gc.gridx = 0;
//        gc.gridy = 2;
//        mainJPanel.add(saveIMGFolder, gc);       
//  
//        gc.gridx = 1;
//        gc.gridy = 2;
//        mainJPanel.add(saveDirectoryTxt, gc);
                
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weighty = 1;
        gc.gridx = 0;
        gc.gridy = 2;
        mainJPanel.add(randomLabel, gc);
        
        gc = new GridBagConstraints();
        JPanel bottomImagePanel = new JPanel();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.weightx = 0.5;
        gc.gridx = 0;
        gc.gridy = 3;
        bottomImagePanel.add(processButton, gc);
        
        gc.weighty = 2;
		gc.weightx = 0.5;
        gc.gridx = 1;
        gc.gridy = 3;
        bottomImagePanel.add(maximizeButton, gc);
     
        JPanel progressPanel = new JPanel();
		gc = new GridBagConstraints();
		

        
		gc.weightx = 0.5;
        gc.gridx = 0;
        gc.gridy = 4;
        progressPanel.add(progressBarLabel, gc);
        

		gc.weightx = 0.5;
        gc.gridx = 1;
        gc.gridy = 4;
        progressPanel.add(progressBar, gc);
        

		gc.weightx = 0.5;
        gc.gridx = 2;
        gc.gridy = 4;
        progressPanel.add(totalTimeLabel, gc);
        
        

        //second column
        //brightness

        JPanel sidePane = new JPanel(new GridBagLayout());
        gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.BOTH;
		gc.weightx =0.5;
		gc.ipady = 15;
		
        gc.gridx = 1;
        gc.gridy = 0;
        mainJPanel.add(brightnessLabel, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        sidePane.add(brightnessSlider, gc);
           
        gc.gridx = 1;
        gc.gridy = 2;
        sidePane.add(hueLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 3;
        sidePane.add(hueSlider, gc);
        
        gc.gridx = 1;
        gc.gridy = 4;
        sidePane.add(saturationLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 5;
        sidePane.add(saturationhueSlider, gc);
        
        gc.gridx = 1;
        gc.gridy = 6;
        sidePane.add(contrastLabel, gc);
      
        gc.gridx = 1;
        gc.gridy = 7;
        sidePane.add(contrastSlider, gc);
        
        gc.gridx = 1;
        gc.gridy = 8;
        sidePane.add(resizeComboBox, gc);
        
        gc.gridx = 1;
        gc.gridy = 9;
        sidePane.add(widthLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 10;
        sidePane.add(widthTxtField, gc);
        
        gc.gridx = 1;
        gc.gridy = 11;
        sidePane.add(heightLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 12;
        sidePane.add(heightTxtField, gc);
        
        gc.gridx = 1;
        gc.gridy = 13;
        sidePane.add(outputLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 14;
        sidePane.add(outputFormatComboBox, gc);
        
        gc.gridx = 1;
        gc.gridy = 15;
        sidePane.add(threadLabel, gc);
        
        gc.gridx = 1;
        gc.gridy = 16;
        sidePane.add(threadTxtField, gc);
        
       

        gc = new GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.NORTHWEST;
        mainJPanel.add(sidePane, gc);
        
        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 3;
        gc.anchor = GridBagConstraints.NORTHWEST;
        mainJPanel.add(bottomImagePanel, gc);
        
        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 4;
        gc.anchor = GridBagConstraints.NORTHWEST;
        mainJPanel.add(progressPanel, gc);
        
        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.NORTHWEST;
        mainJPanel.add(savePanel, gc);

        
        //Menu bar and submenu
        filter.add(greyscale);
        menubar.add(file);
        menubar.add(filter);
        

        
        this.setJMenuBar(menubar);
        
        JScrollPane sp = new JScrollPane(mainJPanel);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.validate();
        this.add(sp);
		this.setVisible(true);
		this.setPreferredSize(new Dimension(800, 650));
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		componentMap =  new HashMap<String, Component>();
		
		for(Component comp : this.getComponents())
			componentMap.put(comp.getName(), comp);
		
	}
	
	
	public HashMap<String, Component> getComponentHashMap(){
		return componentMap;
	}
		
	public void addFileBrowserLis (ActionListener lis){
		selectIMGFolder.addActionListener(lis);
	}
	
	public void addChangeBrightnessEvent (ChangeListener changevent){
		brightnessSlider.addChangeListener(changevent);
	}
	
	public void addChangeContrastEvent (ChangeListener changevent){
		contrastSlider.addChangeListener(changevent);
	}
	
	public void addChangeSaturation (ChangeListener changevent){
		saturationhueSlider.addChangeListener(changevent);
	}
	
	public void addChangeHue (ChangeListener changevent){
		hueSlider.addChangeListener(changevent);
	}
	
	public void addImagesWriteLis (ActionListener lis){
		processButton.addActionListener(lis);
	}
	
	public void addGreyScaleFilterLis(ActionListener lis){
		greyscale.addActionListener(lis);
	}
	
	public JProgressBar getProgressbar(){
		return progressBar;
	}
	
	public JButton getProcessButton(){
		return processButton;
	}
	
	public JTextField getwidthJTextField(){
		return widthTxtField;
	}
	
	public JTextField getheightJTextField(){
		return heightTxtField;
	}
	
	public int getWidthResizeData(){
	
		return getStringToInt(widthTxtField.getText());
	}
	
	public int geHeightResizeData(){
		
		return getStringToInt(heightTxtField.getText());
	}

	private int getStringToInt(String num){
		int i = 0;
		try{
		i = Integer.parseInt(num);
		}catch (NumberFormatException e){
			e.printStackTrace();
		i = 0;
		}
		return i;
	}
	
	public void addResizeComboBoxLis(ActionListener lis){
		resizeComboBox.addActionListener(lis);
	}

	@Override
	public void update(Observable o, Object arg) {
		inputDirectoryTxt.setText(model.getSourceFolder());
		
	}
	
	public ImagePanel getImagePanel(){
		return originalImage;
	}
	
}
