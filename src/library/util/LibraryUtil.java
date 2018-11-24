package library.util;

import java.net.URL;

import javax.swing.ImageIcon;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LibraryUtil {

	private static final String ICON_LOC="icon.png";
	
	
	public static void setStageIcon(Stage stage) {
		System.out.println("stage");
		
		try{
			stage.getIcons().add(new Image("file:G:\\J2EEProjects\\LibraryPOS\\src\\resources\\icons\\icon.png"));
		//	stage.getIcons().add(new Image("@../../../resources/icons/icon.png"));
			
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getCause());
		}
		

		
	}
}
