package documentParser;

import java.util.HashMap;
import java.util.LinkedList;

public class PickingDispatchDocument {
	private Object[][]  matrix;
	private int i, j;
	public Object[][] createPicking(HashMap<String, Order> orders, LinkedList <String> products, LinkedList <String> customers,  String pickingFileName) {
		
	/*	System.out.println("orders: ");
		for(Order o: orders.values()){
			k=1;
			System.out.println("Order Number: " + o.getOrderNumber());
			System.out.println("Customer: " + o.getClientName());
			System.out.println("Brand: " + o.getBrandCode());
			System.out.println("Products: ");
			for(Product p : o.getProducs().values()){
				
				System.out.println(k + "): " + p.getName() + " x " + p.getQty());
				k++;
			}
		}
	 */	

			matrix = new Object[products.size()+2][customers.size()+2];
			matrix[0][0]="PRODOTTI/ACQUIRENTI";
			//System.out.println("matrix lenght: " + matrix.length  + ", " + matrix[0].length);
				for(i=1;i<=customers.size();i++){
					matrix[0][i]=customers.get(i-1);
					//System.out.println("putting: " + customers.get(i-1)  + "in: matrix[0][" + i + "]");
				}
				matrix[0][matrix[0].length-1]="TOTALE";
				for(i=1; i<=products.size();i++){
					matrix[i][0]=products.get(i-1);
					//System.out.println("putting: " + products.get(i-1)  + "in: matrix["+ i + "][0]");
				}
				matrix[matrix.length-1][0]="TOTALE";
				for(i=1;i<matrix.length; i++){
					for(j=1; j<matrix[0].length; j++){
						matrix[i][j]= (Integer) 0;
						
					}
				}
				
			
				/*for(i=0; i< matrix.length; i++){
					for(j=0; j<matrix[0].length; j++){
						System.out.print(matrix[i][j] + " ");
					}
					System.out.println();
				}
				*/
				
			
				
				for(Order o : orders.values()){
					//System.out.println(o.getClientName());
					//System.out.println(customers.indexOf(o.getClientName()));
					j=(Integer) customers.indexOf(o.getClientName()) +1;
					for(Product p : o.getProducs().values()){
						i=(Integer)products.indexOf(p.getName()) +1;
						//System.out.println("i: " + i + "; j: " + j);
						//System.out.println(p.getQty());
						//System.out.println(matrix[i][j]);
						matrix[i][j] = (Integer) ((Integer)matrix[i][j] +  p.getQty());
					}
				}
				
				for(j=1;j<matrix[0].length-1;j++){
					for(i=1; i<matrix.length-1;i++){
						matrix[matrix.length-1][j] = (Integer) ((Integer) matrix[matrix.length-1][j] + (Integer)matrix[i][j]);
					}
				}
				
				for(i=1;i<=matrix.length-1;i++){
					for(j=1;j<matrix[0].length-1;j++){
						matrix[i][matrix[0].length-1] = (Integer) ((Integer) matrix[i][matrix[0].length-1] + (Integer) matrix[i][j]);
					}
				}
		/*for(i =1; i< matrix[0].length; i++){
			String value = (String) matrix[0][i];
			StringBuilder sb = new StringBuilder();
			for(j=0; j<value.length();j++){
				sb.append(value.charAt(j));
				//sb.append("\n");
			}
			String result = sb.toString();
			matrix[0][i] = result;
			
		}
		*/
		return matrix;
	}

}
