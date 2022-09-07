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
	private static ArrayList<String> feedUser = new ArrayList<String>();
	private static ArrayList<String> feedTot = new ArrayList<String>();

    /*
	 * Metodo Replace che permette la modifica di alcuni parametri per rendere 
	 * possibile l'inserimento di dati all'interno del database.
	 */
	public String replace(String str) {
		String s = str;
		s = s.replaceAll("'", "\\\\'");

		return s;
	}

	/*
	 * Metodo connectionDB che restituisce la connessione con localhost:3306, che
	 * permettera' la modifica del database naboocgo. 
	 */
	public Connection connectionDB() {
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			return connection;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}

	/*
	 * Metodo InsertTable che permette l'inserimento di nuovi record.
	 * Introdotta la variabile table per rendere il metodo piu' flessibile, oltre
	 * anche all'inserimento dei diversi parametri a seconda della tabella presa in
	 * consIderazione.
	 */
	public void insertTable(String table, String firstInput, String secondInput, String thirdInput) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		PreparedStatement preparedStmt = null;

		String query = "";

		switch (table) {
			case "Utente":
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "', " + Boolean.parseBoolean(thirdInput) + ")";
				break;
	
			case "Notizia":
				query = "INSERT INTO " + table + " VALUES (null, '" + replace(firstInput) + "', '" + secondInput + "')";
				break;
	
			case "Commento":
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + Integer.parseInt(secondInput) + "', '" + Integer.parseInt(thirdInput) + "')";
				break;
				
			case "Feed":
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "')";
				break;
		}

		preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * Metodo DeleteTable permette l'eliminazione dei record all'interno delle
	 * tabelle.
	 * Introdotte due variabili in input per scelta organizzativa, dato che
	 * principalmente ogni tabella prevedono due colonne significative.
	 */
	public void deleteTable(String table, int Id) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		PreparedStatement preparedStmt = null;
		PreparedStatement newPreparedStmt = null;

		String query = "";
		String queryComment = " ";

		switch (table) {
			case "Utente":
				query = "DELETE FROM " + table + " WHERE UtenteId = '" + Id + "'";
				queryComment = "DELETE FROM Commento WHERE UtenteId = '" + Id + "'";
				
				newPreparedStmt = conn.prepareStatement(queryComment);
				newPreparedStmt.execute();
				break;
	
			case "Notizia":
				query = "DELETE FROM " + table + " WHERE NotiziaId = '" + Id + "'";
				queryComment = "DELETE FROM Commento WHERE NotiziaId = '" + Id + "'";
				
				newPreparedStmt = conn.prepareStatement(queryComment);
				newPreparedStmt.execute();
				break;
	
			case "Commento":
				query = "DELETE FROM " + table + " WHERE CommentoId = '" + Id + "'";
				break;
				
			case "Feed":
				query = "DELETE FROM " + table + " WHERE FeedId = '" + Id + "'";
				break;
		}

		preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * Metodo getId restituisce l'Id del record specificato a seconda di quale sia
	 * la tabella del database.
	 */
	public void alterRow(String table, int Id, String firstInput, String secondInput, String thirdInput) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		PreparedStatement preparedStmt = null;

		String query = " ";

		switch (table) {
			case "Utente":
				query = "UPDATE " + table + " SET  Nickname = '" + firstInput + "', Password = '" + secondInput + "', Subscription = " + Boolean.parseBoolean(thirdInput) + " WHERE  UtenteId= '" + Id + "';";
				break;
	
			case "Notizia":
				query = "UPDATE " + table + " SET  Titolo = '" + firstInput + "', Link = '" + secondInput + "' WHERE NotiziaId = '" + Id + "';";
				break;
	
			case "Commento":
				query = "UPDATE " + table + " SET  Recensione = '" + firstInput + "', UtenteId = '" + secondInput + "', NotiziaId = '" + thirdInput + "' WHERE CommentoId = '" + Id + "';";
				break;
				
			case "Feed":
				query = "UPDATE " + table + " SET  Tipo = '" + firstInput + "', Link = '" + secondInput + "' WHERE FeedId = '" + Id + "';";
				break;
		}

		preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * Metodo contains che restituisce true o false a seconda che le diverse tabelle contegano
	 * quei dati o meno, nei differenti record che le compongono.
	 */
	public boolean contains(String table, String firstInput, String secondInput) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
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
				query = "SELECT * FROM " + table + " WHERE  Titolo = '" +  replace(firstInput) + "' AND Link = '" + secondInput + "'";
				break;
	
			case "Commento":
				query = "SELECT * FROM " + table + " WHERE  UtenteId = '" + Integer.parseInt(firstInput) + "' AND NotiziaId = '" + Integer.parseInt(secondInput) + "'";
				break;
	
			case "Feed":
				query = "SELECT * FROM " + table + " WHERE Tipo = '" + firstInput + "';";
				break;
		}

		st = conn.createStatement();
		rs = st.executeQuery(query);
			
		return rs.next();			
	}

	/*
	 * Metodo GetId che restituisce Id del record, in base a quale sia la tabella
	 * specificata come parametro.
	 */
	public int getId(String table, String IdTable, String firstInput, String secondInput) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = "";
		
		int value = 0;

		switch (table) {
			case "Utente":
				query = "SELECT " + IdTable + " FROM " + table + " WHERE  Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'";
				break;
	
			case "Notizia":
				query = "SELECT " + IdTable + " FROM " + table + " WHERE  Titolo = '" + replace(firstInput) + "' AND Link = '" + secondInput + "'";
				break;
	
			case "Commento":
				query = "SELECT " + IdTable + " FROM " + table + " WHERE  UtenteId = '" + Integer.parseInt(firstInput) + "' AND NotiziaId = '" + Integer.parseInt(secondInput) + "'";
				break;
		}

		st = conn.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			value = rs.getInt(IdTable);
		}

		conn.close();

		return value;
	}

	/*
	 * Metodo GetSubscription che restituisce true o false, a seconda che si tratti di
	 * un profilo premium piuttosto che base.
	 */
	public boolean getSubscription(String table, String firstInput, String secondInput) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
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
	
	/*
	 * Metodo getNews che restituisce l'insieme di tutte le notizie contenute nella.
	 * tabella Notizia del database
	 */
	public String[] getNews(int newId) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = " ";
		String valueTitolo = " ", valueLink = " ";
		String[] values = new String[2];

		query = "SELECT Titolo, Link FROM Notizia WHERE NotiziaId = '" + newId + "'";

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
	 * Metodo getRecensione che ritorna un'unica recensione in relazione alla notizia e all'utente.
	 */
	public String getComment(String table, String firstInput, String secondInput) throws SQLException, HeadlessException  {
		Connection conn = connectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = " ";
		String value = " ";
		
		query = "SELECT Recensione FROM " + table + " WHERE  UtenteId = '" + Integer.parseInt(firstInput) + "' AND NotiziaId = '" + Integer.parseInt(secondInput) + "'";

		st = conn.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			value = rs.getString("Recensione");
		}
		
		conn.close();

		return value;
	}
	
	/*
	 * Metodo getComments che restituisce tutti i commenti relativi al metodo viewComment,
	 * collegati allo stesso profilo che abbia fatto l'accesso, oppure alla notizia letta.
	 */
	public ArrayList<String> getComments(String table, String firstInput, String str) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null;
		ResultSet rs = null;
		
		String query = " ";
		String value = " ";
		ArrayList<String> arrayNews = new ArrayList<String>();

		switch (str)
		{
			case "mineComment":
				query = "SELECT * FROM Commento WHERE UtenteId = '" + Integer.parseInt(firstInput) + "'";
				break;
			case "comment":
				query = "SELECT * FROM Commento WHERE NotiziaId = '" + Integer.parseInt(firstInput) + "'";
				break;
		}
			
		st = conn.createStatement();
		rs = st.executeQuery(query);
		
		while(rs.next()){
			value = rs.getString("Recensione");
			arrayNews.add(value);
		}
			
		conn.close();
		
		return arrayNews;
	}

	/*
	 * Metodo getTitleFromComment che restuisce ogni titolo di una notizia associota 
	 * ad una recensione, soprattutto in ottica del metodo viewComment personale.
	 */
	public ArrayList<String> getTitleFromComment(int utenteId) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null, newSt = null;
		ResultSet rs = null, newRs = null;
		
		int value = 0;
		
		ArrayList<String> arrayTitolo = new ArrayList<String>();
		String str = " ";
		String query = "SELECT NotiziaId FROM Commento WHERE UtenteId = '" + utenteId + "';";
		String newQuery = " ";

		
		st = conn.createStatement();
		newSt = conn.createStatement();
			
		rs = st.executeQuery(query);
			
		while(rs.next()) {
			value = rs.getInt("NotiziaId");
				
			newQuery = "SELECT Titolo FROM Notizia WHERE NotiziaId = '" + value + "';";
			newRs = newSt.executeQuery(newQuery);
				
			while(newRs.next()) {
				str = newRs.getString("Titolo");
				arrayTitolo.add(str);
			}
		}
			
		conn.close();
	
		return arrayTitolo;
	}
	
	/*
	 * Metodo getFeedUser che restituisce l'elenco della feed personalizzata 
	 * per la lettura delle notizie che creano maggiore interesse.
	 */
	public SendMessage getFeedUser(SendMessage response, String tabUser, String idUser, String nickName, String password) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null, newSt = null, allSt = null;
		ResultSet rs = null, newRs = null, allRs = null; 
		
		int value = 0;
		int userId = getId(tabUser, idUser, nickName, password);
		
		String query = "SELECT FeedId FROM UtenteFeed WHERE UtenteId = '" + userId + "';";
		String allQuery = "SELECT Tipo FROM Feed;";
		String newQuery = " ", str = " ";

		response.setText("Le feed associate al tuo account sono le seguenti: \n");

		st = conn.createStatement();
		newSt = conn.createStatement();
		rs = st.executeQuery(query);
			
		while (rs.next()) {
			value = rs.getInt("FeedId");
				
			newQuery = "SELECT Tipo FROM Feed WHERE FeedId = '" + value + "';";
			newRs = newSt.executeQuery(newQuery);
				
			while (newRs.next()) {
				str = newRs.getString("Tipo");
				feedUser.add(str);
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
			
			if (!feedUser.contains(str)) {
				response.setText(response.getText() + str + "\n");
			}
		}
						
		conn.close();
		
		return response;
	}
	
	/*
	 * Metodo getFeeds che restituisce l'insieme delle feed che non sono associate 
	 * alla feed personalizzata 
	 */
	public List<SyndFeed> getFeeds(int utenteId, List<SyndFeed> feeds, FeedFetcher fetcher) throws SQLException, IllegalArgumentException, IOException, FeedException, FetcherException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null, newSt = null;
		ResultSet rs = null, newRs = null;	
		
		int value = 0;
			
		String query = "SELECT FeedId FROM UtenteFeed WHERE UtenteId = '" + utenteId + "'";
		String str = " ";
			
		st = conn.createStatement();
		newSt = conn.createStatement();
		rs = st.executeQuery(query);
	
		while (rs.next()) {
			value = rs.getInt("FeedId");
			String newQuery = "SELECT Link FROM Feed WHERE FeedId = '" + value + "'";
			newRs = newSt.executeQuery(newQuery);

			while (newRs.next()) {
				str = newRs.getString("Link");
				feeds.add(fetcher.retrieveFeed(new URL(str)));					
			}				
		}
		
		return feeds;
	}
	
	/*
	 * Metodo getFeeds che restituisce l'insieme delle feed contenute dal database.
	 */
	public ArrayList<String> getFeedsTot() throws SQLException, IllegalArgumentException, HeadlessException {
		Connection conn = connectionDB();
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
	
	public String getLink(String tipo) throws SQLException, IllegalArgumentException, HeadlessException {		
		Connection conn = connectionDB();
		Statement st = null;
		ResultSet rs = null;	 

		String query = "SELECT Link FROM Feed WHERE Tipo = '" + tipo + "';";			
		String value = " ";
				
		st = conn.createStatement();
		rs = st.executeQuery(query);
	
		while (rs.next()) {
			value = rs.getString("Link");
		}
		
		conn.close();
		
		return value;
	}
	
	/*
	 * Metodo getListId che restituisce tutti gli Id associati alla tabelle
	 * Notizia e Utente, per la visualizzazione all'interno delle Combobox
	 */
	public ArrayList<String> getListId(String table) throws SQLException, IllegalArgumentException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null;
		ResultSet rs = null;	 
		
		ArrayList<String> array = new ArrayList<String>();

		String query = " ", value = " ";
		
		switch(table) {
			case "Utente":
				query = "SELECT UtenteId FROM Utente";
				
				st = conn.createStatement();
				rs = st.executeQuery(query);
			
				while (rs.next()) {
					value = rs.getString("UtenteId");
					array.add(value);					
				}
				break;
			
			case "Notizia":
				query = "SELECT NotiziaId FROM Notizia";
				
				st = conn.createStatement();
				rs = st.executeQuery(query);
			
				while (rs.next()) {
					value = rs.getString("NotiziaId");
					array.add(value);					
				}
				break;
		}
		
		conn.close();
		
		return array;
	}

	public void setComment(String table, String firstInput, String secondInput, String thirdInput) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		PreparedStatement preparedStmt = null;

		String query = " ";
		
		query = "UPDATE " + table + " SET Recensione = '" + firstInput + "' WHERE  UtenteId = '" + Integer.parseInt(secondInput) + "' AND NotiziaId = '" + Integer.parseInt(thirdInput) + "'";

		preparedStmt = conn.prepareStatement(query); 
		preparedStmt.execute();
			
		conn.close();
	}

	public void setFeed(int utenteId, boolean answer, String[] tokens) throws SQLException, HeadlessException {		
		Connection conn = connectionDB();
		PreparedStatement preparedStmt = null;
		Statement st = null;
		ResultSet rs = null;
		
		int feedId = 0;

		String query = " ", queryInsert = " ";
			
		st = conn.createStatement();
			
			for(String str : tokens) {
				if(answer == true) {
					if((!feedUser.contains(str)) && feedTot.contains(str)) {
						query = "SELECT FeedId FROM Feed WHERE Tipo = '" + str + "';";
						rs = st.executeQuery(query);
						
						while(rs.next()) {
							feedId = rs.getInt("FeedId");
						}
						
						queryInsert = "INSERT INTO UtenteFeed VALUES (null, '" + utenteId + "', '" + feedId + "');"; 
						preparedStmt = conn.prepareStatement(queryInsert); 
						preparedStmt.execute();
					}
				}
				else {
					if(feedUser.contains(str)) {						
						query = "SELECT FeedId FROM Feed WHERE Tipo = '" + str + "';";
						rs = st.executeQuery(query);
						
						while(rs.next()) {
							feedId = rs.getInt("FeedId");
						}
						
						queryInsert = "DELETE FROM UtenteFeed WHERE FeedId = '" + feedId + "';"; 
						preparedStmt = conn.prepareStatement(queryInsert);
						preparedStmt.execute();
						
						feedUser.remove(str);
					}
				}
			}
		conn.close();
	}
}