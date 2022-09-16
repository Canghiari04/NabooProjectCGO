package botTelegram.response;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import dataBase.MyDataBase;

public class ResponseOperate {
	private String emojiiPlus = "➕", emojiiLess = "➖", emojiHint = "💡", emojiiJournal = "📰";
	private String emojiOne = "1️⃣", emojiTwo = "2️⃣", emojiThree = "3️⃣", emojiFour = "4️⃣", emojiFive = "5️⃣";
	private MyDataBase dataBase = new MyDataBase();
	
	/*
	 * Metodi setResponse che ritornano un specifico SendMessage, piuttosto che EditTextMessage, nei riguardi di azioni
	 * che portano all'utilizzo delle funzionalita' principali, differenziando messaggi di errore, suggerimenti o di buona riuscita.
	 */
	public SendMessage setResponseStart(Update update) {
		SendMessage newResponse = new SendMessage();
		long chatId = update.getMessage().getChatId();
		newResponse.setChatId(chatId);		

		newResponse.setText(emojiiJournal + " BENVENUTO NEL BOT NABOO " + emojiiJournal 
				+ "\nQuesto bot ti permette di leggere notizie divise per categorie.\n"
				+ "Per poter utilizzare il bot devi essere registrato:\n"
				+ "Se è la prima volta che utilizzi il bot digita /registrazione.\n"
				+ "Se hai già effettuato la registrazione allora digita /accedi.\n"
				+ "In seguito all’accesso, digitando /legginotizie, potrai scegliere tra le varie categorie e scorrere tra le notizie ad esse correlate.\n"
				+ "Inoltre potrai aggiungere preferenze, commentare e visualizzare recensioni di altri utenti tramite gli appositi i comandi.\n"
				+ "Infine tramite i comandi /visualizzapreferiti e /visualizzacommenti, potrai visualizzare corrispettivamente le notizie che abbiano una preferenza e le tue recensioni rilasciate.\n"
				+ "\n"
				+ "(Progetto sviluppato da Matteo Canghiari, Ossama Nadifi, studenti della facoltà di Informatica per il Management dell’Università di Bologna)");	
		return newResponse;
	}
	
	public String setResponseWriteCredentials() {
		String newResponse = " ";	
		newResponse = (emojiHint + " SUGGERIMENTO:\n" + "Le credenziali devono essere scritte nella forma \"username\" spazio \"password\".");	
		return newResponse;
	}
	
	public SendMessage setSuggestionAccess(Update update) {
		SendMessage responseSuggestion = new SendMessage();
		long chatId = update.getMessage().getChatId();
		responseSuggestion.setChatId(chatId);		

		responseSuggestion.setText(emojiHint + " SUGGERIMENTO:\n"
				+ "Hai gia' effettuato l'accesso.\n"
				+ emojiOne + " Digita /legginotizie per leggere le notizie disponibili.\n"
				+ emojiTwo + " Digita /visualizzapreferiti per visualizzare le notizie che abbiano una preferenza.\n"
				+ emojiThree + " Digita /visualizzacommenti per visualizzare i commenti che hai rilasciato.\n"
				+ "(funzione disponibile solo per gli account PREMIUM)\n"
				+ emojiFour + " Digita /modificafeed per stabilire le tue preferenza di lettura.\n"
				+ "(funzione disponibile solo per gli account PREMIUM)");
		return responseSuggestion;
	}
	
	public SendMessage setSuggestionFunction(Update update) {
		SendMessage responseSuggestion = new SendMessage();
		long chatId = update.getMessage().getChatId();
		responseSuggestion.setChatId(chatId);		
		responseSuggestion.setText(emojiHint + " SUGGERIMENTO:\n"
				+ "Non hai ancora effettuato l'accesso.\n"
				+ emojiOne + " Digita /registrazione per registrarti per la prima volta\n"
				+ emojiTwo + " Digita /accedi se hai già effettuato la registazione\n\n"
				+ "Per accedere alle funzionalita' del bot NABOO:\n"
				+ emojiThree + " Digita /visualizzapreferiti per visualizzare le notizie che abbiano una preferenza.\n"
				+ emojiFour + " Digita /visualizzacommenti per visualizzare i commenti che hai rilasciato.\n"
				+ "(funzione disponibile solo per gli account PREMIUM)\n"
				+ emojiFive + " Digita /modificafeed per stabilire le tue preferenza di lettura.\n"
				+ "(funzione disponibile solo per gli account PREMIUM)");
		return responseSuggestion;
	}
	
	public SendMessage setSuggestionChangeFunction(Update update) {
		SendMessage responseSuggestion = new SendMessage();
		long chatId = update.getMessage().getChatId();
		responseSuggestion.setChatId(chatId);		
		responseSuggestion.setText(emojiHint + " SUGGERIMENTO:\n"
				+ "Per sbloccare questa funzionalità l'account deve essere di tipo PREMIUM.\n"
				+ emojiOne + " Digita /modifica per modificare la tua registazione al bot e scegliere le funzionalità PREMIUM.");
		return responseSuggestion;
		
	}
	
