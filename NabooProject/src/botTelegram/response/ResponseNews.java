package botTelegram.response;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ResponseNews {
	private String emojiiNext = "🔜", emojiiBack = "🔙", emojiiStar = "⭐️", emojiiComment = "📝", emojiiViewComment = "📋";

	/*
	 * Metodi setResponse che ritornano un specifico SendMessage, piuttosto che EditTextMessage, 
	 * nei riguardi dell'azione che porta alla lettura di multiple notizie, a discapito dell'evento 
	 * che riporti un unico risultato, differenziando messaggi di errore, suggerimenti o di buona riuscita.
	 */
	public SendMessage setResponse(Update update, String titolo, String link) {
		SendMessage response = new SendMessage();
		long chatId = update.getMessage().getChatId();

		response.setChatId(chatId);
		response.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineFav = new ArrayList<>();
		
		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiNext);
		nextBtn.setCallbackData("NEXT");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEW");
		
		InlineKeyboardButton favBtn = new InlineKeyboardButton();
		favBtn.setText(emojiiStar + " Aggiungi ai preferiti " + emojiiStar);
		favBtn.setCallbackData("ADDNEW");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineFav.add(favBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineFav);

		markupInline.setKeyboard(rowsInline);
		response.setReplyMarkup(markupInline);

		return response;
	}

	public SendMessage setResponseAlone(Update update, String titolo, String link) {
		SendMessage response = new SendMessage();
		long chatId = update.getMessage().getChatId();

		response.setChatId(chatId);
		response.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineFav = new ArrayList<>();

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEW");
		
		InlineKeyboardButton favBtn = new InlineKeyboardButton();
		favBtn.setText(emojiiStar + " Aggiungi ai preferiti " + emojiiStar);
		favBtn.setCallbackData("ADDNEW");

		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineFav.add(favBtn);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineFav);

		markupInline.setKeyboard(rowsInline);
		response.setReplyMarkup(markupInline);

		return response;
	}
	
	public EditMessageText setNewResponse(Update update, String titolo, String link) {
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
		List<InlineKeyboardButton> rowInlineFav = new ArrayList<>();

		InlineKeyboardButton previousBtn = new InlineKeyboardButton();
		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		InlineKeyboardButton commentBtn = new InlineKeyboardButton();

		previousBtn.setCallbackData("PREVIOUS");
		previousBtn.setText(emojiiBack + " Back");

		nextBtn.setCallbackData("NEXT");
		nextBtn.setText("Next " + emojiiNext);

		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEW");

		InlineKeyboardButton favBtn = new InlineKeyboardButton();
		favBtn.setText(emojiiStar + " Aggiungi ai preferiti " + emojiiStar);
		favBtn.setCallbackData("ADDNEW");

		rowInline.add(previousBtn);
		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineFav.add(favBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineFav);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}

	public EditMessageText setNewResponseNext(Update update, String titolo, String link) {
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
		List<InlineKeyboardButton> rowInlineFav = new ArrayList<>();

		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiNext);
		nextBtn.setCallbackData("NEXT");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEW");
		
		InlineKeyboardButton favBtn = new InlineKeyboardButton();
		favBtn.setText(emojiiStar + " Aggiungi ai preferiti " + emojiiStar);
		favBtn.setCallbackData("ADDNEW");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineFav.add(favBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineFav);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
	
	public EditMessageText setNewResponsePrevious(Update update, String titolo, String link) {
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
		List<InlineKeyboardButton> rowInlineFav = new ArrayList<>();
		
		InlineKeyboardButton previousBtn = new InlineKeyboardButton();
		previousBtn.setText(emojiiBack + " Back");
		previousBtn.setCallbackData("PREVIOUS");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEW");
		
		InlineKeyboardButton favBtn = new InlineKeyboardButton();
		favBtn.setText(emojiiStar + " Aggiungi ai preferiti " + emojiiStar);
		favBtn.setCallbackData("ADDNEW");

		rowInline.add(previousBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineFav.add(favBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineFav);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
	
	public SendMessage setResponseCallBack(Update update, String titolo, String link) {
		SendMessage responseCallBack = new SendMessage();
		responseCallBack.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		long chatId = update.getCallbackQuery().getMessage().getChatId();
		responseCallBack.setChatId(chatId);

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineFav = new ArrayList<>();

		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiNext);
		nextBtn.setCallbackData("NEXT");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENT");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEW");

		InlineKeyboardButton favBtn = new InlineKeyboardButton();
		favBtn.setText(emojiiStar + " Aggiungi ai preferiti " + emojiiStar);
		favBtn.setCallbackData("ADDNEW");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineFav.add(favBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineFav);

		markupInline.setKeyboard(rowsInline);
		responseCallBack.setReplyMarkup(markupInline);

		return responseCallBack;
	}
}
