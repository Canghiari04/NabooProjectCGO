package adminInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dataBase.MyDataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ControllerSample implements Initializable {
	
	//TODO: guardare video per ricerca dinamica
	
	private static MyDataBase dataBase = new MyDataBase();
	private static ObservableList<NotiziaTable> oblistNotizia = FXCollections.observableArrayList();
	private static ObservableList<UtenteTable> oblistUtente = FXCollections.observableArrayList();
	private static ObservableList<CommentoTable> oblistCommento = FXCollections.observableArrayList();
	private static ObservableList<FeedTable> oblistFeed = FXCollections.observableArrayList();
    
    private static Stage stage = null;
    private static Scene scene = null;
    private static Parent root = null;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnCommento;

    @FXML
    private Button btnFeed;

    @FXML
    private Button btnFunzioni;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnNotizia;

    @FXML
    private Button btnUtente;

    @FXML
    private ComboBox<String> cmbFunzioni;

    @FXML
    private GridPane gpnCommento;

    @FXML
    private GridPane gpnFeed;

    @FXML
    private GridPane gpnNotizia;

    @FXML
    private GridPane gpnUtente;

    @FXML
    private Pane pnStatus;

    @FXML
    private TextField txtFunzioni;

    @FXML
    private Text txtPreview;
    
    @FXML
    private TableView<NotiziaTable> tblNotizia;
    @FXML
    private TableColumn<NotiziaTable, Integer> cl_idNotizia;
    @FXML
    private TableColumn<NotiziaTable, String> cl_linkNotizia;
    @FXML
    private TableColumn<NotiziaTable, String> cl_titoloNotizia;
    
    @FXML
    private TableView<UtenteTable> tblUtente;
    @FXML
    private TableColumn<UtenteTable, Integer> cl_idUtente;
    @FXML
    private TableColumn<UtenteTable, String> cl_nicknameUtente;
    @FXML
    private TableColumn<UtenteTable, String> cl_passwordUtente;
    @FXML
    private TableColumn<UtenteTable, Boolean> cl_subscriptionUtente;
    
    @FXML
    private TableView<CommentoTable> tblCommento;
    @FXML
    private TableColumn<CommentoTable, Integer> cl_idCommento;
    @FXML
    private TableColumn<CommentoTable, String> cl_recensioneCommento;
    @FXML
    private TableColumn<CommentoTable, String> cl_utenteIdCommento;
    @FXML
    private TableColumn<CommentoTable, Boolean> cl_notiziaIdCommento;
    
    @FXML
    private TableView<FeedTable> tblFeed;
    @FXML
    private TableColumn<FeedTable, Integer> cl_idFeed;
    @FXML
    private TableColumn<FeedTable, String> cl_tipoFeed;
    @FXML
    private TableColumn<FeedTable, String> cl_linkFeed;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		/*
		 * Popolamento della ComboBox
		 */
		
		String[] items = {"Aggiungi", "Modifica", "Elimina", "Ricerca"};
		cmbFunzioni.getItems().addAll(items);
		
		/*
		 * Popolamento della tabelle
		 */
		
		Connection conn = dataBase.ConnectionDB();
		try {
			populateNotizia(conn);
			populateCommento(conn);
			populateFeed(conn);
			populateUtente(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void populateNotizia(Connection conn) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT * FROM Notizia");
			
		while(res.next()) {
			oblistNotizia.add(new NotiziaTable(res.getInt("NotiziaID"), res.getString("Titolo"), res.getString("Link")));
		}
			
		cl_idNotizia.setCellValueFactory(new PropertyValueFactory<>("id"));
		cl_titoloNotizia.setCellValueFactory(new PropertyValueFactory<>("Titolo"));
		cl_linkNotizia.setCellValueFactory(new PropertyValueFactory<>("Link"));
	 
		tblNotizia.setItems(oblistNotizia);
	}
	
	private void populateCommento(Connection conn) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT * FROM Commento");
			
		while(res.next()) {
			oblistCommento.add(new CommentoTable(res.getInt("CommentoID"), res.getString("Recensione"), res.getInt("UtenteID"), res.getInt("NotiziaID")));
		}
		
		cl_idCommento.setCellValueFactory(new PropertyValueFactory<>("id"));
		cl_recensioneCommento.setCellValueFactory(new PropertyValueFactory<>("Recensione"));
		cl_utenteIdCommento.setCellValueFactory(new PropertyValueFactory<>("UtenteId"));
		cl_notiziaIdCommento.setCellValueFactory(new PropertyValueFactory<>("NotiziaId"));
		
		tblCommento.setItems(oblistCommento);	
	}
	
	private void populateFeed(Connection conn) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT * FROM Feed");
			
		while(res.next()) {
			oblistFeed.add(new FeedTable(res.getInt("FeedID"), res.getString("Tipo"), res.getString("Link")));
		}
		
		cl_idFeed.setCellValueFactory(new PropertyValueFactory<>("id"));
		cl_tipoFeed.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
		cl_linkFeed.setCellValueFactory(new PropertyValueFactory<>("Link"));
		
		tblFeed.setItems(oblistFeed);
	}

	private void populateUtente(Connection conn) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT * FROM Utente");
			
		while(res.next()) {
			oblistUtente.add(new UtenteTable(res.getInt("UtenteID"), res.getString("Nickname"), res.getString("Password"), res.getBoolean("Subscription")));
		}
		
		cl_idUtente.setCellValueFactory(new PropertyValueFactory<>("id"));
		cl_nicknameUtente.setCellValueFactory(new PropertyValueFactory<>("Nickname"));
		cl_passwordUtente.setCellValueFactory(new PropertyValueFactory<>("Password"));
		cl_subscriptionUtente.setCellValueFactory(new PropertyValueFactory<>("Subscription"));
		
		tblUtente.setItems(oblistUtente);	
	}

	@FXML
	private void handleClicks(ActionEvent event) throws IOException{
		if(event.getSource() == btnNotizia){
			txtPreview.setText("Notizia");
			pnStatus.setBackground(new Background(new BackgroundFill(Color.rgb(255, 64, 64), CornerRadii.EMPTY, Insets.EMPTY)));			
			gpnNotizia.toFront();
		}
		else if(event.getSource() == btnCommento) {
			txtPreview.setText("Commento");
			pnStatus.setBackground(new Background(new BackgroundFill(Color.rgb(255, 64, 64), CornerRadii.EMPTY, Insets.EMPTY)));
			gpnCommento.toFront();

		}
		else if(event.getSource() == btnFeed) {
			txtPreview.setText("Feed");
			pnStatus.setBackground(new Background(new BackgroundFill(Color.rgb(255, 64, 64), CornerRadii.EMPTY, Insets.EMPTY)));
			gpnFeed.toFront();
		}
		else if
		(event.getSource() == btnUtente) {
			txtPreview.setText("Utente");
			pnStatus.setBackground(new Background(new BackgroundFill(Color.rgb(255, 64, 64), CornerRadii.EMPTY, Insets.EMPTY)));
			gpnUtente.toFront();
		}
		else {
			root = FXMLLoader.load(getClass().getResource("/adminInterface/Login.fxml"));

			scene = new Scene(root);

			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}
	}
	
	@FXML
	private void closeButtonClicked(MouseEvent event) {
		if(event.getSource() == btnClose) {
			System.exit(0);
		}
	}
}