package library.database;

import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoServerException;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.InsertOneOptions;

public class DatabaseHandler {
	
	private MongoClient mongoClient=new MongoClient("localhost",27017);
	
	public DatabaseHandler() {
		setUpBookCollection();
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

}
