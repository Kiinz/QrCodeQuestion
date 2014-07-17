package at.klu.qrcodequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;

public class UserMethodes {
	
	private static StringBuffer input = new StringBuffer();
	
	private static void getdata() {
	    try {
	        StrictMode.ThreadPolicy policy = new StrictMode.
	          ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy); 
	        URL url = new URL("http://192.168.136.80/test.html");
	        HttpURLConnection con = (HttpURLConnection) url
	          .openConnection();
	        readStream(con.getInputStream());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}     

	private static void readStream(InputStream in) {
	  BufferedReader reader = null;
	  
	  try {
	    reader = new BufferedReader(new InputStreamReader(in));
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	      System.out.println(line);
	      input.append(line);
	    }
	  } catch (IOException e) {
	    e.printStackTrace();
	  } finally {
	    if (reader != null) {
	      try {
	        reader.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	  }
	}
	
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
    
    public static User fromJSontoUser() throws JSONException{
    	
    	JSONObject obj = new JSONObject(input.toString());
    	
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

    public static ArrayList<User> getUsersfromJSONString() throws JSONException{
    	
    	getdata();
    	
    	ArrayList <User> users = new ArrayList<User>();
    	
    	JSONObject obj = new JSONObject(input.toString());
    	
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

