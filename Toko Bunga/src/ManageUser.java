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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

public class ManageUser extends Application implements EventHandler<ActionEvent>{

	Connect connect = Connect.getInstance();

	String notification = null;

	Scene scene;
	BorderPane bp;
	
	Image i;
	ImageView iv;

	TableView userTable;
	GridPane formPane, centerBox;
	FlowPane genderPane, btnPane;

	Text titleLbl, userIdLbl, unameLbl, passLbl, emailLbl, phoneNumberLbl, ageLbl, genderLbl;
	TextField userIdField, unameField, passField, emailField, phoneNumberField;
	Spinner<Integer> ageSpinner;
	RadioButton maleBtn, femaleBtn;
	ToggleGroup genderToggle;
	Button updateBtn, deleteBtn;

	Vector<User> users;

	public ManageUser() {
		initialize();
		build();
		style();
		addAction();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Manage Staff Page");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	public void initialize() {
		bp = new AdminTemplatePage();
		formPane = new GridPane();
		centerBox = new GridPane();
		scene = new Scene(bp, 1000, 600);

		i = new Image("file:flower-logo.png");
		iv = new ImageView(i);
		iv.setFitWidth(250);
		iv.setPreserveRatio(true);
		
		users = new Vector<User>();
		userTable = new TableView();
		createTable();

		titleLbl = new Text("Register");
		userIdLbl = new Text("User ID");
		unameLbl = new Text("Username");
		passLbl = new Text("Password");
		emailLbl = new Text("Email");
		phoneNumberLbl = new Text("Phone Number");
		ageLbl = new Text("Age");
		genderLbl = new Text("Gender");

		userIdField = new TextField();
		userIdField.setDisable(true);

		unameField = new TextField();
		unameField.setDisable(true);

		passField = new TextField();
		passField.setDisable(true);

		emailField = new TextField();
		phoneNumberField = new TextField();

		ageSpinner = new Spinner<Integer>(15,70,16);
		maleBtn = new RadioButton("Male");
		femaleBtn = new RadioButton("Female");
		genderPane = new FlowPane(maleBtn, femaleBtn);

		genderToggle = new ToggleGroup();
		genderToggle.getToggles().addAll(maleBtn, femaleBtn);

		updateBtn = new Button("Update User");
		deleteBtn = new Button("Delete User");
		btnPane = new FlowPane(updateBtn, deleteBtn);
		btnPane.setHgap(10);
	}

	public void build() {
		formPane.add(iv, 0, 0, 2, 1);
		
		formPane.add(userIdLbl, 0, 1);
		formPane.add(userIdField, 1, 1);

		formPane.add(unameLbl, 0, 2);
		formPane.add(unameField, 1, 2);

		formPane.add(passLbl, 0, 3);
		formPane.add(passField, 1, 3);

		formPane.add(emailLbl, 0, 4);
		formPane.add(emailField, 1, 4);

		formPane.add(phoneNumberLbl, 0, 5);
		formPane.add(phoneNumberField, 1, 5);

		formPane.add(ageLbl, 0, 6);
		formPane.add(ageSpinner, 1, 6);

		formPane.add(genderLbl, 0, 7);
		formPane.add(genderPane, 1, 7);

		formPane.add(btnPane, 0, 8, 2, 1);

		centerBox.add(userTable, 0, 0);
		centerBox.add(formPane, 1, 0);

		bp.setCenter(centerBox);
	}

