package library.ui.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.model.SettingsModel;
import library.ui.main.MainController;
import library.util.LibraryUtil;

public class LoginController implements Initializable {
	
	
	 @FXML
	    private JFXTextField username;

	    @FXML
	    private JFXPasswordField password;
	    
	    @FXML
	    private Label labelText;
	    
	    SettingsModel settings;

	    @FXML
	    void handleCancel(ActionEvent event) {

	    	System.exit(0);
	    }

	    @FXML
	    void handleSave(ActionEvent event) {
	    	labelText.setText("Library Assistant Login");
	    	labelText.setStyle("-fx-background-color:black;");
	    	
	    	settings=SettingsModel.getModel();
	    	
	    	if(username.getText().equalsIgnoreCase(settings.getUsername()) && BCrypt.checkpw(password.getText(), settings.getPassword())) {
	    	((Stage)username.getScene().getWindow()).close();
	    	loadWindow("/library/ui/main/Main.fxml", "Library Checkout Application");
	    	
	    	}
	    	else {
		    	labelText.setText("Invalid Credentials");
		    	labelText.setStyle("-fx-background-color:#bd081c;");
	    	}
	    	

	    }

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			
		}
		
		void loadWindow(String loc,String title) {
			try {
				System.out.println(loc+title);
				Parent root=FXMLLoader.load(getClass().getResource(loc));
				Stage stage=new Stage(StageStyle.DECORATED);
				Scene scene=new Scene(root);
				stage.setTitle(title);
				stage.setScene(scene);
				stage.show();
				
				LibraryUtil.setStageIcon(stage);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

}
