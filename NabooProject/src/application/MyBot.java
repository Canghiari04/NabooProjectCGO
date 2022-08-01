package application;

import java.io.*;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot // Classe che si focalizza sull'update ricevuto dall'utente
{
	boolean answer = false, reading = false;
	String nickName = " ", password = " ", function = " ";
	String tabUtente = "Utente"; // Specificata per popolare la tabella Utente del database
	int c = 0; // Contatore utilizzato nel metodo modify
	
	static Map<String, String> dictionaryUtente = new HashMap<String, String>(); 
	static ArrayList<Utente> arrayUtente = new ArrayList<Utente>();
	static File fileImport = new File("FileImport.txt");
	static File fileEliminate = new File("fileEliminate.txt");

    public String getBotUsername() 
    {
        return "NabooCGObot";
    }

    public String getBotToken() 
    {
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
    
    public void populateFile(String nickName, String password) // Writer del file per individuare in un prossimo start registrazioni di account gia' avvenute
    {    	
    	try
    	{
	    	FileWriter writerImport = new FileWriter(fileImport, true);
			String str = nickName + " " + password + "\n";

			writerImport.write(str);
	    	writerImport.close();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void populateDictionary(File f) // Allo start del bot telegram inserisce all'interno del dictionary tutti gli account che abbiano gia'    
    { 							     	   // effettuato la registration al bot, dato che ad ogni avvio viene inizializzato il dictionary    
    	dictionaryUtente.clear();
    	
    	try
    	{
        	Scanner scanFile = new Scanner(f);
        	
			while (scanFile.hasNext()) 
			{
				String line = scanFile.nextLine();
				String[] tokens = line.split(" ");
				
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
    
    public void populateArrayList(File f) // Allo start del bot telegram inserisce all'interno dell'arrayList tutti gli account che abbiano gia'
    {								      // effettuato la registration al bot, dato che ad ogni avvio viene inizializzato il dictionary    
    	arrayUtente.clear();
    	
    	try
    	{
        	Scanner scanFile = new Scanner(f);
        	
			while (scanFile.hasNext()) 
			{
				String line = scanFile.nextLine();
				String[] tokens = line.split(" ");
				
				nickName = tokens[0];
				password = tokens[1];
												
				Utente u = new Client(nickName, password);
				arrayUtente.add(u);
			}
						
			scanFile.close();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}     	
    }
    
    public void clearFile(File f) // Usato principalmente per la pulizia del file di appoggio, (fileEliminate) a (fileImport)
    {
    	try
    	{
    		PrintWriter writer = new PrintWriter(f);
        	writer.print("");
        	writer.close();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }

    public void copyFile(File fileInput, File fileOutput) // Copia del fileEliminate nel fileImport, su cui si base il popolamento del dictionaryUtentes
    {
    	try
    	{
    		FileUtils.copyFile(fileInput, fileOutput);
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void function(String str, SendMessage response, Update update) //Switch contenente le funzioni principali del bot telegram, individuandone la correlazione con onUpdateReceived
    {    	
    	char ch = str.charAt(0);    	
    	
    	if(ch == '/')
    	{
    		function = str; // Salvo temporaneamente quale sia la funzione scelta precedentemente, in maniera tale che possa successivamente
    						// utilizzare il corretto update
    		switch(function)
		    {
		    	case "/start":

		            try 
		            {
			            response.setText("Benvenuto nel bot NABOO!");
		                execute(response);
		            } 
		            catch (TelegramApiException e) 
		            {
		                e.printStackTrace();
		            }
		    		
		    		break;
		        
		        case "/registrazione":
		        			        	
		        	try
		        	{
		        		response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
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
		        		response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
			       		execute(response);
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
			        break;	
			        
		        case "/modifica":
		        
		        	try
		        	{
		        		response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
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
			     
		        case "/leggiNotizie":
		    
		        	break;
		        	
		        case "/cercaNotizia":
		        	
		        	break;
		    }
    	}
    	else
    	{
    		switch(function)
		    {   
		        case "/registrazione":
		        	
		        	registration(response, update);
		        	
		        	break;
		        	
		        case "/accedi":
		        	
		        	access(response, update);
		        	
			        break;	
			        	
		        case "/modifica":
		        	
			        if(c == 0) 
			        {		        		
			       		modify(response, update);
			        }
			        else
			        {
			        	c = 0;
			        	function = "/registrazione";
			        	registration(response, update); // Richiamo il metodo registration per rendere possibile la modifica delle proprie credenziali
			       	}
		        
			        break;		
			        
		        case "/elimina":
		        	
		        	delete(response, update);
		        	
			        break;
			        
		        case "/leggiNotizie":
		        	
		        	read(response, update);
		        	
		        	break;
		        	
		        case "/cercaNotizia":
		        	
		        	search(response, update);
		        	
		        	break;
		    }
    	}
    }
    
    public void registration(SendMessage response, Update update)
    {  		    	
		try 
		{
			MyDataBase database = new MyDataBase();
			
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
					reading = true; // Evidenzia la possibilita' che la lettura delle notizie possa avvenire solamente con la propria registration

					dictionaryUtente.put(nickName, password);
					
					Utente u = new Client(nickName, password);
					arrayUtente.add(u);
					
					populateFile(nickName, password); // Aggiungo le nuove credenziali all'interno del file, per popolare al prossimo avvio il dictionary	
					
					database.InsertTable(tabUtente, nickName, password);
					
					response.setText("Nuova registrazione eseguita!");
					execute(response);
				}
				
				System.out.println(dictionaryUtente);
			}
		} 
		catch (TelegramApiException e) 
		{
			e.printStackTrace();
		}
    }
    
    public void access(SendMessage response, Update update) 
    {   		
		try 
		{
			String str = update.getMessage().getText();
			String[] tokens = str.split(" ");

			if (tokens.length != 2) // Condizione specificata per evitare scorretti inserimenti delle credenziale
			{
				response.setText("Attenzione credenziali non corrette riprova!");
				execute(response);
			} 
			else
			{
				nickName = tokens[0];
				password = tokens[1];
												
				if (dictionaryUtente.containsKey(nickName) && dictionaryUtente.containsValue(password)) // Condizione per vericare se sia gia' avvenuta la registration dell'account
				{
					reading = true; // Evidenzia la possibilita' che la lettura delle notizie possa avvenire solamente con il proprio accesso

					response.setText("Accesso eseguito!");
					execute(response);					
				} 
				else
				{
					response.setText("Attenzione credenziali errate!");
					execute(response);
				}
				
				System.out.print(dictionaryUtente);
			}
		}
		catch (TelegramApiException e) 
		{
			e.printStackTrace();
		}
    }
    
    public void modify(SendMessage response, Update update) // Specificata principalmente per permettere la mofica delle proprie credenziali, trovando una correlazione tra
    {														// il metodo delete e il metodo registrazione (specificato per "l'utente stupido")
    	MyDataBase database = new MyDataBase();
    	
    	String lineModify = update.getMessage().getText(); 
    	String[] marks = lineModify.split(" ");
	
    	clearFile(fileEliminate);
    	
    	try
    	{
    		Scanner scanFile = new Scanner(fileImport);
			FileWriter writerImport = new FileWriter(fileEliminate);
    		
			if(marks.length != 2)
    		{
    			response.setText("Attenzione credenziali non corrette riprova!");
    			execute(response);
    		}
			else
			{
	    		if(dictionaryUtente.containsKey(marks[0]) && dictionaryUtente.containsValue(marks[1]))
				{
	    			while(scanFile.hasNext())
	    			{
	    				String line = scanFile.nextLine();
	    				String[] tokens = line.split(" ");
	    			
	    				nickName = tokens[0];
	    				password = tokens[1];
	    				
	    			
	    				if(!marks[0].equals(nickName) && !marks[1].equals(password))
	    				{
	    					String str = nickName + " " + password + "\n";
	    					writerImport.write(str);
	    				}	
	    			}

	    			c++; // Contatore utilizzato per rendere possibile la modify delle proprie credenziali, individuando una correlazione con il metodo registration
	    			
		    		scanFile.close();
					writerImport.close();
					
					copyFile(fileEliminate, fileImport);
					populateDictionary(fileEliminate);
			    	populateArrayList(fileEliminate);
	    			
			    	database.deleteTable(tabUtente, nickName, password);
			    	
			    	response.setText("Inserisci le nuove credenziali");
	    			execute(response);
				}
	    		else 
	    		{
	    			response.setText("Attenzione credenziali errate!");
	    			execute(response);
	    		}		    		
	    	}
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    	catch (TelegramApiException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void delete(SendMessage response, Update update)
    {
    	MyDataBase database = new MyDataBase();

    	String lineRemove = update.getMessage().getText(); 
    	String[] marks = lineRemove.split(" ");
    		
    	clearFile(fileEliminate);
    	
    	try
    	{
    		Scanner scanFile = new Scanner(fileImport);
			FileWriter writerImport = new FileWriter(fileEliminate);
    		
			if(marks.length != 2)
    		{
    			response.setText("Attenzione credenziali non corrette riprova!");
    			execute(response);
    		}
			else
			{
	    		if(dictionaryUtente.containsKey(marks[0]) && dictionaryUtente.containsValue(marks[1]))
				{	    			
	    			while(scanFile.hasNext())
	    			{
	    				String line = scanFile.nextLine();
	    				String[] tokens = line.split(" ");
	    			
	    				nickName = tokens[0];
	    				password = tokens[1];
	    			
	    				if(!marks[0].equals(nickName) && !marks[1].equals(password))
	    				{
	    					String str = nickName + " " + password + "\n";
	    					writerImport.write(str);
	    				}	    				
	    			}
	    			
		    		scanFile.close();
					writerImport.close();
					
					copyFile(fileImport, fileEliminate);
					populateDictionary(fileEliminate);
			    	populateArrayList(fileEliminate);
			    	
			    	database.deleteTable(tabUtente, nickName, password);
			    	
			    	response.setText("Eliminazione eseguita con successo!");
	    			execute(response);
				}
	    		else 
	    		{
	    			response.setText("Attenzione credenziali errate!");
	    			execute(response);
	    		}		    		
	    	}
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    	catch (TelegramApiException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void read(SendMessage response, Update update)
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
    		// ControllerNotizie controller = new ControllerNotizie();
    	}
    }
    
    public void search(SendMessage response, Update update)
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
    		// ControllerNotizie controller = new ControllerNotizie();
    	}
    }
}