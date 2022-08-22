package feedRSS;

import java.awt.HeadlessException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
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
	
	public boolean run(String search, int utenteId) 
	{
		try {
			switch (search) {
				case "cronaca":
					feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/cronaca/cronaca_rss.xml")));
					feedBack = true;
					break;
				case "politica":
					feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/politica/politica_rss.xml")));
					feedBack = true;
					break;
				case "economia":
					feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/economia/economia_rss.xml")));
					feedBack = true;
					break;
				case "cinema":
					feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/cultura/cinema/cinema_rss.xml")));
					feedBack = true;
					break;
				case "tecnologia":
					feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/notizie/tecnologia/tecnologia_rss.xml")));
					feedBack = true;
					break;
				case "cultura":
					feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/notizie/cultura/cultura_rss.xml")));
					feedBack = true;
					break;
				case "sport":
					feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/sport/sport_rss.xml")));
					feedBack = true;
					break;
				case "ultimaOra":
					feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/ansait_rss.xml")));
					feedBack = true;
					break;
				case "personalizzata":
					feeds = dataBase.getFeeds(utenteId, feeds, fetcher);
					feedBack = true;

			}
			 
			for(SyndFeed g : feeds) {
				for(Object o : g.getEntries()) {
					SyndEntry entry = (SyndEntry)o;	

					Notizia n = new Notizia(entry.getTitle(), entry.getLink());
					notizie.add(n);
				}
			}
			
			shuffle(notizie);
			
			FileWriter fw = new FileWriter("GsonImport.json");
			gson.toJson(notizie, fw);
			
			fw.flush();	
			fw.close();
			
		} catch(IllegalArgumentException | IOException | FeedException | FetcherException | HeadlessException e) {
			e.printStackTrace();
		}	
		
		return feedBack;
	}
	
	/*
	 * Metodo shuffle utilizzato per randomizzare le notizie all'interno del file Gson 
	 */
	
	public static<T> void shuffle(List<T> list)
    {
        Random random = new Random();

        for (int i = list.size() - 1; i >= 1; i--)
        {
            int j = random.nextInt(i + 1);

            T obj = list.get(i);
            list.set(i, list.get(j));
            list.set(j, obj);
        }
    }
}