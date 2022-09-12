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
	private String emojiiPlus = "➕", emojiiLess = "➖";
	private MyDataBase dataBase = new MyDataBase();
	
	/*
	 * Metodi setResponse che returnano un specifico SendMessage, piuttosto che EditTextMessage, nei riguardi dell'azione
	 * che porta alla lettura di multiple notizie, a discapito dell'evento che riporti un unico risultato, oltre alla 
	 * registrazione e modifica delle proprie credenziali.
	 */
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
		modifyResponse.setText("Un ultimo passo, quale tipologia di abbonamento preferisci");
		
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
		feedResponse.setText("Inserisci il termine da ricercare.\n\nQui sotto sono riportate le disponibili categorie\n");
		
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
		feedResponse.setText("Inserisci il termine da ricercare.\n\nQui sotto sono riportate le disponibili categorie\n");
		
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