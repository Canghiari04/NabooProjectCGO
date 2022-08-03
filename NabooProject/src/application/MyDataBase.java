package application;

import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.*;

public class MyDataBase // TODO: Commentare e mettere nominativi in inglese
{
	static String url = "jdbc:mysql://localhost:3306/naboocgo", username = "root", password = "2905192704";
	static String tabUtente = "Utente", tabNotizia = "Notizia", tabCommento = "Commento";
	
	/*
	* 
	* Metodo ConnectionDB che restituisce la connessione con localhost:3306, che permettera' la modifica del database naboocgo.
	* 
	*/
	
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
	
	/*
    * Metodo InsertTable che permette l'inserimento di nuovi record
    *
    * Introdotta la variabile table per rendere il metodo piu' flessibile, oltre anche all'inserimento dei diversi input a seconda della tabella presa in considerazione.
    */
	
	public void InsertTable(String table, String firstInput, String secondInput, String thirdInput, String fourthInput) 
	{
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;
		
		String query = "";
		
		try
		{			
			switch(table)
			{
				case "Utente":
					
					query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "', '" + thirdInput + "')"; 
					
					break;
					
				case "Notizia":
					
					query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "', '" + thirdInput + "')"; 
					
					break;
					
				case "Commento":
					
					query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "', '" + thirdInput + "')"; 
					
					break;		
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
	
	/*
	* Metodo DeleteTable permette l'eliminazione dei record all'interno delle tabelle.
	*
	* Introdotte due variabili in input per scelta organizzativa, dato che principalmente le tabelle prevedono due colonne significative ognuna.
	*/
	
	public void DeleteTable(String table, String firstInput, String secondInput)
	{
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;
		
		String query = "";
		
		try
		{
			switch(table)
			{
				case "Utente":
					
					query = "DELETE FROM " + table + " WHERE  Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'"; 
					
					break;
					
				case "Notizia":
					
					query = "DELETE FROM " + table + " WHERE  Titolo = '" + firstInput + "' AND Link = '" + secondInput + "'";
					
					break;
					
				case "Commento":
					
					query = "DELETE FROM " + table + " WHERE  UtenteID = '" + firstInput + "' AND NotiziaID = '" + secondInput + "'";
					
					break;		
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
	
	/*
	 * 
	 * Metodo getID restituisce l'ID del record specificato a seconda di quale sia la tabella del database/
	 *
	 */
	
	public int getID(String table, String idTable, String firstInput, String secondInput) // Introdotti parametri cosi generalizzati per rendere il metodo piu' dinamico possibile
	{
		Connection conn = ConnectionDB();
	    Statement st = null;
	    ResultSet rs = null;
		
		String query = "";
		int value = 0;
		
		try
		{	
			switch(table)
			{
				case "Utente":
					
					query = "SELECT " + idTable + " FROM " + table +" WHERE  Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'";
					
					break;
					
				case "Notizia":
					
					query = "SELECT " + idTable + " FROM " + table +" WHERE  Titolo = '" + firstInput + "' AND Link = '" + secondInput + "'";
					
					break;
					
				case "Commento":
					
					query = "SELECT " + idTable + " FROM " + table +" WHERE  UtenteID = '" + firstInput + "' AND NotiziaID = '" + secondInput + "'";
					
					break;		
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