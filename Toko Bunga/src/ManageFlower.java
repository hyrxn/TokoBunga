import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import connection.Connect;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Product;
import model.User;

public class ManageFlower extends Application implements EventHandler<ActionEvent>{

	Connect connect = Connect.getInstance();
	private String tempId = null;
	//, tempQty=null, tempPrc=null
	String generatedId=null;
	
	String notification="";

	Scene scene;
	BorderPane bp;

	TableView productTable;
	GridPane formPane, centerBox;
	FlowPane genderPane, btnPane;
	VBox btnBox;

	Text productIdLbl, addedByLbl, productNameLbl, colorLbl, priceLbl, quantityLbl, isDelLbl;
	TextField productIdField, addedByField, productNameField, colorField, isDelField;
	Spinner<Integer> priceSpinner, quantitySpinner;

	Button insertBtn, updateBtn, deleteBtn, clearBtn;

	Vector<Product> products;

	public ManageFlower() {
		initialize();
		build();
		style();
		addAction();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Manage Flower Page");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	public void initialize() {

		bp = new AdminTemplatePage();
		formPane = new GridPane();
		centerBox = new GridPane();
		scene = new Scene(bp, 1100, 600);

		products = new Vector<Product>();
		productTable = new TableView();

		createTable();

		productIdLbl = new Text("Product ID");
		addedByLbl = new Text("Added by Staff ID");
		productNameLbl = new Text("Product name");
		colorLbl = new Text("Color");
		priceLbl = new Text("Price");
		quantityLbl = new Text("Quantity");
		isDelLbl = new Text("Status");
		
		productIdField = new TextField();
		productIdField.setDisable(true);
		productIdField.setText(generateID());
		addedByField = new PasswordField();
		addedByField.setDisable(true);
		productNameField = new TextField();
		colorField = new TextField();
		isDelField = new TextField();
		isDelField.setDisable(true);

		priceSpinner = new Spinner<Integer>(0, Integer.MAX_VALUE, 0, 1000);
		quantitySpinner = new Spinner<Integer>(0, Integer.MAX_VALUE, 0);

		insertBtn = new Button("Insert Flower");
		updateBtn = new Button("Update Flower");
		deleteBtn = new Button("Delete Flower");
		clearBtn = new Button("Clear Form");
		btnPane = new FlowPane(insertBtn, updateBtn, deleteBtn);
		btnBox = new VBox();

	}
	
	public void build() {
		
		formPane.add(productIdLbl, 0, 0);
		formPane.add(productIdField, 1, 0);
		
		formPane.add(addedByLbl, 0, 1);
		formPane.add(addedByField, 1, 1);
		
		formPane.add(productNameLbl, 0, 2);
		formPane.add(productNameField, 1, 2);
		
		formPane.add(colorLbl, 0, 3);
		formPane.add(colorField, 1, 3);
		
		formPane.add(priceLbl, 0, 4);
		formPane.add(priceSpinner, 1, 4);
		
		formPane.add(quantityLbl, 0, 5);
		formPane.add(quantitySpinner, 1, 5);
		
		formPane.add(isDelLbl, 0, 6);
		formPane.add(isDelField, 1, 6);
		
		btnBox.getChildren().add(insertBtn);
		btnBox.getChildren().add(updateBtn);
		btnBox.getChildren().add(deleteBtn);
		btnBox.getChildren().add(clearBtn);
		
		formPane.add(btnBox, 0, 7, 2, 1);
		
		centerBox.add(productTable, 0, 0);
		centerBox.add(formPane, 1, 0);
		
		bp.setCenter(centerBox);
	}
	
