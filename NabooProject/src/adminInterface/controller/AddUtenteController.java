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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AddUtenteController  implements Initializable {
	@FXML
	private TextField txtNickname;
	@FXML
	private TextField txtPassword;
	
	@FXML
	private ComboBox<String> cmbSubscription;
	
	@FXML 
	private Button btnSalva;
	@FXML 
	private Button btnCancella;

	private MyDataBase dataBase = new MyDataBase();

	private boolean update = false;
	private int utenteId;

	/*
	 * Metodo initialize in cui viene riempita la ComboBox degli unici due valore
	 * consentiti nel campo subscription.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	    cmbSubscription.getItems().addAll("true", "false");
	}

	/*
	 * Metodo setDatabase che permette l'aggiunta o la modifica dei record all'interno 
	 * del database correlato.
	 */
	@FXML
	private void setDatabase(MouseEvent event) throws HeadlessException, SQLException {
		if(event.getSource() == btnSalva)
		{
			String nickname = txtNickname.getText();
			String password = txtPassword.getText();
			String sub = cmbSubscription.getValue();
			System.out.println(sub);

			if (nickname.isEmpty() || password.isEmpty() || sub.isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Inserisci tutti i dati");
				alert.showAndWait();
			} else {		        	
				if(update == false) {
					dataBase.insertTable("Utente", nickname, password, sub);
					alertAdd();
				}
				else {
					dataBase.alterRow("Utente", utenteId, nickname, password, sub);
					alertModify();
				}

				txtNickname.setText(null);
				txtPassword.setText(null);
				cmbSubscription.setAccessibleText(null);
			}
		}
		else if(event.getSource() == btnCancella) {
			txtNickname.setText(null);
			txtPassword.setText(null);
			cmbSubscription.setValue(null);
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
	void setTextField(int id, String nickname, String password, boolean sub) {
		utenteId = id;
		txtNickname.setText(nickname);
		txtPassword.setText(password);
		cmbSubscription.setValue(String.valueOf(sub));
	}

	/*
	 * Setta la variabile id del classe determinata, a scopo di avanzare la richiesta di una possibile
	 * modifica.
	 */
	void setId(int id) {
		this.utenteId = id;
	}
}