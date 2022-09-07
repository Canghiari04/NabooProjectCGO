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

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	    cmbSubscription.getItems().addAll("true", "false");
	}

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
				}
				else {
					dataBase.alterRow("Utente", utenteId, nickname, password, sub);
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

	void setUpdate(boolean b) {
		this.update = b;
	}

	void setTextField(int id, String nickname, String password, boolean sub) {
		utenteId = id;
		txtNickname.setText(nickname);
		txtPassword.setText(password);
		cmbSubscription.setValue(String.valueOf(sub));
	}

	void setId(int id) {
		this.utenteId = id;
	}
}