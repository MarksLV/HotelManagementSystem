package application;

public class Admins {

	// Administrator variables
	private int id;
	private String username;
	private String password;

	public Admins(int id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;;
	}

	// Getter and setter method for ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	// Getter and setter method for User name
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	// Getter and setter method for Password
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
