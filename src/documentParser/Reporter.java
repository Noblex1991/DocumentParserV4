package documentParser;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

public class Reporter {
	DateFormat df;
	Calendar calobj;
	String text;
	PrintWriter pw;
	int totDocs=0, totPicking=0, totWeighers=0, totOrders=0;
	
	
	public Reporter(String path){
		df = new SimpleDateFormat("dd-MM-yy");
		calobj = Calendar.getInstance();
		text = new String();
		text = df.format(calobj.getTime()); 
		try {
			pw = new PrintWriter(path + "report_" + text +".txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.write("REPORT PRESA ORDINI: " + text);
		pw.write("\r\n");
		pw.write("LOG: ");
	}
	
	public void parseOrder(String orderNo){
		pw.write("\r\nPresa in carico dell'ordine: " + orderNo);
		totOrders++;
		return;
	}
	
	public void readPDF(String filename){
		pw.write("\r\nLettura del file: " + filename);
		totDocs++;
		return;
	}
	
	public void createPicking(String pickingName, String brand){
		pw.write("\r\nCreazione file di picking: " + pickingName);
		if(brand.equals("GLOBAL")){
			pw.write(" GLOBALE");
		} else {
			pw.write(" per il codice di fatturazione: " + brand);
		}
		totPicking++;
		return;
	}
	
	public void createWeigher(String weigherName, String brand){
		pw.write("\r\nCreazione file pesatore: " + weigherName + " per il codice di fatturazione: " + brand);
		totWeighers++;
		return;
	}
	
	public void createResume(){
		pw.write("\r\nFINE LOG");
		pw.write("\r\n");
		pw.write("\r\nTotale file presi letti: " + totDocs);
		pw.write("\r\nTotale ordini presi in carico: " + totOrders);
		pw.write("\r\nTotale file di picking creati: " + totPicking);
		pw.write("\r\nTotale file pesatore creati: " + totWeighers);
		return;
	}
	
	public void fileCloser(){
		pw.close();
		return;
	}
	
	
	
	
}
