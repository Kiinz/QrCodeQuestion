package at.klu.qrcodequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class UserMethods {
	
	private static String input;
	
	public static String UsertoJSon(User person) {
        try {
          // Here we convert Java Object to JSON 
        	
          JSONObject jsonObj = new JSONObject();
          jsonObj.put("firstname", person.getFirstname()); // Set the first name/pair 
          jsonObj.put("lastname", person.getLastname());
          jsonObj.put("nickname", person.getNickname());
          jsonObj.put("id", person.getId());
          jsonObj.put("active", person.getActive());
          jsonObj.put("userId", person.getUserId());

          return jsonObj.toString();

      }
      catch(JSONException ex) {
          ex.printStackTrace();
      }

      return null;
  }
    
    public static User fromJSontoUser() throws JSONException, IOException {

        input = HTTPHelper.makeGetRequest("http://192.168.136.80/test.html");

        JSONObject obj = new JSONObject(input);

    	User user = new User();

    	user.setLastname(obj.getString("lastname"));
    	user.setFirstname(obj.getString("firstname"));
    	user.setActive(Integer.parseInt(obj.getString("active")));
    	user.setId(Integer.parseInt(obj.getString("id")));
    	user.setNickname(obj.getString("nickname"));
    	user.setUserId(obj.getString("userId"));

    	return user;

    }

    public static String fromUserstoJSONArray(ArrayList <User> personen) throws JSONException{
    	JSONObject allUsers = new JSONObject();
    	JSONArray array = new JSONArray();

        for (User aPersonen : personen) {

            JSONObject obj = new JSONObject();
            obj.put("firstname", aPersonen.getFirstname()); // Set the first name/pair
            obj.put("lastname", aPersonen.getLastname());
            obj.put("nickname", aPersonen.getNickname());
            obj.put("id", aPersonen.getId());
            obj.put("active", aPersonen.getActive());
            obj.put("userId", aPersonen.getUserId());

            array.put(obj);
        }
    	allUsers.put("Users", array);

        return allUsers.toString();
}

    public static ArrayList<User> getUsersfromJSONString() throws JSONException, IOException {

    	input = HTTPHelper.makeGetRequest("http://192.168.136.80/test.html");

    	ArrayList <User> users = new ArrayList<User>();

    	JSONObject obj = new JSONObject(input);

    	JSONArray array = obj.getJSONArray("Users");

    	for (int i = 0; i < array.length(); i++){

    		JSONObject user = array.getJSONObject(i);

    		String firstname = user.getString("firstname");
    		String lastname = user.getString("lastname");
    		String nickname = user.getString("nickname");
    		String userId = user.getString("userId");
    		int active = Integer.parseInt(user.getString("active"));
    		int id = Integer.parseInt(user.getString("id"));

    		User user1 = new User(active,firstname,lastname,nickname,userId);
    		users.add(user1);


    	}
    	return users;
    }
}

