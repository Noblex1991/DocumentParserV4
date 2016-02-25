package documentParser;

/*
 * PDFTextParser.java
 *
 * Created on January 24, 2009, 11:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 *
 * @author prasanna
 */
import org.apache.pdfbox.cos.COSDocument;

//import org.apache.commons.lang3.*;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.PrintWriter;

//import java.util.LinkedList;
//import java.util.StringTokenizer;

public class PDFtoTextParser {

    PDFParser parser;
    String parsedText;
    PDFTextStripper pdfStripper;
    PDDocument pdDoc;
    COSDocument cosDoc;
    PDDocumentInformation pdDocInfo;

 

    // PDFTextParser Constructor 
    public PDFtoTextParser() {
    }

    // Extract text from PDF Document
    public String pdftoText(String fileName) {

        
        File f = new File("\\\\t18-nas-01\\Public\\PRESA ORDINI ODOO\\Ordini_di_vendita\\" + fileName);
        System.out.println("Trying parsing text from PDF file " + f.getName() + "....");
        

        if (!f.isFile()) {
            System.out.println("File " + fileName + " does not exist.");
            return null;
        } else {
        	System.out.println("File exists");
        }
        System.out.println("...LOADING..."); //tamarrata a caso 
        System.out.println("1");
        
        try {
        	System.out.println("1.1");
            parser = new PDFParser(new FileInputStream(f));
            System.out.println("1.2");
        } catch (Exception e) {
            System.out.println("Unable to open PDF Parser.");
            return null;
        }
        System.out.println("2");
        try {
        	System.out.println("2.1");
            parser.parse();
            System.out.println("2.2");
            cosDoc = parser.getDocument();
            System.out.println("2.3");
            pdfStripper = new PDFTextStripper();
            System.out.println("2.4");
            pdDoc = new PDDocument(cosDoc);
            System.out.println("2.5");
            parsedText = pdfStripper.getText(pdDoc);
            System.out.println("2.6");
        } catch (Exception e) {
            System.out.println("An exception occured in parsing the PDF Document.");
            e.printStackTrace();
            try {
                if (cosDoc != null) {
                    cosDoc.close();
                }
                if (pdDoc != null) {
                    pdDoc.close();
                }
            } catch (Exception e1) {
            	System.out.println("error pDoc or cosDoc.");
                e.printStackTrace();
            }
            return null;
        }

        System.out.println("3");
/*
		try {
			PrintWriter pw = new PrintWriter("parsedText.txt");
			//pw.write(parsedText);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't write on parsedText.");
			e.printStackTrace();
		}
		//System.out.println("Done.");
*/        
        System.out.println("Text parsed correctly");
        System.out.println("Parsed text: \r\n" + parsedText);
        return parsedText;
    }

    // Write the parsed text from PDF to a file
    public void writeTexttoFile(String pdfText, String fileName) {

        System.out.println("\r\nWriting PDF text to output text file " + fileName + "....");
        try {
            PrintWriter pw = new PrintWriter(fileName);
            pw.print(pdfText);
            pw.close();
        } catch (Exception e) {
            System.out.println("An exception occured in writing the pdf text to file.");
            e.printStackTrace();
        }
        System.out.println("Done.");
    }
}