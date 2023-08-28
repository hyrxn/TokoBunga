import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import connection.Connect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

public class RegisterPage extends Application implements EventHandler<ActionEvent>{
	
	Connect connect = Connect.getInstance();
	private String tempId = null;
	String regisError="", genderSelected="", generatedId;
	
	Image i;
	ImageView iv;
	
	Scene scene;
	BorderPane bp;
	GridPane formPane;
	FlowPane genderPane, btnPane;
	VBox headerBox;

	Text titleLbl, userIdLbl, unameLbl, passLbl, emailLbl, phoneNumberLbl, ageLbl, genderLbl;
	TextField userIdField, unameField, emailField, phoneNumberField;
	PasswordField passField;
	Spinner<Integer> ageSpinner;
	RadioButton maleBtn, femaleBtn;
	ToggleGroup genderToggle;
	Button loginBtn, regisBtn;

	Vector<User> users;
	
	public RegisterPage() {
		initialize();
		build();
		style();
		addAction();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Register Page");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize() {
		bp = new BorderPane();
		formPane = new GridPane();
		scene = new Scene(bp, 400, 600);
		users = new Vector<>();
		
		i = new Image("file:flower-logo.png");
		iv = new ImageView(i);
		iv.setFitWidth(200);
		iv.setPreserveRatio(true);
		
		headerBox = new VBox();

		titleLbl = new Text("Toko Bunga \n    Register");
		userIdLbl = new Text("User ID");
		unameLbl = new Text("Username");
		passLbl = new Text("Password");
		emailLbl = new Text("Email");
		phoneNumberLbl = new Text("Phone Number");
		ageLbl = new Text("Age");
		genderLbl = new Text("Gender");

		userIdField = new TextField();
		userIdField.setDisable(true);
		userIdField.setText(generateId());

		unameField = new TextField();
		emailField = new TextField();
		phoneNumberField = new TextField();

		passField = new PasswordField();

		ageSpinner = new Spinner<Integer>(15,70,16);
		maleBtn = new RadioButton("Male");
		femaleBtn = new RadioButton("Female");
		genderPane = new FlowPane(maleBtn, femaleBtn);

		genderToggle = new ToggleGroup();
		genderToggle.getToggles().addAll(maleBtn, femaleBtn);

		loginBtn = new Button("Login Page");
		regisBtn = new Button("Register");
		btnPane = new FlowPane(loginBtn, regisBtn);

	}
	
	public void build() {
		headerBox.getChildren().add(iv);
		headerBox.getChildren().add(titleLbl);
		
		formPane.add(userIdLbl, 0, 0);
		formPane.add(userIdField, 1, 0);

		formPane.add(unameLbl, 0, 1);
		formPane.add(unameField, 1, 1);

		formPane.add(passLbl, 0, 2);
		formPane.add(passField, 1, 2);

		formPane.add(emailLbl, 0, 3);
		formPane.add(emailField, 1, 3);

		formPane.add(phoneNumberLbl, 0, 4);
		formPane.add(phoneNumberField, 1, 4);

		formPane.add(ageLbl, 0, 5);
		formPane.add(ageSpinner, 1, 5);

		formPane.add(genderLbl, 0, 6);
		formPane.add(genderPane, 1, 6);

		bp.setTop(headerBox);
		bp.setCenter(formPane);
		bp.setBottom(btnPane);
	}

	public void style() {
		bp.setPadding(new Insets(30));
		formPane.setVgap(10);
		formPane.setHgap(10);
		formPane.setPadding(new Insets(10));
		
		headerBox.setAlignment(Pos.CENTER);
		headerBox.setPadding(new Insets(10));
		
		bp.setStyle("-fx-background-color: white");
		bp.setAlignment(titleLbl, Pos.TOP_CENTER);
		bp.setMargin(titleLbl, new Insets(0,0,30,0));
		titleLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 27));
		genderPane.setHgap(10);
		btnPane.setHgap(10);
		regisBtn.setStyle("-fx-background-color: Pink;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
		loginBtn.setStyle("-fx-background-color: Green; -fx-text-fill: White;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
		btnPane.setAlignment(Pos.BASELINE_CENTER);
	}

	public void addAction() {
		regisBtn.setOnAction(this);
		loginBtn.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == regisBtn) {
			if(validateUserInput()) {
				
				addUserData();
				
				Alert regisAlert = new Alert(AlertType.INFORMATION);
				regisAlert.setHeaderText("REGISTRATION SUCCESS");
				regisAlert.setContentText("Click OK to continue");
				regisAlert.showAndWait();
				
				Stage curr = (Stage) bp.getScene().getWindow();

				curr.close();
				Stage next = new Stage();
				try {
					new LoginPage().start(next);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else {
				Alert regisAlert = new Alert(AlertType.INFORMATION);
				regisAlert.setHeaderText("REGISTRATION ERROR");
				regisAlert.setContentText(regisError);
				regisAlert.showAndWait();
			}
		}

		if(event.getSource() == loginBtn) {
			Stage curr = (Stage) bp.getScene().getWindow();

			curr.close();
			Stage next = new Stage();
			try {
				new LoginPage().start(next);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String generateId() {
		String lastId="", nextId="";

		String query = "SELECT * FROM user";
		connect.rs = connect.execQuery(query);

		try {
			while(connect.rs.next()) {
				String id = connect.rs.getString("userID");
				String username = connect.rs.getString("username");
				String password = connect.rs.getString("password");
				String gender = connect.rs.getString("gender");
				String email = connect.rs.getString("email");
				String phoneNumber = connect.rs.getString("phoneNumber");
				Integer age = connect.rs.getInt("age");
				String role = connect.rs.getString("role");
				users.add(new User(id,username, password, gender, email, phoneNumber, age, role));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!users.isEmpty()) {
			lastId = users.get(users.size()-1).getUserId();
			nextId = lastId.substring(2,5);

			User.setCount(Integer.valueOf(nextId)+1);
		}
		String id="US";
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
	
	public boolean validateUserInput() {
		boolean validated=false, filledV=false, genderV=false;
		
		if(unameField.getText().equals("")||passField.getText().equals("")||emailField.getText().equals("")||phoneNumberField.getText().equals("")||genderToggle.getSelectedToggle().isSelected()==false) {
			regisError = "Please make sure all field are filled!";
		}else {
			filledV=true;
			String x = genderToggle.getSelectedToggle().toString();
			genderSelected = x.substring(x.indexOf("'")+1, x.length()-1);
			
			if(genderSelected.equals("Male") || genderSelected.equals("Female")) {
				genderV = true;
			}else {
				regisError = "Gender must be selected, either ‘Male’ or ‘Female’";
			}
			
			if(filledV && genderV) {
				validated = true;
			}
		}
		return validated;
	}

	public void addUserData() {
		String query = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";

		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setString(1, generatedId);
			ps.setString(2, unameField.getText());
			ps.setString(3, passField.getText());
			ps.setString(4, genderSelected);
			ps.setString(5, emailField.getText());
			ps.setString(6, phoneNumberField.getText());
			ps.setInt(7, ageSpinner.getValue());
			ps.setString(8, "customer");
			ps.executeUpdate();

		}catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
