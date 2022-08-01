package application;

import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.*;

public class MyDataBase 
{
	static String url = "jdbc:mysql://localhost:3306/naboocgo", username = "root", password = "2905192704";
	
	public Connection ConnectionDB()
	{
        try 
        {
            Connection connection = DriverManager.getConnection(url, username, password);            
            return connection;
        }
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }      
	}
	
	public void InsertTable(String table, String nickName, String password, String sub)
	{
		Connection conn = ConnectionDB();
		
		try
		{
			String insert = "INSERT INTO " + table + " VALUES (null, '" + nickName + "', '" + password + "', '" + sub + "')"; // Variabile table per rendere maggiormente dinamico l'inserimento, evitando di scrivere codice ridondante
			PreparedStatement preparedStmt = conn.prepareStatement(insert); // TODO: cercare a cosa serve concretamente

		    preparedStmt.execute();
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (HeadlessException e)
		{
			e.printStackTrace();
		}
	}
	
	public void deleteTable(String table, String nickName, String password, String sub)
	{
		Connection conn = ConnectionDB();
		
		try
		{
			String insert = "DELETE FROM " + table + " WHERE  Nickname = '" + nickName + "' AND Password = '" + password + "' AND Sub = '" + sub + "'"; // Variabile table per rendere maggiormente dinamico l'inserimento, evitando di scrivere codice ridondante
			PreparedStatement preparedStmt = conn.prepareStatement(insert); // TODO: cercare a cosa serve concretamente

		    preparedStmt.execute();
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (HeadlessException e)
		{
			e.printStackTrace();
		}
	}
}