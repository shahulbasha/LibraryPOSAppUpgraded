package library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MemberModel {

	private String memberId;
	private String memberName;
	private String phoneNo;
	private String emailId;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public MemberModel(String memberId, String memberName, String phoneNo, String emailId) {
		super();
		this.memberId = memberId;
		this.memberName = memberName;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
	}
	public MemberModel() {
		super();
	}
	
	
}
