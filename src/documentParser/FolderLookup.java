package documentParser;
import java.io.File;
import java.util.*;

public class FolderLookup {
	
	private LinkedList<File> fileList;
	//private String folder;
	
	public FolderLookup(){
		fileList = new LinkedList<File>();
		//this.folder = folder;
	}
	
	public LinkedList<File> listPDFFilesForFolder(final File folder) {
		System.out.println(1);
		int i=1;
	    for (final File fileEntry : folder.listFiles()) {
	    	System.out.println(i);
	    	i++;
	    	if(fileEntry.isFile() && fileEntry.getName().endsWith(".pdf")){
	    		System.out.println("File "+ fileEntry.getName() + " found.");
	    		fileList.add(fileEntry);
	    		System.out.println("Added to file list.");
	    	}
	    	//Recursive search
	        //if (fileEntry.isDirectory()) {
	        //    listPDFFilesForFolder(fileEntry);
	        // } else {
	        //    System.out.println(fileEntry.getName());
	        //}
	    }
	    System.out.println("");
	    return fileList;
	}
	
	
	
	public LinkedList<File> EXTENDEDListFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	    	
	    	if(fileEntry.isFile()){
	    		System.out.println("File "+ fileEntry.getName() + " found.");
	    		fileList.add(fileEntry);
	    		System.out.println("Added to file list.");
	    	}
	    }
	    System.out.println("");
	    return fileList;
	}
}