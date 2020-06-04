package Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BrowseResourceDirectory {

	private List<String> filenames;
	
	
//	protected List<String> getResourceFiles(String path, String extension) throws IOException {
//	    filenames = new ArrayList<>();
//	    try (
//	            InputStream in = getResourceAsStream(path);
//	            BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
//	        String resource;
//
//	        while ((resource = br.readLine()) != null) {
//	        	if(resource.toLowerCase().endsWith("." + extension.toLowerCase())) {
//	        		filenames.add(resource);
//	        	}
//	        }
//	    }
//
//	    return filenames;
//	}
	protected List<String> getResourceFiles(String path, String extension) throws IOException {
	   
	    try (Stream<Path> walk = Files.walk(Paths.get(path))) {

			filenames = walk.filter(Files::isRegularFile)
					.map(x -> x.toString()).collect(Collectors.toList());
			filenames = filenames.stream().filter(element -> element.toLowerCase().endsWith("." + extension.toLowerCase())).collect(Collectors.toList());

			filenames.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}
	    return filenames;
	}
	
	protected void printFileContent(String filename) {
		
	}

	private InputStream getResourceAsStream(String resource) {
	    final InputStream in
	            = getContextClassLoader().getResourceAsStream(resource);

	    return in == null ? getClass().getResourceAsStream(resource) : in;
	}

	private ClassLoader getContextClassLoader() {
	    return Thread.currentThread().getContextClassLoader();
	}
	
	
	public static void main (String[] args) {
		BrowseResourceDirectory brd = new BrowseResourceDirectory();
		try {
			for (String string : brd.getResourceFiles("/", "srt")) {
				System.out.println(string);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //for (File f : browseResourceDirectory("resources")) {
	    //System.out.println(f);
	   //}
	}
}