	public void style() {
		bp.setStyle("-fx-background-color: white");
		formPane.setPadding(new Insets(20));
		formPane.setHgap(10);
		formPane.setVgap(10);
		genderPane.setHgap(10);

		centerBox.getColumnConstraints().add(new ColumnConstraints(700));
		centerBox.getColumnConstraints().add(new ColumnConstraints(300));
		
		updateBtn.setPrefWidth(120);
		deleteBtn.setPrefWidth(120);
		updateBtn.setStyle("-fx-background-color: Pink;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
		deleteBtn.setStyle("-fx-background-color: Green; -fx-text-fill: White;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
	
		btnPane.setAlignment(Pos.CENTER);
		btnPane.setHgap(10);

	}

	public void addAction() {
		updateBtn.setOnAction(this);
		deleteBtn.setOnAction(this);
	}

	public void createTable() {
		TableColumn<User, String> idCol = new TableColumn("userID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
		idCol.setMinWidth(85);

		TableColumn<User, String> usernameCol = new TableColumn("username");
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		usernameCol.setMinWidth(85);

		TableColumn<User, String> passwordCol = new TableColumn("password");
		passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
		passwordCol.setMinWidth(85);

		TableColumn<User, String> emailCol = new TableColumn("email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		emailCol.setMinWidth(85);

		TableColumn<User, String> phoneNumberCol = new TableColumn("phone number");
		phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		phoneNumberCol.setMinWidth(85);

		TableColumn<User, Integer> ageCol = new TableColumn("age");
		ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
		ageCol.setMinWidth(85);

		TableColumn<User, String> genderCol = new TableColumn("gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
		genderCol.setMinWidth(85);
		
		TableColumn<User, String> roleCol = new TableColumn("role");
		roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
		roleCol.setMinWidth(85);

		userTable.getColumns().addAll(idCol, usernameCol, passwordCol, emailCol, phoneNumberCol, ageCol, genderCol, roleCol);
		userTable.setMinHeight(570);
		userTable.setMaxHeight(600);
		userTable.setOnMouseClicked(tableMouseEvent());

		refreshTable();
	}

	private EventHandler<MouseEvent> tableMouseEvent(){
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				TableSelectionModel<User> tableSelectionModel = userTable.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				User user = tableSelectionModel.getSelectedItem();

				userIdField.setText(user.getUserId());
				unameField.setText(user.getUsername());
				passField.setText(user.getPassword());
				emailField.setText(user.getEmail());
				phoneNumberField.setText(user.getPhoneNumber());
				ageSpinner.getValueFactory().setValue(user.getAge());
				if(user.getGender().equalsIgnoreCase("Male")) {
					genderToggle.selectToggle(maleBtn);
				}else {
					genderToggle.selectToggle(femaleBtn);
				}
			}
		};
	}

	private void getData() {
		users.removeAllElements();

		int productNum=0;

		//Select from DB
		String query = "SELECT * FROM user";
		PreparedStatement ps = connect.prepareStatement(query);

		try {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String id = rs.getString(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String gender = rs.getString(4);
				String email = rs.getString(5);
				String phoneNumber = rs.getString(6);
				Integer age = rs.getInt(7);
				String role = rs.getString(8);
				users.add(new User(id,username, password, gender, email, phoneNumber, age, role));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void refreshTable() {
		String lastId="", nextId="";

		getData();
		ObservableList<User> userObs = FXCollections.observableArrayList(users);
		userTable.setItems(userObs);

		if(!users.isEmpty()) {
			lastId = users.get(users.size()-1).getUserId();
			nextId = lastId.substring(2,5);

			User.setCount(Integer.valueOf(nextId)+1);
		}	
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == updateBtn) {
			if(userIdField.getText().isEmpty()) {
				Alert updateAlert = new Alert(AlertType.INFORMATION);
				updateAlert.setHeaderText("UPDATE FAILED");
				updateAlert.setContentText("Make sure user is selected!");
				updateAlert.showAndWait();
			}else {
				if(validateUpdate()) {
					String query = "UPDATE user\n"
							+ "SET  email = ?, phoneNumber = ?, age = ?, gender = ?\n"
							+ "WHERE userID =?";
					PreparedStatement ps = connect.prepareStatement(query);
					System.out.println("hi1");
					try {
						System.out.println("hi2");
						ps.setString(1, emailField.getText());
						ps.setString(2, phoneNumberField.getText());
						ps.setInt(3, ageSpinner.getValue());
						ps.setString(4, genderSelected());
						ps.setString(5, userIdField.getText());
						ps.execute();
					} catch (Exception e2) {
						e2.printStackTrace();
					}

					Alert updateAlert = new Alert(AlertType.INFORMATION);
					updateAlert.setHeaderText("UPDATE SUCCESS");
					updateAlert.setContentText("Click OK to continue");
					updateAlert.showAndWait();

				}else {
					System.out.println("Update failed");

					Alert updateAlert = new Alert(AlertType.INFORMATION);
					updateAlert.setHeaderText("UPDATE FAILED");
					updateAlert.setContentText(notification);
					updateAlert.setHeight(400);
					updateAlert.showAndWait();
				}
			}
		}

		if(event.getSource() == deleteBtn) {

			if(userIdField.getText().equals("")) {

				Alert deleteAlert = new Alert(AlertType.INFORMATION);
				deleteAlert.setHeaderText("DELETE ERROR");
				deleteAlert.setContentText("Make sure User is selected!");
				deleteAlert.showAndWait();

			}else {
				String query = "DELETE FROM user\n"
						+ "WHERE userID = ?";
				PreparedStatement ps = connect.prepareStatement(query);

				try {
					ps.setString(1, userIdField.getText());
					ps.execute();
				}catch (Exception e2) {
					e2.printStackTrace();
				}

				Alert deleteAlert = new Alert(AlertType.INFORMATION);
				deleteAlert.setHeaderText("DELETE SUCCESS");
				deleteAlert.setContentText("Click OK to continue");
				deleteAlert.showAndWait();				
			}
		}
		refreshTable();
		refreshAllValue();
	}

	public String genderSelected() {
		String gender ="";
		String x = genderToggle.getSelectedToggle().toString();
		gender = x.substring(x.indexOf("'")+1, x.length()-1);
		return gender;
	}

	public boolean validateUpdate() {
		boolean validated=false, filled=false, emailV=false, phoneNumberV=false, ageV=false, genderV=false;

		if(emailField.getText().equals("") && phoneNumberField.getText().equals("")) {
			filled = true;
		}else {
			notification = "Please make sure all field are filled!";
		}

		if(emailField.getText().contains("@") && !emailField.getText().startsWith("@") && emailField.getText().endsWith(".com")) {
			emailV = true;
		}else {
			notification = "Email must contain \"@\", must not start with\"@\" and must ends with \".com\"!";
		}

		if(phoneNumberField.getText().length()>=9 && phoneNumberField.getText().length()<=12) {
			phoneNumberV = true;
		}else {
			notification = "Phone Number must be between 9-12 characters!";
		}

		if(ageSpinner.getValue()>=17 && ageSpinner.getValue()<=60) {
			ageV = true;
			
		}else {
			notification = "Age must be between 17-60!";
		}
		String gender = genderSelected();
		if(gender.equals("Male") || gender.equals("Female")) {
			genderV = true;
		}else {
			notification = "Gender must be selected, either ‘Male’ or ‘Female’";
		}

		if(emailV && phoneNumberV && ageV && genderV) {
			validated = true;
		}

		return validated;
	}

	public void refreshAllValue() {
		userIdField.setText("");
		unameField.setText("");
		passField.setText("");
		emailField.setText("");
		phoneNumberField.setText("");
		ageSpinner.getValueFactory().setValue(16);
		maleBtn.setSelected(false);
		femaleBtn.setSelected(false);
	}

}
