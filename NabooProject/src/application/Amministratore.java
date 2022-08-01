package application;

import javax.ws.rs.core.Link;

public class Amministratore extends Utente implements IAmministratore
{
	public Amministratore(String nickName, String password, String sub)
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
		return "[Nickname: " + this.nickName + "][Password: " + this.password + "]";
	}
	
	public boolean importInfo(Link link) 
	{
		return false;
	}
}