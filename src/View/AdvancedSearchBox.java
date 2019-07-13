package View;

/* TextDemo.java requires no other files. */
 
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Controller.Main;
import Demos.ListDemo;
import Model.LineFromAFile;
import Model.SearchResult;
 
public class AdvancedSearchBox extends JPanel implements ActionListener, ListSelectionListener {
    protected JTextField textField;

    private JLabel noOfFiles;
    protected JPanel topPanel, searchResultsInfoPanel;
    private JButton searchButton;
    private JComboBox fileType;
    private final static String newline = "\n";
    private String[] fileTypes = {"SRT"};
    private JTextPane tPane;
    private JList list;
    private DefaultListModel<LineFromAFile> listModel;
    private JScrollPane scrollPane;
    private HashMap<String, SearchResult> resultsForAllFiles;
    private String tooltipText;
 
    public AdvancedSearchBox() {
        super(new GridBagLayout());
        listModel = new DefaultListModel<>();
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
                	System.out.println("What was clicked is: " + list.getSelectedValue());
                	LineFromAFile lineFromAFile = (LineFromAFile)list.getSelectedValue();
                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	            public void run() {
        	                SingleResultView.createAndShowGUI(lineFromAFile, resultsForAllFiles.get(lineFromAFile.getFilename()));
        	            }
        	        });
                } 
            }
        });
        //list.setVisibleRowCount(5);
        //list.setLayoutOrientation(JList.VERTICAL_WRAP);
        scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        

        
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
        add(scrollPane);
        super.update(this.getGraphics());
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
    	listModel.clear();
    	int totalResults = 0;
    	int numberOfFilesWithResults = 0;
    	if(evt.getSource() == searchButton || evt.getSource() == textField) {
    		tooltipText = "<html>";
    		//listModel.clear();
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
				tooltipText = tooltipText + "Found " + searchResult.getResults().size() + " results in file " + searchResult.getFilename() + "<br>";
				//textArea.append("Found " + searchResult.getResults().size() + " results in file " + searchResult.getFilename() + newline);
				//listModel.addElement("Found " + searchResult.getResults().size() + " results in file " + searchResult.getFilename());
				for (LineFromAFile result : searchResult.getResults()) {
					System.out.println("-> Found at line: " + result.getLineNr() + ". Content: " + result.getLineStr());
					//textArea.append("-> Found at line: " + result.getLineNr() + ", " + result.getTime() + ". Content: " + result.getLineStr() + newline);
					listModel.addElement(result);
				}
				System.out.println("-------------------");
				totalResults = totalResults + searchResult.getNumberOfReults();
				if(searchResult.getResults().size() > 0) {
					numberOfFilesWithResults++;
				}
				//textArea.append(newline);
				//listModel.addElement("   ");
			}
	        tooltipText = tooltipText + "</html>";
	        noOfFiles.setText("Your search for '" + text + "' returned " + totalResults + " results in " + numberOfFilesWithResults + " files.");
	        noOfFiles.setToolTipText(tooltipText);
	        //textArea.append(text + newline);
	        //Make sure the new text is visible, even if there
	        //was a selection in the text area.
	        //textArea.setCaretPosition(textArea.getDocument().getLength());
	        scrollPane.repaint();
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
        JFrame frame = new JFrame("Quick Search");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new AdvancedSearchBox();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
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
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}