package feedRSS;

public class Notizia 
{
	String titolo;
	String link;
	
	
	public Notizia(String t, String l) {
		this.titolo=t;
		this.link=l;
	}	
	
	public String getTitolo(){
		return titolo;
	}
	
	public String getLink(){
		return link;
	}
}
