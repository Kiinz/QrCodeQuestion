package at.klu.qrcodequest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Aufrufen {

	public static String toJSon(User person) {
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
    
    public static User fromJSontoUser(String json) throws JSONException{
    	
    	JSONObject obj = new JSONObject(json);
    	
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
    	JSONObject allusers = new JSONObject();
    	JSONArray array = new JSONArray();
    	
    	for (int i = 0; i < personen.size(); i++){
    		
    		JSONObject obj = new JSONObject();
            obj.put("firstname", personen.get(i).getFirstname()); // Set the first name/pair 
            obj.put("lastname", personen.get(i).getLastname());
            obj.put("nickname", personen.get(i).getNickname());
            obj.put("id", personen.get(i).getId());
            obj.put("active", personen.get(i).getActive());
            obj.put("userId",personen.get(i).getUserId());
            
            array.put(obj);
    	}
    	allusers.put("Users", array);
    	
    	String jsonString = allusers.toString();
    	return jsonString;
}

    public static ArrayList<User> getUsersfromJSONString(String json) throws JSONException{
    	
    	ArrayList <User> users = new ArrayList<User>();
    	
    	JSONObject obj = new JSONObject(json);
    	
    	JSONArray array = obj.getJSONArray("Users");
    	
    	for (int i = 0; i < array.length(); i++){
    		
    		JSONObject user = array.getJSONObject(i);
    		
    		String firstname = user.getString("firstname");
    		String lastname = user.getString("lastname");
    		String nickname = user.getString("nickname");
    		String userId = user.getString("userId");
    		int active = Integer.parseInt(user.getString("active"));
    		int id = Integer.parseInt(user.getString("id"));
    		
    		User user1 = new User(id, active,firstname,lastname,nickname,userId);
    		users.add(user1);
    		
    		
    	}
    	return users;
    }
}

