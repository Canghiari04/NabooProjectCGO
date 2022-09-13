package botTelegram;

import java.awt.HeadlessException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.io.FeedException;

import botTelegram.response.ResponseFavorite;
import botTelegram.response.ResponseNews;
import botTelegram.response.ResponseOperate;
import dataBase.MyDataBase;
import feedRSS.FeedReader;
import feedRSS.Notizia;

public class MyBot extends TelegramLongPollingBot
{
	private boolean access = false, modify = false, subscription = false;
	
	private int j = 0, userId = 0;
	
	private String emojiiNoEntry = "⛔️", emojiiWellDone = "✅";
	private String nickname = " ", password = " ", function = " ", title = " ", link = " ";
	private String tabUser = "Utente", tabNews = "Notizia", tabComment = "Commento", tabNewsFavorite = "NotiziaPreferita", IdUser = "UtenteId", IdNews = "NotiziaId";
	
	private static Notizia[] arrayNews;
	private ResponseOperate resOperate = new ResponseOperate();
	private ResponseNews resNews = new ResponseNews();
	private ResponseFavorite resFavorite = new ResponseFavorite();
	private MyDataBase dataBase = new MyDataBase();

	public String getBotUsername() {
		return "NabooCGNbot";
	}

	public String getBotToken() {
		return "5480067721:AAGGX2yduLoYjRtek0G0lqppg5H6bu10hlE";
	}
	
	public void onUpdateReceived(Update update) {
		try {
			if(update.hasMessage() && update.getMessage().hasText()) {
				String str = update.getMessage().getText(); // Istanza di una variabile stringa per permettere la navigazione nelle differenti funzioni.
				long chatId = update.getMessage().getChatId(); // Riferimento alla chat Telegram in utilizzo, assegnato poi ai differenti SendMessage.
				
				SendMessage response = new SendMessage();
				response.setChatId(chatId);
				
				function(str, response, update);
			} 
			else if(update.hasCallbackQuery()) {	
				String callData = update.getCallbackQuery().getData(); // Contiene il riferimento a quale CallbackData sia stato cliccato.
				long chatId = update.getCallbackQuery().getMessage().getChatId();
				
				SendMessage response = new SendMessage();
				response.setChatId(chatId);
				
				if(callData.contains("FAV")) { // Passaggio implementato affinche' si possa distinguere le notizie inerenti alla sezione "preferenze", piuttosto che da quelle "generali".
					callData = "FAV"; // Passaggio reso possibile dall'utilizzo dei CallbackData.
				}
	
				switch (callData) { // In base a quale tipoligia di CallbackData sia stato richiesto vengono richiamate funzionalia' differenti.
					case "BASE":
						registration(response, update);
						break;
					case "PREMIUM":
						registration(response, update);
						break;
					case "MODIFYBASE":
						modifyRow(response, "false");
						break;
					case "MODIFYPREMIUM":
						modifyRow(response, "true");
						break;
					case "ADD":
						changeFeed(response, update);
						break;
					case "ELIMINATE":
						changeFeed(response, update);
						break;
					case "FAV":
						viewFavorite(response, update);
						break;
					default:
						readSearch(response, update);
						break;
				}
			}
		} catch (HeadlessException | IllegalArgumentException | SQLException | TelegramApiException | IOException | FeedException | FetcherException e ) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo function contenente le funzioni principali del bot Telegram, permettendone la 
	 * la navigazione e individuandone la correlazione con onUpdateReceived.
	 */
	public void function(String str, SendMessage response, Update update) throws HeadlessException, IllegalArgumentException, SQLException, TelegramApiException, IOException, FeedException, FetcherException {
		char character = str.charAt(0);

		if(character == '/') { // Controllo che l'update ricevuto si tratti di una funzione.
			function = str; // Salvo temporaneamente quale sia la funzione scelta precedentemente, in maniera tale che possa successivamente utilizzare il corretto update.
			try {
				switch (function) {
				case "/start":
					response.setText("Benvenuto nel bot NABOO!");
					execute(response);
					break;

				case "/registrazione":
					if(access == true) {
						response.setText(emojiiWellDone + " Attenzione hai gia' effettuato il login! " + emojiiWellDone);
					}
					else {
						response.setText("Inserisci il tuo nickname e la password, separati da uno spazio.\n" + "Modalita' di inserimento ('chaz27' 'rossetto12')");
					}
					execute(response);
					break;

				case "/accedi":
					if(access == true) {
						response.setText(emojiiWellDone + " Attenzione hai gia' effettuato il login! " + emojiiWellDone);
					}
					else {
						response.setText("Inserisci il tuo nickname e la password, separati da uno spazio.\n" + "Modalita' di inserimento ('chaz27' 'rossetto12')");
					}
					execute(response);
					break;

				case "/modifica":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
					} 
					else {
						response.setText("Inserisci il tuo nickname e la password, separati da uno spazio.\n" + "Modalita' di inserimento ('chaz27' 'rossetto12')");
					}
					execute(response);
					break;

				case "/legginotizie":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
						execute(response);
					} 
					else {						
						if(subscription == true) {
							response = resOperate.setFeedCategoryPremium(update);
							execute(response);
						}
						else {
							response = resOperate.setFeedCategoryBase(update);
							execute(response);
						}
						
						for(int i = 0; i < arrayNews.length; i++) { // Inizializzo l'arrayNews che conterra' le different notizie che verranno poi visualizzate.
							arrayNews[i] = null;
						}
					}
					break;

				case "/commento":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
						execute(response);
					} 
					else {
						subscription = dataBase.getSubscription(tabUser, nickname, password);
						if (subscription != true) { // In base a quale sia la tipologia di abbonamento, e' possibile compiere tutto l'elenco delle funzioni, oppure un numero limitato.
							response.setText(emojiiNoEntry + " Attenzione la tua tipologia di abbonamento non permette questa funzione! " + emojiiNoEntry);
							execute(response);
						} 
						else {
							response.setText("Inserisci una recensione");
							execute(response);
						}
					}
					break;

				case "/visualizzacommenti":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
						execute(response);
					} 
					else {
						subscription = dataBase.getSubscription(tabUser, nickname, password);
						if (subscription != true) {
							response.setText(emojiiNoEntry + " Attenzione la tua tipologia di abbonamento non permette questa funzione! " + emojiiNoEntry);
							execute(response);
						} 
						else {
							viewComment(response, update);
						}
					}
					break;
					
				case "/visualizzapreferiti":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
						execute(response);
					} 
					else {
						viewFavorite(response, update);
					}
					break;
					
