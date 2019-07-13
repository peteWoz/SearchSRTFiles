package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import Model.LineFromAFile;
import Model.SearchResult;

public class SingleResultView extends JPanel implements ActionListener{
	private DefaultListModel<LineFromAFile> listModel;
    private JScrollPane scrollPane;
    private JList list;
    private JPanel topPanel, bottomPanel;
    private JLabel filename, index, time;
    private LineFromAFile foundLine;
    private static SearchResult wholeFile;
    private List<LineFromAFile> rangeOfLines;
    private int factor = 10;
    protected JEditorPane textArea;
    
    private JButton copy;
    private StringBuffer buffer;
    
    public SingleResultView() {}
    
    
	public SingleResultView(LineFromAFile foundLine, String searchTerm) {
		super(new GridBagLayout());
		this.setPreferredSize(new Dimension(800,700));
		this.foundLine = foundLine;
		listModel = new DefaultListModel();
		int lineNr = foundLine.getLineNr();
		int lowerIndex = Math.max(0, lineNr-factor);
		int upperIndex = Math.min(lineNr+factor, wholeFile.getFileLines().size()-1);
		rangeOfLines = wholeFile.getFileLines().subList(lowerIndex, upperIndex);
		int indexOfSearchedWord;
		String text = "";
		
		buffer = new StringBuffer();
		for (LineFromAFile lineFromAFile : rangeOfLines) {
			listModel.addElement(lineFromAFile);
			if(foundLine == lineFromAFile) {
				text = lineFromAFile.getLineStr();
				indexOfSearchedWord = text.indexOf(searchTerm);
				buffer.append(text.substring(0, indexOfSearchedWord));
				buffer.append("<font color=\"red\"><b>" + searchTerm + "</b></font>");
				buffer.append(text.substring(indexOfSearchedWord + searchTerm.length()) + "\n");
			
			}else {
				buffer.append(lineFromAFile.getLineStr() + "\n");
			}
			System.out.println("Line added to resultsView" + lineFromAFile);
		}
	
 
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //list.setSelectedIndex(Math.max(0, factor-1));
        list.setSelectedValue(foundLine, true);
        JScrollPane scrollPane = new JScrollPane(list);
        //scrollPane.setPreferredSize(new Dimension(600, 600));
        //textArea = new JTextArea (50, 30);
        textArea = new JEditorPane("text/html", "");
        textArea.setEditable(false);
        textArea.setText(buffer.toString());
        JScrollPane scrollPaneBottom = new JScrollPane(textArea);
 
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 1));
        filename = new JLabel("File: " + foundLine.getFilename());
        index = new JLabel("Index: " + foundLine.getLineNr());
        time = new JLabel("Time: " + foundLine.getTime());
        topPanel.add(filename);
        topPanel.add(index);
        topPanel.add(time);
        
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        copy = new JButton("Copy");
        copy.addActionListener(this);
        bottomPanel.add(copy);
        
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        
 
        c.fill = GridBagConstraints.HORIZONTAL;

        add(topPanel, c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
        add(scrollPaneBottom, c);
        add(bottomPanel);
        //scrollPane.repaint();
        //super.update(this.getGraphics());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == copy) {
			Toolkit.getDefaultToolkit()
	        .getSystemClipboard()
	        .setContents(
	                new StringSelection(buffer.toString()),
	                null
	        );
		}
		
	}
	
	 protected static void createAndShowGUI(LineFromAFile arg, SearchResult result, String searchTerm) {
	        //Create and set up the window.
	        JFrame frame = new JFrame("Search Result");
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        wholeFile = result;
	        //Create and set up the content pane.
	        JComponent newContentPane = new SingleResultView(arg, searchTerm);
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
	            	LineFromAFile f = new LineFromAFile("100", "00:58:29,180 --> 00:58:31,160", "soul of the man leaves the physicality");
	            	f.setFilename("262nd KSW from 0_50_10 to 3_03_00.srt");
	                createAndShowGUI(f, null, "abc");
	            }
	        });
	    }

}
