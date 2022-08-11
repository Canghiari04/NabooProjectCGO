package botTelegram;

import java.io.File;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import dataBase.MyDataBase;

public class Main 
{
	static File fileImport = new File("fileImport.txt");
	
    public static void main(String[] args) 
    {
        try 
        {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot());  
            MyDataBase db = new MyDataBase();
            System.out.println(db.getNotizia(1));
            
        } 
        catch (TelegramApiException e) 
        {
            e.printStackTrace();
        }
    }
}