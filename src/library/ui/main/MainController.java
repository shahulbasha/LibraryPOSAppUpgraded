package library.ui.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.effects.JFXDepthManager;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.database.DatabaseHandler;
import library.model.BookModel;
import library.model.MemberModel;

public class MainController implements Initializable{
    @FXML
    private HBox book_info;

    @FXML
    private HBox member_info;
    
    @FXML
    private TextField bookId;

    @FXML
    private Text bookName;

    @FXML
    private Text bookAuthor;

    @FXML
    private Text bookStatus;

    @FXML
    private TextField memberId;

    @FXML
    private Text memberName;

    @FXML
    private Text memberContact;
    

    ObjectMapper mapper=new ObjectMapper();

	@FXML
    void loadAddBook(ActionEvent event) {

		loadWindow("/library/ui/addbook/LibraryAddBook.fxml", "Add New Book");
    }

    @FXML
    void loadAddMember(ActionEvent event) {
		loadWindow("/library/ui/addmember/LibraryAddMember.fxml", "Add New Member");
    }

    @FXML
    void loadBookList(ActionEvent event) {
    	loadWindow("/library/ui/listbook/LibraryBookList.fxml", "View Book List");
    }

    @FXML
    void loadMemberList(ActionEvent event) {
    	loadWindow("/library/ui/listmember/LibraryMemberList.fxml", "View Member List");
    }

    @FXML
    void loadSettings(ActionEvent event) {

    }
    
    @FXML
    void loadMemberInfo(ActionEvent event) {
    	String text = memberId.getText();
    	if(text!=null && !text.isEmpty()) {
    	MongoCollection<Document> memberCollection = DatabaseHandler.getInstance().setUpMemberCollection();
    	Document doc = memberCollection.find(eq("memberId",text)).first();
    	if(doc!=null) {
    	try {
			MemberModel memberModel = mapper.readValue(doc.toJson(), MemberModel.class);
			memberName.setText(memberModel.getMemberName());
			memberContact.setText(memberModel.getEmailId());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	}else {

			memberName.setText("No such Member Available");
			memberContact.setText("");
    	}
    	}
    }
    
    
    @FXML
    void loadBookInfo(ActionEvent event) {
    	String text = bookId.getText();
    	if(text!=null && !text.isEmpty()) {
    	MongoCollection<Document> bookCollection = DatabaseHandler.getInstance().setUpBookCollection();
    	Document doc=bookCollection.find(eq("bookId",text)).first();
    	if(doc!=null) {
    	try {
			BookModel bookModel = mapper.readValue(doc.toJson(), BookModel.class);
			bookName.setText(bookModel.getBookTitle());
			bookAuthor.setText(bookModel.getAuthor());
			bookStatus.setText(bookModel.isAvailable()?"true":"false");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	}
    	else {
			bookName.setText("No such Book Available");
			bookAuthor.setText("");
			bookStatus.setText("");
    	}
    	}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JFXDepthManager.setDepth(book_info, 1);
		JFXDepthManager.setDepth(member_info, 1);
		
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
