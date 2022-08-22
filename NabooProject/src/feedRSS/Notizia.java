package feedRSS;

public class Notizia {
	private String titolo;
	private String link;
	
	public Notizia(String titolo, String link) {
		this.titolo = titolo;
		this.link = link;
	}

	public String getTitolo() {
		return titolo;
	}

	public String getLink() {
		return link;
	}
}