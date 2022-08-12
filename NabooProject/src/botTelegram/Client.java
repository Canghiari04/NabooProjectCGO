package botTelegram;

public class Client extends Utente implements IClient {
	private String sub;

	public Client(String nickName, String password, String sub) {
		super(nickName, password);
		this.sub = sub;
	}

	public String getNickName() {
		return this.nickName;
	}

	public String getPassword() {
		return this.password;
	}

	public String getSub() {
		return this.sub;
	}

	public String toString() {
		return "[Nickname: " + this.nickName + "][Password: " + this.password + "][Sub: " + this.sub + "]";
	}
}