package adminInterface.table;

public class CommentoTable {
	private int id;
	private String recensione;
	private int utenteId;
	private int notiziaId;
	
	public CommentoTable(int id, String recensione, int utenteId, int notiziaId) {
		this.id = id;
		this.recensione = recensione;
		this.utenteId = utenteId;
		this.notiziaId = notiziaId;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getRecensione() {
		return this.recensione;
	}
	
	public int getUtenteId() {
		return this.utenteId;
	}
	
	public int getNotiziaId() {
		return this.notiziaId;
	}
}
