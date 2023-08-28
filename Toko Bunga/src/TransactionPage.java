import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import connection.Connect;
import model.Transaction;
import model.User;

public class TransactionPage extends Application{
	
	Connect connect = Connect.getInstance();
	
	Scene scene;
	BorderPane bp;
	
	TableView transactionTable;
	
	Vector<Transaction> transactions;
	
	public TransactionPage() {
		initialize();
		build();
	}
	

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("admin page");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize() {
		bp = new AdminTemplatePage();
		scene = new Scene(bp, 1000, 600);
		
		transactions = new Vector<Transaction>();
		transactionTable = new TableView();
		
		createTable();
	}
	
	public void build() {
		bp.setCenter(transactionTable);
	}
	
	public void createTable() {
		int bpWidth = (int) bp.getWidth();
		TableColumn<Transaction, String> idCol = new TableColumn("transactionID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
		idCol.setMinWidth(bpWidth/8);

		TableColumn<Transaction, String> buyerCol = new TableColumn("buyer");
		buyerCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		buyerCol.setMinWidth(bpWidth/8);

		TableColumn<Transaction, String> itemNameCol = new TableColumn("Item Name");
		itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		itemNameCol.setMinWidth(bpWidth/8);
		
		TableColumn<Transaction, String> itemDescCol = new TableColumn("Item Description");
		itemDescCol.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
		itemDescCol.setMinWidth(bpWidth/8);

		TableColumn<Transaction, Integer> priceCol = new TableColumn("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setMinWidth(bpWidth/8);

		TableColumn<Transaction, Integer> qtyCol = new TableColumn("Quantity");
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		qtyCol.setMinWidth(bpWidth/8);

		TableColumn<Transaction, Integer> totPriceCol = new TableColumn("TotalPrice");
		totPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		totPriceCol.setMinWidth(bpWidth/8);

		TableColumn<Transaction, String> transactionDateCol = new TableColumn("Transaction Date");
		transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
		transactionDateCol.setMinWidth(bpWidth/8);
		
		transactionTable.getColumns().addAll(idCol, buyerCol, itemNameCol, itemDescCol, priceCol, qtyCol, totPriceCol, transactionDateCol);
		transactionTable.setMaxHeight(600);
		transactionTable.setMaxWidth(1000);

		refreshTable();
	}
	
	private void getData() {
		transactions.removeAllElements();

		int productNum=0;

		//Select from DB
		String query = "SELECT tr.transactionID, us.username, pr.productName, pr.color, pr.price, pr.price*td.quantity AS totalPrice, td.quantity, tr.transactionDate "
				+ "FROM transaction tr JOIN transactiondetail td ON tr.transactionID = td.transactionID "
				+ "JOIN user us ON us.userID = tr.userID "
				+ "JOIN product pr ON pr.productID = td.productID;";
		connect.rs = connect.execQuery(query);
		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String transactionID = rs.getString(1);
				String username = rs.getString(2);
				String itemName = rs.getString(3);
				String itemDescription = rs.getString(4);
				Integer price = rs.getInt(5);
				Integer quantity = rs.getInt(6);
				Integer totalPrice = rs.getInt(7);
				String transactionDate = rs.getString(8);
				transactions.add(new Transaction(transactionID, username, itemName, itemDescription, price, quantity, totalPrice, transactionDate));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void refreshTable() {
		getData();
		ObservableList<Transaction> transactionObs = FXCollections.observableArrayList(transactions);
		transactionTable.setItems(transactionObs);
	}

}
