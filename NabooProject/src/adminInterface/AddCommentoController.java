package adminInterface;

import java.awt.HeadlessException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dataBase.MyDataBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AddCommentoController implements Initializable {
	@FXML
	private TextField txtRecensione;
	
	@FXML
	private ComboBox<String> cmbIdUtente;
	@FXML
	private ComboBox<String> cmbIdNotizia;
	
	@FXML 
	private Button btnSalva;
	@FXML 
	private Button btnCancella;

	private MyDataBase dataBase = new MyDataBase();
	
	private ArrayList<String> arrayIdUtente;
	private ArrayList<String> arrayIdNotizia;

	private boolean update = false;
	private int commentoId;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			arrayIdUtente = dataBase.getListId("Utente");
			arrayIdNotizia = dataBase.getListId("Notizia");
		} catch (HeadlessException | IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		} 
		
		for(String str : arrayIdUtente) {
			cmbIdUtente.getItems().add(str);
		}
		
		for(String str : arrayIdNotizia) {
			cmbIdNotizia.getItems().add(str);
		}
	}

	@FXML
	private void setDatabase(MouseEvent event) throws HeadlessException, SQLException {
		if(event.getSource() == btnSalva)
		{
			String recensione = txtRecensione.getText();
			String utenteId = cmbIdUtente.getValue();
			String notiziaId = cmbIdNotizia.getValue();

			if (recensione.isEmpty() || utenteId.isEmpty() || notiziaId.isEmpty()){
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Inserisci tutti i dati");
				alert.showAndWait();
			} else {
				if(update == false) {
					dataBase.insertTable("Commento", recensione, utenteId, notiziaId);
				}
				else {
					dataBase.alterRow("Commento", commentoId, recensione, utenteId, notiziaId);
				}

				txtRecensione.setText(null);
				cmbIdUtente.setValue(null);
				cmbIdNotizia.setValue(null);
			}
		}
		else if(event.getSource() == btnCancella) {
			txtRecensione.setText(null);
			cmbIdUtente.setValue(null);
			cmbIdNotizia.setValue(null);
		}
	}

	void setUpdate(boolean b) {
		this.update = b;
	}

	void setTextField(int id, String recensione, int utenteId, int notiziaId) {
		commentoId = id;
		txtRecensione.setText(recensione);
		cmbIdUtente.setValue(String.valueOf(utenteId));
		cmbIdNotizia.setValue(String.valueOf(notiziaId));
	}

	void setId(int id) {
		this.commentoId = id;
	}
}