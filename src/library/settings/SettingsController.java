package library.settings;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    }

    @FXML
    void handleSave(ActionEvent event) {

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
