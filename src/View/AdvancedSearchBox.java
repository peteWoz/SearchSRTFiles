package View;

/* AdvancedSearchBox.java requires no other files. */
/* Last modified 27 Jan 2020 */
 
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
 
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;
 
public class AdvancedSearchBox extends JPanel implements ActionListener, ListSelectionListener {
	
    protected JTextField textField, selectedDir;
    private JLabel noOfFiles;
    protected JPanel dirPanel, topPanel, searchResultsInfoPanel;
    private JButton searchButton, openButton;
    private JComboBox fileType;
    private final static String newline = "\n";
    private String[] fileTypes = {"SRT"};
    private JTextPane tPane;
    private JList list;
    private DefaultListModel<LineFromAFile> listModel;
    private JScrollPane scrollPane;
    private Map<String, SearchResult> resultsForAllFiles;
    private String tooltipText, searchTerm;
    private JFileChooser fc;
    private File sourceDir;
    // parameters to set are: no of lines before and after, location of the folder on the disk.
    // TODO Features: select local dir (absolute path + test if sub-folders are read too), generate Youtube link (assume SRT filename is the name of the video).
 
    public AdvancedSearchBox() {
        super(new GridBagLayout());
        
      //Create a file chooser
        fc = new JFileChooser();

        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        openButton = new JButton("Choose Directory");
        openButton.addActionListener(this);
        
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
        	                SingleResultView.createAndShowGUI(lineFromAFile, resultsForAllFiles.get(lineFromAFile.getFilename()), searchTerm);
        	            }
        	        });
                }else if(evt.getClickCount() == 1) {
	                LineFromAFile lineFromAFile = (LineFromAFile)list.getSelectedValue();
	                int index = list.locationToIndex(evt.getPoint());
	                if( index>-1 ) {
	                    list.setToolTipText(lineFromAFile.getFilename());
	                }
                }
            }
        });

        list.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                // no-op
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                JList l = (JList) e.getSource();
                DefaultListModel<LineFromAFile> m = (DefaultListModel<LineFromAFile>) l.getModel();
                int index = l.locationToIndex(e.getPoint());
                if (index > -1) {
                    l.setToolTipText(m.getElementAt(index).getFilename());
                }
            }
        });
        //list.setVisibleRowCount(5);
        //list.setLayoutOrientation(JList.VERTICAL_WRAP);
        scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        
        dirPanel = new JPanel();
        selectedDir = new JTextField(40);
        selectedDir.setEditable(false);
        dirPanel.add(selectedDir);
        dirPanel.add(openButton);
        
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
        add(dirPanel, c);
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
    	//Handle open button action.
        if (evt.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(AdvancedSearchBox.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                sourceDir = fc.getSelectedFile();
                //This is where a real application would open the file.
                System.out.println("Opening: " + sourceDir.getAbsolutePath() + "." + newline);
                selectedDir.setText(sourceDir.getAbsolutePath());
            } else {
            	System.out.println("Open command cancelled by user." + newline);
            }
        }else if(evt.getSource() == searchButton || evt.getSource() == textField) {
    		tooltipText = "<html>";
    		//listModel.clear();
	        searchTerm = textField.getText();
	        textField.setText("");
	        textField.selectAll();
	        System.out.println("Filetype: " + fileType.getSelectedItem() + ". searchTerm: " + searchTerm);
	        
	        //call the controller and get info back in
	        Main main = new Main(selectedDir.getText());
	        SearchResult searchResult;
	        Map<String, SearchResult> temp = main.searchDirectory(searchTerm);
	        resultsForAllFiles = temp
	                .entrySet()
	                .stream()
	                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
	                .collect(
	                    toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                        LinkedHashMap::new));
	        for (String key : resultsForAllFiles.keySet()) {
	        	searchResult = resultsForAllFiles.get(key);
				System.out.println("Found " + searchResult.getResults().size() + " results in file " + searchResult.getFilename());
				if(searchResult.getResults().size() > 0) {
					tooltipText = tooltipText + "Found " + searchResult.getResults().size() + " results in file " + searchResult.getFilename() + "<br>";
				}
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
	        noOfFiles.setText("Your search for '" + searchTerm + "' returned " + totalResults + " results in " + numberOfFilesWithResults + " files.");
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