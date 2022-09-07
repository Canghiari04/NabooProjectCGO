package botTelegram;

public class Client extends Utente implements IClient {
	private String sub;

	public Client(String nickname, String password, String sub) {
		super(nickname, password);
		this.sub = sub;
	}

	public String getNickname() {
		return this.nickname;
	}

	public String getPassword() {
		return this.password;
	}

	public String getSub() {
		return this.sub;
	}
}