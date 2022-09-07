package botTelegram;

public class Amministratore extends Utente {
	public Amministratore(String nickname, String password) {
		super(nickname, password);
	}

	public String getNickname() {
		return this.nickname;
	}

	public String getPassword() {
		return this.password;
	}

	public String toString() {
		return "[Nickname: " + this.nickname + "][Password: " + this.password + "]";
	}
}