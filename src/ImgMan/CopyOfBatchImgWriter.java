//package ImgMan;
//
//import java.awt.Color;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//import javax.imageio.ImageIO;
//import javax.swing.JLabel;
//import javax.swing.SwingWorker;
//
//public class CopyOfBatchImgWriter  extends SwingWorker<Void, Void>{
//
//	private ArrayBlockingQueue<File> FileBlockingQueue;
//	private JLabel resultLabel = new JLabel();
//
//	private float hue;
//	private float saturation;
//	private float brightness;
//	private String savePath;
//	
//		
//	public CopyOfBatchImgWriter( ArrayBlockingQueue<File> FileBlockingQueue, float hue, float saturation, float brightness){
//		this.FileBlockingQueue =  FileBlockingQueue;
//		this.hue = hue;
//		this.saturation = saturation;
//		this.brightness = brightness;
////		this.savePath = savePath;
//	}
//	
//	public void setResultLabel(JLabel label){
//		this.resultLabel = label;
//	}
//	
//	//Batch process all images - HSB values are used to applied to all images in directory and written in save directory
//	@Override
//	protected Void doInBackground() throws Exception {
//		//start new executor service
//		ExecutorService executor = Executors.newFixedThreadPool(4);
//		//while fileblocking queue is not empty; apply specified HSB values to all images
//		
//		while(!FileBlockingQueue.isEmpty()){
//	     Future<String> future = executor.submit(new Callable<String>() {
//	         public String call() throws InterruptedException {
//	
//	        	 File file = FileBlockingQueue.take();
//	        	 BufferedImage image;
//	        	 
//	     		try {
//	     			image = ImageIO.read(new File(file.getAbsoluteFile().toString()));
//	     			image = getEnhancedImagesHSB(image);
//	     			ImageIO.write(image, "png", new File(file.getAbsoluteFile().toString() + "new" + file.getName() ));
//	     			
//	    		} catch (IOException e) {
//	    			e.printStackTrace();
//	    		}
//	     		
//	     			return null; 
//	        	
//	        	 }
//	     });
//	     future.get();
//		}
//		
//	     executor.shutdown();
//		
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//		public BufferedImage getEnhancedImagesHSB(BufferedImage image){
//		int height = image.getHeight();
//		int width = image.getWidth();
//
//		int[] originalPixels = image.getRGB(0,0, width, height, null, 0,width);	
//		BufferedImage newImage = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
//	
//		float[] hsb = new float[]{0,0,0};
//		
//		for (int i = 0; i < originalPixels.length; i++)
//		{
//			Color c = new Color( originalPixels[i]);
//			int red =c.getRed();
//			int green = c.getGreen();
//			int blue = c.getBlue();
//
//			hsb = Color.RGBtoHSB(red, green, blue, hsb);
//			
//			 hsb[0] = (float)(hsb[0] +( hue/360.0));//hue
//			
//			hsb[1] *=  (saturation/100);
//			  if(hsb[1] > 1.0){
//				  hsb[1] = (float)1.0;
//			  }
//			
//			hsb[2] *=  (brightness/100);
//			  if(hsb[2] > 1.0) 
//				  {hsb[2] = (float)1.0;}
//	
//			  originalPixels[i] = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
//
//		}
//		
//		newImage.setRGB(0, 0, width, height, originalPixels, 0, width);
//
//		return newImage;
//	}
//	
//	
//
//}
