package feedRSS;

import java.awt.HeadlessException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;

import dataBase.MyDataBase;
 
public class FeedReader  
{	
	private boolean feedBack = false;
		
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private MyDataBase dataBase = new MyDataBase();
	private FeedFetcher fetcher = new HttpURLFeedFetcher();
	
	private List<Notizia> news = new ArrayList<Notizia>();
	private List<SyndFeed> feeds= new ArrayList<SyndFeed>();
	
	/*
	 * Metodo run che permette il riempimento del file .json tramite l'utilizzo del feed RSS, delle notizie specificate dall'utente in base
	 * alla propria preferenza, qualora il campo prescelto non sia contenuto nella tabella Feed, verra' ricercato attraverso tale parametro 
	 * all'interno di tutti i titoli, appartenenti a notizie di un feed RSS dato di default nel caso in cui si verificasse tale condizione.
	 */
	public void run(String search, int userId) throws SQLException, IllegalArgumentException, IOException, FeedException, FetcherException, HeadlessException {
		clearAll();		
		feedBack = false;
		
		switch (search) {	
			case "personalizzata":
				feeds = dataBase.getFeeds(userId, feeds, fetcher);
				feedBack = true;
				break;
	
			default:
				if(dataBase.contains("Feed", search, null)) {
					feeds.add(fetcher.retrieveFeed(new URL(dataBase.getLink(search))));
					feedBack = true;						
				}
				else
				{
					feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/ansait_rss.xml")));
				}
				break;
		}

		for(SyndFeed g : feeds) {
			for(Object o : g.getEntries()) {
				SyndEntry entry = (SyndEntry)o;	

				Notizia n = new Notizia(entry.getTitle(), entry.getLink());

				if(feedBack == false) {
					String title = n.getTitolo().toLowerCase();

					if(title.contains(search))
					{
						news.add(n);
					}
				}
				else {
					news.add(n);
				}
			}
		}

		shuffle(news);

		PrintWriter writer = new PrintWriter(new FileWriter("GsonImport.json"), true);
		gson.toJson(news, writer);

		writer.close();	
	}
	
	/*
	 * Metodo shuffle utilizzato per randomizzare le notizie all'interno del file .json. 
	 */
	public void shuffle(List<Notizia> news) {
        Random random = new Random();

        for (int i = news.size() - 1; i >= 1; i--)
        {
            int j = random.nextInt(i + 1);

            Notizia obj = news.get(i);
            news.set(i, news.get(j));
            news.set(j, obj);
        }
    }	
	
	/*
	 * Metodo clearAll che elimina ogni elemento contenuto negli ArrayList, prima di effettuare 
	 * l'aggiunta delle differenti notizie.
	 */
	public void clearAll() {
		feeds.clear();
		news.clear();
	}
}