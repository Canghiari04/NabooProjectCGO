package dataBase;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class MyDataBase // TODO: Commentare e mettere nominativi in inglese
{
	static String url = "jdbc:mysql://localhost:3306/naboocgo", username = "root", password = "2905192704";
	static String tabUtente = "Utente", tabNotizia = "Notizia", tabCommento = "Commento";

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
	 * Introdotta la variabile table per rendere il metodo piu' flessibile, oltre anche all'inserimento dei diversi input a seconda della tabella presa in considerazione.
	 */

	public void InsertTable(String table, String firstInput, String secondInput, String thirdInput) {
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;

		String query = "";

		try {
			switch (table) {
			case "Utente":
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "', '" + thirdInput + "')";
				break;

			case "Notizia":
				firstInput = replace(firstInput);
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + secondInput + "')";
				break;

			case "Commento":
				int idUtente = Integer.parseInt(secondInput);
				int idNotizia = Integer.parseInt(thirdInput);
				query = "INSERT INTO " + table + " VALUES (null, '" + firstInput + "', '" + idUtente + "', '"
						+ idNotizia + "')";
				break;
			}

			preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente
			preparedStmt.execute();
			conn.close();
		} catch (SQLException | HeadlessException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo DeleteTable permette l'eliminazione dei record all'interno delle
	 * tabelle.
	 *
	 * Introdotte due variabili in input per scelta organizzativa, dato che
	 * principalmente le tabelle prevedono due colonne significative ognuna.
	 */

	public void DeleteTable(String table, String firstInput, String secondInput) {
		Connection conn = ConnectionDB();
		PreparedStatement preparedStmt = null;

		String query = "";

		try {
			switch (table) {
			case "Utente":
				query = "DELETE FROM " + table + " WHERE  Nickname = '" + firstInput + "' AND Password = '"
						+ secondInput + "'";
				break;

			case "Notizia":
				query = "DELETE FROM " + table + " WHERE  Titolo = '" + firstInput + "' AND Link = '" + secondInput
						+ "'";
				break;

			case "Commento":
				query = "DELETE FROM " + table + " WHERE  UtenteID = '" + firstInput + "' AND NotiziaID = '"
						+ secondInput + "'";
				break;
			}

			preparedStmt = conn.prepareStatement(query); // TODO: cercare a cosa serve concretamente
			preparedStmt.execute();
			conn.close();
		} catch (SQLException | HeadlessException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * Metodo getID restituisce l'ID del record specificato a seconda di quale sia la tabella del database
	 *
	 */

	public int getID(String table, String idTable, String firstInput, String secondInput) // Introdotti parametri cosi generalizzati per rendere il metodo piu' dinamico possibile
	{
		Connection conn = ConnectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = "";
		int value = 0;

		try {
			switch (table) {
			case "Utente":
				query = "SELECT " + idTable + " FROM " + table + " WHERE  Nickname = '" + firstInput+ "' AND Password = '" + secondInput + "'";
				break;

			case "Notizia":
				firstInput = replace(firstInput);
				query = "SELECT " + idTable + " FROM " + table + " WHERE  Titolo = '" + firstInput + "' AND Link = '" + secondInput + "'";
				break;

			case "Commento":
				query = "SELECT " + idTable + " FROM " + table + " WHERE  UtenteID = '" + firstInput + "' AND NotiziaID = '" + secondInput + "'";
				break;
			}

			st = conn.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				value = rs.getInt(idTable);
			}

			conn.close();
		} catch (SQLException | HeadlessException e) {
			e.printStackTrace();
		}

		return value;
	}
	
	public String getRecensione(String table, String firstInput, String secondInput) {
		Connection conn = ConnectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = " ";
		String value = " ";

		try {

			int idUtente = Integer.parseInt(firstInput);
			int idNotizia = Integer.parseInt(secondInput);

			query = "SELECT Recensione FROM " + table + " WHERE  UtenteID = '" + idUtente + "' AND NotiziaID = '" + idNotizia + "'";

			st = conn.createStatement();
			rs = st.executeQuery(query);

			value = rs.getNString(value);
			
			conn.close();
		} catch (SQLException | HeadlessException e) {
			e.printStackTrace();
		}
		
		return value;
	}

	public boolean contains(String table, String firstInput, String secondInput) 
	{
		Connection conn = ConnectionDB();
		Statement st = null;
		ResultSet rs = null;

		String query = "";

		try {
			switch (table) {
			case "Utente":
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
		} catch (SQLException | HeadlessException e) {
			e.printStackTrace();
			return false;
		}
	}
}