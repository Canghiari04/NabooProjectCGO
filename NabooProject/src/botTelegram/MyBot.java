package botTelegram;

import java.awt.HeadlessException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dataBase.MyDataBase;
import feedRSS.FeedReader;
import feedRSS.Notizia;

public class MyBot extends TelegramLongPollingBot // Classe che si focalizza sull'update ricevuto dall'utente
{
	private boolean access = false, modify = false, subscription = false;
	
	private int j = 0, userId = 0;
	
	private String emojiiNoEntry = "⛔️", emojiiWellDone = "✅";
	private String nickname = " ", password = " ", function = " ", title = " ", link = " ";
	private String tabUser = "Utente", tabNews = "Notizia", tabComment = "Commento", IdUser = "UtenteId", IdNews = "NotiziaId";
	
	private static ArrayList<String> feeds = new ArrayList<String>();
	private static ArrayList<Utente> arrayUtente = new ArrayList<Utente>();
	
	private static Notizia[] arrayNews;
	private static Response res = new Response();
	private static MyDataBase dataBase = new MyDataBase();

	public String getBotUsername() {
		return "NabooCGNbot";
	}

	public String getBotToken() {
		return "5480067721:AAGGX2yduLoYjRtek0G0lqppg5H6bu10hlE";
	}
	
	public void onUpdateReceived(Update update) {
		try {
			if(update.hasMessage() && update.getMessage().hasText()) {
				String str = update.getMessage().getText();
				long chatId = update.getMessage().getChatId();
				
				SendVideo vIdeo = new SendVideo();
				vIdeo.setChatId(chatId);
				SendMessage response = new SendMessage();
				response.setChatId(chatId);
				
				function(str, response, vIdeo, update);
			} 
			else if(update.hasCallbackQuery()) {	
				String callData = update.getCallbackQuery().getData(); // Contiene il riferimento a quale CallbackData sia stato cliccato
				long chatId = update.getCallbackQuery().getMessage().getChatId();
				
				SendMessage response = new SendMessage();
				response.setChatId(chatId);
	
				switch (callData) {
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
					default:
						readSearch(response, update);
						break;
				}
			}
		} catch (HeadlessException | IllegalArgumentException | SQLException | TelegramApiException | IOException e ) {
			e.printStackTrace();
		}
	}

	/*
	 * Switch contenente le funzioni principali del bot telegram, indivIduandone la
	 * correlazione con onUpdateReceived
	 */
	public void function(String str, SendMessage response, SendVideo SendVideo, Update update) throws HeadlessException, IllegalArgumentException, SQLException, TelegramApiException, IOException {
		char character = str.charAt(0);

		if(character == '/') {
			function = str; // Salvo temporaneamente quale sia la funzione scelta precedentemente, in maniera tale che possa successivamente utilizzare il corretto update
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
						execute(response);
					} 
					else {
						response.setText("Inserisci il tuo nickname e la password, separati da uno spazio.\n" + "Modalita' di inserimento ('chaz27' 'rossetto12')");
						execute(response);
					}
					break;

				case "/leggiNotizie":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
						execute(response);
					} 
					else {
						response.setText("Inserisci una parola chiave\n\nQui sotto sono riportate le disponibili\n");
						feeds = dataBase.getFeedsTot();
						
						for(String f : feeds) {
							response.setText(response.getText() + f + "\n");
						}
						execute(response);
						
						for(int i = 0; i < arrayNews.length; i++)
							arrayNews[i] = null;
					}
					break;

