package dataBase;

import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.io.FeedException;

public class MyDataBase // TODO: Commentare e mettere nominativi comuni e in inglese
{
	private String url = "jdbc:mysql://localhost:3306/naboocgo", username = "root", password = "2905192704";
	private static ArrayList<String> feedUtente = new ArrayList<String>();
	private static ArrayList<String> feedTot = new ArrayList<String>();

	public String replace(String str) {
		String s = str;
		s = s.replaceAll("'", "\\\\'");

		return s;
	}

	/*
	 * 
	 * Metodo ConnectionDB che restituisce la connessione con localhost:3306, che
	 * permettera' la modifica del database naboocgo.
	 * 
	 */

	public Connection ConnectionDB() {
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			return connection;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}

	/*
	 * Metodo InsertTable che permette l'inserimento di nuovi record
	 *
	 * Introdotta la variabile table per rendere il metodo piu' flessibile, oltre
	 * anche all'inserimento dei diversi input a seconda della tabella presa in
	 * considerazione.
	 */

	public void InsertTable(String table, String firstInput, String secondInput, String thirdInput) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;

		String query = "";

		switch (table) {
			case "Utente":
				boolean subscription = Boolean.parseBoolean(thirdInput);
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "', " + subscription + ")";
				break;
	
			case "Notizia":
				firstInput = replace(firstInput);
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "')";
				break;
	
			case "Commento":
				int idUtente = Integer.parseInt(secondInput);
				int idNotizia = Integer.parseInt(thirdInput);
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + idUtente + "', '" + idNotizia + "')";
				break;
		}

		preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * Metodo DeleteTable permette l'eliminazione dei record all'interno delle
	 * tabelle.
	 *
	 * Introdotte due variabili in input per scelta organizzativa, dato che
	 * principalmente le tabelle prevedono due colonne significative ognuna.
	 */
	
	public void DeleteTable(String table, String firstInput, String secondInput) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;

		String query = "";

		switch (table) {
			case "Utente":
				query = "DELETE FROM " + table + " WHERE Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'";
				break;
	
			case "Notizia":
				firstInput = replace(firstInput);
				query = "DELETE FROM " + table + " WHERE Titolo = '" + firstInput + "' AND Link = '" + secondInput + "'";
				break;
	
			case "Commento":
				int utenteId = Integer.parseInt(firstInput);
				int notiziaId = Integer.parseInt(secondInput);
				query = "DELETE FROM " + table + " WHERE UtenteID = '" + utenteId + "' AND NotiziaID = '" + notiziaId + "'";
				break;
		}

		preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * 
	 * Metodo getID restituisce l'ID del record specificato a seconda di quale sia
	 * la tabella del database
	 *
	 */

	public void alterRow(String table, String idTable, String firstInput, String secondInput, String thirdInput, int ID) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;

		String query = " ";

		switch (table) {
			case "Utente":
				boolean subscription = Boolean.parseBoolean(thirdInput);
				query = "UPDATE " + table + " SET  Nickname = '" + firstInput + "', Password = '" + secondInput + "', Subscription = " + subscription + " WHERE " + idTable + " = '" + ID + "';";
				break;
	
			case "Notizia":
				query = "UPDATE " + table + " SET  Titolo = '" + firstInput + "', Link = '" + secondInput + "' WHERE " + idTable + " = '" + ID + "';";
				break;
	
			case "Commento":
				query = "UPDATE " + table + " SET  UtenteID = '" + firstInput + "', NotiziaID = '" + secondInput + "' WHERE " + idTable + " = '" + ID + "';";
				break;
		}

		preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * Introdotti parametri cosi generalizzati per rendere il metodo piu' dinamico possibile
	 */
	
	public boolean contains(String table, String firstInput, String secondInput) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = "";
		
