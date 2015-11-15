package ro.ubbcluj.cs.email_client.domain;
public class Email {

	private String content;
	private String title;
	private String owner;
	private String receiver;
	Status status;
	private int id=-1;

	public Email(String title, String content,String owner) {
		this.title=title;
		this.content=content;
		this.owner=owner;
		this.status=Status.UNSENT;
	}
	public static Email copy(Email email){
		if (email==null)
			return null;
		Email emailCopy=new Email(email.getTitle(),email.getContent(),email.getOwner());
		emailCopy.setID(email.getID());
		emailCopy.setStatus(email.getStatus());
		emailCopy.setReceiver(email.getReceiver());
		return emailCopy;
	}

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

	public String getOwner() {
		return owner;
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
