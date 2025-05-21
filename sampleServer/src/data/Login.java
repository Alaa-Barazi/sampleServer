package data;

import java.io.Serializable;

/**
 * Class for saving login info (username & password)
 */
public class Login implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username, passwrod;

	/**
	 * @param username loggedIn username
	 * @param passwrod loggedIn password
	 */
	public Login(String username, String passwrod) {
		this.username = username;
		this.passwrod = passwrod;
	}

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return password
	 */
	public String getPasswrod() {
		return passwrod;
	}

}
