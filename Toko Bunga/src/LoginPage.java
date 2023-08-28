import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.Connect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

public class LoginPage extends Application implements EventHandler<ActionEvent>{

	Connect connect = Connect.getInstance();
	static User currUser;
	Image i;
	ImageView iv;
	
	Scene scene;
	VBox bp;
	GridPane formPane;

	Text titleLbl, unameLbl, passLbl;
	TextField unameField;
	PasswordField passField;
	Button loginBtn, regisBtn;
	VBox headerBox, btnBox;

	public static void main(String[] args) {
		launch(args);
	}

	public LoginPage() {
		initialize();
		build();
		style();
		addAction();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Login Page");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	public void initialize() {
		formPane = new GridPane();
		bp = new VBox();
		scene = new Scene(bp, 500, 500);
		
		headerBox = new VBox();
		
		i = new Image("file:flower-logo.png");
		iv = new ImageView(i);
		iv.setFitWidth(200);
		iv.setPreserveRatio(true);

		titleLbl = new Text("Toko Bunga\n      Login");
		unameLbl = new Text("Username");
		passLbl = new Text("Password");

		unameField = new TextField();
		passField = new PasswordField();

		loginBtn = new Button("Login");
		regisBtn = new Button("Register Account Page");
		btnBox = new VBox();

	}

	public void build() {
		headerBox.getChildren().add(iv);
		headerBox.getChildren().add(titleLbl);
		
		formPane.add(unameLbl, 0, 0);
		formPane.add(unameField, 1, 0);

		formPane.add(passLbl, 0, 1);
		formPane.add(passField, 1, 1);

//		formPane.add(loginBtn, 0, 2, 2, 1);
//		formPane.add(regisBtn, 0, 3, 2, 1);

		btnBox.getChildren().add(loginBtn);
		btnBox.getChildren().add(regisBtn);

		bp.getChildren().add(headerBox);
		bp.getChildren().add(formPane);
		bp.getChildren().add(btnBox);
	}

	public void style() {
		bp.setPadding(new Insets(20));
		formPane.setVgap(10);
		formPane.setHgap(10);
		
		formPane.setAlignment(Pos.CENTER);
		headerBox.setAlignment(Pos.CENTER);
		btnBox.setAlignment(Pos.CENTER);
		bp.setMargin(formPane, new Insets(30));
		btnBox.setMargin(loginBtn, new Insets(0,0,20,0));
		unameField.setPrefWidth(bp.getWidth());
		passField.setPrefWidth(bp.getWidth());
		titleLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 27));
		formPane.setPadding(new Insets(0, 80, 0, 80));
		bp.setStyle("-fx-background-color: White");
		
		unameLbl.setFont(Font.font("Verdana", 14));
		passLbl.setFont(Font.font("Verdana", 14));
		
		
		loginBtn.setPrefSize(bp.getWidth()/2, 40);
		regisBtn.setPrefSize(bp.getWidth()/2, 40);
		loginBtn.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		regisBtn.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		loginBtn.setCursor(Cursor.HAND);
		regisBtn.setCursor(Cursor.HAND);
		loginBtn.setStyle("-fx-background-color: Pink;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
		regisBtn.setStyle("-fx-background-color: Green; -fx-text-fill: White;-fx-effect: dropshadow(three-pass-box, #DEDEDE, 0.0, 25.0, 5.0,  5.0); -fx-border-radius: 20 20 20 20;-fx-background-radius: 20 20 20 20;");
	}

	public void addAction() {
		regisBtn.setOnAction(this);
		loginBtn.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == loginBtn) {
			if(checkUser()) {
				//ada data di database
				Alert success = new Alert(AlertType.INFORMATION);
				success.setHeaderText("LOGIN SUCCESS");
				success.setContentText("Click OK to continue");
				success.showAndWait();
				
				Stage curr = (Stage) bp.getScene().getWindow();
				curr.close();
				Stage next = new Stage();

				try {
					if(this.currUser.getRole().equals("admin")) {
						new ManageFlower().start(next);
					}else if(this.currUser.getRole().equals("staff")){
						new StaffMainPage().start(next);
					}else {
						new BuyFlower().start(next);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}else {
				Alert error = new Alert(AlertType.INFORMATION);
				error.setHeaderText("LOGIN FAILED");
				error.setContentText("Username or Password cannot be null!");
				error.show();
			}
		}

		if(event.getSource() == regisBtn) {
			Stage curr = (Stage) bp.getScene().getWindow();

			curr.close();
			Stage next = new Stage();
			try {
				new RegisterPage().start(next);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean checkUser() {
		boolean isUser = false;

		String query = "SELECT * FROM user WHERE username = ? AND password = ?";

		PreparedStatement ps = connect.prepareStatement(query);

		try {
			ps.setString(1, unameField.getText());
			ps.setString(2, passField.getText());
			ResultSet rs = ps.executeQuery();
			

			if(rs.next()) {
				String userId = rs.getString(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String gender = rs.getString(4);
				String email = rs.getString(5);
				String phoneNumber = rs.getString(6);
				int age = rs.getInt(7);
				String role = rs.getString(8);

				System.out.println(userId);
				currUser = new User(userId, username, password, gender, email, phoneNumber, age, role);
				isUser = true;
			}else {
				isUser = false;
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		return isUser;
	}

}
