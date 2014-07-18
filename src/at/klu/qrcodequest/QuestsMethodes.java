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


public class QuestsMethodes {

private static StringBuffer input = new StringBuffer();
	
	private static void getdata() {
	    try {
	        StrictMode.ThreadPolicy policy = new StrictMode.
	          ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy); 
	        URL url = new URL("http://192.168.136.85/quests.html");
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

	public static ArrayList<Quest> getQuestsfromJSONString() throws JSONException{
		
		getdata();
    	
    	ArrayList <Quest> quests = new ArrayList<Quest>();
    	
    	JSONObject obj = new JSONObject(input.toString());
    	
    	JSONArray array = obj.getJSONArray("Quest");
    	
    	for (int i = 0; i < array.length(); i++){
    		
    		JSONObject quest = array.getJSONObject(i);
    		
    		String description = quest.getString("description");
    		String name = quest.getString("name");
    		int questPk = quest.getInt("questPk");
    		int active = quest.getInt("active");
    		int sequence = quest.getInt("sequence");
    		int dtOwner = quest.getInt("dtOwner");
    		int dtRegistration = quest.getInt("dtRegistration");
    		
    		
    		Quest quest1 = new Quest(questPk, active, sequence,dtOwner,dtRegistration,name,description);
    		quests.add(quest1);
	}
    	return quests;
}
}