		switch (table) {
		case "Utente":
			query = "SELECT * FROM " + table + " WHERE  Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'";
			break;
				
		case "Amministratore":
			query = "SELECT * FROM " + table + " WHERE  Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'";
			break;

		case "Notizia":
			firstInput = replace(firstInput);
			query = "SELECT * FROM " + table + " WHERE  Titolo = '" + firstInput + "' AND Link = '" + secondInput + "'";
			break;

		case "Commento":
			int idUtente = Integer.parseInt(firstInput);
			int idNotizia = Integer.parseInt(secondInput);
			query = "SELECT * FROM " + table + " WHERE  UtenteID = '" + idUtente + "' AND NotiziaID = '" + idNotizia + "'";
			break;
		}

		st = conn.createStatement();
		rs = st.executeQuery(query);
			
		return rs.next();			
	}

	public int countNotizia(int notiziaId)  throws SQLException, HeadlessException{
		Connection conn = ConnectionDB();
		Statement preparedStmt = null;
		ResultSet result = null;

		String query = " ";
		
		int value = 0;
		
		query = "SELECT COUNT(NotiziaID) AS Count FROM Commento WHERE NotiziaID = '" + notiziaId + "'";

		preparedStmt = conn.createStatement();
		result = preparedStmt.executeQuery(query);

		while (result.next()) {
			value = result.getInt("Count");
		}

		conn.close();

		return value;
	}

	public int countUtente(int utenteId) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		Statement preparedStmt = null;
		ResultSet result = null;

		String query = " ";
		
		int value = 0;

		query = "SELECT COUNT(UtenteID) AS Count FROM Commento WHERE UtenteID = '" + utenteId + "'";

		preparedStmt = conn.createStatement();
		result = preparedStmt.executeQuery(query);

		while (result.next()) {
			value = result.getInt("Count");
		}

		conn.close();

		return value;
	}
	
	public int getID(String table, String idTable, String firstInput, String secondInput) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = "";
		
		int value = 0;

		switch (table) {
			case "Utente":
				query = "SELECT " + idTable + " FROM " + table + " WHERE  Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'";
				break;
	
			case "Notizia":
				firstInput = replace(firstInput);
				query = "SELECT " + idTable + " FROM " + table + " WHERE  Titolo = '" + firstInput + "' AND Link = '" + secondInput + "'";
				break;
	
			case "Commento":
				int utenteId = Integer.parseInt(firstInput);
				int notiziaId = Integer.parseInt(secondInput);
				query = "SELECT " + idTable + " FROM " + table + " WHERE  UtenteID = '" + utenteId + "' AND NotiziaID = '" + notiziaId + "'";
				break;
		}

		st = conn.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			value = rs.getInt(idTable);
		}

		conn.close();

		return value;
	}

	public boolean getSubscription(String table, String firstInput, String secondInput) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		Statement st = null;
		ResultSet rs = null;
		
		String query = " ";
		
		boolean value = false;
		
		query = "SELECT Subscription FROM  " + table + " WHERE Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'"; 
			
		st = conn.createStatement();
		rs = st.executeQuery(query);
			
		while (rs.next()) {
			value = rs.getBoolean("Subscription");			
		}
			
		conn.close();
		
		return value;
	}
	
	public String[] getNotizia(int idNotizia) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = " ";
		String[] values = new String[2];
		String valueTitolo = " ";
		String valueLink = " ";

		query = "SELECT Titolo, Link FROM Notizia WHERE  NotiziaID = '" + idNotizia + "'";

		st = conn.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			valueTitolo = rs.getString("Titolo");
			valueLink = rs.getString("Link");
		}

		values[0] = valueTitolo;
		values[1] = valueLink;
		conn.close();

		return values;
	}
	
	/*
	 * Metodo getRecensione che ritorna un'unica recensione in relazione alla notizia e all'utente
	 */
	
	public String getRecensione(String table, String firstInput, String secondInput) throws SQLException, HeadlessException  {
		Connection conn = ConnectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = " ";
		String value = " ";
		
		int idUtente = Integer.parseInt(firstInput);
		int idNotizia = Integer.parseInt(secondInput);
		
		query = "SELECT Recensione FROM " + table + " WHERE  UtenteID = '" + idUtente + "' AND NotiziaID = '" + idNotizia + "'";

		st = conn.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			value = rs.getString("Recensione");
		}
		
		conn.close();

		return value;
	}

	/*
	 * Metodo getRecensioni specificato per ottenere la visualizzazione delle proprie recensioni rilasciate, oppure di una determinata notizia 
	 */
	
	public ArrayList<String> getRecensioni(String table, String firstInput, String str) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		Statement st = null;
		ResultSet rs = null;
		
		String query = " ";
		String value = " ";
		ArrayList<String> arrayNotizia = new ArrayList<String>();
		
		int ID = Integer.parseInt(firstInput);

		switch (str)
		{
			case "mineComment":
				query = "SELECT * FROM Commento WHERE UtenteID = '" + ID + "'";
				break;
			case "comment":
				query = "SELECT * FROM Commento WHERE NotiziaID = '" + ID + "'";
				break;
		}
			
		st = conn.createStatement();
		rs = st.executeQuery(query);
		
		while(rs.next()){
			value = rs.getString("Recensione");
			arrayNotizia.add(value);
		}
			
		conn.close();
		
		return arrayNotizia;
	}
	
	public SendMessage getResponse(SendMessage response, String tabUtente, String idUtente, String nickName, String password) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		Statement st = null, newSt = null, allSt = null;
		ResultSet rs = null, newRs = null, allRs = null; 
		
		String query = " ", newQuery = " ", allQuery = " ", str = " ";

		int value = 0;
		int utenteId = getID(tabUtente, idUtente, nickName, password);
		
		response.setText("Le feed associate al tuo account sono le seguenti: \n");

		query = "SELECT FeedID FROM UtenteFeed WHERE UtenteID = '" + utenteId + "';";
		allQuery = "SELECT Tipo FROM Feed;";
			
		st = conn.createStatement();
		newSt = conn.createStatement();
		rs = st.executeQuery(query);
			
		while (rs.next()) {
			value = rs.getInt("FeedID");
				
			newQuery = "SELECT Tipo FROM Feed WHERE FeedID = '" + value + "';";
			newRs = newSt.executeQuery(newQuery);
				
			while (newRs.next()) {
				str = newRs.getString("Tipo");
				feedUtente.add(str);
				response.setText(response.getText() + str + "\n");
			}
		}
			
		response.setText(response.getText() + "\n");
		response.setText(response.getText() + "Le feed disponibili sono: \n");
			
		allSt = conn.createStatement();
		allRs = allSt.executeQuery(allQuery);
			
		while (allRs.next()) {
			str = allRs.getString("Tipo");
			feedTot.add(str);
			
			if (!feedUtente.contains(str)) {
				response.setText(response.getText() + str + "\n");
			}
		}
						
		conn.close();
		
		return response;
	}

	public ArrayList<String> getTitleFromComment(int utenteId) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		Statement st = null, newSt = null;
		ResultSet rs = null, newRs = null;
		
		String str = " ";
		String query = " ";
		String newQuery = " ";
		ArrayList<String> arrayTitolo = new ArrayList<String>();

		int value = 0;
		
		st = conn.createStatement();
		newSt = conn.createStatement();
			
		query = "SELECT NotiziaID FROM Commento WHERE UtenteID = '" + utenteId + "';";
		rs = st.executeQuery(query);
			
		while(rs.next()) {
			value = rs.getInt("NotiziaID");
				
			newQuery = "SELECT Titolo FROM Notizia WHERE NotiziaID = '" + value + "';";
			newRs = newSt.executeQuery(newQuery);
				
			while(newRs.next()) {
				str = newRs.getString("Titolo");
				arrayTitolo.add(str);
			}
		}
			
		conn.close();
	
		return arrayTitolo;
	}
	
	public List<SyndFeed> getFeeds(int utenteId, List<SyndFeed> feeds, FeedFetcher fetcher) throws SQLException, IllegalArgumentException, IOException, FeedException, FetcherException, HeadlessException {
		MyDataBase dataBase = new MyDataBase();
		
		Connection conn = dataBase.ConnectionDB();
		Statement st = null, newSt = null;
		ResultSet rs = null, newRs = null;	
			
		String query = "SELECT FeedID FROM UtenteFeed WHERE UtenteID = '" + utenteId + "'";
		String str = " ";
			
		int value = 0;
				
		st = conn.createStatement();
		newSt = conn.createStatement();
		rs = st.executeQuery(query);
	
		while (rs.next()) {
			value = rs.getInt("FeedID");
			String newQuery = "SELECT Link FROM Feed WHERE FeedID = '" + value + "'";
			newRs = newSt.executeQuery(newQuery);

			while (newRs.next()) {
				str = newRs.getString("Link");
				feeds.add(fetcher.retrieveFeed(new URL(str)));					
			}				
		}
		return feeds;
	}
	
	public ArrayList<String> getFeedsTot() throws SQLException, IllegalArgumentException, HeadlessException {
		MyDataBase dataBase = new MyDataBase();
		
		Connection conn = dataBase.ConnectionDB();
		Statement st = null;
		ResultSet rs = null;	 
		
		ArrayList<String> arrayFeeds = new ArrayList<String>();

		String query = "SELECT Tipo FROM Feed;";			
		String value = " ";
				
		st = conn.createStatement();
		rs = st.executeQuery(query);
	
		while (rs.next()) {
			value = rs.getString("Tipo");
			arrayFeeds.add(value);					
		}
		
		conn.close();
		
		return arrayFeeds;
	}
	
	public void setRecensione(String table, String firstInput, String secondInput, String thirdInput) throws SQLException, HeadlessException {
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;

		String query = " ";
		
		int idUtente = Integer.parseInt(secondInput);
		int idNotizia = Integer.parseInt(thirdInput);
		
		query = "UPDATE " + table + " SET Recensione = '" + firstInput + "' WHERE  UtenteID = '" + idUtente + "' AND NotiziaID = '" + idNotizia + "'";

		preparedStmt = conn.prepareStatement(query); 
		preparedStmt.execute();
			
		conn.close();
	}

	public void setFeed(int utenteId, boolean answer, String[] tokens) throws SQLException, HeadlessException {		
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;
		Statement st = null;
		ResultSet rs = null;
		
		String query = " ", queryInsert = " ";
		int feedId = 0;
			
		st = conn.createStatement();
			
			for(String str : tokens) {
				if(answer == true) {
					if((!feedUtente.contains(str)) && feedTot.contains(str)) {
						query = "SELECT FeedID FROM Feed WHERE Tipo = '" + str + "';";
						rs = st.executeQuery(query);
						
						while(rs.next()) {
							feedId = rs.getInt("FeedID");
						}
						
						queryInsert = "INSERT INTO UtenteFeed VALUES (null, '" + utenteId + "', '" + feedId + "');"; 
						preparedStmt = conn.prepareStatement(queryInsert); 
						preparedStmt.execute();
					}
				}
				else {
					if(feedUtente.contains(str)) {						
						query = "SELECT FeedID FROM Feed WHERE Tipo = '" + str + "';";
						rs = st.executeQuery(query);
						
						while(rs.next()) {
							feedId = rs.getInt("FeedID");
						}
						
						queryInsert = "DELETE FROM UtenteFeed WHERE FeedID = '" + feedId + "';"; 
						preparedStmt = conn.prepareStatement(queryInsert);
						preparedStmt.execute();
						
						feedUtente.remove(str);
					}
				}
			}
		conn.close();
	}
}