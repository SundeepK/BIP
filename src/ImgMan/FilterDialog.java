package ImgMan;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FilterDialog extends JDialog {

	JSlider filterSlider = new JSlider(0,100);
	JOptionPane filterOptionPane;
	int filterValue = 0;
	
	public FilterDialog(Frame owner, String title){
		super(owner, title);

		this.setSize(new Dimension(300,150));
		JOptionPane optionPane = new JOptionPane();
		this.setSize(300,150);
		optionPane.setMessage(new Object[] { "Select a value: ", filterSlider });
		optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
		optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		addFilterSliderLis();
		this.add(optionPane);

	}
	
	public int getFilterValue(){
		return filterValue;
	}
		
	public void setFilterSlider(JSlider slider){
		this.filterSlider = slider;
	}
	
	@Override
	public  void setTitle(String title){
		super.setTitle(title + " - Filter");
	}
	
	public void addFilterSliderLis(){
		
		ChangeListener filterListener = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = ((JSlider)e.getSource());
			       if (!slider.getValueIsAdjusting()) {
			    		filterValue = slider.getValue();
			    		
			         }
			      
			}
		};
		
		filterSlider.addChangeListener(filterListener);

	}
	
	
	
	
	
	
}
