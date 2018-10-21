package library.ui.listmember;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.database.DatabaseHandler;
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
		
		tableView.getItems().setAll(list);
	}


	private void initCol() {

		memberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
		memberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
		phoneNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
		emailId.setCellValueFactory(new PropertyValueFactory<>("emailId"));
		
	}
}
