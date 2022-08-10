package botTelegram;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
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
	private String tabUtente = "Utente", tabNotizia = "Notizia", tabCommento = "Commento", idUtente = "UtenteID", idNotizia = "NotiziaID", idCommento = "CommentoID"; // Specificata per popolare la tabella Utente del
																// database
	private int c = 0, j = 0, utenteId = 0; // Contatore utilizzato nel metodo modify
	private static ArrayList<Utente> arrayUtente = new ArrayList<Utente>();
	private static File fileImport = new File("FileImport.txt");
	private static File fileEliminate = new File("fileEliminate.txt");
	private static ArrayList<Notizia> arrayListNotizia;

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
	 * Metodo PopulateFile che rende la possibilita' di specificare utenti che si
	 * siano gia' registrati all'avvio.
	 * 
	 * Prende in considerazione l'utilizzo del file in cui viene salvata ogni
	 * modifica.
	 */

	public void PopulateFile(String nickName, String password, String sub) {
		try {
			FileWriter writerImport = new FileWriter(fileImport, true);
			String str = nickName + " " + password + " " + sub + "\n";

			writerImport.write(str);
			writerImport.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo ClearFile che predispone l'inizializzazione del fileEliminate su cui
	 * vengono temporaneamente salvati account che non subiscono alcuna modifica
	 */

	public void ClearFile(File f) {
		try {
			PrintWriter writer = new PrintWriter(f);
			writer.print("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo CopyFile che permette la copia del fileEliminate nei confronti del
	 * fileImport, utilizzato all'avvio del bot.
	 */

	public void CopyFile(File fileInput, File fileOutput) {
		try {
			FileUtils.copyFile(fileInput, fileOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Metodo PopulateArray inserisce all'interno del'arrayList tutti gli account
	 * che abbiano gia' effettuato una registrazione al bot Naboo.
	 * 
	 * Questo dovuto poiche' ad ogni avvio la struttura viene inizializzata.
	 */

	public void PopulateArray(File f) {
		arrayUtente.clear();

		try {
			Scanner scanFile = new Scanner(f);

			while (scanFile.hasNext()) {
				String line = scanFile.nextLine();
				String[] tokens = line.split(" ");
				nickName = tokens[0];
				password = tokens[1];
				sub = tokens[2];

				Utente u = new Client(nickName, password, sub);
				arrayUtente.add(u);
			}

			scanFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(arrayUtente);
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
			function = str; /*
							 * Salvo temporaneamente quale sia la funzione scelta precedentemente, in
							 * maniera tale che possa successivamente utilizzare il corretto update
							 */
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
				if (c == 0) {
					Modify(response, update);
				} else {
					c = 0;
					function = "/registrazione";
					Registration(response, update); // Richiamo il metodo registration per rendere possibile la modifica
													// delle proprie credenziali
				}
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

				Utente u = new Client(nickName, password, sub);

				answer = getIn(nickName, password);

				if (answer) /*
							 * In caso dovesse essere presente un account con le stesse credenziali verra'
							 * richiesto nuovamente l'inserimento
							 */
				{
					response.setText(emojiiNoEntry + " Attenzione credenziali gia' presenti! " + emojiiNoEntry);
					execute(response);
				} else {
					access = true; /*
									 * Evidenzia la possibilita' che la lettura delle notizie possa avvenire
									 * solamente con la propria registration
									 */

					arrayUtente.add(u);
					PopulateFile(nickName, password, sub); /*
															 * Aggiungo le nuove credenziali all'interno del file, per
															 * popolare al prossimo avvio il dictionary
															 */

					dataBase.InsertTable(tabUtente, nickName, password, sub);
					utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);

					response.setText(emojiiWellDone + " Registrazione eseguita! " + emojiiWellDone);
					execute(response);
				}

				System.out.println(arrayUtente);
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

			if (tokens.length != 2) /* Condizione specificata per evitare scorretti inserimenti delle credenziale */
			{
				response.setText(emojiiNoEntry + " Attenzione credenziali non corrette riprova! " + emojiiNoEntry);
				execute(response);
			} else {
				nickName = tokens[0];
				password = tokens[1];

				answer = getIn(nickName, password);

				if (answer) /* Condizione per vericare se sia gia' avvenuta la registration dell'account */
				{
					access = true; /*
									 * Evidenzia la possibilita' che la lettura delle notizie possa avvenire
									 * solamente con il proprio accesso
									 */
					utenteId = dataBase.getID(tabUtente, idUtente, nickName, password);

					response.setText(emojiiWellDone + " Accesso eseguito! " + emojiiWellDone);
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
		String[] marks = lineModify.split(" ");

		ClearFile(fileEliminate);

		try {
			Scanner scanFile = new Scanner(fileImport);
			FileWriter writerImport = new FileWriter(fileEliminate);

			if (marks.length != 2) {
				response.setText(emojiiNoEntry + " Attenzione credenziali non corrette riprova! " + emojiiNoEntry);
				execute(response);
			} else {
				answer = getIn(marks[0], marks[1]);

				if (answer) {
					while (scanFile.hasNext()) {
						String line = scanFile.nextLine();
						String[] tokens = line.split(" ");
						nickName = tokens[0];
						password = tokens[1];
						sub = tokens[2];

						if (marks[0].equals(nickName) && marks[1].equals(password)) {
							dataBase.DeleteTable(tabUtente, nickName, password);
						} else {
							String str = nickName + " " + password + " " + sub + "\n";
							writerImport.write(str);
						}
					}

					c++; /*
							 * Contatore utilizzato per rendere possibile la modify delle proprie
							 * credenziali, individuando una correlazione con il metodo registration
							 */

					scanFile.close();
					writerImport.close();

					CopyFile(fileEliminate, fileImport);
					PopulateArray(fileEliminate);

					response.setText("Inserisci le nuove credenziali");
					execute(response);
				} else {
					response.setText(emojiiNoEntry + " Attenzione credenziali errate! " + emojiiNoEntry);
					execute(response);
				}
			}

		} catch (IOException | TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void Delete(SendMessage response, Update update) {
		MyDataBase dataBase = new MyDataBase();

		String lineRemove = update.getMessage().getText();
		String[] marks = lineRemove.split(" ");

		ClearFile(fileEliminate);

		try {
			Scanner scanFile = new Scanner(fileImport);
			FileWriter writerImport = new FileWriter(fileEliminate);

			if (marks.length != 2) {
				response.setText(emojiiNoEntry + " Attenzione credenziali non corrette riprova! " + emojiiNoEntry);
				execute(response);
			} else {
				answer = getIn(marks[0], marks[1]);

				if (answer) {
					while (scanFile.hasNext()) {
						String line = scanFile.nextLine();
						String[] tokens = line.split(" ");

						nickName = tokens[0];
						password = tokens[1];
						sub = tokens[2];

						if (marks[0].equals(nickName) && marks[1].equals(password)) {
							dataBase.DeleteTable(tabUtente, nickName, password);
						} else {
							String str = nickName + " " + password + " " + sub + "\n";
							writerImport.write(str);
						}
					}

					scanFile.close();
					writerImport.close();

					CopyFile(fileEliminate, fileImport);
					PopulateArray(fileEliminate);

					response.setText(emojiiWellDone + " Eliminazione eseguita! " + emojiiWellDone);
					execute(response);
				} else {
					response.setText(emojiiNoEntry + " Attenzione credenziali errate! " + emojiiNoEntry);
					execute(response);
				}
			}
		} catch (IOException | TelegramApiException e) {
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
					titolo = arrayListNotizia.get(j).getTitolo();
					link = arrayListNotizia.get(j).getLink();
					if (j >= (arrayListNotizia.size() - 1)) {
						EditMessageText newResponseNext = res.setNewResponseNext(titolo, link, update);
						execute(newResponseNext);
					} else {
						EditMessageText newResponseNext = res.setNewResponse(titolo, link, update);
						execute(newResponseNext);
					}
					break;

				case "PREVIOUS":
					j--;
					titolo = arrayListNotizia.get(j).getTitolo();
					link = arrayListNotizia.get(j).getLink();
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
				FeedReader reader = new FeedReader();
				reader.run();

				Gson g = new GsonBuilder().setPrettyPrinting().create();
				Notizia[] arrayNotizia = g.fromJson(new FileReader("GsonImport.json"), Notizia[].class);
				arrayListNotizia = new ArrayList<Notizia>();
				String search = update.getMessage().getText();
				search.toLowerCase();

				if (search.equals("tutto")) {
					for (Notizia x : arrayNotizia) {
						arrayListNotizia.add(x);
					}
				} else {
					for (Notizia x : arrayNotizia) {
						link = x.getLink();

						if (link.contains(search)) {
							arrayListNotizia.add(x);
						}
					}
				}
				j = 0;

				titolo = arrayListNotizia.get(j).getTitolo();
				link = arrayListNotizia.get(j).getLink();

				response = res.setResponse(titolo, link, update);
				execute(response);
			}
		} catch (IllegalArgumentException | IOException | TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void Comment(SendMessage response, Update update) {
		MyDataBase dataBase = new MyDataBase();
		int notiziaId = dataBase.getID(tabNotizia, idNotizia, titolo, link);

		if (dataBase.contains(tabNotizia, titolo, link)) {
			System.out.println("Notizia gia' contenuta");
		} else {
			dataBase.InsertTable(tabNotizia, titolo, link, null);
		}

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