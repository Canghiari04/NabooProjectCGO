package application;

import java.util.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot 
{
	final Scanner scan = new Scanner(System.in);
	final Map<String, String> dictonaryUtente = new TreeMap<String, String>();

	@Override
	public void onUpdateReceived(Update update) {
	    // We check if the update has a message and the message has text
	    if (update.hasMessage() && update.getMessage().hasText()) {
	        SendMessage response = new SendMessage(); // Create a SendMessage object with mandatory fields
	        
	        response.setChatId(update.getMessage().getChatId().toString());
	        response.setText(update.getMessage().getText());
	        	  
	        // Trovare il modo per equals("differenti funzionalita'") del bot Telegram
	        
	        if(response.equals("/accedi"))
	        {
	        	response.setText("Inserisci il tuo nick name");
	        	response.getText();
	        	
	        	String nickName = scan.nextLine();
	        	String password = scan.nextLine();
	        	
	        	if(dictonaryUtente.containsValue(password) && dictonaryUtente.containsKey(nickName))
	        	{
	        		
	        	}
	        	else
	        	{
	        		registrazione(nickName, password);
	        	}
	        	
	        }
	        
	        
	        try {
	            execute(response); // Call method to send the message
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	    }
	}

    public String getBotUsername() {
        // TODO
        return "NabooCGObot";
    }

    public String getBotToken() {
        // TODO
        return "5471762884:AAGeRCek_JkVklyP7kYtTYwKL2Xio0ZtpfI";
    }
    
    public boolean registrazione(String nickName, String password)
    {
    	dictonaryUtente.put(nickName, password);
    	
    	Client c = new Client(nickName, password);
    	
    	// Client da aggiungere in qualche struttura dati per la successiva stampa nel file
    }
}