package grocery;
/**
 * @author Rubel
 * A simple GUI based grocery inventory management system.
 *
 */
/**
 * @author Rubel
 * The main class that is only used to create the the user interface
 * and then call the method that will delegate the work of managment 
 * to other classes.
 */
public class Inventory {

	public static void main(String[] args) {
		
		Userinterface ui = new Userinterface(
				"Grocey Inventory Management System");
		
		ui.createAndDisplay();


	}// main ends

}// Inventory Ends
