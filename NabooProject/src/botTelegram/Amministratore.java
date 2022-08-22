package botTelegram;

public class Amministratore extends Utente implements IAmministratore {
	public Amministratore(String nickName, String password) {
		super(nickName, password);
	}

	public String getNickName() {
		return this.nickName;
	}

	public String getPassword() {
		return this.password;
	}

	public String toString() {
		return "[Nickname: " + this.nickName + "][Password: " + this.password + "]";
	}
}