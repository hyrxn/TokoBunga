package model;

public class Transaction {
	protected String transactionID, username, itemName, itemDescription;
	protected int price, quantity, totalPrice;
	protected String transactionDate, userID, itemID;
	
	public Transaction(String transactionID, String username, String itemName, String itemDescription, int price,
			int quantity, int totalPrice, String transactionDate) {
		super();
		this.transactionID = transactionID;
		this.username = username;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.transactionDate = transactionDate;
	}
	
	public Transaction(String transactionID, String itemName, String itemDescription, int price, int quantity,
			int totalPrice, String transactionDate) {
		super();
		this.transactionID = transactionID;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.transactionDate = transactionDate;
	}
	
	public Transaction(String transactionID, String userID, String itemID) {
		super();
		this.transactionID = transactionID;
		this.userID = userID;
		this.itemID = itemID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	
}
