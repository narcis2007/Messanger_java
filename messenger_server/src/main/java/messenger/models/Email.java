package messenger.models;

import java.util.HashMap;
import java.util.Map;

public class Email {

	private String content;
	private String title;
	private String sender;
	private String receiver;
	Status status;
	private int id=-1;

	public Email(String title, String content, String sender, String receiver) {

		this.title = title;
		this.content = content;
		this.sender = sender;
		this.receiver = receiver;
		this.status=Status.SENT;
	}

	public Email(String title, String content, Integer integer) {

	}

	public Email(String title, String content, String sender) {

	}

	public static Email copy(Email email){
		if (email==null)
			return null;
		Email emailCopy=new Email(email.getTitle(),email.getContent(),email.getSender());
		emailCopy.setID(email.getID());
		emailCopy.setStatus(email.getStatus());
		emailCopy.setReceiver(email.getReceiver());
		return emailCopy;
	}
	
	public Map<String,String> toMap(){
		Map<String,String> map=new HashMap<>();
		map.put("content",content);
		map.put("title",title);
		map.put("sender",String.valueOf(sender));
		map.put("receiver",String.valueOf(receiver));
		map.put("status",status.toString());
		map.put("id",String.valueOf(id));
		return map;
		
	}
	
//	public Email(Map<String,String> map){
//		content=map.get("content");
//		title=map.get("title");
//		sender= Integer.valueOf(map.get("sender"));
//		receiver=Integer.valueOf(map.get("receiver"));
//		status=Status.valueOf(map.get("status"));
//		id=Integer.valueOf(map.get("id"));
//
//	}

	public void setStatus(Status status) {
		this.status=status;
		
	}
	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
	public int getID() {
		return id;
	}	
	
	public void setID(int id) {
		this.id=id;
	}

	public String getSender() {
		return sender;
	}

	public void setTitle(String title) {
		this.title=title;
		
	}

	public void setContent(String content) {
		this.content=content;
		
	}	
	
	public Status getStatus(){
		return this.status;
	}
	public void setReceiver(String receiver) {
		this.receiver=receiver;
		
	}
	public String getReceiver() {
		return receiver;
	}
}
