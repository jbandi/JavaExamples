print('Starting swing...')

// Import the Swing GUI components and a few other classes
importPackage(javax.swing); 
importClass(javax.swing.border.EmptyBorder);
importClass(java.awt.event.ActionListener);
importClass(java.net.URL);
importClass(java.io.FileOutputStream);
importClass(java.lang.Thread);

// Create some GUI widgets
var frame = new JFrame("Rhino URL Fetcher");     // The application window
var urlfield = new JTextField(30);               // URL entry field
var button = new JButton("Download");            // Button to start download
var filechooser = new JFileChooser();            // A file selection dialog
var row = Box.createHorizontalBox();             // A box for field and button
var col = Box.createVerticalBox();               // For the row & progress bars
var padding = new EmptyBorder(3,3,3,3);          // Padding for rows

// Put them all together and display the GUI
row.add(urlfield);                               // Input field goes in the row
row.add(button);                                 // Button goes in the row
col.add(row);                                    // Row goes in the column
frame.add(col);                                  // Column goes in the frame
row.setBorder(padding);                          // Add some padding to the row
frame.pack();                                    // Set to minimum size
frame.visible = true;                            // Make the window visible

while (true){
	Thread.sleep(5000);
}