package botTelegram;

import static java.lang.Math.toIntExact;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class Response {
	private String emojiiWellDone = "✅";

	public SendMessage setResponse(String titolo, String link, Update update) {
		SendMessage response = new SendMessage();
		Long chatId = update.getMessage().getChatId();

		response.setChatId(chatId);
		response.setText("Titolo: " + titolo + "\n" + "\nLink: " + link + "\n\nPreview notizia: \n");

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();

		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiWellDone);
		nextBtn.setCallbackData("NEXT");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiWellDone + " Commento " + emojiiWellDone);
		commentBtn.setCallbackData("COMMENT");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
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
		previousBtn.setText("Prev " + emojiiWellDone);

		nextBtn.setCallbackData("NEXT");
		nextBtn.setText("Next " + emojiiWellDone);

		commentBtn.setText(emojiiWellDone + " Commento " + emojiiWellDone);
		commentBtn.setCallbackData("COMMENT");

		rowInline.add(previousBtn);
		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
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
		System.out.println(chatId);

		long messageId = update.getCallbackQuery().getMessage().getMessageId();
		newResponse.setMessageId(toIntExact(messageId));

		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInline = new ArrayList<>();
		List<InlineKeyboardButton> rowInlineComment = new ArrayList<>();

		InlineKeyboardButton nextBtn = new InlineKeyboardButton();
		nextBtn.setText("Next " + emojiiWellDone);
		nextBtn.setCallbackData("NEXT");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiWellDone + " Commento " + emojiiWellDone);
		commentBtn.setCallbackData("COMMENT");

		rowInline.add(nextBtn);
		rowInlineComment.add(commentBtn);
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
		previousBtn.setText("Prev " + emojiiWellDone);
		previousBtn.setCallbackData("PREVIOUS");

		InlineKeyboardButton commentBtn = new InlineKeyboardButton();
		commentBtn.setText(emojiiWellDone + " Commento " + emojiiWellDone);
		commentBtn.setCallbackData("COMMENT");

		rowInline.add(previousBtn);
		rowInlineComment.add(commentBtn);
		rowsInline.add(rowInline);
		rowsInline.add(rowInlineComment);

		markupInline.setKeyboard(rowsInline);
		newResponse.setReplyMarkup(markupInline);

		return newResponse;
	}
}