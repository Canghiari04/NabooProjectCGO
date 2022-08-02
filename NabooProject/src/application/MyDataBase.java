package application;

import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.*;

public class MyDataBase // TODO: Commentare e mettere nominativi in inglese
{
	static String url = "jdbc:mysql://localhost:3306/naboocgo", username = "root", password = "2905192704";
	static String tabUtente = "Utente", tabNotizia = "Notizia", tabCommento = "Commento";
	
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
	
	public void InsertTable(String table, String firstInput, String secondInput, String thirdInput, String fourthInput) // Variabile table per rendere maggiormente dinamico l'inserimento, evitando di scrivere codice ridondante
	{
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;
		
		String query = "";
		
		try
		{			
			if(table == tabUtente) 
			{
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "', '" + thirdInput + "')"; 
			}
			else if(table == tabNotizia)
			{
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "')";
			}
			else if(table == tabCommento)
			{
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "', '" + thirdInput + "', '" + fourthInput + "')"; 
			}
			
			preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente

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
	
	public void deleteTable(String table, String firstInput, String secondInput)
	{
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;
		
		String query = "";
		
		try
		{
			if(table == tabUtente)
			{
				query = "DELETE FROM " + table + " WHERE  Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'"; // Variabile table per rendere maggiormente dinamico l'inserimento, evitando di scrivere codice ridondante
			}
			else if(table == tabNotizia)
			{
				query = "DELETE FROM " + table + " WHERE  Titolo = '" + firstInput + "' AND Link = '" + secondInput + "'";
			}
			else if(table == tabCommento)
			{
				query = "DELETE FROM " + table + " WHERE  UtenteID = '" + firstInput + "' AND NotiziaID = '" + secondInput + "'";
			}
			
			preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente

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
	
	public int getID(String table, String idTable, String firstInput, String secondInput) // Introdotti parametri cosi generalizzati per rendere il metodo piu' dinamico possibile
	{
		Connection conn = ConnectionDB();
	    Statement st = null;
	    ResultSet rs = null;
		
		String query = "";
		int value = 0;
		
		try
		{	
			if(table == tabUtente)
			{
				query = "SELECT " + idTable + " FROM " + table +" WHERE  Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'";
			}
			else if(table == tabNotizia)
			{
				query = "SELECT " + idTable + " FROM " + table +" WHERE  Titolo = '" + firstInput + "' AND Link = '" + secondInput + "'";
			}
			else if(table == tabCommento)
			{
				query = "SELECT " + idTable + " FROM " + table +" WHERE  UtenteID = '" + firstInput + "' AND NotiziaID = '" + secondInput + "'";
			}
			
	        st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while(rs.next())
			{
				value = rs.getInt(idTable);
			}
						
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
		
		return value;
	}
}