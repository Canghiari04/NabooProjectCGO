package application;

import java.io.*;
import java.util.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot 
{
	boolean answer = false, bool = false, access = false;
	String nickName = " ", password = " ", function = " ";
	static Map<String, String> dictionaryUtente = new HashMap<String, String>();
	final File fileImport = new File("FileImport.txt");

    public String getBotUsername() {
        // TODO
        return "NabooCGObot";
    }

    public String getBotToken() {
        // TODO
        return "5471762884:AAGeRCek_JkVklyP7kYtTYwKL2Xio0ZtpfI";
    }
    
    public void popolaFile(String nickName, String password) // TODO: comment of the different function
    {
    	try
    	{
	    	FileWriter writerImport = new FileWriter(fileImport, true);
			String str = nickName + "," + password + "\n";

			writerImport.write(str);
	    	writerImport.close();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void popolaDictionary()
    {
    	try
    	{
        	Scanner scanFile = new Scanner(fileImport);
        	
			while (scanFile.hasNext()) 
			{
				String line = scanFile.nextLine();
				String[] tokens = line.split(",");
				
				nickName = tokens[0];
				password = tokens[1];
												
				dictionaryUtente.put(nickName, password);				
			}
						
			scanFile.close();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}      
    	
		System.out.println(dictionaryUtente);
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
    		
    		switch(function)
		    {
		    	case "/start":
		    		
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
		    		
		    		break;
		        
		        case "/registrazione":
		        			        	
		        	try
		        	{
		        		response.setText("Inserisci il tuo nick name e la password, separati da uno spazio");
			       		execute(response);
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
		        	break;
		        	
		        case "/accedi":
		        	
		        	try
		        	{
		        		response.setText("Inserisci il tuo nick name e la password, separati da uno spazio");
			       		execute(response);
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
			        break;	
			     
		        case "/letturaNotizia":
		    
		        	break;
		    }
    	}
    	else
    	{
    		switch(function)
		    {   
		        case "/registrazione":
		        	
		        	registrazione(response, update);
		        	
		        	break;
		        	
		        case "/accedi":
		        	
		        	accedi(response, update);
		        	
			        break;	
			        
		        case "/letturaNotizie":
		        	
		        	break;
		    }
    	}
    }
    
    public void registrazione(SendMessage response, Update update)
    {  		    	
		try 
		{
			String str = update.getMessage().getText();

			String[] tokens = str.split(" ");

			if (tokens.length != 2) 
			{
				response.setText("Attenzione credenziali non corrette riprova!");
				execute(response);
			} 
			else
			{
				nickName = tokens[0];
				password = tokens[1];
								
				if (dictionaryUtente.containsKey(nickName) && dictionaryUtente.containsValue(password)) 
				{
					response.setText("Attenzione credenziali gia' presenti!");
					execute(response);
				} 
				else
				{
					answer = true;
					dictionaryUtente.put(nickName, password);

					response.setText("Registrazione eseguita!");
					execute(response);
					
					popolaFile(nickName, password);
				}
				
				System.out.println(dictionaryUtente);
			}
		} 
		catch (TelegramApiException e) 
		{
			e.printStackTrace();
		}
    }
    
    public void accedi(SendMessage response, Update update) 
    {   		
		try 
		{
			String str = update.getMessage().getText();

			String[] tokens = str.split(" ");

			if (tokens.length != 2) 
			{
				response.setText("Attenzione credenziale non corrette riprova!");
				execute(response);
			} 
			else
			{
				nickName = tokens[0];
				password = tokens[1];
				
				System.out.print(dictionaryUtente);
				
				if (dictionaryUtente.containsKey(nickName) && dictionaryUtente.containsValue(password)) 
				{
					answer = true;
					response.setText("Accesso eseguito!");
					execute(response);
				} 
				else
				 {
					response.setText("Credenziali non valide!");
					execute(response);
				}
			}
		}
		catch (TelegramApiException e) 
		{
			e.printStackTrace();
		}
    }
}