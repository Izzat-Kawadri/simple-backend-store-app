package entity;

public class Account {
	private String userName;
	private String address;

	
	public Account(String userName, String address) {
		super();
		this.userName = userName;
		this.address = address;
	}
	public Account() {}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
