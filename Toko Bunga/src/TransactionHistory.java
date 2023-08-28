import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import connection.Connect;
import model.Product;
import model.Transaction;

public class TransactionHistory extends Application implements EventHandler<ActionEvent>{

	Connect connect = Connect.getInstance();
	String tempId = null;

	Scene scene;
	BorderPane borderContainer;
	TableView transHistoryTable;

	Vector <Transaction> transHistory;

	public TransactionHistory() {
		initialize();
		build();
	}

	@Override
	public void start(Stage arg0) throws Exception {
		arg0.setScene(scene);
		arg0.setTitle("Transaction History Page");
		arg0.setResizable(false);
		arg0.show();
	}

	public void initialize() {
		borderContainer = new CustomerTemplatePage();
		scene = new Scene(borderContainer, 1000, 600);

		transHistory = new Vector<Transaction>();
		transHistoryTable = new TableView();

		createTable();
	}

	public void build() {
		borderContainer.setCenter(transHistoryTable);
	}

	public void createTable() {
		int bpWidth = (int) borderContainer.getWidth();
		TableColumn<Transaction, String> idCol = new TableColumn("transactionID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
		idCol.setMinWidth(bpWidth/7);

		TableColumn<Transaction, String> itemNameCol = new TableColumn("Item Name");
		itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		itemNameCol.setMinWidth(bpWidth/7);

		TableColumn<Transaction, String> itemDescCol = new TableColumn("Item Description");
		itemDescCol.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
		itemDescCol.setMinWidth(bpWidth/7);

		TableColumn<Transaction, Integer> priceCol = new TableColumn("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setMinWidth(bpWidth/7);

		TableColumn<Transaction, Integer> qtyCol = new TableColumn("Quantity");
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		qtyCol.setMinWidth(bpWidth/7);

		TableColumn<Transaction, Integer> totPriceCol = new TableColumn("TotalPrice");
		totPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		totPriceCol.setMinWidth(bpWidth/7);

		TableColumn<Transaction, String> transactionDateCol = new TableColumn("Transaction Date");
		transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
		transactionDateCol.setMinWidth(bpWidth/7);

		transHistoryTable.getColumns().addAll(idCol, itemNameCol, itemDescCol, priceCol, qtyCol, totPriceCol, transactionDateCol);
		transHistoryTable.setMaxHeight(600);
		transHistoryTable.setMaxWidth(1000);

		refreshTable();
	}

	public void getData() {
		transHistory.removeAllElements();

		int productNum=0;

		//Select from DB
		String query = "SELECT tr.transactionID, pr.productName, pr.color, pr.price, td.quantity, pr.price*td.quantity AS totalPrice, tr.transactionDate "
				+ "FROM transaction tr JOIN transactiondetail td ON tr.transactionID = td.transactionID "
				+ "JOIN user us ON us.userID = tr.userID "
				+ "JOIN product pr ON pr.productID = td.productID \n"
				+ "WHERE tr.userID = ?";
		PreparedStatement ps = connect.prepareStatement(query);

		try {
			ps.setString(1, LoginPage.currUser.getUserId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String transactionID = rs.getString(1);
				String itemName = rs.getString(2);
				String itemDescription = rs.getString(3);
				Integer price = rs.getInt(4);
				Integer quantity = rs.getInt(5);
				Integer totalPrice = rs.getInt(6);
				String transactionDate = rs.getString(7);
				transHistory.add(new Transaction(transactionID, itemName, itemDescription, price, quantity, totalPrice, transactionDate));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshTable() {
		getData();
		ObservableList<Transaction> transactionObs = FXCollections.observableArrayList(transHistory);
		transHistoryTable.setItems(transactionObs);
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
