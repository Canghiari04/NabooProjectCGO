package application;

import java.io.File;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main 
{
	static File fileImport = new File("fileImport.txt");
	
    public static void main(String[] args) 
    {
        try 
        {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot());  
            
            MyBot bot = new MyBot();
            bot.PopulateArray(fileImport);
        } 
        catch (TelegramApiException e) 
        {
            e.printStackTrace();
        }
    }
}