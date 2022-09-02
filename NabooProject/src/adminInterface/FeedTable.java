package adminInterface;

public class  FeedTable{
	private int id;
	private String tipo;
	private String link;
	
	public FeedTable(int id, String tipo, String link) {
		this.id = id;
		this.tipo = tipo;
		this.link = link;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public String getLink() {
		return this.link;
	}
}