	public void style() {
		bp.setStyle("-fx-background-color: white");
		formPane.setPadding(new Insets(50));
		formPane.setHgap(10);
		formPane.setVgap(10);

		centerBox.getColumnConstraints().add(new ColumnConstraints(700));
		centerBox.getColumnConstraints().add(new ColumnConstraints(400));
		
		insertBtn.setPrefWidth(Double.MAX_VALUE);
		updateBtn.setPrefWidth(Double.MAX_VALUE);
		deleteBtn.setPrefWidth(Double.MAX_VALUE);
		clearBtn.setPrefWidth(Double.MAX_VALUE);
		
		insertBtn.setPadding(new Insets(10));
		updateBtn.setPadding(new Insets(10));
		deleteBtn.setPadding(new Insets(10));
		clearBtn.setPadding(new Insets(10));
		
		btnBox.setMargin(updateBtn, new Insets(10,0,10,0));
		btnBox.setMargin(clearBtn, new Insets(10,0,0,0));
		insertBtn.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		updateBtn.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		deleteBtn.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		clearBtn.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		insertBtn.setCursor(Cursor.HAND);
		updateBtn.setCursor(Cursor.HAND);
		deleteBtn.setCursor(Cursor.HAND);
		clearBtn.setCursor(Cursor.HAND);
		insertBtn.setStyle("-fx-background-color: Pink;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
		updateBtn.setStyle("-fx-background-color: Pink;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
		deleteBtn.setStyle("-fx-background-color: Pink;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
		clearBtn.setStyle("-fx-background-color: Green; -fx-text-fill: White;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
	
	}
	
	public void addAction() {
		insertBtn.setOnAction(this);
		updateBtn.setOnAction(this);
		deleteBtn.setOnAction(this);
		clearBtn.setOnAction(this);
	}

	public void createTable() {
		TableColumn<Product, String> idCol = new TableColumn("Product ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
		idCol.setMinWidth(100);

		TableColumn<Product, String> nameCol = new TableColumn("Product Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
		nameCol.setMinWidth(100);

		TableColumn<Product, String> addedByCol = new TableColumn("Added By");
		addedByCol.setCellValueFactory(new PropertyValueFactory<>("addedBy"));
		addedByCol.setMinWidth(100);
		
		TableColumn<Product, String> colorCol = new TableColumn("Color");
		colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
		colorCol.setMinWidth(100);

		TableColumn<Product, Integer> priceCol = new TableColumn("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setMinWidth(100);

		TableColumn<Product, Integer> qtyCol = new TableColumn("Quantity");
		qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		qtyCol.setMinWidth(100);
		
		TableColumn<Product, String> isDelCol = new TableColumn("Status");
		isDelCol.setCellValueFactory(new PropertyValueFactory<>("isDel"));
		isDelCol.setMinWidth(100);

		productTable.getColumns().addAll(idCol, addedByCol, nameCol, colorCol, priceCol, qtyCol, isDelCol);
		productTable.setMinHeight(570);
		productTable.setMaxHeight(570);
		
		productTable.setOnMouseClicked(tableMouseEvent());

		refreshTable();
	}

	private EventHandler<MouseEvent> tableMouseEvent(){
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				TableSelectionModel<Product> tableSelectionModel = productTable.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				Product product = tableSelectionModel.getSelectedItem();

				productIdField.setText(product.getProductID());
				addedByField.setText(product.getAddedBy());
				productNameField.setText(product.getProductName());
				colorField.setText(product.getColor());
				priceSpinner.getValueFactory().setValue(product.getPrice());
				quantitySpinner.getValueFactory().setValue(product.getQuantity());
				isDelField.setText(product.getIsDel());
				
				tempId = product.getProductID();
			}
		};
	}
	
	private void getData() {
		products.removeAllElements();

		int productNum=0;

		//Select from DB
		String query = "SELECT * FROM product";
		PreparedStatement ps = connect.prepareStatement(query);

		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String id = rs.getString(1);
				String addedBy = rs.getString(2);
				String name = rs.getString(3);
				String color = rs.getString(4);
				Integer price = rs.getInt(5);
				Integer quantity = rs.getInt(6);
				Integer isDelete = rs.getInt(7);
				String isDel = "";
				if(isDelete == 1) {
					isDel = "deleted";
				}else {
					isDel = "available";
				}
				products.add(new Product(id, addedBy, name, color, price, quantity, isDel));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshTable() {
		String lastId="", nextId="";

		getData();
		ObservableList<Product> productsObs = FXCollections.observableArrayList(products);
		productTable.setItems(productsObs);

		if(!products.isEmpty()) {
			lastId = products.get(products.size()-1).getProductID();
			nextId = lastId.substring(2,5);

			Product.setCount(Integer.valueOf(nextId)+1);
		}	
	}
	
	public String generateID() {
		String lastId="", nextId="";

		//Select from DB
		String query = "SELECT * FROM product";
		PreparedStatement ps = connect.prepareStatement(query);

		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String id = rs.getString(1);
				String addedBy = rs.getString(2);
				String name = rs.getString(3);
				String color = rs.getString(4);
				Integer price = rs.getInt(5);
				Integer quantity = rs.getInt(6);
				Integer isDelete = rs.getInt(7);
				String isDel = "";
				if(isDelete == 1) {
					isDel = "deleted";
				}else {
					isDel = "available";
				}
				products.add(new Product(id, addedBy, name, color, price, quantity, isDel));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(!products.isEmpty()) {
			lastId = products.get(products.size()-1).getProductID();
			nextId = lastId.substring(2,5);

			User.setCount(Integer.valueOf(nextId)+1);
		}
		String id="PR";
		int x = Integer.parseInt(nextId)+1;

		if(x < 10) {
			id += "00";
		}else if(x >= 10 && x<100) {
			id += "0";
		}
		id+=Integer.toString(x);

		generatedId = id;

		return id;
	}

	@Override
	public void handle(ActionEvent event) {
		String productID = productIdField.getText();
		String addedBy = addedByField.getText();
		String productName = productNameField.getText();
		String color = colorField.getText();
		Integer price = priceSpinner.getValue();
		Integer quantity = quantitySpinner.getValue();
		Integer isDel;
		if(isDelField.getText().equals("deleted")) {
			isDel = 1;
		}else {
			isDel = 0;
		}
		
		if(event.getSource() == insertBtn) {
			productID = generatedId;
			if(validateItemData()) {
				String query = "INSERT INTO product VALUES(?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement st = connect.prepareStatement(query);
				
				try {
					st.setString(1, productID);
					st.setString(2, "admin");
					st.setString(3, productName);
					st.setString(4, color);
					st.setInt(5, price);
					st.setInt(6, quantity);
					st.setInt(7, 0);
					st.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Alert insertAlert = new Alert(AlertType.INFORMATION);
				insertAlert.setHeaderText("INSERT SUCCESS");
				insertAlert.setContentText("Press Enter to Continue");
				insertAlert.show();
			}else {
				Alert insertAlert = new Alert(AlertType.INFORMATION);
				insertAlert.setHeaderText("INSERT FAILED");
				insertAlert.setContentText(notification);
				insertAlert.setHeight(400);
				insertAlert.show();
			}
		}
		
		if(event.getSource() == updateBtn) {
			if(validateItemData()) {
				String query = "UPDATE product\n"
						+ "SET productID = ?, addedBy = ?, productName = ?, color = ?, price = ?, quantity = ?, is_delete = ?\n"
						+ "WHERE productID = ?";
				PreparedStatement ps = connect.prepareStatement(query);
				try {
					ps.setString(1, productID);
					ps.setString(2, addedBy);
					ps.setString(3, productName);
					ps.setString(4, color);
					ps.setInt(5, price);
					ps.setInt(6, quantity);
					ps.setInt(7, isDel);
					ps.setString(8, productID);
					ps.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Alert updateAlert = new Alert(AlertType.INFORMATION);
				updateAlert.setHeaderText("UPDATE SUCCESS");
				updateAlert.setContentText("Click OK to continue");
				updateAlert.show();
				
			}else {
				Alert updateAlert = new Alert(AlertType.INFORMATION);
				updateAlert.setHeaderText("UPDATE FAILED");
				updateAlert.setContentText(notification);
				updateAlert.setHeight(400);
				updateAlert.show();
			}
		}
		
		if(event.getSource() == deleteBtn) {
			if(productNameField.getText().equals("")) {
				
				Alert deleteAlert = new Alert(AlertType.INFORMATION);
				deleteAlert.setHeaderText("DELETE FAILED");
				deleteAlert.setContentText("Make sure Item is selected.");
				deleteAlert.showAndWait();
				
			}else {
				String query = "DELETE FROM product\n"
						+ "WHERE productID = ?";
				PreparedStatement ps = connect.prepareStatement(query);
				
				try {
					ps.setString(1, productID);
					ps.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}				
				
				Alert deleteAlert = new Alert(AlertType.INFORMATION);
				deleteAlert.setHeaderText("DELETE SUCCESS");
				deleteAlert.setContentText("Click OK to continue");
				deleteAlert.showAndWait();
			}
		}
		
		if(event.getSource() == clearBtn) {
			productIdField.setText(generateID());
			productNameField.setText("");
			colorField.setText("");
			priceSpinner.getValueFactory().setValue(0);
			quantitySpinner.getValueFactory().setValue(0);
		}
		
		refreshTable();
		
	}

	public boolean validateItemData() {
		boolean validated=false, selectedV=false, priceV=false, qtyV=false;
		
		String name = productNameField.getText(), desc = colorField.getText();
		int price = priceSpinner.getValue(), qty = quantitySpinner.getValue();
		
		if(!name.equals("")) {
			selectedV = true;
			if(price>0) {
				priceV = true;
			}else {
				notification="Price must be greater than 0!";
			}
			
			if(qty>0) {
				qtyV = true;
			}else {
				notification="Quantity must be greater than 0!";
			}
		}else {
			selectedV = false;
			notification = "Make sure item is selected!";
		}
		
		if(priceV && qtyV && selectedV) {
			validated = true;
		}
		
		return validated;
	}

}
