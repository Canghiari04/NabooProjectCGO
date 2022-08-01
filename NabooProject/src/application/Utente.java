package application;

public abstract class Utente 
{
	protected String nickName;
	protected String password;
	protected String sub;
	
	public Utente(String nickName, String password, String sub)
	{
		this.nickName = nickName;
		this.password = password;
		this.sub = sub;
	}
	
	public abstract String getNickName();
	
	public abstract String getPassword();
	
	public abstract String getSub();
	
	public abstract String toString();
}
