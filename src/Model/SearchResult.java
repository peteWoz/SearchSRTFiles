package Model;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

	private String filename, searchTerm;
	private List<LineFromAFile> fileLines, results;
	public List<LineFromAFile> getFileLines() {
		return fileLines;
	}

	public void setFileLines(List<LineFromAFile> fileLines) {
		this.fileLines = fileLines;
	}

	private int numberOfReults;
	
	
	public SearchResult(String filename, String searchTerm, List<LineFromAFile> fileLines) {
		super();
		this.filename = filename;
		this.searchTerm = searchTerm;
		this.fileLines = fileLines;
		results = new ArrayList<LineFromAFile>();
		this.filterFileContent();
		System.out.println("Called a new search result for file: " + filename + ", which had: " + results.size());
	}
	
	private void filterFileContent(){
		for (LineFromAFile lineFromAFile : fileLines) {
			if(lineFromAFile == null || lineFromAFile.getLineStr() == null) {
				continue;
			}
			if(lineFromAFile.getLineStr().toLowerCase().contains(searchTerm.toLowerCase())) {
				results.add(lineFromAFile);
			}
		}
	}

	public int getNumberOfReults() {
		return results.size();
	}


	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public List<LineFromAFile> getResults() {
		return results;
	}

	public void setResults(List<LineFromAFile> results) {
		this.results = results;
	}

	public SearchResult() {
		// TODO Auto-generated constructor stub
	}

}
