package botTelegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
		
    public static void main(String[] args) 
    {  
        try {      
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot()); 		
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
         
        launch(args);
    }

	@Override
	 public void start(Stage primaryStage){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setRoot(new AnchorPane());
			
			Parent root = FXMLLoader.load(getClass().getResource("/adminInterface/fxml/Login.fxml"));
			
			Scene scene = new Scene(root);
			
			primaryStage.setTitle("NabooCGN");
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}