import java.sql.*;

public class Phase1 {
	
	
	private static String MYSQL_DRIVER =  "com.mysql.jdbc.Driver";
	private static String MYSQL_URL    =  "jdbc:mysql://localhost:3306/test";

	private static Connection con;
	private static Statement  st ;
	//private ResultSet  rs ;
	

	public static void main(String[] args) throws ClassNotFoundException {
			
		try{

			Class.forName(MYSQL_DRIVER);
	        System.out.println("Class Loaded....");
	        con = DriverManager.getConnection(MYSQL_URL,"","");
	        System.out.println("Connected to the database....");
	        st = con.createStatement();
	        
			String strSql = "CREATE TABLE Products " +
					"(id INTEGER not NULL, " +
					" pname VARCHAR(255), " + 
					" price INTEGER not NULL, " + 
					" quantity INTEGER, " + 
					" PRIMARY KEY ( id ))"; 
			
			st.executeUpdate(strSql);
			System.out.println("Database table was created successfully...");
			con.close();
		}
		
		catch(SQLException e){			
			
			e.printStackTrace();
			st =null;
		}
		
		//check to see if both file names are given in the command prompt
		
		if(args.length<2){
			System.out.println("Please check the command line arguments for both input and output files!");
			System.exit(0);// if proper filenames not given exit
		}
		
		FileProcessor.processFile(args[0], args[1]);

	}//main ends
	
}//class phase1 ends
