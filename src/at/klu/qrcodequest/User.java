package at.klu.qrcodequest;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	
	private int id;
	private int dtOwner = 1;
	private String firstname, lastname, nickname, userId;
	private boolean active = true;
	
	public User (){

	}
	
	public User(int id, String firstname, String lastname,
			String nickname, String userId) {
		super();
        this.id = id;
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
	public String getFirstname() {
		return firstname;
	}
	public String getNickname() {
		return nickname;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstname="
				+ firstname + ", lastname=" + lastname + ", nickname="
				+ nickname + ", userId=" + userId + "]";
	}
	
	public String getJSONString() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("active", true);
			jsonObject.put("dtOwner", 1);
			jsonObject.put("firstname", this.firstname);
			jsonObject.put("lastname", this.lastname);
			jsonObject.put("nickname", this.nickname);
			jsonObject.put("userId", this.userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

}
