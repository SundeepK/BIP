package ImgMan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class BatchImgWriter  extends SwingWorker<Void, Void>{

	private ArrayBlockingQueue<File> FileBlockingQueue;
	private JLabel resultLabel = new JLabel();
	private String OUTPUT_FOLDER = "\\BIP_OUT\\" ;
	EnhancedImage enhancedImage;
	private String savePath;
	private JProgressBar progressBar;
	private int progress;
	private Component component;
	float[] hsb;
		
	public BatchImgWriter( ArrayBlockingQueue<File> FileBlockingQueue, EnhancedImage enhancedImage,
			Component component, String savePath){
		 hsb = enhancedImage.getHSB();
		this.FileBlockingQueue =  FileBlockingQueue;
		this.enhancedImage = enhancedImage;
		this.savePath = savePath;
//		progressBar = bar;
//		progressBar.setMaximum(FileBlockingQueue.size()/4);
		this.component = component;
	}
	
	public void setResultLabel(JLabel label){
		this.resultLabel = label;
	}
	
	private synchronized void incrementProgress(){
		++progress;
	}
	
	//Batch process all images - HSB values are used to applied to all images in directory and written in save directory
	@Override
	protected Void doInBackground() throws Exception {
		//start new executor service
		ExecutorService executor = Executors.newFixedThreadPool(4);
		//while fileblocking queue is not empty; apply specified HSB values to all images
		long startTime = System.currentTimeMillis();
		int pro=0;
		Future[] future = new Future[4];
		
		ProgressMonitor monitor = new ProgressMonitor(component, "Progress", "Processing images", 0, FileBlockingQueue.size()/4);
		
		//Check if output directory currently exits - if not, create a new one
		File destinationFolder = new File(savePath + OUTPUT_FOLDER);
		if(!destinationFolder.exists() && !destinationFolder.mkdir())
			throw new IOException("Unable to create directory");
		
		while(!FileBlockingQueue.isEmpty()){
		
		for (int i = 0; i <  4; i++) {

		 	if(FileBlockingQueue.isEmpty())
		 		return null; 
			
		 	future[i] = (executor.submit(new Callable<String>() {
	        public String call() throws InterruptedException {
			
     	 	if(FileBlockingQueue.isEmpty())
    	 		return null; 
     	 	
	        	 File file = FileBlockingQueue.remove();

	     		try {
	     			
	    			BufferedImage in = ImageIO.read(new File(file.getAbsoluteFile().toString()));    			
	     			ImageIO.write( EnhancedImage.getEnhancedImagesRaster(in, hsb), "PNG", new File( savePath + OUTPUT_FOLDER+ file.getName() ));

	     			} catch (IOException e) {
	    			e.printStackTrace();
	     			}
	     		
	     			return null; 
	       }
		
	     }));
	     
		}
	    
		 for(Future f :future)f.get();
    
			++pro;
//			progressBar.setValue(pro);
//			progressBar.repaint();	
			String message = String.format("Completed %d%%.\n", (pro*4));
			monitor.setNote(message);
			monitor.setProgress(pro);
			
			if (monitor.isCanceled()) {
				 break;
			}

		}
		
	    executor.shutdown();
		long endTime = System.currentTimeMillis();

		System.out.println(endTime - startTime);
		((IMView)component).getProcessButton().setEnabled(true);
		
		return null;
	}
	
}
