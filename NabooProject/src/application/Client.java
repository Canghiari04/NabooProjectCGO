package application;

public class Client extends Utente implements IClient
{
	public Client(String nickName, String password, String sub)
	{
		super(nickName, password, sub);
	}
	
	public String getNickName()
	{
		return this.nickName;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public String getSub()
	{
		return this.sub;
	}
	
	public String toString()
	{
		return "[Nickname: " + this.nickName + "][Password: " + this.password + "][Sub: " + this.sub + "]";
	}
}