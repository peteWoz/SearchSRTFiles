package Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.LineFromAFile;
import Model.SearchResult;

public class Main {
	private List<SearchResult> resultsForAllFiles;
	private HashMap<String, SearchResult> resultsMap;

	public Main() {
		resultsForAllFiles = new ArrayList<SearchResult>();
	}
	
	public HashMap<String, SearchResult> searchDirectory(String searchTerm) {
		SearchSingleFileForTerm ssf;
		SearchResult result;
		BrowseResourceDirectory brd = new BrowseResourceDirectory();
		resultsMap = new HashMap<String, SearchResult>();
		try {
			for (String filename : brd.getResourceFiles("/", "srt")) {
				System.out.println(filename);
				ssf = new SearchSingleFileForTerm();
				result = new SearchResult(filename, searchTerm, ssf.readWholeFile(filename));
				if(result.getResults() != null) {
					//resultsForAllFiles.add(result);
					resultsMap.put(filename, result);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultsMap;
	}
	public static void main (String[] args) {
		Main main = new Main();
		for (SearchResult searchResult : main.searchDirectory("soul")) {
			System.out.println("Found " + searchResult.getResults().size() + " results in file " + searchResult.getFilename());
			for (LineFromAFile result : searchResult.getResults()) {
				System.out.println("-> Found at line: " + result.getLineNr() + ". Content: " + result.getLineStr());
			}
			System.out.println("-------------------");
		}
	}

}
