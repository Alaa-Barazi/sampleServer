import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.Order;
import data.UpdateOrderDetails;
import ocsf.server.*;

public class EchoServer extends AbstractServer {
	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	private mysqlConnection instance = null;
	Connection conn = null;

	public EchoServer(int port) {
		super(port);
		this.getDBConnection();
	}

	// Instance methods ************************************************
	public void getDBConnection() {
		try {
			instance = mysqlConnection.getInstance();
			;
			conn = instance.getConnection();
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
		}
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		try {
			// get all existing orders
			if (msg.equals("showAllOrders")) {
				ArrayList<Order> orders = mysqlConnection.printOrders(conn);
				client.sendToClient(orders);
			}
			// update details of order
			if (msg instanceof UpdateOrderDetails) {
				UpdateOrderDetails order = (UpdateOrderDetails) msg;
				int updatedRows = mysqlConnection.updateParkingSpaceANDOrderDate(conn, order.getOrder_number(),
						order.getParking_space(), order.getOrder_date());
				if (updatedRows == 1)
					client.sendToClient("Successfully updated");
				else if (updatedRows == -1)
					client.sendToClient("Invalid order number (there isn't any order with this order number)");
			}
			// send connection info
			if (msg.equals("clientDetails")) {
				String IP = client.getInetAddress().getHostAddress();
				String hostName = client.getInetAddress().getHostName();
				String str = "Client connected:\n" + "IP Address:" + IP + "\nHost Name: " + hostName
						+ "\nConnection status: " + (client.isAlive() ? "Connected" : "Disconnected");

				client.sendToClient(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	/*
	 * private void sentToClient(ConnectionToClient client) { try {
	 * client.sendToClient("message recieved from you!!"); } catch (Exception ex) {
	 * System.out.println("Errir sending to single client"); } }
	 */

	// Method to pull information from the Database
	/*
	 * protected void getAllColumns() { try { mysqlConnection.printOrders(conn); }
	 * catch (Exception ex) {
	 * System.out.println("error printing order's information"); } }
	 */

	// In handle message from client - take care of the msg according to its type
	// (arrayList of students and so on)
	// 2 functions of require executing query from the DB
	// function for connected Client query IP, host address and connection status
	// Order class with relevant properties

}
