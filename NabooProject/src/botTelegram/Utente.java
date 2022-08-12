package botTelegram;

public abstract class Utente 
{
	protected String nickName;
	protected String password;
	
	public Utente(String nickName, String password)
	{
		this.nickName = nickName;
		this.password = password;
	}
	
	public abstract String getNickName();
	
	public abstract String getPassword();
		
	public abstract String toString();
}