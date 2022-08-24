package botTelegram;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import dataBase.MyDataBase;

public class Response {
	private String emojiiNext = "🔜", emojiiBack = "🔙";
	private static MyDataBase dataBase = new MyDataBase();
	
	public SendMessage setRegistrationResponse(Update update, SendMessage response) {
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
	
	public SendMessage setModifyResponse(Update update, SendMessage response) {
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
	
	public SendMessage setResponse(String titolo, String link, Update update) {
		SendMessage response = new SendMessage();
		long chatId = update.getMessage().getChatId();

		response.setChatId(chatId);
		response.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();

		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiNext);
		nextBtn.setCallbackData("NEXT");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(" Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti ");
		viewCommentBtn.setCallbackData("VIEW");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);

		markupInline.setKeyboard(rowsInline);
		response.setReplyMarkup(markupInline);

		return response;
	}

	public EditMessageText setNewResponse(String titolo, String link, Update update) {
		EditMessageText newResponse = new EditMessageText();
		newResponse.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		long chatId = update.getCallbackQuery().getMessage().getChatId();
		newResponse.setChatId(chatId);

		long messageId = update.getCallbackQuery().getMessage().getMessageId();
		newResponse.setMessageId(toIntExact(messageId));

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();

		InlineKeyboardButton previousBtn = new InlineKeyboardButton();
		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		InlineKeyboardButton commentBtn = new InlineKeyboardButton();

		previousBtn.setCallbackData("PREVIOUS");
		previousBtn.setText(emojiiBack + " Back");

		nextBtn.setCallbackData("NEXT");
		nextBtn.setText("Next " + emojiiNext);

		commentBtn.setText(" Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti ");
		viewCommentBtn.setCallbackData("VIEW");

		rowInline.add(previousBtn);
		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
	
	public EditMessageText setNewResponsePrevious(String titolo, String link, Update update) {
		EditMessageText newResponse = new EditMessageText();
		newResponse.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		long chatId = update.getCallbackQuery().getMessage().getChatId();
		newResponse.setChatId(chatId);

		long messageId = update.getCallbackQuery().getMessage().getMessageId();
		newResponse.setMessageId(toIntExact(messageId));

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();

		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiNext);
		nextBtn.setCallbackData("NEXT");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(" Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti ");
		viewCommentBtn.setCallbackData("VIEW");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
	
	public EditMessageText setNewResponseNext(String titolo, String link, Update update) {
		EditMessageText newResponse = new EditMessageText();
		newResponse.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		long chatId = update.getCallbackQuery().getMessage().getChatId();
		newResponse.setChatId(chatId);

		long messageId = update.getCallbackQuery().getMessage().getMessageId();
		newResponse.setMessageId(toIntExact(messageId));

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();

		InlineKeyboardButton previousBtn = new InlineKeyboardButton();
		previousBtn.setText(emojiiBack + " Back");
		previousBtn.setCallbackData("PREVIOUS");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(" Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti ");
		viewCommentBtn.setCallbackData("VIEW");

		rowInline.add(previousBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
		
	public SendMessage setFeedDataResponse(Update update, SendMessage response, String tabUtente, String idUtente, String nickName, String password) {
		SendMessage feedResponse = new SendMessage();
		long chatId = update.getMessage().getChatId();

		feedResponse.setChatId(chatId);		
		feedResponse = dataBase.getResponse(response, tabUtente, idUtente, nickName, password);
		
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();

		InlineKeyboardButton addBtn = new InlineKeyboardButton();
		addBtn.setText("Aggiungi");
		addBtn.setCallbackData("ADD");

		InlineKeyboardButton eliminateBtn = new InlineKeyboardButton();
		eliminateBtn.setText("Elimina");
		eliminateBtn.setCallbackData("ELIMINATE");

		rowInline.add(addBtn);
		rowInline.add(eliminateBtn);
		rowsInline.add(rowInline);

		markupInline.setKeyboard(rowsInline);
		feedResponse.setReplyMarkup(markupInline);

		return feedResponse;
	}
}