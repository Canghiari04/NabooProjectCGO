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

import feedRSS.Notizia;

public class MyDataBase
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
		PreparedStatement preparedStmt = null; // Oggetto precompilato che permette l'esecuzione multipla di query relative ad un certo database.

		String query = "";

		switch (table) {
			case "Utente":
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "', " + Boolean.parseBoolean(thirdInput) + ")";
				break;
	
			case "Notizia":
				query = "INSERT INTO " + table + " VALUES (null, '" + replace(firstInput) + "', '" + secondInput + "')";
				break;
				
			case "NotiziaPreferita":
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "')";
				break;
	
			case "Commento":
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + Integer.parseInt(secondInput) + "', '" + Integer.parseInt(thirdInput) + "')";
				break;
				
			case "Feed":
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "')";
				break;
		}
		preparedStmt = conn.prepareStatement(query);
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * Metodo DeleteTable permette l'eliminazione dei record all'interno delle tabelle.
	 * Introdotte due variabili in input per scelta organizzativa, dato che
	 * principalmente ogni tabella prevedono due colonne significative.
	 */
	public void deleteTable(String table, int Id) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		PreparedStatement preparedStmt = null, newPreparedStmt = null;

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
				
			case "NotiziaPreferita":
				query = "DELETE FROM " + table + " WHERE NotiziaId = '" + Id + "'";
				break;
	
			case "Commento":
				query = "DELETE FROM " + table + " WHERE CommentoId = '" + Id + "'";
				break;
				
			case "Feed":
				query = "DELETE FROM " + table + " WHERE FeedId = '" + Id + "'";
				break;
		}
		preparedStmt = conn.prepareStatement(query); 
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * Metodo alterRow che permette la modifica del record specificato, in base a quale sia la tabella e i 
	 * parametri avanzati.
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
		preparedStmt = conn.prepareStatement(query);
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * Metodo contains che restituisce true o false a seconda che le diverse tabelle contegano
	 * quei dati o meno, nei differenti record che le compongono.
	 */
	public boolean contains(String table, String firstInput, String secondInput) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null; // Oggetto che permette l'esecuzione di una specifica query, dove lo stesso prevede il suo utilizzo qualora debba riportare un risultato.
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
				
			case "NotiziaPreferita":
				query = "SELECT * FROM " + table + " WHERE  UtenteId = '" + Integer.parseInt(firstInput) + "' AND NotiziaId = '" + Integer.parseInt(secondInput) + "'";
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
	 * Metodo getId che restituisce Id del record, in base a quale sia la tabella
	 * specificata come parametro.
	 */
	public int getId(String table, String tableId, String firstInput, String secondInput) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = "";
		
		int value = 0;

		switch (table) {
			case "Utente":
				query = "SELECT " + tableId + " FROM " + table + " WHERE  Nickname = '" + firstInput + "' AND Password = '" + secondInput + "'";
				break;
	
			case "Notizia":
				query = "SELECT " + tableId + " FROM " + table + " WHERE  Titolo = '" + replace(firstInput) + "' AND Link = '" + secondInput + "'";
				break;
	
			case "Commento":
				query = "SELECT " + tableId + " FROM " + table + " WHERE  UtenteId = '" + Integer.parseInt(firstInput) + "' AND NotiziaId = '" + Integer.parseInt(secondInput) + "'";
				break;
		}

		st = conn.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			value = rs.getInt(tableId);
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
	 * Metodo getNew che restituisce la notizia contenuta nella tabella Notizia del database
	 * che abbia quello stesso id passato per parametro, ritornando prima il titolo e link, 
	 * entrambi stringhe, quindi come unico array contenente entrambi.
	 */
	public String[] getNew(int newId) throws SQLException, HeadlessException {
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
		conn.close();

		values[0] = valueTitolo;
		values[1] = valueLink;
		
		return values;
	}
	
	/*
	 * Metodo getNewsFav che restituisce l'insieme di tutte le notizie contenute nella
	 * tabella NotiziaPreferita del database, utilizzando la formula dell'INNER JOIN, ossia
	 * accomunando in unica tabella Notizia e NotiziaPreferita, per poi estrapolare solo i dati necessari.
	 */
	public Notizia[] getNewsFav(int newId) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = " ";
		String valueTitolo = " ", valueLink = " ";
		
		ArrayList<Notizia> valueList = new ArrayList<Notizia>();

		query = " SELECT Titolo, Link FROM Notizia INNER JOIN NotiziaPreferita ON NotiziaPreferita.NotiziaID = Notizia.NotiziaID WHERE UtenteID = '" + newId + "';";

		st = conn.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			valueTitolo = rs.getString("Titolo");
			valueLink = rs.getString("Link");
			
			Notizia value = new Notizia(valueTitolo, valueLink);
			valueList.add(value);
		}
		conn.close();

		Notizia[] arrayNews = new Notizia[valueList.size()];
		arrayNews = valueList.toArray(arrayNews);
		
		return arrayNews;
	}
	
	/*
	 * Metodo getComment che ritorna un'unica recensione in relazione alla notizia e all'utente.
	 */
	public String getComment(String table, String firstInput, String secondInput) throws SQLException, HeadlessException {
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
	 * collegati allo stesso profilo che abbia effettuato l'accesso, oppure alla notizia letta.
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
	 * Metodo getTitleFromComment che restituisce ogni titolo di una notizia associata 
	 * ad una recensione, soprattutto in ottica del metodo viewComment personale, ottenendone cosi 
	 * uno storico.
	 */
	public ArrayList<String> getTitleFromComment(int utenteId) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null, newSt = null;
		ResultSet rs = null, newRs = null;
		
		int value = 0;
		
		String str = " ";
		String query = "SELECT NotiziaId FROM Commento WHERE UtenteId = '" + utenteId + "';", newQuery = " ";
		ArrayList<String> arrayTitolo = new ArrayList<String>();
		
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
	 * Metodo getFeedUser che restituisce l'elenco della feed personalizzate
	 * per la lettura delle notizie che possegano maggiore interesse.
	 */
	public SendMessage getFeedUser(SendMessage response, String tabUser, String idUser, String nickName, String password) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		Statement st = null, newSt = null, allSt = null;
		ResultSet rs = null, newRs = null, allRs = null; 
		
		int value = 0;
		int userId = getId(tabUser, idUser, nickName, password);
		
		String query = "SELECT FeedId FROM UtenteFeed WHERE UtenteId = '" + userId + "';", allQuery = "SELECT Tipo FROM Feed;", newQuery = " ";
		String str = " ";

		response.setText("Le feed associate al tuo account sono le seguenti: \n");

		st = conn.createStatement();
		newSt = conn.createStatement();
		rs = st.executeQuery(query);
			
		while (rs.next()) { // Tramite tale ciclo concatenato, vengono aggiunti tutti i feed RSS associati all'utente che abbia effettuato l'accesso.
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
			
		while (allRs.next()) { // Per poi, aggiungere tutte le feed RSS, che ancora non abbiano ricevuto una preferenza.
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
	 * Metodo getFeeds che restituisce l'insieme delle feed associate 
	 * alla feed personalizzata, permettendone cosi il riempimento del fetcher, che 
	 * consentira' la successiva visualizzazione della sezione che possega la preferenza
	 * dell'utente.
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
	 * Metodo getFeedsTot che restituisce l'insieme delle feed contenute dalla tabell Feed.
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
	
	/*
	 * Metodo getLink che ritorna il link del feed RSS indicato, tramite la tipoogia.
	 */
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
	 * Metodo getListId che restituisce tutti gli id associati alla tabelle
	 * Notizia e Utente, per la visualizzazione all'interno delle ComboBox.
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

	/*
	 * Metodo setComment che permette la modifica di una recensione, qualora sia gia' presente,
	 * nei confronti sia dell'utente che della notizia, specificata.
	 */
	public void setComment(String table, String firstInput, String secondInput, String thirdInput) throws SQLException, HeadlessException {
		Connection conn = connectionDB();
		PreparedStatement preparedStmt = null;

		String query = " ";
		
		query = "UPDATE " + table + " SET Recensione = '" + firstInput + "' WHERE  UtenteId = '" + Integer.parseInt(secondInput) + "' AND NotiziaId = '" + Integer.parseInt(thirdInput) + "'";

		preparedStmt = conn.prepareStatement(query); 
		preparedStmt.execute();
			
		conn.close();
	}

	/*
	 * Metodo setFeed che permette di stabilire, da parte dell'utente che abbia effettuato l'accesso,
	 * le proprie preferenze di lettura.
	 */
	public void setFeed(int utenteId, boolean answer, String[] tokens) throws SQLException, HeadlessException {		
		Connection conn = connectionDB();
		PreparedStatement preparedStmt = null;
		Statement st = null;
		ResultSet rs = null;
		
		int feedId = 0;

		String query = " ", queryInsert = " ";
		feedTot = getFeedsTot();
			
		st = conn.createStatement();
			
		for(String str : tokens) {
			if(answer == true) { // Qualora sia espressa "answer" pari a true, significa l'aggiunta del feed RSS nelle preferenze.
				if((!feedUser.contains(str)) && feedTot.contains(str)) { // Controllo che il feed RSS espresso dall'utente, sia contenuto all'interno della tabella Feed.
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
			else { // Qualora sia espressa "answer" pari a false, significa l'eliminazione del feed RSS dalle preferenze.
				if(feedUser.contains(str)) { // Controllo che il feed RSS espresso, sia contenuto nelle preferenze di lettura, ossia nella tabelle UtenteFeed.
					query = "SELECT FeedID FROM Feed WHERE Tipo = '" + str + "';";
					rs = st.executeQuery(query);
					while(rs.next()) {
						feedId = rs.getInt("FeedID");
					}

					queryInsert = "DELETE FROM UtenteFeed WHERE FeedID = '" + feedId + "';"; 
					preparedStmt = conn.prepareStatement(queryInsert);
					preparedStmt.execute();

					feedUser.remove(str);
				}
			}
		}
		conn.close();
	}
}