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
		
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private final MyDataBase dataBase = new MyDataBase();
	private static FeedFetcher fetcher = new HttpURLFeedFetcher();
	private static List<Notizia> notizie = new ArrayList<Notizia>();
	private static List<SyndFeed> feeds= new ArrayList<SyndFeed>();
	
	public void run(String search, int userId) throws SQLException, IllegalArgumentException, IOException, FeedException, FetcherException, HeadlessException {
		clearAll();		
		feedBack = false;
		
		switch (search) {	
			case "ultimaOra":
				feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/ansait_rss.xml")));
				feedBack = true;
				break;
	
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
						notizie.add(n);
					}
				}
				else {
					notizie.add(n);
				}
			}
		}

		shuffle(notizie);

		PrintWriter writer = new PrintWriter(new FileWriter("GsonImport.json"), true);
		gson.toJson(notizie, writer);

		writer.close();	
	}
	
	/*
	 * Metodo shuffle utilizzato per randomizzare le notizie all'interno del file Gson 
	 */
	public static<T> void shuffle(List<T> list) {
        Random random = new Random();

        for (int i = list.size() - 1; i >= 1; i--)
        {
            int j = random.nextInt(i + 1);

            T obj = list.get(i);
            list.set(i, list.get(j));
            list.set(j, obj);
        }
    }	
	
	public void clearAll() {
		feeds.clear();
		notizie.clear();
	}
}