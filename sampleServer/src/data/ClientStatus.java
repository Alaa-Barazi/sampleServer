package data;

import javafx.beans.property.SimpleStringProperty;

public class ClientStatus {
	private final SimpleStringProperty ip;
	private final SimpleStringProperty hostName;
	private final SimpleStringProperty status;

	public ClientStatus(String ip, String hostName, String status) {
		this.ip = new SimpleStringProperty(ip);
		this.hostName = new SimpleStringProperty(hostName);
		this.status = new SimpleStringProperty(status);
	}

	public String getIp() {
		return ip.get();
	}

	public String getHostName() {
		return hostName.get();
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}
}