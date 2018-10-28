package library.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;

import library.database.DateSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
public class IssueBookModel {
	
	private String bookId;
	private String memberId;
	@JsonSerialize(using=DateSerializer.class)
	@JsonDeserialize(using=DateDeserializer.class)
	@JsonFormat(locale = "hu", shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "IST")
	private Date date;
	private int renewCount;
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	@JsonSerialize(using=DateSerializer.class)
	@JsonDeserialize(using=DateDeserializer.class)
	public Date getDate() {
		return date;
	}
	@JsonSerialize(using=DateSerializer.class)
	@JsonDeserialize(using=DateDeserializer.class)
	public void setDate(Date date) {
		this.date = date;
	}
	public int getRenewCount() {
		return renewCount;
	}
	public void setRenewCount(int renewCount) {
		this.renewCount = renewCount;
	}

}
