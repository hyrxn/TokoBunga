package model;

public class Product {
	protected String productID;
	protected String addedBy;
	protected String productName;
	protected String color;
	protected int price;
	protected int quantity;
	protected String isDel;
	public static int count;
	
	public Product(String productID, String productName, String color, int price, int quantity) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.color = color;
		this.price = price;
		this.quantity = quantity;
		count++;
	}

	//	public Product(String productID, String productName, String color, int price, int quantity, int isDel) {
	//		super();
	//		this.productID = productID;
	//		this.productName = productName;
	//		this.color = color;
	//		this.price = price;
	//		this.quantity = quantity;
	//		this.isDel = isDel;
	//	}

	public Product(String productID, String addedBy, String productName, String color, int price, int quantity, String isDel) {
		super();
		this.productID = productID;
		this.addedBy = addedBy;
		this.productName = productName;
		this.color = color;
		this.price = price;
		this.quantity = quantity;
		this.isDel = isDel;
	}

	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
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
	public static int getCount() {
		return count;
	}
	public static void setCount(int count) {
		Product.count = count;
	}
}
