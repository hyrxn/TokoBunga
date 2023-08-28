
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminTemplatePage extends BorderPane implements EventHandler<ActionEvent>{

//	BorderPane bp;
	
	MenuBar menuBar;
	Menu menu;
	MenuItem manageStaff, manageFlower, transaction, logoutItem;
	
	public AdminTemplatePage() {
		initialize();
		addAction();
	}
	
	public void initialize() {
//		bp = new BorderPane();
		menuBar = new MenuBar();
		menu = new Menu("Menu");
		manageStaff = new MenuItem("Manage User");
		manageFlower = new MenuItem("Manage Flower");
		transaction = new MenuItem("Transactions");
		logoutItem = new MenuItem("Logout");
		
		menuBar.getMenus().add(menu);
		menu.getItems().addAll(manageStaff, manageFlower, transaction, logoutItem);
		
		this.setTop(menuBar);
	}
	
	public void addAction() {
		manageStaff.setOnAction(this);
		manageFlower.setOnAction(this);
		transaction.setOnAction(this);
		logoutItem.setOnAction(this);
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == manageStaff) {
			Stage curr = (Stage) this.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new ManageUser().start(next);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(event.getSource() == manageFlower) {
			Stage curr = (Stage) this.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new ManageFlower().start(next);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(event.getSource() == transaction) {
			Stage curr = (Stage) this.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new TransactionPage().start(next);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(event.getSource() == logoutItem) {
			Stage curr = (Stage) this.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new LoginPage().start(next);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
