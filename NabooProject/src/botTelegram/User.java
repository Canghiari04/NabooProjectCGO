package botTelegram;

public class User extends Utente {
	private String sub;

	public User(String nickname, String password, String sub) {
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
