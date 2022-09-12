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
	private String emojiiNext = "🔜", emojiiBack = "🔙", emojiiComment = "📝", emojiiViewComment = "📋";

	public SendMessage setResponseFav(Update update, String titolo, String link) {
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
		nextBtn.setCallbackData("NEXTFAV");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);

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

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");

		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowsInline.add(rowInlineComment);

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

		InlineKeyboardButton previousBtn = new InlineKeyboardButton();
		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		InlineKeyboardButton commentBtn = new InlineKeyboardButton();

		previousBtn.setCallbackData("PREVIOUSFAV");
		previousBtn.setText(emojiiBack + " Back");

		nextBtn.setCallbackData("NEXTFAV");
		nextBtn.setText("Next " + emojiiNext);

		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");

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

		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiNext);
		nextBtn.setCallbackData("NEXTFAV");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);

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
		
		InlineKeyboardButton previousBtn = new InlineKeyboardButton();
		previousBtn.setText(emojiiBack + " Back");
		previousBtn.setCallbackData("PREVIOUSFAV");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiComment + " Commento ");
		commentBtn.setCallbackData("COMMENTFAV");

		InlineKeyboardButton viewCommentBtn = new InlineKeyboardButton();
		viewCommentBtn.setText(" Visualizza commenti " + emojiiViewComment);
		viewCommentBtn.setCallbackData("VIEWFAV");

		rowInline.add(previousBtn);
		rowInlineComment.add(commentBtn);
		rowInlineComment.add(viewCommentBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
}