package feedRSS;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;
 
public class FeedReader  
{	
	public void run(String search) 
	{
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FeedFetcher fetcher = new HttpURLFeedFetcher();
		List<Notizia> notizie = new ArrayList<Notizia>();
		List<SyndFeed> feeds= new ArrayList<SyndFeed>();
		try 
		{
			switch (search) {
			case "tutto":
				feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/ansait_rss.xml")));
				break;
			case "cronaca":
				feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/cronaca/cronaca_rss.xml")));
				break;
			case "politica":
				feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/politica/politica_rss.xml")));
				break;
			case "mondo":
				feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/mondo/mondo_rss.xml")));
				break;
			case "calcio":
				feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/sport/calcio/calcio_rss.xml")));
				break;
			case "cinema":
				feeds.add(fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/notizie/cultura/cinema/cinema_rss.xml")));
				break;

			}
			 
			
			for(SyndFeed g : feeds)
			{
				for(Object o : g.getEntries()) 
				{
					SyndEntry entry = (SyndEntry)o;	

					Notizia n = new Notizia(entry.getTitle(), entry.getLink());
					notizie.add(n);
				}
			}
			FileWriter fw = new FileWriter("GsonImport.json");
			gson.toJson(notizie, fw);
			fw.flush();	
			fw.close();
		} 
		catch(IllegalArgumentException | IOException | FeedException | FetcherException e) 
		{
			e.printStackTrace();
		}
	}
}