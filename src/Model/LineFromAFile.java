package Model;

import java.util.ArrayList;

public class LineFromAFile {

	private int lineNr;
	private String filename, lineStr;
	private String start, finish, time;
	
	
	public void init(String nr, String time, String value) {
		//System.out.println("Line nr is:"+ nr + ".");
		this.lineNr = Integer.parseInt(cleanTextContent(nr));
		this.time = time;
		String tmp = cleanTextContent(value);
		setLineStr(tmp.substring(1, tmp.length()-1));
		//System.out.println(toString());
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public LineFromAFile(String filename, ArrayList<String> singleLine) {
		if (singleLine.size() < 3) {
			return;
		}
		init (singleLine.get(0), singleLine.get(1), singleLine.subList(2, singleLine.size()).toString());
		this.filename = filename;
	}
	
	@Override
	public String toString() {
		return "Index = " + lineNr + ", time=" + time + ", text=" + lineStr;
	}
	//clean all the non-printable caracters
	private static String cleanTextContent(String text){
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");
 
        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
         
        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");
 
        return text.trim();
    }

	public int getLineNr() {
		return lineNr;
	}

	public void setLineNr(int lineNr) {
		this.lineNr = lineNr;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getLineStr() {
		if(lineStr == null) {
			return "";
		}
		return lineStr;
	}

	public void setLineStr(String lineStr) {
		this.lineStr = lineStr.replaceAll("\\<[^>]*>","");
	}

	public LineFromAFile() {
		// TODO Auto-generated constructor stub
	}

}
