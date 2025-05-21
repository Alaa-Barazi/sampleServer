import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Scanner;

import data.Order;

public class mysqlConnection {
	private static mysqlConnection instance;
	private Connection conn;

//Singleton implementation
	public static mysqlConnection getInstance() {
		if (instance == null) {
			instance = new mysqlConnection();
		}
		return instance;
	}

	public Connection getConnection() {
		return conn;
	}

	private mysqlConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/parkingdb?serverTimezone=Asia/Jerusalem", "root",
					"Aa123456");
			System.out.println("SQL connection succeed");
			/*
			 * LocalDate localDate = LocalDate.of(2024, 4,28); Date today =
			 * Date.valueOf(localDate); updateParkingSpaceANDOrderDate(conn, 15, 20, today);
			 */

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Method to check if specific order exists
	 * 
	 * @param con
	 * @param orderNumber number of order to check if it exists
	 * @return true if order exists otherwise false
	 */
	public static boolean checkValidOrderNumber(Connection con, int orderNumber) {
		Statement stmt;
		boolean exists = false;
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM `order` WHERE order_number=(?)");

			ps.setInt(1, orderNumber);
			ResultSet rs = ps.executeQuery();
			exists = rs.next(); // true if result has at least one row
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return exists;
	}

//Method to update parking space and orderDate for order with number orderNumber
	/**
	 * @param con
	 * @param orderNumber  number of order to update it's values
	 * @param parkingSpace new value of parkingSpace
	 * @param orderDate    new value of orderDate
	 * @return number of rows that were updated after executing the query
	 */
	public static int updateParkingSpaceANDOrderDate(Connection con, int orderNumber, int parkingSpace,
			Date orderDate) {
		int numOfUpdatedRows = 0;
		Statement stmt;
		try {
			if (checkValidOrderNumber(con, orderNumber)) {
				stmt = con.createStatement();
				PreparedStatement ps = con
						.prepareStatement("UPDATE `order` SET parking_space=(?),order_date=(?) WHERE order_number=(?)");

				ps.setInt(1, parkingSpace);
				ps.setDate(2, orderDate);
				ps.setInt(3, orderNumber);
				numOfUpdatedRows = ps.executeUpdate();
			} else {
				// no order exists with this order number
				numOfUpdatedRows = -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numOfUpdatedRows;
	}

	/**
	 * method to return all existing orders from the DB
	 * 
	 * @param con
	 * @return method returns ArrayList containing all existing orders from the
	 *         table in DB
	 */
	public static ArrayList<Order> printOrders(Connection con) {

		ArrayList<Order> allOrders = new ArrayList<>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `order`;");
			while (rs.next()) {
				// Save the values
				Order ord = new Order(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getInt(4), rs.getInt(5),
						rs.getDate(6));
				allOrders.add(ord);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allOrders;
	}

	/**
	 * method to return specific order based on givin ID
	 * 
	 * @param con
	 * @param orderID orderID to get the relevant order
	 * @return order object with the given orderID
	 */
	public static Order returnOrderByID(Connection con, int orderID) {
		Statement stmt;
		Order ord = null;
		try {
			stmt = con.createStatement();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM `order` WHERE order_number=(?)");

			ps.setInt(1, orderID);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ord = new Order(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getInt(4), rs.getInt(5), rs.getDate(6));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ord;
	}

	
	

}
