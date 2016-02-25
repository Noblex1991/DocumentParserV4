package documentParser;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
//import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
//import org.apache.poi.xwpf.usermodel.IBody;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;


import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;






public class DocxCreator {
		
		/*
		public void newWordDoc(String filename, String fileContent)   throws Exception {   
	        XWPFDocument document = new XWPFDocument();   
	        XWPFParagraph tmpParagraph = document.createParagraph();   
	        XWPFRun tmpRun = tmpParagraph.createRun();   
	        tmpRun.setText(fileContent);   
	        tmpRun.setFontSize(18);   
	        FileOutputStream fos = new FileOutputStream(new File("." + filename + ".doc"));   
	        document.write(fos);   
	        fos.close();   
	        document.close(); //!
		}
	    */

	XWPFStyle s;
	private Reporter reporter;
	private HashMap <String, String> customerNames = new HashMap<String, String>();
	public void setReporter(Reporter reporter){
		this.reporter = reporter;
		return;
	}
	
		
		
	
		//private DocParser parser = new DocParser();
	
		private static final String outputBaseFolder = "\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Ordini_di_vendita\\";

		
		public void createWeigherDoc(HashMap<String, Order> mergedOrders, String baseFileName) throws IOException, InvalidFormatException{
	        	int i=0;
	        	int cnt=0;
	        	boolean flag;
	        	String brand;
	        	
	        	for(Order o : mergedOrders.values()){
	        		if(!customerNames.containsKey(o.getBrandCode())){
	        			String aux = o.getClientName().split(" ")[0];
	        			customerNames.put(o.getBrandCode(), aux);
	        		}
	        	}

	        	String filepath = "\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Template\\TEMPLATE_TRACCIATO.dotx";
	        	for(Order o : mergedOrders.values()){
	        		cnt = 0;
	        		brand = o.getBrandCode();
	        		flag=false;
	        		File source = new File(filepath);
	        		File dest = new File(".\\templateW.dotx");
	        		try {
	        		    FileUtils.copyFile(source, dest);
	        		} catch (IOException e) {
	        		    e.printStackTrace();
	        		}
	        		XWPFDocument doc = new XWPFDocument(POIXMLDocument.openPackage("templateW.dotx"));
	        		 
	        		
	        			int index = 0;
	        			try {
	        				//System.out.println("Found template content!");
	        				XWPFTable table = doc.getTables().get(index);
	        				index = doc.getPosOfTable(doc.getTables().get(0));
		        		    XWPFTableRow tableRow = table.getRow(0);
		        		   
		        		    for(Product product : o.getProducs().values()){
				 				tableRow = table.createRow();
				 				tableRow.getCell(0).setText("--------------------------------------------- " + product.getName() + "___________________________ "); //attenzione
				 				String s = new String() + " -------- " + product.getQty();
				 				tableRow.getCell(2).setText(s);
				 				cnt+=product.getQty();
				 				//tableRow = table.createRow();
				 				if(s!="") flag=true;
				 			}
		        		    
	        			} catch (Exception e) {
	        				System.out.println("No template content!");
	        			}
	        			
	        			
			 			for (XWPFHeader h : doc.getHeaderList()) {
			 				//System.out.println("header: " + h.getText());
			 				
			 				for(XWPFParagraph p : h.getParagraphs()){
				 				ArrayList<XWPFRun> runs = new ArrayList<XWPFRun>(p.getRuns());
			        		    if (runs != null) {
			        		        for (XWPFRun r : runs) {
			        		            String text = r.getText(0);
			        		            //System.out.println("text: " + text);

			        		            if (text != null && text.contains("brand")) {
			        		                text = text.replace("brand", customerNames.get(o.getBrandCode()));
			        		                r.setText(text, 0);
			        		            	}
			        		            if (text != null && text.contains("date")) {
			        		            	DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			        		            	Calendar calobj = Calendar.getInstance();
			        		            	System.out.println(df.format(calobj.getTime()));
			        		                text = text.replace("date", df.format(calobj.getTime()));
			        		                r.setText(text, 0);
			        		            	}
			        		        	}
			        		    	}
			 				}

			        	}	
			 			for (XWPFFooter f : doc.getFooterList()) {
			 				for(XWPFParagraph p : f.getParagraphs()){
				 				ArrayList<XWPFRun> runs = new ArrayList<XWPFRun>(p.getRuns());
			        		    if (runs != null) {
			        		        for (XWPFRun r : runs) {
			        		            String text = r.getText(0);
			        		            if (text != null && text.contains("id")) {
			        		                text = text.replace("id", o.getBrandCode() + i);
			        		                r.setText(text, 0);
			        		            	}
			        		        	if (text != null && text.contains("tot")) {
			        		        		String s = new String() + cnt;
			        		                text = text.replace("tot", s);
			        		                r.setText(text, 0);
			        		            	}
			        		        	}
			        		    	}
			 				}

			        	}
			 			if(flag){
			 				//XWPFWordExtractor wx = new XWPFWordExtractor(doc); 
				 			doc.write(new FileOutputStream(outputBaseFolder + baseFileName + i + ".doc"));
				 			reporter.createWeigher(outputBaseFolder + baseFileName + i + ".doc", brand);
				 			//parser.addWeigherDoc(baseFileName + i);
				 			//parser.createPDF(baseFileName + i);
				 			i++;			 				
			 			}

			 			doc.close();
	        	}
	        	
	        	//FileUtils.forceDelete(new File("templateW.dotx"));
		}
		
