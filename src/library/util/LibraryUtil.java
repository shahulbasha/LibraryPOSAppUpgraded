package library.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.swing.ImageIcon;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
	
	
	public static void loadWindow(URL loc,String title,Stage parentStage) {
		try {
	//		System.out.println(loc+title);
			Stage stage=null;
			if(parentStage!=null) {
				stage=parentStage;
			}else {
				stage=new Stage(StageStyle.DECORATED);
			}
			Parent root=FXMLLoader.load(loc); 
			Scene scene=new Scene(root);
			stage.setTitle(title);
			stage.setScene(scene);
			stage.show();
			
			setStageIcon(stage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
