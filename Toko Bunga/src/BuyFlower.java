import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Product;
import model.Transaction;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import connection.Connect;

public class BuyFlower extends Application implements EventHandler<ActionEvent>{

	Connect connect = Connect.getInstance();
	String tempId=null, notification=null;
	int itemQuantity=0, quantityInCart=0;

	Scene scene;
	BorderPane bp;

	TableView itemTable;
	GridPane formPane, centerBox;
	FlowPane genderPane, btnPane;

	Text itemIdLbl, itemNameLbl, itemDescLbl, priceLbl, quantityLbl;
	TextField itemIdField, itemNameField, itemDescField;
	Spinner<Integer> priceSpinner, quantitySpinner;

	Button buyBtn, clearBtn;
	Vector<Product> products;

	public BuyFlower() {
		initialize();
		build();
		style();
		addAction();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("user page");
		stage.show();
	}

	public void initialize() {
		bp = new CustomerTemplatePage();
		formPane = new GridPane();
		centerBox = new GridPane();
		scene = new Scene(bp, 1000, 600);

		products = new Vector<Product>();
		itemTable = new TableView();

		createTable();

		itemIdLbl = new Text("Product ID");
		itemNameLbl = new Text("Product name");
		itemDescLbl = new Text("Color");
		priceLbl = new Text("Price");
		quantityLbl = new Text("Quantity");

		itemIdField = new TextField();
		itemIdField.setDisable(true);
		itemNameField = new TextField();
		itemNameField.setDisable(true);
		itemDescField = new TextField();
		itemDescField.setDisable(true);

		priceSpinner = new Spinner<Integer>(0, Integer.MAX_VALUE, 0, 1000);
		priceSpinner.setDisable(true);
		quantitySpinner = new Spinner<Integer>(0, Integer.MAX_VALUE, 0);
		quantitySpinner.setDisable(true);

		buyBtn = new Button("Buy Flower");
		clearBtn = new Button("Clear Form");
		btnPane = new FlowPane(buyBtn, clearBtn);
	}

	public void build() {

		formPane.add(itemIdLbl, 0, 0);
		formPane.add(itemIdField, 1, 0);

		formPane.add(itemNameLbl, 0, 1);
		formPane.add(itemNameField, 1, 1);

		formPane.add(itemDescLbl, 0, 2);
		formPane.add(itemDescField, 1, 2);

		formPane.add(priceLbl, 0, 3);
		formPane.add(priceSpinner, 1, 3);

		formPane.add(quantityLbl, 0, 4);
		formPane.add(quantitySpinner, 1, 4);

		formPane.add(btnPane, 0, 5, 2, 1);
		formPane.add(clearBtn, 0, 6, 2, 1);

		centerBox.add(itemTable, 0, 0);
		centerBox.add(formPane, 1, 0);

		bp.setCenter(centerBox);

	}

	public void style() {
		formPane.setHgap(10);
		formPane.setVgap(10);
		formPane.setPadding(new Insets(20));
	}

	public void addAction() {
		if(itemIdField.getText().equals("")) {
			clearBtn.setDisable(false);
			buyBtn.setDisable(false);
		}
		clearBtn.setOnAction(this);
		buyBtn.setOnAction(this);
	}

	public void createTable() {
		TableColumn<Product, String> idCol = new TableColumn("Product ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
		idCol.setMinWidth(120);

		TableColumn<Product, String> nameCol = new TableColumn("Product Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
		nameCol.setMinWidth(120);

		TableColumn<Product, String> descCol = new TableColumn("Color");
		descCol.setCellValueFactory(new PropertyValueFactory<>("color"));
		descCol.setMinWidth(120);

		TableColumn<Product, Integer> priceCol = new TableColumn("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setMinWidth(120);

		TableColumn<Product, Integer> qtyCol = new TableColumn("Quantity");
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		qtyCol.setMinWidth(120);

		itemTable.getColumns().addAll(idCol, nameCol, descCol, priceCol, qtyCol);
		itemTable.setMinHeight(570);
		itemTable.setMaxHeight(570);
		itemTable.setOnMouseClicked(tableMouseEvent());

		refreshTable();
	}

	private EventHandler<MouseEvent> tableMouseEvent(){
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				TableSelectionModel<Product> tableSelectionModel = itemTable.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				Product product = tableSelectionModel.getSelectedItem();

				itemIdField.setText(product.getProductID());
				itemNameField.setText(product.getProductName());
				itemDescField.setText(product.getColor());
				priceSpinner.getValueFactory().setValue(product.getPrice());
				priceSpinner.setDisable(true);
				itemQuantity = product.getQuantity();

				quantitySpinner.setDisable(false);

				tempId = product.getProductID();
			}
		};
	}

	private void getData() {
		products.removeAllElements();

		int productNum=0;

		//Select from DB
		String query = "SELECT * FROM product WHERE is_delete = ?";

		PreparedStatement ps = connect.prepareStatement(query);
		try {
			ps.setInt(1, 0);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Integer quantity = rs.getInt(6);
				if(quantity > 0) {
					String id = rs.getString(1);
					String itemName = rs.getString(3);
					String itemDesc = rs.getString(4);
					Integer price = rs.getInt(5);
					products.add(new Product(id,itemName, itemDesc, price, quantity));					
				}else {
					System.out.println("item quantity is 0");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshTable() {
		getData();
		ObservableList<Product> itemObs = FXCollections.observableArrayList(products);
		itemTable.setItems(itemObs);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == buyBtn) {
			quantityCheck();
		}

	}

	public void quantityCheck() {
		int selectedQty = quantitySpinner.getValue();
		String query = "";
		PreparedStatement ps;

		if (itemQuantity < selectedQty) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText(
					"The item you ordered with the itemID (" + tempId + ") is out of stock");
			alert.show();


		} else {
			//INSERT ke dtbs
			LocalDate dateObj = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String transactionDate = dateObj.format(formatter);

			String transactionID = generateID();
			query = "INSERT INTO transaction VALUES ('" + transactionID + "', '" + LoginPage.currUser.getUserId() + "', '"
					+ transactionDate + "')";
			connect.execUpdate(query);
			query = "INSERT INTO transactiondetail VALUES ('" + transactionID + "', '" + tempId + "', '"
					+ selectedQty + "')";
			connect.execUpdate(query);


			//UPDATE quantity
			query = "UPDATE product SET quantity = ? WHERE productID = ?";
			ps = connect.prepareStatement(query);
			try {
				ps.setInt(1, itemQuantity - selectedQty);
				ps.setString(2, tempId);
				ps.execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//Alert
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("SUCCESS");
			alert.setContentText("Successfully bought flower with product id ("
					+ tempId + "), click OK to continue");
			alert.setHeight(400);
			alert.show();

		} 
		refreshTable();
	}



	public String generateID() {
		String lastId="", nextId="";

		//Select from DB
		String query = "SELECT * FROM transaction";
		PreparedStatement ps = connect.prepareStatement(query);

		Vector<Transaction> transactions = new Vector<>();
		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String transactionID = rs.getString(1);
				String userID = rs.getString(2);
				String date = rs.getString(3);
				transactions.add(new Transaction(transactionID, userID, date));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(!transactions.isEmpty()) {
			lastId = transactions.get(transactions.size()-1).getTransactionID();
			nextId = lastId.substring(2,5);

			User.setCount(Integer.valueOf(nextId)+1);
		}
		String id="TR";
		int x = Integer.parseInt(nextId)+1;

		if(x < 10) {
			id += "00";
		}else if(x >= 10 && x<100) {
			id += "0";
		}
		id+=Integer.toString(x);

		return id;
	}
}

