package ImgMan;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Slider {

	public static void main(String[] args) {

	    JFrame frame = new JFrame("Test");

	    JSlider slider = new JSlider();

	    slider.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent ce) {
	            System.out.println(((JSlider) ce.getSource()).getValue());
	        }
	    });

	    frame.add(slider);

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(400, 300);
	    frame.setVisible(true);
	}

}
