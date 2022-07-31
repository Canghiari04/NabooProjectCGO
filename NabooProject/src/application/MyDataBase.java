package application;

import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.*;

public class MyDataBase 
{
	// TODO: implementare le risorse necessarie, video su YouTube
	static String url = "jdbc:mysql://localhost:3306/naboocgo", username = "root", password = "2905192704";
	
	public Connection ConnectionDB()
	{
        System.out.println("Connecting database...");

        try 
        {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }      
	}
	
	public void InsertTable(String table, String nickName, String password)
	{
		Connection conn = ConnectionDB();
		
		try
		{
			String insert = "Insert into " + table + " values (null, '" + nickName + "', '" + password + "')"; // Variabile table per rendere maggiormente dinamico l'inserimento, evitando di scrivere codice ridondante
			
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
