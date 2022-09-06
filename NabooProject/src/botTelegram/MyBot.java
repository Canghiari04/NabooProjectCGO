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
	private boolean answer = false, access = false, modify = false, subscription = false;
	private int j = 0, utenteId = 0; // Contatore utilizzato nel metodo modify
	private String nickName = " ", password = " ", function = " ", titolo = " ", link = " ";
	private String emojiiNoEntry = "⛔️", emojiiWellDone = "✅";
	private String tabUtente = "Utente", tabNotizia = "Notizia", tabCommento = "Commento", idUtente = "UtenteID", idNotizia = "NotiziaID";
	private static Response res = new Response();
	private static MyDataBase dataBase = new MyDataBase();
	private static ArrayList<Utente> arrayUtente = new ArrayList<Utente>();
	private static ArrayList<String> feeds = new ArrayList<String>();
	private static Notizia[] arrayNotizia;

	public String getBotUsername() {
		return "NabooCGNbot";
	}

	public String getBotToken() {
		return "5480067721:AAGGX2yduLoYjRtek0G0lqppg5H6bu10hlE";
	}
	
	public void printPreviewMessage(SendMessage response) throws TelegramApiException {
		execute(response);
	}
	
	public void onUpdateReceived(Update update) {
		try {
			if(update.hasMessage() && update.getMessage().hasText()) {
				String str = update.getMessage().getText();
				long chatId = update.getMessage().getChatId();
				
				SendVideo video = new SendVideo();
				video.setChatId(chatId);
				SendMessage response = new SendMessage();
				response.setChatId(chatId);
				
				Function(str, response, video, update);
			} 
			else if(update.hasCallbackQuery()) {
				SendMessage response = new SendMessage();
	
				String callData = update.getCallbackQuery().getData();
				long chatId = update.getCallbackQuery().getMessage().getChatId();
				response.setChatId(chatId);
	
				switch (callData) {
					case "BASE":
						Registration(response, update);
						break;
					case "PREMIUM":
						Registration(response, update);
						break;
					case "MODIFYBASE":
						ModifyRow(response, "false");
						break;
					case "MODIFYPREMIUM":
						ModifyRow(response, "true");
						break;
					case "ADD":
						changeFeed(response, update);
						break;
					case "ELIMINATE":
						changeFeed(response, update);
						break;
					default:
						ReadSearch(response, update);
						break;
				}
			}
		} catch (HeadlessException | IllegalArgumentException | SQLException | TelegramApiException | IOException e ) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * Metodo getIn il quale prevede la medesima funzionalita' del metodo
	 * contains(), a discapito del fatto che quest ultimo non puo' essere usato per
	 * oggetti.
	 * 
	 */

	public boolean getIn(String nickName, String password) {
		for (Utente c : arrayUtente) {
			if (c.getNickName().equals(nickName) && c.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 
	 * Switch contenente le funzioni principali del bot telegram, individuandone la
	 * correlazione con onUpdateReceived
	 * 
	 */
	
	public void Function(String str, SendMessage response, SendVideo sendVideo, Update update) throws HeadlessException, IllegalArgumentException, SQLException, TelegramApiException, IOException {
		MyDataBase dataBase = new MyDataBase();
		char ch = str.charAt(0);

		if(ch == '/') {
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
						response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
					}
					execute(response);
					break;

				case "/modifica":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
						execute(response);
					} 
					else {
						response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
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
						
						for(int i = 0; i < arrayNotizia.length; i++)
							arrayNotizia[i] = null;
					}
					break;

				case "/commento":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
					} 
					else {
						subscription = dataBase.getSubscription(tabUtente, nickName, password);
						if (subscription != true) {
							response.setText(emojiiNoEntry + " Attenzione la tua tipologia di abbonamento non permette questa funzione! " + emojiiNoEntry);
						} 
						else {
							response.setText("Inserisci un commento");
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
						subscription = dataBase.getSubscription(tabUtente, nickName, password);
						if (subscription != true) {
							response.setText(emojiiNoEntry + " Attenzione la tua tipologia di abbonamento non permette questa funzione! " + emojiiNoEntry);
							execute(response);
						} 
						else {
							ViewComment(response, update);
						}
					}
					break;
					
				case "/modificaFeed":
					if(access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
						execute(response);
					} 
					else {
						subscription = dataBase.getSubscription(tabUtente, nickName, password);
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
						subscription = dataBase.getSubscription(tabUtente, nickName, password);
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
						subscription = dataBase.getSubscription(tabUtente, nickName, password);
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
					Registration(response, update);
					break;
	
				case "/accedi":
					Access(response, update);
					break;
	
				case "/modifica":
					Modify(response, update);
					break;
	
				case "/leggiNotizie":
					ReadSearch(response, update);
					break;
	
				case "/commento":
					Comment(response, update);
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

	public void Registration(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException{
		MyDataBase dataBase = new MyDataBase();
		
		if(update.hasCallbackQuery()) {				
			String callData = update.getCallbackQuery().getData();
				
			switch (callData) {
				case "BASE":
					addClient(nickName, password, "false");
					break;
				case "PREMIUM":
					addClient(nickName, password, "true");
					break;
			}
				
			subscription = dataBase.getSubscription(tabUtente, nickName, password);
			function = " ";
			utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);
			response.setText(emojiiWellDone + " Registrazione eseguita! " + emojiiWellDone);
		}
		else {
			String str = update.getMessage().getText();
			String[] tokens = str.split(" ");
				
			if (tokens.length != 2) { // TODO: controllo su sub
				response.setText(emojiiNoEntry + " Attenzione credenziali non corrette riprova! " + emojiiNoEntry);
			} 
			else {
				nickName = tokens[0];
				password = tokens[1];
					
				boolean answer = dataBase.contains(tabUtente, nickName, password);
				if(answer == true) {
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

	public void addClient(String nickName, String password, String sub) throws HeadlessException, SQLException {
		MyDataBase dataBase = new MyDataBase();
		dataBase.InsertTable(tabUtente, nickName, password, sub);
	}
	
	public void Access(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		// TODO: cercare in accedi le credenziale dell'utente, per stampa "piu' carina"
		MyDataBase dataBase = new MyDataBase();

		String str = update.getMessage().getText();
		String[] tokens = str.split(" ");

		if (tokens.length != 2) { // Condizione specificata per evitare scorretti inserimenti delle credenziale
			response.setText(emojiiNoEntry + " Attenzione credenziali non corrette riprova! " + emojiiNoEntry);
		} 
		else {
			nickName = tokens[0];
			password = tokens[1];

			answer = dataBase.contains(tabUtente, nickName, password);
			subscription = dataBase.getSubscription(tabUtente, nickName, password);

			if (answer) {  // Condizione per vericare se sia gia' avvenuta la registration dell'account
				if (access == true) {
					response.setText(emojiiWellDone + " Accesso gia' eseguito " + emojiiWellDone);
						// TODO: cosa puoi fare dopo questo messaggio? 
				} 
				else {
					access = true; // Evidenzia la possibilita' che la lettura delle notizie possa avvenire solamente con il proprio accesso
					function = " ";
					utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);
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
	 * Metodo Modify specificato principalmente per permettere la modifica delle proprie credenziali. 
	 */

	public void Modify(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		MyDataBase dataBase = new MyDataBase();
		String lineModify = update.getMessage().getText();
		String[] tokens = lineModify.split(" ");
		
		nickName = tokens[0];
		password = tokens[1];
				
		if(tokens.length != 2) {
			response.setText(emojiiNoEntry + "Attenzione inserisci correttamente le credeniziali! " + emojiiNoEntry);
		}
		else if(modify != true)
		{
			if(dataBase.contains(tabUtente, nickName, password)) {
				utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);
					
				if(utenteId != 0) {
					response.setText("Inserisci le nuove credenziali");
					modify = true;
				}
				else {
					response.setText(emojiiNoEntry + "Attenzione credenziali non corrette! " + emojiiNoEntry);
				}
			}
		}
		else {
			response = res.setModifyResponse(update, response);
		}	
		execute(response);	
	}

	public void ModifyRow(SendMessage response, String sub) throws HeadlessException, SQLException, TelegramApiException {
		dataBase.alterRow(tabUtente, nickName, password, sub, utenteId);
		response.setText(emojiiWellDone + " Modifica eseguita! " + emojiiWellDone);
		execute(response);
	}
	
	public void ReadSearch(SendMessage response, Update update) throws HeadlessException, IllegalArgumentException, SQLException, TelegramApiException, IOException {	
		if (update.hasCallbackQuery()) {
			String callData = update.getCallbackQuery().getData();
			switch (callData) {
				case "NEXT":
				j++;
				titolo = arrayNotizia[j].getTitolo();
				link = arrayNotizia[j].getLink();
	
				EditMessageText newResponseNext = new EditMessageText();
				if (j >= (arrayNotizia.length - 1)) {
					newResponseNext = res.setNewResponseNext(titolo, link, update);
				} 
				else {
					newResponseNext = res.setNewResponse(titolo, link, update);
				}
				execute(newResponseNext);
				break;
	
				case "PREVIOUS":
				j--;
				titolo = arrayNotizia[j].getTitolo();
				link = arrayNotizia[j].getLink();
						
				EditMessageText newResponsePrevious = new EditMessageText();
				if (j <= 0) {
					newResponsePrevious = res.setNewResponsePrevious(titolo, link, update);
				} 
				else {
					newResponsePrevious = res.setNewResponse(titolo, link, update);
				}
				execute(newResponsePrevious);
				break;
	
				case "COMMENT":
					SendMessage newResponseComm = new SendMessage();
					long chatId = update.getCallbackQuery().getMessage().getChatId();
					newResponseComm.setChatId(chatId);
					Function("/commento", newResponseComm, null, update);
					break;
						
				case "VIEW":
					SendMessage newResponseViewComm = new SendMessage();
					long chatViewId = update.getCallbackQuery().getMessage().getChatId();
					newResponseViewComm.setChatId(chatViewId);
					Function("/visualizzaCommenti", newResponseViewComm, null, update);
					break;
			}
		} 
		else {
			String search = update.getMessage().getText();
			search.toLowerCase();
			FeedReader reader = new FeedReader();
			reader.run(search, utenteId);
					
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			arrayNotizia = g.fromJson(new FileReader("GsonImport.json"), Notizia[].class);
					
			int length = arrayNotizia.length;
			j = 0;
				
			if(length > 0) {
				titolo = arrayNotizia[j].getTitolo();
				link = arrayNotizia[j].getLink();
						
				if(length == 1) {
					response = res.setResponseAlone(titolo, link, update);
				}
				else {
					response = res.setResponse(titolo, link, update);	
				}
			}
			else {
				response.setText("Spiacenti non abbiamo nessuna risorsa in grado di soddisfare la richiesta");
			}
		}		
		execute(response); 
	}

	public void Comment(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		MyDataBase dataBase = new MyDataBase();
		
		if (!dataBase.contains(tabNotizia, titolo, link))
			dataBase.InsertTable(tabNotizia, titolo, link, null);
		
		int notiziaId = dataBase.getID(tabNotizia, idNotizia, titolo, link);
		int utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);
		String strUtente = Integer.toString(utenteId);
		String strNotizia = Integer.toString(notiziaId);
		String comment = update.getMessage().getText();
		
		if (dataBase.contains(tabCommento, strUtente, strNotizia)) {
			dataBase.setRecensione(tabCommento, comment, strUtente, strNotizia);
			response.setText("Recensione aggiornata: " + comment);
		} 
		else {
			dataBase.InsertTable(tabCommento, comment, strUtente, strNotizia);
			response.setText("Recensione inserita: " + comment);
		}
		execute(response);
	}

	public void ViewComment(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException{
		MyDataBase dataBase = new MyDataBase();
		ArrayList<String> arrayRecensione = new ArrayList<>();
		ArrayList<String> arrayTitolo = new ArrayList<>();

		if (update.hasCallbackQuery()) {
			response.setText("I commenti di questa notizia risultano: \n");
			int notiziaId = dataBase.getID(tabNotizia, idNotizia, titolo, link);
			String notizia = Integer.toString(notiziaId);
			arrayRecensione = dataBase.getRecensioni(tabCommento, notizia, "comment");

			if (arrayRecensione.size() > 0) {
				for (String str : arrayRecensione) {
					response.setText(response.getText() + str + "\n");
				}
			} 
			else {
				response.setText("Non e' presente alcun commento di questa notizia!");
			}
		} 
		else {
			response.setText("I tuoi commenti risultano: \n");
			
			int utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);
			String utente = Integer.toString(utenteId);
			arrayRecensione = dataBase.getRecensioni(tabCommento, utente, "mineComment");
			arrayTitolo = dataBase.getTitleFromComment(utenteId);

			if (arrayRecensione.size() > 0) {
				for (int i = 0; i < arrayRecensione.size(); i++) {
					response.setText(response.getText() + "Titolo: " + arrayTitolo.get(i) + " Recensione: '" + arrayRecensione.get(i) + "'\n");
				}
			} 
			else {
				response.setText("Non e' presente alcun commento di questa notizia!");
			}
		}
		execute(response);
	}

	public void changeFeed(SendMessage response, Update update) throws HeadlessException, IllegalArgumentException, SQLException, TelegramApiException, IOException {		 		
		if(update.hasCallbackQuery()) {
			String callData = update.getCallbackQuery().getData();
			long chatId = update.getCallbackQuery().getMessage().getChatId();
				
			switch (callData) {
				case "ADD":
					SendMessage newResponseAdd = new SendMessage();
					newResponseAdd.setChatId(chatId);
					Function("/aggiungiFeed", newResponseAdd, null, update);
					break;
				case "ELIMINATE":
					SendMessage newResponseEliminate = new SendMessage();
					newResponseEliminate.setChatId(chatId);
					Function("/eliminaFeed", newResponseEliminate, null, update);
					break;
			}
		}
		else {
			response = res.setFeedDataResponse(update, response, tabUtente, idUtente, nickName, password);
		}
		execute(response);
	}
	
	public void addFeed(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		MyDataBase dataBase = new MyDataBase();
		
		String str = update.getMessage().getText();
		String[] tokens = str.split(" ");
		
		dataBase.setFeed(utenteId, true, tokens);
		
		response = res.setFeedDataResponse(update, response, tabUtente, idUtente, nickName, password);
		execute(response);
	}
	
	public void eliminateFeed(SendMessage response, Update update) throws HeadlessException, SQLException, TelegramApiException {
		MyDataBase dataBase = new MyDataBase();
		
		String str = update.getMessage().getText();
		String[] tokens = str.split(" ");
		
		dataBase.setFeed(utenteId, false, tokens);
		
		response = res.setFeedDataResponse(update, response, tabUtente, idUtente, nickName, password);
		execute(response);
	}
}