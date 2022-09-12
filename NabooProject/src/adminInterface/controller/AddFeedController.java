package adminInterface.controller;

import java.awt.HeadlessException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dataBase.MyDataBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AddFeedController implements Initializable {

	@FXML
	private TextField txtTipo;
	@FXML
	private TextField txtLink;

	@FXML 
	private Button btnSalva;
	@FXML 
	private Button btnCancella;

	private MyDataBase dataBase = new MyDataBase();

	private boolean update = false;
	private int feedId;

	@Override
	public void initialize(URL url, ResourceBundle rb) {}

	/*
	 * Metodo setDatabase che permette l'aggiunta o la modifica dei record all'interno 
	 * del database correlato.
	 */
	@FXML
	private void setDatabase(MouseEvent event) throws HeadlessException, SQLException {
		if(event.getSource() == btnSalva)
		{
			String tipo = txtTipo.getText();
			String link = txtLink.getText();

			if (tipo.isEmpty() || link.isEmpty()){
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Inserisci tutti i dati");
				alert.showAndWait();
			} else {
				tipo.toLowerCase();

				if(update == false) {
					dataBase.insertTable("Feed", tipo, link, null);
					alertAdd();
				}
				else {
					dataBase.alterRow("Feed", feedId, tipo, link, null);
					alertModify();
				}

				txtTipo.setText(null);
				txtLink.setText(null);
			}
		}
		else if(event.getSource() == btnCancella) {
			txtTipo.setText(null);
			txtLink.setText(null);
		}
	}
	
	/*
	 * Metodo alertAdd che specifica una finestra di messaggio qualora non siano rispettati
	 * i vincoli di inserimento.
	 */
	public void alertAdd() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Aggiunta eseguita correttamente!");
		alert.showAndWait();
	}
	
	/*
	 * Metodo alertModify che specifica una finestra di messaggio qualora non siano rispettati
	 * i vincoli di inserimento.
	 */
	public void alertModify() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Modifica eseguita correttamente!");
		alert.showAndWait();
	}

	/*
	 * Metodo setUpdate che setta la variabile boolean qualora si tratti di un'operazione
	 * di modifica piuttosto che di aggiunta.
	 */
	void setUpdate(boolean b) {
		this.update = b;
	}

	/*
	 * Metodo setTextField che riprende i parametri visualizzati nella riga appartenente alla TableView
	 * per indirizzarli nella visualizzazione del FXML apposito.
	 */
	void setTextField(int id, String tipo, String link) {
		feedId = id;
		txtTipo.setText(tipo);
		txtLink.setText(link);
	}

	/*
	 * Setta la variabile id del classe determinata, a scopo di avanzare la richiesta di una possibile
	 * modifica.
	 */
	void setId(int id) {
		this.feedId = id;
	}
}