package feedRSS;

import com.sun.syndication.feed.synd.SyndFeed;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
 
public class FeedReader implements Runnable 
{	
	public void run() 
	{

		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FeedFetcher fetcher = new HttpURLFeedFetcher();
		List<Notizia> notizie = new ArrayList<Notizia>();
		
		try 
		{
			SyndFeed feed = fetcher.retrieveFeed(new URL("https://www.ansa.it/sito/ansait_rss.xml"));
			
			for(Object o: feed.getEntries()) 
			{
				SyndEntry entry = (SyndEntry)o;
				Notizia n = new Notizia(entry.getTitle(),entry.getLink());
				notizie.add(n);
			}
			
			FileWriter fw = new FileWriter("GsonImport.json");
			gson.toJson(notizie, fw);
			fw.flush();	
			fw.close();
		} 
		catch (IllegalArgumentException | IOException | FeedException | FetcherException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}