import java.sql.Connection;
import java.util.ArrayList;
import data.Order;
import data.UpdateOrderDetails;
import data.Order;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer {
	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	private mysqlConnection instance = null;
	Connection conn = null;
	private reservationController reservationController;
	private final ServerController controller;

	public EchoServer(int port, ServerController controller) {
		super(port);
		reservationController = new reservationController();
		this.controller = controller;
		// this.getDBConnection();
	}

	// Client connection methods
	@Override
	protected void clientConnected(ConnectionToClient client) {
		String ip = client.getInetAddress().getHostAddress();
		String host = client.getInetAddress().getHostName();
		controller.updateClientStatus(ip, host, "Connected");
	}

	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("Client disconnected");
		String ip = client.getInetAddress().getHostAddress();
		String host = client.getInetAddress().getHostName();
		controller.updateClientStatus(ip, host, "Disconnected");
	}

	// Instance methods ************************************************
	public void getDBConnection() {
		try {
			instance = mysqlConnection.getInstance();
			conn = instance.getConnection();
		} catch (Exception e) {
			System.out.println("Error connecting to DB");
		}
	}

	/**
	 * method for handling client message (by contacting with other controllers)
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		try {
			//check if order with id exists
			if(msg instanceof String && msg.toString().startsWith("OrderID"))
			{
				int ID;
				String str;
				str = msg.toString();
				String[] result = str.split("\\s");
				ID = Integer.parseInt(result[1]);
				Order ord = reservationController.getOrderByID(ID);
				if(ord!=null)
				{
					client.sendToClient(ord);
				}
				else {
					client.sendToClient(null);
				}
				
			}
			// get all existing orders
			if (msg.equals("showAllOrders")) {
				ArrayList<Order> orders = reservationController.getAllOrders();
				client.sendToClient(orders);
			}
			// update details of order
			if (msg instanceof UpdateOrderDetails) {
				UpdateOrderDetails order = (UpdateOrderDetails) msg;
				client.sendToClient(reservationController.updateOrder(order));
			}
			if (msg.equals("connect")) {
				String ip = client.getInetAddress().getHostAddress();
				String host = client.getInetAddress().getHostName();
				controller.updateClientStatus(ip, host, "Connected");
			}

			if (msg.equals("disconnect")) {
				String ip = client.getInetAddress().getHostAddress();
				String host = client.getInetAddress().getHostName();
				controller.updateClientStatus(ip, host, "Disconnected");
			}
			// send connection info
			if (msg.equals("clientDetails")) {
				String IP = client.getInetAddress().getHostAddress();
				String hostName = client.getInetAddress().getHostName();
				String str = "Client connected:\n" + "IP Address:" + IP + "\nHost Name: " + hostName;

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
	

	// In handle message from client - take care of the msg according to its type
	// (arrayList of students and so on)
	// 2 functions of require executing query from the DB
	// function for connected Client query IP, host address and connection status
	// Order class with relevant properties

}
