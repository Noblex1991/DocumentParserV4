package documentParser;

public class Product {
	
	private float price;
	private int qty;
	private String name;
	private String code;
	private String supplier;
	
	
	public float getPrice() {
		return price;
	}

	public int getQty() {
		return qty;
	}

	public String getName() {
		return name;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public void setSupplier(String supplier){
		this.supplier = supplier;
	}
	
	public String getSupplier(){
		return this.supplier;
	}


	
	public Product(String name, String code, float price, float qty){
		this.name=name;
		this.price = price;
		this.qty=(int) Math.ceil(qty);
		this.code=code;
	}
}
