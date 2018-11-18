package library.settings;

import java.net.URL;
import java.util.ResourceBundle;

import org.mindrot.jbcrypt.BCrypt;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.Window;
import library.model.SettingsModel;

public class SettingsController implements Initializable {

    @FXML
    private JFXTextField fine;

    @FXML
    private JFXTextField days;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    void handleCancel(ActionEvent event) {
    	((Stage)days.getScene().getWindow()).close();
    }

    @FXML
    void handleSave(ActionEvent event) {
    	SettingsModel model=SettingsModel.getModel();
    	model.setFinePerDay(Integer.parseInt(fine.getText()));
    	model.setNoOfDays(Integer.parseInt(days.getText()));
    	model.setUsername(username.getText());
    	model.setPassword(BCrypt.hashpw(password.getText(), BCrypt.gensalt()));
    	
    	SettingsModel.writeToFile(model);

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initDefaultValues();
		
	}

	private void initDefaultValues() {
		SettingsModel model=SettingsModel.getModel();
		fine.setText(String.valueOf(model.getFinePerDay()));
		days.setText(String.valueOf(model.getNoOfDays()));
		username.setText(String.valueOf(model.getUsername()));
		password.setText(String.valueOf(model.getPassword()));
		
		
	}

}
