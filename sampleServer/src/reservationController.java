import java.sql.Connection;
import java.util.ArrayList;

import data.Order;
import data.UpdateOrderDetails;

/**
 * class that will control all reservation related methods
 */

public class reservationController {
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

	public reservationController() {
		this.getDBConnection();
	}

	/**
	 * method for receiving all existing orders from the DB
	 * 
	 * @return ArrayList of orders
	 */
	public ArrayList<Order> getAllOrders() {
		ArrayList<Order> allOrders = null;
		try {
			allOrders = mysqlConnection.printOrders(conn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return allOrders;
	}

	/**
	 * method for updating specific order
	 * @param order object with new values for the order
	 * @return string that will assign status of the operation
	 */
	public String updateOrder(UpdateOrderDetails order) {
		String returnMessageToClient = "No Operation was done";
		try {
			int updatedRows = mysqlConnection.updateParkingSpaceANDOrderDate(conn, order.getOrder_number(),
					order.getParking_space(), order.getOrder_date());
			if (updatedRows == 1)
				returnMessageToClient = "Successfully updated";
			else if (updatedRows == -1)
				returnMessageToClient = "Invalid order number (there isn't any order with this order number)";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return returnMessageToClient;
	}
}
