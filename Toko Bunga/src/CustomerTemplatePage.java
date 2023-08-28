
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CustomerTemplatePage extends BorderPane implements EventHandler<ActionEvent>{

//	BorderPane bp;
	
	MenuBar menuBar;
	Menu menu;
	MenuItem buyFlower, transactionHistory, logoutItem;
	
	public CustomerTemplatePage() {
		initialize();
		addAction();
	}
	
	public void initialize() {
//		bp = new BorderPane();
		menuBar = new MenuBar();
		menu = new Menu("Menu");
		buyFlower = new MenuItem("Buy Flower");
		transactionHistory = new MenuItem("Transaction History");
		logoutItem = new MenuItem("Logout");
		
		menuBar.getMenus().add(menu);
		menu.getItems().addAll(buyFlower, transactionHistory, logoutItem);
		
		this.setTop(menuBar);
	}
	
	public void addAction() {
		buyFlower.setOnAction(this);
		transactionHistory.setOnAction(this);
		logoutItem.setOnAction(this);
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == buyFlower) {
			Stage curr = (Stage) this.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new BuyFlower().start(next);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(event.getSource() == transactionHistory) {
			Stage curr = (Stage) this.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new TransactionHistory().start(next);
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
