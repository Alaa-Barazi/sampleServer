import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import data.Login;
import data.ResponseWrapper;

public class LoginController {
	private mysqlConnection instance = null;
	Connection conn = null;

	public void getDBConnection() {
		try {
			instance = mysqlConnection.getInstance();
			conn = instance.getConnection();
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
		}
	}

	public LoginController() {
		this.getDBConnection();
	}

	/**
	 * check if the subscriber entered the right username and password
	 * 
	 * @param con
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean validSubscriber(ResponseWrapper response) {
		Login loginDetails = (Login) response.getData();
		Statement stmt;
		boolean exists = false;
		try {
			System.out.println("Server " + loginDetails.getUsername() + "  " + loginDetails.getPasswrod());
			stmt = conn.createStatement();
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM `subscriber` WHERE BINARY username=(?) AND BINARY password=(?)");
			ps.setString(1, loginDetails.getUsername());
			ps.setString(2, loginDetails.getPasswrod());
			ResultSet rs = ps.executeQuery();
			exists = rs.next();
			System.out.println("Exists? " + exists);
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exists;
	}

	/**
	 * method for checking if any user exists (correct username & password) if so
	 * retreive type of user( subscriber, parking attendant, manager)
	 * 
	 * @param username username to check
	 * @param password password to check
	 * @return type of user (subscriber, parking attendant, manager) if doesn't
	 *         exist - invalid
	 */
	public String validateLogin(String username, String password) {
		String sqlWorker = "SELECT * FROM workers WHERE BINARY username=? AND BINARY password=?";
		String sqlSubscriber = "SELECT * FROM subscribers WHERE BINARY username=? AND BINARY password=?";
		String sqlManager = "SELECT * FROM managers WHERE BINARY username=? AND BINARY password=?";
		try {
			PreparedStatement psWorker = conn.prepareStatement(sqlWorker);
			psWorker.setString(1, username);
			psWorker.setString(2, password);
			ResultSet rsWorker = psWorker.executeQuery();
			if (rsWorker.next()) {
				return "worker";
			}

			PreparedStatement psSub = conn.prepareStatement(sqlSubscriber);
			psSub.setString(1, username);
			psSub.setString(2, password);
			ResultSet rsSub = psSub.executeQuery();
			if (rsSub.next()) {
				return "subscriber";
			}

			PreparedStatement psMng = conn.prepareStatement(sqlManager);
			psMng.setString(1, username);
			psMng.setString(2, password);
			ResultSet rsMng = psMng.executeQuery();
			if (rsMng.next()) {
				return "manager";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "invalid";
	}

}
