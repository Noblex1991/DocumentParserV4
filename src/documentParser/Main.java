package documentParser;


import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.StringTokenizer;


import org.apache.commons.lang3.StringUtils;
 
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import documentParser.FolderLookup;
import documentParser.PDFtoTextParser;
import documentParser.Order;


class AlphaOrder implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
		// TODO Auto-generated method stub
		return o1.compareToIgnoreCase(o2);
	}
}
 
public class Main {
	private static final FolderLookup lookup = new FolderLookup();
	private static final PDFtoTextParser parser = new PDFtoTextParser();
	//private static final PokerDocument poker = new PokerDocument();
	private static final WeigherDocument weigher = new WeigherDocument();
	private static final PickingDispatchDocument picking = new PickingDispatchDocument();
	private static final DocxCreator docx = new DocxCreator(); 

	private static final PrintAndArchive printAndArchive = new PrintAndArchive("\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Ordini_di_vendita\\", "\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Storico\\");
	private static final Reporter reporter = new Reporter("\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Ordini_di_vendita\\");
	
	
    private static String filename = new String();
    private static String pickingFileName = new String();
    private static String weigherFileBaseName = new String();
   // private static String pokerOutputFileName = new String();
    private static String parsed = new String();
    private static String token = new String();
    private static HashMap<String, Order> orders = new HashMap<String, Order>();
    private static HashMap<String, Order> ordersForWeighers = new HashMap<String, Order>();
    private static LinkedList<String> products = new LinkedList<String>();
    private static LinkedList <String> customers = new LinkedList<String>();
    
    private static LinkedList <String> brands = new LinkedList<String>();
    private static HashMap <String, String> brandNames = new HashMap <String, String>();
    
    private static HashMap <String, String> productXsuppliers = new HashMap<String, String>();
    private static LinkedList <String> suppliers = new LinkedList<String>();
    private static HashMap<String, Order> splittedOrders = new HashMap<String, Order>();;
	private static LinkedList<String> splittedCustomers = new LinkedList<String>();
	private static LinkedList<String> splittedProducts = new LinkedList<String>();
    private static StringTokenizer st;
    private static StringBuilder sb = new StringBuilder();
    //private static int i =0;
    /**
     * @param args the command line arguments
     * @throws Exception 
     */
    
   
    
