package adminInterface;

public class NotiziaTable {
	
	private int id;
	private String titolo;
	private String link;
	
	public NotiziaTable(int id, String titolo, String link) {
		this.id = id;
		this.titolo = titolo;
		this.link = link;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getTitolo() {
		return this.titolo;
	}
	
	public String getLink() {
		return this.link;
	}
}
