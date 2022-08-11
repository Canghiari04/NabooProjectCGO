package botTelegram;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
	private boolean answer = false, access = false;
	private String nickName = " ", password = " ", sub = " ", function = " ", titolo = " ", link = " ";
	private String emojiiNoEntry = "⛔️", emojiiWellDone = "✅";
	private String tabUtente = "Utente", tabNotizia = "Notizia", tabCommento = "Commento", idUtente = "UtenteID", idNotizia = "NotiziaID", idCommento = "CommentoID";
	private int c = 0, j = 0, utenteId = 0, notiziaId = 0; // Contatore utilizzato nel metodo modify
	private static ArrayList<Utente> arrayUtente = new ArrayList<Utente>();
	private static Notizia[] arrayNotizia;

	public String getBotUsername() {
		return "NabooCGNbot";
	}

	public String getBotToken() {
		return "5480067721:AAGGX2yduLoYjRtek0G0lqppg5H6bu10hlE";
	}

	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			String str = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();
			SendMessage response = new SendMessage();
			response.setChatId(chatId);

			Function(str, response, update);
		} else if (update.hasCallbackQuery()) {
			long chatId = update.getCallbackQuery().getMessage().getChatId();
			SendMessage response = new SendMessage();
			response.setChatId(chatId);

			ReadSearch(response, update);
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

	public void Function(String str, SendMessage response, Update update) {
		char ch = str.charAt(0);

		if (ch == '/') {
			function = str; // Salvo temporaneamente quale sia la funzione scelta precedentemente, in maniera tale che possa successivamente utilizzare il corretto update
			switch (function) {
			case "/start":
				try {
					response.setText("Benvenuto nel bot NABOO!");
					execute(response);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				break;

			case "/registrazione":
				try {
					response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
					execute(response);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				break;

			case "/accedi":
				try {

					response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
					execute(response);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				break;

			case "/modifica":
				try {
					if (access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! " + emojiiNoEntry);
						execute(response);
					} else {
						response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
						execute(response);
					}
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				break;

			case "/elimina":
				try {
					if (access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! "
								+ emojiiNoEntry);
						execute(response);
					} else {
						response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
						execute(response);
					}
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				break;

			case "/leggiNotizie":
				try {
					if (access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! "
								+ emojiiNoEntry);
						execute(response);
					} else {
						response.setText("Inserisci una parola chiave");
						execute(response);
					}
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				break;

			case "/commento":
				try {
					if (access != true) {
						response.setText(emojiiNoEntry + " Attenzione devi prima eseguire l'accesso al bot NABOO! "
								+ emojiiNoEntry);
						execute(response);
					} else {
						response.setText("Inserisci un commento alla notizia");
						execute(response);
					}
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				break;
			}
		} else {
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

			case "/elimina":
				Delete(response, update);
				break;

			case "/leggiNotizie":
				ReadSearch(response, update);
				break;

			case "/commento":
				Comment(response, update);
				break;
			}
		}
	}

	public void Registration(SendMessage response, Update update) {
		// TODO: richiedere differenti Update, attraverso condizioni
		try {
			MyDataBase dataBase = new MyDataBase();

			String str = update.getMessage().getText();
			String[] tokens = str.split(" ");

			if (tokens.length != 3) // TODO: controllo su sub
			{
				response.setText(emojiiNoEntry + " Attenzione credenziali non corrette riprova! " + emojiiNoEntry);
				execute(response);
			} else {
				nickName = tokens[0];
				password = tokens[1];
				sub = tokens[2];

				answer = dataBase.contains(tabUtente, nickName, password);

				if (answer) // In caso dovesse essere presente un account con le stesse credenziali verra' richiesto nuovamente l'inserimento
				{
					response.setText(emojiiNoEntry + " Attenzione credenziali gia' presenti! " + emojiiNoEntry);
					execute(response);
				} else {
					access = true; // Evidenzia la possibilita' che la lettura delle notizie possa avvenire solamente con la propria registration
					dataBase.InsertTable(tabUtente, nickName, password, sub);

					response.setText(emojiiWellDone + " Registrazione eseguita! " + emojiiWellDone);
					execute(response);
				}
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void Access(SendMessage response, Update update) {
		// TODO: cercare in accedi le credenziale dell'utente, per stampa "piu' carina"
		MyDataBase dataBase = new MyDataBase();

		try {
			String str = update.getMessage().getText();
			String[] tokens = str.split(" ");

			if (tokens.length != 2) // Condizione specificata per evitare scorretti inserimenti delle credenziale
			{
				response.setText(emojiiNoEntry + " Attenzione credenziali non corrette riprova! " + emojiiNoEntry);
				execute(response);
			} else {
				nickName = tokens[0];
				password = tokens[1];

				answer = dataBase.contains(tabUtente, nickName, password);

				if (answer) // Condizione per vericare se sia gia' avvenuta la registration dell'account
				{
					if (access == true) {
						response.setText(emojiiWellDone + " Accesso gia' eseguito " + emojiiWellDone);
						// TODO: cosa puoi fare dopo questo messaggio? 
					} else {
						access = true; // Evidenzia la possibilita' che la lettura delle notizie possa avvenire solamente con il proprio accesso
						response.setText(emojiiWellDone + " Accesso eseguito! " + emojiiWellDone);
					}
					
					execute(response);
				} else {
					response.setText(emojiiNoEntry + " Attenzione credenziali errate! " + emojiiNoEntry);
					execute(response);
				}
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo Modify specificato principalmente per permettere la mofica delle
	 * proprie credenziali.
	 * 
	 * Individuando una correlazione tra il metodo delete e il metodo registrazione
	 * (specificato per "l'utente stupido").
	 */

	public void Modify(SendMessage response, Update update) {
		MyDataBase dataBase = new MyDataBase();
		String lineModify = update.getMessage().getText();
		String[] tokens = lineModify.split(" ");
			
		try {
			if (c == 0) {
				if (tokens.length != 2) {
					response.setText(emojiiNoEntry + " Attenzione credenziali non corrette! " + emojiiNoEntry);
				} else {
					nickName = tokens[0];
					password = tokens[1];
					utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);

					if (utenteId != 0) {
						response.setText(emojiiWellDone + " Inserisci le nuove credenziali" + emojiiWellDone);
						c++;
					}

					execute(response);
				}
			} 
			else {
				if (tokens.length != 3) {
					response.setText(emojiiNoEntry + " Inserisci correttamente le nuove credenziali! " + emojiiNoEntry);
				}
				else {
					c = 0;
					
					nickName = tokens[0];
					password = tokens[1];
					sub = tokens[2];
					
					if(dataBase.alterRow(tabUtente, idUtente, nickName, password, sub, utenteId)) {
						response.setText(emojiiWellDone + " Inserimento eseguito! " + emojiiWellDone);
					}
					else {
						response.setText(emojiiNoEntry + " Inserimento non riuscito " + emojiiNoEntry);
					}
				}
				
				execute(response);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void Delete(SendMessage response, Update update) {
		MyDataBase dataBase = new MyDataBase();

		String lineRemove = update.getMessage().getText();
		String[] tokens = lineRemove.split(" ");

		try {
			if (tokens.length != 2) {
				response.setText(emojiiNoEntry + " Attenzione credenziali non corrette riprova! " + emojiiNoEntry);
				execute(response);
			} else {
				nickName = tokens[0];
				password = tokens[1];
				answer = dataBase.contains(tabUtente, nickName, password);

				if (answer) {
					utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);
					String utente = Integer.toString(utenteId);
					int countUtente = dataBase.countUtente(utenteId);
					dataBase.DeleteTable(tabUtente, nickName, password);
					notiziaId = dataBase.getID(tabNotizia, idNotizia, titolo, link);

					do {
						int countNotizia = dataBase.countNotizia(notiziaId);
						String notizia = Integer.toString(notiziaId);

						if (countNotizia == 1) {
							String[] arrayNotizia = dataBase.getNotizia(notiziaId);
							titolo = arrayNotizia[0];
							link = arrayNotizia[1];

							dataBase.DeleteTable(tabNotizia, titolo, link);
						}

						dataBase.DeleteTable(tabCommento, utente, notizia);
						countUtente--;
						notiziaId--;
					} while (countUtente != 0);

					response.setText(emojiiWellDone + " Eliminazione eseguita! " + emojiiWellDone);
				} else {
					response.setText(emojiiNoEntry + " Attenzione credenziali errate! " + emojiiNoEntry);
				}

				execute(response);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	

	public void ReadSearch(SendMessage response, Update update) {
		Response res = new Response();
		try {
			if (update.hasCallbackQuery()) {
				String callData = update.getCallbackQuery().getData();
				switch (callData) {
				case "NEXT":
					j++;
					titolo = arrayNotizia[j].getTitolo();
					link = arrayNotizia[j].getLink();

					if (j >= (arrayNotizia.length - 1)) {
						EditMessageText newResponseNext = res.setNewResponseNext(titolo, link, update);
						execute(newResponseNext);
					} else {
						EditMessageText newResponseNext = res.setNewResponse(titolo, link, update);
						execute(newResponseNext);
					}
					break;

				case "PREVIOUS":
					j--;
					titolo = arrayNotizia[j].getTitolo();
					link = arrayNotizia[j].getLink();
					if (j <= 0) {
						EditMessageText newResponsePrevious = res.setNewResponsePrevious(titolo, link, update);
						execute(newResponsePrevious);
					} else {
						EditMessageText newResponsePrevious = res.setNewResponse(titolo, link, update);
						execute(newResponsePrevious);
					}
					break;

				case "COMMENT":
					SendMessage newResponseComm = new SendMessage();
					long chatId = update.getCallbackQuery().getMessage().getChatId();
					newResponseComm.setChatId(chatId);
					Function("/commento", newResponseComm, update);
					break;
				}
			} else {
				String search = update.getMessage().getText();
				search.toLowerCase();

				FeedReader reader = new FeedReader();
				reader.run(search);

				Gson g = new GsonBuilder().setPrettyPrinting().create();
				arrayNotizia = g.fromJson(new FileReader("GsonImport.json"), Notizia[].class);

				j = 0;

				titolo = arrayNotizia[j].getTitolo();
				link = arrayNotizia[j].getLink();

				response = res.setResponse(titolo, link, update);
				execute(response);
			}
		} catch (IllegalArgumentException | IOException | TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void Comment(SendMessage response, Update update) {
		MyDataBase dataBase = new MyDataBase();

		if (dataBase.contains(tabNotizia, titolo, link)) {
			System.out.println("Notizia gia' contenuta");
		} else {
			dataBase.InsertTable(tabNotizia, titolo, link, null);
		}

		int notiziaId = dataBase.getID(tabNotizia, idNotizia, titolo, link);
		int utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);
		System.out.println(utenteId);

		String comment = update.getMessage().getText();
		String strUtente = Integer.toString(utenteId);
		String strNotizia = Integer.toString(notiziaId);

		if (dataBase.contains(tabCommento, strUtente, strNotizia)) {
			System.out.println("Commento gia' contenuto");
			String dataBaseComment = dataBase.getRecensione(tabCommento, strUtente, strNotizia);
			response.setText("Recensione gia' presente: " + dataBaseComment);
		} else {
			dataBase.InsertTable(tabCommento, comment, strUtente, strNotizia);
			response.setText("Recensione inserita: " + comment);
		}

		try {
			execute(response);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}