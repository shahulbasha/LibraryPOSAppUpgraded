package library.database;

import static com.mongodb.client.model.Filters.eq;

import java.util.Observable;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoServerException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import library.model.BookModel;
import library.model.MemberModel;

public class DatabaseHandler {
	static DatabaseHandler handler=null;
	private MongoClient mongoClient=new MongoClient("localhost",27017);
	
	private DatabaseHandler() {
		setUpBookCollection();
		setUpMemberCollection();
		setUpIssueBookCollection();
	}
	
	public static DatabaseHandler getInstance() {
		if(handler==null) {
			return new DatabaseHandler();
		}
		return handler;
	}
	
	
	public MongoClient getMongoConnection() {
		return this.mongoClient;
	}
	
	//only database used for this entire application
	public MongoDatabase getMongoDatabase() {
		return this.mongoClient.getDatabase("Library");
	}
	
	//return the collection if it exists or create it and return it
	public MongoCollection<Document> setUpBookCollection() {
		MongoCollection<Document> collection = this.getMongoDatabase().getCollection("book");
		return collection;
	}
	
	public MongoCollection<Document> setUpMemberCollection() {
		MongoCollection<Document> collection = this.getMongoDatabase().getCollection("member");
		return collection;
	}
	
	public MongoCollection<Document> setUpIssueBookCollection(){
		MongoCollection<Document> collection = this.getMongoDatabase().getCollection("issueBook");
		return collection;
	}
/*	public void retrieveMemberById(String memberId){
		MongoCollection<Document> memberCollection = setUpMemberCollection();
		Document doc = memberCollection.find(eq("memberId",memberId)).first();
		doc.toJson();
		
	}
	
	public void retrieveBookById(String bookId){
		MongoCollection<Document> setUpBookCollection = setUpBookCollection();
	}*/
	
	//method checks for availability of a particular document in a collection
	public boolean executeAction(String bookTitle) {
		MongoCollection<Document> collection=setUpBookCollection();
		for (Document doc :  collection.find()) {
			if(doc.containsKey(bookTitle)) {
				return true;
			}
		}
		return false;
	}
	
	public void executeQuery(String bookTitle) {
		
	}

	//get the book collection and insert the received record
	public boolean insertBook(Document document) {
		MongoCollection<Document> bookCollection = setUpBookCollection();
		try {
			bookCollection.createIndex(new Document("bookId",1),new IndexOptions().unique(true));
			bookCollection.insertOne(document);
			System.out.println("Collection Inserted"+bookCollection.count());
		} catch (MongoWriteException e) {
			System.out.println(e.getMessage());
			return false;
		}catch(MongoServerException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean issueBook(Document document) {
		try {
		MongoCollection<Document> collection = setUpIssueBookCollection();
		collection.createIndex(Indexes.ascending("bookId","memberId"));
		collection.insertOne(document);
	} catch (MongoWriteException e) {
		System.out.println(e.getMessage());
		return false;
	}catch(MongoServerException e) {
		e.printStackTrace();
	}
	return true;
	}

	public boolean insertMember(Document doc) {
		try {
		//	System.out.println("About to insert");
			MongoCollection<Document> memberCollection = setUpMemberCollection();
			memberCollection.createIndex(new Document("memberId", 1), new IndexOptions().unique(true));
			memberCollection.insertOne(doc);
		//	System.out.println("Collection Inserted"+memberCollection.count());
		} catch (MongoWriteException e) {
			System.out.println(e.getMessage());
			return false;
		}catch(MongoServerException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	public boolean deleteBook(BookModel model) {
		MongoCollection<Document> bookCollection = setUpBookCollection();
		DeleteResult deleteOne = bookCollection.deleteOne(new Document("bookId", model.getBookId()));	
		
		if(deleteOne.getDeletedCount()==1) {
			return true;
		}
		return false;
		
	}
	
	
	public boolean deleteMember(MemberModel model) {
		MongoCollection<Document> memberCollection = setUpMemberCollection();
		DeleteResult deleteOne = memberCollection.deleteOne(eq("memberId",model.getMemberId()));
		
		if(deleteOne.getDeletedCount()==1) {
			return true;
		}
		return false;
		
	}

	
	public boolean isBookAlreadyIssued(BookModel model) {
		MongoCollection<Document> issueBookCollection = setUpIssueBookCollection();
		long count = issueBookCollection.count(eq("bookId",model.getBookId()));
		System.out.println(count+" that much");
		if(count>0) {
			return true;
		}
		return false;
	}

	public boolean handleEditOperation(BookModel bookModel) {
		MongoCollection<Document> bookCollection = setUpBookCollection();
		//bookCollection.updateMany(eq("bookId",bookModel.getBookId()),new Document("bookTitle",bookModel.getBookTitle(),"author",bookModel.getAuthor(),"publisher",bookModel.getPublisher()),new UpdateOptions().upsert(true));
		
		//UpdateResult updateMany = bookCollection.updateMany(eq("bookId",bookModel.getBookId()),new Document("bookTitle",bookModel.getBookTitle()).append("author", bookModel.getAuthor()).append("publisher", bookModel.getPublisher()));
		
		UpdateResult updateMany = bookCollection.updateMany(eq("bookId",bookModel.getBookId()),Updates.combine(Updates.set("bookTitle", bookModel.getBookTitle()),Updates.set("author", bookModel.getAuthor()),Updates.set("publisher", bookModel.getPublisher())));
		
		if(updateMany.getModifiedCount()==1) {
			return true;
		}
		return false;
		
	}

	public boolean editMember(MemberModel memberModel) {
		MongoCollection<Document> memberCollection = setUpMemberCollection();
		UpdateResult updateMany = memberCollection.updateMany(eq("memberId",memberModel.getMemberId()), Updates.combine(Updates.set("memberName", memberModel.getMemberName()),Updates.set("phoneNo", memberModel.getPhoneNo()),Updates.set("emailId", memberModel.getEmailId())));
		if(updateMany.getModifiedCount()==1) {
			return true;
		}
		return false;
		
	}
	
	
	public ObservableList<PieChart.Data> getBookStatistics(){
		ObservableList<PieChart.Data> bookChart=FXCollections.observableArrayList();
		try {
			long bookCount=setUpBookCollection().count();
			long issuedBookCount=setUpIssueBookCollection().count();
		bookChart.add(new PieChart.Data("Total Books "+"-"+bookCount, bookCount));
		bookChart.add(new PieChart.Data("Issued Books "+"-"+issuedBookCount,issuedBookCount ));
		}
		catch(Exception e) {
			
		}
		return bookChart;
		
	}
	
	public ObservableList<PieChart.Data> getMemberStatistics(){
		
		ObservableList<PieChart.Data> memberChart=FXCollections.observableArrayList();
		try {
			long memberCount=setUpMemberCollection().count();
			long issueCount=setUpIssueBookCollection().count();
			memberChart.add(new PieChart.Data("Total Members "+"-"+memberCount, memberCount));
			memberChart.add(new PieChart.Data("Members with Books "+"-"+issueCount, issueCount));
		}
		catch(Exception e) {
			
		}
		return memberChart;
	}
}
