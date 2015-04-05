package grocery;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 * @author Rubel
 * This class handle the tasks for performing all the SQl operations
 * It has methods for creating a table, searching for a particular 
 * Product in the product table, Delete a product.
 * 
 *
 */
public class SqlHandler {
	// connection parameters
	private static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static String MYSQL_URL = "jdbc:mysql://localhost:3306/test";
	private static Connection con;
	private static PreparedStatement st;

	// variables for IO manipulation
	/**
	 * These are the variables used to open and read from a text file
	 * and after inserting the data into the table it gets logged into a text file
	 */
	private static FileReader fr;
	private static BufferedReader br;
	private static FileWriter fw;

	/**
	 * This method gets invoked when the create table menu option is selected
	 * it creates a new product table in the database
	 */
	public static void CreateTable() {
		try {

			connectToDB();

			String strSql = "CREATE TABLE Products " + "(id INTEGER not NULL, "
					+ " pname VARCHAR(255), " + " price INTEGER not NULL, "
					+ " quantity INTEGER, " + " PRIMARY KEY ( id ))";

			st = con.prepareStatement(strSql);
			st.executeUpdate();

			System.out.println("Database table was created successfully...");
			con.close();
			st = null;
		}

		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "This table is already in the database");
			
			st = null;
		}

	}

	/**
	 * 
	 * This method is called when two file names are selected to perform the intitial task of 
	 * populating the database with new item . 
	 * it needs to two file names 
	 * @param inFile
	 * @param outFile
	 * @param myText
	 */
	public static void processFile(String inFile, String outFile,
			TextArea myText) {

		myText.setText("");
		connectToDB();

		try {
			st = con.prepareStatement("insert into Products (id, pname,price,quantity) "
					+ "values (?, ?, ?, ?)");

			// create objects for file reading and writing
			fr = new FileReader(inFile);
			br = new BufferedReader(fr);
			fw = new FileWriter(outFile);

			// Get date for transaction
			Date dNow = new Date(0);
			SimpleDateFormat ft = new SimpleDateFormat(
					"E yyyy.MM.dd 'at' hh:mm:ss a zzz");

			// Start Reading the file
			String input = br.readLine();

			while (input != null) {

				System.out.println(input);
				myText.append(input + "\n");

				String[] values = input.split("\\s*,\\s*");
				st.setInt(1, Integer.parseInt(values[0]));
				st.setString(2, values[1]);
				st.setInt(3, Integer.parseInt(values[2]));
				st.setInt(4, Integer.parseInt(values[3]));

				st.executeUpdate();

				PrintWriter pw = new PrintWriter(fw);
				pw.println("Insert" + "  " + ft.format(dNow));
				input = br.readLine();

			}// end of while loop

			// close the file

			fr.close();
			br.close();
			fw.close();
			con.close();
			st = null;

		} catch (Exception E) {
			JOptionPane.showMessageDialog(null, "Something went wrong");
			st = null;
		}

	}// end of processFile Method

	/**
	 * Controls the task of searching for a product in the database.
	 * @param myText is where the result of query is printed
	 */
	public static void SelectQuery(TextArea myText) {
		myText.setText("Query Result: " + "\n");

		String userInput = "";
		userInput = JOptionPane
				.showInputDialog("Enter a product Name to search in the database:");
		if (userInput.length() < 1) {
			JOptionPane.showMessageDialog(null, "The search Filed is empty");
			return;
		} else {
			connectToDB();
		}

		try {

			String strSql = "SELECT * FROM Products WHERE pname like ?";

			st = con.prepareStatement(strSql);
			st.setString(1, "%" + userInput + "%");

			ResultSet rset = st.executeQuery();
			int count = 0;
			while (rset.next()) {
				myText.append(rset.getString(1) + " " + rset.getString(2) + " "
						+ rset.getString(3) + " " + rset.getString(4) + "\n");
				count++;

			}
			if (count == 0) {
				myText.append("No matching record found in the table for "
						+ userInput);
			}

			System.out.println("Query Finished!");
			con.close();
			st = null;
		}

		catch (SQLException e) {

			JOptionPane.showMessageDialog(null, "Something went wrong :( ");
			st = null;
		}

	}

	/**
	 * This method is called when a delete operation is attempted
	 * @param myText is where the result of deletion get printed
	 */
	public static void DeleteQuery(TextArea myText) {

		myText.setText("Delete Operation: " + "\n");

		String userInput = "";
		userInput = JOptionPane
				.showInputDialog("Enter a product Name to delete:");
		if (userInput.length() < 1) {
			JOptionPane.showMessageDialog(null, "The input field is empty");
			return;
		}

		connectToDB();
		String strSql = "DELETE FROM Products where pname like ?";
		try {
			st = con.prepareStatement(strSql);
			st.setString(1, "%" + userInput + "%");
			int status = st.executeUpdate();

			myText.append(status + ((status > 1) ? " rows" : " row")
					+ " has been delete\n");
			con.close();
			st = null;

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null, "There was an error!");
		}

	}
	/*
	 * This 
	 */
	public static void selectAll(TextArea myText){
		connectToDB();
		String strSql = "Select * from Products";
		myText.setText("Showing all the products in the Database: \n");
		myText.append("Id     |  pname    | Price    |Quantity \n");
		
		int count = 0;
		try{
			st = con.prepareStatement(strSql);
			ResultSet rset = st.executeQuery();
			while(rset.next()){
				count++;
				myText.append(rset.getString(1) + "      " + rset.getString(2) + "      "
						+ rset.getString(3) + "  " + rset.getString(4) + "\n");
				
				
			}
			myText.append(count +((count>0)?" numbers ": " number ")+ "of records were found") ;
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Something went wrong!");
		}
	}

	/**
	 * This is a private method that takes control of the task of connecting ot the database
	 * 
	 */
	private static void connectToDB() {

		try {

			Class.forName(MYSQL_DRIVER);

			System.out.println("Class Loaded....");
			con = DriverManager.getConnection(MYSQL_URL, "", "");
			System.out.println("Connected to the database....");

		}

		catch (SQLException e) {

			e.printStackTrace();
			st = null;
		} catch (ClassNotFoundException e) {

			JOptionPane.showMessageDialog(null, "Error Connection to the database:(!");
		}

	}

}// end of class

