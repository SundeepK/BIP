package ImgMan;

import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter implements FileFilter {

	private String[] imageExtentions = {"png", "jpg", "gif" };
	
	@Override
	public boolean accept(File file) {
		
		for(String ext : imageExtentions){
			if(file.isFile()){
				if(file.getName().endsWith(ext))
					return true;
			}
		}
		
		return false;
	}
	
	

}
