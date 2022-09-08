package adminInterface.table;

public class UtenteTable {
	private int id;
	private String nickName;
	private String password;
	private boolean subscription;
	
	public UtenteTable(int id, String nickName, String password, boolean subscription) {
		this.id = id;
		this.nickName = nickName;
		this.password = password;
		this.subscription = subscription;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getNickname() {
		return this.nickName;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public boolean getSubscription() {
		return this.subscription;
	}
}