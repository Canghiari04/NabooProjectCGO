package botTelegram.response;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class ResponseFavorite {
	private String emojiiNext = "🔜", emojiiBack = "🔙", emojiiComment = "📝", emojiiViewComment = "📋", emojiiEliminate = "❌", emojiiNoEntry = "⛔️";

	/*
	 * Metodi setResponse che ritornano un specifico SendMessage, piuttosto che EditTextMessage, 
	 * nei riguardi dell'azione che porta alla lettura di multiple notizie, a discapito dell'evento 
	 * che riporti un unico risultato, differenziando messaggi di errore, suggerimenti o di buona riuscita.
	 */
	public SendMessage setResponseFav(Update update, String titolo, String link) {
		SendMessage response = new SendMessage();
		long chatId = update.getMessage().getChatId();

		response.setChatId(chatId);
		response.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineEliminate = new ArrayList<>();
		
		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiNext);
		nextBtn.setCallbackData("NEXTFAV");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");
		
		InlineKeyboardButton eliminateBtn = new InlineKeyboardButton();
		eliminateBtn.setText(emojiiEliminate + " Elimina preferenza " + emojiiEliminate);
		eliminateBtn.setCallbackData("ELIMINATEFAV");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineEliminate.add(eliminateBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineEliminate);

		markupInline.setKeyboard(rowsInline);
		response.setReplyMarkup(markupInline);

		return response;
	}
	
	public SendMessage setResponseAloneFav(Update update, String titolo, String link) {
		SendMessage response = new SendMessage();
		long chatId = update.getMessage().getChatId();

		response.setChatId(chatId);
		response.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineEliminate = new ArrayList<>();

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");
		
		InlineKeyboardButton eliminateBtn = new InlineKeyboardButton();
		eliminateBtn.setText(emojiiEliminate + " Elimina preferenza " + emojiiEliminate);
		eliminateBtn.setCallbackData("ELIMINATEFAV");

		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineEliminate.add(eliminateBtn);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineEliminate);

		markupInline.setKeyboard(rowsInline);
		response.setReplyMarkup(markupInline);

		return response;
	}
	
	public EditMessageText setNewResponseFav(Update update, String titolo, String link) {
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
		List<InlineKeyboardButton> rowInlineEliminate = new ArrayList<>();

		InlineKeyboardButton previousBtn = new InlineKeyboardButton();
		previousBtn.setCallbackData("PREVIOUSFAV");
		previousBtn.setText(emojiiBack + " Back");
		
		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setCallbackData("NEXTFAV");
		nextBtn.setText("Next " + emojiiNext);
		
		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");
		
		InlineKeyboardButton eliminateBtn = new InlineKeyboardButton();
		eliminateBtn.setText(emojiiEliminate + " Elimina preferenza " + emojiiEliminate);
		eliminateBtn.setCallbackData("ELIMINATEFAV");

		rowInline.add(previousBtn);
		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineEliminate.add(eliminateBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineEliminate);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
	
	public EditMessageText setNewResponsePreviousFav(Update update, String titolo, String link) {
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
		List<InlineKeyboardButton> rowInlineEliminate = new ArrayList<>();

		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiNext);
		nextBtn.setCallbackData("NEXTFAV");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");
		
		InlineKeyboardButton eliminateBtn = new InlineKeyboardButton();
		eliminateBtn.setText(emojiiEliminate + " Elimina preferenza " + emojiiEliminate);
		eliminateBtn.setCallbackData("ELIMINATEFAV");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineEliminate.add(eliminateBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineEliminate);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
	
	public EditMessageText setNewResponseNextFav(Update update, String titolo, String link) {
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
		List<InlineKeyboardButton> rowInlineEliminate = new ArrayList<>();
		
		InlineKeyboardButton previousBtn = new InlineKeyboardButton();
		previousBtn.setText(emojiiBack + " Back");
		previousBtn.setCallbackData("PREVIOUSFAV");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");
		
		InlineKeyboardButton eliminateBtn = new InlineKeyboardButton();
		eliminateBtn.setText(emojiiEliminate + " Elimina preferenza " + emojiiEliminate);
		eliminateBtn.setCallbackData("ELIMINATEFAV");

		rowInline.add(previousBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineEliminate.add(eliminateBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineEliminate);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
	
	public EditMessageText setResponseFavEdit(Update update, String titolo, String link) {
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
		List<InlineKeyboardButton> rowInlineEliminate = new ArrayList<>();
		
		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiNext);
		nextBtn.setCallbackData("NEXTFAV");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");
		
		InlineKeyboardButton eliminateBtn = new InlineKeyboardButton();
		eliminateBtn.setText(emojiiEliminate + " Elimina preferenza " + emojiiEliminate);
		eliminateBtn.setCallbackData("ELIMINATEFAV");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineEliminate.add(eliminateBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineEliminate);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}

	public EditMessageText setResponseAloneFavEdit(Update update, String titolo, String link) {
		EditMessageText newResponse = new EditMessageText();
		newResponse.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		long chatId = update.getCallbackQuery().getMessage().getChatId();
		newResponse.setChatId(chatId);

		long messageId = update.getCallbackQuery().getMessage().getMessageId();
		newResponse.setMessageId(toIntExact(messageId));

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineEliminate = new ArrayList<>();

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");
		
		InlineKeyboardButton eliminateBtn = new InlineKeyboardButton();
		eliminateBtn.setText(emojiiEliminate + " Elimina preferenza " + emojiiEliminate);
		eliminateBtn.setCallbackData("ELIMINATEFAV");

		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowInlineEliminate.add(eliminateBtn);
		rowsInline.add(rowInlineComment);
		rowsInline.add(rowInlineEliminate);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
	
	public EditMessageText setBlockResponseFav(Update update) {
		EditMessageText newResponse = new EditMessageText();
		newResponse.setText(emojiiNoEntry + " ERRORE:\n" + "Non e' presente alcuna notizia tra i tuoi preferiti");

		long chatId = update.getCallbackQuery().getMessage().getChatId();
		newResponse.setChatId(chatId);

		long messageId = update.getCallbackQuery().getMessage().getMessageId();
		newResponse.setMessageId(toIntExact(messageId));
		
		return newResponse;
	}
}