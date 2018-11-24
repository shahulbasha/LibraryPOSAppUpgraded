package library.ui.addmember;

import java.net.URL;
import java.util.ResourceBundle;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.database.DatabaseHandler;
import library.model.MemberModel;

public class LibraryAddMemberController implements Initializable {

	 @FXML
	    private JFXTextField memberId;

	    @FXML
	    private JFXTextField memberName;

	    @FXML
	    private JFXTextField phoneNo;

	    @FXML
	    private JFXTextField emailId;
	    
	    DatabaseHandler databaseHandler;
	    
	    boolean isEdit=Boolean.FALSE;

	    @FXML
	    void onAdd(ActionEvent event) {

	    	String id=memberId.getText();
	    	String name=memberName.getText();
	    	String phone=phoneNo.getText();
	    	String email=emailId.getText();
	    	
	    	if(id.isEmpty()||name.isEmpty()||phone.isEmpty()||email.isEmpty()) {
	    		Alert alert=new Alert(AlertType.ERROR);
	    		alert.setContentText("Please fill all the required fields");
	    		alert.setHeaderText(null);
	    		alert.showAndWait();
	    		return;
	    	}
	    	
	    	MemberModel memberModel=new MemberModel(id,name,phone,email);
	    	ObjectMapper mapper=new ObjectMapper();
	    	
	    	if(isEdit) {
	    		
	    		boolean isSuccess = DatabaseHandler.getInstance().editMember(memberModel);
	    		if(isSuccess) {
	    			Alert alert=new Alert(AlertType.INFORMATION);
	    			alert.setTitle("Member Edit");
	    			alert.setContentText("Member Details Saved successfully");
	    			alert.showAndWait();
	    		}
	    		else {
	    			Alert alert=new Alert(AlertType.ERROR);
	    			alert.setTitle("Member Edit");
	    			alert.setContentText("Save Failed. Please try Later");
	    			alert.showAndWait();
	    		}
	    		return;
	    	}
	    	
	    	try {
				String memberJSON = mapper.writeValueAsString(memberModel);
				Document doc = Document.parse(memberJSON);
				boolean insertSuccess = databaseHandler.insertMember(doc);
				if(insertSuccess) {
		    		Alert alert=new Alert(AlertType.INFORMATION);
		    		alert.setContentText("Member Added Successfully");
		    		alert.setHeaderText(null);
		    		alert.showAndWait();
		    	}else {
		    		Alert alert=new Alert(AlertType.ERROR);
		    		alert.setContentText("Could not add a Member at this time. Please check if the Member already exists or try again later!!");
		    		alert.setHeaderText(null);
		    		alert.showAndWait(); 	
				}
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	this.emailId.clear();
	    	this.memberId.clear();
	    	this.memberName.clear();
	    	this.phoneNo.clear();
	    }
	    
	    @FXML
	    private AnchorPane rootPane;

	    @FXML
	    void onCancel(ActionEvent event) {

	        Stage stage=(Stage)	rootPane.getScene().getWindow();
	        stage.close();
	    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		databaseHandler=DatabaseHandler.getInstance();
	}
	
	public void inflateUI(MemberModel model) {
		memberId.setText(model.getMemberId());
		memberName.setText(model.getMemberName());
		phoneNo.setText(model.getPhoneNo());
		emailId.setText(model.getEmailId());
		
		memberId.setEditable(false);
		
		isEdit=Boolean.TRUE;
	}

}
