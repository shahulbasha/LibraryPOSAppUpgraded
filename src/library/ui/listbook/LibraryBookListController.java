package library.ui.listbook;

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
import library.model.BookModel;

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
    
    DatabaseHandler databaseHandler=new DatabaseHandler();
    ObservableList<BookModel> bookList=FXCollections.observableArrayList();
    ObjectMapper mapper=new ObjectMapper();
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCol();
		
		loadData();
	}
	
	
	private void loadData() {

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
		
		bookModel.getItems().setAll(bookList);
	}


	private void initCol() {
		bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
		bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

		
	}

}
