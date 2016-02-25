package documentParser;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;


import org.apache.commons.io.FileUtils;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;


public class PrintAndArchive {

	private String sourcePath = new String();
	private String destPath = new String();
	private String dailyFolderName = new String();
	private File dailyFolder;
	private FolderLookup lookup = new FolderLookup();
	
	public PrintAndArchive(String sourcePath, String destPath){
		this.destPath = destPath;
		this.sourcePath = sourcePath;
		

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		dailyFolderName = dateFormat.format(cal.getTime()).toString();
		
		dailyFolder = new File(destPath + dailyFolderName);
		dailyFolder.mkdir();
		
	}
	
	public void printAndArchiveAll(){
		
		System.out.println("sourcePath: " + sourcePath);
		System.out.println("destPath: " +  destPath);
		System.out.println("dailyFolderName: " + dailyFolderName);
		
		
		
		for(File fileEntry : lookup.EXTENDEDListFilesForFolder(new File(sourcePath))){
			if(fileEntry.getName().endsWith(".doc") || fileEntry.getName().endsWith(".txt") || fileEntry.getName().endsWith(".pdf")){
				 archive(fileEntry.getName());
				// if(!fileEntry.getName().startsWith("parsedText")) print(fileEntry.getName());    //comment to prevent printing every time
			}
		}
		for(File fileEntry : lookup.EXTENDEDListFilesForFolder(new File(sourcePath))){
			if(!fileEntry.getName().endsWith(".jar")){
				if(!fileEntry.delete()){
					System.out.println("Unable to delete useless file: " + fileEntry.getName() + ". Please remove it manually before running again the program" );
				}
			}
		}
	}
	
	
	public void archive(String filename){
		File source = new File(sourcePath + filename);
		File dest = new File(destPath + dailyFolderName + "\\" +  filename);
		System.out.println("File source: " +  source);
		System.out.println("File dest: " + dest);
		try {
		    FileUtils.copyFile(source, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		if(!filename.startsWith("report") && !filename.endsWith(".pdf")){
			try {
				FileUtils.forceDelete(new File(sourcePath + filename));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void print(String filename){
		try {
			Desktop.getDesktop().print(new File(destPath + dailyFolderName + "\\" + filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
