package documentParser;

import java.util.HashMap;

public class Order {

	private String orderNumber, clientCode, brandCode, clientName, orderDate;
	private HashMap<String, Product> products;
	
	public Order(){
		orderNumber = new String();
		clientCode = new String();
		brandCode = new String();
		clientName = new String();
		orderDate = new String();
		products = new HashMap<String, Product>();
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public void addProduct(String name, Product product){
		products.put(name, product);
	}
	
	public HashMap<String, Product> getProducs(){
		return products;
	}
}
