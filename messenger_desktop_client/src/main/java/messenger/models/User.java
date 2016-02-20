package messenger.models;

public class User {

	private String username;
	private String password;
	private int id=-1;
	public User(){

	}
	
	public User(String username, String password) {
		this.username=username;
		this.password=password;
		
	}
	public User(String username) {
		this.username=username;
	}

	@Override
	public String toString() {
		return username;
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
