package adminInterface;


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
				}
				else {
					dataBase.alterRow("Feed", feedId, tipo, link, null);
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

	void setUpdate(boolean b) {
		this.update = b;
	}

	void setTextField(int id, String tipo, String link) {
		feedId = id;
		txtTipo.setText(tipo);
		txtLink.setText(link);
	}

	void setId(int id) {
		this.feedId = id;
	}
}