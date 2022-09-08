package adminInterface.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import adminInterface.table.CommentoTable;
import adminInterface.table.FeedTable;
import adminInterface.table.NotiziaTable;
import adminInterface.table.UtenteTable;
import dataBase.MyDataBase;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class SampleController implements Initializable {

	private static String query = " ";

	private static ObservableList<NotiziaTable> oblistNotizia = FXCollections.observableArrayList();
	private static ObservableList<UtenteTable> oblistUtente = FXCollections.observableArrayList();
	private static ObservableList<CommentoTable> oblistCommento = FXCollections.observableArrayList();
	private static ObservableList<FeedTable> oblistFeed = FXCollections.observableArrayList();

	private static Stage stage = null;
	private static Scene scene = null;
	private static Parent root = null;

	private static NotiziaTable notizia = null;
	private static UtenteTable utente = null;
	private static CommentoTable commento = null;
	private static FeedTable feed = null;

	private static PreparedStatement preparedStatement = null;
	private static ResultSet res = null;

	private static MyDataBase dataBase = new MyDataBase();

	@FXML
	private Button btnClose;

	@FXML
	private Pane pnStatus;

	@FXML
	private Text txtPreview;  

	@FXML
	private GridPane gpnNotizia;
	@FXML
	private Button btnNotizia;
	@FXML 
	private Button btnAddNotizia;
	@FXML
	private Button btnRefreshNotizia;
	@FXML
	private TextField txtRicercaNotizia;
	@FXML
	private TableView<NotiziaTable> tblNotizia;
	@FXML
	private TableColumn<NotiziaTable, Integer> cl_idNotizia;
	@FXML
	private TableColumn<NotiziaTable, String> cl_linkNotizia;
	@FXML
	private TableColumn<NotiziaTable, String> cl_titoloNotizia;
	@FXML
	private TableColumn<NotiziaTable, String> cl_actionNotizia;

	@FXML
	private GridPane gpnUtente;
	@FXML
	private Button btnUtente;
	@FXML 
	private Button btnAddUtente;
	@FXML
	private Button btnRefreshUtente;
	@FXML 
	private TextField txtRicercaUtente;
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
	private TableColumn<UtenteTable, String> cl_actionUtente;

	@FXML
	private GridPane gpnCommento;
	@FXML
	private Button btnCommento;
	@FXML 
	private Button btnAddCommento;
	@FXML
	private Button btnRefreshCommento;
	@FXML
    private TextField txtRicercaCommento;
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
	private TableColumn<CommentoTable, String> cl_actionCommento;
	
	@FXML
	private GridPane gpnFeed;
	@FXML
	private Button btnFeed;
	@FXML 
	private Button btnAddFeed;
	@FXML
	private Button btnRefreshFeed;
	@FXML
    private TextField txtRicercaFeed;
	@FXML
	private TableView<FeedTable> tblFeed;
	@FXML
	private TableColumn<FeedTable, Integer> cl_idFeed;
	@FXML
	private TableColumn<FeedTable, String> cl_tipoFeed;
	@FXML
	private TableColumn<FeedTable, String> cl_linkFeed;
	@FXML
	private TableColumn<FeedTable, String> cl_actionFeed;

	@FXML
	private Button btnLogout;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		changeToEditable();

		Connection conn = dataBase.connectionDB();
		try {
			populateNotizia(conn);
			populateUtente(conn);
			populateCommento(conn);
			populateFeed(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		txtRicercaNotizia.toFront();
		tblNotizia.toFront();
		btnAddNotizia.toFront();
		btnRefreshNotizia.toFront();
	}
	
	public void clearAll() {
		oblistNotizia.clear();
		oblistUtente.clear();
		oblistCommento.clear();
		oblistFeed.clear();
	}

	public void changeToEditable() {
		tblFeed.setEditable(true);
		tblCommento.setEditable(true);
		tblUtente.setEditable(true);
		tblNotizia.setEditable(true);
	}

	@FXML
	private void handleClicks(ActionEvent event) throws IOException {
		if(event.getSource() == btnNotizia){
			pnStatus.setBackground(new Background(new BackgroundFill(Color.rgb(255, 64, 64), CornerRadii.EMPTY, Insets.EMPTY)));

			txtPreview.setText("Notizia");
			
			txtRicercaNotizia.toFront();
			tblNotizia.toFront();
			btnAddNotizia.toFront();
			btnRefreshNotizia.toFront();
		}
		else if(event.getSource() == btnUtente) {
			pnStatus.setBackground(new Background(new BackgroundFill(Color.rgb(255, 64, 64), CornerRadii.EMPTY, Insets.EMPTY)));

			txtPreview.setText("Utente");

			txtRicercaUtente.toFront();
			tblUtente.toFront();
			btnAddUtente.toFront();
			btnRefreshUtente.toFront();
		}
		else if(event.getSource() == btnCommento) {
			pnStatus.setBackground(new Background(new BackgroundFill(Color.rgb(255, 64, 64), CornerRadii.EMPTY, Insets.EMPTY)));

			txtPreview.setText("Commento");
			
			txtRicercaCommento.toFront();
			tblCommento.toFront();
			btnAddCommento.toFront();
			btnRefreshCommento.toFront();
		}
		else if(event.getSource() == btnFeed) {
			pnStatus.setBackground(new Background(new BackgroundFill(Color.rgb(255, 64, 64), CornerRadii.EMPTY, Insets.EMPTY)));

			txtPreview.setText("Feed");
			
			txtRicercaFeed.toFront();
			tblFeed.toFront();
			btnAddFeed.toFront();
			btnRefreshFeed.toFront();
		}
		else {
			root = FXMLLoader.load(getClass().getResource("/adminInterface/fxml/Login.fxml"));
			
			scene = new Scene(root);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
			
			clearAll();
		}
	}
	
	public void populateNotizia(Connection conn) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT * FROM Notizia");

		while(res.next()) {
			oblistNotizia.add(new NotiziaTable(res.getInt("NotiziaID"), res.getString("Titolo"), res.getString("Link")));
			tblNotizia.setItems(oblistNotizia);
		}

		load("Notizia", conn);
	}
	
	public void populateUtente(Connection conn) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT * FROM Utente");

		while(res.next()) {
			oblistUtente.add(new UtenteTable(res.getInt("UtenteID"), res.getString("Nickname"), res.getString("Password"), res.getBoolean("Subscription")));
			tblUtente.setItems(oblistUtente);	
		}

		load("Utente", conn);
	}
	
	public void populateCommento(Connection conn) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT * FROM Commento");

		while(res.next()) {
			oblistCommento.add(new CommentoTable(res.getInt("CommentoID"), res.getString("Recensione"), res.getInt("UtenteID"), res.getInt("NotiziaID")));
			tblCommento.setItems(oblistCommento);
		}

		load("Commento", conn);
	}
	
	public void populateFeed(Connection conn) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT * FROM Feed");

		while(res.next()) {
			oblistFeed.add(new FeedTable(res.getInt("FeedID"), res.getString("Tipo"), res.getString("Link")));
			tblFeed.setItems(oblistFeed);
		}

		load("Feed", conn);
	}

	private void load(String object, Connection conn) {
		switch(object)
		{
		case ("Notizia"):
		cl_idNotizia.setCellValueFactory(new PropertyValueFactory<>("id"));
		cl_titoloNotizia.setCellValueFactory(new PropertyValueFactory<>("Titolo"));
		cl_linkNotizia.setCellValueFactory(new PropertyValueFactory<>("Link"));

		setCallbackNotizia(conn);
		break;

		case ("Utente"):
		cl_idUtente.setCellValueFactory(new PropertyValueFactory<>("id"));
		cl_nicknameUtente.setCellValueFactory(new PropertyValueFactory<>("Nickname"));
		cl_passwordUtente.setCellValueFactory(new PropertyValueFactory<>("Password"));
		cl_subscriptionUtente.setCellValueFactory(new PropertyValueFactory<>("Subscription"));

		setCallbackUtente(conn);
		break;

		case ("Commento"):
		cl_idCommento.setCellValueFactory(new PropertyValueFactory<>("id"));
		cl_recensioneCommento.setCellValueFactory(new PropertyValueFactory<>("Recensione"));
		cl_utenteIdCommento.setCellValueFactory(new PropertyValueFactory<>("UtenteId"));
		cl_notiziaIdCommento.setCellValueFactory(new PropertyValueFactory<>("NotiziaId"));

		setCallbackCommento(conn);
		break;

		case ("Feed"):
		cl_idFeed.setCellValueFactory(new PropertyValueFactory<>("id"));
		cl_tipoFeed.setCellValueFactory(new PropertyValueFactory<>("Tipo"));
		cl_linkFeed.setCellValueFactory(new PropertyValueFactory<>("Link"));

		setCallbackFeed(conn);
		break;
		}
	}
	
	@FXML
	private void getAddView(MouseEvent event) throws IOException {
		Parent parent = null;

		if(event.getSource() == btnAddNotizia) {
			parent = FXMLLoader.load(getClass().getResource("/adminInterface/fxml/AddNotizia.fxml"));
		}
		else if(event.getSource() == btnAddUtente) {
			parent = FXMLLoader.load(getClass().getResource("/adminInterface/fxml/AddUtente.fxml"));
		}
		else if(event.getSource() == btnAddCommento) {
			parent = FXMLLoader.load(getClass().getResource("/adminInterface/fxml/AddCommento.fxml"));
		}
		else if(event.getSource() == btnAddFeed) {
			parent = FXMLLoader.load(getClass().getResource("/adminInterface/fxml/AddFeed.fxml"));
		}

		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	private void searchNotizia() {   
		FilteredList<NotiziaTable> filteredData = new FilteredList<>(oblistNotizia, b -> true);

		txtRicercaNotizia.textProperty().addListener((observable, newValue, oldValue) -> {
			filteredData.setPredicate(info -> {
				if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
					return true;
				}
				
				String search = newValue.toLowerCase();
				
				if(info.getTitolo().toLowerCase().indexOf(search) > -1) {
					return true;
				}
				else if(info.getLink().toLowerCase().indexOf(search) > -1) {
					return true;
				}
				else {
					return false;
				}
			});
		});
		
		SortedList<NotiziaTable> sorted = new SortedList<>(filteredData);
		sorted.comparatorProperty().bind(tblNotizia.comparatorProperty());
		tblNotizia.setItems(sorted);
	}  
	
	@FXML
	private void searchUtente() {   
		FilteredList<UtenteTable> filteredData = new FilteredList<>(oblistUtente, b -> true);

		txtRicercaUtente.textProperty().addListener((observable, newValue, oldValue) -> {
			filteredData.setPredicate(user -> {
				if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
					return true;
				}
				
				String subscription = String.valueOf(user.getSubscription());
				String search = newValue.toLowerCase();
				
				if(user.getNickname().toLowerCase().indexOf(search) > -1) {
					return true;
				}
				else if(user.getPassword().toLowerCase().indexOf(search) > -1) {
					return true;
				}
				else if(subscription.indexOf(search) > -1) {
					return true;
				}
				else {
					return false;
				}
			});
		});
		
		SortedList<UtenteTable> sorted = new SortedList<>(filteredData);
		sorted.comparatorProperty().bind(tblUtente.comparatorProperty());
		tblUtente.setItems(sorted);
	}  
	
	@FXML
	private void searchCommento() {   
		FilteredList<CommentoTable> filteredData = new FilteredList<>(oblistCommento, b -> true);

		txtRicercaCommento.textProperty().addListener((observable, newValue, oldValue) -> {
			filteredData.setPredicate(comment -> {
				if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
					return true;
				}
				
				String idUser = String.valueOf(comment.getUtenteId());
				String idInfo = String.valueOf(comment.getNotiziaId());
				String search = newValue.toLowerCase();
				
				if(idUser.indexOf(search) > -1) {
					return true;
				}
				else if(idInfo.indexOf(search) > -1) {
					return true;
				}
				else if(comment.getRecensione().toLowerCase().indexOf(search) > -1) {
					return true;
				}
				else {
					return false;
				}
			});
		});
		
		SortedList<CommentoTable> sorted = new SortedList<>(filteredData);
		sorted.comparatorProperty().bind(tblCommento.comparatorProperty());
		tblCommento.setItems(sorted);
	}  
	
	@FXML
	private void searchFeed() {   
		FilteredList<FeedTable> filteredData = new FilteredList<>(oblistFeed, b -> true);

		txtRicercaFeed.textProperty().addListener((observable, newValue, oldValue) -> {
			filteredData.setPredicate(fdl -> {
				if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
					return true;
				}
				
				String search = newValue.toLowerCase();
				
				if(fdl.getTipo().toLowerCase().indexOf(search) > -1) {
					return true;
				}
				else if(fdl.getLink().toLowerCase().indexOf(search) > -1) {
					return true;
				}
				else {
					return false;
				}
			});
		});
		
		SortedList<FeedTable> sorted = new SortedList<>(filteredData);
		sorted.comparatorProperty().bind(tblFeed.comparatorProperty());
		tblFeed.setItems(sorted);
	}  

	private void setCallbackNotizia(Connection conn) { // Generics
		Callback<TableColumn<NotiziaTable, String>, TableCell<NotiziaTable, String>> cellFoctory = (TableColumn<NotiziaTable, String> param) -> {
			final TableCell<NotiziaTable, String> cell = new TableCell<NotiziaTable, String>() {
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setGraphic(null);
						setText(null);
					} 
					else {

						FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
						FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

						deleteIcon.setStyle(
								" -fx-cursor: hand ;"
										+ "-glyph-size:28px;"
										+ "-fx-fill:#ff1744;"
								);

						editIcon.setStyle(
								" -fx-cursor: hand ;"
										+ "-glyph-size:28px;"
										+ "-fx-fill:#00E676;"
								);

						deleteIcon.setOnMouseClicked((MouseEvent event) -> {
							notizia = tblNotizia.getSelectionModel().getSelectedItem();
							
							try { 
								dataBase.deleteTable("Notizia", notizia.getId());
								refreshTableNotizia();
								refreshTableCommento();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
							alertEliminate();
						});

						editIcon.setOnMouseClicked((MouseEvent event) -> {
							FXMLLoader loader = new FXMLLoader ();
							notizia = tblNotizia.getSelectionModel().getSelectedItem();
							loader.setLocation(getClass().getResource("/adminInterface/fxml/AddNotizia.fxml"));

							try {		
								loader.load();

								AddNotiziaController addNotizia = loader.getController();
								addNotizia.setUpdate(true);
								addNotizia.setTextField(notizia.getId(), notizia.getTitolo(), notizia.getLink());
								
								Parent parent = loader.getRoot();
								Stage stage = new Stage();
								stage.setScene(new Scene(parent));
								stage.initStyle(StageStyle.UTILITY);
								stage.show();
							} catch (IOException e) {
								e.printStackTrace();
							}							
						});

						HBox managebtn = new HBox(editIcon, deleteIcon);
						managebtn.setStyle("-fx-alignment:center");
						HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
						HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

						setGraphic(managebtn);
						setText(null);
					}
				}
			};

			return cell;
		};

		cl_actionNotizia.setCellFactory(cellFoctory);
		tblNotizia.setItems(oblistNotizia);
	}

	private void setCallbackUtente(Connection conn) {
		Callback<TableColumn<UtenteTable, String>, TableCell<UtenteTable, String>> cellFoctory = (TableColumn<UtenteTable, String> param) -> {
			final TableCell<UtenteTable, String> cell = new TableCell<UtenteTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setGraphic(null);
						setText(null);
					} 
					else {

						FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
						FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

						deleteIcon.setStyle(
								" -fx-cursor: hand ;"
										+ "-glyph-size:28px;"
										+ "-fx-fill:#ff1744;"
								);

						editIcon.setStyle(
								" -fx-cursor: hand ;"
										+ "-glyph-size:28px;"
										+ "-fx-fill:#00E676;"
								);

						deleteIcon.setOnMouseClicked((MouseEvent event) -> {
							utente = tblUtente.getSelectionModel().getSelectedItem();
							
							try { 
								dataBase.deleteTable("Utente", utente.getId());
								refreshTableUtente();
								refreshTableCommento();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
							alertEliminate();
						});

						editIcon.setOnMouseClicked((MouseEvent event) -> {
							FXMLLoader loader = new FXMLLoader ();
							utente = tblUtente.getSelectionModel().getSelectedItem();
							loader.setLocation(getClass().getResource("/adminInterface/fxml/AddUtente.fxml"));

							try {
								loader.load();

								AddUtenteController addUtente = loader.getController();
								addUtente.setUpdate(true);
								addUtente.setTextField(utente.getId(), utente.getNickname(), utente.getPassword(), utente.getSubscription());

								Parent parent = loader.getRoot();
								Stage stage = new Stage();
								stage.setScene(new Scene(parent));
								stage.initStyle(StageStyle.UTILITY);
								stage.show();
							} catch (IOException e) {
								e.printStackTrace();
							}							
						});

						HBox managebtn = new HBox(editIcon, deleteIcon);
						managebtn.setStyle("-fx-alignment:center");
						HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
						HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

						setGraphic(managebtn);
						setText(null);
					}
				}
			};

			return cell;
		};

		cl_actionUtente.setCellFactory(cellFoctory);
		tblUtente.setItems(oblistUtente);
	}

	private void setCallbackCommento(Connection conn) {
		Callback<TableColumn<CommentoTable, String>, TableCell<CommentoTable, String>> cellFoctory = (TableColumn<CommentoTable, String> param) -> {
			final TableCell<CommentoTable, String> cell = new TableCell<CommentoTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setGraphic(null);
						setText(null);
					} 
					else {

						FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
						FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

						deleteIcon.setStyle(
								" -fx-cursor: hand ;"
										+ "-glyph-size:28px;"
										+ "-fx-fill:#ff1744;"
								);

						editIcon.setStyle(
								" -fx-cursor: hand ;"
										+ "-glyph-size:28px;"
										+ "-fx-fill:#00E676;"
								);

						deleteIcon.setOnMouseClicked((MouseEvent event) -> {
							commento = tblCommento.getSelectionModel().getSelectedItem();

							try { 
								dataBase.deleteTable("Commento", commento.getId());
								refreshTableCommento();
							} catch (SQLException e) {
								e.printStackTrace();
							}							
						});

						editIcon.setOnMouseClicked((MouseEvent event) -> {
							FXMLLoader loader = new FXMLLoader ();
							commento = tblCommento.getSelectionModel().getSelectedItem();
							loader.setLocation(getClass().getResource("/adminInterface/fxml/AddCommento.fxml"));

							try {
								loader.load();
							
								AddCommentoController addCommento = loader.getController();
								addCommento.setUpdate(true);
								addCommento.setTextField(commento.getId(), commento.getRecensione(), commento.getUtenteId(), commento.getNotiziaId());

								Parent parent = loader.getRoot();
								Stage stage = new Stage();
								stage.setScene(new Scene(parent));
								stage.initStyle(StageStyle.UTILITY);
								stage.show();

							} catch (IOException e) {
								e.printStackTrace();
							}
						});

						HBox managebtn = new HBox(editIcon, deleteIcon);
						managebtn.setStyle("-fx-alignment:center");
						HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
						HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

						setGraphic(managebtn);
						setText(null);
					}
				}
			};

			return cell;
		};

		cl_actionCommento.setCellFactory(cellFoctory);
		tblCommento.setItems(oblistCommento);
	}

	private void setCallbackFeed(Connection conn) {
		Callback<TableColumn<FeedTable, String>, TableCell<FeedTable, String>> cellFoctory = (TableColumn<FeedTable, String> param) -> {
			final TableCell<FeedTable, String> cell = new TableCell<FeedTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setGraphic(null);
						setText(null);
					} 
					else {

						FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
						FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

						deleteIcon.setStyle(
								" -fx-cursor: hand ;"
										+ "-glyph-size:28px;"
										+ "-fx-fill:#ff1744;"
								);

						editIcon.setStyle(
								" -fx-cursor: hand ;"
										+ "-glyph-size:28px;"
										+ "-fx-fill:#00E676;"
								+ "background-color: #FF4040;"
								);

						deleteIcon.setOnMouseClicked((MouseEvent event) -> {
							feed = tblFeed.getSelectionModel().getSelectedItem();
							
							try { 
								dataBase.deleteTable("Feed", feed.getId());
								refreshTableFeed();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
							alertEliminate();
						});

						editIcon.setOnMouseClicked((MouseEvent event) -> {
							FXMLLoader loader = new FXMLLoader ();
							feed = tblFeed.getSelectionModel().getSelectedItem();
							loader.setLocation(getClass().getResource("/adminInterface/fxml/AddFeed.fxml"));

							try {
								loader.load();

								AddFeedController addFeed = loader.getController();
								addFeed.setUpdate(true);
								addFeed.setId(feed.getId());
								addFeed.setTextField(feed.getId(), feed.getTipo(), feed.getLink());

								Parent parent = loader.getRoot();
								Stage stage = new Stage();
								stage.setScene(new Scene(parent));
								stage.initStyle(StageStyle.UTILITY);
								stage.show();
							} catch (IOException e) {
								e.printStackTrace();
							}
						});

						HBox managebtn = new HBox(editIcon, deleteIcon);
						managebtn.setStyle("-fx-alignment:center");
						HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
						HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

						setGraphic(managebtn);
						setText(null);
					}
				}
			};

			return cell;
		};

		cl_actionFeed.setCellFactory(cellFoctory);
		tblFeed.setItems(oblistFeed);
	}
	
	public void alertEliminate() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Eliminazione eseguita correttamente!");
		alert.showAndWait();
	}

	@FXML
	private void refreshTableNotizia() throws SQLException {
		oblistNotizia.clear();
		Connection conn = dataBase.connectionDB();

		query = "SELECT * FROM Notizia";
		preparedStatement = conn.prepareStatement(query);
		res = preparedStatement.executeQuery();

		while(res.next()) {
			oblistNotizia.add(new NotiziaTable(res.getInt("NotiziaID"), res.getString("Titolo"), res.getString("Link")));
			tblNotizia.setItems(oblistNotizia);
		}
	}
	
	@FXML
	private void refreshTableUtente() throws SQLException {
		oblistUtente.clear();
		Connection conn = dataBase.connectionDB();

		query = "SELECT * FROM Utente";
		preparedStatement = conn.prepareStatement(query);
		res = preparedStatement.executeQuery();

		while (res.next()){
			oblistUtente.add(new UtenteTable(res.getInt("UtenteID"), res.getString("Nickname"), res.getString("Password"), res.getBoolean("Subscription")));
			tblUtente.setItems(oblistUtente);
		}
	}
	
	@FXML
	private void refreshTableCommento() throws SQLException {
		oblistCommento.clear();
		Connection conn = dataBase.connectionDB();

		query = "SELECT * FROM Commento";
		preparedStatement = conn.prepareStatement(query);
		res = preparedStatement.executeQuery();

		while (res.next()){
			oblistCommento.add(new CommentoTable(res.getInt("CommentoID"), res.getString("Recensione"), res.getInt("UtenteID"), res.getInt("NotiziaID")));
			tblCommento.setItems(oblistCommento);
		}
	}
	
	@FXML
	private void refreshTableFeed() throws SQLException {
		oblistFeed.clear();
		Connection conn = dataBase.connectionDB();

		query = "SELECT * FROM Feed";
		preparedStatement = conn.prepareStatement(query);
		res = preparedStatement.executeQuery();

		while (res.next()){
			oblistFeed.add(new FeedTable(res.getInt("FeedID"), res.getString("Tipo"), res.getString("Link")));
			tblFeed.setItems(oblistFeed);
		}	
	}

	@FXML
	private void closeButtonClicked(MouseEvent event) {
		if(event.getSource() == btnClose) {
			System.exit(0);
		}
	}
}