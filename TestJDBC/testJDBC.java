import java.sql.*;
import java.util.Calendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
class  testJDBC
{

	public Connection getConnection() throws Exception
	{
		String url = "jdbc:informix-sqli://10.209.188.77:9088/temp:INFORMIXSERVER=ol_informix1170;user=informix;password=wipro@123";
		Connection conn = null;
		try 
		{
			Class.forName("com.informix.jdbc.IfxDriver");
		} 
		catch (Exception e)
		{
			System.out.println("ERROR: failed to load Informix JDBC driver.");
			e.printStackTrace();
			throw e;
		}

		try 
		{
			conn = DriverManager.getConnection(url);
		} 
		catch (SQLException e) 
		{
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return conn;
	}

	public static void runSelect(Connection conn) throws Exception
	{
		String result = null;
		String selectString;

		selectString = "select name1, name2, name3, LOCOPY(name4) name4, name5 from table1";

		try 
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(selectString);
			while (rs.next()) 
			{
			    String name1	= rs.getString("name1");
				int name2		= rs.getInt("name2");
				Blob name3		= rs.getBlob("name3");
				String name4	= rs.getString("name4");
				Timestamp name5	= rs.getTimestamp("name5");

if (name3 != null)
{
System.out.println(name1 + ":" + name2 + ":"+ name5 + ":" + name3.length());
//createFile(name3, name5);
}
else
{
System.out.println(name1 + ":" + name2 + ":"+ name5);
}


			}
			stmt.close();

		} 
		catch(SQLException e) 
		{
			System.err.println("SQLException: " + e.getMessage());
			throw e;
		}

	}

	private static void createFile (Blob _name3, Timestamp _name5) throws Exception
	{
		FileOutputStream fos = new FileOutputStream("E:/test_" + _name5.toString().replace(":", "-")+ ".png");
		
		fos.write(_name3.getBytes((long)1, (int)_name3.length()));

		fos.close();

	}

	public static void runInsert(Connection conn) throws Exception
	{

		//byte [] ba = Calendar.getInstance().getTime().toString().getBytes();

		File file = new File("e:/exess-baggage.png");
		byte [] ba = new byte[(int)file.length()];

		FileInputStream fis = new FileInputStream(file);

		fis.read(ba);

		String insertString1 = "insert into table1 (name1, name2, name3, name5) values(?, ?, ?, ? )";

		try 
		{
			PreparedStatement pstmt = conn.prepareStatement(insertString1);
			pstmt.setString(1, "ABCDEFGHIJ");
			pstmt.setInt(2, 345);
			pstmt.setBytes(3, ba);
			pstmt.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));

	   		
			pstmt.executeUpdate();

			pstmt.close();

		} 
		catch(SQLException e) 
		{
			System.err.println("SQLException: " + e.getMessage());
			throw e;
		}

	}



	public static void main(String[] args) throws Exception
	{
		Connection conn = new testJDBC().getConnection();

		runSelect(conn);
		//runInsert(conn);

		try
		{
			conn.close();
		}
		catch (SQLException e) 
		{
			System.out.println("ERROR closing conn : " + e.getMessage());
			e.printStackTrace();
			return;
		}
System.out.println("Connection closed...");
	}
}