				case "/commento":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
					} 
					else {
						subscription = dataBase.getSubscription(tabUser, nickname, password);
						if (subscription != true) {
							response.setText(emojiiNoEntry + " Attenzione la tua tipologia di abbonamento non permette questa funzione! " + emojiiNoEntry);
						} 
						else {
							response.setText("Inserisci una recensione");
						}
					}
					execute(response);
					break;

				case "/visualizzaCommenti":
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
					
				case "/modificaFeed":
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
					
				case "/aggiungiFeed": 
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
					} 
					else {
						subscription = dataBase.getSubscription(tabUser, nickname, password);
						if(subscription != true) {
							response.setText(emojiiNoEntry + " Attenzione la tua tipologia di abbonamento non permette questa funzione! " + emojiiNoEntry);
						} 
						else {
							response.setText("Inserisci i feed che vuoi aggiungere");
						}
					}
					execute(response);
					break;
					
				case "/eliminaFeed": 
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
					} 
					else {
						subscription = dataBase.getSubscription(tabUser, nickname, password);
						if(subscription != true) {
							response.setText(emojiiNoEntry + " Attenzione la tua tipologia di abbonamento non permette questa funzione! " + emojiiNoEntry);
						} 
						else {
							response.setText("Inserisci i feed che vuoi eliminare");
						}
					}
					execute(response);
					break;
					
				default:
					response.setText("Attenzione il bot non possiede questa funzionalita'");
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
	
				case "/leggiNotizie":
					readSearch(response, update);
					break;
	
				case "/commento":
					comment(response, update);
					break;
					
				case "/aggiungiFeed":
					addFeed(response, update);
					break;
					
				case "/eliminaFeed":
					eliminateFeed(response, update);
					break;
			}
		}
	}
	
	/*
	 * Metodo getIn il quale prevede la funzionalita' di indivIduare
	 * la presenza di un utente all'interno del database.
	 */
	public boolean getIn(String nickname, String password) {
		for (Utente c : arrayUtente) {
			if (c.getNickname().equals(nickname) && c.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Metodo Registration per permettere la registrazione del proprio
	 * profilo attarverso l'utilizzo del database
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
			
			function = " "; // Stringa utilizzata per navigare tra le diverse funzionalita' dello switch
			subscription = dataBase.getSubscription(tabUser, nickname, password);
			userId = dataBase.getId(tabUser, IdUser, nickname, password);
			response.setText(emojiiWellDone + " Registrazione eseguita! " + emojiiWellDone);
		}
		else {
			String str = update.getMessage().getText();
			String[] tokens = str.split(" ");
				
			if (tokens.length != 2) {
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
					response = res.setRegistrationResponse(update, response);
				}
			}
		}		
		execute(response);
	}
	
	/*
	 * Metodo addClient che permette l'inserimento del nuovo utente all'interno
	 * del database, evIdenziandone un profilo base rispetto ad uno premium
	 */
	public void addClient(String nickname, String password, String subscription) throws HeadlessException, SQLException {
		MyDataBase dataBase = new MyDataBase();
		dataBase.insertTable(tabUser, nickname, password, subscription);
	}
	
	/*
	 * Metodo Access che permette l'accesso al bot Telegram, per 
	 * poi avanzare richieste successive
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
					access = true;
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
	 * Metodo Modify specificato principalmente per permettere la modifica 
	 * delle proprie credenziali, con lo scopo di cambiare tipologia di profilo
	 */
	public void modify(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		String lineModify = update.getMessage().getText();
		String[] tokens = lineModify.split(" ");
		
		nickname = tokens[0];
		password = tokens[1];
				
		if(tokens.length != 2) {
			response.setText(emojiiNoEntry + "Attenzione scrittura non corretta delle credeniziali! " + emojiiNoEntry);
		}
		else if(modify != true)
		{
			if(dataBase.contains(tabUser, nickname, password)) {
				userId = dataBase.getId(tabUser, IdUser, nickname, password);
				if(userId != 0) {
					response.setText("Inserisci le nuove credenziali");
					modify = true;
				}
				else {
					response.setText(emojiiNoEntry + " Attenzione credenziali non corrette! " + emojiiNoEntry);
				}
			}
		}
		else {
			response = res.setModifyResponse(update, response);
		}	
		execute(response);	
	}

	/*
	 * Metodo ModifyRow utilizzato per cambiare tipologia di profilo utente
	 */
	public void modifyRow(SendMessage response, String sub) throws HeadlessException, SQLException, TelegramApiException {
		dataBase.alterRow(tabUser, userId, nickname, password, sub);
		response.setText(emojiiWellDone + " Modifica eseguita! " + emojiiWellDone);
		execute(response);
	}
	
	/*
	 * Metodo ReadSearch che permette di visualizzare le differenti notizie
	 * a seconda di quale preferenza sia stata espressa
	 */
	public void readSearch(SendMessage response, Update update) throws HeadlessException, IllegalArgumentException, SQLException, TelegramApiException, IOException {	
		if (update.hasCallbackQuery()) {
			String callData = update.getCallbackQuery().getData();
			switch (callData) {
				case "NEXT":
				j++;
				title = arrayNews[j].getTitolo();
				link = arrayNews[j].getLink();
	
				EditMessageText newResponseNext = new EditMessageText();
				if (j >= (arrayNews.length - 1)) {
					newResponseNext = res.setNewResponseNext(update, title, link);
				} 
				else {
					newResponseNext = res.setNewResponse(update, title, link);
				}
				execute(newResponseNext);
				break;
	
				case "PREVIOUS":
				j--;
				title = arrayNews[j].getTitolo();
				link = arrayNews[j].getLink();
						
				EditMessageText newResponsePrevious = new EditMessageText();
				if (j <= 0) {
					newResponsePrevious = res.setNewResponsePrevious(update, title, link);
				} 
				else {
					newResponsePrevious = res.setNewResponse(update, title, link);
				}
				execute(newResponsePrevious);
				break;
	
				case "COMMENT":
					long chatId = update.getCallbackQuery().getMessage().getChatId();
					SendMessage newResponseComment = new SendMessage();
					newResponseComment.setChatId(chatId);
					function("/commento", newResponseComment, null, update);
					break;
						
				case "VIEW":
					long chatViewId = update.getCallbackQuery().getMessage().getChatId();
					SendMessage newResponseViewComment = new SendMessage();
					newResponseViewComment.setChatId(chatViewId);
					function("/visualizzaCommenti", newResponseViewComment, null, update);
					break;
			}
		} 
		else {
			String search = update.getMessage().getText();
			search.toLowerCase();
			FeedReader reader = new FeedReader();
			reader.run(search, userId);
					
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			arrayNews = g.fromJson(new FileReader("GsonImport.json"), Notizia[].class);
					
			int length = arrayNews.length;
			j = 0;
				
			if(length > 0) {
				title = arrayNews[j].getTitolo();
				link = arrayNews[j].getLink();
						
				if(length == 1) {
					response = res.setResponseAlone(update, title, link);
				}
				else {
					response = res.setResponse(update, title, link);	
				}
			}
			else {
				response.setText(emojiiNoEntry + " Spiacenti non abbiamo nessuna risorsa in grado di soddisfare la richiesta " + emojiiNoEntry);
			}
		}		
		execute(response); 
	}

	/*
	 * Metodo Comment che permette di aggiungere commenti nei confronti delle 
	 * differenti notizie caricate precedentemente, attraverso l'utilizzo 
	 * principale dei Callback
	 */
	public void comment(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		if (!dataBase.contains(tabNews, title, link)) {
			dataBase.insertTable(tabNews, title, link, null);
		}
	
		int newsId = dataBase.getId(tabNews, IdNews, title, link);
		String strUtente = Integer.toString(userId);
		String strNotizia = Integer.toString(newsId);
		String comment = update.getMessage().getText();
		
		if (dataBase.contains(tabComment, strUtente, strNotizia)) {
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
	 * Metodo ViewComment che permette la visualizzazione sia dei propri commenti
	 * inseriti in uno storico di notizie, ma anche relativi alle recensioni inserite 
	 * esclusivamente a quella determinata notizia, da parte di altri profili
	 */
	public void viewComment(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException{
		ArrayList<String> arrayComment = new ArrayList<>();
		ArrayList<String> arrayTitle = new ArrayList<>();

		if (update.hasCallbackQuery()) {
			response.setText("I commenti di questa notizia risultano: \n");
			int newsId = dataBase.getId(tabNews, IdNews, title, link);
			String info = Integer.toString(newsId);
			arrayComment = dataBase.getComments(tabComment, info, "comment");

			if (arrayComment.size() > 0) {
				for (String str : arrayComment) {
					response.setText(response.getText() + str + "\n");
				}
			} 
			else {
				response.setText("Non e' presente alcun commento di questa notizia!");
			}
		} 
		else {
			response.setText("I tuoi commenti risultano: \n");
			
			arrayComment = dataBase.getComments(tabComment, String.valueOf(userId), "mineComment");
			arrayTitle = dataBase.getTitleFromComment(userId);

			if (arrayComment.size() > 0) {
				for (int i = 0; i < arrayComment.size(); i++) {
					response.setText(response.getText() + "Titolo: " + arrayTitle.get(i) + " Recensione: '" + arrayComment.get(i) + "'\n");
				}
			} 
			else {
				response.setText("Non e' presente alcun commento di questa notizia!");
			}
		}
		execute(response);
	}

	/*
	 * Metodo ChangeFeed che permette di modificare la propria feed personalizzata,
	 * ossia di leggere notizie che riguardano solo argomenti preferiti
	 */
	public void changeFeed(SendMessage response, Update update) throws HeadlessException, IllegalArgumentException, SQLException, TelegramApiException, IOException {		 		
		if(update.hasCallbackQuery()) {
			String callData = update.getCallbackQuery().getData();
			long chatId = update.getCallbackQuery().getMessage().getChatId();
				
			switch (callData) {
				case "ADD":
					SendMessage newResponseAdd = new SendMessage();
					newResponseAdd.setChatId(chatId);
					function("/aggiungiFeed", newResponseAdd, null, update);
					break;
				case "ELIMINATE":
					SendMessage newResponseEliminate = new SendMessage();
					newResponseEliminate.setChatId(chatId);
					function("/eliminaFeed", newResponseEliminate, null, update);
					break;
			}
		}
		else {
			response = res.setFeedDataResponse(update, response, tabUser, IdUser, nickname, password);
		}
		execute(response);
	}
	
	/*
	 * Metodo AddFeed che permette l'inserimento di feed all'interno delle proprie preferenze
	 */
	public void addFeed(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		String str = update.getMessage().getText();
		String[] tokens = str.split(" ");
		
		dataBase.setFeed(userId, true, tokens);
		
		response = res.setFeedDataResponse(update, response, tabUser, IdUser, nickname, password);
		execute(response);
	}
	
	/*
	 * Metodo EliminateFeed che permette l'eliminazione di feed all'interno delle proprie preferenze
	 */
	public void eliminateFeed(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		String str = update.getMessage().getText();
		String[] tokens = str.split(" ");
		
		dataBase.setFeed(userId, false, tokens);
		
		response = res.setFeedDataResponse(update, response, tabUser, IdUser, nickname, password);
		execute(response);
	}
}