				case "/modificafeed":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
						execute(response);
					} 
					else {
						subscription = dataBase.getSubscription(tabUser, nickname, password);
						if(subscription != true) {
							response.setText(emojiiNoEntry + " Attenzione la tua tipologia di abbonamento non permette questa funzione! " + emojiiNoEntry);
							execute(response);
						} 
						else {
							changeFeed(response, update);
						}
					}
					break;
					
				case "/aggiungifeed": 
					response.setText("Inserisci i feed che vuoi aggiungere");
					execute(response);
					break;
					
				case "/eliminafeed": 
					response.setText("Inserisci i feed che vuoi eliminare");
					execute(response);
					break;
					
				default:
					response.setText("Comando non riconosciuto");
					execute(response);
					break;
				}
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		} 
		else {
			switch (function) {
				case "/registrazione":
					registration(response, update);
					break;
	
				case "/accedi":
					access(response, update);
					break;
	
				case "/modifica":
					modify(response, update);
					break;
					
				case "/legginotizie":
					readSearch(response, update);
					break;
	
				case "/commento":
					comment(response, update);
					break;
					
				case "/aggiungifeed":
					addFeed(response, update);
					break;
					
				case "/eliminafeed":
					eliminateFeed(response, update);
					break;
			}
		}
	}
	
	/*
	 * Metodo registration per permettere la registrazione del proprio
	 * profilo attarverso l'utilizzo del database.
	 */
	public void registration(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException{		
		if(update.hasCallbackQuery()) {				
			String callData = update.getCallbackQuery().getData();
			switch (callData) {
				case "BASE":
					addClient(nickname, password, "false");
					break;
				case "PREMIUM":
					addClient(nickname, password, "true");
					break;
			}
			
			function = " "; // Stringa utilizzata per navigare tra le diverse funzionalita' dello switch.
			subscription = dataBase.getSubscription(tabUser, nickname, password);
			userId = dataBase.getId(tabUser, IdUser, nickname, password);
			response.setText(emojiiWellDone + " Registrazione eseguita! " + emojiiWellDone);
		}
		else {
			String str = update.getMessage().getText();
			String[] tokens = str.split(" ");
				
			if (tokens.length != 2) { // Controllo che siano stati inseriti il numero di parametri corretti.
				response.setText(emojiiNoEntry + " Attenzione scrittura delle credenziali non corretta riprova! " + emojiiNoEntry);
			} 
			else {
				nickname = tokens[0];
				password = tokens[1];
				if(dataBase.contains(tabUser, nickname, password)) {
					response.setText(emojiiNoEntry + " Prova altre credenziali! " + emojiiNoEntry);
				}
				else {
					access = true;	
					response = resOperate.setRegistrationResponse(update);
				}
			}
		}		
		execute(response);
	}
	
	/*
	 * Metodo addClient che permette l'inserimento del nuovo utente all'interno
	 * del database, evidenziandone un profilo base rispetto ad uno premium.
	 */
	public void addClient(String nickname, String password, String subscription) throws HeadlessException, SQLException {
		MyDataBase dataBase = new MyDataBase();
		dataBase.insertTable(tabUser, nickname, password, subscription);
	}
	
	/*
	 * Metodo access che permette l'accesso al bot Telegram, per 
	 * poi avanzare richieste di funzionalita' successive.
	 */
	public void access(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		String str = update.getMessage().getText();
		String[] tokens = str.split(" ");

		if (tokens.length != 2) {
			response.setText(emojiiNoEntry + " Attenzione scrittura delle credenziali non corretta riprova! " + emojiiNoEntry);
		} 
		else {
			nickname = tokens[0];
			password = tokens[1];
			subscription = dataBase.getSubscription(tabUser, nickname, password);
			if (dataBase.contains(tabUser, nickname, password)) {
				if (access == true) {
					response.setText(emojiiWellDone + " Accesso gia' eseguito precedentemente " + emojiiWellDone);
				} 
				else {
					access = true; // Setto la variabile "access" in maniera tale da consentire un unico accesso.
					function = " ";
					userId = dataBase.getId(tabUser, IdUser, nickname, password);
					response.setText(emojiiWellDone + " Accesso eseguito! " + emojiiWellDone);
				}
			} 
			else {
				response.setText(emojiiNoEntry + " Attenzione credenziali errate! " + emojiiNoEntry);
			}
		}
		execute(response);
	}

	/*
	 * Metodo modify specificato principalmente per permettere la modifica 
	 * delle proprie credenziali, con lo scopo di cambiare tipologia di profilo.
	 */
	public void modify(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		String lineModify = update.getMessage().getText();
		String[] tokens = lineModify.split(" ");
				
		if(tokens.length != 2) {
			response.setText(emojiiNoEntry + "Attenzione scrittura non corretta delle credeniziali! " + emojiiNoEntry);
		}
		else if(modify != true)
		{
			nickname = tokens[0];
			password = tokens[1];
			if(dataBase.contains(tabUser, nickname, password)) { // Controllo se le credenziali siano gia' presenti.
				userId = dataBase.getId(tabUser, IdUser, nickname, password); // Ulteriore controllo maggioratvio, se all'interno del database sia contenuto.
				if(userId != 0) {
					response.setText("Inserisci le nuove credenziali");
					modify = true;
				}
			}
			else {
				response.setText(emojiiNoEntry + " Attenzione credenziali non corrette! " + emojiiNoEntry);
			}
		}
		else {
			response = resOperate.setModifyResponse(update);
		}	
		execute(response);	
	}

	/*
	 * Metodo modifyRow utilizzato per cambiare tipologia di profilo utente.
	 */
	public void modifyRow(SendMessage response, String sub) throws HeadlessException, SQLException, TelegramApiException {
		dataBase.alterRow(tabUser, userId, nickname, password, sub);
		subscription = dataBase.getSubscription(tabUser, nickname, password);
		modify = false;
		response.setText(emojiiWellDone + " Modifica eseguita! " + emojiiWellDone);
		execute(response);
	}
	
	/*
	 * Metodo readSearch che permette di visualizzare le differenti notizie
	 * a seconda di quale preferenza sia stata espressa.
	 */
	public void readSearch(SendMessage response, Update update) throws HeadlessException, IllegalArgumentException, SQLException, TelegramApiException, IOException, FeedException, FetcherException {	
		if (update.hasCallbackQuery()) { // Primo controllo relativo al fatto se l'update contenga un CallbackData.
			String callData = update.getCallbackQuery().getData();
			switch (callData) {
				case "NEXT":
					j++; // Utilizzo del contatore sia in ottica di salvataggio momentaneo dei dati, oltre a variare la tipologia di EditMessageText visualizzabile.
					title = arrayNews[j].getTitolo(); // Assegno momentaneamente entrambe le variabili affinche' possano essere riprodotte correttamente tramite l'EditMessageText.
					link = arrayNews[j].getLink();
					EditMessageText newResponseNext = new EditMessageText();
					if (j >= (arrayNews.length - 1)) { // Verifico se si tratti della prima notizia della lista, in maniera tale da non permettere la navigazione di notizie precedenti, data la loro mancanza.
						newResponseNext = resNews.setNewResponseNext(update, title, link);
					} 
					else {
						newResponseNext = resNews.setNewResponse(update, title, link);
					}
					execute(newResponseNext);
					break;
	
				case "PREVIOUS":
					j--;
					title = arrayNews[j].getTitolo();
					link = arrayNews[j].getLink();
					EditMessageText newResponsePrevious = new EditMessageText();
					if (j <= 0) {  // Verifico se si tratti dell'ultima notizia della lista, in maniera tale da non permettere la navigazione di notizie successive, data la loro mancanza.
						newResponsePrevious = resNews.setNewResponsePrevious(update, title, link);
					} 
					else {
						newResponsePrevious = resNews.setNewResponse(update, title, link);
					}
					execute(newResponsePrevious);
					break;
	
				case "COMMENT":
					long chatId = update.getCallbackQuery().getMessage().getChatId();
					SendMessage newResponseComment = new SendMessage();
					newResponseComment.setChatId(chatId);
					function("/commento", newResponseComment, update); // Passaggio obbligatorio data la necessita' di acquisire un ulteriore update.
					break;
						
				case "VIEW":
					long chatViewId = update.getCallbackQuery().getMessage().getChatId();
					SendMessage newResponseViewComment = new SendMessage();
					newResponseViewComment.setChatId(chatViewId);
					function("/visualizzacommenti", newResponseViewComment, update);
					break;
					
				case "ADDNEW":
					long chatFavId = update.getCallbackQuery().getMessage().getChatId();
					SendMessage newResponseFav = new SendMessage();
					newResponseFav.setChatId(chatFavId);
					addFavoriteNews(newResponseFav, title, link); // Aggiunta della notizia nella sezione preferenza di lettura.
					break;
					
				default: // Default case che sorge qualora la richiesta di lettura sia relativa ad un preciso argomento, presente tra i feed RSS.
					long chatFeedId = update.getCallbackQuery().getMessage().getChatId();
					SendMessage responseFeed = new SendMessage();
					responseFeed.setChatId(chatFeedId);
					
					String search = update.getCallbackQuery().getData();
					search.toLowerCase();
					FeedReader reader = new FeedReader();
					reader.run(search, userId);
					Gson g = new GsonBuilder().setPrettyPrinting().create();
					arrayNews = g.fromJson(new FileReader("GsonImport.json"), Notizia[].class);
					
					if(arrayNews.length == 0) {
						responseFeed.setText("Non hai alcuna preferenza tra le tue feed, utilizza /modificafeed, per una lettura piu' piacevole");
					}
					else {
						j = 0;
						title = arrayNews[j].getTitolo();
						link = arrayNews[j].getLink();
						responseFeed = resNews.setResponseCallBack(update, title, link);
					}
					execute(responseFeed);
					break;
			}
		} 
		else { // Ramo "else", richiamato qualora sia espressa una richiesta ben determinata e precisa.
			String search = update.getMessage().getText(); // Stringa che contiene la preferenza espressa dall'utente.
			search.toLowerCase();
			FeedReader reader = new FeedReader();
			reader.run(search, userId); // Metodo run di FeedReader, che ha il compito di rispondere alla richiesta avanzata dall'utente.
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			arrayNews = g.fromJson(new FileReader("GsonImport.json"), Notizia[].class); // Dopo aver riempito il file .json tramite il metodo run di FeedReader, tramite la funzione ".fromJson", vengono inseriti nell'arrayNews tutti gli oggetti di tipo Notizia.
					
			int length = arrayNews.length;
			j = 0;
			
			if(length > 0) { // Controllo che la struttura dati non sia vuota, in caso verra' visualizzato il messaggio di errore.
				title = arrayNews[j].getTitolo();
				link = arrayNews[j].getLink();
						
				if(length == 1) {
					response = resNews.setResponseAlone(update, title, link); // Stampa della notizia, che non permette la navigazione poiche' unica.
				}
				else {
					response = resNews.setResponse(update, title, link); // Stampa di multiple notizie, gestite con i Callback.
				}
			}
			else {
				response.setText(emojiiNoEntry + " Spiacenti non abbiamo nessuna risorsa in grado di soddisfare la richiesta " + emojiiNoEntry);
			}
		}		
		execute(response); 
	}

	/*
	 * Metodo comment che permette di aggiungere commenti nei confronti delle 
	 * differenti notizie caricate precedentemente, attraverso l'utilizzo 
	 * principale dei CallbackData.
	 */
	public void comment(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		if (!dataBase.contains(tabNews, title, link)) {
			dataBase.insertTable(tabNews, title, link, null); // Inserimento dell'oggetto Notizia all'interno della tabella, qualora non sia gia' presente.
		}
	
		int newsId = dataBase.getId(tabNews, IdNews, title, link);
		String strUtente = Integer.toString(userId);
		String strNotizia = Integer.toString(newsId);
		String comment = update.getMessage().getText();
		
		if (dataBase.contains(tabComment, strUtente, strNotizia)) { // Controllo che permette l'inserimento della propria recensione o la modifica della stessa, sempre relativa ad una specifica notizia.
			dataBase.setComment(tabComment, comment, strUtente, strNotizia);
			response.setText("Recensione aggiornata: " + comment);
		} 
		else {
			dataBase.insertTable(tabComment, comment, strUtente, strNotizia);
			response.setText("Recensione inserita: " + comment);
		}
		execute(response);
	}

	/*
	 * Metodo viewComment che permette la visualizzazione sia dei propri commenti
	 * inseriti in uno storico di notizie, ma anche relativi alle recensioni inserite 
	 * esclusivamente a quella determinata notizia, da parte di altri profili.
	 */
	public void viewComment(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException{
		ArrayList<String> arrayComment = new ArrayList<>();
		ArrayList<String> arrayTitle = new ArrayList<>();

		if (update.hasCallbackQuery()) { // Callback utilizzato principalmente, qualora sia stata avanzata la richiesta per visualizzare tutti i commenti relativi ad una specifica notizia.
			response.setText("I commenti di questa notizia risultano: \n");
			
			int id = dataBase.getId(tabNews, IdNews, title, link);
			String newsId = Integer.toString(id);
			arrayComment = dataBase.getComments(tabComment, newsId, "comment");
			if (arrayComment.size() > 0) { // Controllo che siano presenti commenti rilasciati della specifica notizia. 
				for (String str : arrayComment) { // Riempimento di tutti i commenti rilasciati di quella specifica notizia.
					response.setText(response.getText() + str + "\n");
				}
			} 
			else {
				response.setText("Non e' presente alcun commento di questa notizia!");
			}
		} 
		else { // Stampa delle recensioni relative all'utente che abbia effettuato l'accesso o la registrazione.
			response.setText("I tuoi commenti risultano \n\n");
			
			arrayTitle = dataBase.getTitleFromComment(userId);
			arrayComment = dataBase.getComments(tabComment, String.valueOf(userId), "mineComment");
			if (arrayComment.size() > 0) { // Controllo che siano presenti commenti rilasciati delllo specifico utente. 
				for (int i = 0; i < arrayComment.size(); i++) { // Riempimento di tutti i commenti rilasciati dallo specifico utente.
					response.setText(response.getText() + " " + i + ") Titolo: " + arrayTitle.get(i) + " \nRecensione: '" + arrayComment.get(i) + "'\n\n");
				}
			} 
			else {
				response.setText("Non e' presente alcun commento di questa notizia!");
			}
		}
		execute(response);
	}
	
	/*
	 * Metodo viewFavorite che permette la visualizzazione delle notizie che possiedono una preferenza,
	 * espressa dall'utente che abbia effettuato l'accesso.
	 */
     public void viewFavorite(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException, IllegalArgumentException, IOException, FeedException, FetcherException{
		if (update.hasCallbackQuery()) { // Primo controllo relativo al fatto se l'update contenga un CallbackData.
			String callData = update.getCallbackQuery().getData();
			switch (callData) { // Medesimo processo che avviene nel metodo readSearch, ma in questo caso impostato solo nelle preferenze di lettura.
				case "NEXTFAV":
					j++;
					title = arrayNews[j].getTitolo();
					link = arrayNews[j].getLink();
					EditMessageText newResponseNext = new EditMessageText();
					if (j >= (arrayNews.length - 1)) { // Verifico se si tratti della prima notizia della lista, in maniera tale da non permettere la navigazione di notizie precedenti, data la loro mancanza.
						newResponseNext = resFavorite.setNewResponseNextFav(update, title, link);
					} 
					else {
						newResponseNext = resFavorite.setNewResponseFav(update, title, link);
					}
					execute(newResponseNext);
					break;
	
				case "PREVIOUSFAV":
					j--;
					title = arrayNews[j].getTitolo();
					link = arrayNews[j].getLink();	
					EditMessageText newResponsePrevious = new EditMessageText();
					if (j <= 0) {  // Verifico se si tratti dell'ultima notizia della lista, in maniera tale da non permettere la navigazione di notizie successive, data la loro mancanza.
						newResponsePrevious = resFavorite.setNewResponsePreviousFav(update, title, link);
					} 
					else {
						newResponsePrevious = resFavorite.setNewResponseFav(update, title, link);
					}
					execute(newResponsePrevious);
					break;
	
				case "COMMENTFAV":
					long chatId = update.getCallbackQuery().getMessage().getChatId();
					SendMessage newResponseComment = new SendMessage();
					newResponseComment.setChatId(chatId);
					function("/commento", newResponseComment, update);
					break;
						
				case "VIEWFAV":
					long chatViewId = update.getCallbackQuery().getMessage().getChatId();
					SendMessage newResponseViewComment = new SendMessage();
					newResponseViewComment.setChatId(chatViewId);
					function("/visualizzacommenti", newResponseViewComment, update);
					break;
					
				case "ELIMINATEFAV":
					dataBase.deleteTable(tabNewsFavorite, dataBase.getId(tabNews, IdNews, title, link));
					
					arrayNews = dataBase.getNewsFav(userId);	
					int length = arrayNews.length;

					EditMessageText  newResponseEliminate = new EditMessageText(); // Utilizzo dell'EditMessageText per rendere la lettura delle notizie fluida, senza interromperla.
					if(length > 0) { // Controllo che la struttura dati non sia vuota, in caso verra' visualizzato il messaggio di errore.
						title = arrayNews[j].getTitolo();
						link = arrayNews[j].getLink();
								
						if(length == 1) {
							newResponseEliminate = resFavorite.setResponseAloneFavEdit(update, title, link); // Stampa della notizia, che non permette la navigazione poiche' unica.
						}
						else {
							newResponseEliminate = resFavorite.setResponseFavEdit(update, title, link); // Stampa di multiple notizie, gestite con i CallbackData.
						}
					}
					else {
						newResponseEliminate = resFavorite.setBlockResponseFav(update);
					}
					execute(newResponseEliminate);
					break;
			}
		} 
		else {
			arrayNews = dataBase.getNewsFav(userId);					
			int length = arrayNews.length;
			j = 0;
				
			if(length > 0) { // Controllo che la struttura dati non sia vuota, in caso verra' visualizzato il messaggio di errore.
				title = arrayNews[j].getTitolo();
				link = arrayNews[j].getLink();
						
				if(length == 1) {
					response = resFavorite.setResponseAloneFav(update, title, link); // Stampa della notizia, che non permette la navigazione poiche' unica.
				}
				else {
					response = resFavorite.setResponseFav(update, title, link); // Stampa di multiple notizie, gestite con i CallbackData.
				}
			}
			else {
				response.setText(emojiiNoEntry + " Non e' presente alcuna notizia tra i tuoi preferiti " + emojiiNoEntry);
			}
		}		
		execute(response); 
	}
     
     /*
 	 * Metodo addFavoriteNews che permette l'aggiunta di una preferenza nei
 	 * confronti di una notizia letta.
 	 */
 	public void addFavoriteNews(SendMessage response, String title, String link) throws HeadlessException, SQLException, TelegramApiException {
 		if (!dataBase.contains(tabNews, title, link)) {
 			dataBase.insertTable(tabNews, title, link, null); // Inserimento dell'oggetto Notizia all'interno della tabella, qualora non sia gia' presente.
 		}
 	
 		int newsId = dataBase.getId(tabNews, IdNews, title, link);
 		String strUtente = Integer.toString(userId);
 		String strNotizia = Integer.toString(newsId);
 		if (!dataBase.contains(tabNewsFavorite, strUtente, strNotizia)) { // Controllo che permette l'inserimento della propria recensione o la modifica della stessa, sempre relativa ad una specifica notizia.
 			dataBase.insertTable(tabNewsFavorite, strUtente, strNotizia, null);
 			response.setText("Notizia aggiunta alle preferenze!");
 		}
 		else {
 			response.setText("Notizia gia' tra i preferiti");
 		}
 		execute(response);
 	}

	/*
	 * Metodo changeFeed che permette di modificare la propria feed personalizzata,
	 * ossia di leggere notizie che riguardano solo argomenti che abbiano una preferenza.
	 */
	public void changeFeed(SendMessage response, Update update) throws HeadlessException, IllegalArgumentException, SQLException, TelegramApiException, IOException, FeedException, FetcherException {		 		
		if(update.hasCallbackQuery()) {
			String callData = update.getCallbackQuery().getData();
			long chatId = update.getCallbackQuery().getMessage().getChatId();
			switch (callData) {
				case "ADD":
					SendMessage newResponseAdd = new SendMessage();
					newResponseAdd.setChatId(chatId);
					function("/aggiungifeed", newResponseAdd, update); //Chiamata del metodo function obbligatoria, dettata dalla necessita' di ottenere un ulteriore update.
					break;
				case "ELIMINATE":
					SendMessage newResponseEliminate = new SendMessage();
					newResponseEliminate.setChatId(chatId);
					function("/eliminafeed", newResponseEliminate, update);
					break;
			}
		}
		else {
			response = resOperate.setFeedDataResponse(update, response, tabUser, IdUser, nickname, password); // Stampa del messaggio, contenente tutti i feed presenti nella tabella, suddividendo quelli che abbiano una preferenza dai feed totali.
		}
		execute(response);
	}
	
	/*
	 * Metodo addFeed che permette l'inserimento di feed all'interno delle proprie preferenze.
	 */
	public void addFeed(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		String str = update.getMessage().getText();
		String[] tokens = str.split(" ");
		
		dataBase.setFeed(userId, true, tokens); // Metodo setFeed che permette la corretta visualizzazione delle feed RSS associate all'utente specifico, tramite l'utilizzo di SendMessage.
		
		response = resOperate.setFeedDataResponse(update, response, tabUser, IdUser, nickname, password);
		execute(response);
	}
	
	/*
	 * Metodo eliminateFeed che permette l'eliminazione di feed all'interno delle proprie preferenze.
	 */
	public void eliminateFeed(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		String str = update.getMessage().getText();
		String[] tokens = str.split(" ");
		
		dataBase.setFeed(userId, false, tokens);
		
		response = resOperate.setFeedDataResponse(update, response, tabUser, IdUser, nickname, password);
		execute(response);
	}
}