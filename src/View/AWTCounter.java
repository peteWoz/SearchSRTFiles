package View;

import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
 
// An AWT program inherits from the top-level container java.awt.Frame
public class AWTCounter extends Frame implements ActionListener {
   private Label lblCount;    // Declare a Label component 
   private TextField tfCount; // Declare a TextField component 
   private Button btnCount;   // Declare a Button component
   private int count = 0;     // Counter's value
 
   // Constructor to setup GUI components and event handlers
   public AWTCounter () {
      setLayout(new FlowLayout());
         // "super" Frame, which is a Container, sets its layout to FlowLayout to arrange
         // the components from left-to-right, and flow to next row from top-to-bottom.
 
      lblCount = new Label("Term");  // construct the Label component
      add(lblCount);                    // "super" Frame container adds Label component
 
      tfCount = new TextField("", 20); // construct the TextField component with initial text
      tfCount.setEditable(true);       // set to read-only
      add(tfCount);                     // "super" Frame container adds TextField component
 
      btnCount = new Button("Search");   // construct the Button component
      add(btnCount);                    // "super" Frame container adds Button component
 
      btnCount.addActionListener(this);

      setTitle("Search subtitles");  // "super" Frame sets its title
      setSize(450, 400);        // "super" Frame sets its initial window size
      setVisible(true);     
   }
   public static void testReadResource() {
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
 
   public static void main(String[] args) {
      // Invoke the constructor to setup the GUI, by allocating an instance
      AWTCounter app = new AWTCounter();
      app.testReadResource();
   }
 
   // ActionEvent handler - Called back upon button-click.
   @Override
   public void actionPerformed(ActionEvent evt) {
      ++count; // Increase the counter value
      // Display the counter value on the TextField tfCount
      tfCount.setText(count + ""); // Convert int to String
   }
}