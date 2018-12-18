package library.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
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
	
	public static void showMaterialDialog(StackPane rootPane, Node blurNode , String message,List<JFXButton> controls,String heading) {
		BoxBlur blur=new BoxBlur(3, 3, 3);
		
		JFXDialogLayout layout=new JFXDialogLayout();
		
		JFXDialog dialog=new JFXDialog(rootPane, layout, JFXDialog.DialogTransition.TOP);

		controls.forEach(controlButton->{
			controlButton.getStyleClass().add("dialog-button");
			controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent)->{
				dialog.close();
			});
		});
		//button.getStyleClass().add("dialog-button");

		
		layout.setHeading(new Text(heading));
		layout.setBody(new Text(message));
		layout.setActions(controls);

		dialog.setOnDialogClosed((JFXDialogEvent jfxEvent) -> {
			blurNode.setEffect(null);
		});
		dialog.show();
		blurNode.setEffect(blur);
	}
}
