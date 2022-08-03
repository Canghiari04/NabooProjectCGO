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
	boolean answer = false, access = false;
	String nickName = " ", password = " ", sub = " ", function = " ";
	String tabUtente = "Utente"; // Specificata per popolare la tabella Utente del database
	int c = 0; // Contatore utilizzato nel metodo modify
	
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
	        	        
	        Function(str, response, update);
	    }    	
    }
    
    /*
	* Metodo PopulateFile che rende la possibilita' di specificare utenti che si siano gia' registrati all'avvio.
	* 
	* Prende in considerazione l'utilizzo del file in cui viene salvata ogni modifica.
	*/
    
    public void PopulateFile(String nickName, String password, String sub) 
    {    	
    	try
    	{
	    	FileWriter writerImport = new FileWriter(fileImport, true);
			String str = nickName + " " + password + " " + sub + "\n";

			writerImport.write(str);
	    	writerImport.close();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    /*
	* Metodo PopulateArray inserisce all'interno del'arrayList tutti gli account che abbiano gia' effettuato una registrazione al bot Naboo.
	* 
	* Questo dovuto poiche' ad ogni avvio la struttura viene inizializzata.
	*/
    
    public void PopulateArray(File f)     
    { 							         
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
				sub = tokens[2];
				
				Utente u = new Client(nickName, password, sub);
												
				arrayUtente.add(u);				
			}
						
			scanFile.close();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}         
    	
    	System.out.println(arrayUtente);
    }
    
    /*
   	* 
   	* Metodo ClearFile che predispone l'inizializzazione del fileEliminate su cui vengono temporaneamente salvati account che non subiscono alcuna modifica
   	* 
   	*/
    
    public void ClearFile(File f)
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

    /*
   	* 
   	* Metodo CopyFile che permette la copia del fileEliminate nei confronti del fileImport, utilizzato all'avvio del bot.
   	* 
   	*/
    
    public void CopyFile(File fileInput, File fileOutput) 
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
    
    /*
   	* 
   	* Metodo getIn il quale prevede la medesima funzionalita' del metodo contains(), a discapito del fatto che quest ultimo non puo' essere usato per oggetti.
   	* 
   	*/
    
    public boolean getIn(String nickName, String password) 
    {
    	for(Utente c : arrayUtente)
    	{
    		if(c.getNickName().equals(nickName) && c.getPassword().equals(password))
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /*
   	* 
   	* Switch contenente le funzioni principali del bot telegram, individuandone la correlazione con onUpdateReceived
   	* 
   	*/
    
    public void Function(String str, SendMessage response, Update update)
    {    	
    	char ch = str.charAt(0);    	
    	
    	if(ch == '/')
    	{
    		function = str; /* Salvo temporaneamente quale sia la funzione scelta precedentemente, in maniera tale che possa successivamente utilizzare il corretto update */
    						
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
		        		if(access != true)
		        		{
		        			response.setText("Attenzione devi prima eseguire l'accesso al bot NABOO!");
				       		execute(response);
		        		}
		        		else
		        		{
		        			response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
				       		execute(response);	
		        		}
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
		        	break;
		        	
		        case "/elimina":
		        	
		        	try
		        	{
		        		if(access != true)
		        		{
		        			response.setText("Attenzione devi prima eseguire l'accesso al bot NABOO!");
				       		execute(response);
		        		}
		        		else
		        		{
		        			response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
				       		execute(response);	
		        		}
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
			        break;	
			     
		        case "/leggiNotizie":
		    
		        	try
		        	{
		        		if(access != true)
		        		{
		        			response.setText("Attenzione devi prima eseguire l'accesso al bot NABOO!");
				       		execute(response);
		        		}
		        		else
		        		{
		        			response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
				       		execute(response);	
		        		}
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
		        	break;
		        	
		        case "/cercaNotizia":
		        	
		        	try
		        	{
		        		if(access != true)
		        		{
		        			response.setText("Attenzione devi prima eseguire l'accesso al bot NABOO!");
				       		execute(response);
		        		}
		        		else
		        		{
		        			response.setText("Inserisci il tuo nickname e la password, separati da uno spazio");
				       		execute(response);	
		        		}
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
		        	break;
		    }
    	}
    	else
    	{
    		switch(function)
		    {   
		        case "/registrazione":
		        	
		        	Registration(response, update);
		        	
		        	break;
		        	
		        case "/accedi":
		        	
		        	Access(response, update);
		        	
			        break;	
			        	
		        case "/modifica":
		        	
		        	try
		        	{
		        		if(access != true)
		        		{
		        			response.setText("Attenzione devi prima eseguire l'accesso al bot NABOO!");
				       		execute(response);
		        		}
		        		else
		        		{
		        			if(c == 0) 
					        {		        		
					       		Modify(response, update);
					        }
					        else
					        {
					        	c = 0;
					        	function = "/registrazione";
					        	Registration(response, update); // Richiamo il metodo registration per rendere possibile la modifica delle proprie credenziali
					       	}
		        		}
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
			        
			        break;		
			        
		        case "/elimina":
		        	
		        	try
		        	{
		        		if(access != true)
		        		{
		        			response.setText("Attenzione devi prima eseguire l'accesso al bot NABOO!");
				       		execute(response);
		        		}
		        		else
		        		{
				        	Delete(response, update);
		        		}
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
			        break;
			        
		        case "/leggiNotizie":
		        	
		        	try
		        	{
		        		if(access != true)
		        		{
		        			response.setText("Attenzione devi prima eseguire l'accesso al bot NABOO!");
				       		execute(response);
		        		}
		        		else
		        		{
				        	Read(response, update);
		        		}
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
		        	break;
		        	
		        case "/cercaNotizia":
		        	
		        	try
		        	{
		        		if(access != true)
		        		{
		        			response.setText("Attenzione devi prima eseguire l'accesso al bot NABOO!");
				       		execute(response);
		        		}
		        		else
		        		{
				        	Search(response, update);
		        		}
		        	}
		        	catch (TelegramApiException e)
		        	{
		        		e.printStackTrace();
		        	}
		        	
		        	break;
		    }
    	}
    }
    
    public void Registration(SendMessage response, Update update)
    {  		    	
		try 
		{
			MyDataBase database = new MyDataBase();
			
			String str = update.getMessage().getText();
			String[] tokens = str.split(" ");
			
			if (tokens.length != 3) // TODO: controllo su sub
			{
				response.setText("Attenzione credenziali non corrette riprova!");
				execute(response);
			}
			else
			{
				nickName = tokens[0];
				password = tokens[1];
				sub = tokens[2];
				
				Utente u = new Client(nickName, password, sub);
				
				answer = getIn(nickName, password);
												
				if(answer) /* In caso dovesse essere presente un account con le stesse credenziali verra' richiesto nuovamente l'inserimento */
				{		 
					response.setText("Attenzione credenziali gia' presenti!");
					execute(response);
				} 
				else
				{
					access = true; /* Evidenzia la possibilita' che la lettura delle notizie possa avvenire solamente con la propria registration */

					arrayUtente.add(u);
					PopulateFile(nickName, password, sub); /* Aggiungo le nuove credenziali all'interno del file, per popolare al prossimo avvio il dictionary */
					
					database.InsertTable(tabUtente, nickName, password, sub, null);
					
					response.setText("Registrazione eseguita!");
					execute(response);
				}
				
				System.out.println(arrayUtente);
			}
		} 
		catch (TelegramApiException e) 
		{
			e.printStackTrace();
		}
    }
    
    public void Access(SendMessage response, Update update) 
    {   	
		try 
		{
			String str = update.getMessage().getText();
			String[] tokens = str.split(" ");
			
			if (tokens.length != 2) /* Condizione specificata per evitare scorretti inserimenti delle credenziale */
			{
				response.setText("Attenzione credenziali non corrette riprova!");
				execute(response);
			} 
			else
			{
				nickName = tokens[0];
				password = tokens[1];
					
				answer = getIn(nickName, password);
				System.out.println(answer);
		
				if(answer) /* Condizione per vericare se sia gia' avvenuta la registration dell'account */
				{
					access = true; /* Evidenzia la possibilita' che la lettura delle notizie possa avvenire solamente con il proprio accesso */

					response.setText("Accesso eseguito!");
					execute(response);					
				} 
				else
				{
					response.setText("Attenzione credenziali errate!");
					execute(response);
				}	
			}
		}
		catch (TelegramApiException e) 
		{
			e.printStackTrace();
		}
    }

    /*
   	* Metodo Modify specificato principalmente per permettere la mofica delle proprie credenziali.
   	* 
   	* Individuando una correlazione tra il metodo delete e il metodo registrazione (specificato per "l'utente stupido").
   	*/
    
    public void Modify(SendMessage response, Update update) 
    {														
    	MyDataBase database = new MyDataBase();
    	
    	String lineModify = update.getMessage().getText(); 
    	String[] marks = lineModify.split(" ");
	
    	ClearFile(fileEliminate);
    	
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
				answer = getIn(marks[0], marks[1]);
				
		    	if(answer)
				{
		    		while(scanFile.hasNext())
		    		{
		    			String line = scanFile.nextLine();
		    			String[] tokens = line.split(" ");
		    		
		    			nickName = tokens[0];
		    			password = tokens[1];
		    			sub = tokens[2];
		    				    			
		    			if(marks[0].equals(nickName) && marks[1].equals(password))
		    			{
		    					
		    			    database.DeleteTable(tabUtente, nickName, password);
		    			}	
		    			else
		    			{
		    				String str = nickName + " " + password + " " + sub + "\n";
		    				writerImport.write(str);
		    			}	
		    		}
	
		    		c++; /* Contatore utilizzato per rendere possibile la modify delle proprie credenziali, individuando una correlazione con il metodo registration */
		    			
			    	scanFile.close();
					writerImport.close();
						
					CopyFile(fileEliminate, fileImport);
					PopulateArray(fileEliminate);
		    						    	
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
    
    public void Delete(SendMessage response, Update update)
    {
    	MyDataBase database = new MyDataBase();

    	String lineRemove = update.getMessage().getText(); 
    	String[] marks = lineRemove.split(" ");
    		    	
    	ClearFile(fileEliminate);
    	
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
				answer = getIn(marks[0], marks[1]);
				
		    	if(answer)
				{
		    		while(scanFile.hasNext())
		    		{
		    			String line = scanFile.nextLine();
		    			String[] tokens = line.split(" ");
		    		
		    			nickName = tokens[0];
		    			password = tokens[1];
		    			sub = tokens[2];
		    					    			
		    			if(marks[0].equals(nickName) && marks[1].equals(password))
		    			{			
		    		    	database.DeleteTable(tabUtente, nickName, password);
		    			}	
		    			else
		    			{
		    				String str = nickName + " " + password + " " + sub + "\n";
		    				writerImport.write(str);
		    			}
		    		}
		    			
			    	scanFile.close();
					writerImport.close();
						
					CopyFile(fileEliminate, fileImport);
					PopulateArray(fileEliminate);
		    								
					response.setText("Eliminazione eseguita!");
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
    
    public void Read(SendMessage response, Update update)
    {
    	if(access != true)
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
    		
    	}
    }
    
    public void Search(SendMessage response, Update update)
    {
    	if(access != true)
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
    		
    	}
    }
}