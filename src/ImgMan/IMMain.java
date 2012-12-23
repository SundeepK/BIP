package ImgMan;

public class IMMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	 
	 IMModel model = new IMModel();
	 IMView view = new IMView(model);
	 IMController controller = new IMController(model, view);
	 model.addObserver(view);
	 
	}

}
