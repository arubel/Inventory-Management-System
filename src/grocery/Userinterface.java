package grocery;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * This is the class for creating the gui and calling the action events
 * @author Rubel
 *
 */

@SuppressWarnings("serial")
public class Userinterface extends JFrame implements ActionListener {

	private Container dataPane;
	private TextArea dataArea;
	private String inFile = "";
	private String outFile = "";
	// two argument constructor
	
	public Userinterface(String title, String output) {
		// inFile = input;
		outFile = output;
		this.setTitle(title);
		this.initialize();
		this.pack();

	}
	// One argument Constructor
	public Userinterface(String string) {

		this.setTitle(string);
		this.initialize();
		this.pack();

	}

	/**
	 * This method is called from the main that carries out the task of launching the actions and creating menus
	 */
	public void createAndDisplay() {

		JMenuBar mb = new JMenuBar();

		JMenu mainMenu = new JMenu("File");
		mb.add(mainMenu);

		JMenu Query = new JMenu("Sql Operations");
		mb.add(Query);

		JMenuItem createTable = new JMenuItem("CreateTable");
		createTable.addActionListener(this);
		mainMenu.add(createTable);

		JMenuItem readFromFile = new JMenuItem("SelectFiles");
		readFromFile.addActionListener(this);
		mainMenu.add(readFromFile);

		JMenuItem searchItem = new JMenuItem("Search");
		searchItem.addActionListener(this);
		Query.add(searchItem);

		JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(this);
		Query.add(deleteItem);

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(this);
		mainMenu.add(exitItem);
		
		JMenuItem viewItem = new JMenuItem("View All Products");
		viewItem.addActionListener(this);
		Query.add(viewItem);

		setJMenuBar(mb);
		setVisible(true);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Every time an action is selected this method is invoked
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String menuItem = e.getActionCommand();
		if (menuItem.equals("CreateTable"))
			SqlHandler.CreateTable();
		if (menuItem.equals("SelectFiles"))
			openFile();
		if (menuItem.equals("Search"))
			SqlHandler.SelectQuery(dataArea);
		if (menuItem.equals("Delete"))
			SqlHandler.DeleteQuery(dataArea);
		if(menuItem.equals("View All Products"))
			SqlHandler.selectAll(dataArea);
		if (menuItem.equals("Exit")) {
			JOptionPane.showMessageDialog(null, "Exiting Application...");
			System.exit(0);
		}

	}

	/**
	 * Set size , width, layout and text area of the Jframe
	 */
	private void initialize() {

		setSize(400, 50);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dataPane = this.getContentPane();

		dataPane.setLayout(new BorderLayout());
		dataPane.add(dataArea = new TextArea(), BorderLayout.CENTER);

		dataArea.setEditable(false);

	}

	/**
	 * This method lets users select two files and does the task of adding data from 
	 * the first file to the database and outputs a log file
	 */
	private void openFile() {

		JFileChooser chooser;
		int status;
		chooser = new JFileChooser();
		status = chooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION)
			inFile = chooser.getSelectedFile().getPath();
		else
			JOptionPane.showMessageDialog(null, "Open File dialog canceled");

		JOptionPane.showMessageDialog(null, "Select Output File");

		chooser = null;
		status = 0;
		chooser = new JFileChooser();
		status = chooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION)
			outFile = chooser.getSelectedFile().getPath();
		else
			JOptionPane.showMessageDialog(null, "Open File dialog canceled");
		if (inFile.length() < 3 || outFile.length() < 3) {
			JOptionPane.showMessageDialog(null, "Input and output file names were not selected properly");
			System.exit(0);
		}
		SqlHandler.processFile(inFile, outFile, dataArea);

	} // open

}
