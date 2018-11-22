package library.ui.addbook;

import java.net.URL;
import java.util.ResourceBundle;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.mongodb.client.MongoCollection;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.database.DatabaseHandler;
import library.model.BookModel;

public class LibraryAddBookController implements Initializable {
	
	@FXML
    private JFXTextField bookId;

    @FXML
    private JFXTextField bookName;

    @FXML
    private JFXTextField author;

    @FXML
    private JFXTextField publisher;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton cancelButton;
    
    DatabaseHandler databaseHandler;
    
    boolean isEditMode=Boolean.FALSE;
    
    @FXML
    private AnchorPane rootPane;

    @FXML
    void onCancel(ActionEvent event) {

    Stage stage=(Stage)	rootPane.getScene().getWindow();
    stage.close();
    
    }

    //on Click of Save add the book to the database
    @FXML
    void onSave(ActionEvent event) {
    	String id = bookId.getText();
    	String bookTitle = bookName.getText();
    	String bookAuthor = author.getText();
    	String bookPublisher = publisher.getText();
    	boolean isAvailable=true;
    	
    	if(id.isEmpty()||bookTitle.isEmpty()||bookAuthor.isEmpty()||bookPublisher.isEmpty()) {
    		Alert alert=new Alert(AlertType.ERROR);
    		alert.setContentText("Please fill all the required fields");
    		alert.setHeaderText(null);
    		alert.showAndWait();
    		return;
    	}
    	
    	if(isEditMode) {
    		DatabaseHandler.getInstance().handleEditOperation();
    	}
    	
    	BookModel bookModel=new BookModel(id,bookTitle,bookAuthor,bookPublisher,isAvailable);
    	ObjectMapper mapper=new ObjectMapper();
    	
    	bookModel.setAuthor(bookAuthor);
    	bookModel.setBookId(id);
    	bookModel.setBookTitle(bookTitle);
    	bookModel.setPublisher(bookPublisher);
    	bookModel.setAvailable(isAvailable);
    	

    	try {
			String bookJson = mapper.writeValueAsString(bookModel);
			Document document = Document.parse(bookJson);
	    	boolean insertSuccess= databaseHandler.insertBook(document);
	    	if(insertSuccess) {
	    		Alert alert=new Alert(AlertType.INFORMATION);
	    		alert.setContentText("Book Title Added Successfully");
	    		alert.setHeaderText(null);
	    		alert.showAndWait();
	    	}else {
	    		Alert alert=new Alert(AlertType.ERROR);
	    		alert.setContentText("Could not add a Book at this time. Please check if the book already exists or try again later!!");
	    		alert.setHeaderText(null);
	    		alert.showAndWait(); 		
	    	}

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	this.author.clear();
    	this.bookId.clear();
    	this.bookName.clear();
    	this.publisher.clear();

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler=DatabaseHandler.getInstance();
	}
	
	public void inflateUI(BookModel model){
		
		isEditMode=Boolean.TRUE;
		bookId.setText(model.getBookId());
		bookName.setText(model.getBookTitle());
		author.setText(model.getAuthor());
		author.setText(model.getPublisher());
		
		bookId.setEditable(false);
		
		
	}
    

}
