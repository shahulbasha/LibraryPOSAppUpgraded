package library.ui.main;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.database.DatabaseHandler;
import library.model.BookModel;
import library.model.IssueBookModel;
import library.model.MemberModel;
import library.util.LibraryUtil;

public class MainController implements Initializable{
	
    @FXML
    private StackPane rootPane;
    
    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private MenuItem close;
    
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
    
    @FXML
    private JFXTextField bookSubmitRenew;
    
    @FXML
    private JFXButton renewButton;

    @FXML
    private JFXButton submissionButton;

/*    @FXML
    private ListView<String> issueRenewListView;*/
    
    @FXML
    private Text memberNameHolder;

    @FXML
    private Text memberEmailHolder;

    @FXML
    private Text memberContactHolder;

    @FXML
    private Text bookNameHolder;

    @FXML
    private Text bookAuthorHolder;

    @FXML
    private Text bookPublisherHolder;

    @FXML
    private Text issueDateHolder;

    @FXML
    private Text daysHolder;

    @FXML
    private Text fineHolder;

    
    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;
    
    @FXML
    private HBox submissionDataContainer;
    
    boolean isReadyForSubmission=false;

    
    @FXML
    void loadMemberInfo(ActionEvent event) {
    	ObjectMapper mapper=new ObjectMapper();
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
			memberId.clear();
    	}
    	}
    }
    
    
    @FXML
    void loadBookInfo(ActionEvent event) {
    	ObjectMapper mapper=new ObjectMapper();
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
			bookId.clear();
			
    	}
    	}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JFXDepthManager.setDepth(book_info, 1);
		JFXDepthManager.setDepth(member_info, 1);
		
		initDrawer();
		
	}
	

	

    @FXML
    void loadIssueBook(ActionEvent event) {

    
    	String bookIdValue=bookId.getText();
    	String memberIdValue=memberId.getText();
    	
    	//if(bookName.getText().equalsIgnoreCase("No Such Book Available")|| memberName.getText().equalsIgnoreCase("No Such Member Available"))
    	if(!bookIdValue.isEmpty() && !memberIdValue.isEmpty()) {
    	IssueBookModel ibModel=new IssueBookModel();
    	ibModel.setBookId(bookIdValue);
    	ibModel.setMemberId(memberIdValue);
    	ibModel.setDate(new Date());
    	ibModel.setRenewCount(0);

    	
    	 DatabaseHandler dBhandler = DatabaseHandler.getInstance();
    	 
    	Alert alert=new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Issue Confirmation");
    	alert.setContentText("Are you sure you want to issue the book "+bookIdValue+" to "+memberIdValue+" ?");
    	Optional<ButtonType> response = alert.showAndWait();
    	
    	ObjectMapper mapper=new ObjectMapper();


    	
    	if(response.get()==ButtonType.OK) {
    	//	MongoCollection<Document> issueBookCollection = dBhandler.setUpIssueBookCollection();
    		MongoCollection<Document> bookCollection = dBhandler.setUpBookCollection();
    		try {
				Document doc = Document.parse(mapper.writeValueAsString(ibModel));
				boolean issueBook = dBhandler.issueBook(doc);
		    	if(issueBook) {
		    		//set Is available to false once book issue
		    		bookCollection.updateOne(eq("bookId",bookIdValue),new Document("$set",new Document("available",false)));
		    		Alert alert1=new Alert(AlertType.INFORMATION);
		    		alert1.setContentText("Book Issued Successfully");
		    		alert1.setHeaderText(null);
		    		alert1.showAndWait();
		    	}else {
		    		Alert alert2=new Alert(AlertType.ERROR);
		    		alert2.setContentText("Could not issue a Book at this time. Please try again later!!");
		    		alert2.setHeaderText(null);
		    		alert2.showAndWait(); 		
		    	}
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
    		
    	}else {
    		Alert alert3=new Alert(AlertType.ERROR);
    		alert3.setContentText("Issue operation Cancelled!!");
    		alert3.setHeaderText(null);
    		alert3.showAndWait(); 
    		
    	}
    	
    	}
    	else {
    		//Alert
    		
    	}

    }
    
    @FXML
    void loadBookSubmitRenewInfo(ActionEvent event) {
    	isReadyForSubmission=false;
    	
    	clearEntries();
        ObservableList<String> issueRenewData=FXCollections.observableArrayList();
        
    	String bookId=bookSubmitRenew.getText();
    	MongoCollection<Document> issueBookCollection = DatabaseHandler.getInstance().setUpIssueBookCollection();
    	MongoCollection<Document> memberCollection = DatabaseHandler.getInstance().setUpMemberCollection();
    	MongoCollection<Document> bookCollection = DatabaseHandler.getInstance().setUpBookCollection();
    	
    	System.out.println("input"+bookId);

    	ObjectMapper mapper=new ObjectMapper();


    	try {
        	Document doc = issueBookCollection.find(eq("bookId",bookId)).first();
        	Document doc1 = bookCollection.find(eq("bookId",bookId)).first();
        	
        	if(doc1!=null) {
        	
    			if(doc!=null) {
			IssueBookModel issueBook = mapper.readValue(doc.toJson(), IssueBookModel.class);
			Document doc2 = memberCollection.find(eq("memberId",issueBook.getMemberId())).first();
        	


			MemberModel memberModel = mapper.readValue(doc2.toJson(),MemberModel.class);
			BookModel bookModel = mapper.readValue(doc1.toJson(),BookModel.class);
			
			memberNameHolder.setText(memberModel.getMemberName());
			memberEmailHolder.setText(memberModel.getEmailId());
			memberContactHolder.setText(memberModel.getPhoneNo());
			bookAuthorHolder.setText(bookModel.getAuthor());
			bookPublisherHolder.setText(bookModel.getPublisher());
			bookNameHolder.setText(bookModel.getBookTitle());
			

			LocalDate localDate=LocalDate.now();
			LocalDate date=issueBook.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Period period=Period.between(localDate, date);
			
			issueDateHolder.setText(date.toString());
			daysHolder.setText(String.valueOf(period.getDays()));
			fineHolder.setText("Not Supported Yet");
			
/*			issueRenewData.add("Issue Date and Time is "+issueBook.getDate().toString());
			issueRenewData.add("Renew Count : "+issueBook.getRenewCount());
			issueRenewData.add("BOOK INFORMATION :");
			issueRenewData.add("	Book ID : "+bookModel.getBookId());
			issueRenewData.add("	Book Name : "+bookModel.getBookTitle());
			issueRenewData.add("	Author : "+bookModel.getAuthor());
			issueRenewData.add("	Publisher : "+bookModel.getPublisher());
			issueRenewData.add("MEMBER INFORMATION :");
			issueRenewData.add("	Member ID : "+issueBook.getMemberId());
			issueRenewData.add("	Member Name : "+memberModel.getMemberName());
			issueRenewData.add("	Member Contact : "+memberModel.getEmailId());*/
			
		//	issueRenewListView.getItems().setAll(issueRenewData);
			isReadyForSubmission=true;
			disableControls(false);
			submissionDataContainer.setOpacity(1);
        	}
			else {
				BoxBlur blur=new BoxBlur(3, 3, 3);
				
				JFXDialogLayout layout=new JFXDialogLayout();
				JFXButton button=new JFXButton("Okay");
				JFXDialog dialog=new JFXDialog(rootPane, layout, JFXDialog.DialogTransition.TOP);

			//	button.getStyleClass().add("dialog-button");
				button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
					dialog.close();
				});
				
				layout.setHeading(new Text("This book is not assigned to any member"));
				layout.setActions(button);
				button.getStyleClass().add("dialog-button");
				dialog.setOnDialogClosed((JFXDialogEvent jfxEvent) -> {
					rootAnchorPane.setEffect(null);
				});
				dialog.show();
				rootAnchorPane.setEffect(blur);
			}
        	}
        	else {
        		
        		BoxBlur blur=new BoxBlur(3, 3, 3);
        		
				JFXDialogLayout layout=new JFXDialogLayout();
				
				JFXDialog dialog=new JFXDialog(rootPane, layout, JFXDialog.DialogTransition.TOP);
				JFXButton button=new JFXButton("Okay");
				//button.getStyleClass().add("dialog-button");
				button.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent)->{
					dialog.close();
				});
				
				layout.setHeading(new Text("No such book exists in our records"));
				layout.setActions(button);
				button.getStyleClass().add("dialog-button");
				dialog.setOnDialogClosed((JFXDialogEvent jfxEvent) -> {
					rootAnchorPane.setEffect(null);
				});
				dialog.show();
				rootAnchorPane.setEffect(blur);
        		
        	}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

    	

    }
    
    private void clearEntries() {
    	memberNameHolder.setText("");
    	memberContactHolder.setText("");
    	memberEmailHolder.setText("");
    	bookAuthorHolder.setText("");
    	bookNameHolder.setText("");
    	bookPublisherHolder.setText("");
    	issueDateHolder.setText("");
    	daysHolder.setText("");
    	fineHolder.setText("");
		
    	disableControls(true);
    	submissionDataContainer.setOpacity(0);
	}
    
    private void disableControls(boolean isDisable) {
    	
    	if(isDisable) {
    		renewButton.setDisable(true);
    		submissionButton.setDisable(true);
    	}
    	else {
    		renewButton.setDisable(false);
    		submissionButton.setDisable(false);
    	}
    }


	@FXML
    void loadOnSubmission(ActionEvent event) {
    	System.out.println(isReadyForSubmission);
    	if(isReadyForSubmission) {
    		String bookId=bookSubmitRenew.getText();
    		
        	Alert alert=new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Issue Confirmation");
        	alert.setContentText("Are you sure you want to submit the book "+bookId);
        	Optional<ButtonType> response = alert.showAndWait();
        	
        	if(response.get()==ButtonType.OK) {

    		MongoCollection<Document> issueBookCollection = DatabaseHandler.getInstance().setUpIssueBookCollection();
    		MongoCollection<Document> bookCollection = DatabaseHandler.getInstance().setUpBookCollection();
    		try {
    		issueBookCollection.deleteOne(eq("bookId",bookId));
    		bookCollection.updateOne(eq("bookId",bookId), new Document("$set" ,new Document("available",true)));
    		Alert alert2=new Alert(AlertType.INFORMATION);
    		alert2.setTitle("Book Submission");
    		alert2.setContentText("Book Submitted successfully");
    		alert2.showAndWait();

    		}
    		catch(Exception e) {
        		Alert alert3=new Alert(AlertType.ERROR);
        		alert3.setTitle("Book Submission");
        		alert3.setContentText("Book Submission failed. Please try again later");
        		alert3.showAndWait();
    		}
    		isReadyForSubmission=false;
    	//	issueRenewListView.getItems().clear();
    		bookSubmitRenew.clear();
    		
    	}

    	}
    	else {
    		Alert alert1=new Alert(AlertType.ERROR);
    		alert1.setTitle("Book Submission");
    		alert1.setContentText("Please select a book to submit and try again later");
    		alert1.showAndWait();
    		return;
    	}
    	
    }
    
    @FXML
    void loadRenew(ActionEvent event) {
    	
		if(isReadyForSubmission) {
    	Alert alert=new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Renew Confirmation");
    	alert.setContentText("Are you sure you want to renew the book "+bookSubmitRenew.getText());
    	Optional<ButtonType> response = alert.showAndWait();
    	ObjectMapper mapper=new ObjectMapper();
    	
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        String formattedDate = sdf.format(new Date());
    	
    	if(response.get()==ButtonType.OK) {
    		MongoCollection<Document> issueBookCollection = DatabaseHandler.getInstance().setUpIssueBookCollection();
    		Document doc = issueBookCollection.find(eq("bookId",bookSubmitRenew.getText())).first();
    		IssueBookModel model;
			try {
				model = mapper.readValue(doc.toJson(), IssueBookModel.class);
				issueBookCollection.updateOne(eq("bookId",bookSubmitRenew.getText()),new Document("$set",new Document("date",formattedDate).append("renewCount", model.getRenewCount()+1)));
				
	    		Alert alert2=new Alert(AlertType.INFORMATION);
	    		alert2.setTitle("Book Renew");
	    		alert2.setContentText("Book Renewed successfully");
	    		alert2.showAndWait();
			} catch (JsonParseException e) {
				e.printStackTrace();
        		Alert alert3=new Alert(AlertType.ERROR);
        		alert3.setTitle("Book Renew");
        		alert3.setContentText("Book Renewal failed. Please try again later");
        		alert3.showAndWait();
			} catch (JsonMappingException e) {
				e.printStackTrace();
        		Alert alert3=new Alert(AlertType.ERROR);
        		alert3.setTitle("Book Renew");
        		alert3.setContentText("Book Renewal failed. Please try again later");
        		alert3.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
        		Alert alert3=new Alert(AlertType.ERROR);
        		alert3.setTitle("Book Renew");
        		alert3.setContentText("Book Renewal failed. Please try again later");
        		alert3.showAndWait();
			}
    		
    		
    	}
		}
		else {
    		Alert alert1=new Alert(AlertType.ERROR);
    		alert1.setTitle("Book Renewal");
    		alert1.setContentText("Please select a book to renew");
    		alert1.showAndWait();
    		return;
			
		}
    	
    }
    
    
    
    // Menu bar Functions
    @FXML
    void handleClose(ActionEvent event) {
    	((Stage)rootPane.getScene().getWindow()).close();
    }
    
    
    @FXML
    void handleMenuAddBook(ActionEvent event) {
    	
    	LibraryUtil.loadWindow(getClass().getResource("/library/ui/addbook/LibraryAddBook.fxml"),"Add New Book",null);
    	
    }

    @FXML
    void handleMenuAddMember(ActionEvent event) {
    	LibraryUtil.loadWindow(getClass().getResource("/library/ui/addmember/LibraryAddMember.fxml"),"Add New Member",null);

    }
    
    @FXML
    void handleFullscreenToggle(ActionEvent event) {
	    Stage stage=(Stage)rootPane.getScene().getWindow();
	    stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    void handleMenuAbout(ActionEvent event) {

    	
    }
    

	
    @FXML
    void handleMenuViewBook(ActionEvent event) {
    	LibraryUtil.loadWindow(getClass().getResource("/library/ui/listbook/LibraryBookList.fxml"),"View Book List",null);
    }

    @FXML
    void handleMenuViewMember(ActionEvent event) {
    	LibraryUtil.loadWindow(getClass().getResource("/library/ui/listmember/LibraryMemberList.fxml"),"View Member List",null);

    }
    
    private void initDrawer() {
    	try {
		VBox toolbar=FXMLLoader.load(getClass().getResource("/library/ui/main/toolbar/Toolbar.fxml"));
		drawer.setSidePane(toolbar);
		
		//HamburgerSlideCloseTransition task=new HamburgerSlideCloseTransition(hamburger);
		HamburgerSlideCloseTransition task=new HamburgerSlideCloseTransition(hamburger);
		task.setRate(-1);
		
		hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
			
			@Override
			public void handle(Event event) {
				task.setRate(task.getRate()*-1);
				task.play();
				if(drawer.isClosed()) {
					drawer.open();
				}
				else {
					drawer.close();
				}
			}
		});
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
