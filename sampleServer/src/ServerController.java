
import data.ClientStatus;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ServerController {
	@FXML
	private TableView<ClientStatus> clientTable;
	@FXML
	private TableColumn<ClientStatus, String> ipColumn;
	@FXML
	private TableColumn<ClientStatus, String> hostNameColumn;
	@FXML
	private TableColumn<ClientStatus, String> statusColumn;

	private final ObservableList<ClientStatus> clients = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
		hostNameColumn.setCellValueFactory(new PropertyValueFactory<>("hostName"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		clientTable.setItems(clients);
	}

	public void updateClientStatus(String ip, String hostName, String status) {
		try {
			Platform.runLater(() -> {
				for (ClientStatus c : clients) {
					if (c.getIp().equals(ip)) {
						c.setStatus(status);
						clientTable.refresh();
						return;
					}
				}
				clients.add(new ClientStatus(ip, hostName, status));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}