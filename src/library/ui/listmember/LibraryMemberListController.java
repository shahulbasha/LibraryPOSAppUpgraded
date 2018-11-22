package library.ui.listmember;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.database.DatabaseHandler;
import library.model.BookModel;
import library.model.MemberModel;

public class LibraryMemberListController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<MemberModel> tableView;

    @FXML
    private TableColumn<MemberModel, String> memberId;

    @FXML
    private TableColumn<MemberModel, String> memberName;

    @FXML
    private TableColumn<MemberModel, String> phoneNo;

    @FXML
    private TableColumn<MemberModel,String> emailId;
    
    
    DatabaseHandler dbHandler;
    ObjectMapper mapper=new ObjectMapper();
    ObservableList<MemberModel> list=FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbHandler=DatabaseHandler.getInstance();
		initCol();
		loadData();
		
	}


	private void loadData() {
		MongoCollection<Document> memberCollection = dbHandler.setUpMemberCollection();
		for (Document doc : memberCollection.find()) {
			try {
				MemberModel readValue = mapper.readValue(doc.toJson(), MemberModel.class);
				list.add(readValue);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		tableView.setItems(list);
	}


	private void initCol() {

		memberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
		memberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
		phoneNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
		emailId.setCellValueFactory(new PropertyValueFactory<>("emailId"));
		
	}
	
	
	 @FXML
	    void deleteMemberfromView(ActionEvent event) {
	    	
	    	MemberModel selectedItem = tableView.getSelectionModel().getSelectedItem();
	    	

	    	if(selectedItem!=null) {
	    		
	    		Alert alert=new Alert(AlertType.CONFIRMATION);
	    		alert.setTitle("Delete Member");
	    		alert.setContentText("Are you sure you want to delete the member "+selectedItem.getMemberName());
	    		
	    		Optional<ButtonType> response = alert.showAndWait();
	    		if(response.get()==ButtonType.OK) {
	    			if(DatabaseHandler.getInstance().deleteMember(selectedItem)) {
	    	    		Alert alert2=new Alert(AlertType.INFORMATION);
	    	    		alert2.setTitle("Delete Success");
	    	    		alert2.setContentText("Member Deleted Successfully");
	    	    		alert2.showAndWait();
	    	    		
	    	    		list.remove(selectedItem);
	    			}
	    			else {
	    	    		Alert alert3=new Alert(AlertType.ERROR);
	    	    		alert3.setTitle("Delete Failure");
	    	    		alert3.setContentText("Please try deleting after sometime");
	    	    		alert3.showAndWait();
	    			}
	    			
	    		}else {
	        		Alert alert1=new Alert(AlertType.INFORMATION);
	        		alert1.setTitle("Cancel");
	        		alert1.setContentText("Deletion Cancelled");
	    		}
	    	}
	    	else {
	    		Alert alert=new Alert(AlertType.ERROR);
	    		alert.setTitle("Select Member");
	    		alert.setContentText("No Member Selected");
	    		return;
	    	}

	    }

}
