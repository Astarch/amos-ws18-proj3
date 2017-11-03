package backend.datamodel;

public class User {
	private String mail; //key value
	private String surename;
	private String firstname;
	private String password;
	
	
	public User(){}

	public User(String surename, String password, String firstname, String mail) {
		this.mail = mail; //key value
		this.surename = surename;
		this.firstname = firstname;
		this.password = password;
		
	}

	public String getSurename() {
		return surename;
	}

	public void setSurename(String surename) {
		this.surename = surename;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [mail=" + mail + ", surename=" + surename + ", firstname=" + firstname + ", password=" + password
				+ "]";
	}
	
	
}