		public void createPickingDoc(Object[][] matrix, String filename, String brand) throws InvalidFormatException, IOException{
			
        	if(!customerNames.containsKey(brand)){
        		
        	}
			
			int kindex =0;
			int startIndex, endIndex, step, maxThreshold=15, split;
			
			if(matrix[0].length<=maxThreshold){
				//System.out.println("len < threshold");
				startIndex=0;
				step=0;
				endIndex=matrix[0].length;
				
			} else {
				//	System.out.println("len > threshold");
					split = (int) Math.ceil((float)(matrix[0].length/maxThreshold)) +1;
					step = (int) Math.ceil((float) (matrix[0].length/split));
					startIndex=0;
					endIndex=step+1;
					//System.out.println("matrix[0].length: "+ matrix[0].length + "; maxThreshold: " + maxThreshold + "; step: "+ step + "; split: " + split);
			}
			
			while(true){
				//System.out.println("\r\n\r\n\r\n\r\n\r\nkindex: " + kindex);
				String filepath = "\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Template\\TEMPLATE_PICKING.dotx";
        		File source = new File(filepath);
        		File dest = new File(".\\templateP.dotx");
        		try {
        		    FileUtils.copyFile(source, dest);
        		} catch (IOException e) {
        		    e.printStackTrace();
        		}
        		XWPFDocument doc = new XWPFDocument(OPCPackage.open("templateP.dotx"));
        		
    			int index = 0;
    			try {
    				index = doc.getPosOfTable(doc.getTables().get(0));
        		    doc.removeBodyElement(index);
    			} catch (Exception e) {
    				System.out.println("No content");
    			}
    			String s = new String();
        		XWPFTable table = doc.createTable();
        		XWPFTableRow tableRow = table.getRow(0);
        		for(int i=0; i< matrix.length; i++){
        				tableRow = table.createRow();
        				if(kindex!=0){
        					s = new String() + matrix[i][0];
        					@SuppressWarnings("unused")
							XWPFTableCell cell ;
        					cell = this.addCarriageReturn(tableRow, s, i, 0);
        				} 
        					for(int j=startIndex;j<endIndex;j++){
            					s = new String() + matrix[i][j];
            					//XWPFTableCell cell;
            					//cell = this.addCarriageReturn(tableRow, s, i, j);
            					tableRow.addNewTableCell().setText(s);
        			    }	
        		}
        		
        		for (XWPFHeader h : doc.getHeaderList()) {
	 				//System.out.println("header: " + h.getText());
	 				
	 				for(XWPFParagraph p : h.getParagraphs()){
		 				ArrayList<XWPFRun> runs = new ArrayList<XWPFRun>(p.getRuns());
	        		    if (runs != null) {
	        		        for (XWPFRun r : runs) {
	        		            String text = r.getText(0);
	        		            //System.out.println("text: " + text);

	        		            if (text != null && text.contains("brand")) {
	        		            	if(brand.equals("GLOBAL")){
	        		            		text = text.replace("brand", "GLOBALE");
	        		            	} else {
	        		            		
	        		            		text = text.replace("brand", brand);
	        		            	}
	        		                
	        		                r.setText(text, 0);
	        		            	}
	        		            if (text != null && text.contains("date")) {
	        		            	DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	        		            	Calendar calobj = Calendar.getInstance();
	        		            	System.out.println(df.format(calobj.getTime()));
	        		                text = text.replace("date", df.format(calobj.getTime()));
	        		                r.setText(text, 0);
	        		            	}
	        		        	}
	        		    	}
	 				}

	        	}	
        		
	 			doc.write(new FileOutputStream(outputBaseFolder + filename + "_" + kindex + ".doc"));
	 			reporter.createPicking(outputBaseFolder + filename + "_" + kindex + ".doc", brand);
	 			kindex++;
        		doc.close();
				//System.out.println("\r\n\r\n\r\n\r\n\r\n\r\nendIndex: " + endIndex + "; matrix[0].length: " + matrix[0].length);
				if(endIndex==matrix[0].length){
					FileUtils.forceDelete(new File("templateP.dotx"));
					return;
				} else {
					startIndex+=step+1;
					endIndex+=step;					
				}
			}		
		}
		
		public XWPFTableCell addCarriageReturn(XWPFTableRow row, String value, int i, int k){
			
			XWPFTableCell cell = row.addNewTableCell();
			if(i==0 && k!=0){
					for(int j=0; j<value.length();j++){
						XWPFParagraph p = cell.addParagraph();
						p.setVerticalAlignment(null);
						XWPFRun run = p.createRun();
						run.setText(String.valueOf(value.charAt(j)));
						//run.addBreak();
					}
			} else {
				cell.setText(value);
			}
			return cell;
		}
}