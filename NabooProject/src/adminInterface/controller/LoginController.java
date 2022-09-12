package adminInterface.controller;


import java.awt.HeadlessException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dataBase.MyDataBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	
	private static MyDataBase dataBase = new MyDataBase();
	
	private static Stage stage;
	private static Scene scene;
	private static Parent root;
	
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnClose;

    @FXML
    private TextField txtNickname;
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Label lblErroreLogin;
    @FXML
    private Label lblErroreNickname;
    @FXML
    private Label lblErrorePassword;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { }
	
	/*
	 * Metodo loginClicked che permette l'accesso alla manipolazione della concretezza dei dati
	 * appartenenti alle differenti tabelle del database.
	 */
	@FXML 
	private void loginClicked(MouseEvent event) throws IOException, HeadlessException, SQLException {
		lblErroreNickname.setText(" ");
		lblErrorePassword.setText(" ");
		
		if(txtNickname.getText().isBlank() && txtPassword.getText().isBlank()) {
			lblErroreLogin.setText("Esegui prima l'accesso!");
			lblErroreNickname.setText("*");
			lblErrorePassword.setText("*");
		}
		else if(txtNickname.getText().isBlank()) {
			lblErroreNickname.setText("*");
		}
		else if(txtPassword.getText().isBlank()) {
			lblErrorePassword.setText("*");
		}
		else {
			String nickname = txtNickname.getText();
			String password = txtPassword.getText();
			
			boolean s = (dataBase.contains("Amministratore", nickname, password));
			System.out.println(s);
			
			if(dataBase.contains("Amministratore", nickname, password)) {
				root = FXMLLoader.load(getClass().getResource("/adminInterface/fxml/Sample.fxml"));
	
				scene = new Scene(root);
					
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();
	
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Bentornato " + nickname + "!");
				alert.setHeaderText("Accesso eseguito correttamente!");
				alert.showAndWait();
			}
			else {
				lblErroreLogin.setText("Credenziali errate!");
			}
		}
	}
	
	/*
	 * Metodo closeButtonClicked che permette la chiusura e l'arresto della parte applicativa
	 * sia dell'amministratore e sia dell'utente.
	 */
	@FXML
	private void closeButtonClicked(MouseEvent event) {
		if(event.getSource() == btnClose) {
			System.exit(0);
		}
	}
	
	/*
	 * Metodo deleteClicked che elimina tutto cio' che e' stato scritto all'interno delle specifiche
	 * TextField appartenenti al file FXML.
	 */
	@FXML 
	private void deleteClicked(MouseEvent event) {
		txtNickname.clear();
		txtPassword.clear();
	}
}