    public static void main(String[] args) throws Exception {
    	
    	//PrintStream out = new PrintStream(new FileOutputStream("\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\console.txt"));
    	//System.setOut(out);
    	
    	System.out.println("TEST OUTPUT");
    	
    	docx.setReporter(reporter);
    	//pokerOutputFileName = "output.txt";
        weigherFileBaseName = "";
        pickingFileName="";
        
        
        String sCurrentLine;
        String productName;
        String supplierName;
        BufferedReader br;
        System.out.println("Getting information from file: \\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Listini\\FornitoriProdotti.txt");
		br = new BufferedReader(new FileReader("\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Listini\\FornitoriProdotti.txt"));
		while ((sCurrentLine = br.readLine()) != null) {
			productName = "";
			supplierName = "";
			StringTokenizer st = new StringTokenizer(sCurrentLine);
			supplierName = st.nextToken();
			while(st.hasMoreTokens()){
				productName = productName + st.nextToken() + " ";
			}
			//System.out.println("Found product: " + productName + " supplied by: " + supplierName);
			productXsuppliers.put(productName, supplierName);

			if(!suppliers.contains(supplierName)){
				suppliers.add(supplierName);
			}
		}
        br.close();
        System.out.println("Done.");
        
        
        System.out.println("Folder lookup at: \\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Ordini_di_vendita\\");
    	for(File f : lookup.listPDFFilesForFolder(new File("\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Ordini_di_vendita\\"))){
    		filename = f.getName();
    		System.out.println("For file:" + filename);
    		parsed=parser.pdftoText(filename);
    		if(parsed==null){
    			System.out.println("Error: can't open file " + filename);
    		} else {
    			System.out.println("Parsing string from " + filename + " into objects");
    			reporter.readPDF(filename);
    			//Order o = new Order();
    			Order o = null;
    			st = new StringTokenizer(parsed, "\n");
    			while(st.hasMoreTokens()){
    				token=st.nextToken();
        			if(StringUtils.startsWithAny(token, new String[] {"Ordine N°", "Nome cliente:", "Codice cliente:", "Data Ordine", "Codice di Fatturazione"})){
        				System.out.println("Found header line: " + token);
        				if(token.startsWith("Ordine")){
        					if(o != null){
        						orders.put(o.getOrderNumber() /*+ i*/, o);
        					}
        					o = new Order();
        					o.setOrderNumber(token);
        					st.nextToken();
        					reporter.parseOrder(o.getOrderNumber());
        				}
        				if(token.contains("Nome cliente")){
        					o.setClientName(st.nextToken());
        					System.out.println("reading client name: " + o.getClientName());
        				}
        				if(token.startsWith("Codice cliente:")){
        					o.setClientCode(st.nextToken());

        				}
        				if(token.startsWith("Data")){
        					o.setOrderDate(st.nextToken());
        				}
        				if(token.startsWith("Codice di Fatturazione")){
        					o.setBrandCode(st.nextToken());
        					if(!brands.contains(o.getBrandCode())){
        						brands.add(o.getBrandCode());
        						
        						brandNames.put(o.getBrandCode(), o.getClientName().split(" ")[0]);
        						
        						System.out.println("adding: " + o.getBrandCode());
        					}
        				}
        				
        			}
        			else if(StringUtils.startsWithAny(token, new String[] {"["})){ 
        				//System.out.println("Found product line: " + token);
        				StringTokenizer st2 = new StringTokenizer(token); 
        				String sReversed = "";	 
        				while (st2.hasMoreTokens()) {
        					sReversed = st2.nextToken() + " " + sReversed;
        				}
        				//System.out.println(sReversed);
        				st2 = new StringTokenizer(sReversed, " ");
        				Float price=0.0f;
        				float qty=0;
        				String name = "";
        				String code = "";
        				DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        				symbols.setDecimalSeparator(',');
						DecimalFormat format = new DecimalFormat("#,#");
						format.setDecimalFormatSymbols(symbols);
						price = format.parse(st2.nextToken()).floatValue();
        				//price = Float.parseFloat(st2.nextToken());
        				st2.nextToken();
        				qty = format.parse(st2.nextToken()).floatValue();
        				code = st2.nextToken();
        				
        				String sBReversed = "";
        				while (st2.hasMoreTokens()) {
        					sBReversed = st2.nextToken() + " " + sBReversed;
        				}
        		//		System.out.println(sBReversed);
        				st2 = new StringTokenizer(sBReversed, " ");
        				st2.nextToken();
        				while(st2.hasMoreTokens()){
        					name += st2.nextToken() + " ";
        				}
        				Product p = new Product(name, code, price, qty);
        				//System.out.println("Product: " + p.getName() + "; Code: " + p.getCode() + "; qty: " + p.getQty());
        				//System.out.println("il cazzo di nome del prodotto è: " + p.getName());
        				//System.out.println("correct supplier is: " + productXsuppliers.get(p.getName()));
        				p.setSupplier(productXsuppliers.get(p.getName()));
        				//System.out.println("assigned: " + p.getSupplier());
        				if(productXsuppliers.containsKey(p.getName())){ //TO BE REMOVED, USEFUL WHILE PRODUCTS NAME AREN'T FIXED YET
        					o.addProduct(name, p);
        				}
        				
        				
        			} else {
        				System.out.println("Found trash line: " + token);
        			}
    			}
   
    			orders.put(o.getOrderNumber() /*+ i*/, o);
    			//i++;
    		}
    	}
    
    	weigherFileBaseName="weigher";

    	ordersForWeighers = orders;
    	
    	    	
    	
    	for(Order o : orders.values()){
    		if(!customers.contains(o.getClientName())){
    			customers.add(o.getClientName());
    			
    		}
    		for(String p : o.getProducs().keySet()){
    			if(!products.contains(p)){
    				products.add(p);
    			}
    		}
    	}
    	

    	
    	

    	Collections.sort(products);
    	Collections.sort(customers);
    	//System.out.println(products);
    	//System.out.println(customers);
    	pickingFileName = "GLOBAL_picking";
    	docx.createPickingDoc(picking.createPicking(orders, products, customers, pickingFileName), pickingFileName, "GLOBAL");
		//picking.createPicking(orders, products, customers, pickingFileName);
    	//printer.print("C:\\PREVENTIVI\\" + pickingFileName + "_0.doc");
    	
    	// picking dispatch splitted by brands
    	
    	int pickingIndex = 0;
    	for(String brand : brands){
    		System.out.println("creating picking document for brand: " + brandNames.get(brand));
    		splittedOrders.clear();
    		splittedCustomers.clear();
    		splittedProducts.clear();
    		for(Order o : orders.values()){
    			//System.out.println("order brand: " + o.getBrandCode());
    			//System.out.println("brand to find " + brand);
    			if(o.getBrandCode().equals(brand.toString())){
    				//System.out.println("found order");
    				splittedOrders.put(o.getOrderNumber(), o);
    				if(!splittedCustomers.contains(o.getClientCode().toString())){
    					splittedCustomers.add(o.getClientName().toString());
    				}
    				for(Product p : o.getProducs().values()){
    					if(!splittedProducts.contains(p.getName())){
    						splittedProducts.add(p.getName().toString());
    					}
    				}
    			}

    		}
    		 
    		
    		
    		sb = new StringBuilder();
    		String s = new String();
    		s = "picking_" + brandNames.get(brand);
    		sb.append("picking_Ragione_di_Fatturazione_").append(pickingIndex);
    		pickingIndex++;
    		System.out.println(sb.toString());
    		System.out.println(s);
        	Collections.sort(splittedProducts);
        	Collections.sort(splittedCustomers);
        	String aux = "CLIENTE: " + brandNames.get(brand);
        	docx.createPickingDoc(picking.createPicking(splittedOrders, splittedProducts, splittedCustomers, sb.toString()), sb.toString(), aux);
    	}
    	
    	
    	
   	
    	pickingIndex = 0;
    	Order filteredOrder;
    	for(String supplier : suppliers){
    		int totP = 0;
    		System.out.println("creating picking document for supplier: " + supplier);
    		splittedOrders.clear();
    		splittedCustomers.clear();
    		splittedProducts.clear();
    		System.out.println("checking into " + orders.size() + " orders");
    		for (Order o : orders.values()){
    			System.out.println("checking into order: " + o.getOrderNumber());
    			filteredOrder = new Order();

    			for(Product p : o.getProducs().values()){
    				System.out.println("checking product: " + p.getName());
    				System.out.println("supplier for this product is: " + p.getSupplier());
    				if(p.getSupplier().equals(supplier)){
    					System.out.println("found product " + p.getName() + " is supplied by: " + supplier);
    					totP++;
    					filteredOrder.addProduct(p.getName(), p);
    					if(!splittedProducts.contains(p.getName())){
    						splittedProducts.add(p.getName());
    					}
    				}
    			}
    			
    			if(totP>0){
    				filteredOrder.setBrandCode(o.getBrandCode());
        			filteredOrder.setClientCode(o.getClientCode());
        			filteredOrder.setClientName(o.getClientName());
        			filteredOrder.setOrderDate(o.getOrderDate());
        			filteredOrder.setOrderNumber(o.getOrderNumber());
        			splittedOrders.put(filteredOrder.getOrderNumber(),filteredOrder);
        			if(!splittedCustomers.contains(filteredOrder.getClientName())){
        				splittedCustomers.add(filteredOrder.getClientName());
        			}
        			
                	
    			}
    			
    		}
    		Collections.sort(splittedProducts);
        	Collections.sort(splittedCustomers);
        	String aux = "FORNITORE: " + supplier;	
        	sb = new StringBuilder();
    		sb.append("picking_Fornitore_").append(pickingIndex);
        	pickingIndex ++;
    		docx.createPickingDoc(picking.createPicking(splittedOrders, splittedProducts, splittedCustomers, sb.toString()), sb.toString(), aux);		
    	}
    	
   	
    	
    	//
    	docx.createWeigherDoc(weigher.createTraks(ordersForWeighers, weigherFileBaseName), weigherFileBaseName); //	WARNING - MUST BE THROWN AT LAST!!!!

    	reporter.createResume();
    	reporter.fileCloser();
    	printAndArchive.printAndArchiveAll();
    }
    
    
}

