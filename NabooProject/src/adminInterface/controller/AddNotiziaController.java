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

public class AddNotiziaController implements Initializable {
	@FXML
	private TextField txtTitolo;
	@FXML
	private TextField txtLink;

	@FXML 
	private Button btnSalva;
	@FXML 
	private Button btnCancella;

	private MyDataBase dataBase = new MyDataBase();

	private boolean update = false;
	private int notiziaId;

	@Override
	public void initialize(URL url, ResourceBundle rb) {}

	@FXML
	private void setDatabase(MouseEvent event) throws HeadlessException, SQLException {
		if(event.getSource() == btnSalva)
		{
			String titolo = txtTitolo.getText();
			String link = txtLink.getText();

			if (titolo.isEmpty() || link.isEmpty()){
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Inserisci tutti i dati");
				alert.showAndWait();
			} else {
				titolo.toLowerCase();

				if(update == false) {
					dataBase.insertTable("Notizia", titolo, link, null);
				}
				else {
					dataBase.alterRow("Notizia", notiziaId, titolo, link, null);
				}

				txtTitolo.setText(null);
				txtLink.setText(null);
			}
		}
		else if(event.getSource() == btnCancella) {
			txtTitolo.setText(null);
			txtLink.setText(null);
		}
	}

	void setUpdate(boolean b) {
		this.update = b;
	}

	void setTextField(int id, String titolo, String link) {
		notiziaId = id;
		txtTitolo.setText(titolo);
		txtLink.setText(link);
	}

	void setId(int id) {
		this.notiziaId = id;
	}
}