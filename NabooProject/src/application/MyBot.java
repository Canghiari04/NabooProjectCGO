package application;

import java.io.*;
import java.util.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot 
{
	boolean answer = false, bool = false, access = false, reading = false;
	String nickName = " ", password = " ", function = " ";
	static Map<String, String> dictionaryUtente = new HashMap<String, String>(); // Always static !
	final File fileImport = new File("FileImport.txt");

    public String getBotUsername() 
    {
        return "NabooCGObot";
    }

    public String getBotToken() 
    {
        return "5471762884:AAGeRCek_JkVklyP7kYtTYwKL2Xio0ZtpfI";
    }
    
    public void popolaFile(String nickName, String password) // Writer del file per individuare in un prossimo start registrazioni di account gia' avvenute
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
    
    public void popolaDictionary() // Allo start del bot telegram inserisce all'interno del dictionary tutti gli account che abbiano gia'   
    { 							   // effettuato la registrazione al bot, dato che ad ogni avvio viene inizializzato il dictionary
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
    		function = str; // Salvo temporaneamente quale sia la funzione scelta precedentemente, in maniera tale che possa successivamente
    						// utilizzare il corretto update
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
			        
		        case "/elimina":
		        	
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
			     
		        case "/letturaNotizie":
		    
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
		        case "/elimina":
		        	
		        	elimina(response, update);
		        	
			        break;
			        
		        case "/letturaNotizie":
		        	
		        	letturaNotizie(response, update);
		        	
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
								
				if (dictionaryUtente.containsKey(nickName) && dictionaryUtente.containsValue(password)) // In caso dovesse essere presente un account con le stesse credenziali
				{																						// verra' richiesto nuovamento l'inserimento
					response.setText("Attenzione credenziali gia' presenti!");
					execute(response);
				} 
				else
				{
					dictionaryUtente.put(nickName, password);

					response.setText("Registrazione eseguita!");
					execute(response);
					
					reading = true; // Evidenzia la possibilita' che la lettura delle notizie possa avvenire solamente con la propria registrazione
					
					popolaFile(nickName, password); // Aggiungo le nuove credenziali all'interno del file, per popolare al prossimo avvio il dictionary
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

			if (tokens.length != 2) // Condizione specificata per evitare scorretti inserimenti delle credenziale
			{
				response.setText("Attenzione credenziale non corrette riprova!");
				execute(response);
			} 
			else
			{
				nickName = tokens[0];
				password = tokens[1];
				
				System.out.print(dictionaryUtente);
				
				if (dictionaryUtente.containsKey(nickName) && dictionaryUtente.containsValue(password)) // Condizione per vericare se sia gia' avvenuta la registrazione dell'account
				{
					response.setText("Accesso eseguito!");
					execute(response);
					
					reading = true; // Evidenzia la possibilita' che la lettura delle notizie possa avvenire solamente con il proprio accesso
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
    
    public void elimina(SendMessage response, Update update)
    {
    	// TODO: terminare eliminazione dell'account 
    		
    	// Leggere il file, trovare il token corretto, eliminare, per poi popolare nuovamente il dictionary
    	
    	popolaDictionary();
    	  
    }
    
    public void letturaNotizie(SendMessage response, Update update)
    {
    	if(reading != true)
    	{
    		try
    		{
    			response.setText("Attenzione devi prima effettura il login!");
        		execute(response);
    		}
    		catch (TelegramApiException e)
    		{
    			e.printStackTrace();
    		}
    		
    	}
    	else
    	{
    		ControllerNotizie controller = new ControllerNotizie();
    	}
    }
}