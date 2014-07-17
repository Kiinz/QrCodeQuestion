package at.klu.qrcodequest;

public class User {
	
	private int id;
	private int active;
	private String firstname;
	private String lastname;
	private String nickname;
	private String userId;
	
	public User (){
		
	}
	
	public User(int id, int active, String firstname, String lastname,
			String nickname, String userId) {
		super();
		this.id = id;
		this.active = active;
		this.firstname = firstname;
		this.lastname = lastname;
		this.nickname = nickname;
		this.userId = userId;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", active=" + active + ", firstname="
				+ firstname + ", lastname=" + lastname + ", nickname="
				+ nickname + ", userId=" + userId + "]";
	}
	
	
	

}
