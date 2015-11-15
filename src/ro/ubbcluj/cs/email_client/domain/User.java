package ro.ubbcluj.cs.email_client.domain;

public class User {

	private String username;
	private String password;
	private int id=-1;
	
	public User(String username, String password) {
		this.username=username;
		this.password=password;
		
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public static User copy(User user) {
		User userCopy=new User(user.getUsername(),user.getPassword());
		return userCopy;
	}

	public void setID(int id) {
		this.id=id;
		
	}

	public Integer getID() {
		return id;
	}
	
}
