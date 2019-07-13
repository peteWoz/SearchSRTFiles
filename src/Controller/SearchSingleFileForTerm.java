package Controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Model.LineFromAFile;
/*
 * 
 * 
 * 	15
	00:01:48,149 --> 00:01:56,909
	invite you here. So, if you can share it
	again. There it is. Can you see it Mr.
 * 
 */

public class SearchSingleFileForTerm {
	private int lineNr;
	private List<LineFromAFile> items=new ArrayList<LineFromAFile>();
	
	public SearchSingleFileForTerm() {
		//items=new ArrayList<LineFromAFile>();
	}
	
	public List<LineFromAFile> readWholeFile(String filename) {
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    InputStream input = classLoader.getResourceAsStream(filename);
		int val = 0;
		lineNr = 0;
		//Boolean termFound = false;
		Scanner file = null;
		//file = new Scanner(input, "utf-8");
		file = new Scanner(input);
		String strLine="";
		ArrayList<String> lines=new ArrayList<String>();
		//Read File Line By Line
		while (file.hasNextLine()){
			strLine = file.nextLine();
		    if (isEmpty(strLine)){
		       items.add(new LineFromAFile(filename, lines));
		       lines= new ArrayList<String>();
		    } else {
			        lines.add(strLine);
			        lineNr++;
		    }
		}
		
		if (lines.size() > 1) {
		  items.add(new LineFromAFile(filename, lines));
		}
		
		/*
		while(file.hasNextLine()){
			String line = file.nextLine();
			System.out.println(line);
			if(line.contains(searchTerm)){
				results.add(new LineFromAFile(lineNr, filename, line));
				//System.out.println(line);
			}
			lineNr++;
		}
		*/
		file.close();
		return items;
	}

	
	public void print() {
		   for (int i=0; i < items.size(); i++){
		     System.out.println (items.get(i));
		   }  
		 }

	private static boolean isEmpty(String s){
		  if(s == null)          return true;
		  if(s.trim().isEmpty()) return true;
		  return false;
	 }
	public static void main (String[] args) {
		SearchSingleFileForTerm ssf = new SearchSingleFileForTerm();
		//List<LineFromAFile> items = ssf.readWholeFile("262nd KSW from 0_50_10 to 3_03_00.srt");
		List<LineFromAFile> items = ssf.readWholeFile("2019-07-01-PM-Mo-SSI-S2.MTK.mp4.gEN.srt");
		for (int i=0; i < items.size(); i++){
		     System.out.println (items.get(i));
		   }  
	}
}