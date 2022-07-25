package application;

import java.util.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot 
{
	boolean answer = false;
	String function = " ";
	final Map<String, String> dictonaryUtente = new TreeMap<String, String>();

    public String getBotUsername() {
        // TODO
        return "NabooCGObot";
    }

    public String getBotToken() {
        // TODO
        return "5471762884:AAGeRCek_JkVklyP7kYtTYwKL2Xio0ZtpfI";
    }
    
    public void onUpdateReceived(Update update)
    {
    	if (update.hasMessage() && update.getMessage().hasText()) 
    	{
  	        
	        String str = update.getMessage().getText();
	        long chatId = update.getMessage().getChatId();
	        
	        SendMessage response = new SendMessage();
	        response.setChatId(chatId); 
	        
	        function(str, response, update);
	    }
    }
    
    public void function(String str, SendMessage response, Update update)
    {
    	char ch = str.charAt(0);
    	
    	if(ch == '/')
    	{
    		function = str;
    	}
    	else
    	{
    		switch(function)
		    {
		    	case "/start":
		    		
		    		start(update);
		    		
		    		break;
		        
		        case "/registrazione":
		        	
		        	registrazione(response, update);
		        	
		        	break;
		        	
		        case "/accedi":
		        	
		        	accedi(response, update);
		        	
			        break;	
		    }
    	}
    }
    
    public void start(Update update) 
    {
        SendMessage message = new SendMessage();
        long chatId = update.getMessage().getChatId();
        
        message.setText("Benvenuto nel bot NABOO!");
        message.setChatId(chatId);
        
        try 
        {
            execute(message);
        } 
        catch (TelegramApiException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void registrazione(SendMessage response, Update update)
    {
  	
    	do {
    		
    		try
    		{
    			response.setText("Inserisci il tuo nick name e la password, separati da uno spazio");
	       		execute(response);
	       		
	       		// TODO: registrazione che puo' avvenire solamente tramite certi criteri, nickName e password divisi da uno spazio, obv e' possibile il nuovo inserimento
	       		
	       		Update updateNickName = new Update();
	       		Update updatePassword = new Update();
	       		
	       		// TODO: meglio farselo dare tutto'uno, per poi separarlo tramite un split con l'opportuno delimitatore
	       		
	       		// TODO: dividire nickName e password tramite split
	       		
	       		String nickName = updateNickName.getMessage().getText();
	       		String password = updatePassword.getMessage().getText();
	       		
	       		System.out.print(nickName + " " + password);
		        	
	        	if(dictonaryUtente.containsKey(nickName) || dictonaryUtente.containsValue(password))
	        	{
	        		response.setText("Attenzione credenziali gia' presenti!");
	        		execute(response);
		        }
		       	else
		       	{
		       		answer = true;
		       		dictonaryUtente.put(nickName, password);
		       		Client c = new Client(nickName, password);
		       	}
    		}
    		catch (TelegramApiException e)
    		{
    			e.printStackTrace();
    		}
    	
    	}while(answer != true);
    }
    
    public void accedi(SendMessage response, Update update) 
    {
    	do { 
    		
    		try
    		{
	       		response.setText("Inserisci il tuo nick name e la password, separati da uno spazio");
	       		execute(response);
	       		
	       		// TODO: meglio farselo dare tutto'uno, per poi separarlo tramite un split con l'opportuno delimitatore
	       		
	       		// TODO: dividire nickName e password tramite split
	       		
	       		String nickName = update.getMessage().getText();
	       		String password = update.getMessage().getText();
		        	
	        	if(!dictonaryUtente.containsKey(nickName) || !dictonaryUtente.containsValue(password))
	        	{
	        		answer = true;
	        		response.setText("im in");
	        		execute(response);
	        		
	        		// Visualization of the next function
		        }
		       	else
		       	{
		       		response.setText("Credenziali non valide!");
		       		execute(response);
		        }
    		}
    		catch (TelegramApiException e)
    		{
    			e.printStackTrace();
    		}
    	}while(answer != true);	
    }
}