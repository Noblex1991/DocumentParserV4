package documentParser;


import java.util.HashMap;

public class WeigherDocument {
	
	private HashMap <String, Order> mergedOrders;
	//private String wholeDocument;
	//private  Object[][] table;
	
	public WeigherDocument(){
		mergedOrders = new HashMap<String, Order>();
		//wholeDocument= new String();
	}
	
	public HashMap<String, Order> createTraks(HashMap<String, Order> orders, String baseFileName){
		for(Order o : orders.values()){
			if(mergedOrders.containsKey(o.getBrandCode())){
				Order order = mergedOrders.get(o.getBrandCode());
				for(Product p : o.getProducs().values()){
					// if(mergedOrders.get(o.getBrandCode()).getProducs().containsKey(p.getName())){
					if(order.getProducs().containsKey(p.getName())){
						Product product = order.getProducs().get(p.getName());
						product.setQty(product.getQty() + p.getQty());
						order.getProducs().put(product.getName(), product);
					} else {
						order.addProduct(p.getName(), p);
					}
				}
			} else {
				mergedOrders.put(o.getBrandCode(), o);
			}
		}
		//print(mergedOrders, baseFileName);
		return mergedOrders;
	}
}