	public SendMessage setRegistrationResponse(Update update) {
		SendMessage registrationResponse = new SendMessage();
		long chatId = update.getMessage().getChatId();
		registrationResponse.setChatId(chatId);		
		registrationResponse.setText("Un ultimo passo, quale tipologia di abbonamento preferisci");
		
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();

		InlineKeyboardButton premiumBtn = new InlineKeyboardButton();
		premiumBtn.setText("Premium");
		premiumBtn.setCallbackData("PREMIUM");

		InlineKeyboardButton baseBtn = new InlineKeyboardButton();
		baseBtn.setText("Base");
		baseBtn.setCallbackData("BASE");

		rowInline.add(premiumBtn);
		rowInline.add(baseBtn);
		rowsInline.add(rowInline);

		markupInline.setKeyboard(rowsInline);
		registrationResponse.setReplyMarkup(markupInline);

		return registrationResponse;
	}
	
	public SendMessage setModifyResponse(Update update) {
		SendMessage modifyResponse = new SendMessage();
		long chatId = update.getMessage().getChatId();
		modifyResponse.setChatId(chatId);		
		modifyResponse.setText("Un ultimo passo, quale tipologia di abbonamento preferisci:");
		
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();

		InlineKeyboardButton premiumBtn = new InlineKeyboardButton();
		premiumBtn.setText("Premium");
		premiumBtn.setCallbackData("MODIFYPREMIUM");

		InlineKeyboardButton baseBtn = new InlineKeyboardButton();
		baseBtn.setText("Base");
		baseBtn.setCallbackData("MODIFYBASE");

		rowInline.add(premiumBtn);
		rowInline.add(baseBtn);
		rowsInline.add(rowInline);

		markupInline.setKeyboard(rowsInline);
		modifyResponse.setReplyMarkup(markupInline);

		return modifyResponse;
	}
	
	public SendMessage setFeedCategoryPremium(Update update) throws HeadlessException, IllegalArgumentException, SQLException {
		SendMessage feedResponse = new SendMessage();
		long chatId = update.getMessage().getChatId();
		feedResponse.setChatId(chatId);		
		feedResponse.setText("Inserisci il termine da ricercare.\n\nOppure qui sotto sono riportate le categorie disponibili:\n");
		
		ArrayList<String> arrayFeed = dataBase.getFeedsTot();

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

		for(String str : arrayFeed) {
			InlineKeyboardButton btn = new InlineKeyboardButton();
			btn.setText(str.toUpperCase());
			btn.setCallbackData(str);
			
			List<InlineKeyboardButton> rowInline = new ArrayList<>();
			rowInline.add(btn);
			rowsInline.add(rowInline);
		}
		
		InlineKeyboardButton btn = new InlineKeyboardButton();
		btn.setText("PERSONALIZZATA");
		btn.setCallbackData("personalizzata");
		
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		rowInline.add(btn);
		rowsInline.add(rowInline);

		markupInline.setKeyboard(rowsInline);
		feedResponse.setReplyMarkup(markupInline);

		return feedResponse;
	}
	
	public SendMessage setFeedCategoryBase(Update update) throws HeadlessException, IllegalArgumentException, SQLException {
		SendMessage feedResponse = new SendMessage();
		long chatId = update.getMessage().getChatId();
		feedResponse.setChatId(chatId);		
		feedResponse.setText("Inserisci il termine da ricercare.\n\nOppure qui sotto sono riportate le categorie disponibili:\n");
		
		ArrayList<String> arrayFeed = dataBase.getFeedsTot();

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

		for(String str : arrayFeed) {
			InlineKeyboardButton btn = new InlineKeyboardButton();
			btn.setText(str.toUpperCase());
			btn.setCallbackData(str);
			
			List<InlineKeyboardButton> rowInline = new ArrayList<>();
			rowInline.add(btn);
			rowsInline.add(rowInline);
		}

		markupInline.setKeyboard(rowsInline);
		feedResponse.setReplyMarkup(markupInline);

		return feedResponse;
	}
		
	public SendMessage setFeedDataResponse(Update update, SendMessage response, String tabUtente, String idUtente, String nickName, String password) throws HeadlessException, SQLException {
		SendMessage feedResponse = new SendMessage();
		long chatId = update.getMessage().getChatId();

		feedResponse.setChatId(chatId);		
		feedResponse = dataBase.getFeedUser(response, tabUtente, idUtente, nickName, password);
		
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();

		InlineKeyboardButton addBtn = new InlineKeyboardButton();
		addBtn.setText(emojiiPlus + " Aggiungi ");
		addBtn.setCallbackData("ADD");

		InlineKeyboardButton eliminateBtn = new InlineKeyboardButton();
		eliminateBtn.setText(" Elimina " + emojiiLess);
		eliminateBtn.setCallbackData("ELIMINATE");

		rowInline.add(addBtn);
		rowInline.add(eliminateBtn);
		rowsInline.add(rowInline);

		markupInline.setKeyboard(rowsInline);
		feedResponse.setReplyMarkup(markupInline);

		return feedResponse;
	}
}