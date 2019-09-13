package View;

/* TextDemo.java requires no other files. */
 
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import Controller.Main;
import Model.LineFromAFile;
import Model.SearchResult;
import static java.util.Comparator.comparingInt;

 
public class SimpleSearchBox extends JPanel implements ActionListener {
    protected JTextField textField;
    protected JTextArea textArea, feedbackArea;
    private JLabel noOfFiles;
    protected JPanel topPanel, searchResultsInfoPanel;
    private JButton searchButton;
    private JComboBox fileType;
    private final static String newline = "\n";
    private String[] fileTypes = {"SRT"};
    private JTextPane tPane;
    private HashMap<String, SearchResult> resultsForAllFiles;
    
 
    public SimpleSearchBox() {
        super(new GridBagLayout());
        topPanel = new JPanel();
        searchResultsInfoPanel = new JPanel();
        noOfFiles = new JLabel("Type the search term, select the file type and press SEARCH.");
        searchResultsInfoPanel.add(noOfFiles);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        
        fileType = new JComboBox(fileTypes);
        fileType.setSelectedIndex(0);
 
        textField = new JTextField(20);
        textField.addActionListener(this);
 
        textArea = new JTextArea (30, 100);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
 
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
 
        c.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(textField);
        topPanel.add(fileType);
        topPanel.add(searchButton);
        add(topPanel, c);
        add(searchResultsInfoPanel, c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
    }
    public static void testReadResource() {
//    	InputStream res =  Main.class.getResourceAsStream("/my-resource.txt");
//
//    	BufferedReader reader = new BufferedReader(new InputStreamReader(res));
//    	String line = null;
//    		while ((line = reader.readLine()) != null) {
//    			System.out.println(line);
//    		}
//    		reader.close();
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	InputStream input = classLoader.getResourceAsStream("test.png");
    	Image image;

    	try {
    	    image = ImageIO.read(input);
    	    System.out.println("Read the image fine...");
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }
 
    public void actionPerformed(ActionEvent evt) {
    	textArea.setText("");
    	int totalResults = 0;
    	int numberOfFilesWithResults = 0;
    	if(evt.getSource() == searchButton || evt.getSource() == textField) {
	        String text = textField.getText();
	        textField.setText("");
	        textField.selectAll();
	        System.out.println("Filetype: " + fileType.getSelectedItem() + ". searchTerm: " + text);
	        
	        //call the controller and get info back in
	        Main main = new Main();
	        SearchResult searchResult;
	        resultsForAllFiles = main.searchDirectory(text);
	        for (String key : resultsForAllFiles.keySet()) {
	        	searchResult = resultsForAllFiles.get(key);
				System.out.println("Found " + searchResult.getResults().size() + " results in file " + searchResult.getFilename());
				textArea.append("Found " + searchResult.getResults().size() + " results in file " + searchResult.getFilename() + newline);
				for (LineFromAFile result : searchResult.getResults()) {
					System.out.println("-> Found at line: " + result.getLineNr() + ". Content: " + result.getLineStr());
					textArea.append("-> Found at line: " + result.getLineNr() + ", " + result.getTime() + ". Content: " + result.getLineStr() + newline);
				}
				System.out.println("-------------------");
				totalResults = totalResults + searchResult.getNumberOfReults();
				if(searchResult.getResults().size() > 0) {
					numberOfFilesWithResults++;
				}
				textArea.append(newline);
			}
	        noOfFiles.setText("Your search for '" + text + "' returned " + totalResults + " results in " + numberOfFilesWithResults + " files.");
	 
	        //textArea.append(text + newline);
	        //Make sure the new text is visible, even if there
	        //was a selection in the text area.
	        textArea.setCaretPosition(textArea.getDocument().getLength());
	        super.update(this.getGraphics());
    	}
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        frame.add(new SimpleSearchBox());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        testReadResource();
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}