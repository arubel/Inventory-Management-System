import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class FileProcessor  {
	//connection parameters
	private static String MYSQL_DRIVER =  "com.mysql.jdbc.Driver";
	private static String MYSQL_URL    =  "jdbc:mysql://localhost:3306/test";
	private static Connection con;
	private static PreparedStatement  st ;
	
	//variables for IO manipulation
	private static FileReader fr;
	private static BufferedReader br;
	private  static FileWriter fw;
	
	
	public static void processFile (String inFile, String outFile){
		
		try{
			
			//Establish the connection			
			Class.forName(MYSQL_DRIVER);
	        con = DriverManager.getConnection(MYSQL_URL,"","");
			st = con.prepareStatement ("insert into Products (id, pname,price,quantity) " +
					"values (?, ?, ?, ?)");

			
			//create objects for file reading and writing
			fr = new FileReader(inFile);
			br=new BufferedReader(fr);
			fw = new FileWriter(outFile);
			
			//Get date for transaction
			Date dNow = new Date(0);
    	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    	    
    	    // string to hold DML command for mySql
    	    
    	    String strSql=null;
    	    
    	    //Start Reading the file
    		String 	input=br.readLine();
    		
    		while(input!=null){
    			
    			System.out.println(input);
    			
    			
    			
    			String[] values = input.split("\\s*,\\s*");
    			st.setInt(1, Integer.parseInt(values[0]));
    			st.setString(2, values[1]);
    			st.setInt(3, Integer.parseInt(values[2]));
    			st.setInt(4, Integer.parseInt(values[3]));
    			
    			
    			st.executeUpdate();
    						
    			PrintWriter pw=new PrintWriter(fw);	
    			pw.println("Insert"+"  "+ft.format(dNow));
    			input = br.readLine();
       		 		
    		}//end of while loop
    		
    		
    		br.close();
    		fw.close();
    		con.close();
						
		}
		catch(Exception E){
			E.printStackTrace();
			st = null;
		}
		
	}//end of processFile Method
	

}//end of class


//to do Add exception handing for existing table
//if the table exists then just go to the next line
//start inputting intial data