package botTelegram;

public abstract class Utente 
{
	protected String nickname;
	protected String password;
	
	public Utente(String nickname, String password)
	{
		this.nickname = nickname;
		this.password = password;
	}
	
	public abstract String getNickname();
	
	public abstract String getPassword();		
}