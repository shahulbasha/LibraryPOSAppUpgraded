package library.ui.listbook;

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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.database.DatabaseHandler;
import library.model.BookModel;
import library.ui.addbook.LibraryAddBookController;
import library.ui.main.MainController;
import library.util.LibraryUtil;

public class LibraryBookListController implements Initializable  {
	@FXML
    private AnchorPane rootPane;
	
    @FXML
    private TableView<BookModel> bookModel;

    @FXML
    private TableColumn<BookModel, String> bookIdColumn;

    @FXML
    private TableColumn<BookModel, String> bookTitleColumn;

    @FXML
    private TableColumn<BookModel, String> authorColumn;

    @FXML
    private TableColumn<BookModel, String> publisherColumn;

    @FXML
    private TableColumn<BookModel, String> availabilityColumn;
    
    DatabaseHandler databaseHandler=DatabaseHandler.getInstance();
    ObservableList<BookModel> bookList=FXCollections.observableArrayList();
    ObjectMapper mapper=new ObjectMapper();
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCol();
		
		loadData();
	}
	
	
	private void loadData() {
    	bookList.clear();
		MongoCollection<Document> bookCollection = databaseHandler.setUpBookCollection();
		for (Document book : bookCollection.find()) {
			try {
				BookModel readValue = mapper.readValue(book.toJson(), BookModel.class);
				bookList.add(readValue);
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
		
		bookModel.setItems(bookList);
	}


	private void initCol() {
		bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
		bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

		
	}
	
	
    @FXML
    void deleteBookfromView(ActionEvent event) {
    	
    	BookModel selectedItem = bookModel.getSelectionModel().getSelectedItem();
    	

    	if(!DatabaseHandler.getInstance().isBookAlreadyIssued(selectedItem)) {
    		System.out.println("Inside");
    	if(selectedItem!=null) {
    		
    		Alert alert=new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Delete Book");
    		alert.setContentText("Are you sure you want to delete the book "+selectedItem.getBookTitle());
    		
    		Optional<ButtonType> response = alert.showAndWait();
    		if(response.get()==ButtonType.OK) {
    			if(DatabaseHandler.getInstance().deleteBook(selectedItem)) {
    	    		Alert alert2=new Alert(AlertType.INFORMATION);
    	    		alert2.setTitle("Delete Success");
    	    		alert2.setContentText("Book Deleted Successfully");
    	    		alert2.showAndWait();
    	    		
    	    		bookList.remove(selectedItem);
    			}
    			else {
    	    		Alert alert3=new Alert(AlertType.ERROR);
    	    		alert3.setTitle("Delete Failure");
    	    		alert3.setContentText("Please try delting after sometime");
    	    		alert3.showAndWait();
    			}
    			
    		}else {
        		Alert alert1=new Alert(AlertType.INFORMATION);
        		alert1.setTitle("Cancel");
        		alert1.setContentText("Deletion Cancelled");
        		alert1.showAndWait();
    		}
    	}
    	else {
    		Alert alert=new Alert(AlertType.ERROR);
    		alert.setTitle("Select Book");
    		alert.setContentText("No Book Selected");
    		alert.showAndWait();
    	}
    	}
    	else {
    		System.out.println("Outside");
    		Alert alert=new Alert(AlertType.ERROR);
    		alert.setTitle("Delete Book");
    		alert.setContentText("This book cannot be deleted as it is already issued");
    		alert.showAndWait();
    	}


    }
    
    

    @FXML
    void editBook(ActionEvent event) {
    	
    	BookModel selectedItem = bookModel.getSelectionModel().getSelectedItem();
    	
		try {

			FXMLLoader loader=new FXMLLoader(getClass().getResource("/library/ui/addbook/LibraryAddBook.fxml"));
			Parent root=loader.load();
			
			LibraryAddBookController controller =(LibraryAddBookController) loader.getController();
			controller.inflateUI(selectedItem);
			Stage stage=new Stage(StageStyle.DECORATED);
			Scene scene=new Scene(root);
			stage.setTitle("Edit Book");
			stage.setScene(scene);
			stage.show();
			
			stage.setOnCloseRequest((e)->{
				refreshView(new ActionEvent());
			});
			
			LibraryUtil.setStageIcon(stage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	


    }

    
    @FXML
    void refreshView(ActionEvent event) {

    	loadData();
    }
    
    
   

}
