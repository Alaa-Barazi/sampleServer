import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import data.Login;
import data.ResponseWrapper;

public class subscriberController {
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

	public subscriberController() {
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
}
